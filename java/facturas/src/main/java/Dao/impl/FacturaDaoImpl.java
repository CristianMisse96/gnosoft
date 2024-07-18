/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao.impl;

import Config.Conexion;
import Dao.FacturaDAO;
import Exception.FacturaException;
import Vo.FacturaVO;
import Vo.ItemFacturaVO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author cristian
 */
public class FacturaDaoImpl implements FacturaDAO {

    private final Conexion cn;
    private static final String SQL_EXISE_FACTURA = "SELECT COUNT(*) FROM fact WHERE fact_id = ?";
    
    public FacturaDaoImpl(){
        this.cn = new Conexion();
    }
    
    @Override
    public FacturaVO saveFactura(FacturaVO facturaVO) throws SQLException {

        String sqlFactura = "INSERT INTO fact (fact_numerofactura, fact_nombrecliente, fact_fecha, fact_subtotal, fact_iva, fact_total) "
                + "VALUES (nextval('seq_fact_numerofactura'), ?, ?, ?, ?, ?)";

        String sqlItem = "INSERT INTO items (item_articulo, item_cantidad, item_valor, item_total, item_facturaid) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = cn.getConnection();
                PreparedStatement psFactura = con.prepareStatement(sqlFactura, Statement.RETURN_GENERATED_KEYS);
                PreparedStatement psItem = con.prepareStatement(sqlItem)) {

            con.setAutoCommit(false);
            //Insertar factura
            psFactura.setString(1, facturaVO.getNombreCliente());
            psFactura.setDate(2, new java.sql.Date(facturaVO.getFecha().getTime()));
            psFactura.setDouble(3, facturaVO.getSubtotal());
            psFactura.setDouble(4, facturaVO.getIva());
            psFactura.setDouble(5, facturaVO.getTotal());

            psFactura.executeUpdate();

            try (ResultSet generatedKeys = psFactura.getGeneratedKeys()) {

                if (generatedKeys.next()) {
                    facturaVO.setId(generatedKeys.getLong(1));
                    facturaVO.setNumeroFactura(generatedKeys.getLong(2));
                }
            }

            // Insertar Items
            for (ItemFacturaVO item : facturaVO.getItems()) {
                psItem.setString(1, item.getArticulo());
                psItem.setInt(2, item.getCantidad());
                psItem.setDouble(3, item.getValorItem());
                psItem.setDouble(4, item.getTotalItem());
                psItem.setLong(5, facturaVO.getId());
                psItem.executeUpdate();
            }

            con.commit();

            return facturaVO;
        } catch (Exception e) {
            // Si ocurre un error, deshacer la transacción
            try (Connection con = cn.getConnection()) {
                if (con != null) {
                con.rollback();
                }
            } catch (SQLException ex) {
                System.out.println(String.format("Ocurrio un error ejecutando query insert-> %s", ex.getMessage()));
            }
            throw e;
        }finally {
            if (cn != null) {
                cn.closeConnection();// Cerrar la conexión al finalizar
            }
        }
    }

    @Override
    public FacturaVO updateFactura(FacturaVO nuevaFacturaVO) throws FacturaException,SQLException {
        
        String sqlUpdateFactura = "UPDATE fact SET fact_nombrecliente = ?, fact_subtotal = ?, fact_iva = ?, fact_total = ? WHERE fact_id = ?";
        String sqlInsertItem = "INSERT INTO items (item_articulo, item_cantidad, item_valor, item_total, item_facturaid) VALUES (?, ?, ?, ?, ?)";
        String sqlUpdateItem = "UPDATE items SET item_articulo = ?, item_cantidad = ?, item_valor = ?, item_total = ? WHERE item_id = ?";
        String sqlDeleteItem = "DELETE FROM items WHERE item_id = ?";
        
        
        try (Connection con = cn.getConnection();
                PreparedStatement psUpdateFactura = con.prepareStatement(sqlUpdateFactura);
                PreparedStatement psInsertItem = con.prepareStatement(sqlInsertItem);
                PreparedStatement psUpdateItem = con.prepareStatement(sqlUpdateItem);
                PreparedStatement psDeleteItem = con.prepareStatement(sqlDeleteItem);
                PreparedStatement psExisteFactura= con.prepareStatement(SQL_EXISE_FACTURA)) {
            
            con.setAutoCommit(false);
            
            psExisteFactura.setLong(1, nuevaFacturaVO.getId());
            try (ResultSet rsCountFactura = psExisteFactura.executeQuery();) {

                if (!rsCountFactura.next() || rsCountFactura.getInt(1) == 0) {
                    throw new FacturaException("La factura no existe o ha sido eliminada");
                }
            }
            
            // Actualizar la factura
            psUpdateFactura.setString(1, nuevaFacturaVO.getNombreCliente());
            psUpdateFactura.setDouble(2, nuevaFacturaVO.getSubtotal());
            psUpdateFactura.setDouble(3, nuevaFacturaVO.getIva());
            psUpdateFactura.setDouble(4, nuevaFacturaVO.getTotal());
            psUpdateFactura.setLong(5, nuevaFacturaVO.getId());
            psUpdateFactura.executeUpdate();
            
             // Obtener ítems actuales de la base de datos
            List<Long> itemsGuardados = getCurrentItemIds(nuevaFacturaVO.getId(), con);
            
             // Actualizar o insertar ítems
            for (ItemFacturaVO item : nuevaFacturaVO.getItems()) {
                if (item.getId() == null || !itemsGuardados.contains(item.getId())) {
                    // Insertar nuevo ítem
                    psInsertItem.setString(1, item.getArticulo());
                    psInsertItem.setInt(2, item.getCantidad());
                    psInsertItem.setDouble(3, item.getValorItem());
                    psInsertItem.setDouble(4, item.getTotalItem());
                    psInsertItem.setLong(5, nuevaFacturaVO.getId());
                    psInsertItem.executeUpdate();
                } else {
                    // Actualizar ítem existente
                    psUpdateItem.setString(1, item.getArticulo());
                    psUpdateItem.setInt(2, item.getCantidad());
                    psUpdateItem.setDouble(3, item.getValorItem());
                    psUpdateItem.setDouble(4, item.getTotalItem());
                    psUpdateItem.setLong(5, item.getId());
                    psUpdateItem.executeUpdate();
                    itemsGuardados.remove(item.getId());
                }
            }
            
            // Eliminar ítems que ya no están en la lista
            for (Long itemId : itemsGuardados) {
                psDeleteItem.setLong(1, itemId);
                psDeleteItem.executeUpdate();
            }

            con.commit();
            
            return nuevaFacturaVO;
        
        }catch (Exception e) {
            // Si ocurre un error, deshacer la transacción
            try (Connection con = cn.getConnection()) {
                if (con != null) {
                con.rollback();
                }
            } catch (SQLException ex) {
                System.out.println(String.format("Ocurrio un error ejecutando query actualizar-> %s", ex.getMessage()));
            }
            
            throw e;
        }finally {
            if (cn != null) {
                cn.closeConnection();// Cerrar la conexión al finalizar
            }
        }
    }
    
     private List<Long> getCurrentItemIds(Long facturaId, Connection con) throws SQLException {
        String sqlGetItems = "SELECT item_id FROM items WHERE item_facturaid = ?";
        List<Long> itemIds = new ArrayList<>();
        try (PreparedStatement psGetItems = con.prepareStatement(sqlGetItems)) {
            psGetItems.setLong(1, facturaId);
            try (ResultSet rs = psGetItems.executeQuery()) {
                while (rs.next()) {
                    itemIds.add(rs.getLong("item_id"));
                }
            }
        }
        return itemIds;
    }

    @Override
    public void deleteFactura(Long idFactura) throws FacturaException, SQLException {
        
        String sqlDeleteItems = "DELETE FROM items WHERE item_facturaid = ?";
        String sqlDeleteFactura = "DELETE FROM fact WHERE fact_id = ?";
        
        try (Connection con = cn.getConnection();
                PreparedStatement psDeleteItems  = con.prepareStatement(sqlDeleteItems);
                PreparedStatement psDeleteFactura = con.prepareStatement(sqlDeleteFactura);
                PreparedStatement psExisteFactura= con.prepareStatement(SQL_EXISE_FACTURA)) {
            
            con.setAutoCommit(false);
            
            psExisteFactura.setLong(1, idFactura);
            try (ResultSet rsCountFactura = psExisteFactura.executeQuery();) {

                if (!rsCountFactura.next() || rsCountFactura.getInt(1) == 0) {
                    throw new FacturaException("La factura no existe o ha sido eliminada");
                }
            }
            
        // Eliminar los items
        psDeleteItems.setLong(1, idFactura);
        int itemsDeleted = psDeleteItems.executeUpdate();
        if (itemsDeleted == 0) {
            throw new FacturaException("No se encontraron items para la factura especificada. Puede haber sido eliminada por otro usuario.");
        }

        // Eliminar la factura
        psDeleteFactura.setLong(1, idFactura);
        int facturaDeleted = psDeleteFactura.executeUpdate();

        if (facturaDeleted == 0) {
            throw new FacturaException("No se pudo eliminar la factura. Puede haber sido eliminada por otro usuario.");
        }
        con.commit();
            
        
        }catch (Exception e) {
            // Si ocurre un error, deshacer la transacción
            try (Connection con = cn.getConnection()) {
                if (con != null) {
                con.rollback();
                }
            } catch (SQLException ex) {
                System.out.println(String.format("Ocurrio un error ejecutando query delete-> %s", ex.getMessage()));
            }
            
            throw e;
        }finally {
            if (cn != null) {
                cn.closeConnection();// Cerrar la conexión al finalizar
            }
        }
     
        
    }

    @Override
    public FacturaVO getFacturaById(Long facturaId) throws FacturaException, SQLException {
        
        FacturaVO resp= new FacturaVO();
        
        String sqlGetFactura = "SELECT * FROM fact WHERE fact_id = ?";
        String sqlGetItems = "SELECT  * FROM items WHERE item_facturaid = ?";
        
         try (Connection con = cn.getConnection();
                PreparedStatement psGetFactura  = con.prepareStatement(sqlGetFactura);
                PreparedStatement psGetItems = con.prepareStatement(sqlGetItems)) {
            
        // Obtener la factura por ID
        psGetFactura.setLong(1, facturaId);
        try (ResultSet rsGetFactura = psGetFactura.executeQuery()) {
            if (rsGetFactura.next()) {
                resp.setId(rsGetFactura.getLong("fact_id"));
                resp.setNumeroFactura(rsGetFactura.getLong("fact_numerofactura"));
                resp.setNombreCliente(rsGetFactura.getString("fact_nombrecliente"));
                resp.setFecha(rsGetFactura.getDate("fact_fecha"));
                resp.setSubtotal(rsGetFactura.getDouble("fact_subtotal"));
                resp.setIva(rsGetFactura.getDouble("fact_iva"));
                resp.setTotal(rsGetFactura.getDouble("fact_total"));
            } else {
                throw new FacturaException("La factura no existe o ha sido eliminada");
            }
        }
        
        // Obtener los items asociados a la factura
        psGetItems.setLong(1, facturaId);
        try (ResultSet rsGetItems = psGetItems.executeQuery()) {
                while (rsGetItems.next()) {
                    ItemFacturaVO itemFacturaVO = new ItemFacturaVO();
                    itemFacturaVO.setId(rsGetItems.getLong("item_id"));
                    itemFacturaVO.setArticulo(rsGetItems.getString("item_articulo"));
                    itemFacturaVO.setCantidad(rsGetItems.getInt("item_cantidad"));
                    itemFacturaVO.setValorItem(rsGetItems.getDouble("item_valor"));
                    itemFacturaVO.setTotalItem(rsGetItems.getDouble("item_total"));
                    
                    resp.getItems().add(itemFacturaVO);
                }
            }
        
        }catch (Exception e) {
            // Si ocurre un error, deshacer la transacción
            try (Connection con = cn.getConnection()) {
                if (con != null) {
                con.rollback();
                }
            } catch (SQLException ex) {
                System.out.println(String.format("Ocurrio un error ejecutando query findById-> %s", ex.getMessage()));
            }
            
            throw e;
        }finally {
            if (cn != null) {
                cn.closeConnection();// Cerrar la conexión al finalizar
            }
        }
     
        return resp;
    }

    @Override
    public List<FacturaVO> getFacturas() throws SQLException {
        
         ArrayList<FacturaVO> resp= new ArrayList<FacturaVO>();
        
        String sqlGetFacturas = "SELECT * FROM fact";
        
         try (Connection con = cn.getConnection();
              PreparedStatement psGetFactura  = con.prepareStatement(sqlGetFacturas);
              ResultSet rsGetFactura = psGetFactura.executeQuery();) {
            
                while(rsGetFactura.next()){
                    FacturaVO facturaVo= new FacturaVO();
                    facturaVo.setId(rsGetFactura.getLong("fact_id"));
                    facturaVo.setNumeroFactura(rsGetFactura.getLong("fact_numerofactura"));
                    facturaVo.setNombreCliente(rsGetFactura.getString("fact_nombrecliente"));
                    facturaVo.setFecha(rsGetFactura.getDate("fact_fecha"));
                    facturaVo.setSubtotal(rsGetFactura.getDouble("fact_subtotal"));
                    facturaVo.setIva(rsGetFactura.getDouble("fact_iva"));
                    facturaVo.setTotal(rsGetFactura.getDouble("fact_total"));
                    
                    resp.add(facturaVo);
                }
            
        
        }catch (Exception e) {
            // Si ocurre un error, deshacer la transacción
            try (Connection con = cn.getConnection()) {
                if (con != null) {
                con.rollback();
                }
            } catch (SQLException ex) {
                System.out.println(String.format("Ocurrio un error ejecutando query getAll-> %s", ex.getMessage()));
            }
            
            throw e;
        }finally {
            if (cn != null) {
                cn.closeConnection();// Cerrar la conexión al finalizar
            }
        }
     
        return resp;
    }

}

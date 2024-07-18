/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Mediator.impl;

import Dao.FacturaDAO;
import Dao.impl.FacturaDaoImpl;
import Exception.FacturaException;
import Mediator.FacturaMediator;
import Vo.FacturaVO;
import java.sql.SQLException;
import java.util.List;
import util.ValidarCamposFacturaUtil;

/**
 *
 * @author cristian
 */
public class FacturaMediatorImpl implements FacturaMediator{
    
    private final FacturaDAO facturaDAO;

    public FacturaMediatorImpl() {
        this.facturaDAO= new FacturaDaoImpl();
    }
    
    @Override
    public FacturaVO saveFactura(FacturaVO facturaVO) throws FacturaException,SQLException {
        
        ValidarCamposFacturaUtil.validarCampos(facturaVO);
        
        return facturaDAO.saveFactura(facturaVO);
        
    }

    @Override
    public FacturaVO updateFactura(FacturaVO nuevaFacturaVO) throws FacturaException,SQLException{
        
        ValidarCamposFacturaUtil.validarCampos(nuevaFacturaVO);
       
       
        return facturaDAO.updateFactura(nuevaFacturaVO);
        
    }

    @Override
    public void deleteFactura(Long idFactura) throws FacturaException, SQLException {
        
        facturaDAO.deleteFactura(idFactura);
    }

    @Override
    public FacturaVO getFacturaById(Long facturaId) throws FacturaException, SQLException {
        
        return facturaDAO.getFacturaById(facturaId);
    }

    @Override
    public List<FacturaVO> getFacturas() throws SQLException {
        
        return facturaDAO.getFacturas();
    }

   
 
}

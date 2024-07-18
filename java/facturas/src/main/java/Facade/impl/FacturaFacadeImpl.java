/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Facade.impl;

import Exception.FacturaException;
import Facade.FacturaFacade;
import Mediator.FacturaMediator;
import Mediator.impl.FacturaMediatorImpl;
import Vo.FacturaVO;
import Vo.RespuestaVO;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author cristian
 */
public class FacturaFacadeImpl implements FacturaFacade {
    
    private final FacturaMediator facturaMediator;

    public FacturaFacadeImpl() {
        this.facturaMediator=new FacturaMediatorImpl();
    }
    
    
    @Override
    public RespuestaVO saveFactura(FacturaVO facturaVO) {
       RespuestaVO res= new RespuestaVO();
       
        try {
            FacturaVO bussines= facturaMediator.saveFactura(facturaVO);
            res.setNegocio(bussines);
            res.setOk(true);
            res.setMensaje("Prceso exitoso se ha guargado la factura");
            res.setStatus(HttpServletResponse.SC_CREATED);
            
        } catch (Exception e) {
            
            if(e instanceof FacturaException){
                return new RespuestaVO(null, e.getMessage(), false, HttpServletResponse.SC_BAD_REQUEST);
            }else{
                Logger.getLogger("FacturaFacadeImpl").log(Level.SEVERE, "Ocurrio un error al guardar la factura con el siguiente mensaje de error -> {0}", e.getMessage());
                return new RespuestaVO(null, "Ocurrio un error inesperado", false, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        }
        
        return res;
    }

    @Override
    public RespuestaVO updateFactura(FacturaVO nuevaFacturaVO) {
        
        RespuestaVO res= new RespuestaVO();
       
        try {
            FacturaVO bussines= facturaMediator.updateFactura(nuevaFacturaVO);
            res.setNegocio(bussines);
            res.setOk(true);
            res.setMensaje("Prceso exitoso se ha actualizado la factura");
            res.setStatus(HttpServletResponse.SC_OK);
            
        } catch (Exception e) {
            
            if(e instanceof FacturaException){
                return new RespuestaVO(null, e.getMessage(), false, HttpServletResponse.SC_BAD_REQUEST);
            }else{
                Logger.getLogger("FacturaFacadeImpl").log(Level.SEVERE, "Ocurrio un error al actualizar la factura con el siguiente mensaje de error -> {0}", e.getMessage());
                return new RespuestaVO(null, "Ocurrio un error inesperado", false, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        }
        
        return res;
    }

    @Override
    public RespuestaVO deleteFactura(Long idFactura) {
        
        RespuestaVO res= new RespuestaVO();
       
        try {
            facturaMediator.deleteFactura(idFactura);
            res.setOk(true);
            res.setMensaje("Prceso exitoso se ha eliminado la factura");
            res.setStatus(HttpServletResponse.SC_OK);
            
        } catch (Exception e) {
            
            if(e instanceof FacturaException){
                return new RespuestaVO(null, e.getMessage(), false, HttpServletResponse.SC_BAD_REQUEST);
            }else{
                Logger.getLogger("FacturaFacadeImpl").log(Level.SEVERE, "Ocurrio un error al eliminar la factura con el siguiente mensaje de error -> {0}", e.getMessage());
                return new RespuestaVO(null, "Ocurrio un error inesperado", false, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        }
        
        return res;
    }

    @Override
    public RespuestaVO getFacturaById(Long facturaId) {
        
        RespuestaVO res= new RespuestaVO();
       
        try {
            FacturaVO bussines= facturaMediator.getFacturaById(facturaId);
            res.setNegocio(bussines);
            res.setOk(true);
            res.setMensaje("Prceso exitoso se ha consultado la factura");
            res.setStatus(HttpServletResponse.SC_OK);
            
        } catch (Exception e) {
            
            if(e instanceof FacturaException){
                return new RespuestaVO(null, e.getMessage(), false, HttpServletResponse.SC_BAD_REQUEST);
            }else{
                Logger.getLogger("FacturaFacadeImpl").log(Level.SEVERE, "Ocurrio un error al consultar la factura con id {0} mensaje de error -> {1}", new Object []{facturaId, e.getMessage()});
                return new RespuestaVO(null, "Ocurrio un error inesperado", false, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        }
        
        return res;
    }

    @Override
    public RespuestaVO getFacturas() {
        
        RespuestaVO res= new RespuestaVO();
       
        try {
            List<FacturaVO> bussines= facturaMediator.getFacturas();
            res.setNegocio(bussines);
            res.setOk(true);
            res.setMensaje("Prceso exitoso se ha consultado las facturas");
            res.setStatus(HttpServletResponse.SC_OK);
            
        } catch (Exception e) {
            
            if(e instanceof FacturaException){
                return new RespuestaVO(null, e.getMessage(), false, HttpServletResponse.SC_BAD_REQUEST);
            }else{
                Logger.getLogger("FacturaFacadeImpl").log(Level.SEVERE, "Ocurrio un error al consultar las facturas, mensaje de error -> {0}", e.getMessage());
                return new RespuestaVO(null, "Ocurrio un error inesperado", false, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        }
        
        return res;
    }
    
    
}

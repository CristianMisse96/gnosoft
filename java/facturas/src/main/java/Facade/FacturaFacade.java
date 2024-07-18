/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Facade;

import Vo.FacturaVO;
import Vo.RespuestaVO;

/**
 *
 * @author cristian
 */
public interface FacturaFacade {

    public RespuestaVO saveFactura(FacturaVO facturaVO);

    public RespuestaVO updateFactura(FacturaVO nuevaFacturaVO);

    public RespuestaVO deleteFactura(Long idFactura);

    public RespuestaVO getFacturaById(Long facturaId);

    public RespuestaVO getFacturas();


    
}

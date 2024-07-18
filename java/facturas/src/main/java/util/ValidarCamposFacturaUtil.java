/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import Exception.FacturaException;
import Vo.FacturaVO;

/**
 *
 * @author cristian
 */
public class ValidarCamposFacturaUtil {
    
    public static void validarCampos(FacturaVO facturaVO) throws FacturaException {
        
      if (facturaVO.getNombreCliente() == null || facturaVO.getNombreCliente().isEmpty()) {
        throw new FacturaException("El nombre del cliente no puede estar vacío");
      }else if(facturaVO.getSubtotal() <=0.0){
          throw new FacturaException("El subtotal debe ser mayor que cero");
      }else if(facturaVO.getIva() <=0){
          throw new FacturaException("El IVA debe ser mayor que cero");
      }else if(facturaVO.getTotal()<=0){
          throw new FacturaException("El total debe ser mayor que cero");
      }else if(facturaVO.getItems().isEmpty()){
          throw new FacturaException("La factura debe contener al menos un ítem");
      }
    }
}

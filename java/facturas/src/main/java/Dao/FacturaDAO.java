/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Exception.FacturaException;
import Vo.FacturaVO;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author cristian
 */
public interface FacturaDAO {

    public FacturaVO saveFactura(FacturaVO facturaVO) throws SQLException;

    public FacturaVO updateFactura(FacturaVO nuevaFacturaVO) throws FacturaException, SQLException;

    public void deleteFactura(Long idFactura) throws FacturaException, SQLException;

    public FacturaVO getFacturaById(Long facturaId) throws FacturaException, SQLException;

    public List<FacturaVO> getFacturas() throws SQLException;
    
}

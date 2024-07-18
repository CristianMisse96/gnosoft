/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Webservice;

import Facade.FacturaFacade;
import Facade.impl.FacturaFacadeImpl;
import Vo.FacturaVO;
import Vo.RespuestaVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author cristian
 */
public class FacturaWebService extends HttpServlet {
    
    private final FacturaFacade facturaFacade;
    private final ObjectMapper objectMapper;
    private static final String CONTENT_TYPE="application/json";
    private static final String CHARACTER_ENCODING="UTF-8";

    public FacturaWebService() {
        this.objectMapper = new ObjectMapper();
        this.facturaFacade=new FacturaFacadeImpl();
    }
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet FacturaWebService</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet FacturaWebService at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType(CONTENT_TYPE);
        response.setCharacterEncoding(CHARACTER_ENCODING);
        RespuestaVO res= new RespuestaVO();
        
        // Obtener el ID de la factura del path variable
        String pathInfo = request.getPathInfo();
        if (pathInfo != null && pathInfo.split("/").length >= 2) {
            
            Long facturaId = Long.parseLong(pathInfo.split("/")[1]);
            res = facturaFacade.getFacturaById(facturaId);
        }else{
            res = facturaFacade.getFacturas();
        }
        
        response.getWriter().write(objectMapper.writeValueAsString(res));
        response.setStatus(res.getStatus());
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType(CONTENT_TYPE);
        response.setCharacterEncoding(CHARACTER_ENCODING);
        
        FacturaVO facturaVO = objectMapper.readValue(request.getInputStream(), FacturaVO.class);
        
        RespuestaVO res = facturaFacade.saveFactura(facturaVO);
        
        response.getWriter().write(objectMapper.writeValueAsString(res));
        response.setStatus(res.getStatus());
        
        
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        resp.setContentType(CONTENT_TYPE);
        resp.setCharacterEncoding(CHARACTER_ENCODING);
        
         // Obtener el ID de la factura del path variable
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.split("/").length < 2) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Ocurrio un error inesperado");
            Logger.getLogger("FacturaWebService").severe("No se propociono el ID de factura a actualizar");
            return;
        }
        
        Long facturaId = Long.parseLong(pathInfo.split("/")[1]);

        // Leer la nueva factura del body
        FacturaVO nuevaFacturaVO = objectMapper.readValue(req.getInputStream(), FacturaVO.class);
        nuevaFacturaVO.setId(facturaId);
        
        RespuestaVO res = facturaFacade.updateFactura(nuevaFacturaVO);
        
        resp.getWriter().write(objectMapper.writeValueAsString(res));
        resp.setStatus(res.getStatus());
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        resp.setContentType(CONTENT_TYPE);
        resp.setCharacterEncoding(CHARACTER_ENCODING);
        
         // Obtener el ID de la factura del path variable
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.split("/").length < 2) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Ocurrio un error inesperado");
            Logger.getLogger("FacturaWebService").severe("No se propociono el ID de factura a eliminar");
            return;
        }
        
        RespuestaVO res = facturaFacade.deleteFactura(Long.valueOf(pathInfo.split("/")[1]));
        
        resp.getWriter().write(objectMapper.writeValueAsString(res));
        resp.setStatus(res.getStatus());
    }
    
    
    
    

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}

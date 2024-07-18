/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author cristian
 */
public class Conexion {
    
    Connection con;
    String url="jdbc:postgresql://localhost:5432/facturacion";
    String user="postgres";
    String pass="1234";
    
    public Conexion(){
        try {
            Class.forName("org.postgresql.Driver");            
        } catch (Exception e) {
            System.err.println("Error"+e);
        }
    }
    
    public Connection getConnection(){
        try {
            if (con == null || con.isClosed()) {
                con = DriverManager.getConnection(url, user, pass);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener la conexión: " + e.getMessage());
        }
        return con;
    }
    
    public void closeConnection() {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }
}

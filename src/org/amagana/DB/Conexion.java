
package org.amagana.DB;

// */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/* IMPORTACIONES */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/

import java.sql.*;

/**
                                        * @author Leonel                                        
                                                                                                                .--------------------------------------------------------.      
Documentacion Nombre completo: Angel Leonel Magania Torres      .%%%%%%%%%%%%%%%%%%%%%%%%%.      
                                                                                                            .%%%%%%%%%%%%%%%%%%%%%%%%%.      
* Fecha de creacion:                                                                           .%%%%%%%%%%%%%*%%%%%%%%%%%%.      
  * 05/04/2024                                                                                      .%%%%%%%%%%%%#=%%%%%%%%%%%%.      
                                                                                                             .%%%%%%%%%%%+==%%%%%%%%%%%%.      
* Fecha de Modificacion:                                                                       .%%%%%%%%%%#==*#%%%%%%%%%%%%.      
  8/04 // 10/04 // 11/04 // 3/05                                                                  #%%%%%%%%%%#==%%%%%%%%%%%%.      
                                                                                                                 -%%%%%%%%%%%*=#%%%%%%%%%%+       
                                                                                                                    .*%%%%%%%%%*====#%%%%%%%#:       
                                                                                                                      .=%%%%%%%#=======#%%%%%%+.       
                                                                                                                         =%%%%%*=========+%%%%%.        
                                                                                                                               +*++==============++.         
                                                                                                                                  .:================-.          
                                                                                                                                      .-=============:.           
                                                                                                                                      .:==========-.             
                                                                                                                                         .:======-.               
                                                                                                                                              .:--:.                 

 */
public class Conexion {
    
    // DECLARACION DE VARIABLES

    // */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/* CONEXION MYSQL Y SINGLETON */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/
    
    private Connection conexion;
    private static Conexion instancia;
    
    public static Conexion getInstance() {
        if (instancia == null) {
            instancia = new Conexion();
        }
        return instancia;
    }

    // LINEA DE CONECCION 

    // */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/* METODO DE CONEXION */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/
    
    public Conexion() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            conexion = DriverManager.getConnection
        ("jdbc:mysql://localhost:3306/SuperKinal?useSSL=false","root","admi");
        } catch (ClassNotFoundException | SQLException
                | InstantiationException | IllegalAccessException e) {
            e.printStackTrace(); 
        }
    }

    // */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/* GETTERS Y SETTERS */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/
    
    

    public Connection getConexion() {
        return conexion;
    }

    public void setConexion(Connection conexion) {
        this.conexion = conexion;
    }
}

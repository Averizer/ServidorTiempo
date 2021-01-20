/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.Time;
import java.time.LocalTime;

/**
 *
 * @author luis
 */
public class timebd {
    private final String url = "jdbc:mysql://localhost/datos_reloj";
    PreparedStatement psPrepararSentencia;
    Connection conn = null;
    String userDB = "root";
    String passDB = "password";
    

    public void conectar(){
        try{   //Inicio del try
         
         //Class.forName("com.mysql.jdbc.Driver");     //Con el metodo de la clase forName, le pasamos el driver de MySQL para que lo cargue    
         conn = DriverManager.getConnection(url, userDB, passDB);    //Apuntamos nuestro objeto con a el intento de conectarse con los parametros o las credenciales que tenemos en MYSQL
        //Aqui mandamos la url donde viene la direccion de la BD, nuestro nombre de usuario y la contraseña, que por defecto al instalar viene vacia
        if (conn!=null){                         //Si logramos conectarnos, con deja de apuntar a null y obtenemos conexion
            System.out.println("Conexión a base de datos funcionando");                //Sin funciona imprimimos en consola un mensaje
            }
        }//cerramos el try
         catch(SQLException e)        //Agarramos excepciones de tipo SQL
         {
         System.out.println(e);          //las mostramos en consola
         }
         /*catch(ClassNotFoundException e)       //agarramos excepciones de tipo clase en java
         {
          System.out.println(e);               //las mostramos en consola
         }*/
    }
    
    public Connection conectado(){  //Este metodo de tipo Connection nos devuelve el estado del objeto
      return conn;
    }

    public void desconectar(){     //Por seguridad, cuando terminemos sentencias, cerramos la conexion o si la necesitamos cerrar por otro caso
      conn = null;                  //Ahora de nuevo con sera null
      System.out.println("La conexion la BD se ha cerrado");

    } 
    
    
    public void saveHoraCentral(String hprev, String href) throws SQLException{
        conn = DriverManager.getConnection(url, userDB, passDB);
        try {
            Statement stmt = (Statement) conn.createStatement();          
            String req = "INSERT INTO hora_central (hPrev, hRef) "
                    + "VALUES ('"+ hprev +"','" + href +"' );";
            stmt.executeUpdate(req);
        } catch (SQLException e) {
            System.out.println("Error:");
            System.out.println(e);
        }
        //System.out.println("save hora central");
        conn = null;
    };
    
    public void saveEquipos(String ip, String port, String latencia) throws SQLException{
        conn = DriverManager.getConnection(url, userDB, passDB);
        try {
            Statement stmt = (Statement) conn.createStatement();          
            String req = "INSERT INTO equipos (ip, port, latencia_ms) "
                    + "VALUES ('"+ ip +"','" + port +"','"+latencia+ "' );";
            stmt.executeUpdate(req);
        } catch (SQLException e) {
            System.out.println("Error:");
            System.out.println(e);
        }
        //System.out.println("save equipos");
        conn = null;
    };
    
    public String getHoraCentral() throws SQLException{
        conn = DriverManager.getConnection(url, userDB, passDB);
        String resultado="";
        String lastID = "";
        String lastHPrev = "";
        String lastHRef = "";
        try{
            // if you only need a few columns, specify them by name instead of using "*"
            String query = "SELECT id, hPrev, hRef FROM hora_central WHERE id = (SELECT MAX(id))";
            // create the java statement
            Statement st = conn.createStatement();
            // execute the query, and get a java resultset
            ResultSet rs = st.executeQuery(query);
            // iterate through the java resultset
            while (rs.next())
            {
              lastID = rs.getString("id");
              lastHPrev = rs.getString("hPrev");
              lastHRef = rs.getString("hRef");
            }
            st.close();
        }catch (SQLException e){
            System.err.println("Error! ");
            System.err.println(e.getMessage());
        }
        resultado = lastID + "," + lastHPrev + "," + lastHRef;
        //System.out.println(resultado);
        conn = null;
        //System.out.println("get hora central");
        return resultado;
    }
    
    public String getEquipo(int id ) throws SQLException{
        conn = DriverManager.getConnection(url, userDB, passDB);
        String resultado="";
        String lastID = "";
        String lastLatencia = "";
        try{
            // if you only need a few columns, specify them by name instead of using "*"
            String query = "SELECT id, latencia_ms FROM equipos WHERE id = (SELECT MAX("+id+"))";
            // create the java statement
            Statement st = conn.createStatement();
            // execute the query, and get a java resultset
            ResultSet rs = st.executeQuery(query);
            // iterate through the java resultset
            while (rs.next())
            {
              lastID = rs.getString("id");
              lastLatencia = rs.getString("latencia_ms");
            }
            st.close();
        }catch (SQLException e){
            System.err.println("Error! ");
            System.err.println(e.getMessage());
        }
        resultado = lastID + "," + lastLatencia;
        conn = null;
        //System.out.println("get equipo");
        return resultado;
    }
    
    public void saveHoraEquipos(int idhSincr, int idEquipo, String hEquipo, String aEquipo) throws SQLException{
        conn = DriverManager.getConnection(url, userDB, passDB);
        try {
            Statement stmt = (Statement) conn.createStatement();          
            String req = "INSERT INTO hora_equipos (idhSincr, idEquipo, hEquipo, aEquipo) "
                    + "VALUES ("+ idhSincr +"," + idEquipo +",'"+ hEquipo + "','" + aEquipo + "');";
            stmt.executeUpdate(req);
        } catch (SQLException e) {
            System.out.println("Error:");
            System.out.println(e);
        }
        conn = null;
    }
    
}

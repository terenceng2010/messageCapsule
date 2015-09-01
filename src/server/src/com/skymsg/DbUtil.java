package com.skymsg;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.postgis.*;


public class DbUtil {
	   private static Connection con = null;
	   public static Connection getConnection() throws Exception {	
			
			try 
		    {
		      Class.forName("org.postgresql.Driver").newInstance();
		      String url = "jdbc:postgresql://localhost:5432/skymsg";
		      con = DriverManager.getConnection(url, "postgres" , "23512921" );
		      ((org.postgresql.PGConnection)con).addDataType("geometry",Class.forName("org.postgis.PGgeometry"));
		      ((org.postgresql.PGConnection)con).addDataType("box3d",Class.forName("org.postgis.PGbox3d"));		    
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
			
			return con;
		}	   
}

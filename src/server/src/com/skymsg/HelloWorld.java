package com.skymsg;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.sql. * ;
import org.postgresql.* ;

@WebServlet(name="HelloServlet", urlPatterns={"/hello.view"},
            loadOnStartup=1)
public class HelloWorld extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
                        throws ServletException, IOException {
    	try 
        {
          Class.forName("org.postgresql.Driver").newInstance();
          String url = "jdbc:postgresql://localhost:5432/skymsg" ;
          Connection con = DriverManager.getConnection(url, "postgres" , "23512921" );
          Statement st = con.createStatement();
          String sql = "select * from activities" ;
          ResultSet rs = st.executeQuery(sql);
           while (rs.next())
           {
              System.out.print(rs.getString( 1 ));
              System.out.println(rs.getString( 2 ));
          } 
          rs.close();
          st.close();
          con.close();
        }
        catch (Exception ee)
        {
           System.out.print(ee.getMessage());
        } 
    	PrintWriter out = resp.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Hello Servlet</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1> Hello! World!</h1>");
        out.println("</body>");
        out.println("</html>");
        out.close();
    }  
        
    
}

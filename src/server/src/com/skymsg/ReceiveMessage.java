package com.skymsg;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class ReceiverMessage
 */
@WebServlet("/ReceiveMessage")
public class ReceiveMessage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String USERID_QUERY = "select userid from public.user where email @@ to_tsquery(?)";
	private static final String MESSAGE_QUERY = "select * from public.message where receiverid=?";   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReceiveMessage() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		  String strUserID = request.getParameter("userID");
		  String strErrMsg = null;
		  List<String> receiveMessage = new ArrayList<String>();
		  try {
			  receiveMessage = receiveMessage(strUserID);
			  	
			  
		    response.setContentType("text/plain");
		    PrintWriter out = response.getWriter();
			for (int i = 0; i < receiveMessage.size(); i++) {
				out.println(receiveMessage.get(i));
			}
		  } catch(Exception e) {
		    strErrMsg = "Unable to validate user / password in database";
		  }

	}
	
	private Connection getConnection() throws Exception {	
		Connection con = null;
		try 
	    {
	      Class.forName("org.postgresql.Driver").newInstance();
	      String url = "jdbc:postgresql://localhost:5432/skymsg";
	      con = DriverManager.getConnection(url, "postgres" , "23512921" );
	    
	    }
	    catch (Exception ee)
	    {
	       System.out.print(ee.getMessage());
	       throw ee;
	    } 
		return con;
	}

	private List<String> receiveMessage(String strUserID) throws Exception {
		 List<String> receiveMessage = new ArrayList<String>();
		  Connection conn = null;
		  try {
		    conn = getConnection();
		    PreparedStatement prepStmt = conn.prepareStatement(MESSAGE_QUERY);
		    System.out.println(prepStmt);
		    prepStmt.setInt(1, Integer.parseInt(strUserID));
		    System.out.println(prepStmt);
		    ResultSet rs = prepStmt.executeQuery();
		    if(rs.next()) {
		    	receiveMessage.add(rs.getString("body") + "," + rs.getInt("senderid"));
		      
		    }
		    else{
		    	System.out.println("no result");
		    }
		 } catch(Exception e) {
		   System.out.println("validateLogon: Error while validating password: "+e.getMessage());
		   throw e;
		 } finally {
		    closeConnection(conn);
		 }
		 return receiveMessage;
		}

	private void closeConnection(Connection conn) {
		try {
			if(conn!=null && !conn.isClosed()) {
				conn.close();
			}
		} catch(SQLException sqle) {
			System.out.println("Error while closing connection.");
		}
	}
}



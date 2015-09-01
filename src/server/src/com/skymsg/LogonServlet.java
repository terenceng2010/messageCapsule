package com.skymsg;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 * Servlet implementation class LogonServlet
 */
@WebServlet("/LogonServlet")
public class LogonServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
//	private static final String DBNAME = "tf_loginappdb";
//	private static final String DB_USERNAME = "postgres";
//	private static final String DB_PASSWORD = "23512921";

	private static final String LOGIN_QUERY = "select * from public.user where email @@ to_tsquery(?) and password @@ to_tsquery(?)";
//	private static final String HOME_PAGE = "/skymsg/Home.jsp";
//	private static final String LOGIN_PAGE = "/skymsg/Login.jsp";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LogonServlet() {
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
		  String strUserName = request.getParameter("email");
		  String strPassword = request.getParameter("password");
		  String strErrMsg = null;
		  HttpSession session = request.getSession();
		  boolean isValidLogon = false;
		  try {
		    isValidLogon = authenticateLogin(strUserName, strPassword);
		    if(isValidLogon) {
		       session.setAttribute("email", strUserName);
		    } else {
		       strErrMsg = "User name or Password is invalid. Please try again.";
		    }
		  } catch(Exception e) {
		    strErrMsg = "Unable to validate user / password in database";
		  }

		  if(isValidLogon) {
		    response.setContentType("text/plain");
		    PrintWriter out = response.getWriter();
		    out.println("Valid Login");
		    
		  } else {
			response.setContentType("text/plain");
		    PrintWriter out = response.getWriter();
		    out.println("Invalid Login");
		  }

		}

private boolean authenticateLogin(String strUserName, String strPassword) throws Exception {
	  boolean isValid = false;
	  Connection conn = null;
	  try {
	    conn = DbUtil.getConnection();
	    PreparedStatement prepStmt = conn.prepareStatement(LOGIN_QUERY);
	    
//	    System.out.println(strUserName);
//	    System.out.println(strPassword);
	    
	    prepStmt.setString(1, strUserName);
	    prepStmt.setString(2, strPassword);
	    ResultSet rs = prepStmt.executeQuery();
	    if(rs.next()) {
	      System.out.println("User login is valid in DB");
	      isValid = true;  
	    }
	 } catch(Exception e) {
	   System.out.println("validateLogon: Error while validating password: "+e.getMessage());
	   throw e;
	 } finally {
	    closeConnection(conn);
	 }
	 return isValid;
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
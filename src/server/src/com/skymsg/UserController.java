package com.skymsg;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gcm.server.*;

@WebServlet("/UserController")
public class UserController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static String INSERT_OR_EDIT = "/user.jsp";
    private static String LIST_USER = "/listUser.jsp";
    private UserDao dao;

    public UserController() {
        super();
        dao = new UserDao();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String forward="";
//        String action = request.getParameter("action");
//
//        if (action.equalsIgnoreCase("delete")){
//            int userId = Integer.parseInt(request.getParameter("userId"));
//            dao.deleteUser(userId);
//            forward = LIST_USER;
//            request.setAttribute("users", dao.getAllUsers());    
//        } else if (action.equalsIgnoreCase("edit")){
//            forward = INSERT_OR_EDIT;
//            int userId = Integer.parseInt(request.getParameter("userId"));
//            User user = dao.getUserById(userId);
//            request.setAttribute("user", user);
//        } else if (action.equalsIgnoreCase("listUser")){
//            forward = LIST_USER;
//            request.setAttribute("users", dao.getAllUsers());
//        } else {
//            forward = INSERT_OR_EDIT;
//        }
//
//        RequestDispatcher view = request.getRequestDispatcher(forward);
//        view.forward(request, response);
    }

    //for register and login
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
    	User user = new User();
        user.setFirstName(request.getParameter("firstName"));
        user.setLastName(request.getParameter("lastName"));
        user.setPassword(request.getParameter("password"));
        user.setEmail(request.getParameter("email"));
        user.setGcmregid(request.getParameter("gcmregid"));
        String action=request.getParameter("action");

        
        if(action.equals("login"))
        {
        	User existUser= dao.getUserByPassword(request.getParameter("email"),request.getParameter("password"));
            if(existUser !=null )
            {
            	//normal login
                JSONObject jsonReply=existUser.toJson();
                try {
					jsonReply.put("validUser", true);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                response.setContentType("text/json");              
                response.getWriter().println(jsonReply.toString());  
                System.out.println(jsonReply);
            }
            else
            {
            	//wrong email or password
                JSONObject jsonReply= new JSONObject();
                try {
					jsonReply.put("validUser", false);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                response.setContentType("text/json");              
                response.getWriter().println(jsonReply.toString());  
                System.out.println(jsonReply);
            }  
        }
        else if(action.equals("updateGCMregid"))
        {
        	//user.setUserid(Integer.parseInt(userid));
    		response.setContentType("text/plain");
    		PrintWriter out = response.getWriter();
    		
    		
        	//assume already login success
        	User existUserUseGcmregid = dao.getUserBygcmregid(request.getParameter("gcmregid"));
        	User existUser = dao.getUserByEmail(request.getParameter("email"));	        
        	Boolean updateGCMregid = false;
	        System.out.println(existUserUseGcmregid);
    		
	        //this gcmid NO ONE is using
    		if(existUserUseGcmregid.getEmail()==null){
    			
//    			if(existUser.getGcmregid()!=null)
//    			{
//    				out.println("This app can only allow one account per phone. Please do not signin to other phone. If you really want, please uninstall the apps from both phone and reinstall it in the phone you desire to use the app");
//    			}
    				updateGCMregid = dao.updateUsergcmregid(user);
    				
    			if(updateGCMregid==false)
    			{
    				System.out.print("Update fail");
    				out.println("Update fail");
    			}
    			else
    			{
    				
    				System.out.print("Update success");	
    				out.println("Update success");
    				
    			}
    		}
    		else{
    			//is that the owner?
    			if (existUserUseGcmregid.getEmail().equals(request.getParameter("email")) ){
    			    System.out.println("No need to update your gcmid");
    				out.println("No need to update your gcmid");
    			}
    			else{
    				System.out.println("This app can only allow one account per phone. Please do not signin to other phone. If you really want, please uninstall the apps from both phone and reinstall it in the phone you desire to use the app");
    				out.println("This app can only allow one account per phone. Please do not signin to other phone. If you really want, please uninstall the apps from both phone and reinstall it in the phone you desire to use the app");
    			}
    		}


    		
        }else if(action.equals("register"))
        {
        	User existUser= dao.getUserByEmail(request.getParameter("email"));
        	System.out.print(existUser);
        	if(existUser.getEmail()==null){
        		Boolean addUserResult = dao.addUser(user);
        		if(addUserResult){
        		    response.setContentType("text/plain");
        		    PrintWriter out = response.getWriter();
        		    out.println("Register Success");        			
        		}else{
        		    response.setContentType("text/plain");
        		    PrintWriter out = response.getWriter();
        		    out.println("Register Fail");      			
        		}
        	}
        	else
        	{
    		    response.setContentType("text/plain");
    		    PrintWriter out = response.getWriter();
    		    out.println("Register Fail. Email Address is used"); 	
        	}
        }

        request.setAttribute("users", dao.getAllUsers());

    }
}

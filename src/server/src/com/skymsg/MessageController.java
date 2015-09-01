package com.skymsg;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.postgis.PGgeometry;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;

/**
 * Servlet implementation class MessageController
 */
@WebServlet("/MessageController")
public class MessageController extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private UserDao userDao;
    private MessageDao messageDao;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MessageController() {
        super();
        // TODO Auto-generated constructor stub
        messageDao = new MessageDao();
        userDao = new UserDao();
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
		
		


   
        //grab requesting action user info
        User user= userDao.getUserById(Integer.parseInt(request.getParameter("userid")));
    	//grab receiver info	
        User receiveMessageUser = userDao.getUserByEmail(request.getParameter("recipientEmail"));
        
        
        System.out.println(request.getParameter("recipientEmail"));
        //create a new message template
        SkymsgMessage skymsgMessage = new SkymsgMessage();
        

//        if(request.getParameter("weather_condition")!=null)
//        	skymsgMessage.setWeatherCondition(Integer.parseInt(request.getParameter("weather_condition")) );
        if(request.getParameter("revealDate")!=null)
        	skymsgMessage.setRevealDate(request.getParameter("revealDate"));
        if(request.getParameter("notifyDate")!=null)
        	skymsgMessage.setNotifyDate(request.getParameter("notifyDate"));
        
//        if(request.getParameter("date_to_notify")!=null)
//        	skymsgMessage.setMovable(Boolean.parseBoolean(request.getParameter("movable")));
//        if(request.getParameter("date_to_notify")!=null)
//        	skymsgMessage.setTemperature(Integer.parseInt(request.getParameter("temperature")));
//        if(request.getParameter("date_to_notify")!=null)
//        	skymsgMessage.setTemperatureRelated(Boolean.parseBoolean(request.getParameter("temperature_lower_or_higher")));
    	
        skymsgMessage.setBody(request.getParameter("body"));
    	
        if(request.getParameter("userid")!=null)
        	skymsgMessage.setSenderid(Integer.parseInt(request.getParameter("userid")));


        
    	try {
			if(request.getParameter("latitude")!=null)
    		skymsgMessage.setLocation(PGgeometry.geomFromString("SRID=" + "4326" + ";" + "POINT("+ request.getParameter("latitude") +" "+ request.getParameter("longitude")+")") );
			skymsgMessage.setAddress(request.getParameter("address"));
    	} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	if(request.getParameter("createdate")!=null)
    	skymsgMessage.setCreateDate(Timestamp.valueOf(request.getParameter("createdate")));        
        
        String action=request.getParameter("action");
        
        if(action.equalsIgnoreCase("sendplainmessage"))
        {
        	skymsgMessage.setReceiverid(receiveMessageUser.getUserid());
        	Boolean addMessageResult = messageDao.addPlainOldMessage(skymsgMessage);
        	
        	if(addMessageResult==true){
        		
        		//send to GCM to client
        	    String registrationId = receiveMessageUser.getGcmregid();
        	    System.out.println("regid" + registrationId);
        	    Sender sender = new Sender("AIzaSyCswqO6BE5lxsyQ6gHeI7_BzvPvPfj-uyU");
                Message message = new Message.Builder()
                .collapseKey("1")
                .delayWhileIdle(false)
                .addData("type","plainmessage")
                .addData("message", user.getFirstName()+user.getLastName()+" has sent a message to you")
                .addData("firstname",user.getFirstName())
                .addData("body",skymsgMessage.getBody())
                .build();
        	    Result result = sender.send(message, registrationId, 5);
        	    String status = "Sent message to one device: " + result;
        	    System.out.println(status); 
        	    
        	    //response to client
    		    response.setContentType("text/plain");
    		    PrintWriter out = response.getWriter();
    		    out.println("Message Sent");         		
        	}else{
        	    //response to client
    		    response.setContentType("text/plain");
    		    PrintWriter out = response.getWriter();
    		    out.println("Message Not Sent");             		
        	}
        }
        else if(action.equalsIgnoreCase("senddatemessage"))
        {
        	skymsgMessage.setReceiverid(receiveMessageUser.getUserid());
        	Boolean addMessageResult = messageDao.addDateMessage(skymsgMessage);
        	
        	if(addMessageResult==true){
        		    
        	    //response to client
    		    response.setContentType("text/plain");
    		    PrintWriter out = response.getWriter();
    		    out.println("Message Sent");         		
        	}else{
        	    //response to client
    		    response.setContentType("text/plain");
    		    PrintWriter out = response.getWriter();
    		    out.println("Message Not Sent");             		
        	}        	
        }
        else if(action.equalsIgnoreCase("sendlocationmessage"))
        {
        	skymsgMessage.setReceiverid(receiveMessageUser.getUserid());
        	Boolean addMessageResult = messageDao.addLocationMessage(skymsgMessage);
        	
        	if(addMessageResult==true){
    		    
        	    //response to client
    		    response.setContentType("text/plain");
    		    PrintWriter out = response.getWriter();
    		    out.println("Message Sent");         		
        	}else{
        	    //response to client
    		    response.setContentType("text/plain");
    		    PrintWriter out = response.getWriter();
    		    out.println("Message Not Sent");             		
        	}            	
        }
        else if(action.equalsIgnoreCase("receivenewmessage"))
        {
        	List<SkymsgMessage> messages = messageDao.getAllMessages(user.getUserid());
        	System.out.println(messages);
		    response.setContentType("text/json");
		    PrintWriter out = response.getWriter();
		    out.print("[");
		    
		    //iterate through the message list
		    for(int i=0; i < messages.size(); i++)
		    {
		    	if(i==messages.size()-1)
		    	{
		    		out.print(messages.get(i).toJson());
		    	}else
		    	out.print(messages.get(i).toJson()+",");
		    	
		    }
	
		    out.print("]");
        }

	}

}

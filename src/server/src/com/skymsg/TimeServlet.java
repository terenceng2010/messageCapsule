package com.skymsg;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;

/**
 * Servlet implementation class TimeServlet
 */
@WebServlet(value="/TimeServlet",loadOnStartup=1)
public class TimeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private UserDao userDao;
    private MessageDao messageDao;   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TimeServlet() {
        super();
        // TODO Auto-generated constructor stub
        messageDao = new MessageDao();
        userDao = new UserDao();
    }

    public void init() throws ServletException 
    {
        Timer timer = new Timer();
        timer.schedule(new MyTask(), 1000, 60000);
        System.out.println("現在時間：" + new Date());
       
//        try {
//            Thread.sleep(100);
//        }
//        catch(InterruptedException e) {
//        }

        
    }

  
    class MyTask extends java.util.TimerTask 
    {
    	//your code here
        public void run() 
        {
        	System.out.println("任務時間：" + new Date());
        	List<SkymsgMessage> messagesToBeNotified = new ArrayList<SkymsgMessage>();
        	List<SkymsgMessage> messagesToBeRevealed = new ArrayList<SkymsgMessage>();
        	
        	messagesToBeNotified = messageDao.getAllMessagesToBeNotified(); //if the message has set to have notify i.e date_to_notify not null
        	
        	 int messageSentForNotified= 0;
        	 for(int i=0; i < messagesToBeNotified.size(); i++)
        	 {
        		 
        		User receivingUser = userDao.getUserById(messagesToBeNotified.get(i).getReceiverid());
        		 
        		User sendingUser= userDao.getUserById(messagesToBeNotified.get(i).getSenderid());
        		
        		String msgRevealDate =null;
        		String msgRevealLocation ="";
        		
        		
        		if(messagesToBeNotified.get(i).getRevealDate()!=null)
        			msgRevealDate = messagesToBeNotified.get(i).getRevealDate().toString();
        		if(messagesToBeNotified.get(i).getLocation()!=null)
        			msgRevealLocation = "at " + messagesToBeNotified.get(i).getAddress() +  messagesToBeNotified.get(i).getLocation().getFirstPoint().x +  messagesToBeNotified.get(i).getLocation().getFirstPoint().y ; 

	         		//send to GCM to client
	         	    String registrationId = receivingUser.getGcmregid();
	         	    System.out.println("regid" + registrationId);
	         	    Sender sender = new Sender("AIzaSyCswqO6BE5lxsyQ6gHeI7_BzvPvPfj-uyU");
	                 Message message = new Message.Builder()
	                 .collapseKey("1")
	                 .delayWhileIdle(false)
	                 .addData("type","notify")
	                 .addData("message", sendingUser.getFirstName()+sendingUser.getLastName()+" has sent a message to you")
	                 .addData("revealDate","will be revealed in"+ msgRevealDate +  msgRevealLocation)
	                 .build();
	         	    Result result = null;
					try {
						result = sender.send(message, registrationId, 5);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(result!=null)
					{
						String status = "Sent notify message to one device: " + result;
						System.out.println(status);
						messageSentForNotified++;
					}
	         	    

        	 }
        	 System.out.println("Sent notify message to "+ messageSentForNotified +" device: " );
    	    
        	 
        	 messagesToBeRevealed = messageDao.getAllMessagesToBeRevealed(); //Time Related only, not include Time + Location Message
        	int messageSentForRevealed= 0;
        	 for(int i=0; i < messagesToBeRevealed.size(); i++)
        	 {
        		 
        		User receivingUser = userDao.getUserById(messagesToBeRevealed.get(i).getReceiverid());
        		 
        		User sendingUser= userDao.getUserById(messagesToBeRevealed.get(i).getSenderid());
        		
        		String msgRevealDate =null;
        		String msgRevealLocation = null;
        		
        		
        		if(messagesToBeRevealed.get(i).getRevealDate()!=null)
        			msgRevealDate = messagesToBeRevealed.get(i).getRevealDate().toString();
        		if(messagesToBeRevealed.get(i).getLocation()!=null)
        			msgRevealLocation = " at " + messagesToBeNotified.get(i).getAddress() +  messagesToBeNotified.get(i).getLocation().getFirstPoint().x +  messagesToBeNotified.get(i).getLocation().getFirstPoint().y ; 

	         		//send to GCM to client
	         	    String registrationId = receivingUser.getGcmregid();
	         	    System.out.println("regid" + registrationId);
	         	    Sender sender = new Sender("AIzaSyCswqO6BE5lxsyQ6gHeI7_BzvPvPfj-uyU");
	                 Message message = new Message.Builder()
	                 .collapseKey("1")
	                 .delayWhileIdle(false)
	                 .addData("type","reveal")
	                 .addData("message", sendingUser.getFirstName()+sendingUser.getLastName()+" 's message is revealed!")
	                 .addData("revealDate","in"+ msgRevealDate )
	                 .build();
	         	    Result result = null;
					try {
						result = sender.send(message, registrationId, 5);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(result!=null)
					{
						String status = "Sent reveal message to one device: " + result;
						System.out.println(status);
						messageSentForRevealed++;
					}
	         	    

        	 }
        	 System.out.println("Sent reveal message to "+ messageSentForRevealed +" device: " );        	
        }
    }
    
    public void destroy() 
    {
        super.destroy(); 
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
	}

}

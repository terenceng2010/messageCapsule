package com.skymsg;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.android.gcm.server.*;

/**
 * Servlet implementation class SendMessage
 */
@WebServlet("/SendMessage")
public class SendMessage extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SendMessage() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	    String registrationId = "APA91bG00LBn8Zs2LcJbwblLycndjCeP8fuedW8eq3OVhoGjN0CcNOzcEeuhbRHwpMxJjLr-s9SRFJPvClJ9lBotS6hraaifqlA1oVm49XsMwF1a-GSes5m_vIQkCo3QD-dcJwRbTR0l";
	    Sender sender = new Sender("AIzaSyCswqO6BE5lxsyQ6gHeI7_BzvPvPfj-uyU");
        Message message = new Message.Builder()
        .collapseKey("1")
        .timeToLive(3)
        .delayWhileIdle(true)
        .addData("message", "message is created at builder")
        .build();
	    Result result = sender.send(message, registrationId, 5);
	    String status = "Sent message to one device: " + result;
	    System.out.println(status);	
	    
	    String registrationId2 = "APA91bH7Ok3zpcTDnaZr6CTSNrK5jg8D_vfw0xtscV0cw4Plix4kTOAyuLc6Fp95x4ItDaViqPKmJzX0yIb9AbWqDYTMWSMeCjWg2Ht0C4OdVLBcap0f81kUj3DbOYO48Ygc9_wZEqyT";
	    Sender sender2 = new Sender("AIzaSyCswqO6BE5lxsyQ6gHeI7_BzvPvPfj-uyU");
        Message message2 = new Message.Builder()
        .collapseKey("1")
        .timeToLive(3)
        .delayWhileIdle(true)
        .addData("message", "message is created at builder")
        .build();
	    Result result2 = sender2.send(message2, registrationId2, 5);
	    String status2 = "Sent message to one device: " + result2;	   
	    System.out.println(status2);	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

	}

}

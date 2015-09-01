package com.terence;

import java.util.Random;

import com.google.android.gcm.GCMBaseIntentService;
import com.turbomanage.httpclient.BasicHttpClient;
import com.turbomanage.httpclient.HttpResponse;
import com.turbomanage.httpclient.ParameterMap;

import android.os.Bundle;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;


public class GCMIntentService extends GCMBaseIntentService {
	
	private int mId=12345;
	Random rand = new Random(); 
	
	
	public GCMIntentService()
	{
		super("40015296375");
	}
	@Override
	protected void onError(Context arg0, String arg1) {
		// TODO Auto-generated method stub
		Log.d("onError", arg1);
	}


	@Override
	protected void onMessage(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub
		String name = null;
		String type = null;
		name = arg1.getStringExtra("message");
		type = arg1.getStringExtra("type");
		System.out.println(arg1.toString());
		System.out.println(name);
		
//		//get current userid
//		SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); 	
//		int userid = pref.getInt("currentUserid", 0);
//		System.out.println("current user id:"+ userid );
//		//initial a request for all new message
//		BasicHttpClient httpClient = new BasicHttpClient("http://158.182.6.176:9090");		
//
//		
//	    ParameterMap paramsMap = httpClient.newParams()
//	            .add("continue", "/")
//	            .add("userid", String.valueOf(userid))
//	            .add("action", "receivenewmessage");
//	    
//	    httpClient.addHeader("someheader", "value");
//	    httpClient.setConnectionTimeout(5000); // 2s
//	    HttpResponse httpResponse = httpClient.post("/skymsg/MessageController", paramsMap);
//	    System.out.println(httpResponse.getBodyAsString());	
	    
		if(type.equals("plainmessage"))
		{
			String body = arg1.getStringExtra("body");
			String firstname = arg1.getStringExtra("firstname");
			NotificationCompat.Builder mBuilder =
			        new NotificationCompat.Builder(this)
			        .setSmallIcon(R.drawable.ic_launcher)
			        .setContentTitle(name)
			        .setContentText(body);
			// Creates an explicit intent for an Activity in your app
			Intent resultIntent = new Intent(this, MessageReceived.class);
			mBuilder.setDefaults(Notification.DEFAULT_ALL);
			mBuilder.setAutoCancel(true);
			mBuilder.setWhen(System.currentTimeMillis());
			mBuilder.setTicker("New Message From "+ firstname);
	
			// The stack builder object will contain an artificial back stack for the
			// started Activity.
			// This ensures that navigating backward from the Activity leads out of
			// your application to the Home screen.
			TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
			// Adds the back stack for the Intent (but not the Intent itself)
			stackBuilder.addParentStack(LoginActivity.class);
			// Adds the Intent that starts the Activity to the top of the stack
			stackBuilder.addNextIntent(resultIntent);
			PendingIntent resultPendingIntent =
			        stackBuilder.getPendingIntent(
			            0,
			            PendingIntent.FLAG_UPDATE_CURRENT
			        );
			mBuilder.setContentIntent(resultPendingIntent);
			NotificationManager mNotificationManager =
			    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			// mId allows you to update the notification later on.
		
			mNotificationManager.notify(rand.nextInt(50), mBuilder.build());	
		}else if(type.equals("notify")){
			
			String revealDate = arg1.getStringExtra("revealDate");
			
			NotificationCompat.Builder mBuilder =
			        new NotificationCompat.Builder(this)
			        .setSmallIcon(R.drawable.ic_launcher)
			        .setContentTitle(name)
			        .setContentText(revealDate);
			// Creates an explicit intent for an Activity in your app
			Intent resultIntent = new Intent(this, MessageReceived.class);
			mBuilder.setDefaults(Notification.DEFAULT_ALL);
			mBuilder.setAutoCancel(true);
			mBuilder.setWhen(System.currentTimeMillis());
			mBuilder.setTicker("Pre-message");
	
			// The stack builder object will contain an artificial back stack for the
			// started Activity.
			// This ensures that navigating backward from the Activity leads out of
			// your application to the Home screen.
			TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
			// Adds the back stack for the Intent (but not the Intent itself)
			stackBuilder.addParentStack(LoginActivity.class);
			// Adds the Intent that starts the Activity to the top of the stack
			stackBuilder.addNextIntent(resultIntent);
			PendingIntent resultPendingIntent =
			        stackBuilder.getPendingIntent(
			            0,
			            PendingIntent.FLAG_UPDATE_CURRENT
			        );
			mBuilder.setContentIntent(resultPendingIntent);
			NotificationManager mNotificationManager =
			    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			// mId allows you to update the notification later on.
			mNotificationManager.notify(rand.nextInt(50), mBuilder.build());	
			
		}else if(type.equals("reveal")){
			String revealDate = arg1.getStringExtra("revealDate");
			
			NotificationCompat.Builder mBuilder =
			        new NotificationCompat.Builder(this)
			        .setSmallIcon(R.drawable.ic_launcher)
			        .setContentTitle(name)
			        .setContentText(revealDate);
			// Creates an explicit intent for an Activity in your app
			Intent resultIntent = new Intent(this, MessageReceived.class);
			mBuilder.setDefaults(Notification.DEFAULT_ALL);
			mBuilder.setAutoCancel(true);
			mBuilder.setWhen(System.currentTimeMillis());
			mBuilder.setTicker("A Message is revealed to you!");
	
			// The stack builder object will contain an artificial back stack for the
			// started Activity.
			// This ensures that navigating backward from the Activity leads out of
			// your application to the Home screen.
			TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
			// Adds the back stack for the Intent (but not the Intent itself)
			stackBuilder.addParentStack(LoginActivity.class);
			// Adds the Intent that starts the Activity to the top of the stack
			stackBuilder.addNextIntent(resultIntent);
			PendingIntent resultPendingIntent =
			        stackBuilder.getPendingIntent(
			            0,
			            PendingIntent.FLAG_UPDATE_CURRENT
			        );
			mBuilder.setContentIntent(resultPendingIntent);
			NotificationManager mNotificationManager =
			    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			// mId allows you to update the notification later on.
			mNotificationManager.notify(mId, mBuilder.build());				
		}else if(type.equals("senderReceiveMessageRevealToReceiver")){
			String body = arg1.getStringExtra("body");
			
			NotificationCompat.Builder mBuilder =
			        new NotificationCompat.Builder(this)
			        .setSmallIcon(R.drawable.ic_launcher)
			        .setContentTitle(name)
			        .setContentText(body);
			
			// Creates an explicit intent for an Activity in your app
			Intent resultIntent = new Intent(this, SentMessageActivity.class);
			mBuilder.setDefaults(Notification.DEFAULT_ALL);
			mBuilder.setAutoCancel(true);
			mBuilder.setWhen(System.currentTimeMillis());
			mBuilder.setTicker("Your Message is revealed to someone!");
	
			// The stack builder object will contain an artificial back stack for the
			// started Activity.
			// This ensures that navigating backward from the Activity leads out of
			// your application to the Home screen.
			TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
			// Adds the back stack for the Intent (but not the Intent itself)
			stackBuilder.addParentStack(LoginActivity.class);
			// Adds the Intent that starts the Activity to the top of the stack
			stackBuilder.addNextIntent(resultIntent);
			PendingIntent resultPendingIntent =
			        stackBuilder.getPendingIntent(
			            0,
			            PendingIntent.FLAG_UPDATE_CURRENT
			        );
			mBuilder.setContentIntent(resultPendingIntent);
			NotificationManager mNotificationManager =
			    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			// mId allows you to update the notification later on.
			mNotificationManager.notify(rand.nextInt(50), mBuilder.build());		
			
		}else if(type.equals("receiveNewMessage")){
			String body = arg1.getStringExtra("body");
			
			NotificationCompat.Builder mBuilder =
			        new NotificationCompat.Builder(this)
			        .setSmallIcon(R.drawable.ic_launcher)
			        .setContentTitle(name)
			        .setContentText(body);
			
			// Creates an explicit intent for an Activity in your app
			Intent resultIntent = new Intent(this, MessageReceived.class);
			mBuilder.setDefaults(Notification.DEFAULT_ALL);
			mBuilder.setAutoCancel(true);
			mBuilder.setWhen(System.currentTimeMillis());
			mBuilder.setTicker("Someone leaves you a message");
	
			// The stack builder object will contain an artificial back stack for the
			// started Activity.
			// This ensures that navigating backward from the Activity leads out of
			// your application to the Home screen.
			TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
			// Adds the back stack for the Intent (but not the Intent itself)
			stackBuilder.addParentStack(LoginActivity.class);
			// Adds the Intent that starts the Activity to the top of the stack
			stackBuilder.addNextIntent(resultIntent);
			PendingIntent resultPendingIntent =
			        stackBuilder.getPendingIntent(
			            0,
			            PendingIntent.FLAG_UPDATE_CURRENT
			        );
			mBuilder.setContentIntent(resultPendingIntent);
			NotificationManager mNotificationManager =
			    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			// mId allows you to update the notification later on.
			mNotificationManager.notify(rand.nextInt(50), mBuilder.build());				
		}else if(type.equals("receiveNewMessageWeather")){
			
			
			String body = arg1.getStringExtra("body");
			String weatherCondition = arg1.getStringExtra("weathercondtion");
			if(weatherCondition==null){
				weatherCondition="1";
			}
			NotificationCompat.Builder mBuilder =
			        new NotificationCompat.Builder(this)
			        .setContentTitle(name)
			        .setContentText(body);
			
			switch(Integer.parseInt(weatherCondition)){	
				case 1:
					mBuilder.setSmallIcon(R.drawable.w1);
				break;
				case 2:
					mBuilder.setSmallIcon(R.drawable.w2);
				break;	
				case 3:
					mBuilder.setSmallIcon(R.drawable.w3);
				break;	
				case 4:
					mBuilder.setSmallIcon(R.drawable.w4);
				break;	
				case 5:
					mBuilder.setSmallIcon(R.drawable.w5);
				break;	
				case 6:
					mBuilder.setSmallIcon(R.drawable.w6);
				break;	
				case 7:
					mBuilder.setSmallIcon(R.drawable.w7);
				break;	
				case 8:
					mBuilder.setSmallIcon(R.drawable.w8);
				break;	
				case 9:
					mBuilder.setSmallIcon(R.drawable.w9);
				break;		
			}			

			// Creates an explicit intent for an Activity in your app
			Intent resultIntent = new Intent(this, MessageReceived.class);
			mBuilder.setDefaults(Notification.DEFAULT_ALL);
			mBuilder.setAutoCancel(true);
			mBuilder.setWhen(System.currentTimeMillis());
			mBuilder.setTicker("Someone leaves you a weather message");
	
			// The stack builder object will contain an artificial back stack for the
			// started Activity.
			// This ensures that navigating backward from the Activity leads out of
			// your application to the Home screen.
			TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
			// Adds the back stack for the Intent (but not the Intent itself)
			stackBuilder.addParentStack(LoginActivity.class);
			// Adds the Intent that starts the Activity to the top of the stack
			stackBuilder.addNextIntent(resultIntent);
			PendingIntent resultPendingIntent =
			        stackBuilder.getPendingIntent(
			            0,
			            PendingIntent.FLAG_UPDATE_CURRENT
			        );
			mBuilder.setContentIntent(resultPendingIntent);
			NotificationManager mNotificationManager =
			    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			// mId allows you to update the notification later on.
			mNotificationManager.notify(rand.nextInt(50), mBuilder.build());				
		}
//		Intent ReceiveMessageAndNotificationServiceIntent = new Intent(this, ReceiveMessageAndNotificationService.class);
//		startService(ReceiveMessageAndNotificationServiceIntent);		
	}

	@Override
	protected void onRegistered(Context arg0, String arg1) {
		// TODO Auto-generated method stub
		BasicHttpClient httpClient = new BasicHttpClient("http://158.182.6.176:9090");		


	    ParameterMap paramsMap = httpClient.newParams()
	            .add("continue", "/")
	            .add("regId", arg1)
	            .add("action", "update");
	    httpClient.addHeader("someheader", "value");
	    httpClient.setConnectionTimeout(5000); // 2s
	    HttpResponse httpResponse = httpClient.post("/skymsg/LogonServlet", paramsMap);
	    System.out.println(httpResponse.getBodyAsString());		
	}

	@Override
	protected void onUnregistered(Context arg0, String arg1) {
		// TODO Auto-generated method stub
		BasicHttpClient httpClient = new BasicHttpClient("http://158.182.6.176:9090");		


	    ParameterMap paramsMap = httpClient.newParams()
	            .add("continue", "/")
	            .add("regId", arg1)
	            .add("action", "delete");
	    httpClient.addHeader("someheader", "value");
	    httpClient.setConnectionTimeout(5000); // 2s
	    HttpResponse httpResponse = httpClient.post("/skymsg/LogonServlet", paramsMap);
	    System.out.println(httpResponse.getBodyAsString());	
	}



}

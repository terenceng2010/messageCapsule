package com.terence;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.terence.LoginActivity.HttpTask;
import com.turbomanage.httpclient.BasicHttpClient;
import com.turbomanage.httpclient.HttpResponse;
import com.turbomanage.httpclient.ParameterMap;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class MessageReceived extends Activity {

   // private static final String FILENAME = "friendList.txt";
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message_list);
		
//        //Create a new file and write some data
//        try {
//           FileOutputStream mOutput = openFileOutput(FILENAME, Activity.MODE_PRIVATE);
//           String data = "terence,terenceng2010@gmail.com\nkan,kan@gmail.com\nkan,kan@gmail.com\nkan,kan@gmail.com\nkan,kan@gmail.com\nkan,kan@gmail.com\nkan,kan@gmail.com\nkan,kan@gmail.com\nkan,kan@gmail.com\nkan,kan@gmail.com\nkan,kan@gmail.com\nkan,kan@gmail.com\nkan,kan@gmail.com\nkan,kan@gmail.com\nkan,kan@gmail.com\nkan,kan@gmail.com\nkan,kan@gmail.com\nkan,kan@gmail.com\nkan,kan@gmail.com\nkan,kan@gmail.com\nkan,kan@gmail.com\nkan,kan@gmail.com\nkan,kan@gmail.com\nkan,kan@gmail.com\nkan,kan@gmail.com\nkan,kan@gmail.com\nkan,kan@gmail.com\nkan,kan@gmail.com\nkan,kan@gmail.com\nkan,kan@gmail.com\nkan,kan@gmail.com\nkan,kan@gmail.com\n";
//           
//           
//           //data += "hello"; 
//            mOutput.write(data.getBytes());
//            mOutput.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        
		
		//get current userid
		SharedPreferences pref = this.getSharedPreferences("MyPref", 0); 	
		int userid = pref.getInt("currentUserid", 0);
		String useridString =userid+"";
		System.out.println("current user id:"+ userid );
		//initial a request for all new message
		new HttpTask().execute(useridString);
		

	
		
	}	

	
	
	public final class HttpTask extends
    AsyncTask<String/* Param */, Boolean /* Progress */, String /* Result */> {
	
		BasicHttpClient httpClient = new BasicHttpClient("http://158.182.6.176:9090");	
		
		@Override
		protected String doInBackground(String... params) {
		    publishProgress(true);
		    // Do the usual httpclient thing to get the result
		    String userid=null;
		    for( int i = 0; i < params.length; i++){
		    	if(i==0)
		    	{
		    		userid = params[0];
		    	}
		    }
		    ParameterMap paramsMap = httpClient.newParams()
		            .add("continue", "/")
		            .add("userid", String.valueOf(userid))
		            .add("action", "receivenewmessage");
		    
		    httpClient.addHeader("someheader", "value");
		    httpClient.setConnectionTimeout(5000); // 5s
		    HttpResponse httpResponse = httpClient.post("/skymsg/MessageController", paramsMap);
		   // System.out.println(httpResponse.getBodyAsString());	
		    return httpResponse.getBodyAsString();
		}  
		@Override
		protected void onProgressUpdate(Boolean... progress) {
		    // line below coupled with 
		    //    getWindow().requestFeature(Window.FEATURE_INDETERMINATE_PROGRESS) 
		    //    before setContentView 
		    // will show the wait animation on the top-right corner
	
		}
		
		@Override
		protected void onPostExecute(String result) {
		    publishProgress(false);
		    // Do something with result in your activity
		   // List<String> messageList = new ArrayList<String>(); 
		    ArrayList<MessageRecord> messageRecords = new ArrayList<MessageRecord>();
		    
		    System.out.println(result);
		    try {
		    	
		   
		    	JSONArray array = new JSONArray(result);
		    	for (int i = 0; i < array.length(); i++) {
		    	    JSONObject row = array.getJSONObject(i);
		    	    
		    	    MessageRecord messageRecord = new MessageRecord();
		    	    messageRecord.setPublicMsg(false);
		    	       	    

		    	    messageRecord.setMessageid(Integer.parseInt(row.getString("messageid")));
		    	    messageRecord.setBody(row.getString("body"));
		    	    if(Boolean.parseBoolean(row.getString("revealed"))==false){
		    	    	if(row.isNull("address")==false){
		    	    		messageRecord.setBody("This message is hidden until you go to the address!");
		    	    	
		    	    	}else{
		    	    		messageRecord.setBody("This message is hidden until the reveal date!");
		    	    	}
		    	    
		    	    }
		    	    
		    	    
		    	    messageRecord.setSenderid(Integer.parseInt(row.getString("senderid")));
		    	    
		    	    //in case of receiving message
		    	    if(row.isNull("sendername")==false){
		    	    	messageRecord.setSenderName(row.getString("sendername"));
		    	    }
		    	    
		    	    //in case of receiving message
		    	    if(row.isNull("senderemail")==false){
		    	    	messageRecord.setSenderEmail(row.getString("senderemail"));
		    	    }
		    	    
		    	    if(row.isNull("receiverid")==false){
		    	    	messageRecord.setReceiverid(Integer.parseInt(row.getString("receiverid")));
		    	    }
		    	    
		    	    if(row.isNull("address")==false){
		    	    	 messageRecord.setAddress(row.getString("address"));

		    	    }	    	   

		    	    if(row.isNull("distance")==false){

			    	     messageRecord.setDistance(Float.parseFloat(row.getString("distance")));
		    	    }
		    	    if(row.isNull("latitude")==false){
			    	    messageRecord.setLatitude(row.getString("latitude"));
			    	    messageRecord.setLongitude(row.getString("longitude"));	    	    	
		    	    	
			    	  //  messageList.add("Latitude: " ++" "+"Longitude: " +);		    	    	
		    	    
		    	    }
		    	   
		    	    if(row.isNull("speed")==false){
		    	    	messageRecord.setMovable(true);
		    	    	messageRecord.setInitalSpeed(Double.parseDouble(row.getString("speed")));
		    	    	messageRecord.setHeading(row.getString("heading"));
		    	    }
		    	    
		    	    if(row.isNull("image")==false)
		    	    {
		    	    	
		    	    	messageRecord.setImage(row.getString("image"));
		    	    	if(Boolean.parseBoolean(row.getString("revealed"))==true){
			    	    	String path = Environment.getExternalStorageDirectory().toString();
			    	    	new File(path+"/DCIM/Skymsg/").mkdirs();
		                    File file = new File(path, "/DCIM/Skymsg/"+row.getString("messageid")+".jpg");		    	    	
			    	    	byte[] decodeImage = Base64.decode(row.getString("image"), Base64.DEFAULT);
			    	    	Bitmap bmp=BitmapFactory.decodeByteArray(decodeImage,0,decodeImage.length);
			    	    	
			    	    	       FileOutputStream out = new FileOutputStream(file);
			    	    	       bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);		    	    		
		    	    	}

		    	    
		    	    }		    	    
		    	    
		    	    if(row.isNull("notifyDate")==false){
		    	    	messageRecord.setNotifyDate(row.getString("notifyDate"));
		    	    }
		    	    if(row.isNull("revealDate")==false){
		    	    	messageRecord.setRevealDate(row.getString("revealDate"));
		    	    	messageRecord.setRevealed(true);
		    	    }

		    	    if(row.isNull("createDate")==false){
		    	    	messageRecord.setCreateDate(row.getString("createDate"));
		    	    }
		    	    messageRecord.setRevealed(Boolean.parseBoolean(row.getString("revealed")));
		    	    if(row.getString("publicmsg").equals("true")){
		    	    	messageRecord.setPublicMsg(true);
		    	    }		    	    
		    	    messageRecords.add(messageRecord);
		    	
		    	}		    	

				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    

	    

	       
			
			ListView listView = (ListView) findViewById(R.id.MessageListViewId);
			if(messageRecords!=null)
			{
				//reverse the list
			
				Collections.sort(messageRecords);
				listView.setAdapter(new MessageItemAdapter(getApplicationContext(), R.layout.message_list_item, messageRecords) );	
				
				View empty = findViewById(android.R.id.empty);
				((LinearLayout)empty.getParent()).removeView(empty);			
			}		    	           
		    
		}		
	} 
	
}

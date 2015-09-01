package com.terence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.turbomanage.httpclient.BasicHttpClient;
import com.turbomanage.httpclient.HttpResponse;
import com.turbomanage.httpclient.ParameterMap;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.LinearLayout;

public class FriendListActivity extends Activity {

    private static final String FILENAME = "friendList.txt";
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend_list);
		
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
        
		
		
		ArrayList<FriendRecord> items = null;
		items = readRecord();
    

       
		
		ListView listView = (ListView) findViewById(R.id.ListViewId);
		if(items!=null)
		{
			listView.setAdapter(new UserItemAdapter(this, R.layout.friend_list_item, items) );	
			
			View empty = findViewById(android.R.id.empty);
			((LinearLayout)empty.getParent()).removeView(empty);			
		}
	
		
	}
	
	private static final int GETFRIEND=1;
	public void addFriendByImportPhoneContact(View v){
		
		Intent intent = new Intent();   
		intent.setClass(getApplicationContext(), ImportContactActivity.class);
		startActivityForResult(intent,GETFRIEND);
	}
	
	//read current record in file
	public ArrayList<FriendRecord> readRecord(){
		
		ArrayList<FriendRecord> items = null;
        //Read the created file and display to the screen
        try {
        
           //open the friend list
           FileInputStream mInput = openFileInput(FILENAME);
           
           //use scanner to read the file line by line
           Scanner sc = new Scanner(mInput);
           
           //String to store it
           String raw = "";
           
           //append all lines in a single String with \n
           while(sc.hasNext())
           {
        	   raw +=sc.nextLine()+"\n";
           }
          
           mInput.close();

           //parse the raw String
           items = parse(raw.trim());
               
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }		
		return items;
		
	}
	
	//check whether a single record already exist in a line
	public Boolean readRecord(String email){	
        //Read the created file and display to the screen
        try {      
           //open the friend list
           FileInputStream mInput = openFileInput(FILENAME);      
           //use scanner to read the file line by line
           Scanner sc = new Scanner(mInput);         
           //while there is still remain lines to parse
           while(sc.hasNext()){  	   
        	   if(sc.nextLine().contains(email))
        	   {
        		   return true;
        	   }
           }           
    
           mInput.close();
          
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }		
		
		return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	super.onActivityResult(requestCode, resultCode, data);

	switch(requestCode){
	case GETFRIEND:
		ArrayList<String> emails = data.getExtras().getStringArrayList("emails");
		String emailsString =null;
		for(String e : emails){
		    //http request for the user exist
		    if(readRecord(e)==false)
		    {
		    	if(e!=null){
		    		if(emailsString==null){
		    			emailsString+=e;
		    		}else{
		    			emailsString+= ","+ e;
		    		}
		    		
		    	}
		    		
		    }else{
			    Toast.makeText(FriendListActivity.this, 
			    		e+" Email aleady added", Toast.LENGTH_SHORT).show();	    	
		    }				
		}
		if(emailsString!=null){
			
			new  CheckFriendExistBatchHttpTask().execute(emailsString);
		    Toast.makeText(FriendListActivity.this, 
		    		"Searching...", Toast.LENGTH_SHORT).show();	  
		}
			
	}
	}
	
	//when the add btn is clicked....
	public void addFriendByEmail(View view){
		
		EditText addFriendByEmailTv = (EditText) findViewById(R.id.addFriendByEmailET);

	    
	    //http request for the user exist
	    if(readRecord(addFriendByEmailTv.getText().toString())==false)
	    {
	    	if(addFriendByEmailTv.getText().toString()!=null)
		    	new CheckFriendExistHttpTask().execute(addFriendByEmailTv.getText().toString());
	    	
		    Toast.makeText(FriendListActivity.this, 
		    		"Searching...", Toast.LENGTH_LONG).show();	    	
	    }else{
		    Toast.makeText(FriendListActivity.this, 
		    		"User aleady added", Toast.LENGTH_LONG).show();	    	
	    }
	   
	}
	
	public void inviteFriend(View view){
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("message/rfc822");
		intent.putExtra(Intent.EXTRA_EMAIL, "emailaddress@emailaddress.com");
		intent.putExtra(Intent.EXTRA_SUBJECT, "Join Message Capsule!");
		intent.putExtra(Intent.EXTRA_TEXT, "Download Message Capsule from here http://158.182.6.176:9090/skymsg/asset/messagecapsule.apk");

		startActivity(Intent.createChooser(intent, "Send Email"));		
	}
	
	public void deleteList(View view){
		deleteFile(FILENAME);
	}
    
		/* Simple CSV Parser */
	   private static final int COL_NAME = 0;
	   private static final int COL_EMAIL = 1;
	
	   private ArrayList<FriendRecord> parse(String raw) {
	        ArrayList<FriendRecord> results = new ArrayList<FriendRecord>();
	        FriendRecord current = null;
	
	        StringTokenizer st = new StringTokenizer(raw,",\n");
	   int state = COL_NAME;
	   while(st.hasMoreTokens()) {
	        switch(state) {
	        case COL_NAME:
	           current = new FriendRecord();
	           current.username = st.nextToken();
	            state = COL_EMAIL;
	            break;
	       case COL_EMAIL:
	           current.email = st.nextToken();
	           state = COL_NAME;
	           results.add(current);
	            break;
	       }
	    }
	
	   return results;
	}	
	   
		public final class CheckFriendExistHttpTask extends
	    AsyncTask<String/* Param */, Boolean /* Progress */, String /* Result */> {
		
			BasicHttpClient httpClient = new BasicHttpClient("http://158.182.6.176:9090");	
			
			@Override
			protected String doInBackground(String... params) {
			    publishProgress(true);
			    // Do the usual httpclient thing to get the result
			    String email=null;
			    for( int i = 0; i < params.length; i++){
			    	if(i==0)
			    	{
			    		email = params[0];
			    	}
			    }
			    ParameterMap paramsMap = httpClient.newParams()
			            .add("continue", "/")
			            .add("email", String.valueOf(email))
			            .add("action", "checkfriendexist");
			    
			   
			    httpClient.setConnectionTimeout(5000); // 2s
			    HttpResponse httpResponse = httpClient.post("/skymsg/UserController", paramsMap);
			    System.out.println(httpResponse.getBodyAsString());	
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
		    
			    String firstName =null;
			    String lastName = null;
			    String email = null;
			    System.out.println(result);
			    try {
			    	
			    		JSONObject user = new JSONObject(result);
			    	    firstName = user.getString("firstName");
			    	    lastName = user.getString("lastName");
			    	    email = user.getString("email");    	
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    
			    if(email!=null){
			    	
			        //Create a new file and write some data
			        try {
			           FileOutputStream mOutput = openFileOutput(FILENAME, Activity.MODE_APPEND);
			          
			           String data = firstName+lastName+","+email+"\n";
			           
			           
			           //data += "hello"; 
			            mOutput.write(data.getBytes());
			            mOutput.close();
			        } catch (FileNotFoundException e) {
			            e.printStackTrace();
			        } catch (IOException e) {
			            e.printStackTrace();
			        }
			        
					ArrayList<FriendRecord> items = null;
					items = readRecord();
					
					ListView listView = (ListView) findViewById(R.id.ListViewId);
					if(items!=null)
					listView.setAdapter(new UserItemAdapter(getApplicationContext(), R.layout.friend_list_item, items) );
			    	
					View empty = findViewById(android.R.id.empty);
					
					if(empty!=null)
						((LinearLayout)empty.getParent()).removeView(empty);		

			    }else{
			    Toast.makeText(FriendListActivity.this, 
			    		"User not exist in system", Toast.LENGTH_LONG).show();
			    }
			}		
		}
		
		public final class CheckFriendExistBatchHttpTask extends
	    AsyncTask<String/* Param */, Boolean /* Progress */, String /* Result */> {
		
			BasicHttpClient httpClient = new BasicHttpClient("http://158.182.6.176:9090");	
			
			@Override
			protected String doInBackground(String... params) {
			    publishProgress(true);
			    // Do the usual httpclient thing to get the result
			    String email=null;
			    for( int i = 0; i < params.length; i++){
			    	if(i==0)
			    	{
			    		email = params[0];
			    	}
			    }
			    ParameterMap paramsMap = httpClient.newParams()
			            .add("continue", "/")
			            .add("email", String.valueOf(email))
			            .add("action", "checkfriendexistbatch");
			    
			   
			    httpClient.setConnectionTimeout(5000); // 2s
			    HttpResponse httpResponse = httpClient.post("/skymsg/UserController", paramsMap);
			    System.out.println(httpResponse.getBodyAsString());	
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
			   			    
			    System.out.println(result);
			    try {
		    	    
				    	JSONArray array = new JSONArray(result);
				    	for (int i = 0; i < array.length(); i++) {
				    	    JSONObject row = array.getJSONObject(i);
				    	    
				    	   
					        //Create a new file and write some data
					        try {
					          
					          
					           if(row.isNull("email")==false && row.isNull("lastName")==false && row.isNull("firstName")==false ){
					        	   FileOutputStream mOutput = openFileOutput(FILENAME, Activity.MODE_APPEND);
						           String data =  row.getString("firstName")+row.getString("lastName")+","+row.getString("email")+"\n";				           
						           
						           //data += "hello"; 
						            mOutput.write(data.getBytes());
						            mOutput.close();					        	   
					           }

					        } catch (FileNotFoundException e) {
					            e.printStackTrace();
					        } catch (IOException e) {
					            e.printStackTrace();
					        }
					        
					        
					        //read and refresh the friend list
							ArrayList<FriendRecord> items = null;
							items = readRecord();
							
							ListView listView = (ListView) findViewById(R.id.ListViewId);
							if(items!=null)
							listView.setAdapter(new UserItemAdapter(getApplicationContext(), R.layout.friend_list_item, items) );
					    	
							View empty = findViewById(android.R.id.empty);
							
							if(empty!=null)
								((LinearLayout)empty.getParent()).removeView(empty);		
		    	
				    	}				    	    
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    

			}		
		}  		
}

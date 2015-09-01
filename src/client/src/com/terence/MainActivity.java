package com.terence;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gcm.GCMRegistrar;
import com.terence.LoginActivity.HttpTask;
import com.turbomanage.httpclient.BasicHttpClient;
import com.turbomanage.httpclient.HttpResponse;
import com.turbomanage.httpclient.ParameterMap;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.LocalActivityManager;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.Toast;
import android.content.SharedPreferences;

public class MainActivity extends TabActivity{

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.activity_main, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
	    switch(item.getItemId()){
	    case R.id.friend_list:
	    	Intent launchNewIntent = new Intent(MainActivity.this,FriendListActivity.class);
	    	startActivity(launchNewIntent);
	        return true;
	    case R.id.sent_message:
	    	launchNewIntent = new Intent(MainActivity.this,SentMessageActivity.class);
	    	startActivity(launchNewIntent);
	        return true;
	    case R.id.stat:
	    	launchNewIntent = new Intent(MainActivity.this,StatActivity.class);
	    	startActivity(launchNewIntent);
	        return true;
	    case R.id.menu_settings:
	    	launchNewIntent = new Intent(MainActivity.this,FriendListActivity.class);
	    	startActivity(launchNewIntent);
	        return true;
	    case R.id.logout:
	    	SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
	    	Editor editor = pref.edit();
	    	editor.remove("login");
	    	editor.commit();
	    	finish();
	        return true;	        
	    }
           
	   
	    return false;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
//		setContentView(R.layout.activity_main);
		
		TabHost tabHost = this.getTabHost();

		
        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("Send").setContent(
                new Intent(this, SendMessageActivity.class)));
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("Received").setContent(
                new Intent(this, MessageReceived.class)));
        tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("Receive").setContent(
                new Intent(this, MessageReceiveForLocationActivity.class)));



		
//		Button sendMessageBtn = (Button) findViewById(R.id.sendMessageBtn);
//		sendMessageBtn.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//            	// Perform action on click
//		    	Intent goToNextActivity = new Intent(getApplicationContext(), SendMessageActivity.class);
//		    	startActivity(goToNextActivity);
//            }
//        });	
//
//		
//		Button receiveMessageBtn = (Button) findViewById(R.id.receiveMessageBtn);
//		receiveMessageBtn.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//            	// Perform action on click
//		    	Intent goToNextActivity = new Intent(getApplicationContext(), MessageReceiveForLocationActivity.class);
//		    	startActivity(goToNextActivity);
//            }
//        });		
//		
//		Button receivedMessageBtn = (Button) findViewById(R.id.receivedMessageBtn);
//		receivedMessageBtn.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//            	// Perform action on click
//		    	Intent goToNextActivity = new Intent(getApplicationContext(), MessageReceived.class);
//		    	startActivity(goToNextActivity);
//            }
//        });			
//		
//		Button settingsBtn = (Button) findViewById(R.id.settingsBtn);
//		settingsBtn.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//            	// Perform action on click
//		    	Intent goToNextActivity = new Intent(getApplicationContext(), SettingsActivity.class);
//		    	startActivity(goToNextActivity);
//            }
//        });		
//
//		Button friendListBtn = (Button) findViewById(R.id.friendListBtn);
//		friendListBtn.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//            	// Perform action on click
//		    	Intent goToNextActivity = new Intent(getApplicationContext(), FriendListActivity.class);
//		    	startActivity(goToNextActivity);
//            }
//        });		
		
		GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);
		String regId = GCMRegistrar.getRegistrationId(this);
		if (regId.equals("")) {
		  GCMRegistrar.register(this, "40015296375");
		  regId = GCMRegistrar.getRegistrationId(this);
		} else {
			  //String TAG = null;
			//Log.v(TAG, "Already registered");
		}
		System.out.println(regId);
		
		Bundle bData = this.getIntent().getExtras();
		String userEmailString = bData.getString("email");
		String userPasswordString = bData.getString("password");
		int useridString = bData.getInt("userid");
		
		SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode

		//no one has ever login in this phone
		if(pref.getString("currentUserEmail", null)==null)
		{
			Editor editor = pref.edit();
			editor.putString("currentUserEmail", userEmailString);
			editor.putString("currentPasswordString", userPasswordString);
			editor.putInt("currentUserid", useridString);
			
			editor.putBoolean("login", true);
			editor.commit();
			new HttpTask().execute(userEmailString,userPasswordString,regId);
		}
		else if(pref.getString("currentUserEmail", null).equals(userEmailString)) //someone has logined in this phone, who is the first user.
		{
			Editor editor = pref.edit();
			editor.putInt("currentUserid", useridString);
			editor.putString("currentPasswordString", userPasswordString);
			editor.putBoolean("login", true);
			editor.commit();
			new HttpTask().execute(userEmailString,userPasswordString,regId);
		}else{ //someone has logined in this phone, who is not the first user.
			
		    Context context = getApplicationContext();
		    int duration = Toast.LENGTH_SHORT;

		    CharSequence text = "Sorry, one account for one particular phone only. If you wish to use this account on this phone, let previous user signin in this phone again and click erase account";


		    Toast toast = Toast.makeText(context, text, duration);
		    toast.show();
		}
		

		
	}
	public final class HttpTask extends
    AsyncTask<String/* Param */, Boolean /* Progress */, String /* Result */> {
	
		BasicHttpClient httpClient = new BasicHttpClient("http://158.182.6.176:9090");
		
		@Override
		protected String doInBackground(String... params) {
		    publishProgress(true);
		    // Do the usual httpclient thing to get the result
		    String userEmail=null;
		    String password=null;
		    String regID=null;
		    for( int i = 0; i < params.length; i++){
		    	if(i==0)
		    	{
		    		userEmail = params[0];
		    	}
		    	if(i==1)
		    	{
		    		password = params[1];
		    	}
		    	if(i==2)
		    	{
		    		regID = params[2];
		    	}
		    }
		    ParameterMap paramsMap = httpClient.newParams()
		            .add("continue", "/")
		            .add("email", userEmail)
		            .add("password", password)
		            .add("gcmregid", regID)
		            .add("action", "updateGCMregid");
		    httpClient.addHeader("someheader", "value");
		    httpClient.setConnectionTimeout(10000); // 2s
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
		    MainActivity.this.setProgressBarIndeterminateVisibility(progress[0]);
		}
		
		@Override
		protected void onPostExecute(String result) {
		    publishProgress(false);
		    // Do something with result in your activity
		    Context context = getApplicationContext();
		    int duration = Toast.LENGTH_SHORT;

		    CharSequence text = result;


		    Toast toast = Toast.makeText(context, text, duration);
		    toast.show();
		    
		    
		    System.out.println(result);

		}
	
	}
	

}
//import android.support.v4.app.FragmentActivity;
//import android.support.v4.app.FragmentTabHost;
//
///**
// * This demonstrates how you can implement switching between the tabs of a
// * TabHost through fragments, using FragmentTabHost.
// */
//public class MainActivity extends FragmentActivity {
//    private FragmentTabHost mTabHost;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        setContentView(R.layout.activity_main);
//        mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
//        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
//
//        mTabHost.addTab(mTabHost.newTabSpec("simple").setIndicator("Simple"),
//        		LoaderCursorSupport.CursorLoaderListFragment.class, null);
//        mTabHost.addTab(mTabHost.newTabSpec("contacts").setIndicator("Contacts"),
//                LoaderCursorSupport.CursorLoaderListFragment.class, null);
//        mTabHost.addTab(mTabHost.newTabSpec("custom").setIndicator("Custom"),
//        		LoaderThrottleSupport.ThrottledLoaderListFragment.class, null);
//        mTabHost.addTab(mTabHost.newTabSpec("throttle").setIndicator("Throttle"),
//                LoaderThrottleSupport.ThrottledLoaderListFragment.class, null);
//    }
//}

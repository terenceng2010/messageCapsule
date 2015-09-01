package com.terence;



import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.turbomanage.httpclient.BasicHttpClient;
import com.turbomanage.httpclient.HttpResponse;
import com.turbomanage.httpclient.ParameterMap;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.json.*;
public class LoginActivity extends Activity {
	EditText userEmail;
	EditText userPassword;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_login);
		
    	//create the editText field
		SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
		String currentUserEmail = pref.getString("currentUserEmail", "");
		
		userEmail = (EditText)findViewById(R.id.emailedTV);
		userEmail.setText(currentUserEmail);
		
		//login cookie, if already logged in
		if(pref.getBoolean("login", false)){
			
		   Intent goToNextActivity = new Intent(getApplicationContext(), MainActivity.class);
            //new�@��Bundle����A�ñN�n�ǻ�����ƶǤJ
           Bundle bundle = new Bundle();
           bundle.putString("email",pref.getString("currentUserEmail", null));
           bundle.putString("password",pref.getString("currentUserPassword", null));
           bundle.putInt("userid", pref.getInt("currentUserid", 0));
             //�NBundle����assign��intent
           goToNextActivity.putExtras(bundle);		    	
           startActivity(goToNextActivity);		
		}
		
		userPassword  = (EditText)findViewById(R.id.passwordedTV);
        final Button loginBtn = (Button) findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	// Perform action on click
            	

            	//grab the content to fields and convert them to string
            	String userEmailString = userEmail.getText().toString();
            	String userPasswordString = userPassword.getText().toString();
            	
            	new HttpTask().execute(userEmailString,userPasswordString);
            }
        });
        
        final Button registerActBtn = (Button) findViewById(R.id.registerActBtn);
        registerActBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	// Perform action on click
		    	Intent goToNextActivity = new Intent(getApplicationContext(), RegisterActivity.class);
		    	startActivity(goToNextActivity);
            }
        });	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_login, menu);
		return true;
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
		    for( int i = 0; i < params.length; i++){
		    	if(i==0)
		    	{
		    		userEmail = params[0];
		    	}
		    	if(i==1)
		    	{
		    		password = params[1];
		    	}
		    }
		    ParameterMap paramsMap = httpClient.newParams()
		            .add("continue", "/")
		            .add("email", userEmail)
		            .add("password", password)
		            .add("action", "login");
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
		    LoginActivity.this.setProgressBarIndeterminateVisibility(progress[0]);
		}
		
		@Override
		protected void onPostExecute(String result) {
		    publishProgress(false);
		    // Do something with result in your activity
			Boolean validUser = false;
			int userid = 0;
		    Context context = getApplicationContext();
		    int duration = Toast.LENGTH_SHORT;


		    
		    
		    System.out.println(result);
		    try {
				JSONObject parseResult = new JSONObject(result);
				validUser = parseResult.getBoolean("validUser");
				userid = parseResult.getInt("userid");
				System.out.println(userid);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    if(validUser==true)
		    {
			    CharSequence text = "Login Success";
		    	Intent goToNextActivity = new Intent(getApplicationContext(), MainActivity.class);
	             //new�@��Bundle����A�ñN�n�ǻ�����ƶǤJ
	            Bundle bundle = new Bundle();
	            bundle.putString("email",userEmail.getText().toString());
	            bundle.putString("password", userPassword.getText().toString());
	            bundle.putInt("userid", userid);
	              //�NBundle����assign��intent
	            goToNextActivity.putExtras(bundle);		    	
		    	startActivity(goToNextActivity);
			    Toast toast = Toast.makeText(context, text, duration);
			    toast.show();
		    }
		    else
		    {
		    	CharSequence text = "Login Fail";
			    Toast toast = Toast.makeText(context, text, duration);
			    toast.show();
		    }
		}
	
	}
}

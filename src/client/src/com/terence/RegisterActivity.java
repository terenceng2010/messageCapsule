package com.terence;


import com.turbomanage.httpclient.BasicHttpClient;
import com.turbomanage.httpclient.HttpResponse;
import com.turbomanage.httpclient.ParameterMap;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends Activity {
	EditText userEmail;
	EditText userPassword;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_register);
		
        final Button loginBtn = (Button) findViewById(R.id.registerBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	// Perform action on click
            	
            	//grab the editText field
            	userEmail = (EditText)findViewById(R.id.emailedTV);
            	EditText userFirstName = (EditText)findViewById(R.id.firstNameedTV);
            	EditText userLastName = (EditText)findViewById(R.id.lastNameedTV);
            	userPassword = (EditText)findViewById(R.id.passwordedTV);
            	//convert them to string
            	String userEmailString = userEmail.getText().toString();
            	String userFirstNameString = userFirstName.getText().toString();
            	String userLastNameString = userLastName.getText().toString();
            	String userPasswordString = userPassword.getText().toString();

            	if(isValidEmail(userEmailString) && !userFirstNameString.equals("") && !userLastNameString.equals("") &&  !userPasswordString.equals("")){

            		new HttpTask().execute(userEmailString,userFirstNameString,userLastNameString,userPasswordString);            		
            	}else{
        		    int duration = Toast.LENGTH_SHORT;

        		    Toast toast = Toast.makeText(getApplicationContext(), "Email is not in correct format.", duration);
        		    toast.show();            		
            	}
            	
            }
        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_register, menu);
		return true;
	}
	
	public final static boolean isValidEmail(CharSequence target) {
	    if (target == null) {
	        return false;
	    } else {
	        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
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
		    String userFirstName=null;
		    String userLastName=null;
		    String password=null;
		    for( int i = 0; i < params.length; i++){
		    	if(i==0)
		    	{
		    		userEmail = params[0];
		    	}
		    	if(i==1)
		    	{
		    		userFirstName = params[1];
		    	}
		    	if(i==2)
		    	{
		    		userLastName = params[2];
		    	}		    	
		    	if(i==3)
		    	{
		    		password = params[3];
		    	}
		    }
		    ParameterMap paramsMap = httpClient.newParams()
		            .add("continue", "/")
		            .add("email", userEmail)
		            .add("firstName", userFirstName)
		            .add("lastName", userLastName)
		            .add("password", password)
		            .add("action", "register");
		    httpClient.addHeader("someheader", "value");
		    httpClient.setConnectionTimeout(10000); // 10s
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
			RegisterActivity.this.setProgressBarIndeterminateVisibility(progress[0]);
		}
		
		@Override
		protected void onPostExecute(String result) {
		    publishProgress(false);
		    // Do something with result in your activity
		    Context context = getApplicationContext();
		    CharSequence text = result;
		    int duration = Toast.LENGTH_SHORT;

		    Toast toast = Toast.makeText(context, text, duration);
		    toast.show();
		    
		    
		    System.out.println(result);

		    if(result.contains("Success"))
		    {
		    	
		    	Intent goToNextActivity = new Intent(getApplicationContext(), MainActivity.class);
	            Bundle bundle = new Bundle();
	            bundle.putString("email",userEmail.getText().toString());
	            bundle.putString("password", userPassword.getText().toString());
	            goToNextActivity.putExtras(bundle);
	            
		    	startActivity(goToNextActivity);
		    }
		}
	
	}
}

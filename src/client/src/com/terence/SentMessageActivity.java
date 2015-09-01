package com.terence;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class SentMessageActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sent_message);
			
		
	}

	public void sentMessage(View v){
    	Intent goToNextActivity = new Intent(getApplicationContext(), SentLocationMessageWebViewActivity.class);
    	startActivity(goToNextActivity);			
	}
	public void sentLocationMessage(View v){
    	Intent goToNextActivity = new Intent(getApplicationContext(), SentLocationMessageActivity.class);
    	startActivity(goToNextActivity);		
	}

}

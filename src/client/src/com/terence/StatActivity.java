package com.terence;

import android.os.Bundle;
import android.app.Activity;
import android.content.SharedPreferences;
import android.view.Menu;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class StatActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stat);
		WebView myWebView = (WebView) findViewById(R.id.webview);
		WebSettings webSettings = myWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
		int currentUserId = pref.getInt("currentUserid",0);
		myWebView.loadUrl("http://158.182.6.176:9090/skymsg/userStat.jsp?userid="+currentUserId);		
	}


}

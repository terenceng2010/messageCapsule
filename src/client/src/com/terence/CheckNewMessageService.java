package com.terence;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.terence.MessageReceiveForLocationActivity.receiveNearNewMessageHttpTask;
import com.turbomanage.httpclient.BasicHttpClient;
import com.turbomanage.httpclient.HttpResponse;
import com.turbomanage.httpclient.ParameterMap;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;


public class CheckNewMessageService extends IntentService  {

	public CheckNewMessageService() {
		super("CheckNewMessageService");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent arg0) {
		// TODO Auto-generated method stub
		if(isNetworkAvailable()){

			
		  	//create the location manager and listener
			final LocationManager locationManager =
			        (LocationManager) getSystemService(Context.LOCATION_SERVICE);

			final LocationListener locationListener = new LocationListener() {
				public void onLocationChanged(Location location) { //當坐標改變時觸發此函數，如果Provider傳進相同的坐標，它就不會被觸發
				// log it when the location changes
				
					System.out.println("SuperMap"+ "Location changed : Lat: "
					+ location.getLatitude() + " Lng: "
					+ location.getLongitude());
			
					BasicHttpClient httpClient = new BasicHttpClient("http://158.182.6.176:9090");
				    // Do the usual httpclient thing to get the result
					//get current userid
					SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); 	
					int userid = pref.getInt("currentUserid", 0);
					
					System.out.println("current user id:"+ userid );
					
				    String latitude=location.getLatitude()+"";
				    String longitude=location.getLongitude()+"";
				    String range="1000";

				    ParameterMap paramsMap = httpClient.newParams()
				            .add("continue", "/")
				            .add("userid", String.valueOf(userid))
				            .add("latitude",latitude)
				            .add("longitude",longitude)
				            .add("range",range)
				            .add("action", "receivenewmessagenearuser");
				    		
				    
				   // httpAbstractClient.addHeader("Content-Type", "multipart/form-data");
				    httpClient.setConnectionTimeout(5000); // 2s
				   // HttpResponse post = httpAbstractClient.post("/skymsg/MessageController","multipart/form-data", data);
				    HttpResponse httpResponse = httpClient.post("/skymsg/MessageController", paramsMap);
  
					stopSelf();
				}

				public void onProviderDisabled(String provider) {
				// Provider被disable時觸發此函數，比如GPS被關閉
				}

				public void onProviderEnabled(String provider) {
				// Provider被enable時觸發此函數，比如GPS被打開
				}

				public void onStatusChanged(String provider, int status, Bundle extras) {
				// Provider的轉態在可用、暫時不可用和無服務三個狀態直接切換時觸發此函數
				}
				}; 
				
	        	locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
	        			1000, 10, locationListener);  			
		}
	}

	private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	         = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null;
	}

}

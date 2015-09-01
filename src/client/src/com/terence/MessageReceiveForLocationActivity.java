package com.terence;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.turbomanage.httpclient.AbstractHttpClient;
import com.turbomanage.httpclient.BasicHttpClient;
import com.turbomanage.httpclient.HttpResponse;
import com.turbomanage.httpclient.ParameterMap;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import android.widget.Toast;

public class MessageReceiveForLocationActivity extends ListActivity {
	byte[] addressByte = null  ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message_receive_for_location);
		
		final EditText rangeEditText = (EditText) findViewById(R.id.searchRange);
		rangeEditText.setText("100");
		final Button rangeSetBtn = (Button) findViewById(R.id.searchRangeBtn);


		//initial a request for all new message
    	

    	//create the location manager and listener
		final LocationManager locationManager =
		        (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		final LocationListener locationListener = new LocationListener() {
			public void onLocationChanged(Location location) { //當坐標改變時觸發此函數，如果Provider傳進相同的坐標，它就不會被觸發
			// log it when the location changes
			
				System.out.println("SuperMap"+ "Location changed : Lat: "
				+ location.getLatitude() + " Lng: "
				+ location.getLongitude());
	
				//latitude.setText(location.getLatitude()+"");
				//longtiude.setText(location.getLongitude()+"");
				
//				Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
//				try {
//					List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
//					
//					
//					if (addresses != null && addresses.size() > 0) {
//			            Address geocoderAddress = addresses.get(0);
//			            for(int i=0;i<addresses.size();i++)
//			            {
//			            	System.out.println(addresses.get(i).toString());
//			            }
//			            // Format the first line of address (if available), city, and country name.
//			            String addressText = String.format("%s, %s, %s",
//			            		geocoderAddress.getMaxAddressLineIndex() > 0 ? geocoderAddress.getAddressLine(0)+geocoderAddress.getAddressLine(1) : "",			            				
//			            				geocoderAddress.getLocality(),
//			            				geocoderAddress.getCountryName());
//			            // Update the UI via a message handler.
//			            address.setText(addressText);
//			        }			
//					
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				new receiveNearNewMessageHttpTask().execute(location.getLatitude()+"",location.getLongitude()+"",rangeEditText.getText().toString());    
			
			
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

        	
			rangeSetBtn.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	            	// Perform action on click
	            	Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
	            	new receiveNearNewMessageHttpTask().execute(lastKnownLocation.getLatitude()+"",lastKnownLocation.getLongitude()+"",rangeEditText.getText().toString());    
	            }
	        });	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_message_receive_for_location,
				menu);
		return true;
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
	// TODO Auto-generated method stub
	super.onListItemClick(l, v, position, id);
	 
	 String selection = l.getItemAtPosition(position).toString();
	 if(selection.contains("Latitude:"))
	 {
		 String latitudeWithText = selection.substring(selection.lastIndexOf("Latitude:"), selection.indexOf("Longitude:")).trim();
		 
		 String longitudeWithText = selection.substring(selection.lastIndexOf("Longitude:")).trim();
		 
		 String latitude= latitudeWithText.substring(10);
	     String longitude=longitudeWithText.substring(11);
		 System.out.println(latitude);
		 System.out.println(longitude);
		 Toast.makeText(this, selection, Toast.LENGTH_LONG).show();
		 
		 Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:" + latitude + "," + longitude + "?z=17&q=" + latitude + "," + longitude));
		 startActivity(intent);
	 }
	}
	
	public final class receiveNearNewMessageHttpTask extends
    AsyncTask<String/* Param */, Boolean /* Progress */, String /* Result */> {
	
		BasicHttpClient httpClient = new BasicHttpClient("http://158.182.6.176:9090");
		AbstractHttpClient httpAbstractClient = new BasicHttpClient("http://158.182.6.176:9090");
		private byte[] data;	
		
		@Override
		protected String doInBackground(String... params) {
		    publishProgress(true);
		    // Do the usual httpclient thing to get the result
			//get current userid
			SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); 	
			int userid = pref.getInt("currentUserid", 0);
			
			System.out.println("current user id:"+ userid );
			
		    String latitude=null;
		    String longitude=null;
		    String range=null;
		    for( int i = 0; i < params.length; i++){
		    	if(i==0)
		    	{
		    		latitude = params[0];
		    	}
		    	
		    	if(i==1)
		    	{
		    		longitude = params[1];
		    	}
		    	
		    	if(i==2)
		    	{
		    		range = params[2];
		    	}		    	
		    }
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
		    addressByte = httpResponse.getBody();	
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
		    List<String> messageList = new ArrayList<String>(); 

		  // System.out.println(result);
		    try {
		    	
		   
		    	JSONArray array = new JSONArray(result);
		    	for (int i = 0; i < array.length(); i++) {
		    	    JSONObject row = array.getJSONObject(i);
		    	    messageList.add("Message id: " + row.getString("messageid"));
		    	    messageList.add("Sender id: " + row.getString("senderid"));
		    	    messageList.add(URLDecoder.decode(row.getString("body"), "UTF-8"));
		    	    messageList.add("Location: "+URLDecoder.decode(row.getString("address"), "UTF-8"));
		    	    messageList.add("Distance(meter): "+row.getString("distance")); 
		    	    messageList.add("Latitude: " +row.getString("latitude")+" "+"Longitude: " +row.getString("longitude"));
		    	   
		    	}		    	

				
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
	        String[] values =  messageList.toArray(new String[messageList.size()]);
	        for(int i=0;i<values.length;i++){
	        	System.out.println(values[i]);
	        }
	            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
	              R.layout.location_message_textview, values);
	            
	            setListAdapter(adapter);	    
	            
		    
		}		
	}
	
	public void goArView(View v){
		Intent goArViewIntent = new Intent(getApplicationContext(), MessageReceivedForLocationInArViewActivity.class);  
		goArViewIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		getApplicationContext().startActivity(goArViewIntent);		
	}
}

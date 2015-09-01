package com.terence;


import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.json.JSONException;
import org.json.JSONObject;

import com.terence.LoginActivity.HttpTask;
import com.turbomanage.httpclient.AbstractHttpClient;
import com.turbomanage.httpclient.BasicHttpClient;
import com.turbomanage.httpclient.HttpResponse;
import com.turbomanage.httpclient.ParameterMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;


public class SendMessageActivity extends Activity {
    private EditText recipientEmail;
	private EditText messageBody;
	private TextView latitude, longtiude, address;
	private TextView speed, heading, seekBarValue;
	private Button publicMsgToggleBtn,movableMessageToggleBtn,temperatureMsgToggleBtn,weatherToggleBtn;
	
	private RadioGroup radioSexGroup;
	private RadioButton radioSexButton;
	private SeekBar seekBar;
	private ListAdapter weatherAdapter;
	private String weather_condition_value;
	String encodedImage=null;
	private final static int GETMOVABLEINFO=1;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_send_message);
        
        recipientEmail = (EditText)findViewById(R.id.recipientEmailET);
		messageBody = (EditText)findViewById(R.id.bodyET);
		
		if(getIntent() != null){
			Bundle recipientEmailBundle = getIntent().getExtras();		
			if(recipientEmailBundle != null){
				if(recipientEmailBundle.getString("email")!= null){
					recipientEmail.setText(recipientEmailBundle.getString("email"));
				}					
			}
		}

        final LinearLayout mainLayout = (LinearLayout)findViewById(R.id.sendMessageLinearLayout);
        
        final Button dateToggleBtn = (Button)findViewById(R.id.dateToggleButton);
        final Button locationToggleBtn = (Button)findViewById(R.id.locationToggleButton);
        weatherToggleBtn = (Button)findViewById(R.id.weatherToggleButton);
        Button sendMessageBtn = (Button) findViewById(R.id.sendMessageBtnReal);
        //^--essential layout
       
        final View dateView = getLayoutInflater().inflate(R.layout.send_message_date_related_items, mainLayout,false);
        final View locationView = getLayoutInflater().inflate(R.layout.send_message_location_related_items, mainLayout,false);
        final View weatherView = getLayoutInflater().inflate(R.layout.send_message_weather_related_items, mainLayout,false);
		
    	latitude = (TextView)locationView.findViewById(R.id.latitudeTV);
    	longtiude = (TextView)locationView.findViewById(R.id.longtiudeTV); 
    	address = (TextView)locationView.findViewById(R.id.addressTV); 
    	speed = (TextView)locationView.findViewById(R.id.SpeedTV); 
    	heading = (TextView)locationView.findViewById(R.id.HeadingTV); 
    	
    	movableMessageToggleBtn = (Button) locationView.findViewById(R.id.movableMessageToggleBtn);
    	publicMsgToggleBtn = (Button) locationView.findViewById(R.id.publicMsgToggleBtn);

        
    	final DatePicker messageNotifyDatePicker = (DatePicker) dateView.findViewById(R.id.messageNotifyDatePicker);
    	final TimePicker messageNotifyTimePicker = (TimePicker) dateView.findViewById(R.id.messageNotifyTimePicker);
        final Spinner notifyDatetimeZoneItems = (Spinner)dateView.findViewById(R.id.messageNotifyTimeZoneSpinner);
        final CheckBox messageNotifyCheckBox =(CheckBox)dateView.findViewById(R.id.messageNotifyCheckBox);
    	final DatePicker messageRevealDatePicker = (DatePicker) dateView.findViewById(R.id.messageRevealDatePicker);
    	final TimePicker messageRevealTimePicker = (TimePicker) dateView.findViewById(R.id.messageRevealTimePicker);    	
        final Spinner revealDatetimeZoneItems = (Spinner)dateView.findViewById(R.id.messageRevealTimeZoneSpinner);
    	
       
        imageView = (ImageView) findViewById(R.id.selectImageIV);
        
        temperatureMsgToggleBtn = (Button) weatherView.findViewById(R.id.temperatureMsgToggleBtn);
        seekBar = (SeekBar)weatherView.findViewById(R.id.seekBar1);
        seekBarValue = (TextView)weatherView.findViewById(R.id.seekbarvalue);
        
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

		   @Override
		   public void onProgressChanged(SeekBar seekBar, int progress,
		     boolean fromUser) {
		    // TODO Auto-generated method stub
		    seekBarValue.setText(String.valueOf(progress));
		   }
		
		   @Override
		   public void onStartTrackingTouch(SeekBar seekBar) {
		    // TODO Auto-generated method stub
		   }
		
		   @Override
		   public void onStopTrackingTouch(SeekBar seekBar) {
		    // TODO Auto-generated method stub
		   }
        }); 
        

        
    	//Read all timezones to spinners
    	ArrayAdapter <CharSequence> timeZoneadapter =
    	          new ArrayAdapter <CharSequence> (this, android.R.layout.simple_spinner_item );
    	timeZoneadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	    String[]TZ = TimeZone.getAvailableIDs();
    	    ArrayList<String> TZ1 = new ArrayList<String>();
    	    for(int i = 0; i < TZ.length; i++) {
    	        if(!(TZ1.contains(TimeZone.getTimeZone(TZ[i]).getDisplayName()))) {
    	            TZ1.add(TimeZone.getTimeZone(TZ[i]).getDisplayName());
    	        }
    	    }
    	    for(int i = 0; i < TZ1.size(); i++) {
    	    	timeZoneadapter.add(TZ1.get(i));
    	    }
    	    
    	    notifyDatetimeZoneItems.setAdapter(timeZoneadapter);
    	    for(int i = 0; i < TZ1.size(); i++) {
    	    	
    	    	//display user current timezone
    	        if(TZ1.get(i).equals(TimeZone.getDefault().getDisplayName())) {
    	        	notifyDatetimeZoneItems.setSelection(i);
    	        }
    	    }   	
    	    
    	    revealDatetimeZoneItems.setAdapter(timeZoneadapter);
    	    for(int i = 0; i < TZ1.size(); i++) {
    	        if(TZ1.get(i).equals(TimeZone.getDefault().getDisplayName())) {
    	        	revealDatetimeZoneItems.setSelection(i);
    	        }
    	    }
    	
//    	//weather condition spinner
//        Spinner spinner_items = (Spinner)weatherView.findViewById(R.id.spinner1);
//
//		//從res/values/string.xml讀取資料到Spinner
//		ArrayAdapter< CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.wetherConditionItems,android.R.layout.simple_spinner_item);
//		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		spinner_items.setAdapter(adapter);
				
		final WeatherConditionItem[] weatherConditionItems = {
			    new WeatherConditionItem("Sky is clear", R.drawable.w1),
			    new WeatherConditionItem("Few clouds", R.drawable.w2),
				new WeatherConditionItem("Scattered clouds", R.drawable.w3),
				new WeatherConditionItem("Broken clouds", R.drawable.w4),
				new WeatherConditionItem("Shower rain", R.drawable.w5),
				new WeatherConditionItem("Rain", R.drawable.w6),
				new WeatherConditionItem("Thunderstorm", R.drawable.w7),
				new WeatherConditionItem("Snow", R.drawable.w8),
				new WeatherConditionItem("Mist", R.drawable.w9),
				new WeatherConditionItem("No specific weather condition", R.drawable.w1),
			};

			weatherAdapter = new ArrayAdapter<WeatherConditionItem>(
			    this,
			    android.R.layout.select_dialog_item,
			    android.R.id.text1,
			    weatherConditionItems){
				
				
			        public View getView(int position, View convertView, ViewGroup parent) {
			            //User super class to create the View
			            View v = super.getView(position, convertView, parent);
			            TextView tv = (TextView)v.findViewById(android.R.id.text1);

			            //Put the image on the TextView
			            tv.setCompoundDrawablesWithIntrinsicBounds(weatherConditionItems[position].icon, 0, 0, 0);

			            //Add margin between image and text (support various screen densities)
			            int dp5 = (int) (5 * getResources().getDisplayMetrics().density + 0.5f);
			            tv.setCompoundDrawablePadding(dp5);

			            return v;
			        }
			        
		        
			    };

				radioSexGroup = (RadioGroup) findViewById(R.id.radioGroupTemp);

				
		//create the location manager and listener
		final LocationManager locationManager =
		        (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

		final LocationListener locationListener = new LocationListener() {
			public void onLocationChanged(Location location) { //當坐標改變時觸發此函數，如果Provider傳進相同的坐標，它就不會被觸發
			// log it when the location changes
			
				System.out.println("SuperMap"+ "Location changed : Lat: "
				+ location.getLatitude() + " Lng: "
				+ location.getLongitude());
				latitude.setText(location.getLatitude()+"");
				longtiude.setText(location.getLongitude()+"");
				
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
				System.out.println("location listener");
				new GetAddressHttpTask().execute(location.getLatitude()+"",location.getLongitude()+"");    
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

    	
 
    	
		dateToggleBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	// Perform action on click
            	
            	
        	    int dateToggleBtnIndex = mainLayout.indexOfChild(dateToggleBtn);   		
        	    if (((ToggleButton) v).isChecked())
        	    {
        	    	mainLayout.addView(dateView,dateToggleBtnIndex+1);
                    System.out.println("index: "+dateToggleBtnIndex);
               
        	    }
        	    else
        	    {
        	    	mainLayout.removeViewAt(dateToggleBtnIndex+1);
        	    }

            }
        });
        
		locationToggleBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	// Perform action on click
            	
        	    int locationToggleBtnIndex = mainLayout.indexOfChild(locationToggleBtn);
        	    if (((ToggleButton) v).isChecked())
        	    {
      	    	
        	    	mainLayout.addView(locationView,locationToggleBtnIndex+1);
                    System.out.println("index: "+locationToggleBtnIndex);
        	    }
        	    else
        	    {
        	    	mainLayout.removeViewAt(locationToggleBtnIndex+1);
        	    }        	    

            }
        });
        
		weatherToggleBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	// Perform action on click
            	
        	    int weatherToggleBtnIndex = mainLayout.indexOfChild(weatherToggleBtn);
        	    if (((ToggleButton) v).isChecked())
        	    {
        	    	

        	    		    
        	    	final CharSequence choice[] = { "sky is clear", "few clouds", "scattered clouds","broken clouds",
        	    			"shower rain","rain","thunderstorm","snow","mist","No specific weather condition"};
        	    	
        			DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        			    @Override
        			    public void onClick(DialogInterface dialog, int which) {
        			    	
        	                  if(choice[which]=="sky is clear")
        	                	  weather_condition_value="1";
        	                  else if (choice[which]=="few clouds")
        	                	  weather_condition_value="2";
        	                  else if (choice[which]=="scattered clouds")
        	                	  weather_condition_value="3";
        	                  else if (choice[which]=="broken clouds")
        	                	  weather_condition_value="4";
        	                  else if (choice[which]=="shower rain")
        	                	  weather_condition_value="5";
        	                  else if (choice[which]=="rain")
        	                	  weather_condition_value="6";
        	                  else if (choice[which]=="thunderstorm")
        	                	  weather_condition_value="7";
        	                  else if (choice[which]=="snow")
        	                	  weather_condition_value="8";
        	                  else if (choice[which]=="mist")
        	                	  weather_condition_value="9";
        	                  else if(choice[which]=="No specific weather condition")
        	                	  weather_condition_value=null;
        			    }
        			};
        			
        			
        			AlertDialog.Builder builder = new AlertDialog.Builder(SendMessageActivity.this);
        			builder.setTitle("Choose a weather condtion:").setAdapter(weatherAdapter, dialogClickListener).show();        	    	
        	    	
        			//below old code
        	    	mainLayout.addView(weatherView,weatherToggleBtnIndex+1);
                    System.out.println("index: "+weatherToggleBtnIndex);
        	    }
        	    else
        	    {
        	    	mainLayout.removeViewAt(weatherToggleBtnIndex+1);
        	    }           	    

            }
        });        


		
		movableMessageToggleBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	// Perform action on click
            	
        	    int movableMessageToggleBtnIndex = mainLayout.indexOfChild(movableMessageToggleBtn);
        	    if (((ToggleButton) v).isChecked())
        	    {
//        	    	mainLayout.addView(locationView,movableMessageToggleBtnIndex+1);
//                    System.out.println("index: "+movableMessageToggleBtnIndex);
        			Intent intent = new Intent();   
        			intent.setClass(SendMessageActivity.this, TiltActivity.class);
        			startActivityForResult(intent,GETMOVABLEINFO);        	    	
        	    }
        	    else
        	    {
//        	    	mainLayout.removeViewAt(movableMessageToggleBtnIndex+1);
        	    }           	    

            }
        });

		

        sendMessageBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	// Perform action on click
            	
            	
            	
	        	 Boolean dateToggleBtnCheck = ((ToggleButton) dateToggleBtn).isChecked();
	        	 Boolean locationToggleBtnCheck =  ((ToggleButton) locationToggleBtn).isChecked();    	    
	        	 Boolean weatherToggleBtnCheck =  ((ToggleButton) weatherToggleBtn).isChecked();
	        	 Boolean imageSelectCheck = false;
	        	 
	        	 if(encodedImage!=null){
	        		 imageSelectCheck= true;
	        	 }
	        	 
	        	 System.out.println(dateToggleBtnCheck.toString()+locationToggleBtnCheck.toString()+weatherToggleBtnCheck.toString()+imageSelectCheck.toString());
	        	 
	          	//grab the content to fields and convert them to string
	          	String recipientEmailString = recipientEmail.getText().toString();
	          	String messageBodyString = messageBody.getText().toString();
	          	
	          	//----------------------------Date Related Started
    			SimpleDateFormat formatUTC = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.ROOT );
    				//System.out.println(notifyDatetimeZoneItems.getSelectedItem().toString());
    				//System.out.println(revealDatetimeZoneItems.getSelectedItem().toString());
    			
    			 TimeZone notifyTimeZone=null;
    			 TimeZone revealTimeZone=null;
    			 String[]TZ = TimeZone.getAvailableIDs();
    	    	    
    	    	    for(int i = 0; i < TZ.length; i++) {
    	    	        if(TimeZone.getTimeZone(TZ[i]).getDisplayName().equals(notifyDatetimeZoneItems.getSelectedItem().toString())) {
    	    	        	notifyTimeZone = TimeZone.getTimeZone(TZ[i]);
    	    	        }
    	    	    }
    	    	    
    	    	    for(int i = 0; i < TZ.length; i++) {
    	    	        if(TimeZone.getTimeZone(TZ[i]).getDisplayName().equals(revealDatetimeZoneItems.getSelectedItem().toString())) {
    	    	        	revealTimeZone = TimeZone.getTimeZone(TZ[i]);
    	    	        }
    	    	    }
    	    	    
    	    	    //notify date
	    			formatUTC.setTimeZone(notifyTimeZone);
	    				//System.out.println("Year: "+messageNotifyDatePicker.getYear()+"Month: " +(messageNotifyDatePicker.getMonth()+1) + "Day: " +messageNotifyDatePicker.getDayOfMonth()+ "Hour: "+messageNotifyTimePicker.getCurrentHour()+"Minute: "+messageNotifyTimePicker.getCurrentMinute());
	    			Date notifyDate = null;
					try {
						notifyDate = formatUTC.parse(messageNotifyDatePicker.getYear()+"-"+(messageNotifyDatePicker.getMonth()+1)+"-"+messageNotifyDatePicker.getDayOfMonth()+" "+messageNotifyTimePicker.getCurrentHour()+":"+messageNotifyTimePicker.getCurrentMinute()+":00" );
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					long notifyTimeZoneOffset =(formatUTC.getTimeZone().getOffset(System.currentTimeMillis()))/(1000*60*60);
					String notifyTimeZoneOffsetString = "";
					if(Long.signum(notifyTimeZoneOffset)==1)
					{
						notifyTimeZoneOffsetString = "+" +Long.toString(notifyTimeZoneOffset);
					}else{
						notifyTimeZoneOffsetString = Long.toString(notifyTimeZoneOffset);
					}
					 
						//System.out.println("Notify Date: " + formatUTC.format(notifyDate)+" " + notifyTimeZoneOffsetString);
            		
					String notifyDateString = formatUTC.format(notifyDate)+" " + notifyTimeZoneOffsetString;
					
					
					//reveal date
					formatUTC.setTimeZone(revealTimeZone);
						//System.out.println("Year: "+messageNotifyDatePicker.getYear()+"Month: " +(messageNotifyDatePicker.getMonth()+1) + "Day: " +messageNotifyDatePicker.getDayOfMonth()+ "Hour: "+messageNotifyTimePicker.getCurrentHour()+"Minute: "+messageNotifyTimePicker.getCurrentMinute());
	    			Date revealDate = null;
					try {
						revealDate = formatUTC.parse(messageRevealDatePicker.getYear()+"-"+(messageRevealDatePicker.getMonth()+1)+"-"+messageRevealDatePicker.getDayOfMonth()+" "+messageRevealTimePicker.getCurrentHour()+":"+messageRevealTimePicker.getCurrentMinute()+":00" );
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					long revealTimeZoneOffset =(formatUTC.getTimeZone().getOffset(System.currentTimeMillis()))/(1000*60*60);
					String revealTimeZoneOffsetString = "";
					if(Long.signum(revealTimeZoneOffset)==1)
					{
						revealTimeZoneOffsetString = "+" +Long.toString(revealTimeZoneOffset);
					}else{
						revealTimeZoneOffsetString = Long.toString(revealTimeZoneOffset);
					}
					 
						//System.out.println("Reveal Date: " + formatUTC.format(revealDate)+" " + revealTimeZoneOffsetString);
					
					String revealDateString = formatUTC.format(revealDate)+" " + revealTimeZoneOffsetString;
				//----------------------------Date related ended
					
	          	if(dateToggleBtnCheck ==false && locationToggleBtnCheck == false && weatherToggleBtnCheck==false){
	          		System.out.println("plain old message");
	          		
	          		new SendMessageHttpTask().execute(recipientEmailString,messageBodyString);
	          		
	          		
	            }else if(dateToggleBtnCheck==true && locationToggleBtnCheck == false){
	            	System.out.println("date message");	

						
		            	 if(messageNotifyCheckBox.isChecked())
		            	 {
		            		 
		            		 new SendMessageHttpTask().execute(recipientEmailString,messageBodyString,"noNotifyDate",revealDateString);  
		            		 
		            	 }else{
		            		
							 System.out.println("Notify Date: " + notifyDateString);
		            		 System.out.println("Reveal Date: " + revealDateString);	
		            		 new SendMessageHttpTask().execute(recipientEmailString,messageBodyString,notifyDateString,revealDateString);  
		            	 }
		            	//new SendDateMessageHttpTask().execute(recipientEmailString,messageBodyString,latitude,longitude);  
	            }else if(dateToggleBtnCheck==true && locationToggleBtnCheck == true )
	            {
	            	System.out.println("location message(w/ notify date and revealdate )");	
	            	 if(messageNotifyCheckBox.isChecked())
	            	 {
	            		 new SendMessageHttpTask().execute(recipientEmailString,messageBodyString,"noNotifyDate",revealDateString,latitude.getText().toString(),longtiude.getText().toString(),address.getText().toString());  
	            	 }else{
	            		 new SendMessageHttpTask().execute(recipientEmailString,messageBodyString,notifyDateString,revealDateString,latitude.getText().toString(),longtiude.getText().toString(),address.getText().toString());  
	            	 }
	            	 
	            }else if(dateToggleBtnCheck==false && locationToggleBtnCheck == true ){
	            	System.out.println("location message(no notify,instant reveal)");	
	            	 new SendMessageHttpTask().execute(recipientEmailString,messageBodyString,"noNotifyDate","noRevealDate",latitude.getText().toString(),longtiude.getText().toString(),address.getText().toString());
	            }else if(weatherToggleBtnCheck==true){
	            	System.out.println("weather message");
	            	 new SendMessageHttpTask().execute(recipientEmailString,messageBodyString);	            	
	            }
            }
        });
        
        
        
    }
	
	//send out the message
	public final class SendMessageHttpTask extends
    AsyncTask<String/* Param */, Boolean /* Progress */, String /* Result */> {
	
		BasicHttpClient httpClient = new BasicHttpClient("http://158.182.6.176:9090");
		
		@Override
		protected String doInBackground(String... params) {
		    publishProgress(true);
		    // Do the usual httpclient thing to get the result
		    String recipientEmail=null;
		    String body=null;
		    String notifyDate=null;
		    String revealDate=null;
		    String latitude =null;
		    String longitude =null;
		    String address=null;
		    
		    byte[] data = null;	
		  //get current userid
			SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); 	
			int userid = pref.getInt("currentUserid", 0);
			System.out.println("current user id:"+ userid );		    
		    for( int i = 0; i < params.length; i++){
		    	if(i==0)
		    	{
		    		recipientEmail = params[0];
		    		System.out.println(recipientEmail);
		    	}
		    	if(i==1)
		    	{
		    		body = params[1];
		    	}
		    	
		    	if(i==2)
		    	{
		    		notifyDate=params[2];
		    	}
		    	
		    	if(i==3)
		    	{
		    		revealDate=params[3];
		    	}
		    	
		    	if(i==4)
		    	{
		    		latitude=params[4];
		    	}
		    	
		    	if(i==5)
		    	{
		    		longitude=params[5];
		    	}
		    	
		    	if(i==6)
		    	{
		    		address=params[6];
		    	}		    	
		    }
		    
		    ParameterMap paramsMap =null;
		    if(params.length==2)
		    {
		    	paramsMap = httpClient.newParams()
		            .add("continue", "/")
		            .add("recipientEmail", recipientEmail)
		            .add("body", body)
		            .add("userid",userid+"") 
		            .add("action", "sendplainmessage");
		    	

		    
		    }else if(params.length==4){
			    	paramsMap = httpClient.newParams()
			            .add("continue", "/")
			            .add("recipientEmail", recipientEmail)
			            .add("body", body)
			            .add("userid",userid+"")
			            .add("notifyDate", notifyDate)
			            .add("revealDate", revealDate)
			            .add("action", "senddatemessage");
		    }else{
			    	paramsMap = httpClient.newParams()
			            .add("continue", "/")
			            .add("recipientEmail", recipientEmail)
			            .add("body", body)
			            .add("userid",userid+"")
			            .add("notifyDate", notifyDate)
			            .add("revealDate", revealDate)
			            .add("latitude", latitude)
			            .add("longitude", longitude)
			            .add("address", address)
			            .add("action", "sendlocationmessage");
			    	
			    	Boolean movableMessageToggleBtnCheck = ((ToggleButton) movableMessageToggleBtn).isChecked();
			    	if(movableMessageToggleBtnCheck){
				    	if(speed.getText().toString().contains("N/A")==false && heading.getText().toString().contains("N/A")==false ){
				    		paramsMap.add("speed", speed.getText().toString());
				    		paramsMap.add("heading", heading.getText().toString());
				    	}				    			    		
			    	}
		    	
			    	Boolean publicMsgToggleBtnCheck = ((ToggleButton) publicMsgToggleBtn).isChecked();
			    	if(publicMsgToggleBtnCheck){
			    		paramsMap.add("publicMsg", "true");
			    	}else{
			    		paramsMap.add("publicMsg", "false");
			    	}
			    	
			    	


		    }
		    
		    //with image?
	    	if(encodedImage!=null){
	    		paramsMap.add("image", encodedImage);
	    	}
	    	
	    	//with weather set up?
	    	Boolean weatherToggleBtnCheck = ((ToggleButton) weatherToggleBtn).isChecked();
	    	if(weatherToggleBtnCheck){
	    		
	    		paramsMap.add("continue", "/");
	            paramsMap.add("recipientEmail", recipientEmail);
	            paramsMap.add("body", body);
	            paramsMap.add("userid",userid+"");		
	    		paramsMap.add("action", "sendweathermessage");
	    		
	    		//weather condition exist?
	    		if(weather_condition_value!=null)
	    		{
	    			paramsMap.add("weather_condition", weather_condition_value);
	    		}
	    			
	    		//temperature condition exist?
		    	Boolean temperatureToggleBtnCheck = ((ToggleButton) temperatureMsgToggleBtn).isChecked();
		    	if(temperatureToggleBtnCheck){
		    		
			        // get selected radio button from radioGroup
					int selectedId = radioSexGroup.getCheckedRadioButtonId();
		 
					if(selectedId==R.id.higherTempRB){
						paramsMap.add("temperature_lower_or_higher", "true");
						paramsMap.add("temperature", seekBarValue.getText().toString());
					}else if(selectedId==R.id.smallThanTempRB){
						paramsMap.add("temperature_lower_or_higher", "true");
						paramsMap.add("temperature", seekBarValue.getText().toString()); 
					}
		    	}		    		
	    	}	    	
	    	
		    httpClient.setConnectionTimeout(10000); // 2s
		    HttpResponse httpResponse = httpClient.post("/skymsg/MessageController", paramsMap);
		    System.out.println(httpResponse.getBodyAsString());
		    
//			AbstractHttpClient httpAbstractClient = new BasicHttpClient("http://158.182.6.176:9090");
//			
//		    HttpResponse post = httpAbstractClient.post("/skymsg/MessageController","multipart/form-data", data);
		    
		    return httpResponse.getBodyAsString();
		}
		
		@Override
		protected void onProgressUpdate(Boolean... progress) {
		    // line below coupled with 
		    //    getWindow().requestFeature(Window.FEATURE_INDETERMINATE_PROGRESS) 
		    //    before setContentView 
		    // will show the wait animation on the top-right corner
		    SendMessageActivity.this.setProgressBarIndeterminateVisibility(progress[0]);
		}
		
		@Override
		protected void onPostExecute(String result) {
		    publishProgress(false);
		    // Do something with result in your activity
		    Context context = getApplicationContext();
		    int duration = Toast.LENGTH_SHORT;

		    System.out.println("Result:"+result);
			if(result.contains("Message Sent") ){
				System.out.println("up");
			    Toast toast = Toast.makeText(context,R.string.messageSentSuccess , duration);
			    toast.show();
			}else{
				System.out.println("down");
			    Toast toast = Toast.makeText(context,R.string.mesasgeSentFail , duration);
			    toast.show();				
			}
			
			
			

		}
	
	}

	//upload message image 
	public final class UploadPhotoHttpTask extends
    AsyncTask<Object/* Param */, Boolean /* Progress */, String /* Result */> {
	
		AbstractHttpClient httpAbstractClient = new BasicHttpClient("http://158.182.6.176:9090");
		
		protected String doInBackground(Object... params) {
		    publishProgress(true);
		    // Do the usual httpclient thing to get the result

		    byte[] data = null;	
		    int messageId;
		    
		    data = (byte[]) params[0];
		    messageId =(Integer)params[1];
		  //get current userid
			SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); 	
			int userid = pref.getInt("currentUserid", 0);
			System.out.println("current user id:"+ userid );		    
		
			
			httpAbstractClient.addHeader("messageId",messageId+"");
			
		    HttpResponse post = httpAbstractClient.post("/skymsg/upload",AbstractHttpClient.MULTIPART, data);
		    
		    return post.getBodyAsString();
		}
		
		@Override
		protected void onProgressUpdate(Boolean... progress) {
		    // line below coupled with 
		    //    getWindow().requestFeature(Window.FEATURE_INDETERMINATE_PROGRESS) 
		    //    before setContentView 
		    // will show the wait animation on the top-right corner
		    SendMessageActivity.this.setProgressBarIndeterminateVisibility(progress[0]);
		}
		
		@Override
		protected void onPostExecute(String result) {
		    publishProgress(false);
		    // Do something with result in your activity
		    Context context = getApplicationContext();
		    int duration = Toast.LENGTH_SHORT;

		    System.out.println("Result:"+result);
			if(result.contains("Message Sent") ){
				System.out.println("up");
			    Toast toast = Toast.makeText(context,R.string.messageSentSuccess , duration);
			    toast.show();
			}else{
				System.out.println("down");
			    Toast toast = Toast.makeText(context,R.string.mesasgeSentFail , duration);
			    toast.show();				
			}
			
			
			

		}

	
	}
	
	//return position's address
	public final class GetAddressHttpTask extends
    AsyncTask<String/* Param */, Boolean /* Progress */, String /* Result */> {
	
		BasicHttpClient httpClient = new BasicHttpClient(" http://nominatim.openstreetmap.org");
		
		@Override
		protected String doInBackground(String... params) {
		    publishProgress(true);
		    // Do the usual httpclient thing to get the result
		    String lat=null;
		    String lon=null;
		    
		  //get current userid
		    
		    for( int i = 0; i < params.length; i++){
		    	if(i==0)
		    	{
		    		lat = params[0];

		    	}
		    	if(i==1)
		    	{
		    		lon = params[1];
		    	}
		    }
		    		
		    ParameterMap paramsMap = httpClient.newParams()
		            .add("continue", "/")
		            .add("lat", lat)
		            .add("lon",lon)
		            .add("format", "json")
		            .add("action", "sendplainmessage");
		    httpClient.addHeader("someheader", "value");
		    httpClient.setConnectionTimeout(10000); // 2s
		    HttpResponse httpResponse = httpClient.get("/reverse", paramsMap);
		    System.out.println(httpResponse.getBodyAsString());
		    return httpResponse.getBodyAsString();
		}
		
		@Override
		protected void onProgressUpdate(Boolean... progress) {
		    // line below coupled with 
		    //    getWindow().requestFeature(Window.FEATURE_INDETERMINATE_PROGRESS) 
		    //    before setContentView 
		    // will show the wait animation on the top-right corner
		    SendMessageActivity.this.setProgressBarIndeterminateVisibility(progress[0]);
		}
		
		@Override
		protected void onPostExecute(String result) {
		    publishProgress(false);
		    // Do something with result in your activity
		    String addressString=null;
		    try {
				JSONObject parseResult = new JSONObject(result);
				addressString = parseResult.getString("display_name");
	
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    System.out.println("Result:"+result);
		    
		    
		    address.setText(addressString);

		}
	
	}

	    private static final int SETIMAGE = 2;
	    private Bitmap bitmap;
	    private Bitmap smallBitmap;
	    private ImageView imageView;
	    
	   public void pickImage(View View) {
	        Intent intent = new Intent();
	        intent.setType("image/*");
	        intent.setAction(Intent.ACTION_GET_CONTENT);
	        intent.addCategory(Intent.CATEGORY_OPENABLE);
	        startActivityForResult(intent, SETIMAGE);
	    }
	 
	    @Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	        if (requestCode == SETIMAGE && resultCode == Activity.RESULT_OK){
	            try {
	                // We need to recyle unused bitmaps
	                if (bitmap != null) {
	                    bitmap.recycle();
	                }
	                InputStream stream = getContentResolver().openInputStream(
	                        data.getData());
	                
	                //original pic
	                bitmap = BitmapFactory.decodeStream(stream);
	                
	                //new byte array output stream for decode
	                ByteArrayOutputStream baos = new ByteArrayOutputStream();
	                
	                //create a small pic for upload
	                System.out.println("bitmap width: "+bitmap.getWidth());
	                System.out.println("bitmap height: "+bitmap.getHeight());
	                
	                double scaleFactor=1;
	                if(bitmap.getHeight()>=bitmap.getWidth()){
	                	scaleFactor = ((double)640 / bitmap.getHeight()) ;
	                }else{
	                	scaleFactor = ((double)640 / bitmap.getWidth()) ;
	                }
	                //division, either one has to be cast to double
	               
	                System.out.println("Scalefactor: "+scaleFactor);
	                smallBitmap = Bitmap.createScaledBitmap(bitmap,(int)(bitmap.getWidth()*scaleFactor), (int)(bitmap.getHeight()*scaleFactor), true);
	                smallBitmap.compress(Bitmap.CompressFormat.JPEG, 30, baos); //bm is the bitmap object   
	                byte[] b = baos.toByteArray();	                
	                encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
	                stream.close();
	                
	                
	                imageView.setImageBitmap(bitmap);
	                
	            } catch (FileNotFoundException e) {
	                e.printStackTrace();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }else if(requestCode == GETMOVABLEINFO && resultCode == Activity.RESULT_FIRST_USER){
	            if(data != null) {  
	               // request_text.setText(data.getStringExtra("request_text_for_third"));
	            	speed.setText(data.getDoubleExtra("totalacclerationmax",0)+""); 
	            	heading.setText(data.getStringExtra("direction")); 
	            }  
	        }
	        super.onActivityResult(requestCode, resultCode, data);
	    }
	
	
}
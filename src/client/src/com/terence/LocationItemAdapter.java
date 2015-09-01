package com.terence;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class LocationItemAdapter extends ArrayAdapter<LocationRecord> {
    private ArrayList<LocationRecord> locations;

    public LocationItemAdapter(Context context, int textViewResourceId, ArrayList<LocationRecord> locations) {
        super(context, textViewResourceId, locations);
        this.locations = locations;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
	  if (v == null) {
	            LayoutInflater vi = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            v = vi.inflate(R.layout.location_list_item, null);
	  }
      
	  	  LocationRecord location = locations.get(position);
		  if (location != null) {
			  		final TextView latitude = (TextView) v.findViewById(R.id.latitudeTVLocationList);
		            final TextView longitude = (TextView) v.findViewById(R.id.longitudeTVLocationList);
		            final TextView address = (TextView) v.findViewById(R.id.addressTVLocationList);
		            LinearLayout eachLocation = (LinearLayout) v.findViewById(R.id.eachLocation);
		      if (latitude != null) {
		    	  latitude.setText(location.latitude+"");
		      }
		
		      if(longitude != null) {
		    	  longitude.setText(location.longitude+"");
		      }
		      if (address !=null){
		    	  address.setText(location.address);
		      }
		      
		      eachLocation.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Toast.makeText(getContext(), address.getText().toString(), Toast.LENGTH_LONG).show();
					Intent sendMessageIntent = new Intent(getContext(), SendMessageActivity.class);  
					sendMessageIntent.putExtra("address", address.getText().toString());  
					sendMessageIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					getContext().startActivity(sendMessageIntent);
					
//					 Intent i = new Intent();  
//		                i.putExtra("request",text+"\n"+"从SecondActivity传递到ThirdActivity");  
//		                setResult(Activity.RESULT_OK,i);  
//		                finish();				
				
				}
		    	  
		      });
		  }
		  
		  return v;
    }
    

    
}

package com.terence;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MessageItemAdapter extends ArrayAdapter<MessageRecord> {
    private ArrayList<MessageRecord> messages;

    public MessageItemAdapter(Context context, int textViewResourceId, ArrayList<MessageRecord> messages) {
        super(context, textViewResourceId, messages);
        this.messages = messages;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
	  if (v == null) {
	            LayoutInflater vi = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            v = vi.inflate(R.layout.message_list_item, null);
	  }
	  
      final ImageView dateRelated = (ImageView) v.findViewById(R.id.DateRelated);
      final ImageView locationRelated = (ImageView) v.findViewById(R.id.LocationRelated);
      final ImageView weatherRelated = (ImageView) v.findViewById(R.id.WeatherRelated);
      final ImageView publicMsg = (ImageView) v.findViewById(R.id.publicMessage);
      final ImageView withImage = (ImageView) v.findViewById(R.id.withImage);
      final ImageView movingMsg = (ImageView) v.findViewById(R.id.movableMsg);
      
      final TextView messageBody = (TextView) v.findViewById(R.id.messageBody);
      final TextView messageId = (TextView) v.findViewById(R.id.messageId);
      final TextView senderId = (TextView) v.findViewById(R.id.senderId);
      final TextView senderName = (TextView) v.findViewById(R.id.senderName);
      final TextView senderEmail = (TextView) v.findViewById(R.id.senderEmail);
      final TextView receiverId = (TextView) v.findViewById(R.id.receiverId);
      final TextView receiverName = (TextView) v.findViewById(R.id.receiverName);
      final TextView receiverEmail = (TextView) v.findViewById(R.id.receiverEmail);
      final TextView createDate = (TextView) v.findViewById(R.id.createDate);
      final TextView revealDate = (TextView) v.findViewById(R.id.revealDate);
      final TextView notifyDate= (TextView) v.findViewById(R.id.notifyDate);
      final TextView latitude = (TextView) v.findViewById(R.id.latitude);
      final TextView longitude = (TextView) v.findViewById(R.id.longitude);
      final TextView address = (TextView) v.findViewById(R.id.address);
      final TextView currentSpeed = (TextView) v.findViewById(R.id.currentSpeed);
      final TextView currentHeading = (TextView) v.findViewById(R.id.currentHeading);
      final TextView distanceFromUserToMessage = (TextView) v.findViewById(R.id.distanceFromUserToMessage);
      final Button replyMessageBtn = (Button) v.findViewById(R.id.replyMessageBtn);
      final Button deleteMessageBtn = (Button) v.findViewById(R.id.deleteMessageBtn);
      final Button reportSpamBtn = (Button) v.findViewById(R.id.reportSpamBtn);
      
      LinearLayout eachmessage = (LinearLayout) v.findViewById(R.id.eachmessage);      
		  final MessageRecord message = messages.get(position);
		  if (message != null) {
		             
		            try {
						messageBody.setText(URLDecoder.decode(message.getBody(), "UTF-8"));
					} catch (UnsupportedEncodingException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		          
		            messageId.setText(message.getMessageid()+"");
		            senderId.setText(message.getSenderid()+"");
		            if(message.getSenderName()!=null){
		            	senderName.setText(message.getSenderName());
		            	senderEmail.setText(message.getSenderEmail());
		            }else{
		            	senderName.setText("");
		            	senderEmail.setText("");
		            }
		            //getSenderName
		            //getSenderEmail
		            if(message.getReceiverid()!=0){ 	
		            	receiverId.setText(message.getReceiverid()+"");
		            	
		            	//getReceiverName
		            	//getReceiverEmail
		            }else{
		            	receiverId.setText("");
		            }
		            if(message.getReceiverName()!=null){
		            	receiverName.setText(message.getReceiverName());
		            	receiverEmail.setText(message.getReceiverEmail());
		            }else{
		            	receiverName.setText("");
		            	receiverEmail.setText("");
		            }
		            createDate.setText(message.getCreateDate());
		            if(message.getRevealDate()!=null){
		            	revealDate.setText(message.getRevealDate());
		            	dateRelated.setImageDrawable(getContext().getResources().getDrawable(R.drawable.date_related));
		            }else{
		            	revealDate.setText("");
		            	dateRelated.setImageDrawable(null);
		            }
		            if(message.getNotifyDate()!=null){
		            	notifyDate.setText(message.getNotifyDate());
		            	
		            }else{
		            	notifyDate.setText("");
		            }
		            if(message.getLatitude()!=null){
		            	latitude.setText(message.getLatitude());
		            	longitude.setText(message.getLongitude());
		            	try {
							address.setText(URLDecoder.decode(message.getAddress(), "UTF-8"));
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		            	if(message.getDistance()!=0.0)
		            	distanceFromUserToMessage.setText(message.getDistance()+"");
		            	locationRelated.setImageDrawable(getContext().getResources().getDrawable(R.drawable.pin));
		            }else{
		            	latitude.setText("");
		            	longitude.setText("");
		            	address.setText("");		
		            	locationRelated.setImageDrawable(null);
		            }
		            
		            if(message.getInitalSpeed()!=0.0){
		            	currentSpeed.setText((int)message.getInitalSpeed()+"");
		            	currentHeading.setText(message.getHeading());
		            	movingMsg.setImageDrawable(getContext().getResources().getDrawable(R.drawable.movable_msg));    
		            }else{
		            	currentSpeed.setText("");
		            	currentHeading.setText("");		
		            	movingMsg.setImageDrawable(null);
		            }
		            
		            if(message.getImage()!=null){
		            	withImage.setImageDrawable(getContext().getResources().getDrawable(R.drawable.with_picture));
		            }else{
		            	withImage.setImageDrawable(null);
		            }
		      
		            if(message.getPublicMsg()==true){
		            	publicMsg.setImageDrawable(getContext().getResources().getDrawable(R.drawable.public_msg));
		            }else{
		            	publicMsg.setImageDrawable(null);
		            }
		      
		      locationRelated.setOnClickListener(new OnClickListener(){

				
		    	  @Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
		    		  	
		    			 Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:" + latitude.getText().toString() + "," + longitude.getText().toString() + "?z=17&q=" +  latitude.getText().toString() + "," + longitude.getText().toString()));
		    			 intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		    			 getContext().startActivity(intent);
				
				}
		    	  
		      });
		      
		      withImage.setOnClickListener(new OnClickListener(){

					
		    	  @Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
		    		   if(message.getRevealed()==true){
			    	    	String path = Environment.getExternalStorageDirectory().toString();		    	    
		                    File file = new File(path, "/DCIM/Skymsg/"+messageId.getText().toString()+".jpg");
		                    
			    			Intent intent = new Intent(); 
			    			intent.setAction(android.content.Intent.ACTION_VIEW);
			    			intent.setDataAndType(Uri.fromFile(file), "image/*");
			    			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			    			getContext().startActivity(intent);		    			   
		    		   }else{
		    			   Toast.makeText(getContext(),"The picture is hidden for now", Toast.LENGTH_LONG).show();
		    		   }

				
				}
		    	  
		      });		            
		      replyMessageBtn.setOnClickListener(new OnClickListener(){

				
		    	  @Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Toast.makeText(getContext(), senderEmail.getText().toString(), Toast.LENGTH_LONG).show();
					Intent sendMessageIntent = new Intent(getContext(), SendMessageActivity.class);  
					sendMessageIntent.putExtra("email", senderEmail.getText().toString());  
					sendMessageIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					getContext().startActivity(sendMessageIntent);
				}
		    	  
		      });

		      deleteMessageBtn.setOnClickListener(new OnClickListener(){

					
		    	  @Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Toast.makeText(getContext(), messageId.getText().toString(), Toast.LENGTH_LONG).show();

				}
		    	  
		      });
		      
		      reportSpamBtn.setOnClickListener(new OnClickListener(){

					
		    	  @Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Toast.makeText(getContext(), messageId.getText().toString(), Toast.LENGTH_LONG).show();
					Intent sendMessageIntent = new Intent(getContext(), SendMessageActivity.class);  
					sendMessageIntent.putExtra("messageId", messageId.getText().toString());  
					sendMessageIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					getContext().startActivity(sendMessageIntent);
				}
		    	  
		      });		      
		  }
		  
		  return v;
    }
    

    
}

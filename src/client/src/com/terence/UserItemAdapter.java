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

public class UserItemAdapter extends ArrayAdapter<FriendRecord> {
    private ArrayList<FriendRecord> users;

    public UserItemAdapter(Context context, int textViewResourceId, ArrayList<FriendRecord> users) {
        super(context, textViewResourceId, users);
        this.users = users;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
	  if (v == null) {
	            LayoutInflater vi = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            v = vi.inflate(R.layout.friend_list_item, null);
	  }
      
		  FriendRecord user = users.get(position);
		  if (user != null) {
		            TextView username = (TextView) v.findViewById(R.id.username);
		            final TextView email = (TextView) v.findViewById(R.id.email);
		            LinearLayout eachUser = (LinearLayout) v.findViewById(R.id.eachuser);
		      if (username != null) {
		                username.setText(user.username);
		      }
		
		      if(email != null) {
		          email.setText(user.email);
		      }
		      eachUser.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Toast.makeText(getContext(), email.getText().toString(), Toast.LENGTH_LONG).show();
					Intent sendMessageIntent = new Intent(getContext(), SendMessageActivity.class);  
					sendMessageIntent.putExtra("email", email.getText().toString());  
					sendMessageIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					getContext().startActivity(sendMessageIntent);
				}
		    	  
		      });
		  }
		  
		  return v;
    }
    

    
}

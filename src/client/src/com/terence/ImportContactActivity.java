package com.terence;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.terence.FriendListActivity.CheckFriendExistHttpTask;

import com.turbomanage.httpclient.BasicHttpClient;
import com.turbomanage.httpclient.HttpResponse;
import com.turbomanage.httpclient.ParameterMap;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.app.Activity;
import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class ImportContactActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_import_contact);
		
		new GetNameTask().execute("anything");
	
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_import_contact, menu);
		return true;
	}
	public final class GetNameTask extends
    AsyncTask<String/* Param */, Boolean /* Progress */, ArrayList<String> /* Result */> {
	
		
		
		protected ArrayList<String> doInBackground(String... params) {
		    publishProgress(true);
		    // Do the usual httpclient thing to get the result
		    ArrayList<String> names = getNameEmailDetails();    
		    return names;
		}  
		@Override
		protected void onProgressUpdate(Boolean... progress) {
		    // line below coupled with 
		    //    getWindow().requestFeature(Window.FEATURE_INDETERMINATE_PROGRESS) 
		    //    before setContentView 
		    // will show the wait animation on the top-right corner
			ImportContactActivity.this.setProgressBarIndeterminateVisibility(progress[0]);
		}
		
		protected void onPostExecute(ArrayList<String> email ) {
		    publishProgress(false);
		    // Do something with result in your activity
		    
		    Intent i=new Intent();
		    Bundle b=new Bundle();
		    b.putStringArrayList("emails", email);
		    i.putExtras(b);
		    setResult(RESULT_OK,i);
		    finish();
		}
	
	} 
	
    public ArrayList<String> getNameEmailDetails(){
        ArrayList<String> names = new ArrayList<String>();
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,null, null, null, null);
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                Cursor cur1 = cr.query( 
                        ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", 
                                new String[]{id}, null); 
                while (cur1.moveToNext()) { 
                    //to get the contact names
                    String name=cur1.getString(cur1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    Log.e("Name :", name);
                    String email = cur1.getString(cur1.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                    Log.e("Email", email);
                    if(email!=null){
                        names.add(email);
                    }
                } 
                cur1.close();
            }
        }
        return names;
    }	
    
    
}

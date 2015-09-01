package com.terence;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class TiltActivity extends Activity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private TextView rawValueView,totalAcclerationView,totalAcclerationMaxView;
    private View mTop, mBottom, mLeft, mRight;
    double totalacclerationmax = 0;
    final private static int SETHEADING = 2;
 
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_tilt);

        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        rawValueView = (TextView)findViewById(R.id.rawValueView);
        totalAcclerationView = (TextView)findViewById(R.id.valuesTotalAccleration);
        totalAcclerationMaxView = (TextView)findViewById(R.id.valuesTotalAcclerationMax);
        mTop = findViewById(R.id.top);
        mBottom = findViewById(R.id.bottom);
        mLeft = findViewById(R.id.left);
        mRight = findViewById(R.id.right);
        

  
    }

     protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer,
            SensorManager.SENSOR_DELAY_UI);
            
     }

    protected void onPause() {
         super.onPause();
        mSensorManager.unregisterListener(this);

     }



    public void onAccuracyChanged(Sensor sensor, int accuracy) { }

    public void onSensorChanged(SensorEvent event) {
        float[] values = event.values;
         float x = values[0] / 10;
         float y = values[1] / 10;
         int scaleFactor;

         if(x > 0) {
             scaleFactor = (int)Math.min(x * 255, 255);
            mRight.setBackgroundColor(Color.TRANSPARENT);
            mLeft.setBackgroundColor(Color.argb(scaleFactor, 255, 0, 0));
         } else {
             scaleFactor = (int)Math.min(Math.abs(x) * 255, 255);
            mRight.setBackgroundColor(Color.argb(scaleFactor, 255, 0, 0));
            mLeft.setBackgroundColor(Color.TRANSPARENT);
         }

         if(y > 0) {
             scaleFactor = (int)Math.min(y * 255, 255);
            mTop.setBackgroundColor(Color.TRANSPARENT);
            mBottom.setBackgroundColor(Color.argb(scaleFactor, 255, 0, 0));
         } else {
             scaleFactor = (int)Math.min(Math.abs(y) * 255, 255);
            mTop.setBackgroundColor(Color.argb(scaleFactor, 255, 0, 0));
            mBottom.setBackgroundColor(Color.TRANSPARENT);
         }
        //Display the raw values
         rawValueView.setText(String.format("X: %1$1.2f, Y: %2$1.2f, Z: %3$1.2f",
                values[0], values[1], values[2]));
        
        double totalaccleration = Math.sqrt(values[0]*values[0] + values[1] * values[1] + values[2] * values[2]);
        totalAcclerationView.setText("Total accleration: "+ totalaccleration);
        
       
        if(totalacclerationmax<totalaccleration){
        	totalacclerationmax = totalaccleration;
        	totalAcclerationMaxView.setText("Max Total accleration: " + totalacclerationmax );	
        }
        
        if(totalacclerationmax>=20){
        	
        	Toast.makeText(getApplicationContext(), totalacclerationmax+"", Toast.LENGTH_LONG).show();        	
        	
        	Intent intent = new Intent();   
			intent.setClass(TiltActivity.this, CompassActivity.class);
			intent.putExtra("totalacclerationmax", totalacclerationmax); 
			
			startActivityForResult(intent, SETHEADING);  
        	
        }
 
     }
    
    @Override  
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {       
        super.onActivityResult(requestCode, resultCode, data);        
        if(requestCode==SETHEADING && data != null){  
            setResult(Activity.RESULT_FIRST_USER, data);  
            finish();  
        }  
    }     
}
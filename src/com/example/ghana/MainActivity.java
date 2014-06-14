package com.example.ghana;

import android.support.v7.app.ActionBarActivity;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {
	
	
	private LocationManager locationManager=null;
    private LocationListener locationListener=null;
   
    private Button btnGetLocation = null;
    private TextView editLocation = null; 
   
    private static final String TAG = "Debug";
    private Boolean flag = false;
	
	private static final int CAMERA_REQUEST = 1888; 
    private ImageView imageView;
    private TextView t1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE); 
		Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		final double longitude = location.getLongitude();
		final double latitude = location.getLatitude();
		t1 = (TextView)findViewById(R.id.TextView1);		
		 this.imageView = (ImageView)this.findViewById(R.id.imageView1);
	     Button photoButton = (Button) this.findViewById(R.id.button1);
	     photoButton.setOnClickListener(new View.OnClickListener() {

	            @Override
	            public void onClick(View v) {
	                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
	                startActivityForResult(cameraIntent, CAMERA_REQUEST); 
	                t1.setText("Longitude =  " + longitude + "| Latitude: " + latitude);
	          
	            }
	        });
	      


	}
	

	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {  
            Bitmap photo = (Bitmap) data.getExtras().get("data"); 
            imageView.setImageBitmap(photo);
        }  
    } 

	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}



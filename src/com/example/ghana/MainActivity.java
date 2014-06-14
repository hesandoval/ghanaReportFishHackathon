package com.example.ghana;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Calendar;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import android.support.v7.app.ActionBarActivity;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements LocationListener {
	
	private static final int CAMERA_REQUEST = 1888; 
    private ImageView imageView;
    private Button submit;
    private static final String TAG = "SendingJSON";
    EditText t1;
    double longitude;
	double latitude;
	String longc, latc;
    String photograph;
    String description;
    Location location;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		
		final LocationManager lm = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

	    if ( !lm.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
	        buildAlertMessageNoGps();
	    }
	    
		
		final Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if(location != null && location.getTime() > Calendar.getInstance().getTimeInMillis() - 2 * 60 * 1000) {
			Toast.makeText(getApplicationContext(), "Location recieved.", Toast.LENGTH_SHORT).show();
			latitude = location.getLatitude();
			longitude = location.getLongitude();
			
        }
        else {
            // Request a new location if necessary
        	lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            Toast.makeText(getApplicationContext(), "Receiving location...", Toast.LENGTH_SHORT).show();
        }
		
	
   
		 this.imageView = (ImageView)this.findViewById(R.id.imageView1);
	     Button photoButton = (Button) this.findViewById(R.id.button1);
	     photoButton.setOnClickListener(new View.OnClickListener() {

	            @Override
	            public void onClick(View v) {
	                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
	                startActivityForResult(cameraIntent, CAMERA_REQUEST); 
	                
	          
	            }
	        });
	     
	     t1 = (EditText) findViewById(R.id.EditText1);
	     description = t1.getText().toString();
	     
	     submit = (Button)findViewById(R.id.button2);
	     
	     while(location == null)
	     {
	    	 lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
	    	 Toast.makeText(getApplicationContext(), "Receiving location...", Toast.LENGTH_SHORT).show();
	     }
	     longitude = location.getLongitude();
	     latitude = location.getLatitude();
		 Toast.makeText(getApplicationContext(), "Complete. Longitude = "+longitude + "|| Latitiude = " + latitude, Toast.LENGTH_SHORT).show();
		 
	     submit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "Complete. Longitude = "+longitude + "|| Latitiude = " + latitude, Toast.LENGTH_SHORT).show();
				longc = Double.toString(longitude);
				latc = Double.toString(latitude);
				sendJson(photograph, description, longc, latc);
				
				
			}
		});
	  
	      


	}
	
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
	    if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
	        Bitmap photo = (Bitmap) data.getExtras().get("data"); 
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
	        photo.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object   
	        byte[] b = baos.toByteArray(); 
	        photograph = Base64.encodeToString(b, Base64.DEFAULT);
	        imageView.setImageBitmap(photo);


	    }  
	}

	
	
	public void onLocationChanged(Location location) {
        if (location != null) {
        // Do something withthe current location

        // If you only want one location update un-comment this line
        //mLocationManager.removeUpdates(this);
        }
    }

    public void onProviderDisabled(String arg0) {}
    public void onProviderEnabled(String arg0) {}
    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {}

	
	
	private void buildAlertMessageNoGps() {
		// TODO Auto-generated method stub
		 final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		    builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
		           .setCancelable(false)
		           .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		               public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
		                   startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
		               }
		           })
		           .setNegativeButton("No", new DialogInterface.OnClickListener() {
		               public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
		                    dialog.cancel();
		               }
		           });
		    final AlertDialog alert = builder.create();
		    alert.show();
		
		
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
	
	public void sendJson(final String photo, final String description, final String longc, final String lat) {
		Thread t = new Thread() {

			public void run() {
				HttpClient client = new DefaultHttpClient();
				HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
				//HttpResponse response;
	            JSONObject json = new JSONObject();

	            try {
	            	HttpPost post = new HttpPost("http://192.168.43.115:8000/incident_report/receiveReport");
	            	json.put("photograph", photo);
	            	json.put("description", description);
	            	json.put("long-coordinate", longc);
	            	json.put("lat-coordinate", lat);
	            	StringEntity se = new StringEntity( json.toString());  
	            	se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
	            	post.setEntity(se);
	            	//HttpResponse response = 
	            			client.execute(post);
	            	Log.d(TAG, "JSON file posted");
         

	            } catch(Exception e) {
	            	e.printStackTrace();
	            	//logs to track if connection was established
	            	Log.d(TAG, "Caught Exception");
	            	Log.d(TAG, "Cannot Establish Connection");
	            }

			}
		};
		t.start();      
	}
}

  


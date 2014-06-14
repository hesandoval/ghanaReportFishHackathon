package com.example.ghana;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;

public class Splash extends Activity {

	private final int SPLASH_DISPLAY_LENGHT = 2000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        /* New Handler to start the Menu-Activity 
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
            
        		Intent mainIntent = new Intent(Splash.this, MainActivity.class);
                Splash.this.startActivity(mainIntent);
                Splash.this.finish();
            	
            }
        }, SPLASH_DISPLAY_LENGHT);
    }
}

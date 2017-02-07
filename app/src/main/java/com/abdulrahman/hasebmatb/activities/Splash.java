package com.abdulrahman.hasebmatb.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.abdulrahman.hasebmatb.R;
import com.abdulrahman.hasebmatb.helper.SharedPref;

public class Splash extends AppCompatActivity {


    Handler handler;
    SharedPreferences userSharedPreferences;
    SharedPreferences.Editor editor;
    int progressstatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        userSharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        editor = userSharedPreferences.edit();
        handler = new Handler();

        turnGPSOn();



        new Thread(new Runnable() {
            @Override
            public void run() {
                while (progressstatus < 100) {
                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            progressstatus += 10;
                        }


                    });
                    try {
                        Thread.sleep(100);

                    } catch (InterruptedException e) {
                        e.printStackTrace();

                    }

                };
                if(userSharedPreferences.getInt("userId", 0)==0 ){
                    Intent i = new Intent(Splash.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }else{
                    Intent i = new Intent(Splash.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
//
                Splash.this.finish();
            }
        }).start();


    }

    private void turnGPSOn(){
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if(!provider.contains("gps")){ //if gps is disabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            sendBroadcast(poke);
        }
    }

}

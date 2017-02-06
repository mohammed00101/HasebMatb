package com.abdulrahman.hasebmatb.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
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
}

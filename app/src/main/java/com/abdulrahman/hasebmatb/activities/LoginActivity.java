package com.abdulrahman.hasebmatb.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.abdulrahman.hasebmatb.R;
import com.abdulrahman.hasebmatb.helper.Constants;
import com.abdulrahman.hasebmatb.helper.SharedPref;
import com.abdulrahman.hasebmatb.model.MatbLocation;
import com.abdulrahman.hasebmatb.model.PostModel;
import com.abdulrahman.hasebmatb.service.GPS_Service;
import com.abdulrahman.hasebmatb.service.MyInstanceIDService;
import com.abdulrahman.hasebmatb.webTasks.WebConnectionTask;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LoginActivity extends AppCompatActivity {


    Button loginBtn, skiptoHomeBtn;
    TextView signupTxt, email, password;
    SharedPreferences userSharedPreferences;
    SharedPreferences.Editor editor;
   // private   MyInstanceIDService myInstanceIDService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
      //  myInstanceIDService = new MyInstanceIDService();
        init();



        LocationManager locateManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabled = locateManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
// check if enabled and if not send user to the GPS settings
        if (!enabled) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        } else {



            if (Constants.isFirstTimeRun) {
                // Actual zoom will be performed when location service
                // is connected
                Constants.isFirstTimeRun = false;
                Constants.savePreferences(getApplicationContext());
            }

        } //


        skiptoHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        signupTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    void init() {
        loginBtn = (Button) findViewById(R.id.login_login);
        skiptoHomeBtn = (Button) findViewById(R.id.login_skiptoHome);
        signupTxt = (TextView) findViewById(R.id.login_signupTxt);
        email = (TextView) findViewById(R.id.login_email_editTxt);
        password = (TextView) findViewById(R.id.login_password_editTxt);
        userSharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        editor = userSharedPreferences.edit();

    }


    public void send_login_data(View v) {
        new WebConnectionTask(getBaseContext(), "login.php", new HashMap<String, String>() {{
            put("email", email.getText().toString());
            put("password", password.getText().toString());
            put("token", userSharedPreferences.getString("token",null));

        }}) {
            @Override
            public void onRespnseComplete(String response) {
                try {
                    int userId = Integer.parseInt(response);
                    if (userId == 0) throw new Exception();
                    userSharedPreferences.edit().putInt("userId", userId).commit();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                } catch (Exception ex) {
                    Toast.makeText(LoginActivity.this, "تأكد من الايميل وكلمة السر", Toast.LENGTH_LONG).show();
                }
            }
        };
    }


}






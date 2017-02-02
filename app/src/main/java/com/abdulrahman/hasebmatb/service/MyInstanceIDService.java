package com.abdulrahman.hasebmatb.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.abdulrahman.hasebmatb.webTasks.WebConnectionTask;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.util.HashMap;

/**
 * Created by Mohamed Yossif on 30/01/2017.
 */
public class MyInstanceIDService extends FirebaseInstanceIdService {

    private String token;
    private int userId;
    private SharedPreferences userSharedPreferences;


    @Override
    public void onCreate() {
        Log.d(">>>>>>>>>.","instanc create");
        super.onCreate();
    }

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        userSharedPreferences = getSharedPreferences("user", MODE_PRIVATE);

        userSharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        userId = userSharedPreferences.getInt("userId", 0);
        token = FirebaseInstanceId.getInstance().getToken();
        userSharedPreferences.edit().putString("token",token).commit();
        Log.d(">>>>>>>>>token : ",token);


        if (userId != 0){


            new WebConnectionTask(getBaseContext(), "userToken.php", new HashMap<String, String>() {{
            put("id", ""+ userSharedPreferences.getInt("userId", 0) );
            put("token",token);

        }}) {
            @Override
            public void onRespnseComplete(String response) {


                Log.d(">>>>>>>>>token : ",response);
            }
        };
      }
    }
}

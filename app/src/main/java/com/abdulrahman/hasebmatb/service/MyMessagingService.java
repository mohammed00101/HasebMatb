package com.abdulrahman.hasebmatb.service;

import android.content.SharedPreferences;
import android.util.Log;

import com.abdulrahman.hasebmatb.helper.Constants;
import com.abdulrahman.hasebmatb.model.MatbLocation;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Mohamed Yossif on 30/01/2017.
 */
public class MyMessagingService extends FirebaseMessagingService {
    private SharedPreferences userSharedPreferences;

   private  MatbLocation matbLocation;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        userSharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        Map  matbLoc =remoteMessage.getData();
        matbLocation = new MatbLocation(Integer .parseInt((String)matbLoc.get("matbId"))
                                   ,Double.parseDouble((String)matbLoc.get("lat"))
                                 ,Double.parseDouble((String)matbLoc.get("lng"))) ;


        if(Constants.MATB_LOCATION == null){
            Constants.MATB_LOCATION =new ArrayList<MatbLocation>();
        }


        Constants.MATB_LOCATION.add(matbLocation);

        Gson gson = new Gson();
        String json = gson.toJson(Constants.MATB_LOCATION);
        userSharedPreferences.edit().putString("Matb", json).commit();
       // Log.d(">>>>>>>>>>",userSharedPreferences.getString("Matb", null));
        GPS_Service.setArrayList();
    }
}

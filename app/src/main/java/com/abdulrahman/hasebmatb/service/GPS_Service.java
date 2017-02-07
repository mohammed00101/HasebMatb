package com.abdulrahman.hasebmatb.service;

/**
 * Created by abdulrahman on 12/26/16.
 */

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.FloatMath;
import android.util.Log;
import android.widget.Toast;

import com.abdulrahman.hasebmatb.R;
import com.abdulrahman.hasebmatb.helper.Constants;
import com.abdulrahman.hasebmatb.model.MatbLocation;
import com.abdulrahman.hasebmatb.model.PostModel;
import com.abdulrahman.hasebmatb.webTasks.WebConnectionTask;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.maps.android.SphericalUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;


public class GPS_Service extends Service {

    private LocationListener listener;
    private LocationManager locationManager;
    public static final int FIVE_MINUTE = 300000;
    public static final int REQUEST_CODE = 111131;
    Notification.Builder mBuilder;
    private static   ArrayList<MatbLocation> arrayList;
    private static  SharedPreferences userSharedPreferences;
    public  CurrentLocation currentLocation;
    private  Location nextMatb ;
    private static  SharedPreferences.Editor editor;
    private  double newdDistance =0  , oldDistance = 0;
    public static boolean isServiceRuning = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        userSharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
//
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Task().execute();
//        setArrayList();
//
//        isServiceRuning = true;
//        currentLocation = new CurrentLocation(getBaseContext()){
//            @Override
//            public void currentLocation(Location location) {
//                super.currentLocation(location);
//
//                nextMatb = new Location(location.getProvider());
//                for (MatbLocation matb : arrayList) {
//                    nextMatb.setLatitude(matb.getLat());
//                    nextMatb.setLongitude(matb.getLng());
////                    latLng2=new LatLng(nextMatb.getLatitude(),nextMatb.getLongitude());
//                    newdDistance = location.distanceTo(nextMatb);
//                    //Log.d(">>>>>>>>>>>>.",location.getLatitude()+"   "+location.getLongitude()+" 1+2 "+nextMatb.getLatitude()+"   "+  nextMatb.getLongitude()+"="+dist);
////                   dist=distanceBetween(latLng1,latLng2);
//                    if(newdDistance>= 0  && newdDistance <Constants.DISTINATION  ){
//                        Constants.RunAlarm(getBaseContext());
//                       // oldDistance = newdDistance;
//                    }
//                }
//                //send Notification
//            }
//        };

        return START_STICKY;
    }

    @Override
    public void onDestroy() {

        isServiceRuning = false;
    }

    public static  void setArrayList() {
        editor=userSharedPreferences.edit();
        if(userSharedPreferences.getString("Matb", null) !=null){
            Gson gson = new Gson();
            String json = userSharedPreferences.getString("Matb", null);
            Type type = new TypeToken<  ArrayList<MatbLocation> >() {}.getType();
            arrayList= gson.fromJson(json, type);
        }
    }


    class  Task extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {

//            while (true){
                setArrayList();


            isServiceRuning = true;
                currentLocation = new CurrentLocation(getBaseContext()){
                    @Override
                    public void currentLocation(Location location) {
                        super.currentLocation(location);

                        nextMatb = new Location(location.getProvider());
                        for (MatbLocation matb : arrayList) {
                            nextMatb.setLatitude(matb.getLat());
                            nextMatb.setLongitude(matb.getLng());
//                    latLng2=new LatLng(nextMatb.getLatitude(),nextMatb.getLongitude());
                            newdDistance = location.distanceTo(nextMatb);
                            //Log.d(">>>>>>>>>>>>.",location.getLatitude()+"   "+location.getLongitude()+" 1+2 "+nextMatb.getLatitude()+"   "+  nextMatb.getLongitude()+"="+dist);
//                   dist=distanceBetween(latLng1,latLng2);
                            if(newdDistance>= 0  && newdDistance <Constants.DISTINATION  ){
                                Constants.RunAlarm(getBaseContext());
                                Toast.makeText(GPS_Service.this, "lllllllllllllllllllllllllllllllllll", Toast.LENGTH_SHORT).show();
                                // oldDistance = newdDistance;
                            }
                        }
                        //send Notification
                    }
                };
//            }
            return null;

        }
    }
}

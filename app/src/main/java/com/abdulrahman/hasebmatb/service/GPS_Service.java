package com.abdulrahman.hasebmatb.service;

/**
 * Created by abdulrahman on 12/26/16.
 */

import android.Manifest;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.abdulrahman.hasebmatb.helper.Constants;
import com.abdulrahman.hasebmatb.model.MatbLocation;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class GPS_Service extends Service {

    private LocationListener listener;
    private LocationManager locationManager;
    public static final int FIVE_MINUTE = 300000;
    public static final int REQUEST_CODE = 111131;
    Notification.Builder mBuilder;
    private static   ArrayList<MatbLocation> arrayList;
    private static  SharedPreferences userSharedPreferences;
    CurrentLocation1 currentLocation;
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
        try {
            locationManager = (LocationManager) this.getSystemService(this.LOCATION_SERVICE);
//
            setArrayList();


            isServiceRuning = true;
            currentLocation = new CurrentLocation1(getBaseContext()){
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
                            // oldDistance = newdDistance;
                        }
                    }
                    //send Notification
                }
            };
//            }

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(locationManager.NETWORK_PROVIDER, 20000, 0, currentLocation);



            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 20000, 0, currentLocation);

        }catch (Exception e){}
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Task().execute();
//        setArrayList();
//
//        isServiceRuning = true;
//        currentLocation = new CurrentLocation1(getBaseContext()){
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
                currentLocation = new CurrentLocation1(getBaseContext()){
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



   private class CurrentLocation1 implements LocationListener {
        private LocationManager locationManager;
        private Location location;
        private Context context;

        public CurrentLocation1(Context context) {
            this.context = context;
            try {
                locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
//
                // Getting Current Location From GPS
                location = getLastKnownLocation();

                if (location != null) {
                    onLocationChanged(location);
                }

                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                locationManager.requestLocationUpdates(location.getProvider(), 20000, 0, this);

            }catch (Exception e){}



        }

        @Override
        public void onLocationChanged(Location location) {
            currentLocation(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

            location = getLastKnownLocation();
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(location.getProvider(), 20000, 0, this);
        }

        @Override
        public void onProviderDisabled(String provider) {

        } public Location getLastKnownLocation() {
            Location bestLocation = null;

            try {
                locationManager = (LocationManager) context.getApplicationContext().getSystemService(context.LOCATION_SERVICE);
                List<String> providers = locationManager.getProviders(true);
                for (String provider : providers) {
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return null;
                    }
                    Location l = locationManager.getLastKnownLocation(provider);
                    if (l == null) {
                        continue;
                    }
                    if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                        // Found best last known location: %s", l);
                        bestLocation = l;
                    }
                }
            }catch (Exception e){
            }
            return bestLocation;
        }



        public void  currentLocation(Location location){};
    }

}

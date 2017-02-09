package com.abdulrahman.hasebmatb.activities;


import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;

import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.abdulrahman.hasebmatb.R;
import com.abdulrahman.hasebmatb.adapter.DrawerItemCustomAdapter;
import com.abdulrahman.hasebmatb.fragments.DrawerFragment;
import com.abdulrahman.hasebmatb.helper.Constants;
import com.abdulrahman.hasebmatb.model.MatbLocation;
import com.abdulrahman.hasebmatb.model.DataModel;
import com.abdulrahman.hasebmatb.service.GPS_Service;
import com.abdulrahman.hasebmatb.webTasks.WebConnectionTask;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


//import com.google.android.gms.location.LocationListener;


public class MainActivity extends AppCompatActivity implements DrawerFragment.FragmentDrawerListener, LocationListener {

    private BroadcastReceiver broadcastReceiver;
    ///--------------

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private String[] mNavigationDrawerItemTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    Toolbar toolbar;
    public static ProgressDialog pd;
    private CharSequence mDrawerTitle;
    ActionBarDrawerToggle toggle;
    DrawerFragment drawerFragment;
    private CharSequence mTitle;
    private GoogleMap mMap;
    private LatLng mCenterLatLong;
    double userLat, userLong;
    private double mLatitude = 0;
    private double mLongitude = 0;
    SupportMapFragment mapFragment;
    android.support.v7.app.ActionBarDrawerToggle mDrawerToggle;
    private Location location;
    private  SharedPreferences userSharedPreferences;
    private LocationManager locationManager;
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.abdulrahman.hasebmatb.R.layout.activity_main);

        checkPlayServices();
        if(!GPS_Service.isServiceRuning)
            startService(new Intent(getApplicationContext(), GPS_Service.class));

      //  MyInstanceIDService.r


        userSharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        editor=userSharedPreferences.edit();
        get_matb_Location();
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);


        LocationManager locateManager=(LocationManager)getSystemService(LOCATION_SERVICE);
        boolean enabled = locateManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
// check if enabled and if not send user to the GPS settings
        if (!enabled) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }else {
            if (Constants.isFirstTimeRun) {
                // Actual zoom will be performed when location service
                // is connected

                Constants.isAlarmOn=true;
                Constants.isUseVibrate=true;
                Constants.isFirstTimeRun = false;
                Constants.savePreferences(getApplicationContext());
            }

            Constants.restorePreferences(getApplicationContext());
            int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

            if (status != ConnectionResult.SUCCESS) { // Google Play Services are not available

                int requestCode = 10;
                Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
                dialog.show();

            } else { // Google Play Services are available

                // Getting LocationManager object from System Service LOCATION_SERVICE
                locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

                // Getting Current Location From GPS

                location = getLastKnownLocation();

                if (location != null) {
                    onLocationChanged(location);


                    locationManager.requestLocationUpdates(location.getProvider(), 20000, 0, this);
                }
                // Initializing

                // Getting reference to SupportMapFragment of the activity_main
                SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

                fm.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        try {
                            mMap = googleMap;
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));

                            if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                return;
                            }

                            // mGoogleMap.setMyLocationEnabled(true);
                            mMap.clear();
                            LatLng startPoint = new LatLng(mLatitude, mLongitude);
                            // draw the marker at the current position
                            drawMarker(startPoint);
                        } catch (Exception e) {

                        }

                    }
                });
            }


//            if (!runtime_permissions()) {
//                Intent i = new Intent(getApplicationContext(), GPS_Service.class);
//                startService(i);
//            }

        } //
        //--------------------------------------------------------------------
        mTitle = mDrawerTitle = getTitle();
        mNavigationDrawerItemTitles = getResources().getStringArray(R.array.navigation_drawer_items_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        setupToolbar();

        DataModel[] drawerItem = new DataModel[4];

        drawerItem[0] = new DataModel(R.drawable.home, "الرئيسيه");
        drawerItem[1] = new DataModel(R.drawable.news, "اخبار الطرق ");
        drawerItem[2] = new DataModel(R.drawable.setting, "اعدادات");
        if( userSharedPreferences.getInt("userId", 0)==0){
            drawerItem[3] = new DataModel(R.drawable.logout, "تسجيل الدخول");
           // startService(new Intent(this,MyInstanceIDService.class ));

        }else {
            drawerItem[3] = new DataModel(R.drawable.logout, "تسجيل الخروج");
        }


        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);

        DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this, R.layout.list_view_item_row, drawerItem);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            toolbar.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }

        //Here Is The Fragment
        drawerFragment = (DrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, mDrawerLayout, toolbar);
        drawerFragment.setDrawerListener(this);
    }

    void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        selectItem(position);
    }

    @Override
    public void onLocationChanged(Location location) {

        // Draw the marker, if destination location is not set
      //  Log.d(">>>>>>>>>>>>.", "" + location.getLongitude());

        mLatitude = location.getLatitude();
        mLongitude = location.getLongitude();
        LatLng point = new LatLng(mLatitude, mLongitude);

        try {
            mMap.clear();
            drawMarker(point);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(point));
            //  mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(14));

            mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));


        } catch (Exception e) {

        }

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {
        location = getLastKnownLocation();
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
        if(location != null)
        locationManager.requestLocationUpdates(location.getProvider(), 20000, 0, this);

    }


    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }

    }

    private void selectItem(int position) {

//        Fragment fragment = null;

        switch (position) {
            case 0:
                mDrawerLayout.closeDrawer(Gravity.RIGHT);
                break;
            case 1:
                mDrawerLayout.closeDrawer(Gravity.RIGHT);
                startActivity(new Intent(MainActivity.this, PostsActivity.class));
                break;
            case 2:
                mDrawerLayout.closeDrawer(Gravity.RIGHT);
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                break;
            case 3:
                userSharedPreferences.edit().putInt("userId",0).commit();
                Intent  intent1=new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent1);
                finish();
                break;

            default:
                break;
        }

    }

    ////////// PLAY SERVICE
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                //finish();
            }
            return false;
        }
        return true;
    }

    private void drawMarker(LatLng point) {

        try {
            // Creating MarkerOptions
            MarkerOptions options = new MarkerOptions();
            // Setting the position of the marker
            options.position(point);

            Marker m = mMap.addMarker(options);
            m.setPosition(point);
            m.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.new_marker));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));

//            mMap.addMarker(options);
        } catch (Exception e) {

        }

    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == 100) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
//                Intent i = new Intent(getApplicationContext(), GPS_Service.class);
//                startService(i);
//            } else {
//                runtime_permissions();
//            }
//        }
//    }

    private boolean runtime_permissions() {
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
            return true;
        }
        return false;
    }
//
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    private void checkAlarm() {
        String action = getIntent().getAction();
        if (TextUtils.equals(action, Constants.ACTION_STOP_ALARM)) {
            stopAlarm();
        }
    }

    private void stopAlarm() {
        // Stop
        // nothing to do
    }
    public void push_matb(View view) {
        if( userSharedPreferences.getInt("userId", 0)==0){
            Toast.makeText(this, "برجاء تسجيل دخولك لتتمكن من اضافة مطب", Toast.LENGTH_LONG).show();

        }else {
            try{
                if (location!=null)   {
                    new WebConnectionTask(getBaseContext(), "insertMatb.php", new HashMap<String, String>() {{
                        put("userId", "" +userSharedPreferences.getInt("userId", 0));
                        String lat=location.getLatitude()+"";
                        String longt=location.getLongitude()+"";
                        put("lat", lat );
                        put("lng", longt );
                    }}) {
                        @Override
                        public void onRespnseComplete(String response) {
                            Toast.makeText(MainActivity.this, "تم اضافة المطب بنجاح", Toast.LENGTH_LONG).show();
                        }
                    };
                }

            }catch (Exception e){

            }
        }
    }
    private Location getLastKnownLocation() {
        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        return bestLocation;
    }


    public void get_matb_Location() {
        new WebConnectionTask(getBaseContext(), "getMatb.php", null) {
            @Override
            public void onRespnseComplete(String response) {
                try {

                    Gson gson = new Gson();
                    Constants.MATB_LOCATION = new ArrayList<MatbLocation>(Arrays.asList(gson.fromJson(response, MatbLocation[].class)));

                    if (userSharedPreferences.getString("Matb", null) ==null){
                        String json = gson.toJson(Constants.MATB_LOCATION);
                        editor.putString("Matb", json);
                        editor.commit();
                    }


                } catch (Exception ex) {
                    ex.printStackTrace();

                }
            }
        };
    }
}

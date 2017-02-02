package com.abdulrahman.hasebmatb.activities;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.Toast;

import com.abdulrahman.hasebmatb.R;
import com.abdulrahman.hasebmatb.adapter.PostAdapter;
import com.abdulrahman.hasebmatb.helper.CustomButton;
import com.abdulrahman.hasebmatb.helper.CustomEditText;
import com.abdulrahman.hasebmatb.model.PostModel;
import com.abdulrahman.hasebmatb.service.CurrentLocation;
import com.abdulrahman.hasebmatb.webTasks.WebConnectionTask;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class PostsActivity extends AppCompatActivity {


    private SharedPreferences userSharedPreferences;
    private Location location;
    private List<PostModel> posts;
    private CurrentLocation currentLocation;
    private Calendar cal;
    private ListView postsListView;
    private PostAdapter postAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        init();
        pDialog = new ProgressDialog(PostsActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();
        new WebConnectionTask(getBaseContext(),"getPosts.php",null) {
            @Override
            public void onRespnseComplete(String response) {
                try {
                    Gson gson = new Gson();
                    posts = Arrays.asList(gson.fromJson(response, PostModel[].class));
                    postAdapter=new PostAdapter(posts,PostsActivity.this);
                    postsListView.setAdapter(postAdapter);
                    pDialog.dismiss();
                }catch (Exception ex){
                    Toast.makeText(PostsActivity.this, "تأكد من الاتصال بالانترنت", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                }

            }
        };
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new WebConnectionTask(getBaseContext(),"getPosts.php",null) {
                    @Override
                    public void onRespnseComplete(String response) {
                        try {
                            Gson gson = new Gson();
                            posts = Arrays.asList(gson.fromJson(response, PostModel[].class));
                            postAdapter=new PostAdapter(posts,PostsActivity.this);
                            postsListView.setAdapter(postAdapter);
                        }catch (Exception ex){
                        }
                    }
                };
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }

                }, 1800);
            }
        });
    }
    void init() {
        userSharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        postsListView= (ListView) findViewById(R.id.posts_listview);
        currentLocation = new CurrentLocation(getBaseContext());
        swipeRefreshLayout= (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

    }

    public  void push_post(View view){

        if(userSharedPreferences.getInt("userId",0)==0){
            Toast.makeText(this, "برجاء تسجيل الدخول لتتمكن من كتابة خبر", Toast.LENGTH_SHORT).show();

        }else{
            final Dialog dialog = new Dialog(PostsActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
            dialog.setContentView(R.layout.feed_back_dialog);
            final CustomEditText postTxt= (CustomEditText) dialog.findViewById(R.id.postDetail);
            CustomButton writePostBtn= (CustomButton) dialog.findViewById(R.id.done);
            writePostBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+1:00"));
                    new WebConnectionTask(getBaseContext(),"posts.php",new HashMap<String , String>(){{
                        int userId=userSharedPreferences.getInt("userId",0);
                        String timeNow=String.valueOf(new Date().getTime());
                        String location=getLocationName();
                        put("userId",""+userId);
                        put("time",timeNow );
                        put("text",postTxt.getText().toString());
                        put("location", location);
                    }}) {
                        @Override
                        public void onRespnseComplete(String response) {
                            Log.d(">>n",response);
                            new WebConnectionTask(getBaseContext(),"getPosts.php",null) {
                                @Override
                                public void onRespnseComplete(String response) {
                                    try {
                                        Gson gson = new Gson();
                                        posts = Arrays.asList(gson.fromJson(response, PostModel[].class));
                                        postAdapter=new PostAdapter(posts,PostsActivity.this);
                                        postsListView.setAdapter(postAdapter);
                                    }catch (Exception ex){

                                        Toast.makeText(PostsActivity.this, ""+ex.getMessage(), Toast.LENGTH_SHORT).show();
                                        Toast.makeText(PostsActivity.this, "----------", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            };
                        }
                    };
                    dialog.dismiss();
                }
            });
            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(true);
            dialog.show();




        }


    }

    public  String getLocationName(){
        Geocoder geocoder = new Geocoder(this,Locale.getDefault());
        String fnialAddress = null;
        location = currentLocation.getLastKnownLocation();

        StringBuilder builder = new StringBuilder();
        try {

            List<Address> address = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(), 2);
            int maxLines = address.get(0).getMaxAddressLineIndex();

            String address1 = address.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = address.get(0).getLocality();
            String state = address.get(0).getAdminArea();
            String country = address.get(0).getCountryName();
            String postalCode = address.get(0).getPostalCode();
            String knownName = address.get(0).getFeatureName();
            String knownName1 = address.get(0).getSubAdminArea();
//            for (int i=0; i<maxLines; i++) {
//                String addressStr = address.get(0).getAddressLine(i);
//                builder.append(addressStr);
//                builder.append(" ");
//            }

            fnialAddress=city+"."+knownName1;
//            fnialAddress = builder.toString(); //This is the complete address.
        } catch (Exception e) {
            e.printStackTrace();
        }

        return  fnialAddress;
    }



    public List<PostModel> get_posts(){

        return  posts;
    }




}

package com.abdulrahman.hasebmatb.webTasks;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONObject;

import java.net.CacheRequest;
import java.net.MalformedURLException;
import java.net.URL;

import java.util.Map;

/**
 * Created by Mohamed Yossif on 01/01/2017.
 */
public abstract class WebConnectionTask {

    private String baseUrl = "http://matbapp.16mb.com/";
    private String urlRequest;
    private String respnse;
    private RequestQueue requestQ;
    private Context context;

    public WebConnectionTask(Context context, String page, final Map<String, String> parameters) {

        urlRequest = baseUrl + page;
        this.context = context;

       /* Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024);
        Network network = new BasicNetwork(new HurlStack());


        requestQ = new RequestQueue(cache,network);
        requestQ.start();*/


        requestQ =Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlRequest, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                    onRespnseComplete(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(">>>>>>...",""+error.getMessage());



            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return parameters;
            }
        };


        requestQ.add(stringRequest);

    }

    public URL getBaseUrl() throws MalformedURLException {

        return new URL(baseUrl);
    }

    public  abstract void  onRespnseComplete(String response);
}

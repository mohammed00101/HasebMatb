package com.abdulrahman.hasebmatb.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.abdulrahman.hasebmatb.helper.Constants;

/**
 * Created by Mohamed Yossif on 31/12/2016.
 */
public class BootBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

            Constants.GPS_Service =new Intent(context,GPS_Service.class);
            context.startService(Constants.GPS_Service);
    }
}

package com.abdulrahman.hasebmatb.helper;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.abdulrahman.hasebmatb.R;
import com.abdulrahman.hasebmatb.activities.MainActivity;
import com.abdulrahman.hasebmatb.model.MatbLocation;
import com.abdulrahman.hasebmatb.service.GPS_Service;

import java.util.List;

/**
 * Created by abdulrahman on 12/26/16.
 */

public class Constants {


    public  static Intent GPS_Service;

    // Milliseconds per second
    public final static String PREF_RINGTONE_URI = "ringtoneUri";
    public static final String ACTION_STOP_ALARM = "StopAlarm";
    public static int DISTINATION=200;

    public final static String PREF_USE_VIBRATE = "useVibrate";
    public final static String PREF_IS_ALARM_ON = "isAlarmOn";
    public final static String PREF_ALARM_SET_TIME = "alarmSetTime";
    public final static String PREF_IS_FIRST_TIME_RUN = "isFirstTimeRun";


    public static boolean isFirstTimeRun;
    public static  Uri ringtoneUri;

    public static boolean isUseVibrate=true;
    public static long alarmSetTime;
    public static boolean isAlarmOn=true;

    public static List<MatbLocation> MATB_LOCATION;
    private static final int MILLISECONDS_PER_SECOND = 1000;
    // Update frequency in seconds
    private static final int UPDATE_INTERVAL_IN_SECONDS = 60;
    // Update frequency in milliseconds
    public static final long UPDATE_INTERVAL = MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS;
    // The fastest update frequency, in seconds
    private static final int FASTEST_INTERVAL_IN_SECONDS = 60;
    // A fast frequency ceiling in milliseconds
    public static final long FASTEST_INTERVAL = MILLISECONDS_PER_SECOND * FASTEST_INTERVAL_IN_SECONDS;
    // Stores the lat / long pairs in a text file
    public static final String LOCATION_FILE = "sdcard/location.txt";
    // Stores the connect / disconnect data in a text file
    public static final String LOG_FILE = "sdcard/log.txt";


    public static final String RUNNING = "runningInBackground"; // Recording data in background

    public static final String APP_PACKAGE_NAME = "com.abdulrahman.hasebmatb";

    /**
     * Suppress default constructor for noninstantiability
     */


    private Constants() {
        throw new AssertionError();
    }

    public static void RunAlarm (Context context){
//

        if(isAlarmOn){

            SharedPreferences settings = PreferenceManager
                    .getDefaultSharedPreferences(context);
            if (isFirstTimeRun) {
                // Default alarm sound
                Uri defaultUri = RingtoneManager
                        .getDefaultUri(RingtoneManager.TYPE_ALARM);
                if (defaultUri == null) {
                    defaultUri = RingtoneManager
                            .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    if (defaultUri == null) {
                        defaultUri = RingtoneManager
                                .getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                    }
                }
                ringtoneUri = defaultUri;
                sendNotification(ringtoneUri,isUseVibrate,context);

            } else {
                String sRingtoneUri = settings.getString(
                        PREF_RINGTONE_URI, null);
                if (sRingtoneUri == null) {
                    ringtoneUri = null;
                } else {
                    ringtoneUri = Uri.parse(sRingtoneUri);
                }
                sendNotification(ringtoneUri,isUseVibrate,context);
            }

        }else {
            Toast.makeText(context, "برجاء تفعيل منبه المطبات ", Toast.LENGTH_SHORT).show();
        }

    }




    public  static  void restorePreferences(Context context) {
        SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);

        isFirstTimeRun = settings.getBoolean(
                "isFirtTimeRun", true);

        if (isFirstTimeRun) {
            // Default alarm sound
            Uri defaultUri = RingtoneManager
                    .getDefaultUri(RingtoneManager.TYPE_ALARM);
            if (defaultUri == null) {
                defaultUri = RingtoneManager
                        .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                if (defaultUri == null) {
                    defaultUri = RingtoneManager
                            .getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                }
            }
            ringtoneUri = defaultUri;
        } else {
            String sRingtoneUri = settings.getString(
                    PREF_RINGTONE_URI, null);
            if (sRingtoneUri == null) {
                ringtoneUri = null;
            } else {
                ringtoneUri = Uri.parse(sRingtoneUri);
            }

  }

        isUseVibrate = settings.getBoolean(PREF_USE_VIBRATE,
                false);

        isAlarmOn = settings.getBoolean(PREF_IS_ALARM_ON, false);

        alarmSetTime = settings.getLong(PREF_ALARM_SET_TIME, 0);
    }



    //Save Prefrences
    public static void savePreferences(Context context) {
        SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();

        editor.putBoolean(PREF_IS_FIRST_TIME_RUN, isFirstTimeRun);

        if (ringtoneUri != null) {
            editor.putString(PREF_RINGTONE_URI,
                    ringtoneUri.toString());
        } else {
            editor.putString(PREF_RINGTONE_URI, null);
        }
        editor.putBoolean(PREF_USE_VIBRATE, isUseVibrate);
        editor.putBoolean(PREF_IS_ALARM_ON, isAlarmOn);
        editor.putLong(PREF_ALARM_SET_TIME, alarmSetTime);

        // no save for locationTechnique : managed by PreferenceManager

        editor.commit();
    }




    public static void sendNotification(Uri ringtoneUri, boolean isUseVibrate,Context context) {
        restorePreferences(context);
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        // Create an explicit content Intent that starts the main Activity
        Intent notificationIntent = new Intent(context,
                MainActivity.class);

        //TODO Action Stop Alarm
//        notificationIntent.setAction(GeoAlarmUtils.ACTION_STOP_ALARM);

        // Construct a task stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);

        // Adds the main Activity to the task stack as the parent
        stackBuilder.addParentStack(MainActivity.class);

        // Push the content Intent onto the stack
        stackBuilder.addNextIntent(notificationIntent);

        // Get a PendingIntent containing the entire back stack
        PendingIntent notificationPendingIntent = stackBuilder
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // Get a notification builder that's compatible with platform versions
        // >= 4
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                context);


        // Set the notification contents
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(
                        context.getString(R.string.notification_title))
                .setContentText(
                        context.getString(R.string.notification_text))
                .setContentIntent(notificationPendingIntent)
                .setAutoCancel(true);

        if (ringtoneUri != null) {
            builder.setSound(ringtoneUri,
                    Notification.STREAM_DEFAULT);
        }

        if (isUseVibrate) {
            builder.setVibrate(new long[] { 0, 200, 1000, 200, 1000, 200, 1000,
                    200, 1000 });
        }

        // Get an instance of the Notification manager
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

//        // Issue the notification
        mNotificationManager.notify(1,
                builder.build());
    }

}

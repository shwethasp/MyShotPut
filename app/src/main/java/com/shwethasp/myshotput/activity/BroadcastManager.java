package com.shwethasp.myshotput.activity;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.ServiceCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.shwethasp.myshotput.R;
import com.shwethasp.myshotput.SQLite.DBHelper;
import com.shwethasp.myshotput.Service.MyAlarmService;
import com.shwethasp.myshotput.model.ModelClass;
import com.shwethasp.myshotput.model.ModelDateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static android.app.ActivityManager.*;

/**
 * Created by shwethap on 29-04-2016.
 */

//WakefulBroadcastReceiver
public class BroadcastManager extends BroadcastReceiver {
    SetupActivity mSetupActivity;
    DBHelper db;
    public static ArrayList<String> notiArrayList;
    ArrayList<String> ascending;
    public static Context context;
    private SharedPreferences sharedpreferences;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("broadcast broadcast", "check notification");
        mSetupActivity = new SetupActivity();
        this.context = context;
        sharedpreferences = context.getSharedPreferences(ModelClass.MyPREFERENCES, Context.MODE_PRIVATE);
        boolean notifying = sharedpreferences.getBoolean(ModelClass.notifybutton, false);
        if(notifying)
            Notification(context, "Your shotput Alert");
    }

    public void Notification(Context context, String message) {
        // Set Notification Title
        String strtitle = context.getString(R.string.notificationtitle);

        if (isAppIsInBackground(MainActivity.mContext)) {

                Intent intent = new Intent(context, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);



                // Open NotificationView.java Activity
                PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);


                // Create Notification using NotificationCompat.Builder
                NotificationCompat.Builder builder = new NotificationCompat.Builder(
                        context)
                        // Set Icon
                        .setSmallIcon(R.drawable.round_clicked)
                        // Set Ticker Message
                        .setTicker(message)
                        // Set Title
                        .setContentTitle(context.getString(R.string.notificationtitle))
                        // Set Text
                        //           .setContentText(message)
                        // Add an Action Button below Notification
                        //          .addAction(R.drawable.smilingface, "Action Button", pIntent)
                        // Set PendingIntent into Notification
                        .setContentIntent(pIntent)
                        // Dismiss Notification
                        .setAutoCancel(true);

                // Create Notification Manager
                NotificationManager notificationmanager = (NotificationManager) context
                        .getSystemService(Context.NOTIFICATION_SERVICE);
                // Build Notification with Notification Manager
                notificationmanager.notify(0, builder.build());

//            }
        } else {
            if (context != null) {
                Intent intent = new Intent(context, NotificationView.class);
                PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent,
                        0);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(
                        context)
                        // Set Icon
                        .setSmallIcon(R.drawable.round_clicked)
                        // Set Ticker Message
                        .setTicker(message)
                        // Set Title
                        .setContentTitle(context.getString(R.string.notificationtitle))
//                    .setContentIntent(pIntent)
                        // Dismiss Notification
                        .setContentIntent(pIntent)
                        .setAutoCancel(true);
                // Create Notification Manager
                NotificationManager notificationmanager = (NotificationManager) context
                        .getSystemService(Context.NOTIFICATION_SERVICE);
                notificationmanager.notify(0, builder.build());
            }
        }
    }
    //  }


    // Check for network availability
    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    //check for app foreground or background
    private boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        if (context != null) {
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
                List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
                for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                    if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                        for (String activeProcess : processInfo.pkgList) {
                            if (activeProcess.equals(context.getPackageName())) {
                                isInBackground = false;
                            }
                        }
                    }
                }
            } else {
                List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
                ComponentName componentInfo = taskInfo.get(0).topActivity;
                if (componentInfo.getPackageName().equals(context.getPackageName())) {
                    isInBackground = false;
                }
            }

        }
        return isInBackground;
    }
}
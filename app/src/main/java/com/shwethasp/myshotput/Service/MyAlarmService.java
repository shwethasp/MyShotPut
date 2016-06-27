package com.shwethasp.myshotput.Service;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.provider.Telephony;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.shwethasp.myshotput.R;
import com.shwethasp.myshotput.SQLite.DBHelper;
import com.shwethasp.myshotput.activity.BroadcastManager;
import com.shwethasp.myshotput.activity.MainActivity;
import com.shwethasp.myshotput.activity.NotificationView;
import com.shwethasp.myshotput.activity.SetupActivity;
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

/**
 * Created by shwethap on 31-05-2016.
 */
public class MyAlarmService extends Service {
    SetupActivity mSetupActivity;
    DBHelper db;
    public static ArrayList<String> notiArrayList;
    ArrayList<String> ascending;
    public static Context context;
    private NotificationManager mManager;
    private SharedPreferences sharedpreferences;
    private Handler handler;


    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     * <p/>
     * /** @param name Used to name the worker thread, important only for debugging.
     */

   /* public MyAlarmService(Context context) {
        super();
//        mSetupActivity = new SetupActivity();
        this.context = context;
    }*/
    public long AlarmTime() {
        // mSetupActivity =this;
        db = new DBHelper(this);
        ArrayList<ModelDateTime> notiArrayList1 = (ArrayList<ModelDateTime>) db.getAllTimeDate();
        if (notiArrayList1.size() == 0) {
            MainActivity.timedisplaytext.setText("OK, for current shotput is Disabled,Enable or Exit");
        } else if (notiArrayList1.size() != 0) {
            notiArrayList = new ArrayList<String>();
            for (int d = 0; d < notiArrayList1.size(); d++) {
                Log.e(" time is ", notiArrayList1.get(d).getTime());
                Log.e("date is ", notiArrayList1.get(d).getDate());
                String currentdate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                String strDate1 = currentdate;
                String strDate2 = notiArrayList1.get(d).getDate();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date currentdates = null;
                try {
                    currentdates = sdf.parse(strDate1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Date appdate = null;
                try {
                    appdate = sdf.parse(strDate2);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (notiArrayList1.get(d).getDate().equals("0") || appdate.before(currentdates)) {

                    notiArrayList.add(notiArrayList1.get(d).getTime());
                }
                //  mArrayList = db.getAllTimeBydate();
            }
            if (notiArrayList.size() != 0)
                Collections.sort(notiArrayList);
            ascending = new ArrayList<String>();
            for (int i = 0; i < notiArrayList.size(); i++) {
                String store = notiArrayList.get(i);

           /* if((new Date(ConvertTimes(store)).equals(new Date(CurrentTime())))){
                ascending.add(store);
            }*/
                String checkTime = new SimpleDateFormat("HH:mm").format(new Date());

                if (new Date(ConvertTimes(store)).after(new Date(CurrentTime()))) {
                    ascending.add(store);
                    Log.e("notification greater array list", store);
                }
                if (checkTime.contentEquals(store)) {
                    ascending.add(store);
                }
            }
            if (ascending.size() != 0)
                Collections.sort(ascending);

            ArrayList<String> greater = new ArrayList<String>();
            if (ascending.size() == 0) {
            } else {
                for (int j = 0; j < ascending.size(); j++) {
                    Log.e("sorted greater values", ascending.get(j));
                }
            }
        }

        // ArrayList<String> notiArrayList = db.getAllTime();

        // Calendar calendar = Calendar.getInstance();
        String mPickTime;
        if (ascending != null)
            if (ascending.size() != 0)
                mPickTime = ascending.get(0).toString();
            else
                mPickTime = "Disabled";
        else
            mPickTime = "Disabled";
        String spinner = db.getIntervalbyTime1(mPickTime).toString();
        //   if (!spinner.contentEquals("Select")) {
        Date mBeforeDate = null;
        if (!mPickTime.contentEquals("Disabled") && !spinner.contentEquals("Select")) {
            int mHours = 0, mMinutes = 0;
            if (mPickTime.charAt(0) == '0') {
                mHours = Integer.parseInt(String.valueOf(mPickTime.charAt(1)));
            } else {
                mHours = Integer.parseInt(mPickTime.substring(0, mPickTime.indexOf(":")));
            }

            if (mPickTime.charAt(3) == '0') {
                mMinutes = Integer.parseInt(String.valueOf(mPickTime.charAt(4)));
            } else {
                //mMinutes = Integer.parseInt(mPickTime.substring((mPickTime.indexOf(':') + 1), mPickTime.length()));
                //  mMinutes = Integer.parseInt(mPickTime.substring(3, 4));
                mMinutes = Integer.parseInt(mPickTime.substring(3, 5));
            }

            Date dt = new Date();
            dt.setHours(mHours);
            dt.setMinutes(mMinutes);

            Date B = dt;
            //   Date A = dt;
            //check if it is greater than current time


            mBeforeDate = new Date(B.getTime() - TimeUnit.MINUTES.toMillis(Integer.parseInt(spinner)));
            //Current Time
            String checkTime = new SimpleDateFormat("HH:mm").format(new Date());
            String beforedate = new SimpleDateFormat("HH:mm").format(mBeforeDate);
            if (checkTime.contentEquals(beforedate)) {

                return mBeforeDate.getTime();
            } else {

            }
        }
        return 0;
    }

    private long ConvertTimes(String str) {
        Calendar calendar = Calendar.getInstance();
        String[] a = str.split(":");

        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(a[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(a[1]));
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTimeInMillis();
    }

    private long CurrentTime() {
        long time = System.currentTimeMillis();
        return time;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.e("onCreate", "Service started");
        /*Intent alarmIntent = new Intent(this, BroadcastManager.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        //at specific time
        if (AlarmTime() != 0) {
            Log.e("onCreate","Calling broadcast recvier");
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, AlarmTime(),1000, pendingIntent);
        }*/

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        /*Intent alarmIntent = new Intent(this, BroadcastManager.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);*/
        //at specific time


        CallingThread();


     /*   Log.e("onStartCommand", "onStartCommand");
        sharedpreferences = this.getSharedPreferences(ModelClass.MyPREFERENCES, Context.MODE_PRIVATE);
        boolean notifying = sharedpreferences.getBoolean(ModelClass.notifybutton, false);

        if(notifying) {
            if (AlarmTime() != 0) {
                Log.e("onCreate", "Calling broadcast recvier");
                Notification(this, "Your shotput Alert");
//                alarmManager.set(AlarmManager.RTC_WAKEUP, AlarmTime(), pendingIntent);
            }
        }*/
       // return Service.START_STICKY;


        //  Toast.makeText(getApplicationContext(), "Service Running ", 1).show();
        //   Log.e("OnStartCommand","Service Running");

      /*  sharedpreferences = context.getSharedPreferences(ModelClass.MyPREFERENCES, Context.MODE_PRIVATE);
        boolean notifying = sharedpreferences.getBoolean(ModelClass.notifybutton, false);
        if(notifying)*/

        return super.onStartCommand(intent, flags, startId);

    }

    public void CallingThread() {
        final int RATE = 1000;
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.e("onStartCommand", "onStartCommand");
                sharedpreferences = getSharedPreferences(ModelClass.MyPREFERENCES, Context.MODE_PRIVATE);
                boolean notifying = sharedpreferences.getBoolean(ModelClass.notifybutton, false);

                if (notifying) {
                    if (AlarmTime() != 0) {
                        Log.e("onCreate", "Calling broadcast recvier");
                        Notification(getApplicationContext(), "Your shotput Alert");
                        handler.postDelayed(this,1000);
//                alarmManager.set(AlarmManager.RTC_WAKEUP, AlarmTime(), pendingIntent);
                    }

                }
            }
        }, RATE);


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

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        // Getting Notification Service
       /* mManager = (NotificationManager) this.getApplicationContext()
                .getSystemService(
                        this.getApplicationContext().NOTIFICATION_SERVICE);

		*//*  When the user taps the notification we have to show the Home Screen
         of our App, this job can be done with the help of the following
		 Intent.*//*

        Intent intent1 = new Intent(this.getApplicationContext(), SetupActivity.class);

        Notification notification = new Notification(R.drawable.smilingface,
                "See My App something for you", System.currentTimeMillis());

        intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingNotificationIntent = PendingIntent.getActivity(
                this.getApplicationContext(), 0, intent1,
                PendingIntent.FLAG_UPDATE_CURRENT);

     //   notification.flags |= Notification.FLAG_AUTO_CANCEL;

      *//*notification.setLatestEventInfo(getApplicationContext(),
                "SANBOOK", "See My App something for you",
                pendingNotificationIntent);*//*

        mManager.notify(0, notification);*/
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("onDestroy ", "service destoryed");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

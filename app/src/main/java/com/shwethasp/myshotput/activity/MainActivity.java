package com.shwethasp.myshotput.activity;


import android.app.AlarmManager;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.shwethasp.myshotput.R;
import com.shwethasp.myshotput.SQLite.DBHelper;
import com.shwethasp.myshotput.Service.MyAlarmService;
import com.shwethasp.myshotput.Service.MyService;
import com.shwethasp.myshotput.model.ModelClass;
import com.shwethasp.myshotput.model.ModelDateTime;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import ru.biovamp.widget.CircleLayout;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    int[] bodytypes = {R.drawable.belly, R.drawable.belly4, R.drawable.belly2, R.drawable.belly3};
    public static TextView timedisplaytext, PatternDialogTitle;
    LinearLayout patternup, patterndown;
    CircleLayout circleLayout;
    boolean isPatternclicked = false;
    Dialog mPatternPlusDialog, mPatternMinusDialog;
    boolean patternplusclicked = false;
    boolean patternminusclicked = false;
    boolean isStart = false;
    boolean onLaunch = false;
    boolean onLaunch1 = false;

    private static int countvalue = 0;

    public static ImageView imageView1, imageView2, imageView3, imageView4, imageView5,
            imageView6, imageView7, imageView8, imageView9, imageView10,
            imageView11, imageView12, imageView13,
            bodytype1, faces,
            patterndownimage7, patterndownimage6, patterndownimage5, patterndownimage4, patterndownimage3, patterndownimage2,
            patterndownimage1, patternupimage8, patternupimage9, patternupimage10, patternupimage11, patternupimage12,
            patternupimage13, patternupimage14;
    RelativeLayout patternrelativeLayout;

    public static Button imageminus, imageplus, patternminus, patternplus, setup, image, pattern,
            dialog_no, dialog_send, dialog_cancel, dialog_yes;

    int bodyimagecounter = 0;
    private SharedPreferences sharedpreferences;
    DBHelper db;
    public static ArrayList<String> mArrayList;

    public static ArrayList<ModelDateTime> mTimeDateArray;
    int i, d;
    String a;
    long time;
    public static ArrayList<String> result;
    public static ArrayList<String> datearray;
    public static Context mContext;
    ImageView nextpatternimg, nextimg;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private PendingIntent pendingIntent;
    private AlarmManager alarmManager;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeUI();
        db = new DBHelper(this);
        mContext = this;
        // Create Notification Manager
        NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Dismiss Notification
        notificationmanager.cancel(0);
        sharedpreferences = getApplicationContext().getSharedPreferences(ModelClass.MyPREFERENCES, Context.MODE_PRIVATE);
        boolean patternsave = sharedpreferences.getBoolean(ModelClass.patterns, false); //0 is the default value
        if (patternsave) {
            isPatternclicked = true;

            circleLayout.setVisibility(View.VISIBLE);
            patternup.setVisibility(View.INVISIBLE);
            patterndown.setVisibility(View.INVISIBLE);
            patternplus.setBackgroundColor(getResources().getColor(R.color.plusdisable));
            patternminus.setBackgroundColor(getResources().getColor(R.color.darkblue));
        } else {
            isPatternclicked = false;
            patterndown.setVisibility(View.VISIBLE);
            patternup.setVisibility(View.VISIBLE);
            circleLayout.setVisibility(View.INVISIBLE);
            patternminus.setBackgroundColor(getResources().getColor(R.color.minusdisable));
            patternplus.setBackgroundColor(getResources().getColor(R.color.darkblue));
        }

        int i = sharedpreferences.getInt(ModelClass.images, 0);
        bodytype1.setImageResource(bodytypes[i]);
        bodyimagecounter = i;
        if (bodyimagecounter == 0) {
            imageminus.setBackgroundColor(getResources().getColor(R.color.minusdisable));

        } else if (bodyimagecounter == 3) {
            imageplus.setBackgroundColor(getResources().getColor(R.color.plusdisable));
        }

        SetupActivity.isSoundONclicked = sharedpreferences.getBoolean(ModelClass.soundbutton, false);

        imageplus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (bodyimagecounter < 3) {
                    bodyimagecounter++;
                    bodytype1.setImageResource(bodytypes[bodyimagecounter]);
                    SharedPreferences.Editor imagepluss = sharedpreferences.edit();
                    imagepluss.putInt(ModelClass.images, bodyimagecounter);
                    imagepluss.commit();
                    imageminus.setBackgroundColor(getResources().getColor(R.color.darkgreen));
                    if (bodyimagecounter >= 3)
                        imageplus.setBackgroundColor(getResources().getColor(R.color.plusdisable));
                }
            }
        });

        imageminus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bodyimagecounter > 0) {
                    bodyimagecounter--;
                    bodytype1.setImageResource(bodytypes[bodyimagecounter]);
                    SharedPreferences.Editor imageminuss = sharedpreferences.edit();
                    imageminuss.putInt(ModelClass.images, bodyimagecounter);
                    imageminuss.commit();
                    imageplus.setBackgroundColor(getResources().getColor(R.color.darkgreen));
                    if (bodyimagecounter <= 0) {
                        imageminus.setBackgroundColor(getResources().getColor(R.color.minusdisable));

                    }
                }
            }
        });
        patternminus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPatternclicked) {

                    //mPatternPlusDialog.show();
                    circleLayout.setVisibility(View.INVISIBLE);
                    patternup.setVisibility(View.VISIBLE);
                    patterndown.setVisibility(View.VISIBLE);
                    patternminus.setBackgroundColor(getResources().getColor(R.color.minusdisable));
                    patternplus.setBackgroundColor(getResources().getColor(R.color.darkblue));
                    isPatternclicked = false;

                    SharedPreferences.Editor pattern = sharedpreferences.edit();
                    pattern.putBoolean(ModelClass.patterns, false);
                    pattern.commit();
                    // Toast.makeText(MainActivity.this, "minus pattern saved", Toast.LENGTH_LONG).show();
                }

            }

        });

        patternplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isPatternclicked) {

                    mPatternPlusDialog.show();

                    patternup.setVisibility(View.INVISIBLE);
                    patterndown.setVisibility(View.INVISIBLE);
                    circleLayout.setVisibility(View.VISIBLE);
                    patternplus.setBackgroundColor(getResources().getColor(R.color.plusdisable));
                    patternminus.setBackgroundColor(getResources().getColor(R.color.darkblue));
                    isPatternclicked = true;
                    SharedPreferences.Editor pattern = sharedpreferences.edit();
                    pattern.putBoolean(ModelClass.patterns, true);
                    pattern.commit();


                }


            }
        });
        setup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SetupActivity.class);
                startActivity(i);

            }
        });

        imageView1.setTag(R.drawable.round);
        imageView2.setTag(R.drawable.round);
        imageView3.setTag(R.drawable.round);
        imageView4.setTag(R.drawable.round);
        imageView5.setTag(R.drawable.round);
        imageView6.setTag(R.drawable.round);
        imageView7.setTag(R.drawable.round);
        imageView8.setTag(R.drawable.round);
        imageView9.setTag(R.drawable.round);
        imageView10.setTag(R.drawable.round);
        imageView11.setTag(R.drawable.round);
        imageView12.setTag(R.drawable.round);
        imageView13.setTag(R.drawable.round);

        imageView1.setOnClickListener(this);
        imageView2.setOnClickListener(this);
        imageView3.setOnClickListener(this);
        imageView4.setOnClickListener(this);
        imageView5.setOnClickListener(this);
        imageView6.setOnClickListener(this);
        imageView7.setOnClickListener(this);
        imageView8.setOnClickListener(this);
        imageView9.setOnClickListener(this);
        imageView10.setOnClickListener(this);
        imageView11.setOnClickListener(this);
        imageView12.setOnClickListener(this);
        imageView13.setOnClickListener(this);


        patterndownimage7.setTag(R.drawable.round);
        patterndownimage6.setTag(R.drawable.round);
        patterndownimage5.setTag(R.drawable.round);
        patterndownimage4.setTag(R.drawable.round);
        patterndownimage3.setTag(R.drawable.round);
        patterndownimage2.setTag(R.drawable.round);
        patterndownimage1.setTag(R.drawable.round);
        patternupimage8.setTag(R.drawable.round);
        patternupimage9.setTag(R.drawable.round);
        patternupimage10.setTag(R.drawable.round);
        patternupimage11.setTag(R.drawable.round);
        patternupimage12.setTag(R.drawable.round);
        patternupimage13.setTag(R.drawable.round);
        patternupimage14.setTag(R.drawable.round);

        patterndownimage7.setOnClickListener(this);
        patterndownimage6.setOnClickListener(this);
        patterndownimage5.setOnClickListener(this);
        patterndownimage4.setOnClickListener(this);
        patterndownimage3.setOnClickListener(this);
        patterndownimage2.setOnClickListener(this);
        patterndownimage1.setOnClickListener(this);

        patternupimage8.setOnClickListener(this);
        patternupimage9.setOnClickListener(this);
        patternupimage10.setOnClickListener(this);
        patternupimage11.setOnClickListener(this);
        patternupimage12.setOnClickListener(this);
        patternupimage13.setOnClickListener(this);
        patternupimage14.setOnClickListener(this);


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
       /* Intent intents=new Intent("MyBroadcast");
        intents.setClass(this, BroadcastManager.class);
        this.sendBroadcast(intents);*/

       /* // Retrieve a PendingIntent that will perform a broadcast
        Intent alarmIntent = new Intent(this, BroadcastManager.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
       */

        /*if (result == null || result.size() == 0) {
            //added extra
        } else if (result.size() != 0) {
            if (SetupActivity.isnotifyONclicked) {
                //  if (MainActivity.mContext != null) {

                Intent alarmIntent = new Intent(this, BroadcastManager.class);
                pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                //at specific time
                if (AlarmTime() != 0) {
                    alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, AlarmTime(),10000, pendingIntent);
                    Log.e("Broadcast Receiver Started", String.valueOf(AlarmTime()));

                }
            }
        }*/
        startService(new Intent(getBaseContext(), MyService.class));

        //startService(new Intent(this, MyAlarmService.class));
    }
 /*   // broadcast a custom intent.
    public void broadcastIntent(View view) {
        Intent intent = new Intent();
        intent.setAction("MyBroadcast");
        sendBroadcast(intent);
    }*/
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.dialog_cancel:
                mPatternPlusDialog.dismiss();

                circleLayout.setVisibility(View.INVISIBLE);
                patterndown.setVisibility(View.VISIBLE);
                patternup.setVisibility(View.VISIBLE);
                patternminus.setBackgroundColor(getResources().getColor(R.color.minusdisable));
                patternplus.setBackgroundColor(getResources().getColor(R.color.darkblue));
                isPatternclicked = false;
                break;

            case R.id.dialog_send:
                mPatternPlusDialog.dismiss();
                isStart = true;
                circleLayout.setVisibility(View.VISIBLE);
                patterndown.setVisibility(View.INVISIBLE);
                patternup.setVisibility(View.INVISIBLE);
                patternplus.setBackgroundColor(getResources().getColor(R.color.plusdisable));
                patternminus.setBackgroundColor(getResources().getColor(R.color.darkblue));
                isPatternclicked = true;
                break;

            case R.id.dialog_no:
                mPatternMinusDialog.dismiss();
                circleLayout.setVisibility(View.INVISIBLE);
                patterndown.setVisibility(View.VISIBLE);
                patternup.setVisibility(View.VISIBLE);
                patternminus.setBackgroundColor(getResources().getColor(R.color.minusdisable));
                patternplus.setBackgroundColor(getResources().getColor(R.color.darkblue));


                isPatternclicked = false;
                break;

            case R.id.dialog_yes:
                mPatternMinusDialog.dismiss();
                circleLayout.setVisibility(View.VISIBLE);
                patterndown.setVisibility(View.INVISIBLE);
                patternup.setVisibility(View.INVISIBLE);
                patternplus.setBackgroundColor(getResources().getColor(R.color.plusdisable));
                patternminus.setBackgroundColor(getResources().getColor(R.color.darkblue));
                isPatternclicked = true;
                break;

            case R.id.patterndownimage7:

                if ((Integer) patterndownimage7.getTag() == R.drawable.round) {
                    patterndownimage7.setImageDrawable(getResources().getDrawable(R.drawable.roundred));
                    patterndownimage7.setTag(R.drawable.roundred);
                    if ((Integer) patterndownimage7.getTag() == R.drawable.roundred) {
                        if (db.getTimeHistory(0) == "") {
                            timedisplaytext.setText("You have not used this location");
                        } else {
                            timedisplaytext.setText("Last Used: " + db.getTimeHistory(0));
                        }

                    }
                    ButtonRedtoBlack(patterndownimage6);
                    ButtonRedtoBlack(patterndownimage5);
                    ButtonRedtoBlack(patterndownimage4);
                    ButtonRedtoBlack(patterndownimage3);
                    ButtonRedtoBlack(patterndownimage2);
                    ButtonRedtoBlack(patterndownimage1);
                    ButtonRedtoBlack(patternupimage8);
                    ButtonRedtoBlack(patternupimage9);
                    ButtonRedtoBlack(patternupimage10);
                    ButtonRedtoBlack(patternupimage11);
                    ButtonRedtoBlack(patternupimage12);
                    ButtonRedtoBlack(patternupimage13);
                    ButtonRedtoBlack(patternupimage14);

                    ButtonViolet(patterndownimage6);
                    ButtonViolet(patterndownimage5);
                    ButtonViolet(patterndownimage4);
                    ButtonViolet(patterndownimage3);
                    ButtonViolet(patterndownimage2);
                    ButtonViolet(patterndownimage1);
                    ButtonViolet(patternupimage8);
                    ButtonViolet(patternupimage9);
                    ButtonViolet(patternupimage10);
                    ButtonViolet(patternupimage11);
                    ButtonViolet(patternupimage12);
                    ButtonViolet(patternupimage13);
                    ButtonViolet(patternupimage14);

                } else {
                    changeImage(patterndownimage7, patterndownimage6);
                    if ((Integer) patterndownimage7.getTag() == R.drawable.roundyellow)
                        db.insertShotTime(SaveCurrentShotTime(), 0);

                    patterndownimage5.setImageResource(R.drawable.round);
                    patterndownimage4.setImageResource(R.drawable.round);
                    patterndownimage3.setImageResource(R.drawable.round);
                    patterndownimage2.setImageResource(R.drawable.round);
                    patterndownimage1.setImageResource(R.drawable.round);
                    patternupimage8.setImageResource(R.drawable.round);
                    patternupimage9.setImageResource(R.drawable.round);
                    patternupimage10.setImageResource(R.drawable.round);
                    patternupimage11.setImageResource(R.drawable.round);
                    patternupimage12.setImageResource(R.drawable.round);
                    patternupimage13.setImageResource(R.drawable.round);
                    patternupimage14.setImageResource(R.drawable.round);

                    patterndownimage5.setTag(R.drawable.round);
                    patterndownimage4.setTag(R.drawable.round);
                    patterndownimage3.setTag(R.drawable.round);
                    patterndownimage2.setTag(R.drawable.round);
                    patterndownimage1.setTag(R.drawable.round);
                    patternupimage8.setTag(R.drawable.round);
                    patternupimage9.setTag(R.drawable.round);
                    patternupimage10.setTag(R.drawable.round);
                    patternupimage11.setTag(R.drawable.round);
                    patternupimage12.setTag(R.drawable.round);
                    patternupimage13.setTag(R.drawable.round);
                    patternupimage14.setTag(R.drawable.round);
                }
                break;
            case R.id.patterndownimage6:
                if ((Integer) patterndownimage6.getTag() == R.drawable.round) {
                    patterndownimage6.setImageDrawable(getResources().getDrawable(R.drawable.roundred));
                    patterndownimage6.setTag(R.drawable.roundred);
                    if ((Integer) patterndownimage6.getTag() == R.drawable.roundred) {
                        if (db.getTimeHistory(1) == "") {
                            timedisplaytext.setText("You have not used this location");
                        } else {
                            timedisplaytext.setText("Last Used: " + db.getTimeHistory(1));
                        }

                    }

                    ButtonRedtoBlack(patterndownimage7);
                    ButtonRedtoBlack(patterndownimage5);
                    ButtonRedtoBlack(patterndownimage4);
                    ButtonRedtoBlack(patterndownimage3);
                    ButtonRedtoBlack(patterndownimage2);
                    ButtonRedtoBlack(patterndownimage1);
                    ButtonRedtoBlack(patternupimage8);
                    ButtonRedtoBlack(patternupimage9);
                    ButtonRedtoBlack(patternupimage10);
                    ButtonRedtoBlack(patternupimage11);
                    ButtonRedtoBlack(patternupimage12);
                    ButtonRedtoBlack(patternupimage13);
                    ButtonRedtoBlack(patternupimage14);

                    ButtonViolet(patterndownimage7);
                    ButtonViolet(patterndownimage5);
                    ButtonViolet(patterndownimage4);
                    ButtonViolet(patterndownimage3);
                    ButtonViolet(patterndownimage2);
                    ButtonViolet(patterndownimage1);
                    ButtonViolet(patternupimage8);
                    ButtonViolet(patternupimage9);
                    ButtonViolet(patternupimage10);
                    ButtonViolet(patternupimage11);
                    ButtonViolet(patternupimage12);
                    ButtonViolet(patternupimage13);
                    ButtonViolet(patternupimage14);
                } else {
                    changeImage(patterndownimage6, patterndownimage5);
                    if ((Integer) patterndownimage6.getTag() == R.drawable.roundyellow) {
                        db.insertShotTime(SaveCurrentShotTime(), 1);
                    }
                    patterndownimage7.setImageResource(R.drawable.round);
                    patterndownimage4.setImageResource(R.drawable.round);
                    patterndownimage3.setImageResource(R.drawable.round);
                    patterndownimage2.setImageResource(R.drawable.round);
                    patterndownimage1.setImageResource(R.drawable.round);
                    patternupimage8.setImageResource(R.drawable.round);
                    patternupimage9.setImageResource(R.drawable.round);
                    patternupimage10.setImageResource(R.drawable.round);
                    patternupimage11.setImageResource(R.drawable.round);
                    patternupimage12.setImageResource(R.drawable.round);
                    patternupimage13.setImageResource(R.drawable.round);
                    patternupimage14.setImageResource(R.drawable.round);

                    patterndownimage7.setTag(R.drawable.round);
                    patterndownimage4.setTag(R.drawable.round);
                    patterndownimage3.setTag(R.drawable.round);
                    patterndownimage2.setTag(R.drawable.round);
                    patterndownimage1.setTag(R.drawable.round);
                    patternupimage8.setTag(R.drawable.round);
                    patternupimage9.setTag(R.drawable.round);
                    patternupimage10.setTag(R.drawable.round);
                    patternupimage11.setTag(R.drawable.round);
                    patternupimage12.setTag(R.drawable.round);
                    patternupimage13.setTag(R.drawable.round);
                    patternupimage14.setTag(R.drawable.round);

                }
                break;


            case R.id.patterndownimage5:
                if ((Integer) patterndownimage5.getTag() == R.drawable.round) {
                    patterndownimage5.setImageDrawable(getResources().getDrawable(R.drawable.roundred));
                    // isbuttonRed = true;
                    patterndownimage5.setTag(R.drawable.roundred);
                    if ((Integer) patterndownimage5.getTag() == R.drawable.roundred) {
                        if (db.getTimeHistory(2) == "") {
                            timedisplaytext.setText("You have not used this location");
                        } else {
                            timedisplaytext.setText("Last Used: " + db.getTimeHistory(2));
                        }

                    }

                    ButtonRedtoBlack(patterndownimage6);
                    ButtonRedtoBlack(patterndownimage7);
                    ButtonRedtoBlack(patterndownimage4);
                    ButtonRedtoBlack(patterndownimage3);
                    ButtonRedtoBlack(patterndownimage2);
                    ButtonRedtoBlack(patterndownimage1);
                    ButtonRedtoBlack(patternupimage8);
                    ButtonRedtoBlack(patternupimage9);
                    ButtonRedtoBlack(patternupimage10);
                    ButtonRedtoBlack(patternupimage11);
                    ButtonRedtoBlack(patternupimage12);
                    ButtonRedtoBlack(patternupimage13);
                    ButtonRedtoBlack(patternupimage14);

                    ButtonViolet(patterndownimage6);
                    ButtonViolet(patterndownimage7);
                    ButtonViolet(patterndownimage4);
                    ButtonViolet(patterndownimage3);
                    ButtonViolet(patterndownimage2);
                    ButtonViolet(patterndownimage1);
                    ButtonViolet(patternupimage8);
                    ButtonViolet(patternupimage9);
                    ButtonViolet(patternupimage10);
                    ButtonViolet(patternupimage11);
                    ButtonViolet(patternupimage12);
                    ButtonViolet(patternupimage13);
                    ButtonViolet(patternupimage14);

                } else {
                    changeImage(patterndownimage5, patterndownimage4);
                    if ((Integer) patterndownimage5.getTag() == R.drawable.roundyellow) {
                        db.insertShotTime(SaveCurrentShotTime(), 2);
                    }

                    patterndownimage7.setImageResource(R.drawable.round);
                    patterndownimage6.setImageResource(R.drawable.round);
                    patterndownimage3.setImageResource(R.drawable.round);
                    patterndownimage2.setImageResource(R.drawable.round);
                    patterndownimage1.setImageResource(R.drawable.round);
                    patternupimage8.setImageResource(R.drawable.round);
                    patternupimage9.setImageResource(R.drawable.round);
                    patternupimage10.setImageResource(R.drawable.round);
                    patternupimage11.setImageResource(R.drawable.round);
                    patternupimage12.setImageResource(R.drawable.round);
                    patternupimage13.setImageResource(R.drawable.round);
                    patternupimage14.setImageResource(R.drawable.round);

                    patterndownimage7.setTag(R.drawable.round);
                    patterndownimage6.setTag(R.drawable.round);
                    patterndownimage3.setTag(R.drawable.round);
                    patterndownimage2.setTag(R.drawable.round);
                    patterndownimage1.setTag(R.drawable.round);
                    patternupimage8.setTag(R.drawable.round);
                    patternupimage9.setTag(R.drawable.round);
                    patternupimage10.setTag(R.drawable.round);
                    patternupimage11.setTag(R.drawable.round);
                    patternupimage12.setTag(R.drawable.round);
                    patternupimage13.setTag(R.drawable.round);
                    patternupimage14.setTag(R.drawable.round);

                }
                break;

            case R.id.patterndownimage4:

                if ((Integer) patterndownimage4.getTag() == R.drawable.round) {
                    patterndownimage4.setImageDrawable(getResources().getDrawable(R.drawable.roundred));
                    patterndownimage4.setTag(R.drawable.roundred);
                    if ((Integer) patterndownimage4.getTag() == R.drawable.roundred) {
                        if (db.getTimeHistory(3) == "") {
                            timedisplaytext.setText("You have not used this location");
                        } else {
                            timedisplaytext.setText("Last Used: " + db.getTimeHistory(3));
                        }

                    }
                    ButtonRedtoBlack(patterndownimage6);
                    ButtonRedtoBlack(patterndownimage5);
                    ButtonRedtoBlack(patterndownimage7);
                    ButtonRedtoBlack(patterndownimage3);
                    ButtonRedtoBlack(patterndownimage2);
                    ButtonRedtoBlack(patterndownimage1);
                    ButtonRedtoBlack(patternupimage8);
                    ButtonRedtoBlack(patternupimage9);
                    ButtonRedtoBlack(patternupimage10);
                    ButtonRedtoBlack(patternupimage11);
                    ButtonRedtoBlack(patternupimage12);
                    ButtonRedtoBlack(patternupimage13);
                    ButtonRedtoBlack(patternupimage14);

                    ButtonViolet(patterndownimage6);
                    ButtonViolet(patterndownimage5);
                    ButtonViolet(patterndownimage7);
                    ButtonViolet(patterndownimage3);
                    ButtonViolet(patterndownimage2);
                    ButtonViolet(patterndownimage1);
                    ButtonViolet(patternupimage8);
                    ButtonViolet(patternupimage9);
                    ButtonViolet(patternupimage10);
                    ButtonViolet(patternupimage11);
                    ButtonViolet(patternupimage12);
                    ButtonViolet(patternupimage13);
                    ButtonViolet(patternupimage14);

                } else {
                    changeImage(patterndownimage4, patterndownimage3);
                    if ((Integer) patterndownimage4.getTag() == R.drawable.roundyellow) {
                        db.insertShotTime(SaveCurrentShotTime(), 3);
                    }
                    patterndownimage7.setImageResource(R.drawable.round);
                    patterndownimage6.setImageResource(R.drawable.round);
                    patterndownimage5.setImageResource(R.drawable.round);
                    patterndownimage2.setImageResource(R.drawable.round);
                    patterndownimage1.setImageResource(R.drawable.round);
                    patternupimage8.setImageResource(R.drawable.round);
                    patternupimage9.setImageResource(R.drawable.round);
                    patternupimage10.setImageResource(R.drawable.round);
                    patternupimage11.setImageResource(R.drawable.round);
                    patternupimage12.setImageResource(R.drawable.round);
                    patternupimage13.setImageResource(R.drawable.round);
                    patternupimage14.setImageResource(R.drawable.round);

                    patterndownimage7.setTag(R.drawable.round);
                    patterndownimage6.setTag(R.drawable.round);
                    patterndownimage5.setTag(R.drawable.round);
                    patterndownimage2.setTag(R.drawable.round);
                    patterndownimage1.setTag(R.drawable.round);
                    patternupimage8.setTag(R.drawable.round);
                    patternupimage9.setTag(R.drawable.round);
                    patternupimage10.setTag(R.drawable.round);
                    patternupimage11.setTag(R.drawable.round);
                    patternupimage12.setTag(R.drawable.round);
                    patternupimage13.setTag(R.drawable.round);
                    patternupimage14.setTag(R.drawable.round);
                }
                break;

            case R.id.patterndownimage3:

                if ((Integer) patterndownimage3.getTag() == R.drawable.round) {
                    patterndownimage3.setImageDrawable(getResources().getDrawable(R.drawable.roundred));
                    patterndownimage3.setTag(R.drawable.roundred);

                    if ((Integer) patterndownimage3.getTag() == R.drawable.roundred) {
                        if (db.getTimeHistory(4) == "") {
                            timedisplaytext.setText("You have not used this location");
                        } else {
                            timedisplaytext.setText("Last Used: " + db.getTimeHistory(4));
                        }

                    }
                    ButtonRedtoBlack(patterndownimage6);
                    ButtonRedtoBlack(patterndownimage5);
                    ButtonRedtoBlack(patterndownimage4);
                    ButtonRedtoBlack(patterndownimage7);
                    ButtonRedtoBlack(patterndownimage2);
                    ButtonRedtoBlack(patterndownimage1);
                    ButtonRedtoBlack(patternupimage8);
                    ButtonRedtoBlack(patternupimage9);
                    ButtonRedtoBlack(patternupimage10);
                    ButtonRedtoBlack(patternupimage11);
                    ButtonRedtoBlack(patternupimage12);
                    ButtonRedtoBlack(patternupimage13);
                    ButtonRedtoBlack(patternupimage14);

                    ButtonViolet(patterndownimage6);
                    ButtonViolet(patterndownimage5);
                    ButtonViolet(patterndownimage4);
                    ButtonViolet(patterndownimage7);
                    ButtonViolet(patterndownimage2);
                    ButtonViolet(patterndownimage1);
                    ButtonViolet(patternupimage8);
                    ButtonViolet(patternupimage9);
                    ButtonViolet(patternupimage10);
                    ButtonViolet(patternupimage11);
                    ButtonViolet(patternupimage12);
                    ButtonViolet(patternupimage13);
                    ButtonViolet(patternupimage14);
                } else {
                    changeImage(patterndownimage3, patterndownimage2);
                    if ((Integer) patterndownimage3.getTag() == R.drawable.roundyellow)
                        db.insertShotTime(SaveCurrentShotTime(), 4);

                    patterndownimage7.setImageResource(R.drawable.round);
                    patterndownimage6.setImageResource(R.drawable.round);
                    patterndownimage5.setImageResource(R.drawable.round);
                    patterndownimage4.setImageResource(R.drawable.round);
                    patterndownimage1.setImageResource(R.drawable.round);
                    patternupimage8.setImageResource(R.drawable.round);
                    patternupimage9.setImageResource(R.drawable.round);
                    patternupimage10.setImageResource(R.drawable.round);
                    patternupimage11.setImageResource(R.drawable.round);
                    patternupimage12.setImageResource(R.drawable.round);
                    patternupimage13.setImageResource(R.drawable.round);
                    patternupimage14.setImageResource(R.drawable.round);

                    patterndownimage7.setTag(R.drawable.round);
                    patterndownimage6.setTag(R.drawable.round);
                    patterndownimage5.setTag(R.drawable.round);
                    patterndownimage4.setTag(R.drawable.round);
                    patterndownimage1.setTag(R.drawable.round);
                    patternupimage8.setTag(R.drawable.round);
                    patternupimage9.setTag(R.drawable.round);
                    patternupimage10.setTag(R.drawable.round);
                    patternupimage11.setTag(R.drawable.round);
                    patternupimage12.setTag(R.drawable.round);
                    patternupimage13.setTag(R.drawable.round);
                    patternupimage14.setTag(R.drawable.round);

                }
                break;
            case R.id.patterndownimage2:

                if ((Integer) patterndownimage2.getTag() == R.drawable.round) {
                    patterndownimage2.setImageDrawable(getResources().getDrawable(R.drawable.roundred));
                    patterndownimage2.setTag(R.drawable.roundred);
                    if ((Integer) patterndownimage2.getTag() == R.drawable.roundred) {
                        if (db.getTimeHistory(5) == "") {
                            timedisplaytext.setText("You have not used this location");
                        } else {
                            timedisplaytext.setText("Last Used: " + db.getTimeHistory(5));
                        }

                    }
                    ButtonRedtoBlack(patterndownimage6);
                    ButtonRedtoBlack(patterndownimage5);
                    ButtonRedtoBlack(patterndownimage4);
                    ButtonRedtoBlack(patterndownimage3);
                    ButtonRedtoBlack(patterndownimage7);
                    ButtonRedtoBlack(patterndownimage1);
                    ButtonRedtoBlack(patternupimage8);
                    ButtonRedtoBlack(patternupimage9);
                    ButtonRedtoBlack(patternupimage10);
                    ButtonRedtoBlack(patternupimage11);
                    ButtonRedtoBlack(patternupimage12);
                    ButtonRedtoBlack(patternupimage13);
                    ButtonRedtoBlack(patternupimage14);

                    ButtonViolet(patterndownimage6);
                    ButtonViolet(patterndownimage5);
                    ButtonViolet(patterndownimage4);
                    ButtonViolet(patterndownimage3);
                    ButtonViolet(patterndownimage7);
                    ButtonViolet(patterndownimage1);
                    ButtonViolet(patternupimage8);
                    ButtonViolet(patternupimage9);
                    ButtonViolet(patternupimage10);
                    ButtonViolet(patternupimage11);
                    ButtonViolet(patternupimage12);
                    ButtonViolet(patternupimage13);
                    ButtonViolet(patternupimage14);
                } else {
                    changeImage(patterndownimage2, patterndownimage1);
                    if ((Integer) patterndownimage2.getTag() == R.drawable.roundyellow) {
                        db.insertShotTime(SaveCurrentShotTime(), 5);
                    }

                    patterndownimage7.setImageResource(R.drawable.round);
                    patterndownimage6.setImageResource(R.drawable.round);
                    patterndownimage5.setImageResource(R.drawable.round);
                    patterndownimage4.setImageResource(R.drawable.round);
                    patterndownimage3.setImageResource(R.drawable.round);
                    patternupimage8.setImageResource(R.drawable.round);
                    patternupimage9.setImageResource(R.drawable.round);
                    patternupimage10.setImageResource(R.drawable.round);
                    patternupimage11.setImageResource(R.drawable.round);
                    patternupimage12.setImageResource(R.drawable.round);
                    patternupimage13.setImageResource(R.drawable.round);
                    patternupimage14.setImageResource(R.drawable.round);

                    patterndownimage7.setTag(R.drawable.round);
                    patterndownimage6.setTag(R.drawable.round);
                    patterndownimage5.setTag(R.drawable.round);
                    patterndownimage4.setTag(R.drawable.round);
                    patterndownimage3.setTag(R.drawable.round);
                    patternupimage8.setTag(R.drawable.round);
                    patternupimage9.setTag(R.drawable.round);
                    patternupimage10.setTag(R.drawable.round);
                    patternupimage11.setTag(R.drawable.round);
                    patternupimage12.setTag(R.drawable.round);
                    patternupimage13.setTag(R.drawable.round);
                    patternupimage14.setTag(R.drawable.round);

                }
                break;
            case R.id.patterndownimage1:
                if ((Integer) patterndownimage1.getTag() == R.drawable.round) {
                    patterndownimage1.setImageDrawable(getResources().getDrawable(R.drawable.roundred));
                    patterndownimage1.setTag(R.drawable.roundred);
                    if ((Integer) patterndownimage1.getTag() == R.drawable.roundred) {
                        if (db.getTimeHistory(6) == "") {
                            timedisplaytext.setText("You have not used this location");
                        } else {
                            timedisplaytext.setText("Last Used: " + db.getTimeHistory(6));
                        }

                    }
                    ButtonRedtoBlack(patterndownimage6);
                    ButtonRedtoBlack(patterndownimage5);
                    ButtonRedtoBlack(patterndownimage4);
                    ButtonRedtoBlack(patterndownimage3);
                    ButtonRedtoBlack(patterndownimage2);
                    ButtonRedtoBlack(patterndownimage7);
                    ButtonRedtoBlack(patternupimage8);
                    ButtonRedtoBlack(patternupimage9);
                    ButtonRedtoBlack(patternupimage10);
                    ButtonRedtoBlack(patternupimage11);
                    ButtonRedtoBlack(patternupimage12);
                    ButtonRedtoBlack(patternupimage13);
                    ButtonRedtoBlack(patternupimage14);

                    ButtonViolet(patterndownimage6);
                    ButtonViolet(patterndownimage5);
                    ButtonViolet(patterndownimage4);
                    ButtonViolet(patterndownimage3);
                    ButtonViolet(patterndownimage2);
                    ButtonViolet(patterndownimage7);
                    ButtonViolet(patternupimage8);
                    ButtonViolet(patternupimage9);
                    ButtonViolet(patternupimage10);
                    ButtonViolet(patternupimage11);
                    ButtonViolet(patternupimage12);
                    ButtonViolet(patternupimage13);
                    ButtonViolet(patternupimage14);
                } else {
                    changeImage(patterndownimage1, patternupimage8);
                    if ((Integer) patterndownimage1.getTag() == R.drawable.roundyellow)
                        db.insertShotTime(SaveCurrentShotTime(), 6);

                    patterndownimage7.setImageResource(R.drawable.round);
                    patterndownimage6.setImageResource(R.drawable.round);
                    patterndownimage5.setImageResource(R.drawable.round);
                    patterndownimage4.setImageResource(R.drawable.round);
                    patterndownimage3.setImageResource(R.drawable.round);
                    patterndownimage2.setImageResource(R.drawable.round);
                    patternupimage9.setImageResource(R.drawable.round);
                    patternupimage10.setImageResource(R.drawable.round);
                    patternupimage11.setImageResource(R.drawable.round);
                    patternupimage12.setImageResource(R.drawable.round);
                    patternupimage13.setImageResource(R.drawable.round);
                    patternupimage14.setImageResource(R.drawable.round);

                    patterndownimage7.setTag(R.drawable.round);
                    patterndownimage6.setTag(R.drawable.round);
                    patterndownimage5.setTag(R.drawable.round);
                    patterndownimage4.setTag(R.drawable.round);
                    patterndownimage3.setTag(R.drawable.round);
                    patterndownimage2.setTag(R.drawable.round);
                    patternupimage9.setTag(R.drawable.round);
                    patternupimage10.setTag(R.drawable.round);
                    patternupimage11.setTag(R.drawable.round);
                    patternupimage12.setTag(R.drawable.round);
                    patternupimage13.setTag(R.drawable.round);
                    patternupimage14.setTag(R.drawable.round);

                }
                break;
            case R.id.patternupimage8:

                if ((Integer) patternupimage8.getTag() == R.drawable.round) {
                    patternupimage8.setImageDrawable(getResources().getDrawable(R.drawable.roundred));
                    patternupimage8.setTag(R.drawable.roundred);
                    if ((Integer) patternupimage8.getTag() == R.drawable.roundred) {
                        if (db.getTimeHistory(7) == "") {
                            timedisplaytext.setText("You have not used this location");
                        } else {
                            timedisplaytext.setText("Last Used: " + db.getTimeHistory(7));
                        }
                    }

                    ButtonRedtoBlack(patterndownimage6);
                    ButtonRedtoBlack(patterndownimage5);
                    ButtonRedtoBlack(patterndownimage4);
                    ButtonRedtoBlack(patterndownimage3);
                    ButtonRedtoBlack(patterndownimage2);
                    ButtonRedtoBlack(patterndownimage1);
                    ButtonRedtoBlack(patterndownimage7);
                    ButtonRedtoBlack(patternupimage9);
                    ButtonRedtoBlack(patternupimage10);
                    ButtonRedtoBlack(patternupimage11);
                    ButtonRedtoBlack(patternupimage12);
                    ButtonRedtoBlack(patternupimage13);
                    ButtonRedtoBlack(patternupimage14);

                    ButtonViolet(patterndownimage6);
                    ButtonViolet(patterndownimage5);
                    ButtonViolet(patterndownimage4);
                    ButtonViolet(patterndownimage3);
                    ButtonViolet(patterndownimage2);
                    ButtonViolet(patterndownimage1);
                    ButtonViolet(patterndownimage7);
                    ButtonViolet(patternupimage9);
                    ButtonViolet(patternupimage10);
                    ButtonViolet(patternupimage11);
                    ButtonViolet(patternupimage12);
                    ButtonViolet(patternupimage13);
                    ButtonViolet(patternupimage14);
                } else {
                    changeImage(patternupimage8, patternupimage9);
                    if ((Integer) patternupimage8.getTag() == R.drawable.roundyellow)
                        db.insertShotTime(SaveCurrentShotTime(), 7);

                    patterndownimage7.setImageResource(R.drawable.round);
                    patterndownimage6.setImageResource(R.drawable.round);
                    patterndownimage5.setImageResource(R.drawable.round);
                    patterndownimage4.setImageResource(R.drawable.round);
                    patterndownimage3.setImageResource(R.drawable.round);
                    patterndownimage2.setImageResource(R.drawable.round);
                    patterndownimage1.setImageResource(R.drawable.round);
                    patternupimage10.setImageResource(R.drawable.round);
                    patternupimage11.setImageResource(R.drawable.round);
                    patternupimage12.setImageResource(R.drawable.round);
                    patternupimage13.setImageResource(R.drawable.round);
                    patternupimage14.setImageResource(R.drawable.round);

                    patterndownimage7.setTag(R.drawable.round);
                    patterndownimage6.setTag(R.drawable.round);
                    patterndownimage5.setTag(R.drawable.round);
                    patterndownimage4.setTag(R.drawable.round);
                    patterndownimage3.setTag(R.drawable.round);
                    patterndownimage2.setTag(R.drawable.round);
                    patterndownimage1.setTag(R.drawable.round);
                    patternupimage10.setTag(R.drawable.round);
                    patternupimage11.setTag(R.drawable.round);
                    patternupimage12.setTag(R.drawable.round);
                    patternupimage13.setTag(R.drawable.round);
                    patternupimage14.setTag(R.drawable.round);

                }
                break;
            case R.id.patternupimage9:

                if ((Integer) patternupimage9.getTag() == R.drawable.round) {
                    patternupimage9.setImageDrawable(getResources().getDrawable(R.drawable.roundred));
                    patternupimage9.setTag(R.drawable.roundred);
                    if ((Integer) patternupimage9.getTag() == R.drawable.roundred) {
                        if (db.getTimeHistory(8) == "") {
                            timedisplaytext.setText("You have not used this location");
                        } else {
                            timedisplaytext.setText("Last Used: " + db.getTimeHistory(8));
                        }

                    }
                    ButtonRedtoBlack(patterndownimage6);
                    ButtonRedtoBlack(patterndownimage5);
                    ButtonRedtoBlack(patterndownimage4);
                    ButtonRedtoBlack(patterndownimage3);
                    ButtonRedtoBlack(patterndownimage2);
                    ButtonRedtoBlack(patterndownimage1);
                    ButtonRedtoBlack(patternupimage8);
                    ButtonRedtoBlack(patterndownimage7);
                    ButtonRedtoBlack(patternupimage10);
                    ButtonRedtoBlack(patternupimage11);
                    ButtonRedtoBlack(patternupimage12);
                    ButtonRedtoBlack(patternupimage13);
                    ButtonRedtoBlack(patternupimage14);

                    ButtonViolet(patterndownimage6);
                    ButtonViolet(patterndownimage5);
                    ButtonViolet(patterndownimage4);
                    ButtonViolet(patterndownimage3);
                    ButtonViolet(patterndownimage2);
                    ButtonViolet(patterndownimage1);
                    ButtonViolet(patternupimage8);
                    ButtonViolet(patterndownimage7);
                    ButtonViolet(patternupimage10);
                    ButtonViolet(patternupimage11);
                    ButtonViolet(patternupimage12);
                    ButtonViolet(patternupimage13);
                    ButtonViolet(patternupimage14);
                } else {
                    changeImage(patternupimage9, patternupimage10);
                    if ((Integer) patternupimage9.getTag() == R.drawable.roundyellow)
                        db.insertShotTime(SaveCurrentShotTime(), 8);

                    patterndownimage7.setImageResource(R.drawable.round);
                    patterndownimage6.setImageResource(R.drawable.round);
                    patterndownimage5.setImageResource(R.drawable.round);
                    patterndownimage4.setImageResource(R.drawable.round);
                    patterndownimage3.setImageResource(R.drawable.round);
                    patterndownimage2.setImageResource(R.drawable.round);
                    patterndownimage1.setImageResource(R.drawable.round);
                    patternupimage8.setImageResource(R.drawable.round);
                    patternupimage11.setImageResource(R.drawable.round);
                    patternupimage12.setImageResource(R.drawable.round);
                    patternupimage13.setImageResource(R.drawable.round);
                    patternupimage14.setImageResource(R.drawable.round);

                    patterndownimage7.setTag(R.drawable.round);
                    patterndownimage6.setTag(R.drawable.round);
                    patterndownimage5.setTag(R.drawable.round);
                    patterndownimage4.setTag(R.drawable.round);
                    patterndownimage3.setTag(R.drawable.round);
                    patterndownimage2.setTag(R.drawable.round);
                    patterndownimage1.setTag(R.drawable.round);
                    patternupimage8.setTag(R.drawable.round);
                    patternupimage11.setTag(R.drawable.round);
                    patternupimage12.setTag(R.drawable.round);
                    patternupimage13.setTag(R.drawable.round);
                    patternupimage14.setTag(R.drawable.round);

                }
                break;
            case R.id.patternupimage10:
                if ((Integer) patternupimage10.getTag() == R.drawable.round) {
                    patternupimage10.setImageDrawable(getResources().getDrawable(R.drawable.roundred));
                    patternupimage10.setTag(R.drawable.roundred);
                    if ((Integer) patternupimage10.getTag() == R.drawable.roundred) {
                        if (db.getTimeHistory(9) == "") {
                            timedisplaytext.setText("You have not used this location");
                        } else {
                            timedisplaytext.setText("Last Used: " + db.getTimeHistory(9));
                        }
                    }
                    ButtonRedtoBlack(patterndownimage6);
                    ButtonRedtoBlack(patterndownimage5);
                    ButtonRedtoBlack(patterndownimage4);
                    ButtonRedtoBlack(patterndownimage3);
                    ButtonRedtoBlack(patterndownimage2);
                    ButtonRedtoBlack(patterndownimage1);
                    ButtonRedtoBlack(patternupimage8);
                    ButtonRedtoBlack(patternupimage9);
                    ButtonRedtoBlack(patterndownimage7);
                    ButtonRedtoBlack(patternupimage11);
                    ButtonRedtoBlack(patternupimage12);
                    ButtonRedtoBlack(patternupimage13);
                    ButtonRedtoBlack(patternupimage14);

                    ButtonViolet(patterndownimage6);
                    ButtonViolet(patterndownimage5);
                    ButtonViolet(patterndownimage4);
                    ButtonViolet(patterndownimage3);
                    ButtonViolet(patterndownimage2);
                    ButtonViolet(patterndownimage1);
                    ButtonViolet(patternupimage8);
                    ButtonViolet(patternupimage9);
                    ButtonViolet(patterndownimage7);
                    ButtonViolet(patternupimage11);
                    ButtonViolet(patternupimage12);
                    ButtonViolet(patternupimage13);
                    ButtonViolet(patternupimage14);
                } else {
                    changeImage(patternupimage10, patternupimage11);
                    if ((Integer) patternupimage10.getTag() == R.drawable.roundyellow)
                        db.insertShotTime(SaveCurrentShotTime(), 9);

                    patterndownimage7.setImageResource(R.drawable.round);
                    patterndownimage6.setImageResource(R.drawable.round);
                    patterndownimage5.setImageResource(R.drawable.round);
                    patterndownimage4.setImageResource(R.drawable.round);
                    patterndownimage3.setImageResource(R.drawable.round);
                    patterndownimage2.setImageResource(R.drawable.round);
                    patterndownimage1.setImageResource(R.drawable.round);
                    patternupimage8.setImageResource(R.drawable.round);
                    patternupimage9.setImageResource(R.drawable.round);
                    patternupimage12.setImageResource(R.drawable.round);
                    patternupimage13.setImageResource(R.drawable.round);
                    patternupimage14.setImageResource(R.drawable.round);

                    patterndownimage7.setTag(R.drawable.round);
                    patterndownimage6.setTag(R.drawable.round);
                    patterndownimage5.setTag(R.drawable.round);
                    patterndownimage4.setTag(R.drawable.round);
                    patterndownimage3.setTag(R.drawable.round);
                    patterndownimage2.setTag(R.drawable.round);
                    patterndownimage1.setTag(R.drawable.round);
                    patternupimage8.setTag(R.drawable.round);
                    patternupimage9.setTag(R.drawable.round);
                    patternupimage12.setTag(R.drawable.round);
                    patternupimage13.setTag(R.drawable.round);
                    patternupimage14.setTag(R.drawable.round);

                }
                break;
            case R.id.patternupimage11:
                if ((Integer) patternupimage11.getTag() == R.drawable.round) {
                    patternupimage11.setImageDrawable(getResources().getDrawable(R.drawable.roundred));
                    patternupimage11.setTag(R.drawable.roundred);
                    if ((Integer) patternupimage11.getTag() == R.drawable.roundred) {
                        if (db.getTimeHistory(10) == "") {
                            timedisplaytext.setText("You have not used this location");
                        } else {
                            timedisplaytext.setText("Last Used: " + db.getTimeHistory(10));
                        }
                    }

                    ButtonRedtoBlack(patterndownimage6);
                    ButtonRedtoBlack(patterndownimage5);
                    ButtonRedtoBlack(patterndownimage4);
                    ButtonRedtoBlack(patterndownimage3);
                    ButtonRedtoBlack(patterndownimage2);
                    ButtonRedtoBlack(patterndownimage1);
                    ButtonRedtoBlack(patternupimage8);
                    ButtonRedtoBlack(patternupimage9);
                    ButtonRedtoBlack(patternupimage10);
                    ButtonRedtoBlack(patterndownimage7);
                    ButtonRedtoBlack(patternupimage12);
                    ButtonRedtoBlack(patternupimage13);
                    ButtonRedtoBlack(patternupimage14);

                    ButtonViolet(patterndownimage6);
                    ButtonViolet(patterndownimage5);
                    ButtonViolet(patterndownimage4);
                    ButtonViolet(patterndownimage3);
                    ButtonViolet(patterndownimage2);
                    ButtonViolet(patterndownimage1);
                    ButtonViolet(patternupimage8);
                    ButtonViolet(patternupimage9);
                    ButtonViolet(patternupimage10);
                    ButtonViolet(patterndownimage7);
                    ButtonViolet(patternupimage12);
                    ButtonViolet(patternupimage13);
                    ButtonViolet(patternupimage14);
                } else {
                    changeImage(patternupimage11, patternupimage12);
                    if ((Integer) patternupimage11.getTag() == R.drawable.roundyellow)
                        db.insertShotTime(SaveCurrentShotTime(), 10);

                    patterndownimage7.setImageResource(R.drawable.round);
                    patterndownimage6.setImageResource(R.drawable.round);
                    patterndownimage5.setImageResource(R.drawable.round);
                    patterndownimage4.setImageResource(R.drawable.round);
                    patterndownimage3.setImageResource(R.drawable.round);
                    patterndownimage2.setImageResource(R.drawable.round);
                    patterndownimage1.setImageResource(R.drawable.round);
                    patternupimage8.setImageResource(R.drawable.round);
                    patternupimage9.setImageResource(R.drawable.round);
                    patternupimage10.setImageResource(R.drawable.round);
                    patternupimage13.setImageResource(R.drawable.round);
                    patternupimage14.setImageResource(R.drawable.round);

                    patterndownimage7.setTag(R.drawable.round);
                    patterndownimage6.setTag(R.drawable.round);
                    patterndownimage5.setTag(R.drawable.round);
                    patterndownimage4.setTag(R.drawable.round);
                    patterndownimage3.setTag(R.drawable.round);
                    patterndownimage2.setTag(R.drawable.round);
                    patterndownimage1.setTag(R.drawable.round);
                    patternupimage8.setTag(R.drawable.round);
                    patternupimage9.setTag(R.drawable.round);
                    patternupimage10.setTag(R.drawable.round);
                    patternupimage13.setTag(R.drawable.round);
                    patternupimage14.setTag(R.drawable.round);
                }
                break;
            case R.id.patternupimage12:
                if ((Integer) patternupimage12.getTag() == R.drawable.round) {
                    patternupimage12.setImageDrawable(getResources().getDrawable(R.drawable.roundred));
                    patternupimage12.setTag(R.drawable.roundred);
                    if ((Integer) patternupimage12.getTag() == R.drawable.roundred) {
                        if (db.getTimeHistory(11) == "") {
                            timedisplaytext.setText("You have not used this location");
                        } else {
                            timedisplaytext.setText("Last Used: " + db.getTimeHistory(11));
                        }

                    }
                    ButtonRedtoBlack(patterndownimage6);
                    ButtonRedtoBlack(patterndownimage5);
                    ButtonRedtoBlack(patterndownimage4);
                    ButtonRedtoBlack(patterndownimage3);
                    ButtonRedtoBlack(patterndownimage2);
                    ButtonRedtoBlack(patterndownimage1);
                    ButtonRedtoBlack(patternupimage8);
                    ButtonRedtoBlack(patternupimage9);
                    ButtonRedtoBlack(patternupimage10);
                    ButtonRedtoBlack(patternupimage11);
                    ButtonRedtoBlack(patterndownimage7);
                    ButtonRedtoBlack(patternupimage13);
                    ButtonRedtoBlack(patternupimage14);

                    ButtonViolet(patterndownimage6);
                    ButtonViolet(patterndownimage5);
                    ButtonViolet(patterndownimage4);
                    ButtonViolet(patterndownimage3);
                    ButtonViolet(patterndownimage2);
                    ButtonViolet(patterndownimage1);
                    ButtonViolet(patternupimage8);
                    ButtonViolet(patternupimage9);
                    ButtonViolet(patternupimage10);
                    ButtonViolet(patternupimage11);
                    ButtonViolet(patterndownimage7);
                    ButtonViolet(patternupimage13);
                    ButtonViolet(patternupimage14);
                } else {
                    changeImage(patternupimage12, patternupimage13);
                    if ((Integer) patternupimage12.getTag() == R.drawable.roundyellow)
                        db.insertShotTime(SaveCurrentShotTime(), 11);

                    patterndownimage7.setImageResource(R.drawable.round);
                    patterndownimage6.setImageResource(R.drawable.round);
                    patterndownimage5.setImageResource(R.drawable.round);
                    patterndownimage4.setImageResource(R.drawable.round);
                    patterndownimage3.setImageResource(R.drawable.round);
                    patterndownimage2.setImageResource(R.drawable.round);
                    patterndownimage1.setImageResource(R.drawable.round);
                    patternupimage8.setImageResource(R.drawable.round);
                    patternupimage9.setImageResource(R.drawable.round);
                    patternupimage10.setImageResource(R.drawable.round);
                    patternupimage11.setImageResource(R.drawable.round);
                    patternupimage14.setImageResource(R.drawable.round);

                    patterndownimage7.setTag(R.drawable.round);
                    patterndownimage6.setTag(R.drawable.round);
                    patterndownimage5.setTag(R.drawable.round);
                    patterndownimage4.setTag(R.drawable.round);
                    patterndownimage3.setTag(R.drawable.round);
                    patterndownimage2.setTag(R.drawable.round);
                    patterndownimage1.setTag(R.drawable.round);
                    patternupimage8.setTag(R.drawable.round);
                    patternupimage9.setTag(R.drawable.round);
                    patternupimage10.setTag(R.drawable.round);
                    patternupimage11.setTag(R.drawable.round);
                    patternupimage14.setTag(R.drawable.round);

                }
                break;
            case R.id.patternupimage13:
                if ((Integer) patternupimage13.getTag() == R.drawable.round) {
                    patternupimage13.setImageDrawable(getResources().getDrawable(R.drawable.roundred));
                    patternupimage13.setTag(R.drawable.roundred);
                    if ((Integer) patternupimage13.getTag() == R.drawable.roundred) {
                        if (db.getTimeHistory(12) == "") {
                            timedisplaytext.setText("You have not used this location");
                        } else {
                            timedisplaytext.setText("Last Used: " + db.getTimeHistory(12));
                        }
                    }
                    ButtonRedtoBlack(patterndownimage6);
                    ButtonRedtoBlack(patterndownimage5);
                    ButtonRedtoBlack(patterndownimage4);
                    ButtonRedtoBlack(patterndownimage3);
                    ButtonRedtoBlack(patterndownimage2);
                    ButtonRedtoBlack(patterndownimage1);
                    ButtonRedtoBlack(patternupimage8);
                    ButtonRedtoBlack(patternupimage9);
                    ButtonRedtoBlack(patternupimage10);
                    ButtonRedtoBlack(patternupimage11);
                    ButtonRedtoBlack(patternupimage12);
                    ButtonRedtoBlack(patterndownimage7);
                    ButtonRedtoBlack(patternupimage14);

                    ButtonViolet(patterndownimage6);
                    ButtonViolet(patterndownimage5);
                    ButtonViolet(patterndownimage4);
                    ButtonViolet(patterndownimage3);
                    ButtonViolet(patterndownimage2);
                    ButtonViolet(patterndownimage1);
                    ButtonViolet(patternupimage8);
                    ButtonViolet(patternupimage9);
                    ButtonViolet(patternupimage10);
                    ButtonViolet(patternupimage11);
                    ButtonViolet(patternupimage12);
                    ButtonViolet(patterndownimage7);
                    ButtonViolet(patternupimage14);
                } else {
                    changeImage(patternupimage13, patternupimage14);
                    if ((Integer) patternupimage13.getTag() == R.drawable.roundyellow)
                        db.insertShotTime(SaveCurrentShotTime(), 12);

                    patterndownimage7.setImageResource(R.drawable.round);
                    patterndownimage6.setImageResource(R.drawable.round);
                    patterndownimage5.setImageResource(R.drawable.round);
                    patterndownimage4.setImageResource(R.drawable.round);
                    patterndownimage3.setImageResource(R.drawable.round);
                    patterndownimage2.setImageResource(R.drawable.round);
                    patterndownimage1.setImageResource(R.drawable.round);
                    patternupimage8.setImageResource(R.drawable.round);
                    patternupimage9.setImageResource(R.drawable.round);
                    patternupimage10.setImageResource(R.drawable.round);
                    patternupimage11.setImageResource(R.drawable.round);
                    patternupimage12.setImageResource(R.drawable.round);

                    patterndownimage7.setTag(R.drawable.round);
                    patterndownimage6.setTag(R.drawable.round);
                    patterndownimage5.setTag(R.drawable.round);
                    patterndownimage4.setTag(R.drawable.round);
                    patterndownimage3.setTag(R.drawable.round);
                    patterndownimage2.setTag(R.drawable.round);
                    patterndownimage1.setTag(R.drawable.round);
                    patternupimage8.setTag(R.drawable.round);
                    patternupimage9.setTag(R.drawable.round);
                    patternupimage10.setTag(R.drawable.round);
                    patternupimage11.setTag(R.drawable.round);
                    patternupimage12.setTag(R.drawable.round);

                }
                break;
            case R.id.patternupimage14:
                if ((Integer) patternupimage14.getTag() == R.drawable.round) {
                    patternupimage14.setImageDrawable(getResources().getDrawable(R.drawable.roundred));
                    patternupimage14.setTag(R.drawable.roundred);
                    if ((Integer) patternupimage14.getTag() == R.drawable.roundred) {
                        if (db.getTimeHistory(13) == "") {
                            timedisplaytext.setText("You have not used this location");
                        } else {
                            timedisplaytext.setText("Last Used: " + db.getTimeHistory(13));
                        }
                    }
                    ButtonRedtoBlack(patterndownimage6);
                    ButtonRedtoBlack(patterndownimage5);
                    ButtonRedtoBlack(patterndownimage4);
                    ButtonRedtoBlack(patterndownimage3);
                    ButtonRedtoBlack(patterndownimage2);
                    ButtonRedtoBlack(patterndownimage1);
                    ButtonRedtoBlack(patternupimage8);
                    ButtonRedtoBlack(patternupimage9);
                    ButtonRedtoBlack(patternupimage10);
                    ButtonRedtoBlack(patternupimage11);
                    ButtonRedtoBlack(patternupimage12);
                    ButtonRedtoBlack(patternupimage13);
                    ButtonRedtoBlack(patterndownimage7);

                    ButtonViolet(patterndownimage6);
                    ButtonViolet(patterndownimage5);
                    ButtonViolet(patterndownimage4);
                    ButtonViolet(patterndownimage3);
                    ButtonViolet(patterndownimage2);
                    ButtonViolet(patterndownimage1);
                    ButtonViolet(patternupimage8);
                    ButtonViolet(patternupimage9);
                    ButtonViolet(patternupimage10);
                    ButtonViolet(patternupimage11);
                    ButtonViolet(patternupimage12);
                    ButtonViolet(patternupimage13);
                    ButtonViolet(patterndownimage7);
                } else {
                    changeImage(patternupimage14, patterndownimage7);
                    if ((Integer) patternupimage14.getTag() == R.drawable.roundyellow)
                        db.insertShotTime(SaveCurrentShotTime(), 13);

                    patterndownimage1.setImageResource(R.drawable.round);
                    patterndownimage6.setImageResource(R.drawable.round);
                    patterndownimage5.setImageResource(R.drawable.round);
                    patterndownimage4.setImageResource(R.drawable.round);
                    patterndownimage3.setImageResource(R.drawable.round);
                    patterndownimage2.setImageResource(R.drawable.round);
                    patternupimage8.setImageResource(R.drawable.round);
                    patternupimage9.setImageResource(R.drawable.round);
                    patternupimage10.setImageResource(R.drawable.round);
                    patternupimage11.setImageResource(R.drawable.round);
                    patternupimage12.setImageResource(R.drawable.round);
                    patternupimage13.setImageResource(R.drawable.round);

                    patterndownimage1.setTag(R.drawable.round);
                    patterndownimage6.setTag(R.drawable.round);
                    patterndownimage5.setTag(R.drawable.round);
                    patterndownimage4.setTag(R.drawable.round);
                    patterndownimage3.setTag(R.drawable.round);
                    patterndownimage2.setTag(R.drawable.round);
                    patternupimage8.setTag(R.drawable.round);
                    patternupimage9.setTag(R.drawable.round);
                    patternupimage10.setTag(R.drawable.round);
                    patternupimage11.setTag(R.drawable.round);
                    patternupimage12.setTag(R.drawable.round);
                    patternupimage13.setTag(R.drawable.round);
                }
                break;

            case R.id.imageView1:
                if ((Integer) imageView1.getTag() == R.drawable.round) {
                    imageView1.setImageDrawable(getResources().getDrawable(R.drawable.roundred));
                    imageView1.setTag(R.drawable.roundred);
                    if ((Integer) imageView1.getTag() == R.drawable.roundred) {
                        if (db.getTimeHistory(14) == "") {
                            timedisplaytext.setText("You have not used this location");
                        } else {
                            timedisplaytext.setText("Last Used: " + db.getTimeHistory(14));
                        }
                    }
                    ButtonRedtoBlack(imageView2);
                    ButtonRedtoBlack(imageView3);
                    ButtonRedtoBlack(imageView4);
                    ButtonRedtoBlack(imageView5);
                    ButtonRedtoBlack(imageView6);
                    ButtonRedtoBlack(imageView7);
                    ButtonRedtoBlack(imageView8);
                    ButtonRedtoBlack(imageView9);
                    ButtonRedtoBlack(imageView10);
                    ButtonRedtoBlack(imageView11);
                    ButtonRedtoBlack(imageView12);
                    ButtonRedtoBlack(imageView13);

                    ButtonViolet(imageView2);
                    ButtonViolet(imageView3);
                    ButtonViolet(imageView4);
                    ButtonViolet(imageView5);
                    ButtonViolet(imageView6);
                    ButtonViolet(imageView7);
                    ButtonViolet(imageView8);
                    ButtonViolet(imageView9);
                    ButtonViolet(imageView10);
                    ButtonViolet(imageView11);
                    ButtonViolet(imageView12);
                    ButtonViolet(imageView13);

                } else {
                    changePatternRoundImage(imageView1, imageView2);
                    if ((Integer) imageView1.getTag() == R.drawable.roundyellow)
                        db.insertShotTime(SaveCurrentShotTime(), 14);

                    imageView3.setImageResource(R.drawable.round);
                    imageView4.setImageResource(R.drawable.round);
                    imageView5.setImageResource(R.drawable.round);
                    imageView6.setImageResource(R.drawable.round);
                    imageView7.setImageResource(R.drawable.round);
                    imageView8.setImageResource(R.drawable.round);
                    imageView9.setImageResource(R.drawable.round);
                    imageView10.setImageResource(R.drawable.round);
                    imageView11.setImageResource(R.drawable.round);
                    imageView12.setImageResource(R.drawable.round);
                    imageView13.setImageResource(R.drawable.round);

                    imageView3.setTag(R.drawable.round);
                    imageView4.setTag(R.drawable.round);
                    imageView5.setTag(R.drawable.round);
                    imageView6.setTag(R.drawable.round);
                    imageView7.setTag(R.drawable.round);
                    imageView8.setTag(R.drawable.round);
                    imageView9.setTag(R.drawable.round);
                    imageView10.setTag(R.drawable.round);
                    imageView11.setTag(R.drawable.round);
                    imageView12.setTag(R.drawable.round);
                    imageView13.setTag(R.drawable.round);
                }
                break;
            case R.id.imageView2:
                if ((Integer) imageView2.getTag() == R.drawable.round) {
                    imageView2.setImageDrawable(getResources().getDrawable(R.drawable.roundred));
                    imageView2.setTag(R.drawable.roundred);
                    if ((Integer) imageView2.getTag() == R.drawable.roundred) {
                        if (db.getTimeHistory(15) == "") {
                            timedisplaytext.setText("You have not used this location");
                        } else {
                            timedisplaytext.setText("Last Used: " + db.getTimeHistory(15));
                        }
                    }
                    ButtonRedtoBlack(imageView1);
                    ButtonRedtoBlack(imageView3);
                    ButtonRedtoBlack(imageView4);
                    ButtonRedtoBlack(imageView5);
                    ButtonRedtoBlack(imageView6);
                    ButtonRedtoBlack(imageView7);
                    ButtonRedtoBlack(imageView8);
                    ButtonRedtoBlack(imageView9);
                    ButtonRedtoBlack(imageView10);
                    ButtonRedtoBlack(imageView11);
                    ButtonRedtoBlack(imageView12);
                    ButtonRedtoBlack(imageView13);

                    ButtonViolet(imageView1);
                    ButtonViolet(imageView3);
                    ButtonViolet(imageView4);
                    ButtonViolet(imageView5);
                    ButtonViolet(imageView6);
                    ButtonViolet(imageView7);
                    ButtonViolet(imageView8);
                    ButtonViolet(imageView9);
                    ButtonViolet(imageView10);
                    ButtonViolet(imageView11);
                    ButtonViolet(imageView12);
                    ButtonViolet(imageView13);
                } else {
                    changePatternRoundImage(imageView2, imageView3);
                    if ((Integer) imageView2.getTag() == R.drawable.roundyellow)
                        db.insertShotTime(SaveCurrentShotTime(), 15);

                    imageView1.setImageResource(R.drawable.round);
                    imageView4.setImageResource(R.drawable.round);
                    imageView5.setImageResource(R.drawable.round);
                    imageView6.setImageResource(R.drawable.round);
                    imageView7.setImageResource(R.drawable.round);
                    imageView8.setImageResource(R.drawable.round);
                    imageView9.setImageResource(R.drawable.round);
                    imageView10.setImageResource(R.drawable.round);
                    imageView11.setImageResource(R.drawable.round);
                    imageView12.setImageResource(R.drawable.round);
                    imageView13.setImageResource(R.drawable.round);

                    imageView1.setTag(R.drawable.round);
                    imageView4.setTag(R.drawable.round);
                    imageView5.setTag(R.drawable.round);
                    imageView6.setTag(R.drawable.round);
                    imageView7.setTag(R.drawable.round);
                    imageView8.setTag(R.drawable.round);
                    imageView9.setTag(R.drawable.round);
                    imageView10.setTag(R.drawable.round);
                    imageView11.setTag(R.drawable.round);
                    imageView12.setTag(R.drawable.round);
                    imageView13.setTag(R.drawable.round);
                }
                break;
            case R.id.imageView3:
                if ((Integer) imageView3.getTag() == R.drawable.round) {
                    imageView3.setImageDrawable(getResources().getDrawable(R.drawable.roundred));
                    imageView3.setTag(R.drawable.roundred);
                    if ((Integer) imageView3.getTag() == R.drawable.roundred) {
                        if (db.getTimeHistory(16) == "") {
                            timedisplaytext.setText("You have not used this location");
                        } else {
                            timedisplaytext.setText("Last Used: " + db.getTimeHistory(16));
                        }
                    }
                    ButtonRedtoBlack(imageView2);
                    ButtonRedtoBlack(imageView1);
                    ButtonRedtoBlack(imageView4);
                    ButtonRedtoBlack(imageView5);
                    ButtonRedtoBlack(imageView6);
                    ButtonRedtoBlack(imageView7);
                    ButtonRedtoBlack(imageView8);
                    ButtonRedtoBlack(imageView9);
                    ButtonRedtoBlack(imageView10);
                    ButtonRedtoBlack(imageView11);
                    ButtonRedtoBlack(imageView12);
                    ButtonRedtoBlack(imageView13);

                    ButtonViolet(imageView2);
                    ButtonViolet(imageView1);
                    ButtonViolet(imageView4);
                    ButtonViolet(imageView5);
                    ButtonViolet(imageView6);
                    ButtonViolet(imageView7);
                    ButtonViolet(imageView8);
                    ButtonViolet(imageView9);
                    ButtonViolet(imageView10);
                    ButtonViolet(imageView11);
                    ButtonViolet(imageView12);
                    ButtonViolet(imageView13);
                } else {
                    changePatternRoundImage(imageView3, imageView4);
                    if ((Integer) imageView3.getTag() == R.drawable.roundyellow)
                        db.insertShotTime(SaveCurrentShotTime(), 16);


                    imageView1.setImageResource(R.drawable.round);
                    imageView2.setImageResource(R.drawable.round);
                    imageView5.setImageResource(R.drawable.round);
                    imageView6.setImageResource(R.drawable.round);
                    imageView7.setImageResource(R.drawable.round);
                    imageView8.setImageResource(R.drawable.round);
                    imageView9.setImageResource(R.drawable.round);
                    imageView10.setImageResource(R.drawable.round);
                    imageView11.setImageResource(R.drawable.round);
                    imageView12.setImageResource(R.drawable.round);
                    imageView13.setImageResource(R.drawable.round);

                    imageView1.setTag(R.drawable.round);
                    imageView2.setTag(R.drawable.round);
                    imageView5.setTag(R.drawable.round);
                    imageView6.setTag(R.drawable.round);
                    imageView7.setTag(R.drawable.round);
                    imageView8.setTag(R.drawable.round);
                    imageView9.setTag(R.drawable.round);
                    imageView10.setTag(R.drawable.round);
                    imageView11.setTag(R.drawable.round);
                    imageView12.setTag(R.drawable.round);
                    imageView13.setTag(R.drawable.round);
                }
                break;
            case R.id.imageView4:
                if ((Integer) imageView4.getTag() == R.drawable.round) {
                    imageView4.setImageDrawable(getResources().getDrawable(R.drawable.roundred));
                    imageView4.setTag(R.drawable.roundred);
                    if ((Integer) imageView4.getTag() == R.drawable.roundred) {
                        if (db.getTimeHistory(17) == "") {
                            timedisplaytext.setText("You have not used this location");
                        } else {
                            timedisplaytext.setText("Last Used: " + db.getTimeHistory(17));
                        }
                    }
                    ButtonRedtoBlack(imageView2);
                    ButtonRedtoBlack(imageView3);
                    ButtonRedtoBlack(imageView1);
                    ButtonRedtoBlack(imageView5);
                    ButtonRedtoBlack(imageView6);
                    ButtonRedtoBlack(imageView7);
                    ButtonRedtoBlack(imageView8);
                    ButtonRedtoBlack(imageView9);
                    ButtonRedtoBlack(imageView10);
                    ButtonRedtoBlack(imageView11);
                    ButtonRedtoBlack(imageView12);
                    ButtonRedtoBlack(imageView13);

                    ButtonViolet(imageView2);
                    ButtonViolet(imageView3);
                    ButtonViolet(imageView1);
                    ButtonViolet(imageView5);
                    ButtonViolet(imageView6);
                    ButtonViolet(imageView7);
                    ButtonViolet(imageView8);
                    ButtonViolet(imageView9);
                    ButtonViolet(imageView10);
                    ButtonViolet(imageView11);
                    ButtonViolet(imageView12);
                    ButtonViolet(imageView13);
                } else {
                    changePatternRoundImage(imageView4, imageView5);

                    if ((Integer) imageView4.getTag() == R.drawable.roundyellow)
                        db.insertShotTime(SaveCurrentShotTime(), 17);

                    imageView1.setImageResource(R.drawable.round);
                    imageView2.setImageResource(R.drawable.round);
                    imageView3.setImageResource(R.drawable.round);
                    imageView6.setImageResource(R.drawable.round);
                    imageView7.setImageResource(R.drawable.round);
                    imageView8.setImageResource(R.drawable.round);
                    imageView9.setImageResource(R.drawable.round);
                    imageView10.setImageResource(R.drawable.round);
                    imageView11.setImageResource(R.drawable.round);
                    imageView12.setImageResource(R.drawable.round);
                    imageView13.setImageResource(R.drawable.round);

                    imageView1.setTag(R.drawable.round);
                    imageView2.setTag(R.drawable.round);
                    imageView3.setTag(R.drawable.round);
                    imageView6.setTag(R.drawable.round);
                    imageView7.setTag(R.drawable.round);
                    imageView8.setTag(R.drawable.round);
                    imageView9.setTag(R.drawable.round);
                    imageView10.setTag(R.drawable.round);
                    imageView11.setTag(R.drawable.round);
                    imageView12.setTag(R.drawable.round);
                    imageView13.setTag(R.drawable.round);
                }
                break;
            case R.id.imageView5:
                if ((Integer) imageView5.getTag() == R.drawable.round) {
                    imageView5.setImageDrawable(getResources().getDrawable(R.drawable.roundred));
                    imageView5.setTag(R.drawable.roundred);
                    if ((Integer) imageView5.getTag() == R.drawable.roundred) {
                        if (db.getTimeHistory(18) == "") {
                            timedisplaytext.setText("You have not used this location");
                        } else {
                            timedisplaytext.setText("Last Used: " + db.getTimeHistory(18));
                        }

                    }
                    ButtonRedtoBlack(imageView2);
                    ButtonRedtoBlack(imageView3);
                    ButtonRedtoBlack(imageView4);
                    ButtonRedtoBlack(imageView1);
                    ButtonRedtoBlack(imageView6);
                    ButtonRedtoBlack(imageView7);
                    ButtonRedtoBlack(imageView8);
                    ButtonRedtoBlack(imageView9);
                    ButtonRedtoBlack(imageView10);
                    ButtonRedtoBlack(imageView11);
                    ButtonRedtoBlack(imageView12);
                    ButtonRedtoBlack(imageView13);

                    ButtonViolet(imageView2);
                    ButtonViolet(imageView3);
                    ButtonViolet(imageView4);
                    ButtonViolet(imageView1);
                    ButtonViolet(imageView6);
                    ButtonViolet(imageView7);
                    ButtonViolet(imageView8);
                    ButtonViolet(imageView9);
                    ButtonViolet(imageView10);
                    ButtonViolet(imageView11);
                    ButtonViolet(imageView12);
                    ButtonViolet(imageView13);
                } else {
                    changePatternRoundImage(imageView5, imageView6);
                    if ((Integer) imageView5.getTag() == R.drawable.roundyellow)
                        db.insertShotTime(SaveCurrentShotTime(), 18);

                    imageView1.setImageResource(R.drawable.round);
                    imageView2.setImageResource(R.drawable.round);
                    imageView3.setImageResource(R.drawable.round);
                    imageView4.setImageResource(R.drawable.round);
                    imageView7.setImageResource(R.drawable.round);
                    imageView8.setImageResource(R.drawable.round);
                    imageView9.setImageResource(R.drawable.round);
                    imageView10.setImageResource(R.drawable.round);
                    imageView11.setImageResource(R.drawable.round);
                    imageView12.setImageResource(R.drawable.round);
                    imageView13.setImageResource(R.drawable.round);

                    imageView1.setTag(R.drawable.round);
                    imageView2.setTag(R.drawable.round);
                    imageView3.setTag(R.drawable.round);
                    imageView4.setTag(R.drawable.round);
                    imageView7.setTag(R.drawable.round);
                    imageView8.setTag(R.drawable.round);
                    imageView9.setTag(R.drawable.round);
                    imageView10.setTag(R.drawable.round);
                    imageView11.setTag(R.drawable.round);
                    imageView12.setTag(R.drawable.round);
                    imageView13.setTag(R.drawable.round);
                }
                break;
            case R.id.imageView6:
                if ((Integer) imageView6.getTag() == R.drawable.round) {
                    imageView6.setImageDrawable(getResources().getDrawable(R.drawable.roundred));
                    imageView6.setTag(R.drawable.roundred);
                    if ((Integer) imageView6.getTag() == R.drawable.roundred) {
                        if (db.getTimeHistory(19) == "") {
                            timedisplaytext.setText("You have not used this location");
                        } else {
                            timedisplaytext.setText("Last Used: " + db.getTimeHistory(19));
                        }
                    }
                    ButtonRedtoBlack(imageView2);
                    ButtonRedtoBlack(imageView3);
                    ButtonRedtoBlack(imageView4);
                    ButtonRedtoBlack(imageView5);
                    ButtonRedtoBlack(imageView1);
                    ButtonRedtoBlack(imageView7);
                    ButtonRedtoBlack(imageView8);
                    ButtonRedtoBlack(imageView9);
                    ButtonRedtoBlack(imageView10);
                    ButtonRedtoBlack(imageView11);
                    ButtonRedtoBlack(imageView12);
                    ButtonRedtoBlack(imageView13);

                    ButtonViolet(imageView2);
                    ButtonViolet(imageView3);
                    ButtonViolet(imageView4);
                    ButtonViolet(imageView5);
                    ButtonViolet(imageView1);
                    ButtonViolet(imageView7);
                    ButtonViolet(imageView8);
                    ButtonViolet(imageView9);
                    ButtonViolet(imageView10);
                    ButtonViolet(imageView11);
                    ButtonViolet(imageView12);
                    ButtonViolet(imageView13);
                } else {
                    changePatternRoundImage(imageView6, imageView7);
                    if ((Integer) imageView6.getTag() == R.drawable.roundyellow)
                        db.insertShotTime(SaveCurrentShotTime(), 19);

                    imageView1.setImageResource(R.drawable.round);
                    imageView2.setImageResource(R.drawable.round);
                    imageView3.setImageResource(R.drawable.round);
                    imageView4.setImageResource(R.drawable.round);
                    imageView5.setImageResource(R.drawable.round);
                    imageView8.setImageResource(R.drawable.round);
                    imageView9.setImageResource(R.drawable.round);
                    imageView10.setImageResource(R.drawable.round);
                    imageView11.setImageResource(R.drawable.round);
                    imageView12.setImageResource(R.drawable.round);
                    imageView13.setImageResource(R.drawable.round);

                    imageView1.setTag(R.drawable.round);
                    imageView2.setTag(R.drawable.round);
                    imageView3.setTag(R.drawable.round);
                    imageView4.setTag(R.drawable.round);
                    imageView5.setTag(R.drawable.round);
                    imageView8.setTag(R.drawable.round);
                    imageView9.setTag(R.drawable.round);
                    imageView10.setTag(R.drawable.round);
                    imageView11.setTag(R.drawable.round);
                    imageView12.setTag(R.drawable.round);
                    imageView13.setTag(R.drawable.round);
                }
                break;
            case R.id.imageView7:
                if ((Integer) imageView7.getTag() == R.drawable.round) {
                    imageView7.setImageDrawable(getResources().getDrawable(R.drawable.roundred));
                    imageView7.setTag(R.drawable.roundred);
                    if ((Integer) imageView7.getTag() == R.drawable.roundred) {
                        if (db.getTimeHistory(20) == "") {
                            timedisplaytext.setText("You have not used this location");
                        } else {
                            timedisplaytext.setText("Last Used: " + db.getTimeHistory(20));
                        }
                    }
                    ButtonRedtoBlack(imageView2);
                    ButtonRedtoBlack(imageView3);
                    ButtonRedtoBlack(imageView4);
                    ButtonRedtoBlack(imageView5);
                    ButtonRedtoBlack(imageView6);
                    ButtonRedtoBlack(imageView1);
                    ButtonRedtoBlack(imageView8);
                    ButtonRedtoBlack(imageView9);
                    ButtonRedtoBlack(imageView10);
                    ButtonRedtoBlack(imageView11);
                    ButtonRedtoBlack(imageView12);
                    ButtonRedtoBlack(imageView13);

                    ButtonViolet(imageView2);
                    ButtonViolet(imageView3);
                    ButtonViolet(imageView4);
                    ButtonViolet(imageView5);
                    ButtonViolet(imageView6);
                    ButtonViolet(imageView1);
                    ButtonViolet(imageView8);
                    ButtonViolet(imageView9);
                    ButtonViolet(imageView10);
                    ButtonViolet(imageView11);
                    ButtonViolet(imageView12);
                    ButtonViolet(imageView13);
                } else {
                    changePatternRoundImage(imageView7, imageView8);
                    if ((Integer) imageView7.getTag() == R.drawable.roundyellow)
                        db.insertShotTime(SaveCurrentShotTime(), 20);

                    imageView1.setImageResource(R.drawable.round);
                    imageView2.setImageResource(R.drawable.round);
                    imageView3.setImageResource(R.drawable.round);
                    imageView4.setImageResource(R.drawable.round);
                    imageView5.setImageResource(R.drawable.round);
                    imageView6.setImageResource(R.drawable.round);
                    imageView9.setImageResource(R.drawable.round);
                    imageView10.setImageResource(R.drawable.round);
                    imageView11.setImageResource(R.drawable.round);
                    imageView12.setImageResource(R.drawable.round);
                    imageView13.setImageResource(R.drawable.round);

                    imageView1.setTag(R.drawable.round);
                    imageView2.setTag(R.drawable.round);
                    imageView3.setTag(R.drawable.round);
                    imageView4.setTag(R.drawable.round);
                    imageView5.setTag(R.drawable.round);
                    imageView6.setTag(R.drawable.round);
                    imageView9.setTag(R.drawable.round);
                    imageView10.setTag(R.drawable.round);
                    imageView11.setTag(R.drawable.round);
                    imageView12.setTag(R.drawable.round);
                    imageView13.setTag(R.drawable.round);
                }
                break;
            case R.id.imageView8:
                if ((Integer) imageView8.getTag() == R.drawable.round) {
                    imageView8.setImageDrawable(getResources().getDrawable(R.drawable.roundred));
                    imageView8.setTag(R.drawable.roundred);
                    if ((Integer) imageView8.getTag() == R.drawable.roundred) {
                        if (db.getTimeHistory(21) == "") {
                            timedisplaytext.setText("You have not used this location");
                        } else {
                            timedisplaytext.setText("Last Used: " + db.getTimeHistory(21));
                        }
                    }
                    ButtonRedtoBlack(imageView2);
                    ButtonRedtoBlack(imageView3);
                    ButtonRedtoBlack(imageView4);
                    ButtonRedtoBlack(imageView5);
                    ButtonRedtoBlack(imageView6);
                    ButtonRedtoBlack(imageView7);
                    ButtonRedtoBlack(imageView1);
                    ButtonRedtoBlack(imageView9);
                    ButtonRedtoBlack(imageView10);
                    ButtonRedtoBlack(imageView11);
                    ButtonRedtoBlack(imageView12);
                    ButtonRedtoBlack(imageView13);

                    ButtonViolet(imageView2);
                    ButtonViolet(imageView3);
                    ButtonViolet(imageView4);
                    ButtonViolet(imageView5);
                    ButtonViolet(imageView6);
                    ButtonViolet(imageView7);
                    ButtonViolet(imageView1);
                    ButtonViolet(imageView9);
                    ButtonViolet(imageView10);
                    ButtonViolet(imageView11);
                    ButtonViolet(imageView12);
                    ButtonViolet(imageView13);
                } else {
                    changePatternRoundImage(imageView8, imageView9);
                    if ((Integer) imageView8.getTag() == R.drawable.roundyellow)
                        db.insertShotTime(SaveCurrentShotTime(), 21);

                    imageView10.setImageResource(R.drawable.round);
                    imageView11.setImageResource(R.drawable.round);
                    imageView12.setImageResource(R.drawable.round);
                    imageView13.setImageResource(R.drawable.round);
                    imageView1.setImageResource(R.drawable.round);
                    imageView2.setImageResource(R.drawable.round);
                    imageView3.setImageResource(R.drawable.round);
                    imageView4.setImageResource(R.drawable.round);
                    imageView5.setImageResource(R.drawable.round);
                    imageView6.setImageResource(R.drawable.round);
                    imageView7.setImageResource(R.drawable.round);


                    imageView10.setTag(R.drawable.round);
                    imageView11.setTag(R.drawable.round);
                    imageView12.setTag(R.drawable.round);
                    imageView13.setTag(R.drawable.round);
                    imageView1.setTag(R.drawable.round);
                    imageView2.setTag(R.drawable.round);
                    imageView3.setTag(R.drawable.round);
                    imageView4.setTag(R.drawable.round);
                    imageView5.setTag(R.drawable.round);
                    imageView6.setTag(R.drawable.round);
                    imageView7.setTag(R.drawable.round);

                }
                break;
            case R.id.imageView9:

                if ((Integer) imageView9.getTag() == R.drawable.round) {
                    imageView9.setImageDrawable(getResources().getDrawable(R.drawable.roundred));
                    imageView9.setTag(R.drawable.roundred);
                    if ((Integer) imageView9.getTag() == R.drawable.roundred) {
                        if (db.getTimeHistory(22) == "") {
                            timedisplaytext.setText("You have not used this location");
                        } else {
                            timedisplaytext.setText(db.getTimeHistory(22));
                        }
                    }
                    ButtonRedtoBlack(imageView2);
                    ButtonRedtoBlack(imageView3);
                    ButtonRedtoBlack(imageView4);
                    ButtonRedtoBlack(imageView5);
                    ButtonRedtoBlack(imageView6);
                    ButtonRedtoBlack(imageView7);
                    ButtonRedtoBlack(imageView8);
                    ButtonRedtoBlack(imageView1);
                    ButtonRedtoBlack(imageView10);
                    ButtonRedtoBlack(imageView11);
                    ButtonRedtoBlack(imageView12);
                    ButtonRedtoBlack(imageView13);

                    ButtonViolet(imageView2);
                    ButtonViolet(imageView3);
                    ButtonViolet(imageView4);
                    ButtonViolet(imageView5);
                    ButtonViolet(imageView6);
                    ButtonViolet(imageView7);
                    ButtonViolet(imageView8);
                    ButtonViolet(imageView1);
                    ButtonViolet(imageView10);
                    ButtonViolet(imageView11);
                    ButtonViolet(imageView12);
                    ButtonViolet(imageView13);
                } else {
                    changePatternRoundImage(imageView9, imageView10);
                    if ((Integer) imageView9.getTag() == R.drawable.roundyellow)
                        db.insertShotTime(SaveCurrentShotTime(), 22);
                    imageView11.setImageResource(R.drawable.round);
                    imageView12.setImageResource(R.drawable.round);
                    imageView13.setImageResource(R.drawable.round);
                    imageView1.setImageResource(R.drawable.round);
                    imageView2.setImageResource(R.drawable.round);
                    imageView3.setImageResource(R.drawable.round);
                    imageView4.setImageResource(R.drawable.round);
                    imageView5.setImageResource(R.drawable.round);
                    imageView6.setImageResource(R.drawable.round);
                    imageView7.setImageResource(R.drawable.round);
                    imageView8.setImageResource(R.drawable.round);

                    imageView11.setTag(R.drawable.round);
                    imageView12.setTag(R.drawable.round);
                    imageView13.setTag(R.drawable.round);
                    imageView1.setTag(R.drawable.round);
                    imageView2.setTag(R.drawable.round);
                    imageView3.setTag(R.drawable.round);
                    imageView4.setTag(R.drawable.round);
                    imageView5.setTag(R.drawable.round);
                    imageView6.setTag(R.drawable.round);
                    imageView7.setTag(R.drawable.round);
                    imageView8.setTag(R.drawable.round);

                }

                break;
            case R.id.imageView10:

                if ((Integer) imageView10.getTag() == R.drawable.round) {
                    imageView10.setImageDrawable(getResources().getDrawable(R.drawable.roundred));
                    imageView10.setTag(R.drawable.roundred);
                    if ((Integer) imageView10.getTag() == R.drawable.roundred) {
                        if (db.getTimeHistory(23) == "") {
                            timedisplaytext.setText("You have not used this location");
                        } else {
                            timedisplaytext.setText("Last Used: " + db.getTimeHistory(23));
                        }
                    }
                    ButtonRedtoBlack(imageView2);
                    ButtonRedtoBlack(imageView3);
                    ButtonRedtoBlack(imageView4);
                    ButtonRedtoBlack(imageView5);
                    ButtonRedtoBlack(imageView6);
                    ButtonRedtoBlack(imageView7);
                    ButtonRedtoBlack(imageView8);
                    ButtonRedtoBlack(imageView9);
                    ButtonRedtoBlack(imageView1);
                    ButtonRedtoBlack(imageView11);
                    ButtonRedtoBlack(imageView12);
                    ButtonRedtoBlack(imageView13);

                    ButtonViolet(imageView2);
                    ButtonViolet(imageView3);
                    ButtonViolet(imageView4);
                    ButtonViolet(imageView5);
                    ButtonViolet(imageView6);
                    ButtonViolet(imageView7);
                    ButtonViolet(imageView8);
                    ButtonViolet(imageView9);
                    ButtonViolet(imageView1);
                    ButtonViolet(imageView11);
                    ButtonViolet(imageView12);
                    ButtonViolet(imageView13);
                } else {
                    changePatternRoundImage(imageView10, imageView11);
                    if ((Integer) imageView10.getTag() == R.drawable.roundyellow)
                        db.insertShotTime(SaveCurrentShotTime(), 23);

                    imageView12.setImageResource(R.drawable.round);
                    imageView13.setImageResource(R.drawable.round);
                    imageView1.setImageResource(R.drawable.round);
                    imageView2.setImageResource(R.drawable.round);
                    imageView3.setImageResource(R.drawable.round);
                    imageView4.setImageResource(R.drawable.round);
                    imageView5.setImageResource(R.drawable.round);
                    imageView6.setImageResource(R.drawable.round);
                    imageView7.setImageResource(R.drawable.round);
                    imageView8.setImageResource(R.drawable.round);
                    imageView9.setImageResource(R.drawable.round);

                    imageView12.setTag(R.drawable.round);
                    imageView13.setTag(R.drawable.round);
                    imageView1.setTag(R.drawable.round);
                    imageView2.setTag(R.drawable.round);
                    imageView3.setTag(R.drawable.round);
                    imageView4.setTag(R.drawable.round);
                    imageView5.setTag(R.drawable.round);
                    imageView6.setTag(R.drawable.round);
                    imageView7.setTag(R.drawable.round);
                    imageView8.setTag(R.drawable.round);
                    imageView9.setTag(R.drawable.round);

                }

                break;
            case R.id.imageView11:
                if ((Integer) imageView11.getTag() == R.drawable.round) {
                    imageView11.setImageDrawable(getResources().getDrawable(R.drawable.roundred));
                    imageView11.setTag(R.drawable.roundred);
                    if ((Integer) imageView11.getTag() == R.drawable.roundred) {
                        if (db.getTimeHistory(24) == "") {
                            timedisplaytext.setText("You have not used this location");
                        } else {
                            timedisplaytext.setText("Last Used: " + db.getTimeHistory(24));
                        }
                    }
                    ButtonRedtoBlack(imageView2);
                    ButtonRedtoBlack(imageView3);
                    ButtonRedtoBlack(imageView4);
                    ButtonRedtoBlack(imageView5);
                    ButtonRedtoBlack(imageView6);
                    ButtonRedtoBlack(imageView7);
                    ButtonRedtoBlack(imageView8);
                    ButtonRedtoBlack(imageView9);
                    ButtonRedtoBlack(imageView10);
                    ButtonRedtoBlack(imageView1);
                    ButtonRedtoBlack(imageView12);
                    ButtonRedtoBlack(imageView13);

                    ButtonViolet(imageView2);
                    ButtonViolet(imageView3);
                    ButtonViolet(imageView4);
                    ButtonViolet(imageView5);
                    ButtonViolet(imageView6);
                    ButtonViolet(imageView7);
                    ButtonViolet(imageView8);
                    ButtonViolet(imageView9);
                    ButtonViolet(imageView10);
                    ButtonViolet(imageView1);
                    ButtonViolet(imageView12);
                    ButtonViolet(imageView13);
                } else {
                    changePatternRoundImage(imageView11, imageView12);
                    if ((Integer) imageView11.getTag() == R.drawable.roundyellow)
                        db.insertShotTime(SaveCurrentShotTime(), 24);


                    imageView13.setImageResource(R.drawable.round);
                    imageView1.setImageResource(R.drawable.round);
                    imageView2.setImageResource(R.drawable.round);
                    imageView3.setImageResource(R.drawable.round);
                    imageView4.setImageResource(R.drawable.round);
                    imageView5.setImageResource(R.drawable.round);
                    imageView6.setImageResource(R.drawable.round);
                    imageView7.setImageResource(R.drawable.round);
                    imageView8.setImageResource(R.drawable.round);
                    imageView9.setImageResource(R.drawable.round);
                    imageView10.setImageResource(R.drawable.round);

                    imageView13.setTag(R.drawable.round);
                    imageView1.setTag(R.drawable.round);
                    imageView2.setTag(R.drawable.round);
                    imageView3.setTag(R.drawable.round);
                    imageView4.setTag(R.drawable.round);
                    imageView5.setTag(R.drawable.round);
                    imageView6.setTag(R.drawable.round);
                    imageView7.setTag(R.drawable.round);
                    imageView8.setTag(R.drawable.round);
                    imageView9.setTag(R.drawable.round);
                    imageView10.setTag(R.drawable.round);
                }

                break;
            case R.id.imageView12:

                if ((Integer) imageView12.getTag() == R.drawable.round) {
                    imageView12.setImageDrawable(getResources().getDrawable(R.drawable.roundred));
                    imageView12.setTag(R.drawable.roundred);
                    if ((Integer) imageView12.getTag() == R.drawable.roundred) {
                        if (db.getTimeHistory(25) == "") {
                            timedisplaytext.setText("You have not used this location");
                        } else {
                            timedisplaytext.setText("Last Used: " + db.getTimeHistory(25));
                        }
                    }
                    ButtonRedtoBlack(imageView2);
                    ButtonRedtoBlack(imageView3);
                    ButtonRedtoBlack(imageView4);
                    ButtonRedtoBlack(imageView5);
                    ButtonRedtoBlack(imageView6);
                    ButtonRedtoBlack(imageView7);
                    ButtonRedtoBlack(imageView8);
                    ButtonRedtoBlack(imageView9);
                    ButtonRedtoBlack(imageView10);
                    ButtonRedtoBlack(imageView11);
                    ButtonRedtoBlack(imageView1);
                    ButtonRedtoBlack(imageView13);

                    ButtonViolet(imageView2);
                    ButtonViolet(imageView3);
                    ButtonViolet(imageView4);
                    ButtonViolet(imageView5);
                    ButtonViolet(imageView6);
                    ButtonViolet(imageView7);
                    ButtonViolet(imageView8);
                    ButtonViolet(imageView9);
                    ButtonViolet(imageView10);
                    ButtonViolet(imageView11);
                    ButtonViolet(imageView1);
                    ButtonViolet(imageView13);
                } else {
                    changePatternRoundImage(imageView12, imageView13);
                    if ((Integer) imageView12.getTag() == R.drawable.roundyellow)
                        db.insertShotTime(SaveCurrentShotTime(), 25);
                    imageView1.setImageResource(R.drawable.round);
                    imageView2.setImageResource(R.drawable.round);
                    imageView3.setImageResource(R.drawable.round);
                    imageView4.setImageResource(R.drawable.round);
                    imageView5.setImageResource(R.drawable.round);
                    imageView6.setImageResource(R.drawable.round);
                    imageView7.setImageResource(R.drawable.round);
                    imageView8.setImageResource(R.drawable.round);
                    imageView9.setImageResource(R.drawable.round);
                    imageView10.setImageResource(R.drawable.round);
                    imageView11.setImageResource(R.drawable.round);

                    imageView1.setTag(R.drawable.round);
                    imageView2.setTag(R.drawable.round);
                    imageView3.setTag(R.drawable.round);
                    imageView4.setTag(R.drawable.round);
                    imageView5.setTag(R.drawable.round);
                    imageView6.setTag(R.drawable.round);
                    imageView7.setTag(R.drawable.round);
                    imageView8.setTag(R.drawable.round);
                    imageView9.setTag(R.drawable.round);
                    imageView10.setTag(R.drawable.round);
                    imageView11.setTag(R.drawable.round);

                }
                break;
            case R.id.imageView13:

                if ((Integer) imageView13.getTag() == R.drawable.round) {
                    imageView13.setImageDrawable(getResources().getDrawable(R.drawable.roundred));
                    imageView13.setTag(R.drawable.roundred);
                    if ((Integer) imageView13.getTag() == R.drawable.roundred) {
                        if (db.getTimeHistory(26) == "") {
                            timedisplaytext.setText("You have not used this location");
                        } else {
                            timedisplaytext.setText("Last Used: " + db.getTimeHistory(26));
                        }
                    }
                    ButtonRedtoBlack(imageView2);
                    ButtonRedtoBlack(imageView3);
                    ButtonRedtoBlack(imageView4);
                    ButtonRedtoBlack(imageView5);
                    ButtonRedtoBlack(imageView6);
                    ButtonRedtoBlack(imageView7);
                    ButtonRedtoBlack(imageView8);
                    ButtonRedtoBlack(imageView9);
                    ButtonRedtoBlack(imageView10);
                    ButtonRedtoBlack(imageView11);
                    ButtonRedtoBlack(imageView12);
                    ButtonRedtoBlack(imageView1);

                    ButtonViolet(imageView2);
                    ButtonViolet(imageView3);
                    ButtonViolet(imageView4);
                    ButtonViolet(imageView5);
                    ButtonViolet(imageView6);
                    ButtonViolet(imageView7);
                    ButtonViolet(imageView8);
                    ButtonViolet(imageView9);
                    ButtonViolet(imageView10);
                    ButtonViolet(imageView11);
                    ButtonViolet(imageView12);
                    ButtonViolet(imageView1);
                } else {
                    changePatternRoundImage(imageView13, imageView1);
                    if ((Integer) imageView13.getTag() == R.drawable.roundyellow) {
                        db.insertShotTime(SaveCurrentShotTime(), 26);
                    }
                    imageView2.setImageResource(R.drawable.round);
                    imageView3.setImageResource(R.drawable.round);
                    imageView4.setImageResource(R.drawable.round);
                    imageView5.setImageResource(R.drawable.round);
                    imageView6.setImageResource(R.drawable.round);
                    imageView7.setImageResource(R.drawable.round);
                    imageView8.setImageResource(R.drawable.round);
                    imageView9.setImageResource(R.drawable.round);
                    imageView10.setImageResource(R.drawable.round);
                    imageView11.setImageResource(R.drawable.round);
                    imageView12.setImageResource(R.drawable.round);

                    imageView2.setTag(R.drawable.round);
                    imageView3.setTag(R.drawable.round);
                    imageView4.setTag(R.drawable.round);
                    imageView5.setTag(R.drawable.round);
                    imageView6.setTag(R.drawable.round);
                    imageView7.setTag(R.drawable.round);
                    imageView8.setTag(R.drawable.round);
                    imageView9.setTag(R.drawable.round);
                    imageView10.setTag(R.drawable.round);
                    imageView11.setTag(R.drawable.round);
                    imageView12.setTag(R.drawable.round);
                }
                break;
            default:
                break;
        }
    }


    private void initializeUI() {
        bodytype1 = (ImageView) findViewById(R.id.bodyimage1);
        dialog_send = (Button) findViewById(R.id.dialog_send);
        dialog_no = (Button) findViewById(R.id.dialog_cancel);
        dialog_cancel = (Button) findViewById(R.id.dialog_no);
        dialog_yes = (Button) findViewById(R.id.dialog_yes);
        activityPlusDialog();
        // activityMinusDialog();

        imageminus = (Button) findViewById(R.id.imageminus);
        imageplus = (Button) findViewById(R.id.imageplus);
        patternminus = (Button) findViewById(R.id.patternminus);
        patternplus = (Button) findViewById(R.id.patternplus);
        setup = (Button) findViewById(R.id.setup);
        image = (Button) findViewById(R.id.image);
        pattern = (Button) findViewById(R.id.pattern);

        patternrelativeLayout = (RelativeLayout) findViewById(R.id.patternrelativeLayout);
        patternup = (LinearLayout) findViewById(R.id.patternup);
        patterndown = (LinearLayout) findViewById(R.id.patterndown);
        circleLayout = (CircleLayout) findViewById(R.id.view);

        patterndownimage7 = (ImageView) findViewById(R.id.patterndownimage7);
        patterndownimage6 = (ImageView) findViewById(R.id.patterndownimage6);
        patterndownimage5 = (ImageView) findViewById(R.id.patterndownimage5);
        patterndownimage4 = (ImageView) findViewById(R.id.patterndownimage4);
        patterndownimage3 = (ImageView) findViewById(R.id.patterndownimage3);
        patterndownimage2 = (ImageView) findViewById(R.id.patterndownimage2);
        patterndownimage1 = (ImageView) findViewById(R.id.patterndownimage1);


        patternupimage8 = (ImageView) findViewById(R.id.patternupimage8);
        patternupimage9 = (ImageView) findViewById(R.id.patternupimage9);
        patternupimage10 = (ImageView) findViewById(R.id.patternupimage10);
        patternupimage11 = (ImageView) findViewById(R.id.patternupimage11);
        patternupimage12 = (ImageView) findViewById(R.id.patternupimage12);
        patternupimage13 = (ImageView) findViewById(R.id.patternupimage13);
        patternupimage14 = (ImageView) findViewById(R.id.patternupimage14);


        timedisplaytext = (TextView) findViewById(R.id.timedisplaytext);
        faces = (ImageView) findViewById(R.id.faces);


        imageView1 = (ImageView) findViewById(R.id.imageView1);
        imageView2 = (ImageView) findViewById(R.id.imageView2);
        imageView3 = (ImageView) findViewById(R.id.imageView3);
        imageView4 = (ImageView) findViewById(R.id.imageView4);
        imageView5 = (ImageView) findViewById(R.id.imageView5);
        imageView6 = (ImageView) findViewById(R.id.imageView6);
        imageView7 = (ImageView) findViewById(R.id.imageView7);
        imageView8 = (ImageView) findViewById(R.id.imageView8);
        imageView9 = (ImageView) findViewById(R.id.imageView9);
        imageView10 = (ImageView) findViewById(R.id.imageView10);
        imageView11 = (ImageView) findViewById(R.id.imageView11);
        imageView12 = (ImageView) findViewById(R.id.imageView12);
        imageView13 = (ImageView) findViewById(R.id.imageView13);

    }

    private String SaveCurrentShotTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    private long CurrentTime() {
        time = System.currentTimeMillis();
        return time;
    }

    private long ConvertTimes(String str) {
        Calendar calendar = Calendar.getInstance();
        String[] a = str.split(":");

        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(a[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(a[1]));
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTimeInMillis();
    }

    private String CurrentTimeCurrent() {
        Calendar c = Calendar.getInstance();

        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
        Date now = new Date();
        String strTime = sdfTime.format(now);

       /* //Current Time
        Date checkTime = new Date("HH:mm");
        Calendar calendar3 = Calendar.getInstance();
        calendar3.setTime(checkTime);
        Date actualTime = calendar3.getTime();*/
      /*  try {
            Date inTime = sdfTime.parse(strTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }*/


     /*   try {
            SimpleDateFormat sd = new SimpleDateFormat("HH:mm");
            Date d = sd.parse(c.toString());
        }catch (Exception e){
            e.printStackTrace();
        }*/
        return strTime;
    }

    private Date ConvertTimescurrent(String str) {
        Date mBeforeDate = null;
        int mHours = 0, mMinutes = 0;
        if (str.charAt(0) == '0') {
            mHours = Integer.parseInt(String.valueOf(str.charAt(1)));
        } else {
            mHours = Integer.parseInt(str.substring(0, str.indexOf(":")));
        }

        if (str.charAt(3) == '0') {
            mMinutes = Integer.parseInt(String.valueOf(str.charAt(4)));
        } else {
            mMinutes = Integer.parseInt(str.substring(3, 5));
        }

        Date dt = new Date();
        dt.setHours(mHours);
        dt.setMinutes(mMinutes);

        Date B = dt;
        /*Date currenttime = new Date();
        Date a = new Date(B.getTime() - Integer.parseInt(String.valueOf(currenttime)));
        if (Integer.parseInt(String.valueOf(a)) == 0) {
            return String.valueOf(a);
        }
        return String.valueOf(B.getTime());*/
        return B;
    }

    private void compareDates() {

        String mPickTime = result.get(0).toString();
        String spinnersave = db.getIntervalbyTime1(mPickTime).toString();
        if (!spinnersave.contentEquals("Select")) {

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
            Date A = dt;

            Date mBeforeDate = new Date(B.getTime() - TimeUnit.MINUTES.toMillis(Integer.parseInt(spinnersave)));
            Date mAfterDate = new Date(A.getTime() + TimeUnit.MINUTES.toMillis(Integer.parseInt(spinnersave)));


            if (mBeforeDate.before(new Date(CurrentTime())) && mAfterDate.after(new Date(CurrentTime()))) {

                timedisplaytext.setText("OK, for current shotput at " + result.get(0) + " Touch a shotput location or Exit");
                faces.setImageResource(R.drawable.smilingface);
                if (SetupActivity.isSoundONclicked) {

                    if (!onLaunch) {

                        Handler asd = new Handler();
                        Runnable asda = new Runnable() {
                            @Override
                            public void run() {
                                MediaPlayer m = new MediaPlayer();
                                try {
                                    Log.e("Abhishek", "MediaPlayer Start");
                                    AssetFileDescriptor descriptor = mContext.getAssets().openFd("rising_tone.mp3");
                                    m.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
                                    descriptor.close();

                                    m.prepare();
                                    m.start();
                                } catch (Exception e) {
                                    Log.e("Abhishek Ex:", e.toString());
                                }
                            }
                        };

                        asd.postDelayed(asda, 1000);


                    }
                }
                onLaunch = true;
                //onLaunch1 = false;

            } else {
                //   timedisplaytext.setText("It’s too early for your next ShotPut at " + sharedpreferences.getString(ModelClass.Time, ""));
//            if (ShotAceeptBeforeTime == false) {
                timedisplaytext.setText("Your next scheduled shotput is at " + result.get(0));
                faces.setImageResource(R.drawable.sadface);
                if (SetupActivity.isSoundONclicked) {
                    if (!onLaunch) {
                        Handler asd = new Handler();

                        Runnable asda = new Runnable() {
                            @Override
                            public void run() {

                                MediaPlayer m = new MediaPlayer();
                                try {
                                    Log.e("Abhishek ", "MediaPlayer Start");
                                    AssetFileDescriptor descriptor = mContext.getAssets().openFd("descending_tone.mp3");
                                    m.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
                                    descriptor.close();

                                    m.prepare();
                                    m.start();
                                } catch (Exception e) {
                                    Log.e("Abhishek Ex:", e.toString());
                                }
                            }
                        };

                        asd.postDelayed(asda, 1000);

                        //  }
                    }


                }
                onLaunch = true;
                //  onLaunch = false;
            }
        }

    }

    private void changeImage(ImageView curntImg, ImageView nxtImg) {
        if ((Integer) curntImg.getTag() == R.drawable.roundviolet) {
            curntImg.setImageResource(R.drawable.roundyellow);
            curntImg.setTag(R.drawable.roundyellow);
            nxtImg.setImageResource(R.drawable.roundviolet);
            nxtImg.setTag(R.drawable.roundviolet);
            // ShotAceeptBeforeTime = true;
            //   curntImg.setTag(R.drawable.round);
            //nxtImg.setTag(R.drawable.round);
            timedisplaytext.setText("Shotput Accepted");
            // timedisplaytext.setText("Your next shotput is expected at " +result.get(1));
            faces.setImageResource(R.drawable.sleepingface);
//            SharedPreferences prefs = getSharedPreferences("prf", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putInt(ModelClass.nextImage, nxtImg.getId());
            editor.apply();
            if (SetupActivity.isSoundONclicked) {
                final MediaPlayer mp = new MediaPlayer();
                if (mp.isPlaying()) {
                    mp.stop();
                }

                try {
                    mp.reset();
                    AssetFileDescriptor afd;
                    afd = getAssets().openFd("chime_tone.mp3");
                    mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                    mp.prepare();
                    mp.start();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        } else if ((Integer) curntImg.getTag() == R.drawable.roundred) {
            curntImg.setImageResource(R.drawable.roundyellow);
            curntImg.setTag(R.drawable.roundyellow);
            nxtImg.setImageResource(R.drawable.roundviolet);
            //curntImg.setTag(R.drawable.round);
            nxtImg.setTag(R.drawable.roundviolet);//checking purpose
            //nxtImg.setTag(R.drawable.roundred);//uncomment the proper one
            //  ShotAceeptBeforeTime = true;
            timedisplaytext.setText("Shotput Accepted");

            SharedPreferences.Editor nextImg = sharedpreferences.edit();
            nextImg.putInt(ModelClass.nextImage, nxtImg.getId());
            nextImg.apply();
            faces.setImageResource(R.drawable.sleepingface);
            if (SetupActivity.isSoundONclicked) {
                final MediaPlayer mp = new MediaPlayer();
                if (mp.isPlaying()) {
                    mp.stop();
                }

                try {
                    mp.reset();
                    AssetFileDescriptor afd;
                    afd = getAssets().openFd("chime_tone.mp3");
                    mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                    mp.prepare();
                    mp.start();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (result != null)
            if (result.size() != 0)
                db.shotAcceptBeforeTime1(result.get(0));

//                timedisplaytext.setText("shot next is "+result.get(1));
        //  mArrayList = db.getAllTimeByboolean(false);
        mTimeDateArray = (ArrayList<ModelDateTime>) db.getAllTimeDate();
        if (mTimeDateArray.size() == 0) {
            timedisplaytext.setText("OK, for current shotput is Disabled,Enable or Exit");
        } else if (mTimeDateArray.size() != 0) {
            datearray = new ArrayList<String>();
            for (d = 0; d < mTimeDateArray.size(); d++) {
                Log.e(" time is ", mTimeDateArray.get(d).getTime());
                Log.e("date is ", mTimeDateArray.get(d).getDate());


                String currentdate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                String strDate1 = currentdate;
                String strDate2 = mTimeDateArray.get(d).getDate();
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

                if (mTimeDateArray.get(d).getDate().equals("0") || appdate.before(currentdates)) {

                    datearray.add(mTimeDateArray.get(d).getTime());
                    //  mArrayList = db.getAllTimeBydate();

                }

            }
            Collections.sort(datearray);
            result = new ArrayList<String>();
            for (i = 0; i < datearray.size(); i++)

            {
                Log.e("ascending", datearray.get(i));
                a = datearray.get(i);

                //Current Time

                String checkTime = new SimpleDateFormat("HH:mm").format(new Date());

//  new Date(CurrentTimeCurrent()).compareTo(new Date(ConvertTimescurrent(a))) == 0)
                if ((new Date(ConvertTimes(a)).after(new Date(CurrentTime())))) {
                    // new Date(ConvertTimes(a)).equals(new Date(CurrentTime()))) {
                    //   boolean x = currentTime.equals(ConvertTimescurrent(a));
                    result.add(a);
                    Log.e("greater array list", a);
                    Log.e("current time", String.valueOf(time));
                }

                if (checkTime.contentEquals(a)) {
                    result.add(a);
                    Log.e("greater array list", a);
                    Log.e("current time", String.valueOf(time));
                }
            }

            Collections.sort(result);

            ArrayList<String> greater = new ArrayList<String>();
            if (result.size() == 0) {
                //  timedisplaytext.setText("OK, for current shotput is Disabled,Enable or Exit");
            } else {
                for (int j = 0; j < result.size(); j++) {
                    Log.e("sorted greater values", result.get(j));
                }
                compareDates();

            }

        }
    }

    private void changePatternRoundImage(ImageView curntImg, ImageView nxtImg) {
        if ((Integer) curntImg.getTag() == R.drawable.roundviolet) {
            curntImg.setImageResource(R.drawable.roundyellow);
            curntImg.setTag(R.drawable.roundyellow);
            nxtImg.setImageResource(R.drawable.roundviolet);
            nxtImg.setTag(R.drawable.roundviolet);
            // ShotAceeptBeforeTime = true;
            //   curntImg.setTag(R.drawable.round);
            //nxtImg.setTag(R.drawable.round);
            timedisplaytext.setText("Shotput Accepted");
            // timedisplaytext.setText("Your next shotput is expected at " +result.get(1));
            faces.setImageResource(R.drawable.sleepingface);
//            SharedPreferences prefs = getSharedPreferences("prf", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putInt(ModelClass.nextPatternRoundImage, nxtImg.getId());
            editor.apply();
            if (SetupActivity.isSoundONclicked) {
                final MediaPlayer mp = new MediaPlayer();
                if (mp.isPlaying()) {
                    mp.stop();
                }

                try {
                    mp.reset();
                    AssetFileDescriptor afd;
                    afd = getAssets().openFd("chime_tone.mp3");
                    mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                    mp.prepare();
                    mp.start();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        } else if ((Integer) curntImg.getTag() == R.drawable.roundred) {
            curntImg.setImageResource(R.drawable.roundyellow);
            curntImg.setTag(R.drawable.roundyellow);
            nxtImg.setImageResource(R.drawable.roundviolet);
            //curntImg.setTag(R.drawable.round);
            nxtImg.setTag(R.drawable.roundviolet);//checking purpose
            //nxtImg.setTag(R.drawable.roundred);//uncomment the proper one
            //  ShotAceeptBeforeTime = true;
            timedisplaytext.setText("Shotput Accepted");

            SharedPreferences.Editor nextImg = sharedpreferences.edit();
            nextImg.putInt(ModelClass.nextImage, nxtImg.getId());
            nextImg.apply();
            faces.setImageResource(R.drawable.sleepingface);
            if (SetupActivity.isSoundONclicked) {
                final MediaPlayer mp = new MediaPlayer();
                if (mp.isPlaying()) {
                    mp.stop();
                }

                try {
                    mp.reset();
                    AssetFileDescriptor afd;
                    afd = getAssets().openFd("chime_tone.mp3");
                    mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                    mp.prepare();
                    mp.start();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (result != null)
            if (result.size() != 0)
                db.shotAcceptBeforeTime1(result.get(0));

//                timedisplaytext.setText("shot next is "+result.get(1));
        //  mArrayList = db.getAllTimeByboolean(false);
        mTimeDateArray = (ArrayList<ModelDateTime>) db.getAllTimeDate();// dont take all dates
        if (mTimeDateArray.size() == 0) {
            timedisplaytext.setText("OK, for current shotput is Disabled,Enable or Exit");
        } else if (mTimeDateArray.size() != 0) {
            datearray = new ArrayList<String>();
            for (d = 0; d < mTimeDateArray.size(); d++) {
                Log.e(" time is ", mTimeDateArray.get(d).getTime());
                Log.e("date is ", mTimeDateArray.get(d).getDate());


                String currentdate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                String strDate1 = currentdate;
                String strDate2 = mTimeDateArray.get(d).getDate();
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

                if (mTimeDateArray.get(d).getDate().equals("0") || appdate.before(currentdates)) {

                    datearray.add(mTimeDateArray.get(d).getTime());
                    //  mArrayList = db.getAllTimeBydate();

                }
                //Current Time
                String checkTime = new SimpleDateFormat("HH:mm").format(new Date());
           /*     Date checkTime = null;
                try {
                    checkTime = new SimpleDateFormat("HH:mm").parse(CurrentTimeCurrent());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar calendar3 = Calendar.getInstance();
                calendar3.setTime(checkTime);
                Date actualTime = calendar3.getTime();
              *//*  if(actualTime.compareTo(new Date(ConvertTimes(a))) == 0) {
                    actualTime.getTime();
                }*//*
*/

                if (checkTime.contentEquals(a)) {
                    result.add(a);
                    Log.e("greater array list", a);
                    Log.e("current time", String.valueOf(time));
                }

            }
            Collections.sort(datearray);
            result = new ArrayList<String>();
            for (i = 0; i < datearray.size(); i++)

            {
                Log.e("ascending", datearray.get(i));
                a = datearray.get(i);
                //Current Time
//                Date checkTime = null;
                String checkTime = new SimpleDateFormat("HH:mm").format(new Date());

                /*Calendar calendar3 = Calendar.getInstance();
                calendar3.setTime(checkTime);
                Date actualTime = calendar3.getTime();*/
              /*  if(actualTime.compareTo(new Date(ConvertTimes(a))) == 0) {
                    actualTime.getTime();
                }*/


                if ((new Date(ConvertTimes(a)).after(new Date(CurrentTime())))) {
                    //   new Date(ConvertTimes(a)).equals(new Date(CurrentTime()))) {
                    // new Date(CurrentTimeCurrent()).compareTo(new Date(ConvertTimescurrent(a))) == 0) {
                    //   boolean x = currentTime.equals(ConvertTimescurrent(a));
                    result.add(a);
                    Log.e("greater array list", a);
                    Log.e("current time", String.valueOf(time));
                }
                if (checkTime.contentEquals(a)) {
                    result.add(a);
                }
            }

            Collections.sort(result);

            ArrayList<String> greater = new ArrayList<String>();
            if (result.size() == 0) {
                //  timedisplaytext.setText("OK, for current shotput is Disabled,Enable or Exit");
            } else {
                for (int j = 0; j < result.size(); j++) {
                    Log.e("sorted greater values", result.get(j));
                }
                compareDates();

            }

        }
    }

    private void ButtonRedtoBlack(ImageView thisimage) {
        if ((Integer) thisimage.getTag() == R.drawable.roundred) {
            thisimage.setImageDrawable(getResources().getDrawable(R.drawable.round));
            thisimage.setTag(R.drawable.round);
        }
    }

    private void ButtonViolet(ImageView thisimage) {
        if ((Integer) thisimage.getTag() == R.drawable.roundviolet) {
            thisimage.setImageDrawable(getResources().getDrawable(R.drawable.roundviolet));
        }
    }


    private void activityPlusDialog() {
        mPatternPlusDialog = new Dialog(this);
        mPatternPlusDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mPatternPlusDialog.setContentView(R.layout.patternplus_dialog);
        mPatternPlusDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mPatternPlusDialog.setCancelable(false);

        PatternDialogTitle = (TextView) mPatternPlusDialog.findViewById(R.id.dialog_title);
        dialog_no = (Button) mPatternPlusDialog.findViewById(R.id.dialog_cancel);
        dialog_send = (Button) mPatternPlusDialog.findViewById(R.id.dialog_send);

        dialog_no.setOnClickListener(this);
        dialog_send.setOnClickListener(this);

    }

    private long AlarmTime() {
        String mPickTime;
        if (result.size() != 0)
            mPickTime = result.get(0).toString();
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

/*
    private void activityMinusDialog() {
        mPatternMinusDialog = new Dialog(this);
        mPatternMinusDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mPatternMinusDialog.setContentView(R.layout.patternminus_dialog);
        mPatternMinusDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mPatternMinusDialog.setCancelable(false);

        PatternDialogTitle = (TextView) mPatternMinusDialog.findViewById(R.id.dialog_title);
        dialog_cancel = (Button) mPatternMinusDialog.findViewById(R.id.dialog_no);
        dialog_yes = (Button) mPatternMinusDialog.findViewById(R.id.dialog_yes);

        dialog_cancel.setOnClickListener(this);
        dialog_yes.setOnClickListener(this);
    }*/

    public void onResume() {
        super.onResume();
        int nextpattern = sharedpreferences.getInt(ModelClass.nextPatternRoundImage, 0);
        if (nextpattern == 0) {
            imageView8.setImageResource(R.drawable.roundviolet);
            imageView8.setTag(R.drawable.roundviolet);
        } else if (nextpattern != 0) {
            nextpatternimg = (ImageView) findViewById(nextpattern);
            nextpatternimg.setImageResource(R.drawable.roundviolet);
            nextpatternimg.setTag(R.drawable.roundviolet);
        }
        if ((Integer) imageView8.getTag() == R.drawable.roundviolet) {
            imageView8.setImageResource(R.drawable.roundviolet);
            imageView8.setTag(R.drawable.roundviolet);
        } else {
            imageView8.setImageResource(R.drawable.round);
            imageView8.setTag(R.drawable.round);
        }


        int next = sharedpreferences.getInt(ModelClass.nextImage, 0);
        if (next == 0) {
            patterndownimage7.setImageResource(R.drawable.roundviolet);
            patterndownimage7.setTag(R.drawable.roundviolet);

        } else if (next != 0) {
            nextimg = (ImageView) findViewById(next);
//            nextimg.setId(next);
            nextimg.setImageResource(R.drawable.roundviolet);
            nextimg.setTag(R.drawable.roundviolet);
           /* if((Integer)patternupimage14.getTag()==R.drawable.round && patterndownimage7.getTag()==R.drawable.roundviolet){
                patterndownimage7.setImageResource(R.drawable.round);
            }*/

            if ((Integer) patterndownimage7.getTag() == R.drawable.roundviolet) {
                //  if ((Integer)nextimg.getTag() != R.drawable.roundviolet) {
                patterndownimage7.setImageResource(R.drawable.roundviolet);
                patterndownimage7.setTag(R.drawable.roundviolet);

            } else {
                patterndownimage7.setImageResource(R.drawable.round);
                patterndownimage7.setTag(R.drawable.round);
            }
            if ((Integer) imageView8.getTag() == R.drawable.roundviolet) {
                imageView8.setImageResource(R.drawable.roundviolet);
                imageView8.setTag(R.drawable.roundviolet);
            } else {
                imageView8.setImageResource(R.drawable.round);
                imageView8.setTag(R.drawable.round);
            }
        }

        if (sharedpreferences.getBoolean(ModelClass.soundbutton, false)) {
            SetupActivity.isSoundONclicked = true;
        } else {
            SetupActivity.isSoundONclicked = false;
        }
        if (sharedpreferences.getBoolean(ModelClass.notifybutton, false)) {
            SetupActivity.isnotifyONclicked = true;
        } else {
            SetupActivity.isnotifyONclicked = false;
        }
        if (sharedpreferences.getBoolean(ModelClass.lockbutton, false)) {
            imageminus.setVisibility(View.INVISIBLE);
            image.setVisibility(View.INVISIBLE);
            imageplus.setVisibility(View.INVISIBLE);
            patternminus.setVisibility(View.INVISIBLE);
            pattern.setVisibility(View.INVISIBLE);
            patternplus.setVisibility(View.INVISIBLE);
        } else

        {
            imageminus.setVisibility(View.VISIBLE);
            image.setVisibility(View.VISIBLE);
            imageplus.setVisibility(View.VISIBLE);
            patternminus.setVisibility(View.VISIBLE);
            pattern.setVisibility(View.VISIBLE);
            patternplus.setVisibility(View.VISIBLE);
        }

        String time1 = db.getTimeByButtonName1(SetupActivity.TIME_DIALOG_ID1);
        String time2 = db.getTimeByButtonName1(SetupActivity.TIME_DIALOG_ID2);
        String time3 = db.getTimeByButtonName1(SetupActivity.TIME_DIALOG_ID3);
        String time4 = db.getTimeByButtonName1(SetupActivity.TIME_DIALOG_ID4);
        String time5 = db.getTimeByButtonName1(SetupActivity.TIME_DIALOG_ID5);

        String spin1 = db.getIntervalByButtonName1(0);
        String spin2 = db.getIntervalByButtonName1(1);
        String spin3 = db.getIntervalByButtonName1(2);
        String spin4 = db.getIntervalByButtonName1(3);
        String spin5 = db.getIntervalByButtonName1(4);

//TODO:  compairing all saved times in buttons
        // String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        mTimeDateArray = (ArrayList<ModelDateTime>) db.getAllTimeDate();


        if (mTimeDateArray.size() == 0) {
            timedisplaytext.setText("OK, for current shotput is Disabled,Enable or Exit");
        } else if (mTimeDateArray.size() != 0) {
            mArrayList = new ArrayList<String>();
//            datearray = new ArrayList<String>();
            for (d = 0; d < mTimeDateArray.size(); d++) {
                Log.e(" time is ", mTimeDateArray.get(d).getTime());
                Log.e("date is ", mTimeDateArray.get(d).getDate());


                String currentdate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                String strDate1 = currentdate;
                String strDate2 = mTimeDateArray.get(d).getDate();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date currentdates = null;
                try {
                    currentdates = sdf.parse(strDate1);
                } catch (ParseException e) {
                    Log.e("ParseException 1", e.toString());
                }
                Date appdate = null;
                try {
                    appdate = sdf.parse(strDate2);
                } catch (ParseException e) {
                    Log.e("ParseException 2", e.toString());
                }

                if (mTimeDateArray.get(d).getDate().equals("0") || appdate.before(currentdates)) {

                    mArrayList.add(mTimeDateArray.get(d).getTime());
                }
            }

            Collections.sort(mArrayList);
            result = new ArrayList<String>();
            for (i = 0; i < mArrayList.size(); i++)

            {
                Log.e("ascending", mArrayList.get(i));
                a = mArrayList.get(i);
// for 12.00 AM ==00:00 cannot span midnight(checks here)


                //  new Date(ConvertTimescurrent(a)).equals(CurrentTimeCurrent()))
                if ((new Date(ConvertTimes(a)).after(new Date(CurrentTime())))) {

                    //   CurrentTimeCurrent().compareTo(new Date(ConvertTimes(a))) == 0){
                    //   new Date(ConvertTimes(a)).equals(new Date(CurrentTime()))) {
                    //       new Date(CurrentTimeCurrent()).compareTo(new Date(ConvertTimescurrent(a))) == 0) {
                    result.add(a);
                    Log.e("greater array list", a);
                    Log.e("current time", String.valueOf(time));
                }
                //Current Time
                String checkTime = new SimpleDateFormat("HH:mm").format(new Date());
              /*  if(actualTime.compareTo(new Date(ConvertTimes(a))) == 0) {
                    actualTime.getTime();
                }*/
                if (checkTime.contentEquals(a)) {
                    result.add(a);
                    Log.e("greater array list", a);
                    Log.e("current time", String.valueOf(time));
                }

            }
            Collections.sort(result);

            ArrayList<String> greater = new ArrayList<String>();
            if (result.size() == 0) {
                timedisplaytext.setText("OK, for current shotput is Disabled,Enable or Exit");
            } else {
                for (int j = 0; j < result.size(); j++) {
                    Log.e("sorted greater values", result.get(j));
                }
                compareDates();

            }

        }
        //    registerReceiver(new BroadcastManager(), SendNotification());


    /*    if (result == null || result.size() == 0) {
            //added extra
        } else if (result.size() != 0) {
            if (SetupActivity.isnotifyONclicked) {
                //  if (MainActivity.mContext != null) {

                Intent alarmIntent = new Intent(this, BroadcastManager.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                //at specific time
                if (AlarmTime() != 0){
                    //   AlarmTime();
                    alarmManager.set(AlarmManager.RTC_WAKEUP, AlarmTime(), pendingIntent);
          *//*  Toast.makeText(getApplicationContext(),
                    "Broadcast Receiver Started", Toast.LENGTH_LONG)
                    .show();*//*

                }
            }
        }*/
         /*Intent intents=new Intent("MyBroadcast");
        intents.setClass(this, BroadcastManager.class);
        this.sendBroadcast(intents);*/

    }

    public IntentFilter SendNotification() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("MyBroadcast");

        return intentFilter;
    }

}

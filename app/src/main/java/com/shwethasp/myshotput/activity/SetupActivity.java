package com.shwethasp.myshotput.activity;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.LongSparseArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;

import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.shwethasp.myshotput.R;
import com.shwethasp.myshotput.SQLite.DBHelper;
import com.shwethasp.myshotput.Service.MyAlarmService;
import com.shwethasp.myshotput.Service.MyService;
import com.shwethasp.myshotput.model.ModelClass;
import com.shwethasp.myshotput.model.ModelDateTime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by shwethap on 26-04-2016.
 */
public class SetupActivity extends AppCompatActivity implements View.OnClickListener {
    TextView mToolBar_Title;
    public static Button pickTime1, pickTime2, pickTime3, pickTime4, pickTime5;
    public static boolean isLockChecked = false;
    private int pHour;
    private int pMinute;
    public static boolean isnotifyONclicked = false;
    public static boolean isSoundONclicked = false;
    public static Spinner spinner1, spinner2, spinner3, spinner4, spinner5;
    public static boolean isPicTime1Clicked = false;
    public static boolean isPicTime2Clicked = false;
    public static boolean isPicTime3Clicked = false;
    public static boolean isPicTime4Clicked = false;
    public static boolean isPicTime5Clicked = false;
    private SharedPreferences sharedpreferences;
    ArrayList<String> ascending;
    /**
     * This integer will uniquely define the dialog to be used for displaying time picker.
     */
    public static final int TIME_DIALOG_ID1 = 0;
    public static final int TIME_DIALOG_ID2 = 1;
    public static final int TIME_DIALOG_ID3 = 2;
    public static final int TIME_DIALOG_ID4 = 3;
    public static final int TIME_DIALOG_ID5 = 4;

    String[] str;
    DBHelper db;
    boolean timeisPresent = false;
    public static ArrayList<String> notiArrayList;

    private TimePickerDialog.OnTimeSetListener mTimeSetListener;

    public static Switch mSwitchNotify, mSwitchSound, mSwitchLock;
    private static final SetupActivity mSetupActivity = new SetupActivity();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
//        getSupportActionBar().hide();
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        mToolBar_Title = (TextView) findViewById(R.id.toolbar_title);
        mToolBar_Title.setText("Setup");
        db = new DBHelper(this);
        initializeUI();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        Drawable uparrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        uparrow.setColorFilter(getResources().getColor(R.color.text_theme), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(uparrow);

        pickTime1.setOnClickListener(this);
        pickTime2.setOnClickListener(this);
        pickTime3.setOnClickListener(this);
        pickTime4.setOnClickListener(this);
        pickTime5.setOnClickListener(this);


        sharedpreferences = getApplicationContext().getSharedPreferences(ModelClass.MyPREFERENCES, Context.MODE_PRIVATE);
        boolean notifying = sharedpreferences.getBoolean(ModelClass.notifybutton, false); //0 is the default value
        if (notifying) {
            isnotifyONclicked = true;
            mSwitchNotify.setChecked(true);
        } else {
            isnotifyONclicked = false;
            mSwitchNotify.setChecked(false);
        }

        boolean sounding = sharedpreferences.getBoolean(ModelClass.soundbutton, false);
        if (sounding) {
            isSoundONclicked = true;
            mSwitchSound.setChecked(true);
        } else {
            isSoundONclicked = false;
            mSwitchSound.setChecked(false);
        }

        boolean locking = sharedpreferences.getBoolean(ModelClass.lockbutton, false);
        if (locking) {
            isLockChecked = true;
            mSwitchLock.setChecked(true);
        } else {
            isLockChecked = false;
            mSwitchLock.setChecked(false);
        }

        /** Get the current time */
        final Calendar cal = Calendar.getInstance();
        pHour = cal.get(Calendar.HOUR_OF_DAY);
        pMinute = cal.get(Calendar.MINUTE);

        str = getResources().getStringArray(R.array.minutes);
        final SpinnerAdapter adap = new ArrayAdapter<String>(this, R.layout.spinner_item, str);


// set the default according to value
        spinner1.setAdapter(adap);
        spinner2.setAdapter(adap);
        spinner3.setAdapter(adap);
        spinner4.setAdapter(adap);
        spinner5.setAdapter(adap);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                db.updateInterval1(str[position].toString(), 0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String spin2 = str[position].toString();
                db.updateInterval1(spin2, 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                db.updateInterval1(str[position].toString(), 2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                db.updateInterval1(str[position].toString(), 3);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                db.updateInterval1(str[position].toString(), 4);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSwitchNotify.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor notify = sharedpreferences.edit();
                notify.putBoolean(ModelClass.notifybutton, isChecked);
                notify.commit();
            }
        });

        mSwitchSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor sound = sharedpreferences.edit();
                sound.putBoolean(ModelClass.soundbutton, isChecked);
                sound.commit();
            }
        });
        mSwitchLock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor lock = sharedpreferences.edit();
                lock.putBoolean(ModelClass.lockbutton, isChecked);
                lock.commit();
            }
        });


    }

    /**
     * Create a new dialog for time picker
     */

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case TIME_DIALOG_ID1:
                // final String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                int selectedPosition1 = spinner1.getSelectedItemPosition();
                final String spin1 = str[selectedPosition1].toString();
                return new TimePickerDialog(this,
                        mTimeSetListener =
                                new TimePickerDialog.OnTimeSetListener() {
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                        pHour = hourOfDay;
                                        pMinute = minute;
                                        updateDisplay(new StringBuilder()
                                                .append(pad(pHour)).append(":")
                                                .append(pad(pMinute)).toString(), spin1, TIME_DIALOG_ID1);


                                    }
                                }, pHour, pMinute, false);

            case TIME_DIALOG_ID2:
                final String date1 = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                int selectedPosition2 = spinner2.getSelectedItemPosition();
                final String spin2 = str[selectedPosition2].toString();
                return new TimePickerDialog(this,
                        mTimeSetListener =
                                new TimePickerDialog.OnTimeSetListener() {
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                        pHour = hourOfDay;
                                        pMinute = minute;
                                        updateDisplay(new StringBuilder()
                                                .append(pad(pHour)).append(":")
                                                .append(pad(pMinute)).toString(), spin2, TIME_DIALOG_ID2);


                                    }
                                }, pHour, pMinute, false);

            case TIME_DIALOG_ID3:

                int selectedPosition3 = spinner3.getSelectedItemPosition();
                final String spin3 = str[selectedPosition3].toString();
                return new TimePickerDialog(this,
                        mTimeSetListener =
                                new TimePickerDialog.OnTimeSetListener() {
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                        pHour = hourOfDay;
                                        pMinute = minute;
                                        updateDisplay(new StringBuilder()
                                                .append(pad(pHour)).append(":")
                                                .append(pad(pMinute)).toString(), spin3, TIME_DIALOG_ID3);


                                    }
                                }, pHour, pMinute, false);

            case TIME_DIALOG_ID4:
                int selectedPosition4 = spinner4.getSelectedItemPosition();
                final String spin4 = str[selectedPosition4].toString();
                return new TimePickerDialog(this,
                        mTimeSetListener =
                                new TimePickerDialog.OnTimeSetListener() {
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                        pHour = hourOfDay;
                                        pMinute = minute;
                                        updateDisplay(new StringBuilder()
                                                .append(pad(pHour)).append(":")
                                                .append(pad(pMinute)).toString(), spin4, TIME_DIALOG_ID4);


                                    }
                                }, pHour, pMinute, false);

            case TIME_DIALOG_ID5:
                int selectedPosition5 = spinner5.getSelectedItemPosition();
                final String spin5 = str[selectedPosition5].toString();
                return new TimePickerDialog(this,
                        mTimeSetListener =
                                new TimePickerDialog.OnTimeSetListener() {
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                        pHour = hourOfDay;
                                        pMinute = minute;
                                        updateDisplay(new StringBuilder()
                                                .append(pad(pHour)).append(":")
                                                .append(pad(pMinute)).toString(), spin5, TIME_DIALOG_ID5);


                                    }
                                }, pHour, pMinute, false);
        }
        return null;

    }

    /**
     * Updates the time in the Button
     */
    private void updateDisplay(String time, String interval, int id) {
        final String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        ArrayList<ModelDateTime> mArrayAllTime = (ArrayList<ModelDateTime>) db.getAllTimeDate();
        if (mArrayAllTime.size() == 0) {
            if (id == 0) {
                pickTime1.setText(time);
                if (db.getTimeByButtonName1(id).contentEquals("Disabled")) {
                    db.insertTimeInterval1(time, interval, id);

                } else {

                    db.updateTime1(id, time);

                }

            } else if (id == 1) {
                pickTime2.setText(time);
                if (db.getTimeByButtonName1(id).contentEquals("Disabled")) {

                    db.insertTimeInterval1(time, interval, id);

                } else {
                    db.updateTime1(id, time);

                }
            } else if (id == 2) {
                pickTime3.setText(time);
                if (db.getTimeByButtonName1(id).contentEquals("Disabled")) {
                    db.insertTimeInterval1(time, interval, id);

                } else {
                    db.updateTime1(id, time);

                }
            } else if (id == 3) {
                pickTime4.setText(time);
                if (db.getTimeByButtonName1(id).contentEquals("Disabled")) {
                    db.insertTimeInterval1(time, interval, id);

                } else {
                    db.updateTime1(id, time);

                }
            } else {
                pickTime5.setText(time);
                if (db.getTimeByButtonName1(id).contentEquals("Disabled")) {
                    db.insertTimeInterval1(time, interval, id);

                } else {
                    db.updateTime1(id, time);

                }
            }

        } else {
            for (int i = 0; i < mArrayAllTime.size(); i++) {
                mArrayAllTime.get(i);            //not properly storing
                if (mArrayAllTime.get(i).getTime().equals(time)) {
                    timeisPresent = true;
                    break;

                } else {
                    timeisPresent = false;
                }
            }


            if (id == 0) {
                if (timeisPresent) {
                    Toast.makeText(getApplicationContext(), "you cannot set repeated time", Toast.LENGTH_LONG).show();
                } else {
                    pickTime1.setText(time);
                    if (db.getTimeByButtonName1(id).contentEquals("Disabled")) {
                        db.insertTimeInterval1(time, interval, id);

                    } else {
                        db.updateTime1(id, time);


                    }
                }
            } else if (id == 1) {
                if (timeisPresent) {
                    Toast.makeText(getApplicationContext(), "you cannot set repeated time", Toast.LENGTH_LONG).show();
                } else {
                    pickTime2.setText(time);
                    if (db.getTimeByButtonName1(id).contentEquals("Disabled")) {
                        db.insertTimeInterval1(time, interval, id);

                    } else {
                        db.updateTime1(id, time);

                    }
                }
            } else if (id == 2) {
                if (timeisPresent) {
                    Toast.makeText(getApplicationContext(), "you cannot set repeated time", Toast.LENGTH_LONG).show();
                } else {
                    pickTime3.setText(time);
                    if (db.getTimeByButtonName1(id).contentEquals("Disabled")) {
                        db.insertTimeInterval1(time, interval, id);
                    } else {
                        db.updateTime1(id, time);

                    }
                }
            } else if (id == 3) {
                if (timeisPresent) {
                    Toast.makeText(getApplicationContext(), "you cannot set repeated time", Toast.LENGTH_LONG).show();
                } else {
                    pickTime4.setText(time);
                    if (db.getTimeByButtonName1(id).contentEquals("Disabled")) {
                        db.insertTimeInterval1(time, interval, id);
                    } else {
                        db.updateTime1(id, time);

                    }
                }
            } else if (id == 4) {
                if (timeisPresent) {
                    Toast.makeText(getApplicationContext(), "you cannot set repeated time", Toast.LENGTH_LONG).show();
                } else {
                    pickTime5.setText(time);
                    if (db.getTimeByButtonName1(id).contentEquals("Disabled")) {
                        db.insertTimeInterval1(time, interval, id);
                    } else {
                        db.updateTime1(id, time);

                    }
                }
            }

        }
        if (MainActivity.result == null || MainActivity.result.size() == 0) {
            //added extra
        } else if (MainActivity.result.size() != 0) {
            if (isnotifyONclicked) {

            }
        }
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
  /*  private long CurrentTimeCurrent(){
        DateFormat df = new SimpleDateFormat("dd MM yyyy, HH:mm");
        String date = df.format(Calendar.getInstance().getTime());
        return Long.parseLong(date);
    }*/

    public static SetupActivity getInstance() {
        return mSetupActivity;
    }


    /**
     * Add padding to numbers less than ten
     */

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    private void initializeUI() {

        mSwitchNotify = (Switch) findViewById(R.id.switch_notify);
        mSwitchSound = (Switch) findViewById(R.id.switch_sound);
        mSwitchLock = (Switch) findViewById(R.id.switch_lock);

        pickTime1 = (Button) findViewById(R.id.pickTime1);
        pickTime2 = (Button) findViewById(R.id.pickTime2);
        pickTime3 = (Button) findViewById(R.id.pickTime3);
        pickTime4 = (Button) findViewById(R.id.pickTime4);
        pickTime5 = (Button) findViewById(R.id.pickTime5);

        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        spinner3 = (Spinner) findViewById(R.id.spinner3);
        spinner4 = (Spinner) findViewById(R.id.spinner4);
        spinner5 = (Spinner) findViewById(R.id.spinner5);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.pickTime1:

                if (!isPicTime1Clicked) {
                    showDialog(TIME_DIALOG_ID1);

                    isPicTime1Clicked = true;
                } else {
                    pickTime1.setText("Disabled");
                    isPicTime1Clicked = false;
                    // db.deleteTime(0);
                    db.deleteTime1(0);

                }
                break;

            case R.id.pickTime2:
                if (!isPicTime2Clicked) {
                    showDialog(TIME_DIALOG_ID2);
                    isPicTime2Clicked = true;
                } else {
                    pickTime2.setText("Disabled");
                    isPicTime2Clicked = false;
                    // db.deleteTime(1);
                    db.deleteTime1(1);
                }
                break;
            case R.id.pickTime3:
                if (!isPicTime3Clicked) {
                    showDialog(TIME_DIALOG_ID3);
                    isPicTime3Clicked = true;
                } else {
                    pickTime3.setText("Disabled");
                    isPicTime3Clicked = false;
                    //db.deleteTime(2);
                    db.deleteTime1(2);


                }
                break;
            case R.id.pickTime4:
                if (!isPicTime4Clicked) {
                    showDialog(TIME_DIALOG_ID4);
                    isPicTime4Clicked = true;
                } else {
                    pickTime4.setText("Disabled");
                    isPicTime4Clicked = false;
                    db.deleteTime1(3);

                }
                break;
            case R.id.pickTime5:
                if (!isPicTime5Clicked) {
                    showDialog(TIME_DIALOG_ID5);
                    isPicTime5Clicked = true;
                } else {
                    pickTime5.setText("Disabled");
                    isPicTime5Clicked = false;
                    db.deleteTime1(4);
                }
                break;
            default:
                break;
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public void onResume() {
        super.onResume();

        pickTime1.setText(db.getTimeByButtonName1(TIME_DIALOG_ID1));
        pickTime2.setText(db.getTimeByButtonName1(TIME_DIALOG_ID2));
        pickTime3.setText(db.getTimeByButtonName1(TIME_DIALOG_ID3));
        pickTime4.setText(db.getTimeByButtonName1(TIME_DIALOG_ID4));
        pickTime5.setText(db.getTimeByButtonName1(TIME_DIALOG_ID5));

        String spin1 = db.getIntervalByButtonName1(0);
        for (int i = 0; i < str.length; i++) {
            if (str[i].contentEquals(spin1))
                spinner1.setSelection(i);
        }

        String spin2 = db.getIntervalByButtonName1(1);
        for (int i = 0; i < str.length; i++) {
            if (str[i].contentEquals(spin2))
                spinner2.setSelection(i);
        }

        String spin3 = db.getIntervalByButtonName1(2);
        for (int i = 0; i < str.length; i++) {
            if (str[i].contentEquals(spin3))
                spinner3.setSelection(i);
        }

        String spin4 = db.getIntervalByButtonName1(3);
        for (int i = 0; i < str.length; i++) {
            if (str[i].contentEquals(spin4))
                spinner4.setSelection(i);
        }

        String spin5 = db.getIntervalByButtonName1(4);
        for (int i = 0; i < str.length; i++) {
            if (str[i].contentEquals(spin5))
                spinner5.setSelection(i);


        }

    }
}


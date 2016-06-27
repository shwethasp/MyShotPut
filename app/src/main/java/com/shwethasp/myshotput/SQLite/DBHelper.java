package com.shwethasp.myshotput.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.shwethasp.myshotput.activity.BroadcastManager;
import com.shwethasp.myshotput.activity.SetupActivity;
import com.shwethasp.myshotput.model.ModelDateTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by shwethap on 06-05-2016.
 */
public class DBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Save_Time";
    public static final String TABLE_TIME = "time";
    public static final String TABLE_TIME1 = "time1";
    public static final String TIME_COLUMN_ID = "id";
    public static final String TIME_COLUMN_TIME = "time";
    public static final String TIME_COLUMN_BUTTONNAME = "buttonname";
    public static final String TIME_COLUMN_SPINNER = "spinnervalues";
    public static final String TIME_COLUMN_ISSHOTPUT = "isshotput";
    public static final String TIME_COLUMN_DATE = "date";

    //for shot table
    private static final String TABLE_SHOTS = "shots";
    public static final String SHOTS_COLUMN_ID = "id";
    public static final String SHOTS_COLUMN_TIME = "time";
    public static final String SHOTS_COLUMN_BUTTONNAME = "buttonid";
    private Context context;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
       // this.context = context;
    }



    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
//create table for setting time and interval
        String CREATE_TIME_TABLE = "CREATE TABLE " + TABLE_TIME + "("
                + TIME_COLUMN_ID + " INTEGER PRIMARY KEY," + TIME_COLUMN_TIME + " TEXT,"
                + TIME_COLUMN_BUTTONNAME + " TEXT," + TIME_COLUMN_SPINNER + " TEXT," + TIME_COLUMN_ISSHOTPUT + " TEXT)";
        db.execSQL(CREATE_TIME_TABLE);
        String CREATE_TIME_TABLE1 = "CREATE TABLE " + TABLE_TIME1 + "("
                + TIME_COLUMN_ID + " INTEGER PRIMARY KEY," + TIME_COLUMN_TIME + " TEXT,"
                + TIME_COLUMN_BUTTONNAME + " TEXT," + TIME_COLUMN_SPINNER + " TEXT," + TIME_COLUMN_DATE + " TEXT)";
        db.execSQL(CREATE_TIME_TABLE1);
        //Create table for SHOTS
        String CREATE_SHOTS_TABLE = "CREATE TABLE " + TABLE_SHOTS + "("
                + SHOTS_COLUMN_ID + " INTEGER PRIMARY KEY," + SHOTS_COLUMN_TIME + " TEXT,"
                + SHOTS_COLUMN_BUTTONNAME + " TEXT)";
        db.execSQL(CREATE_SHOTS_TABLE);

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS time");
        // Create tables again
        onCreate(db);
    }

    // code to add the new time
    public String insertTime(String time, int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TIME_COLUMN_TIME, time);
        contentValues.put(TIME_COLUMN_BUTTONNAME, id);
        // Inserting Row
        long i = db.insert(TABLE_TIME, null, contentValues);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
        return null;
    }

    // code to add the new time
    public String insertShotTime(String time, int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SHOTS_COLUMN_TIME, time);
        contentValues.put(SHOTS_COLUMN_BUTTONNAME, id);
        // Inserting Row
        long i = db.insert(TABLE_SHOTS, null, contentValues); //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
        return null;
    }

    public String insertTimeInterval(String time, String interval, int id, boolean bool) {
        // String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TIME_COLUMN_TIME, time);
        contentValues.put(TIME_COLUMN_SPINNER, interval);
        contentValues.put(TIME_COLUMN_BUTTONNAME, id);
        contentValues.put(TIME_COLUMN_ISSHOTPUT, String.valueOf(bool));
        long i = db.insert(TABLE_TIME, null, contentValues);
        db.close(); // Closing database connection
        return null;
    }

    public String insertTimeInterval1(String time, String interval, int id) {
        // String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TIME_COLUMN_TIME, time);
        contentValues.put(TIME_COLUMN_SPINNER, interval);
        contentValues.put(TIME_COLUMN_BUTTONNAME, id);
        contentValues.put(TIME_COLUMN_DATE, "0");
        long i = db.insert(TABLE_TIME1, null, contentValues);
        db.close(); // Closing database connection
        return null;
    }

    public String insertInterval(String spinnervalue, int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TIME_COLUMN_SPINNER, spinnervalue);
        contentValues.put(TIME_COLUMN_BUTTONNAME, id);

        db.insert(TABLE_TIME, null, contentValues);
        // db.insert(TABLE_TIME,contentValues, TIME_COLUMN_BUTTONNAME + "=?", new String[]{Integer.toString(id)});

        db.close();

        return null;
    }

    public Cursor getData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_TIME, null);
        return res;
    }

    public Cursor getShotData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_SHOTS, null);
        return res;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_TIME);
        return numRows;
    }

    //code to update the single time
    public int updateTime(int id, String time, boolean status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TIME_COLUMN_TIME, time);
        contentValues.put(TIME_COLUMN_BUTTONNAME, id);
        contentValues.put(TIME_COLUMN_ISSHOTPUT, String.valueOf(status));
        // updating row
        int i = db.update(TABLE_TIME, contentValues, TIME_COLUMN_BUTTONNAME + " = ?",
                new String[]{Integer.toString(id)});
        return i;

    }

    public int updateTime1(int id, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TIME_COLUMN_TIME, time);
        contentValues.put(TIME_COLUMN_BUTTONNAME, id);
        contentValues.put(TIME_COLUMN_DATE, "0");
        // updating row
        int i = db.update(TABLE_TIME1, contentValues, TIME_COLUMN_BUTTONNAME + " = ?",
                new String[]{Integer.toString(id)});
        return i;

    }

    public int updateInterval(String spinnervalue, int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TIME_COLUMN_SPINNER, spinnervalue);
        contentValues.put(TIME_COLUMN_BUTTONNAME, id);
        return db.update(TABLE_TIME, contentValues, TIME_COLUMN_BUTTONNAME + "=?", new String[]{Integer.toString(id)});
    }
    public int updateInterval1(String spinnervalue, int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TIME_COLUMN_SPINNER, spinnervalue);
        contentValues.put(TIME_COLUMN_BUTTONNAME, id);
        return db.update(TABLE_TIME1, contentValues, TIME_COLUMN_BUTTONNAME + "=?", new String[]{Integer.toString(id)});
    }

    public int updateShotTime(String time, int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SHOTS_COLUMN_TIME, time);
        contentValues.put(SHOTS_COLUMN_BUTTONNAME, id);
        return db.update(TABLE_SHOTS, contentValues, SHOTS_COLUMN_BUTTONNAME + "=?", new String[]{Integer.toString(id)});
    }

    public int shotAcceptBeforeTime(String time, boolean bool) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        // contentValues.put(TIME_COLUMN_TIME,time);
        contentValues.put(TIME_COLUMN_TIME, time);
        contentValues.put(TIME_COLUMN_ISSHOTPUT, String.valueOf(bool));
        return db.update(TABLE_TIME, contentValues, TIME_COLUMN_TIME + "=?", new String[]{time});
    }
    public int shotAcceptBeforeTime1(String time) {
        String currentdate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        // contentValues.put(TIME_COLUMN_TIME,time);
        contentValues.put(TIME_COLUMN_TIME, time);
        contentValues.put(TIME_COLUMN_DATE,currentdate );
        return db.update(TABLE_TIME1, contentValues, TIME_COLUMN_TIME + "=?", new String[]{time});
    }

    public int shotAcceptBeforeTimeDate(String time) {
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        // contentValues.put(TIME_COLUMN_TIME,time);
        contentValues.put(TIME_COLUMN_TIME, time);
        contentValues.put(TIME_COLUMN_DATE, date);
        return db.update(TABLE_TIME1, contentValues, TIME_COLUMN_TIME + "=?", new String[]{time});
    }

    public String getTimeByButtonName(int id) {
        String time = "Disabled";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = null;
        try {
            c = db.query(false, TABLE_TIME, new String[]{TIME_COLUMN_TIME}, TIME_COLUMN_BUTTONNAME + "=?", new String[]{String.valueOf(id)}, null,
                    null, null, null, null);

            if (c != null) {
                if (c.moveToFirst()) {
                    do {

                        time = c.getString(c.getColumnIndex(TIME_COLUMN_TIME));

                    } while (c.moveToNext());
                }
            }
        } catch (Exception e) {
            Log.e("getTimeByButtonName Exception", e.getMessage().toString());

        } finally {
            c.close();
            db.close();
        }

        return time;
    }

    public String getTimeByButtonName1(int id) {
        String time = "Disabled";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = null;
        try {
            c = db.query(false, TABLE_TIME1, new String[]{TIME_COLUMN_TIME}, TIME_COLUMN_BUTTONNAME + "=?", new String[]{String.valueOf(id)}, null,
                    null, null, null, null);

            if (c != null) {
                if (c.moveToFirst()) {
                    do {

                        time = c.getString(c.getColumnIndex(TIME_COLUMN_TIME));

                    } while (c.moveToNext());
                }
            }
        } catch (Exception e) {
            Log.e("getTimeByButtonName Exception", e.getMessage().toString());

        } finally {
            c.close();
            db.close();
        }

        return time;
    }
    public String getIntervalByButtonName(int id) {
        String spinnervalue = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = null;
        try {
            c = db.query(false, TABLE_TIME, new String[]{TIME_COLUMN_SPINNER}, TIME_COLUMN_BUTTONNAME + "=?", new String[]{String.valueOf(id)}, null,
                    null, null, null, null);

            if (c != null) {
                if (c.moveToFirst()) {
                    do {

                        spinnervalue = c.getString(c.getColumnIndex(TIME_COLUMN_SPINNER));

                    } while (c.moveToNext());
                }
            }
        } catch (Exception e) {
            Log.e("getIntervalByButtonName Exception", e.getMessage().toString());

        } finally {
            c.close();
            db.close();
        }

        return spinnervalue;
    }
    public String getIntervalByButtonName1(int id) {
        String spinnervalue = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = null;
        try {
            c = db.query(false, TABLE_TIME1, new String[]{TIME_COLUMN_SPINNER}, TIME_COLUMN_BUTTONNAME + "=?", new String[]{String.valueOf(id)}, null,
                    null, null, null, null);

            if (c != null) {
                if (c.moveToFirst()) {
                    do {

                        spinnervalue = c.getString(c.getColumnIndex(TIME_COLUMN_SPINNER));

                    } while (c.moveToNext());
                }
            }
        } catch (Exception e) {
            Log.e("getIntervalByButtonName Exception", e.getMessage().toString());

        } finally {
            c.close();
            db.close();
        }

        return spinnervalue;
    }

    public String getTimeHistory(int id) {
        String time = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = null;
        try {
            c = db.query(false, TABLE_SHOTS, new String[]{SHOTS_COLUMN_TIME}, SHOTS_COLUMN_BUTTONNAME + "=?", new String[]{String.valueOf(id)}, null,
                    null, null, null, null);

            if (c != null) {
                if (c.moveToFirst()) {
                    do {

                        time = c.getString(c.getColumnIndex(SHOTS_COLUMN_TIME));

                    } while (c.moveToNext());
                }
            }
        } catch (Exception e) {
            Log.e("getTimehistoryByButtonname Exception", e.getMessage().toString());

        } finally {
            c.close();
            db.close();
        }

        return time;
    }

    public ArrayList<String> getAllTime() {
        ArrayList<String> listTime = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = null;
        try {
            c = db.query(false, TABLE_TIME, null, null, null, null, null, null, null, null);


            if (c != null) {
                if (c.moveToFirst()) {
                    do {

                        listTime.add(c.getString(c.getColumnIndex(TIME_COLUMN_TIME)));
                    } while (c.moveToNext());
                }
            }
        } finally {
            c.close();
            db.close();
        }
        return listTime;
    }

    public ArrayList<String> getAllTimeBydate() {
          // String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        ArrayList<String> listTime = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = null;
        try {
//            c = db.query(false, TABLE_TIME, null, null, null, null, null, null, null, null);
            c = db.query(false, TABLE_TIME1, new String[]{TIME_COLUMN_TIME}, TIME_COLUMN_DATE + "=?", new String[]{String.valueOf("0")}, null,
                    null, null, null, null);
            if (c != null) {
                if (c.moveToFirst()) {
                    do {

                        listTime.add(c.getString(c.getColumnIndex(TIME_COLUMN_TIME)));
                    } while (c.moveToNext());
                }
            }
        } finally {
            c.close();
            db.close();
        }
        return listTime;
    }

    public ArrayList<String> getAllTimeByboolean(boolean b) {
        ArrayList<String> listTime = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = null;
        try {
//            c = db.query(false, TABLE_TIME, null, null, null, null, null, null, null, null);
            c = db.query(false, TABLE_TIME, new String[]{TIME_COLUMN_TIME}, TIME_COLUMN_ISSHOTPUT + "=?", new String[]{String.valueOf(b)}, null,
                    null, null, null, null);
            if (c != null) {
                if (c.moveToFirst()) {
                    do {

                        listTime.add(c.getString(c.getColumnIndex(TIME_COLUMN_TIME)));
                    } while (c.moveToNext());
                }
            }
        } finally {
            c.close();
            db.close();
        }
        return listTime;
    }

    /**
     * Getting all labels
     * returns list of labels
     */
    public List<String> getAllIntervals() {
        List<String> labels = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_TIME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }

    public List<ModelDateTime> getAllTimeDate() {
        ArrayList<ModelDateTime> labels = new ArrayList<ModelDateTime>();

        // Select All Query
        String selectQuery = "SELECT " + TIME_COLUMN_TIME + "," + TIME_COLUMN_DATE + " FROM " + TABLE_TIME1;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ModelDateTime modelDateTime = new ModelDateTime();
                modelDateTime.setTime(cursor.getString(0));
                modelDateTime.setDate(cursor.getString(1));
                labels.add(modelDateTime);
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }

    public List<ModelDateTime> getAllTimeDateBySetUp() {
        ArrayList<ModelDateTime> labels = new ArrayList<ModelDateTime>();

        // Select All Query
        String selectQuery = "SELECT " + TIME_COLUMN_TIME + "," + TIME_COLUMN_DATE + " FROM " + TABLE_TIME1;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ModelDateTime modelDateTime = new ModelDateTime();
                modelDateTime.setTime(cursor.getString(0));
                modelDateTime.setDate(cursor.getString(1));
                labels.add(modelDateTime);
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }

    public String getIntervalbyTime(String time) {
        String value = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = null;
        try {
            c = db.query(false, TABLE_TIME, new String[]{TIME_COLUMN_SPINNER}, TIME_COLUMN_TIME + "=?", new String[]{String.valueOf(time)}, null,
                    null, null, null, null);

            if (c != null) {
                if (c.moveToFirst()) {
                    do {

                        value = c.getString(c.getColumnIndex(TIME_COLUMN_SPINNER));

                    } while (c.moveToNext());
                }
            }
        } catch (Exception e) {
            Log.e("getIntervalbybuttonid Exception", e.getMessage().toString());

        } finally {
            c.close();
            db.close();
        }

        return value;

    }
    public String getIntervalbyTime1(String time) {
        String value = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = null;
        try {
            c = db.query(false, TABLE_TIME1, new String[]{TIME_COLUMN_SPINNER}, TIME_COLUMN_TIME + "=?", new String[]{String.valueOf(time)}, null,
                    null, null, null, null);

            if (c != null) {
                if (c.moveToFirst()) {
                    do {

                        value = c.getString(c.getColumnIndex(TIME_COLUMN_SPINNER));

                    } while (c.moveToNext());
                }
            }
        } catch (Exception e) {
            Log.e("getIntervalbybuttonid Exception", e.getMessage().toString());

        } finally {
            c.close();
            db.close();
        }

        return value;

    }

    public void deleteTime(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TIME, TIME_COLUMN_BUTTONNAME + " = ?",
                new String[]{Integer.toString(id)});
    }
    public void deleteTime1(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TIME1, TIME_COLUMN_BUTTONNAME + " = ?",
                new String[]{Integer.toString(id)});
    }


}


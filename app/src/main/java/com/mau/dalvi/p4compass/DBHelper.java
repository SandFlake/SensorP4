package com.mau.dalvi.p4compass;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Calendar;

public class DBHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelperDude";
    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "StepsDatabase.db";

    private ArrayList<DateStepsModel> DateStepsModel;



    private static final String TABLE_STEPS_SUMMARY = "StepsSummary";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String PASSWORD = "password";
    private static final String STEPS_COUNT = "stepscount";
    private static final String START_TIME = "starttime";
    private static final String CREATION_DATE = "creationdate";

    private static final String CREATE_TABLE_STEPS_SUMMARY = "CREATE TABLE " +
            TABLE_STEPS_SUMMARY + "( " + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME + " TEXT, "
            + PASSWORD + " TEXT, " + STEPS_COUNT + " INTEGER, " + START_TIME + " DOUBLE, "
            + CREATION_DATE + " TEXT " + " );";


    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, DATABASE_NAME, factory, 4);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_STEPS_SUMMARY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STEPS_SUMMARY);
        onCreate(db);
    }

   /* public boolean createStepsEntry() {
        boolean isDateAlreadyPresent = false;
        boolean createSuccessful = false;
        int currentDateStepCounts = 0;
        Calendar mCalendar = Calendar.getInstance();
        String todayDate = String.valueOf(mCalendar.get(Calendar.MONTH) + "/"
                + String.valueOf(mCalendar.get(Calendar.DAY_OF_MONTH))) + "/"
                + String.valueOf(mCalendar.get(Calendar.YEAR));

        String selectQuery = "SELECT " + STEPS_COUNT + " FROM " + TABLE_STEPS_SUMMARY + " WHERE "
                + CREATION_DATE + " = '" + todayDate + "'";

        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);
            if (c.moveToFirst()) {
                do {
                    isDateAlreadyPresent = true;
                    currentDateStepCounts = c.getInt((c.getColumnIndex(STEPS_COUNT)));
                } while (c.moveToNext());
            }
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(CREATION_DATE, todayDate);
            if (isDateAlreadyPresent) {
                values.put(STEPS_COUNT, ++currentDateStepCounts);
                int row = db.update(TABLE_STEPS_SUMMARY, values, CREATION_DATE + " = '"
                        + todayDate + "'", null);

                if (row == 1) {
                    createSuccessful = true;
                }
                db.close();
            } else {
                values.put(STEPS_COUNT, 1);
                long row = db.insert(TABLE_STEPS_SUMMARY, null, values);

                if (row != -1) {
                    createSuccessful = true;
                }
                db.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return createSuccessful;
    }*/

    public void addNewUser(User user) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME, user.getName());
        values.put(PASSWORD, user.getPassword());
        values.put(STEPS_COUNT, 0);
        values.put(START_TIME, 1);
        values.put(CREATION_DATE, getDate());
        db.insert(TABLE_STEPS_SUMMARY, null, values);
        db.close();
    }

    public boolean checkUserNameTaken(String username) {
        Log.d(TAG, "checkUserNameTaken:  GOT HERE LEL");
        SQLiteDatabase db = getWritableDatabase();
        Log.d(TAG, "checkUserNameTaken: GOT HERE ????????" + username);
        String query = "SELECT * FROM " + TABLE_STEPS_SUMMARY + " WHERE " + NAME + " = '" + username + "'";
        //Cursor point to a location in your results
        Cursor c = db.rawQuery(query, null);
        //Move to the first row in your results
        c.moveToFirst();
        boolean nameExists = c.getCount() > 0;
        db.close();
        c.close();

        Log.d(TAG, "checkUserNameTaken: SURELY YOU JEST" + username);
        return nameExists;
    }

    public String getDate() {
        Calendar cal = Calendar.getInstance();
        String date = cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.YEAR);
        return date;
    }

    public String getUserPassword(String username) {
        String userPassword = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_STEPS_SUMMARY + " WHERE " + NAME + " = '" + username + "'";
        //Cursor point to a location in your results
        Cursor c = db.rawQuery(query, null);
        //Move to the first row in your results
        c.moveToFirst();
        userPassword = c.getString(c.getColumnIndex(PASSWORD));
        db.close();
        c.close();
        return userPassword;
    }

    public int getUserSteps(String username, String date) {
        int steps;
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_STEPS_SUMMARY + " WHERE " + NAME + " = '" + username +
                "' AND " + CREATION_DATE + " = '" + date + "'";
        Cursor c = db.rawQuery(query, null);
        if (c != null && c.moveToFirst()) {
            steps = c.getInt(c.getColumnIndex(STEPS_COUNT));
            return steps;
        } else {
            return 0;
        }


    }

    public double getUserStartTime(String username, String checkDate) {
        double startTime = 0;
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT starttime FROM " + TABLE_STEPS_SUMMARY + " WHERE " + NAME + " = '" + username + "' AND " + CREATION_DATE + " = '" + checkDate + "'";
        //Cursor point to a location in your results
        Cursor c = db.rawQuery(query, null);
        //Move to the first row in your results
        c.moveToFirst();
        try {
            startTime = c.getDouble(c.getColumnIndex(START_TIME));
        } catch (CursorIndexOutOfBoundsException e) {
            Log.d(TAG, e.getMessage());
        }
        db.close();
        c.close();
        return startTime;
    }

    public void setStartTime(String username, String checkDate, double startTime) {
        double startTimeStamp = startTime;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(START_TIME, startTimeStamp);
        db.update(TABLE_STEPS_SUMMARY, values, NAME + " = '" + username + "' AND " + CREATION_DATE
                + " = '" + checkDate + "'", null);
    }

    public void addUserSteps(String username, double start) {
        int steps = 1;
        String date = getDate();
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(START_TIME, start);
        values.put(STEPS_COUNT, steps);
        values.put(CREATION_DATE, date);
        db.update(TABLE_STEPS_SUMMARY, values, NAME + " = '" + username + "'", null);
    }

    public void resetUserSteps(String username) {
        int steps = 0;
        double time = 1;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(STEPS_COUNT, steps);
        values.put(START_TIME, time);
        db.update(TABLE_STEPS_SUMMARY, values, NAME + " = '" + username + "'", null);
    }

    public void updateUserSteps(String username, String checkdate) {
        int steps = getUserSteps(username, checkdate) + 1;
        String mdate = getDate();
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(STEPS_COUNT, steps);
        Log.d(TAG, "number of steps: " + steps);
        db.update(TABLE_STEPS_SUMMARY, values, NAME + " = '" + username + "' AND " + CREATION_DATE + " = '" + mdate + "'", null);
    }

    public boolean pickedDate(String datePicked) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_STEPS_SUMMARY + " WHERE " + CREATION_DATE + " = '" + datePicked + "'";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        boolean dateExists = c.getCount() > 0;
        db.close();
        c.close();
        return dateExists;
    }


    public ArrayList<DateStepsModel> readStepsEntries(String username) {
        ArrayList<DateStepsModel> mStepCountList = new
                ArrayList<DateStepsModel>();
        String selectQuery = "SELECT * FROM " +
                TABLE_STEPS_SUMMARY + " WHERE " + NAME + " = '" + username + "''";
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);
            if (c.moveToFirst()) {
                do {
                    DateStepsModel mDateStepsModel = new
                            DateStepsModel();
                    mDateStepsModel.mDate = c.getString
                            ((c.getColumnIndex(CREATION_DATE)));
                    mDateStepsModel.mStepCount = c.getInt
                            ((c.getColumnIndex(STEPS_COUNT)));
                    mStepCountList.add(mDateStepsModel);
                } while (c.moveToNext());
            }
            db.close();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "readStepsEntries: " + mStepCountList);
        return mStepCountList;

    }


    public Cursor viewData(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * from " + TABLE_STEPS_SUMMARY;
        Cursor cursor = db.rawQuery(query, null);

        return cursor;
    }
}


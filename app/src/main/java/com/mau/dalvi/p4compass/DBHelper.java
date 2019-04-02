package com.mau.dalvi.p4compass;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DBHelper extends SQLiteOpenHelper {

    private ArrayList<DateStepsModel> DateStepsModel;
    private static final String TAG = "DatabaseHelperDude";
    private static final int databaseVersion = 6;
    private static final String databaseName = "StepsDatabase.db";

    private static final String UserStepTable = "StepsSummary";
    private static final String id = "id";
    private static final String name = "name";
    private static final String password = "password";
    private static final String stepCount = "stepscount";
    private static final String startTime = "starttime";
    private static final String creationDate = "creationdate";

    private static final String CREATE_TABLE_STEPS_SUMMARY = "CREATE TABLE " +
            UserStepTable + "( " + id + " INTEGER PRIMARY KEY AUTOINCREMENT, " + name + " TEXT, "
            + password + " TEXT, " + stepCount + " INTEGER, " + startTime + " DOUBLE, "
            + creationDate + " TEXT " + " );";


    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, databaseName, factory, databaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_STEPS_SUMMARY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + UserStepTable);
        onCreate(db);
    }


    public void addNewUser(User user) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(name, user.getName());
        values.put(password, user.getPassword());
        values.put(stepCount, 0);
        values.put(startTime, 1);
        values.put(creationDate, getDate());
        db.insert(UserStepTable, null, values);
        db.close();
    }

    public boolean checkUserNameTaken(String username) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + UserStepTable + " WHERE " + name + " = '" + username + "'";
        //Cursor point to a location in your results
        Cursor c = db.rawQuery(query, null);
        //Move to the first row in your results
        c.moveToFirst();
        boolean nameExists = c.getCount() > 0;
        db.close();
        c.close();

        return nameExists;
    }

    public String getDate() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String date = sdf.format(c);
        return date;
    }

    public String getUserPassword(String username) {
        String userPassword = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + UserStepTable + " WHERE " + name + " = '" + username + "'";
        //Cursor point to a location in your results
        Cursor c = db.rawQuery(query, null);
        //Move to the first row in your results
        c.moveToFirst();
        userPassword = c.getString(c.getColumnIndex(password));
        Log.d(TAG, "getUserPassword: " + userPassword);
        db.close();
        c.close();
        return userPassword;
    }

    public int getUserSteps(String username, String date) {
        int steps;
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + UserStepTable + " WHERE " + name + " = '" + username +
                "' AND " + creationDate + " = '" + date + "'";
        Cursor c = db.rawQuery(query, null);
        if (c != null && c.moveToFirst()) {
            steps = c.getInt(c.getColumnIndex(stepCount));
            return steps;
        } else {
            return 0;
        }


    }


    public void setStartTime(String username, String checkDate, double startTime) {
        double startTimeStamp = startTime;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.startTime, startTimeStamp);
        db.update(UserStepTable, values, name + " = '" + username + "' AND " + creationDate
                + " = '" + checkDate + "'", null);
    }

    public void addUserSteps(String username, double start) {
        int steps = 1;
        String date = getDate();
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(startTime, start);
        values.put(stepCount, steps);
        values.put(creationDate, date);
        db.update(UserStepTable, values, name + " = '" + username + "'", null);
    }

    public void resetUserSteps(String username) {
        int steps = 0;
        double time = 1;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(stepCount, steps);
        values.put(startTime, time);
        db.update(UserStepTable, values, name + " = '" + username + "'", null);
    }

    public void updateUserSteps(String username, String checkdate) {
        int steps = getUserSteps(username, checkdate) + 1;
        String date = getDate();
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(stepCount, steps);
        db.update(UserStepTable, values, name + " = '" + username + "' AND " + creationDate + " = '" + date + "'", null);
    }

    public boolean pickedDate(String datePicked) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + UserStepTable + " WHERE " + creationDate + " = '" + datePicked + "'";
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
                UserStepTable + " WHERE " + name + " = '" + username + "'";
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);
            if (c.moveToFirst()) {
                do {
                    DateStepsModel mDateStepsModel = new
                            DateStepsModel();
                    mDateStepsModel.mDate = c.getString
                            ((c.getColumnIndex(creationDate)));
                    mDateStepsModel.mStepCount = c.getInt
                            ((c.getColumnIndex(stepCount)));
                    mStepCountList.add(mDateStepsModel);
                } while (c.moveToNext());
            }
            db.close();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mStepCountList;

    }

}


package com.mau.dalvi.p4compass;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

public class StepCounter extends Service implements SensorEventListener {

    private static final String TAG = "StepCounter";

    private SensorManager sensorManager;
    private Sensor stepDetector;
    private DBHelper db;
    private LocalBinder mBinder;
    private MainActivity mainActivity;
    private boolean startTimeSet;

    public StepCounter() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mBinder = new LocalBinder();
        Toast.makeText(this, "Service created", Toast.LENGTH_LONG).show();

        sensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR) != null) {
            Toast.makeText(this, "step detector found", Toast.LENGTH_SHORT).show();
            stepDetector = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        } else {
            Toast.makeText(this, "No step detector available", Toast.LENGTH_LONG).show();
        }

        db = new DBHelper(this, null, null);

    }


    public int onStartCommand(Intent intent, int flags, int startID) {
        Toast.makeText(this, "Services started", Toast.LENGTH_LONG).show();
        return super.onStartCommand(intent, flags, startID);

    }

    public void setListenerActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    private String getDate() {
        Calendar cal = Calendar.getInstance();
        String date = cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.MONTH ) + "/" + cal.get(Calendar.YEAR);
        Log.d(TAG, "Today's date: " + date);
        return date;
    }

    public void resetStartTime() {
        startTimeSet = false;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        sensorManager.registerListener(this, stepDetector, SensorManager.SENSOR_DELAY_NORMAL);
        return mBinder;
    }

    public class LocalBinder extends Binder {
        StepCounter getService() {
            return StepCounter.this;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        Log.d(TAG, "onSensorChanged: steps since reboot " + String.valueOf(event.values[0]));


        double timestampSeconds = event.timestamp / 1000000000;
        String date = getDate();
        if (!startTimeSet) {
            db.setStartTime(mainActivity.getUsername(), date, timestampSeconds);
            Log.d(TAG, "onSensorChanged: " + mainActivity.getUsername());
            startTimeSet = true;
        } else {
            if (!db.pickedDate(date)) {
                Log.d(TAG, date + " does not exist");
                Log.d(TAG, "Step registered");
                db.addUserSteps(mainActivity.getUsername(), timestampSeconds);
                mainActivity.addStep();
                Log.d(TAG, "steps added to " + date);
            } else {
                Log.d(TAG, date + " exists!");
                Log.d(TAG, "Step registered");
                db.updateUserSteps(mainActivity.getUsername(), getDate());
                mainActivity.addStep();
                Log.d(TAG, "steps updated on " + date);
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


}

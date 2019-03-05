package com.mau.dalvi.p4compass;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

public class StepCounter extends Service implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor stepDetector;
    private ServiceConnection serviceConnection;

    private MainActivity mainActivity;
    private ServiceConnection mConnection;
    private Intent stepsIntent;
    public boolean mBound;
    private LocalBinder mBinder;
    private DBHelper mStepsDBHelper;
    private OnChangeListener listener;


    public StepCounter(){
    }

    public void setListenerActivity(OnChangeListener listener){
     mListener = listener;
    }

    public void onCreate(){
        super.onCreate();
        mBinder = new LocalBinder();

        sensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR) != null){
            stepDetector = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
            mStepsDBHelper = new DBHelper(this);
        }

        mConnection = new ServiceConnection(this);
        stepsIntent = new Intent(this, StepCounter.class);
        bindService(stepsIntent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        sensorManager.registerListener(this, stepDetector, SensorManager.SENSOR_DELAY_NORMAL);
        return mBinder;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        mStepsDBHelper.createStepsEntry();
        mListener.update();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public class LocalBinder extends Binder {
        StepCounter getService() {
            return StepCounter.this;
        }
    }

    public void onDestroy(){
        super.onDestroy();
        if(mBound){
            unbindService(mConnection);
            mBound = false;
        }
        Log.v("Pedometer ", "service removed");
    }
}

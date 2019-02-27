/*
package com.mau.dalvi.p4compass;


import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import static android.content.Context.SENSOR_SERVICE;


*/
/**
 * A simple {@link Fragment} subclass.
 *//*

public class CompassFragment extends Fragment implements SensorEventListener {

    private ImageView imgCompass;
    private SensorManager mSensorManager;
    private Sensor mAccelerometerSensor, mMagnetometerSensor, mOrientationSensor;
    private boolean useOrientationAPI;
    private float mLastAccelerometer, mLastMagnetometer, mRotationMatrix, mCurrentDegree, azimuthInDegress;
    private boolean mLastAccelerometerSet, mLastMagnetometerSet;
    private String lastUpdateTime;
    private Sensor mOrientation;



    public CompassFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_compass, container, false);


        mSensorManager = (SensorManager) getContext().getSystemService (SENSOR_SERVICE);
        mAccelerometerSensor = mSensorManager.getDefaultSensor (Sensor.TYPE_ACCELEROMETER);
        mMagnetometerSensor = mSensorManager.getDefaultSensor (Sensor.TYPE_MAGNETIC_FIELD);
        mOrientationSensor = mSensorManager.getDefaultSensor (Sensor.TYPE_ORIENTATION);
        imgCompass = (ImageView) view.findViewById(R.id.imgCompass);

        return view;
    }

    public void onResume(){
        super.onResume();
        if (useOrientationAPI){
            mSensorManager.registerListener(this, mAccelerometerSensor, SensorManager.SENSOR_DELAY_UI);
            mSensorManager.registerListener(this, mMagnetometerSensor, SensorManager.SENSOR_DELAY_UI);
        } else {
            mSensorManager.registerListener(this, mOrientationSensor, SensorManager.SENSOR_DELAY_UI);
        }

    }

    public void onPause(){
        super.onPause();
        if (useOrientationAPI){
            mSensorManager.unregisterListener(this, mAccelerometerSensor);
            mSensorManager.unregisterListener(this, mMagnetometerSensor);
        } else {
            mSensorManager.unregisterListener(this, mOrientationSensor);
        }

    }

    public void rotateUsingOrientationAPI(SensorEvent event) {
        if (event.sensor == mAccelerometerSensor) {
            System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.length);
            mLastAccelerometerSet = true;
        } else if (event.sensor == mMagnetometerSensor) {
            System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.length);
            mLastMagnetometerSet = true;
        } //only 4 times in 1 second if

         (mLastAccelerometerSet && mLastMagnetometerSet && System.currentTimeMillis() - lastUpdateTime > 250) {
            SensorManager.getRotationMatrix(mRotationMatrix, null, mLastAccelerometer, mLastMagnetometer);
            SensorManager.getOrientation(mRotationMatrix, mOrientation);

            float azimuthInRadians = mOrientation[0];

            float azimuthInDegrees = (float) (Math.toDegrees(azimuthInRadians) + 360) % 360;
            RotateAnimation mRotateAnimation = new RotateAnimation( mCurrentDegree, -azimuthInDegress, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            mRotateAnimation.setDuration(250);
            mRotateAnimation.setFillAfter(true);

            imgCompass.startAnimation(mRotateAnimation);
            mCurrentDegree = -azimuthInDegress;
            lastUpdateTime = System.currentTimeMillis();
        } }

    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
*/

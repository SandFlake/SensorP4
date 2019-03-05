
package com.mau.dalvi.p4compass;


import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.TriggerEvent;
import android.hardware.TriggerEventListener;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import static android.content.Context.SENSOR_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */

public class CompassFragment extends Fragment implements SensorEventListener {


    private ImageView imgCompass;
    private SensorManager mSensorManager;
    private Sensor mAccelerometerSensor, mMagnetometerSensor;
    private float mLastAccelerometer[], mLastMagnetometer[], mRotationMatrix[], mCurrentDegree, azimuthInDegress;
    private boolean mLastAccelerometerSet, mLastMagnetometerSet;
    private long lastUpdateTime;
    private float mOrientation[];

    private Sensor significantMotion;
    private MediaPlayer mMediaPlayer;
    private boolean isAccelerometerPresent;
    private boolean isFirstValue;
    private float x, y, z, last_x, last_y, last_z;
    private float shakeThreshold;
    private TextView tv1;
    private TriggerEventListener triggerEventListener;


    public CompassFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_compass, container, false);

        tv1 = view.findViewById(R.id.tv1);


        mSensorManager = (SensorManager) getContext().getSystemService(SENSOR_SERVICE);
        mAccelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        imgCompass = view.findViewById(R.id.imgCompass);
        mMediaPlayer = MediaPlayer.create(getContext().getApplicationContext(), R.raw.drums);
        significantMotion = mSensorManager.getDefaultSensor(Sensor.TYPE_SIGNIFICANT_MOTION);

        if (significantMotion != null) {
            mSensorManager.requestTriggerSensor(new SignificantMotionListener(), significantMotion);
        }


        return view;
    }

    public class SignificantMotionListener extends TriggerEventListener {
        @Override
        public void onTrigger(TriggerEvent event) {

            tv1.setText("SM triggered! click for another request");

        }
    }


    public void onResume() {
        super.onResume();

        mSensorManager.registerListener(this, mAccelerometerSensor, SensorManager.SENSOR_DELAY_UI);
        Toast.makeText(getContext(), "Accelerometer found", Toast.LENGTH_LONG).show();
        this.isFirstValue = false;

        mSensorManager.registerListener(this, mMagnetometerSensor, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(this, significantMotion, SensorManager.SENSOR_DELAY_NORMAL);


    }

    public void onPause() {
        super.onPause();

        mSensorManager.unregisterListener(this, mAccelerometerSensor);
        mSensorManager.unregisterListener(this, mMagnetometerSensor);
        mSensorManager.unregisterListener(this, significantMotion);

    }

    public void rotateUsingOrientationAPI(SensorEvent event) {


        if (event.sensor == mAccelerometerSensor) {
            System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.length);
            mLastAccelerometerSet = true;
        } else if (event.sensor == mMagnetometerSensor) {
            System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.length);
            mLastMagnetometerSet = true;
        } //only 4 times in 1 second

        if (mLastAccelerometerSet && mLastMagnetometerSet && System.currentTimeMillis() - lastUpdateTime > 250) {
            SensorManager.getRotationMatrix(mRotationMatrix, null, mLastAccelerometer, mLastMagnetometer);
            SensorManager.getOrientation(mRotationMatrix, mOrientation);

            float azimuthInRadians = mOrientation[0];

            float azimuthInDegrees = (float) (Math.toDegrees(azimuthInRadians) + 360) % 360;
            RotateAnimation mRotateAnimation = new RotateAnimation(mCurrentDegree, -azimuthInDegress, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            mRotateAnimation.setDuration(250);
            mRotateAnimation.setFillAfter(true);

            imgCompass.startAnimation(mRotateAnimation);
            mCurrentDegree = -azimuthInDegress;
            lastUpdateTime = System.currentTimeMillis();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        x = event.values[0];
        y = event.values[1];
        z = event.values[2];

        Log.d("x and y values changing", "maybe");

        if (isFirstValue) {
            float deltaX = Math.abs(last_x - x);
            float deltaY = Math.abs(last_y - y);
            float deltaZ = Math.abs(last_z - z);

            if ((deltaX > shakeThreshold && deltaY > shakeThreshold)
                    || (deltaX > shakeThreshold && deltaZ > shakeThreshold)
                    || (deltaY > shakeThreshold && deltaZ > shakeThreshold)) {

                if (!mMediaPlayer.isPlaying()) {
                    mMediaPlayer.start();
                }
            }

        }

        last_x = x;
        last_y = y;
        last_z = z;
        isFirstValue = true;


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}


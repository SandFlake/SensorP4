
package com.mau.dalvi.p4compass;


import android.content.Context;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.SENSOR_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */

public class CompassFragment extends Fragment implements SensorEventListener {

    private static final String TAG = "CompassFragment";
    private ImageView imgCompass;
    private TextView tv1, tvSteps, tvStepsSec;
    private Button btnReset, btnHistory;

    private SensorManager mSensorManager;
    private Sensor mAccelerometerSensor, mMagnetometerSensor;
    private MediaPlayer mMediaPlayer;
    private DBHelper dbHelper;
    MainActivity mainActivity;

    private float[] mLastAccelerometer = new float[3];
    private float[] mLastMagnetometer = new float[3];
    private float[] mRotationMatrix = new float[9];
    private float[] mOrientation = new float[3];

    private boolean mLastAccelerometerSet = false;
    private boolean mLastMagnetometerSet = false;
    private boolean isFirstValue;
    private float x, y, z, last_x, last_y, last_z;
    private float currentDegree = 0f;
    private float shakeThreshold = 50;
    private double steps = 0;
    private int secondCounter;
    Timer timer;
    private double lastUpdateTime;


    public CompassFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_compass, container, false);

        dbHelper = new DBHelper(mainActivity, null, null);

        Log.d(TAG, "onCreateView: service bound yey");

        tvSteps = view.findViewById(R.id.tvSteps);
        tvStepsSec = view.findViewById(R.id.tvStepsSec);
        btnReset = view.findViewById(R.id.btnReset);
        btnHistory = view.findViewById(R.id.btnHistory);
        imgCompass = view.findViewById(R.id.imgCompass);
        timer = new Timer();

        mSensorManager = (SensorManager) getContext().getSystemService(SENSOR_SERVICE);
        mAccelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        mMediaPlayer = MediaPlayer.create(getContext().getApplicationContext(), R.raw.drums);


        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.btnResetClicked();
                secondCounter = 0;
                steps = 0;
                dbHelper.resetUserSteps(mainActivity.getUsername());
                tvSteps.setText("Amount of steps: " + steps);
                tvStepsSec.setText("Steps per second: N/A");
            }
        });

        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.readStepsEntries(mainActivity.getUsername());
                mainActivity.btnHistoryClicked();

                }

        });

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                secondCounter++;
            }
        }, 1000, 1000);

        addStep();
        return view;
    }



    public void onResume() {
        super.onResume();

        mSensorManager.registerListener(this, mAccelerometerSensor, SensorManager.SENSOR_DELAY_UI);
        Toast.makeText(getContext(), "Accelerometer found & reg", Toast.LENGTH_LONG).show();
        mSensorManager.registerListener(this, mMagnetometerSensor, SensorManager.SENSOR_DELAY_UI);
        Toast.makeText(getContext(), "Magnet sensor found & reg", Toast.LENGTH_SHORT).show();
    }

    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this, mAccelerometerSensor);
        mSensorManager.unregisterListener(this, mMagnetometerSensor);
        Toast.makeText(getContext(), "Acc and Mag unregistered", Toast.LENGTH_SHORT).show();
    }


    public void setActivity(MainActivity activity) {
        this.mainActivity = activity;
    }

    public void addStep() {
        steps = (double) dbHelper.getUserSteps(mainActivity.getUsername(), dbHelper.getDate());
        Log.d(TAG, "addStep: melons" + steps + " " + dbHelper.getDate() + " apples " + dbHelper.getDate());

        tvSteps.setText("Amount of steps: " + steps);
        Log.d(TAG, "addStep: current number of steg " + steps);

        if (secondCounter != 0) {
            tvStepsSec.setText("Steps per second: " + (steps / secondCounter));
            Log.d(TAG, "addStep: strawberries" + (steps / secondCounter));
        } else {
            tvStepsSec.setText("Steps per second: " + 0);
        }
    }


    @Override
    public void onSensorChanged(SensorEvent event) {

        //Compass through API
        if (event.sensor == mAccelerometerSensor) {
            System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.length);
            mLastAccelerometerSet = true;
        } else if (event.sensor == mMagnetometerSensor) {
            System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.length);
            mLastMagnetometerSet = true;
        } //only 4 times in 1 second

        if (mLastAccelerometerSet && mLastMagnetometerSet && System.currentTimeMillis() - lastUpdateTime > 500) {
            SensorManager.getRotationMatrix(mRotationMatrix, null, mLastAccelerometer, mLastMagnetometer);
            SensorManager.getOrientation(mRotationMatrix, mOrientation);
            float azimuthInRadians = mOrientation[0];
            float azimuthInDegrees = (float) (Math.toDegrees(azimuthInRadians) + 360) % 360;
            RotateAnimation mRotateAnimation = new RotateAnimation(currentDegree, -azimuthInDegrees,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            mRotateAnimation.setDuration(10);
            mRotateAnimation.setFillAfter(true);

            imgCompass.startAnimation(mRotateAnimation);
            currentDegree = -azimuthInDegrees;
            lastUpdateTime = System.currentTimeMillis();
        }


        // Rotation of image when a shake is detected
        if (event.sensor == mAccelerometerSensor) {
            x = event.values[0];
            y = event.values[1];
            z = event.values[2];

            if (isFirstValue) {

                float speed = Math.abs(x + y + z - last_x - last_y - last_z);

                if (speed > shakeThreshold) {
                    if (!mMediaPlayer.isPlaying()) {
                        mMediaPlayer.start();

                        float toDegrees = new Random().nextFloat() * Integer.MAX_VALUE % 720;
                        RotateAnimation spinRandom = new RotateAnimation(currentDegree, toDegrees,
                                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

                        imgCompass.startAnimation(spinRandom);
                        spinRandom.setDuration(3000);
                    }

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


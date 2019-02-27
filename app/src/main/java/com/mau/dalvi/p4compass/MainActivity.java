package com.mau.dalvi.p4compass;


import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.TriggerEvent;
import android.hardware.TriggerEventListener;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private TextView tv1, tv2;
    private SensorManager mSensorManager;
    private Sensor gravity, orientation, significantMotion, mAccelerometer;
    private MediaPlayer mMediaPlayer;
    private boolean isAccelerometerPresent;
    private boolean isFirstValue;
    private float x, y, z, last_x, last_y, last_z;
    private float shakeThreshold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv1 = findViewById(R.id.tv1);
        mSensorManager = (SensorManager)getSystemService (SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor (Sensor.TYPE_ACCELEROMETER);
        mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.drums);

        mSensorManager.requestTriggerSensor(new SignificantMotionListener(), significantMotion);

    }

    protected void onResume() {
        super.onResume();

            mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
            Toast.makeText(this, "Accelerometer found",Toast.LENGTH_LONG ).show();
            this.isFirstValue = false;

    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener( this,    mAccelerometer); }

    @Override
    public void onSensorChanged(SensorEvent event) {

        x = event.values[0];
        y = event.values[1];
        z = event.values[2];

        if (isFirstValue) {
            float deltaX = Math.abs(last_x - x);
            float deltaY = Math.abs(last_y - y);
            float deltaZ = Math.abs(last_z - z);

            if ((deltaX > shakeThreshold && deltaY > shakeThreshold)
                || (deltaX > shakeThreshold && deltaZ > shakeThreshold )
                    || (deltaY > shakeThreshold && deltaZ > shakeThreshold)) {

                if(!mMediaPlayer.isPlaying()){
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

    private class SignificantMotionListener extends TriggerEventListener {

        @Override
        public void onTrigger(TriggerEvent event) {
            tv1.setText("SM triggered! click for another request");
        }
    }
}

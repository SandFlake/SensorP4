package com.mau.dalvi.p4compass;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;

public class MyServiceConnection implements ServiceConnection {

    private StepCounter stepCounter;
    private MainActivity ma;
    private CompassFragment compassFragment;


    public MyServiceConnection(CompassFragment compassFragment) {
        this.compassFragment = compassFragment;
    }
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        StepCounter.LocalBinder binder = (StepCounter.LocalBinder) service;

        stepCounter  = binder.getService();
        stepCounter.mBound = true;
        stepCounter.setListenerActivity(compassFragment);

    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

        stepCounter.mBound = false;
    }
}

package com.mau.dalvi.p4compass;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;

public class MyServiceConnection implements ServiceConnection {

    private StepCounter stepCounter;
    private MainActivity mainActivity;
    private CompassFragment compassFragment;


    public MyServiceConnection(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        StepCounter.LocalBinder binder = (StepCounter.LocalBinder) service;


        mainActivity.stepCounter = binder.getService();
        mainActivity.bound = true;
        mainActivity.stepCounter.setListenerActivity(mainActivity);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        mainActivity.bound = false;
    }
}

package com.mau.dalvi.p4compass;


import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.TriggerEvent;
import android.hardware.TriggerEventListener;
import android.media.MediaPlayer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    StepCounter stepCounter;
    private DBHelper dbHelper;
    private LoginFragment loginFragment;
    MyServiceConnection serviceConnection;
    private FragmentManager fragMan;
    private CompassFragment compassFragment;
    private String username;
    boolean bound;

    ListView userList;
    ArrayList<String> listitem;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragMan = getSupportFragmentManager();
        serviceConnection = new MyServiceConnection(this);
        dbHelper = new DBHelper(this, null, null);

        listitem = new ArrayList<>();
     //   userList = findViewById(R.id.user_list);
        viewData();

        if (compassFragment == null) {
            compassFragment = new CompassFragment();
            compassFragment.setActivity(this);
        }

        if (loginFragment == null) {
            loginFragment = new LoginFragment();
            loginFragment.setActivity(this);
            setFragment(loginFragment, true);
        }
    }

    private void viewData() {
        Cursor cursor = dbHelper.viewData();

        if (cursor.getCount() == 0 ){
            Log.d(TAG, "viewData: there is no data yet");
        } else {
            while (cursor.moveToNext()){
                listitem.add(cursor.getString(1));
            }

            adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listitem);
            userList.setAdapter(adapter);
        }
    }


    public void setFragment(Fragment fragment, boolean backstack) {
        FragmentTransaction transaction = fragMan.beginTransaction();

        transaction.replace(R.id.container, fragment);

        if (backstack) {
            transaction.addToBackStack(null);
        }

        transaction.commit();
    }

    public void btnLoginClicked() {
        setFragment(compassFragment, true);
    }

    public void btnResetClicked() {
        resetHistory();
    }

    public void bindService(Intent intent) {
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (bound) {
            unbindService(serviceConnection);
            bound = false;
        }
        Toast.makeText(this, "Service destroyed", Toast.LENGTH_SHORT).show();

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void resetHistory() {
        dbHelper.resetUserSteps(username);
    }

    public void addStep() {
        compassFragment.addStep();
    }

}

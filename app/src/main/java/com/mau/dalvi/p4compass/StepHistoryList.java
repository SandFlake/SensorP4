package com.mau.dalvi.p4compass;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;





public class StepHistoryList extends Fragment {

    private static final String TAG = "StepHistoryList";
    private ListView lvList;
    private ArrayList<String> arrayList;
    private ArrayAdapter<String> arrayAdapter;
    private DBHelper dbHelper;
    MainActivity mainActivity;

    public StepHistoryList(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step_history_list, container, false);
        initComponents(view);
        setListView();
        return view;
    }


    private void initComponents(View view){

        lvList = view.findViewById(R.id.list_item_view);
        dbHelper = new DBHelper(mainActivity, null, null);
    }

    public void setActivity(MainActivity mainActivity){this.mainActivity = mainActivity; }

    private void setListView(){
    arrayList = new ArrayList<>();
        Log.d(TAG, "setListView: got name " + mainActivity.getUsername());
    List<DateStepsModel> userList = dbHelper.readStepsEntries(mainActivity.getUsername());
    for (int i = 0; i < userList.size(); i++) {
        arrayList.add("Date: " + userList.get(i).mDate + "    Steps taken: " + userList.get(i).mStepCount);
        Log.d(TAG, "setListView: " + userList.get(i).mDate + " " + userList.get(i).mStepCount);
    }

    arrayAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(),
            R.layout.fragment_list_view, R.id.tvListItem, arrayList);
            lvList.setAdapter(arrayAdapter);
    }

    }




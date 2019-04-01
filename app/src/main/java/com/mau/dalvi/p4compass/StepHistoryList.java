package com.mau.dalvi.p4compass;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;





public class StepHistoryList extends Fragment {

    private ListView lvList;
    private ArrayList<String> arrayList;
    private ArrayAdapter<String> arrayAdapter;
    private DBHelper db;
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
    }

    private void setListView(){
    arrayList = new ArrayList<>();
    List<DateStepsModel> userList = db.readStepsEntries(mainActivity.getUsername());
    for (int i = 0; i < userList.size(); i++) {
        arrayList.add("Date: " + userList.get(i).mDate + " steps taken: " + userList.get(i).mStepCount);
    }

    arrayAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(),
            R.layout.fragment_list_view, R.id.tvListItem, arrayList);
            lvList.setAdapter(arrayAdapter);
    }

    }

  /*  private ListView listView;
    private ListAdapter adapter;
    List<User> userData;
    MainActivity mainActivity;


    public StepHistoryList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_step_history_list, container, false);

        listView = view.findViewById(R.id.list_item_view);
        List<String> test_list = new ArrayList<String>();
       // List<User> step_history_test = new ArrayList<User>();
        test_list.add("uno");
        test_list.add("dos");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String> (
                mainActivity,
                android.R.layout.simple_list_item_1,
                test_list );

        listView.setAdapter(arrayAdapter);

        return view;
    }*/



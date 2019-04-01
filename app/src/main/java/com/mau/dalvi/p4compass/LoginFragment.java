package com.mau.dalvi.p4compass;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    private static final String TAG = "LoginFragment";

    private EditText etUserName;
    private EditText etPassword;
    private Button btnLogin;
    private String username, password;
    private DBHelper dbHelper;
    StepCounter step;
    MainActivity mainActivity;
    private MyServiceConnection myServiceConnection;

    public LoginFragment() {
        // Required empty public constructor
    }

    public void setActivity(MainActivity main) {
        mainActivity = main;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        initComponents(view);
        dbHelper = new DBHelper(mainActivity, null, null);


        btnLogin.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (etUserName.getText().toString().isEmpty() || etPassword.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Can't start without a name/password mate", Toast.LENGTH_LONG).show();
                } else {
                    username = etUserName.getText().toString();
                    password = etPassword.getText().toString();

                    mainActivity.setUsername(username);

                    if (!dbHelper.checkUserNameTaken(username)) {
                        if (password.length() >= 6 && username.length() >= 4) {
                            dbHelper.addNewUser(new User(username, password, 0));
                            mainActivity.setUsername(username);
                            mainActivity.btnLoginClicked();
                            startActivity();
                        } else if (password.length() < 6) {
                            Toast.makeText(getActivity(), "Password must be 6 characters", Toast.LENGTH_SHORT).show();
                        } else if (username.length() < 4) {
                            Toast.makeText(getActivity(), "Username must be 4 characters", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (password.equals(dbHelper.getUserPassword(username))) {
                            startActivity();
                            mainActivity.btnLoginClicked();
                        } else {
                            System.out.println("Password is wrong");
                        }
                    }
                }
                mainActivity.setUsername(username);
                startActivity();
            }
        });
        return (view);

    }


    private void initComponents(View view) {
        etUserName = view.findViewById(R.id.etUserName);
        etPassword = view.findViewById(R.id.etPassword);
        btnLogin = view.findViewById(R.id.btnLogin);
    }

    private void startActivity() {
        Intent intent = new Intent(mainActivity, StepCounter.class);
        intent.putExtra("Username", username);
        intent.putExtra("Password", password);
        myServiceConnection = new MyServiceConnection(mainActivity);
        mainActivity.bindService(intent, myServiceConnection, Context.BIND_AUTO_CREATE);

    }

}

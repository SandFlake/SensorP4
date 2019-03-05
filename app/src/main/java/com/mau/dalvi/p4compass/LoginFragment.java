package com.mau.dalvi.p4compass;


import android.os.Bundle;
import android.support.v4.app.AppComponentFactory;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
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
public class LoginFragment extends Fragment{

    private EditText etUserName;
    private EditText etPassword;
    private Button btnLogin;
    private String username, password;
    private Controller controller;
    private DBHelper dbHelper;


    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        initComponents(view);


        btnLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etUserName.getText().toString().isEmpty() ||  etPassword.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Can't start without a name mate", Toast.LENGTH_LONG).show();
                } else {
                    String username = etUserName.getText().toString();
                    String password = etPassword.getText().toString();

                    if (!dbHelper.checkUserNameTaken(username)){
                            if (password.length() >= 6 && username.length() > 4) {
                                    dbHelper.addNewUser(new User(username, password, 0));
                                    startActivity(i);
                            }
                            }
                }
            }
        });
        return (view);
    }


    public void setController(Controller controller){
        this.controller = controller;
    }


        private void initComponents(View view) {
            etUserName = view.findViewById(R.id.etUserName);
            etPassword = view.findViewById(R.id.etPassword);
            btnLogin = view.findViewById(R.id.btnLogin);
        }


}


}

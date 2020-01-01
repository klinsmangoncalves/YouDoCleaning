package br.com.kmg.youdocleaning.ui;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;

import br.com.kmg.youdocleaning.R;
import br.com.kmg.youdocleaning.util.PreferencesManagerUtil;

public class LoginActivity extends AppCompatActivity {

    Button btLogin;
    private final String LOGIN_EXTRA = "login_extra";
    private final String PASSWORD_EXTRA = "password_extra";
    private TextInputLayout tivLogin;
    private TextInputLayout tivPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        if(PreferencesManagerUtil.isLogged(getApplicationContext())){
            goToNextActivity();
        }

        btLogin = findViewById(R.id.bt_do_login);
        tivLogin = findViewById(R.id.tiv_login);
        tivPassword = findViewById(R.id.tiv_password);

        if(savedInstanceState != null && savedInstanceState.containsKey(LOGIN_EXTRA)){
            tivLogin.getEditText().setText(savedInstanceState.getString(LOGIN_EXTRA));
            tivPassword.getEditText().setText(savedInstanceState.getString(PASSWORD_EXTRA));
        }

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLogin();
            }
        });

    }

    private void doLogin(){
        //will handle the custom login for each company user in the future
        PreferencesManagerUtil.setLogged(getApplicationContext(), true);
        goToNextActivity();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(LOGIN_EXTRA, tivLogin.getEditText().getText().toString() );
        outState.putString(PASSWORD_EXTRA, tivPassword.getEditText().getText().toString() );
    }

    private void goToNextActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }
}

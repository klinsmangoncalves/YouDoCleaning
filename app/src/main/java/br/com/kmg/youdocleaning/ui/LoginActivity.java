package br.com.kmg.youdocleaning.ui;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;

import br.com.kmg.youdocleaning.R;
import br.com.kmg.youdocleaning.util.PreferencesManagerUtil;

public class LoginActivity extends AppCompatActivity {

    Button btLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(PreferencesManagerUtil.isLogged(getApplicationContext())){
            goToNextActivity();
        }

        btLogin = findViewById(R.id.bt_do_login);
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

    private void goToNextActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }
}

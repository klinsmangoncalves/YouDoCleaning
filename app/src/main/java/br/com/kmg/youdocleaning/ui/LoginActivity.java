package br.com.kmg.youdocleaning.ui;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import br.com.kmg.youdocleaning.R;
import br.com.kmg.youdocleaning.util.PreferencesManagerUtil;

public class LoginActivity extends AppCompatActivity {

    Button btLogin;
    private final String LOGIN_EXTRA = "login_extra";
    private final String PASSWORD_EXTRA = "password_extra";
    private TextInputLayout tivLogin;
    private TextInputLayout tivPassword;
    private TextView tvResetPassword;

    private FirebaseAuth mAuth;

    private final String TAG = "LoginActivity_";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();

        btLogin = findViewById(R.id.bt_do_login);
        tivLogin = findViewById(R.id.tiv_login);
        tivPassword = findViewById(R.id.tiv_password);
        tvResetPassword = findViewById(R.id.tv_forgot_password);

        if(savedInstanceState != null && savedInstanceState.containsKey(LOGIN_EXTRA)){
            tivLogin.getEditText().setText(savedInstanceState.getString(LOGIN_EXTRA));
            tivPassword.getEditText().setText(savedInstanceState.getString(PASSWORD_EXTRA));
        }

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login = tivLogin.getEditText().getText().toString();
                String password = tivPassword.getEditText().getText().toString();
                loginFirebase(login, password);
            }
        });

        tvResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login = tivLogin.getEditText().getText().toString();
                 resetPassword(login);
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            //loginFirebase("klinsman.goncalves@gmail.com","123456");
        }else {
            goToNextActivity(true);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(LOGIN_EXTRA, tivLogin.getEditText().getText().toString() );
        outState.putString(PASSWORD_EXTRA, tivPassword.getEditText().getText().toString() );
    }

    private void loginFirebase(String email, String password){

        if(email == null || email.isEmpty() || password == null || password.isEmpty()){
            Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email.trim().toLowerCase(), password.trim())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                    }
                });
    }

    private void updateUI(FirebaseUser currentUser) {
        Log.d(TAG, currentUser != null ? currentUser.getUid() : " null ");
        if(currentUser != null ){
            goToNextActivity(false);
        }
    }

    private void resetPassword(String emailAddress){
        if(emailAddress == null | emailAddress.isEmpty()){
            Toast.makeText(getApplicationContext(), getString(R.string.msg_invalid_email), Toast.LENGTH_LONG).show();
            return;
        }

        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.sendPasswordResetEmail(emailAddress.toLowerCase().trim())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), getString(R.string.msg_email_sent), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), getString(R.string.error_network), Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    private void goToNextActivity(boolean noAnimation){
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        if(noAnimation){
            this.overridePendingTransition(0, 0);
        }

    }
}

package br.com.kmg.youdocleaning.application;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class CleaningApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}

package br.com.kmg.youdocleaning;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;
import android.widget.Toast;

public class CleaningProgress extends AppCompatActivity {
    private Chronometer chronometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cleaning_progress);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab_report_issue);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        chronometer = findViewById(R.id.chronometer);
        //chronometer.setFormat("%s");
        chronometer.setBase(SystemClock.elapsedRealtime());

        startChronometer();
    }

    public void startChronometer() {
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.start();
    }


    public void stopChronometer() {
        chronometer.setBase(SystemClock.elapsedRealtime());
    }

}

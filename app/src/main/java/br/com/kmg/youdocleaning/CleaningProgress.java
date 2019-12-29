package br.com.kmg.youdocleaning;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.kmg.youdocleaning.database.FirebaseManager;
import br.com.kmg.youdocleaning.model.Cleaning;
import br.com.kmg.youdocleaning.model.CleaningStatus;

public class CleaningProgress extends AppCompatActivity implements FirebaseManager.OnReadFirebaseCurrentCleaning {
    private Chronometer chronometer;
    private Button mFinishCleaning;
    private FloatingActionButton fab;
    private TextView tvTimeStarted;
    private Cleaning mCleaning;
    private String mCurrentKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cleaning_progress);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        mFinishCleaning = findViewById(R.id.bt_finish_cleaning);
        chronometer = findViewById(R.id.chronometer);
        fab = findViewById(R.id.fab_report_issue);
        tvTimeStarted = findViewById(R.id.tv_time_started);

        setClickListeners();

        FirebaseManager.getInstance().getCurrentCleaning();
        FirebaseManager.getInstance().setmCurrentCleaningListener(this);

//        startChronometer(SystemClock.elapsedRealtime());
    }

    private void setClickListeners(){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mFinishCleaning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCleaning != null){
                    mCleaning.setFinishCleaning(new Date());
                    mCleaning.setStatus(CleaningStatus.FINISHED.getDescription());
                    FirebaseManager.getInstance().saveCleaning(mCleaning);
                    FirebaseManager.getInstance().deleteCurrentCleaning();
                    openMainActivity();
                }
            }
        });
    }

    public void startChronometer(long startedTime) {
        Log.d("HORARIO", String.valueOf(SystemClock.elapsedRealtime()));
        Log.d("HORARIO date ", String.valueOf(new Date().getTime()));
        Log.d("HORARIO parameter ", String.valueOf(startedTime));
        chronometer.setBase(startedTime);
        chronometer.start();
    }

    @Override
    public void onReadCurrentCleaning(Cleaning cleaning) {
        mCleaning = cleaning;
        mFinishCleaning.setEnabled(true);
        long currentTimeMilis = new Date().getTime();
        long startedCleaningTime = cleaning.getStartCleaning().getTime();
        long diff = currentTimeMilis - startedCleaningTime;
        long elapsedTime = SystemClock.elapsedRealtime() - diff;

        startChronometer(elapsedTime);
        FirebaseManager.getInstance().setmCurrentCleaningListener(null);
        setStartedCleaning(cleaning.getStartCleaning());
    }

    private void setStartedCleaning(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String dateTime = formatter.format(date);
        tvTimeStarted.setText("Início: " + dateTime);
    }

    private void openMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}

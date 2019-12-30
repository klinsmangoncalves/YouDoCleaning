package br.com.kmg.youdocleaning;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
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
import br.com.kmg.youdocleaning.database.FirestoreManager;
import br.com.kmg.youdocleaning.model.Cleaning;
import br.com.kmg.youdocleaning.model.CleaningStatus;
import br.com.kmg.youdocleaning.model.FireStoreCleaning;
import br.com.kmg.youdocleaning.model.Timestamp;

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
                finishCleaning();
            }
        });
    }

    public void startChronometer(long startedTime) {
        chronometer.setBase(startedTime);
        chronometer.start();
    }

    private void finishCleaning(){
        if (mCleaning != null){
            mCleaning.setFinishCleaning( new Timestamp());
            mCleaning.setStatus(CleaningStatus.FINISHED.getDescription());
            FirebaseManager.getInstance().saveCleaning(mCleaning);
            FirebaseManager.getInstance().deleteCurrentCleaning();
            FirestoreManager.getInstance().saveCleaning(new FireStoreCleaning(mCleaning));
            openMainActivity();
            Toast.makeText(this, getString(R.string.cleaning_finished_message), Toast.LENGTH_LONG).show();
            updateWidgets(null);
        }
    }

    @Override
    public void onReadCurrentCleaning(Cleaning cleaning) {
        if(cleaning != null){
            mCleaning = cleaning;
            mFinishCleaning.setEnabled(true);
            long currentTimeMillis = new Date().getTime();
            long startedCleaningTime = cleaning.getStartCleaning().getTime();
            long diff = currentTimeMillis - startedCleaningTime;
            long elapsedTime = SystemClock.elapsedRealtime() - diff;

            startChronometer(elapsedTime);
            FirebaseManager.getInstance().setmCurrentCleaningListener(null);
            setStartedCleaning(cleaning.getStartCleaning());
            updateWidgets(elapsedTime);
        }
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

    private void updateWidgets(Long elapsedTime){
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int [] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, CleaningWidgetProvider.class));
        CleaningWidgetProvider.updateCleaningWidgets(this, appWidgetManager, appWidgetIds, elapsedTime);
    }
}

package br.com.kmg.youdocleaning.ui;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

import br.com.kmg.youdocleaning.R;
import br.com.kmg.youdocleaning.database.AppConfigResource;
import br.com.kmg.youdocleaning.database.FireBaseCleaningManager;
import br.com.kmg.youdocleaning.database.FirestoreManager;
import br.com.kmg.youdocleaning.listener.OnReadFirebaseCurrentCleaning;
import br.com.kmg.youdocleaning.model.Cleaning;
import br.com.kmg.youdocleaning.model.CleaningStatus;
import br.com.kmg.youdocleaning.model.FireStoreCleaning;
import br.com.kmg.youdocleaning.model.Timestamp;
import br.com.kmg.youdocleaning.util.DateUtil;

public class CleaningProgress extends AppCompatActivity implements OnReadFirebaseCurrentCleaning {
    private Chronometer chronometer;
    private Button mFinishCleaning;
    private FloatingActionButton fab;
    private TextView tvTimeStarted;
    private Cleaning mCleaning;

    private final String TAG = "CleaningProgress";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cleaning_progress);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mFinishCleaning = findViewById(R.id.bt_finish_cleaning);
        chronometer = findViewById(R.id.chronometer);
        fab = findViewById(R.id.fab_report_issue);
        tvTimeStarted = findViewById(R.id.tv_time_started);

        setClickListeners();

        FireBaseCleaningManager.getInstance().getCurrentCleaning();
        FireBaseCleaningManager.getInstance().setmCurrentCleaningListener(this);
    }

    private void setClickListeners(){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, getString(R.string.menu_report_issue), Snackbar.LENGTH_LONG)
                        .setAction(getString(R.string.action_open_whatsapp), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                reportIssue();
                            }
                        }).show();
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
            FireBaseCleaningManager.getInstance().saveCleaning(mCleaning);
            FireBaseCleaningManager.getInstance().deleteCurrentCleaning();
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
            FireBaseCleaningManager.getInstance().setmCurrentCleaningListener(null);
            setStartedCleaning(cleaning.getStartCleaning());
            updateWidgets(elapsedTime);
        }
    }

    private void setStartedCleaning(Date date){
        String dateTime = DateUtil.getDateStringFromDate(date);
        tvTimeStarted.setText(getString(R.string.label_cleaning_start, dateTime));
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_progress, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_call_manager:
                callManager();
                break;
            case R.id.menu_about:
                callAboutScreenActivity();
                break;
            case R.id.menu_finish_cleaning:
                finishCleaning();
                break;
            case R.id.menu_report_issue:
                reportIssue();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void callManager(){
        String contactNumber = AppConfigResource.getInstance().getConfig().getContactNumberWhatsApp();
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + contactNumber));
        startActivity(intent);
    }

    private void callAboutScreenActivity(){
    }

    private void reportIssue(){
        if(mCleaning == null){
            return;
        }
        String message = getString(R.string.msg_issue_report, mCleaning.getIdDepartment());
        String contactNumber = AppConfigResource.getInstance().getConfig().getContactNumberWhatsApp();

        if(contactNumber == null){
            return;
        }

        sendWhatsappMessage(contactNumber, message);
    }

    private void sendWhatsappMessage(String contactNumber, String startMessage){
        String url = "";
        try {
            url = "https://api.whatsapp.com/send?phone="+ contactNumber +"&text=" + URLEncoder.encode(startMessage, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, e.toString());
            Toast.makeText(this, getString(R.string.error_unexpected), Toast.LENGTH_LONG).show();
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
}

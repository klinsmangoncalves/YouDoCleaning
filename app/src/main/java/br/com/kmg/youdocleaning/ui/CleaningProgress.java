package br.com.kmg.youdocleaning.ui;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.kmg.youdocleaning.R;
import br.com.kmg.youdocleaning.adapter.TaskListAdapter;
import br.com.kmg.youdocleaning.database.AppConfigResource;
import br.com.kmg.youdocleaning.database.FireBaseCleaningManager;
import br.com.kmg.youdocleaning.database.FirestoreManager;
import br.com.kmg.youdocleaning.listener.OnReadFirebaseCurrentCleaning;
import br.com.kmg.youdocleaning.model.Cleaning;
import br.com.kmg.youdocleaning.model.CleaningStatus;
import br.com.kmg.youdocleaning.model.CleaningTask;
import br.com.kmg.youdocleaning.model.FireStoreCleaning;
import br.com.kmg.youdocleaning.model.Timestamp;
import br.com.kmg.youdocleaning.util.DateUtil;

public class CleaningProgress extends AppCompatActivity implements OnReadFirebaseCurrentCleaning {
    private Button mbtFinishCleaning;
    private FloatingActionButton fab;
    private TextView tvTimeStarted;
    private Cleaning mCleaning;
    private ListView lvTasks;
    Toolbar toolbar;

    private final String TAG = "CleaningProgress";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cleaning_progress);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mbtFinishCleaning = findViewById(R.id.bt_finish_cleaning);
        fab = findViewById(R.id.fab_report_issue);
        tvTimeStarted = findViewById(R.id.tv_time_started);
        lvTasks = findViewById(R.id.lv_tasks);

        setClickListeners();

        FireBaseCleaningManager.getInstance().setmCurrentCleaningListener(this);
        FireBaseCleaningManager.getInstance().getCurrentCleaning(FirebaseAuth.getInstance().getCurrentUser().getUid());

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

        mbtFinishCleaning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishCleaning();
            }
        });
    }


    private void finishCleaning(){
        if (mCleaning != null){
            mCleaning.setFinishCleaning( new Timestamp());
            mCleaning.setStatus(CleaningStatus.FINISHED.getDescription());
            mCleaning.setUserId(FirebaseAuth.getInstance().getCurrentUser().getUid());
            FireBaseCleaningManager.getInstance().saveCleaning(mCleaning);
            FireBaseCleaningManager.getInstance().deleteCurrentCleaning(FirebaseAuth.getInstance().getCurrentUser().getUid());
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
            mbtFinishCleaning.setEnabled(true);
            mbtFinishCleaning.setBackgroundResource(R.drawable.bg_button_stop);
            long currentTimeMillis = new Date().getTime();
            long startedCleaningTime = cleaning.getStartCleaning().getTime();
            long diff = currentTimeMillis - startedCleaningTime;
            long elapsedTime = SystemClock.elapsedRealtime() - diff;

            FireBaseCleaningManager.getInstance().setmCurrentCleaningListener(null);
            setStartedCleaning(cleaning.getStartCleaning());
            updateWidgets(elapsedTime);
            setLvTasksList(cleaning.getIdDepartment());
        }
    }

    private void setLvTasksList(String departmentId){
        FirestoreManager.getInstance().getTasks(departmentId, new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<CleaningTask> cleaningTaskList = new ArrayList<>();
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        CleaningTask cleaningTask = document.toObject(CleaningTask.class);
                        cleaningTaskList.add(cleaningTask);
                        Log.d(TAG, document.getId() + " => " + document.getData());
                    }
                    setCleaningTasksListAdapter(cleaningTaskList);
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.qr_code_not_recognized_message), Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private void setCleaningTasksListAdapter(List<CleaningTask> cleaningTasks){
        TaskListAdapter adapter = new TaskListAdapter(cleaningTasks);
        lvTasks.setAdapter(adapter);

    }
    private void setStartedCleaning(Date date){
        String dateTime = DateUtil.getDateStringFromDate(date);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            toolbar.setTitle(getString(R.string.label_cleaning_start, dateTime));
        } else {
            tvTimeStarted.setText(getString(R.string.label_cleaning_start, dateTime));
        }

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
            case R.id.menu_logout:
                doLogout();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void callManager(){
        String contactNumber = AppConfigResource.getInstance().getContacts().getCellphone();
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + contactNumber));
        startActivity(intent);
    }

    private void callAboutScreenActivity(){
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    private void reportIssue(){
        if(mCleaning == null){
            return;
        }
        String message = getString(R.string.msg_issue_report, mCleaning.getIdDepartment());
        String contactNumber = AppConfigResource.getInstance().getContacts().getWhatsapp();

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

    private void doLogout(){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}

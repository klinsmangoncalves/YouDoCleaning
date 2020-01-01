package br.com.kmg.youdocleaning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import br.com.kmg.youdocleaning.adapter.CleaningListAdapter;
import br.com.kmg.youdocleaning.database.AppConfigResource;
import br.com.kmg.youdocleaning.database.FireBaseCleaningManager;
import br.com.kmg.youdocleaning.listener.OnReadCleaningListListener;
import br.com.kmg.youdocleaning.listener.OnReadFirebaseCurrentCleaning;
import br.com.kmg.youdocleaning.model.Cleaning;
import br.com.kmg.youdocleaning.model.CleaningStatus;
import br.com.kmg.youdocleaning.model.Timestamp;

public class MainActivity extends AppCompatActivity implements OnReadFirebaseCurrentCleaning {

    private Button mBtReadQRCode;
    private boolean mLaunchedWidget = false;
    private LinearLayoutManager mLinearLayoutManager;
    private CleaningListAdapter mFirebaseAdapter;
    private RecyclerView mMessageRecyclerView;
    private TextView tvEmptyCleaningList;
    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        AppConfigResource.getInstance();

        mMessageRecyclerView = findViewById(R.id.rv_cleaning_list);
        mBtReadQRCode = findViewById(R.id.bt_read_qr_code);
        tvEmptyCleaningList = findViewById(R.id.tv_cleaning_list_empty);

        GridLayoutManager gridLayoutManager;

        //In landscape more movie posters can be set in activity, so, it checks the
        //  orientation before set the grid.
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            int GRID_LENGTH_PORTRAIT = 2;
            gridLayoutManager = new GridLayoutManager(this, GRID_LENGTH_PORTRAIT);
        } else {
            int GRID_LENGTH_LANDSCAPE = 3;
            gridLayoutManager = new GridLayoutManager(this, GRID_LENGTH_LANDSCAPE);
        }

        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true);
        mMessageRecyclerView.setLayoutManager(gridLayoutManager);

        FireBaseCleaningManager.getInstance().getCurrentCleaning();
        FireBaseCleaningManager.getInstance().setmCurrentCleaningListener(this);

        Intent intent = getIntent();
        if(intent != null && intent.hasExtra(CleaningWidgetProvider.WIDGET_INFO_EXTRA)){
            mLaunchedWidget = true;
        }

        mFirebaseAdapter = FireBaseCleaningManager.getInstance().getFirebaseCleaningAdapter();
        mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = mFirebaseAdapter.getItemCount();
                int lastVisiblePosition = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the user is at the bottom of
                //the list, scroll to the bottom of the list to show the newly added message.
                if (lastVisiblePosition == -1 || (positionStart >= (friendlyMessageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    mMessageRecyclerView.scrollToPosition(positionStart);
                }
            }
        });

        mMessageRecyclerView.setAdapter(mFirebaseAdapter);
        setListeners();

    }

    private void setListeners(){
        mBtReadQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readQrCode();
            }
        });

        mFirebaseAdapter.setmReadListListener(new OnReadCleaningListListener() {
            @Override
            public void onReadCleaningList() {
                tvEmptyCleaningList.setVisibility(View.GONE);
            }
        });
    }

    private void readQrCode(){
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setPrompt(getString(R.string.label_qr_code_reading));
        intentIntegrator.setBeepEnabled(false);
        intentIntegrator.setBarcodeImageEnabled(false);
        intentIntegrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null && result.getContents() != null) {
            String qrCodeDepartment = result.getContents();
            Cleaning cleaning = new Cleaning(qrCodeDepartment, new Timestamp(), null, CleaningStatus.RUNNING.getDescription());
            FireBaseCleaningManager.getInstance().saveCurrentCleaning(cleaning);
            Toast.makeText(this, getString(R.string.qr_code_recognized_message), Toast.LENGTH_LONG).show();
            openProgressActivity();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onReadCurrentCleaning(Cleaning cleaning) {
        if(cleaning != null){
            openProgressActivity();
        } else {
            mBtReadQRCode.setEnabled(true);
            if(mLaunchedWidget){
                readQrCode();
            }
        }
    }

    private void openProgressActivity(){
        Intent intent = new Intent(this, CleaningProgress.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAdapter.startListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

        String message = getString(R.string.msg_issue_report_no_department);
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

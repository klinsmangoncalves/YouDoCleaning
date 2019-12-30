package br.com.kmg.youdocleaning;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import br.com.kmg.youdocleaning.database.FirebaseManager;
import br.com.kmg.youdocleaning.database.FirestoreManager;
import br.com.kmg.youdocleaning.model.Cleaning;
import br.com.kmg.youdocleaning.model.CleaningStatus;
import br.com.kmg.youdocleaning.model.FireStoreCleaning;
import br.com.kmg.youdocleaning.model.Timestamp;

public class
MainActivity extends AppCompatActivity implements FirebaseManager.OnReadFirebaseCurrentCleaning {

    private Button mBtReadQRCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtReadQRCode = findViewById(R.id.bt_read_qr_code);

        FirebaseManager.getInstance().getCurrentCleaning();
        FirebaseManager.getInstance().setmCurrentCleaningListener(this);

        mBtReadQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readQrCode();
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
            FirebaseManager.getInstance().saveCurrentCleaning(cleaning);
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
        }
    }

    private void openProgressActivity(){
        Intent intent = new Intent(this, CleaningProgress.class);
        startActivity(intent);
        finish();
    }
}

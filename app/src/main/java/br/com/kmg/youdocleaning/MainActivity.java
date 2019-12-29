package br.com.kmg.youdocleaning;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.Serializable;
import java.util.Date;

import br.com.kmg.youdocleaning.database.FirebaseManager;
import br.com.kmg.youdocleaning.model.Cleaning;
import br.com.kmg.youdocleaning.model.CleaningStatus;

public class MainActivity extends AppCompatActivity {

    private Button mBtReadQRCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtReadQRCode = findViewById(R.id.bt_read_qr_code);
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
            Cleaning cleaning = new Cleaning(qrCodeDepartment, new Date(), null, CleaningStatus.RUNNING.getDescription());
            String cleaningId = FirebaseManager.getInstance().saveCleaning(cleaning);
            Intent intent = new Intent(this, CleaningProgress.class);
            startActivity(intent);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}

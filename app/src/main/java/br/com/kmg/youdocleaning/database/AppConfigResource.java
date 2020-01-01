package br.com.kmg.youdocleaning.database;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import br.com.kmg.youdocleaning.model.AppConfig;

public class AppConfigResource {

    private final String TAG = "AppConfigResource";
    private final String APP_CONFIG_DOCUMENT = "appconfig";
    private static AppConfigResource instance;
    private AppConfig config;


    private AppConfigResource (){
        readConfig();
    }

    private void readConfig(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(APP_CONFIG_DOCUMENT);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                config = dataSnapshot.getValue(AppConfig.class);
                if(config != null){
                    Log.d(TAG, config.getContactNumberCall());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, databaseError.getMessage());
            }
        });
    }

    public static AppConfigResource getInstance(){
        if(instance == null){
            instance = new AppConfigResource();
        }
        return instance;
    }

    public AppConfig getConfig() {
        return config;
    }
}

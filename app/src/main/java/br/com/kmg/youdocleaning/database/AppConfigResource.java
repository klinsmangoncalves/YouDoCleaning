package br.com.kmg.youdocleaning.database;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import br.com.kmg.youdocleaning.model.ManagerContacts;

public class AppConfigResource {

    private final String TAG = "AppConfigResource";
    private static AppConfigResource instance;
    private ManagerContacts contacts;


    private AppConfigResource (){
        readConfig();
    }

    private void readConfig(){

        FirestoreManager.getInstance().getContacts(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        contacts = task.getResult().toObject(ManagerContacts.class);
                    }
                }
            }
        });
    }

    public static AppConfigResource getInstance(){
        if(instance == null){
            instance = new AppConfigResource();
        }
        return instance;
    }

    public ManagerContacts getContacts() {
        return contacts;
    }
}

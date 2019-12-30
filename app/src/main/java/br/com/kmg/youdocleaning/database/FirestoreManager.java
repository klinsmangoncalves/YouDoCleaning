package br.com.kmg.youdocleaning.database;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import br.com.kmg.youdocleaning.model.FireStoreCleaning;

public class FirestoreManager {

    final String TAG = "FirestoreManager";
    final FirebaseFirestore db;
    private static FirestoreManager manager;

    private FirestoreManager() {
        this.db = FirebaseFirestore.getInstance();
    }

    public static FirestoreManager getInstance(){
        if(manager == null){
            manager = new FirestoreManager();
        }
     return manager;
    }

    public void saveCleaning(FireStoreCleaning data){
        db.collection("cleanings")
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }
}

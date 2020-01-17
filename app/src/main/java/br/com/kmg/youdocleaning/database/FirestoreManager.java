package br.com.kmg.youdocleaning.database;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import br.com.kmg.youdocleaning.model.FireStoreCleaning;
import br.com.kmg.youdocleaning.model.Sector;

public class FirestoreManager {

    final String TAG = "FirestoreManager";
    final FirebaseFirestore db;
    private static final String DOCUMENT_CLEANING = "cleanings";
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

    public void getTasks(String sectorId, OnCompleteListener<QuerySnapshot> onCompleteListener){
        Log.d(TAG, "getTasks");
        CollectionReference docRef = db.collection("sectors").document(sectorId).collection("tasks");
        docRef.get().addOnCompleteListener(onCompleteListener);

    }

    public void getSector(String sectorId, OnCompleteListener<DocumentSnapshot> listener){
        //sectorId = "Tg8eEpFCpurFCPSkvrdP";
        DocumentReference docRef = db.collection("sectors").document(sectorId);
        docRef.get().addOnCompleteListener(listener);
    }

    public void saveCleaning(FireStoreCleaning data){
        db.collection(DOCUMENT_CLEANING)
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

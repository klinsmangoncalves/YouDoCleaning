package br.com.kmg.youdocleaning.database;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import br.com.kmg.youdocleaning.model.Cleaning;

public class FirebaseManager {

    private final String TAG = "FireBaseManager";
    private final String CLEANING_DOCUMENT = "cleaning";
    private final String CURRENT_CLEANING = "currentCleaning";

    final FirebaseDatabase database;
    private static FirebaseManager instance;

    private OnReadFirebaseCurrentCleaning mCurrentCleaningListener;

    public interface OnReadFirebaseCurrentCleaning {
        void onReadCurrentCleaning(Cleaning cleaning);
    }

    public static FirebaseManager getInstance(){
        if(instance == null){
            instance = new FirebaseManager();
        }
        return instance;
    }

    private FirebaseManager() {
        database = FirebaseDatabase.getInstance();
    }

    public String saveCleaning(Cleaning cleaning){
        DatabaseReference cleaningRef = database.getReference(CLEANING_DOCUMENT);
        String cleanIdKey = cleaningRef.push().getKey();
        cleaningRef.child(cleanIdKey).setValue(cleaning);
        Log.d(TAG, "saveCleaning - getReference: " + cleanIdKey);
        return cleanIdKey;
    }

    public void saveCurrentCleaning(Cleaning cleaning){
        Log.d(TAG, "setCurrentCleaning()");
        DatabaseReference cleaningRef = database.getReference(CURRENT_CLEANING);
        cleaningRef.setValue(cleaning);
    }

    public void deleteCurrentCleaning(){
        Log.d(TAG, "deleteCurrentCleaning()");
        DatabaseReference cleaningRef = database.getReference(CURRENT_CLEANING);
        cleaningRef.removeValue();
    }

    public void getCurrentCleaning(){
        Log.d(TAG, "getCurrentCleaning()");
        DatabaseReference cleaningRef = database.getReference(CURRENT_CLEANING);
        cleaningRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Cleaning cleaning = null;
                if(dataSnapshot != null){
                    cleaning = dataSnapshot.getValue(Cleaning.class);
                }

                if(mCurrentCleaningListener != null){
                    mCurrentCleaningListener.onReadCurrentCleaning(cleaning);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    public void saveCleaning(Cleaning cleaning, String key){
        DatabaseReference cleaningRef = database.getReference(CLEANING_DOCUMENT);
        cleaningRef.child(key).setValue(cleaning);
    }

    public void getCleaningById(String id){
        DatabaseReference cleaningRef = database.getReference(CLEANING_DOCUMENT);

        Log.d(TAG, "getCleaningById - ID: " + id);
        cleaningRef.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot != null){
                    Cleaning cleaning = dataSnapshot.getValue(Cleaning.class);
                } else {
                    Log.d(TAG, "dataSnapshot - VALOR NULO" );
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    public OnReadFirebaseCurrentCleaning getmCurrentCleaningListener() {
        return mCurrentCleaningListener;
    }

    public void setmCurrentCleaningListener(OnReadFirebaseCurrentCleaning mCurrentCleaningListener) {
        this.mCurrentCleaningListener = mCurrentCleaningListener;
    }
}

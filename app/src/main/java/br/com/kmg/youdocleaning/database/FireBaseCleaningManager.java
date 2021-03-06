package br.com.kmg.youdocleaning.database;

import android.util.Log;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import br.com.kmg.youdocleaning.adapter.CleaningListAdapter;
import br.com.kmg.youdocleaning.listener.OnReadFirebaseCurrentCleaning;
import br.com.kmg.youdocleaning.model.Cleaning;
import br.com.kmg.youdocleaning.model.FireStoreCleaning;

public class FireBaseCleaningManager {

    private final String TAG = "FireBaseCleaningManager";
    private final String CLEANING_DOCUMENT = "cleaning";
    private final String CURRENT_CLEANING = "currentCleaning";
    final FirebaseDatabase database;
    private static FireBaseCleaningManager instance;

    private OnReadFirebaseCurrentCleaning mCurrentCleaningListener;

    public static FireBaseCleaningManager getInstance(){
        if(instance == null){
            instance = new FireBaseCleaningManager();
        }
        return instance;
    }

    private FireBaseCleaningManager() {
        database = FirebaseDatabase.getInstance();
    }

    public String saveCleaning(Cleaning cleaning){
        DatabaseReference cleaningRef = database.getReference(CLEANING_DOCUMENT);
        String cleanIdKey = cleaningRef.push().getKey();
        cleaningRef.child(cleanIdKey).setValue(cleaning);
        return cleanIdKey;
    }

    public void saveCurrentCleaning(Cleaning cleaning){
        DatabaseReference cleaningRef = database.getReference(CURRENT_CLEANING);
        cleaningRef.setValue(cleaning);
    }

    public void deleteCurrentCleaning(){
        DatabaseReference cleaningRef = database.getReference(CURRENT_CLEANING);
        cleaningRef.removeValue();
    }

    public void finishCleaning(){
        DatabaseReference cleaningRef = database.getReference(CURRENT_CLEANING);
        cleaningRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Cleaning cleaning = null;
                if(dataSnapshot != null){
                    cleaning = dataSnapshot.getValue(Cleaning.class);
                    if(cleaning != null){
                        FirestoreManager.getInstance().saveCleaning(new FireStoreCleaning(cleaning));
                        deleteCurrentCleaning();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }

    public void getCurrentCleaning(){
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
        cleaningRef.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot != null){
                    Cleaning cleaning = dataSnapshot.getValue(Cleaning.class);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    public CleaningListAdapter getFirebaseCleaningAdapter(){

        DatabaseReference mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference messagesRef = mFirebaseDatabaseReference.child("cleaning");

        SnapshotParser<Cleaning> parser = new SnapshotParser<Cleaning>() {
            @Override
            public Cleaning parseSnapshot(DataSnapshot dataSnapshot) {
                Cleaning friendlyMessage = dataSnapshot.getValue(Cleaning.class);
                return friendlyMessage;
            }
        };

        FirebaseRecyclerOptions<Cleaning> options = new FirebaseRecyclerOptions.Builder<Cleaning>()
                .setQuery(messagesRef, parser).build();
        return new CleaningListAdapter(options);
    }

    public OnReadFirebaseCurrentCleaning getmCurrentCleaningListener() {
        return mCurrentCleaningListener;
    }

    public void setmCurrentCleaningListener(OnReadFirebaseCurrentCleaning mCurrentCleaningListener) {
        this.mCurrentCleaningListener = mCurrentCleaningListener;
    }

}

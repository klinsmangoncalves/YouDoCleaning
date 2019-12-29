package br.com.kmg.youdocleaning.database;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.com.kmg.youdocleaning.model.Cleaning;

public class FirebaseManager {

    private final String CLEANING_DOCUMENT = "cleaning";

    final FirebaseDatabase database;

    private static FirebaseManager instance;

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
        String cleaningId = cleaningRef.getKey();
        if(cleaningId != null) {
            database.getReference().child(cleaningId).push().setValue(cleaning);
        }
        return cleaningId;
    }
}

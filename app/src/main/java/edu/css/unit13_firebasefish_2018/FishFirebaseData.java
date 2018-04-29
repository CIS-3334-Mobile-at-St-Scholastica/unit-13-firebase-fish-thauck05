package edu.css.unit13_firebasefish_2018;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tgibbons on 4/24/2018.
 */

public class FishFirebaseData {
    DatabaseReference myFishDbRef;
    FirebaseAuth firebaseAuth;
    public static final String FishDataTag = "Fish Data";
    private String userId; //Firebase Authentication ID for logged in user


    public DatabaseReference open(AppCompatActivity mainActivity)  {
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myFishDbRef = database.getReference(FishDataTag);
        // set user id for user
        userId = getUserId(mainActivity);
        return myFishDbRef;
    }

    public void close() {
    }

    // get users ID from Firebase
    private String getUserId(AppCompatActivity activity) {
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user == null) {
            // user signed out
            Log.d("CIS3334","onAuthStateChanged - User is not signed in");
            Intent signInIntent = new Intent(activity, LoginActivity.class);
            activity.startActivity(signInIntent);
        }
        return user.getUid();
    }

    public Fish createFish(String species, String weightInOz, String dateCaught) {           //Added String rating as a parameter
        // ---- Get a new database key for the vote
        String key = myFishDbRef.child(FishDataTag).push().getKey();
        // String key = "REPLACE THIS WITH KEY FROM DATABASE";
        // ---- set up the fish object
        Fish newFish = new Fish(key, species, weightInOz, dateCaught);
        // ---- write the vote to Firebase
        myFishDbRef.child("users").child(userId).child(key).setValue(newFish);
        return newFish;
    }

    public Fish createFish( String species, String weightInOz, String dateCaught, String locationLatitude, String locationLongitude) {           //Added String rating as a parameter
        // ---- Get a new database key for the vote
        String key = myFishDbRef.child(FishDataTag).push().getKey();
        //String key = "REPLACE THIS WITH KEY FROM DATABASE";
        // ---- set up the fish object
        Fish newFish = new Fish(key, species, weightInOz, dateCaught, locationLatitude,locationLongitude);
        // ---- write the vote to Firebase
        myFishDbRef.child("users").child(userId).child(key).setValue(newFish);
        return newFish;
    }

    public void deleteFish(Fish fish) {
        String key = fish.getKey();
        myFishDbRef.child("users").child(userId).child(key).removeValue();
    }

    public List<Fish> getAllFish(DataSnapshot dataSnapshot) {
        List<Fish> fishList = new ArrayList<Fish>();
        for (DataSnapshot data : dataSnapshot.child("users").child(userId).getChildren()) {
            Log.d("CIS3334","=== getAllFish ===" + data.toString());
            Fish fish = data.getValue(Fish.class);
            fishList.add(fish);
        }
        return fishList;
    }


}


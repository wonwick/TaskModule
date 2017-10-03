package com.won.taskmodule;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Oshan Wickramaratne on 2017-09-06.
 */

public class taskHandler {

    void getAvailableTasks() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("OverviewAvailableTask");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String tasks = dataSnapshot.getValue(String.class);
                Log.d("the shit", "Value is: " + tasks);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("the shit", "Failed to read value.", error.toException());
            }
        });

    }

}

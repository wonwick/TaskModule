package com.won.taskmodule;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class LiveDetailsActivity extends AppCompatActivity {
    private static final int TABLE_ROW_MARGIN = 7;
    TableLayout t1;
    FirebaseDatabase secondDatabase;
    String vin;
    FirebaseApp secondApp=null;
    DatabaseReference acceptedTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_details);
        t1=(TableLayout) findViewById(R.id.data_table) ;
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setApiKey("AIzaSyBOI2Z9nPPvEdr2ksXXvsDpSzJNfj5_2Jo")
                .setApplicationId("1:900422074195:android:0b4422affc419165")
                .setDatabaseUrl("https://android-obd-reader-master.firebaseio.com")
                .build();
        Log.d("noOFapps:",""+FirebaseApp.getApps(this).size());
        if (FirebaseApp.getApps(this).size()<2) {
            secondApp = FirebaseApp.initializeApp(getApplicationContext(), options, "second app");
        }


        secondDatabase = FirebaseDatabase.getInstance(FirebaseApp.getApps(this).get(1));
        Intent intent = getIntent();
        vin = intent.getStringExtra("vehicleNo");
        Log.d("vinGot:",vin);
        acceptedTask = secondDatabase.getReference("vehicleDetails").child(vin);
    }

    @Override
    protected void onStart(){
        super.onStart();

        acceptedTask.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap <String,String> OBDReadings= (HashMap<String, String>) dataSnapshot.getValue();
                int i=0;
                t1.removeAllViews();
                if (OBDReadings==null){
                    addTableRow("id" + i, "No Values Available", " At this moment ");
                }

                else {
                    for (Map.Entry<String, String> entry : OBDReadings.entrySet()) {
                        addTableRow("id" + i, entry.getKey(), entry.getValue());
                        i++;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    private void addTableRow(String id, String key, String val) {

        TableRow tr = new TableRow(this);
        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(TABLE_ROW_MARGIN, TABLE_ROW_MARGIN, TABLE_ROW_MARGIN,
                TABLE_ROW_MARGIN);
        tr.setLayoutParams(params);

        TextView name = new TextView(this);
        name.setGravity(Gravity.RIGHT);
        name.setText(key + ": ");
        TextView value = new TextView(this);
        value.setGravity(Gravity.LEFT);
        value.setText(val);
        value.setTag(id);
        tr.addView(name);
        tr.addView(value);
        t1.addView(tr, params);
    }


}

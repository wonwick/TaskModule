package com.won.taskmodule;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Testing extends AppCompatActivity {
    SharedPreferences sharedPreferences ;
    String userName;
    int tripNo;
    public String s;
    Button stopBtn;
    Button chk;
//    Testing.SendLocation sendLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userName = sharedPreferences.getString("appUser", "");
        tripNo=129;
//        sendLocation = new Testing.SendLocation();

        chk = (Button) findViewById(R.id.stat_btn);
        stopBtn = (Button) findViewById(R.id.stop_btn);

        chk.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        StartService();
                    }
                }
        );

        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StopService();
            }
        });


        //Log.d("jobX", "selected job: " + s + " curLat" + LocationService.lat + " curLon" + LocationService.lon);
    }

//    public class newMessage extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            Bundle extra = intent.getExtras();
//            Double lati = extra.getDouble("lat");
//            Double longi = extra.getDouble("lon");
//            Log.d("GPSloc", "Got from Intent: " + lati + " " + longi);
////            DatabaseReference chat=FirebaseDatabase.getInstance().getReference("loc").push();
////            chat.child("lon").setValue(longi);
////            chat.child("lat").setValue(lati);
////            java.util.Date dt = new java.util.Date();
////            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
////            String currentTime = sdf.format(dt);
////            sendLocation.setUrl("http://35.188.127.20/mobileApp/addLocation.php");
////            String[] keyWords = {"userName","tripNo","lon","lat","timeStamp"};
////            String[] values = {userName,Integer.toString(tripNo),longi.toString(),lati.toString() ,currentTime};
////            sendLocation.setKeywords(keyWords);
////            sendLocation.setValues(values);
////            sendLocation.execute();
////            Toast.makeText(getApplicationContext(),"result: "+ userName+" "+Integer.toString(tripNo)+" "+longi.toString()+" "+lati.toString() +" "+currentTime, Toast.LENGTH_SHORT).show();
//
//        }
//    }

    public void StartService() {
        startService(new Intent(this, LocationService.class));
       // newMessage messageReceiver = new newMessage();
       // registerReceiver(messageReceiver, new IntentFilter("loc"));

    }

    public void StopService() {
        stopService(new Intent(this, LocationService.class));
        // Log.d("GPSloc","lat: "+LocationService.lat+" long: "+LocationService.lon);
    }
//    class SendLocation extends SendPostRequest {
//
//        @Override
//        protected void onPostExecute(String result) {
//            Log.d("empDetails",result);
//            Toast.makeText(getApplicationContext(),"result: "+ result, Toast.LENGTH_SHORT).show();
//
//
//        }
//    }
}


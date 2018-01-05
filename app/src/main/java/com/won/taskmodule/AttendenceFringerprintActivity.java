package com.won.taskmodule;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import com.multidots.fingerprintauth.FingerPrintAuthCallback;
import com.multidots.fingerprintauth.FingerPrintAuthHelper;

import org.json.JSONException;
import org.json.JSONObject;

public class AttendenceFringerprintActivity extends Activity implements FingerPrintAuthCallback {
    //implement FingerPrintAuthCallback predefined function set on activity
    FingerPrintAuthHelper mAttendanceFingerprint; //defining fingerprint anchor
    public int attempt_count= 5; //defining attempt counter for fingerprint
    public static Timer timer = new Timer(); // casting timer objects
    SharedPreferences sharedPreferences;
    String userName;
    public static int seconds_left = 30; // defining time for delay
    int attendance;
    SharedPreferences.Editor editor;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //disable default title of the activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendence_fringerprint);
        //casting fingerprint object
        mAttendanceFingerprint= FingerPrintAuthHelper.getHelper(this, this);
        //casting sharedpreference objects
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userName=sharedPreferences.getString("appUser","");
        editor = sharedPreferences.edit();
        mContext=getApplicationContext();
        //casting fingerprint object
    }

    @Override
    protected void onResume() {
        super.onResume();
        //start finger print authentication on resume
        mAttendanceFingerprint.startAuth();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //stop fingerprint authentication on pause
        mAttendanceFingerprint.stopAuth();
    }
    @Override
    public void onNoFingerPrintHardwareFound() {
        //if no fingerprint sensor found on the device toasts this
        Toast.makeText(this,"no fingerprint sensor",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNoFingerPrintRegistered() {
        //if no fingerprint is registered toasts this message
        Toast.makeText(this,"enroll at least one fingerprint",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBelowMarshmallow() {
        //Device running below API 23 version of android that does not support finger print authentication.
    }

    @Override
    public void onAuthSuccess(FingerprintManager.CryptoObject cryptoObject) {
        //if authentication verified toast this message
        Toast.makeText(this,"authentication verified!",Toast.LENGTH_SHORT).show();
        //set attendance and leave on database using
        attendance= sharedPreferences.getInt("attendance", 0);
        AttendenceFringerprintActivity.AttendanceDetailsPostRequest sendAttendance=new AttendenceFringerprintActivity.AttendanceDetailsPostRequest();
        String [] keywords={"userName","attend"};
        sendAttendance.setKeywords(keywords);
        String [] values={userName,""+attendance};
        sendAttendance.setValues(values);
        sendAttendance.setUrl("http://35.188.127.20/mobileApp/addAttendance.php");
        sendAttendance.execute();
        //Intent nextActivity = new Intent("com.example.sasankasandes.sparktec.OBDpairActivity");
        //startActivity(nextActivity);

    }

    @Override

    public void onAuthFailed(int errorCode, String errorMessage) {
        //attempt counter to restrict abusing fingerprint sensor or unauthorised attempts;
        attempt_count-=1;
        if (attempt_count>0){
            //ImageView fingerprintImage = (ImageView) findViewById(R.id.imageView3);
            //Animation shake = AnimationUtils.loadAnimation(AttendanceFingerprintActivity.this, R.anim.wobble);
            Toast.makeText(this, "Unable to recognize fingerprint You have " + attempt_count + "more attempts", Toast.LENGTH_SHORT).show();
            //shake.reset();
            //shake.setFillAfter(true);
            //fingerprintImage.startAnimation(shake);
        }
        else {
            Toast.makeText(this, "Fingerprint has suspended for " + seconds_left + "seconds", Toast.LENGTH_SHORT).show();
            MyTimer();
        }
    }

    /*switch (errorCode) {    //Parse the error code for recoverable/non recoverable error.
        case AuthErrorCodes.CANNOT_RECOGNIZE_ERROR:



            break;

        case AuthErrorCodes.NON_RECOVERABLE_ERROR:
            shake.reset();
            shake.setFillAfter(true);
            fingerprintImage.startAnimation(shake);
            break;

        case AuthErrorCodes.RECOVERABLE_ERROR:

            shake.reset();
            shake.setFillAfter(true);
            fingerprintImage.startAnimation(shake);
            break;
    }*/
    public void MyTimer(){
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (seconds_left>0){
                    Toast.makeText(AttendenceFringerprintActivity.this, "Fingerprint has suspended for " + seconds_left + "seconds", Toast.LENGTH_SHORT).show();
                    seconds_left--;
                }
                else{
                    cancel();
                    attempt_count= 5;
                    seconds_left=30;
                    mAttendanceFingerprint.startAuth();

                }
            }
        };
        timer.scheduleAtFixedRate(task,0,1000);
    }
    public void StartService() {
        startService(new Intent(this, LocationService.class));
        // newMessage messageReceiver = new newMessage();
        // registerReceiver(messageReceiver, new IntentFilter("loc"));

    }

    public void StopService() {
        stopService(new Intent(this, LocationService.class));
        // Log.d("GPSloc","lat: "+LocationService.lat+" long: "+LocationService.lon);
    }


    class AttendanceDetailsPostRequest extends SendPostRequest {

        @Override
        protected void onPostExecute(String result) {
            Log.d("MyVehiclesDetails",result);
             attendance= sharedPreferences.getInt("attendance", 0);
             attendance=(attendance+1)%2;
             editor.putInt("attendance",attendance);
             editor.commit();

            Log.d("sasa","intent succes");
            Intent intent = new Intent(AttendenceFringerprintActivity.this, EmployeeActivity.class);
            startActivity(intent);





        }
    }
}

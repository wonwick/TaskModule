package com.won.taskmodule;

import android.app.Activity;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import com.multidots.fingerprintauth.FingerPrintAuthCallback;
import com.multidots.fingerprintauth.FingerPrintAuthHelper;

public class AttendenceFringerprintActivity extends Activity implements FingerPrintAuthCallback {
    //implement FingerPrintAuthCallback predefined function set on activity
    FingerPrintAuthHelper mAttendanceFingerprint; //defining fingerprint anchor
    public int attempt_count= 5; //defining attempt counter for fingerprint
    public static Timer timer = new Timer(); // casting timer objects
    public static int seconds_left = 30; // defining time for delay
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //disable default title of the activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendence_fringerprint);
        mAttendanceFingerprint= FingerPrintAuthHelper.getHelper(this, this);
        //casting fingerprint object
    }

    @Override
    protected void onResume() {
        super.onResume();
        //start finger print authentication
        mAttendanceFingerprint.startAuth();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //mAttendanceFingerprint.stopAuth();
    }
    @Override
    public void onNoFingerPrintHardwareFound() {
        Toast.makeText(this,"no fingerprint sensor",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNoFingerPrintRegistered() {
        Toast.makeText(this,"enroll at least one fingerprint",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBelowMarshmallow() {
        //Device running below API 23 version of android that does not support finger print authentication.
    }

    @Override
    public void onAuthSuccess(FingerprintManager.CryptoObject cryptoObject) {
        Toast.makeText(this,"authentication verified!",Toast.LENGTH_SHORT).show();
        //Intent nextActivity = new Intent("com.example.sasankasandes.sparktec.OBDpairActivity");
        //startActivity(nextActivity);

    }

    @Override

    public void onAuthFailed(int errorCode, String errorMessage) {

        attempt_count-=1;
        if (attempt_count>0){
            ImageView fingerprintImage = (ImageView) findViewById(R.id.imageView3);
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
}

package com.won.taskmodule;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Boolean isFirstTime;
        int type;
        int secondsDelayed=1;

        SharedPreferences app_preferences = PreferenceManager
                .getDefaultSharedPreferences(SplashActivity.this);

        SharedPreferences.Editor editor = app_preferences.edit();

        isFirstTime = app_preferences.getBoolean("isFirstTime", true);

        if (isFirstTime) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            editor.putBoolean("isFirstTime", false);
            editor.commit();

        } else {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            int userType = sharedPreferences.getInt("appUserType", 0);
            switch (userType) {
                case 1:
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            Intent goToEmployeeBranch = new Intent(SplashActivity.this, EmployeeActivity.class);
                            startActivity(goToEmployeeBranch);
                            finish();
                        }
                    }, secondsDelayed * 1000);

                    break;

                case 2:
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            Intent goToVehicleOwnerBranch = new Intent(SplashActivity.this, VehicleOwnerActivity.class);
                            startActivity(goToVehicleOwnerBranch);
                            finish();
                        }
                    }, secondsDelayed * 1000);

                    break;

                case 0:
                    Log.d("SplashLog", "no privious login recorded");
                    Toast.makeText(SplashActivity.this, "no privious login recorded", Toast.LENGTH_LONG).show();
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }, secondsDelayed * 1000);

                    break;
//app open directly
            }

        }
    }
}
package com.won.taskmodule;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    EditText editTextUserName;
    EditText editTextPassword;
    String userName;
    String passWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editTextUserName = (EditText) findViewById(R.id.editTextUserName);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);



    }

    @Override
    protected void onStart() {
        super.onStart();
        FragmentManager fragmentManager = getSupportFragmentManager();
        NetworkStatusDialogFragment networkStatusDialogFragment = new NetworkStatusDialogFragment();
        if (!isNetworkConnected()){
            networkStatusDialogFragment.show(fragmentManager,"");
        }
    }

    void authenticate() {
        LoginRequest loginRequest = new LoginRequest();
        // Add your data
        userName = editTextUserName.getText().toString();
        passWord = editTextPassword.getText().toString();


        loginRequest.setUrl("http://35.188.127.20/mobileApp/mobileLogin.php");
        String[] keyWords = {"userName", "password"};
        String[] values = {userName, passWord};
        loginRequest.setKeywords(keyWords);
        loginRequest.setValues(values);


        // Execute HTTP Post Request
        loginRequest.execute();
    }





    void login(View view) {
        Log.d("button clicked","button clicked logging");
        authenticate();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);


    }

    class LoginRequest extends SendPostRequest{

        @Override
        protected void onPostExecute(String result) {
            Log.d("userType","userType :"+result);
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = sharedPreferences.edit();
            try{
                int userType = Integer.parseInt(result);

                switch (userType) {
                    case 1:

                        editor.putInt("appUserType", 1);
                        editor.putString("appUser",userName);
                        editor.commit();
                        Toast.makeText(LoginActivity.this, "Login recorded as a employee ", Toast.LENGTH_LONG).show();
                        Intent goToEmployeeBranch = new Intent(LoginActivity.this, EmployeeActivity.class);
                        startActivity(goToEmployeeBranch);
                        break;

                    case 2:
                        editor.putInt("appUserType", 2);
                        editor.putString("appUser",userName);
                        editor.commit();
                        Toast.makeText(LoginActivity.this, "login recorded as a vehicleOwner ", Toast.LENGTH_LONG).show();
                        Intent goToVehicleOwnerBranch = new Intent(LoginActivity.this, VehicleOwnerActivity.class);
                        startActivity(goToVehicleOwnerBranch);
                        break;
                    case -1:
                        Log.d("userType","empty post error");
                        Toast.makeText(LoginActivity.this, "emptyPost error ", Toast.LENGTH_LONG).show();
                    case 0:
                        Log.d("userType","unidentified username and password");
                        Toast.makeText(LoginActivity.this, "unidentified username and password ", Toast.LENGTH_LONG).show();
                        break;


                }
            }
            catch (Exception e){
                Log.d("userType","userType: "+result +" ERROR");
                Toast.makeText(LoginActivity.this, "userType ERROR ", Toast.LENGTH_LONG).show();

            }
        }
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }
}

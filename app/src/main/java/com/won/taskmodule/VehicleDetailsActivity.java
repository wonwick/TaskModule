package com.won.taskmodule;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class VehicleDetailsActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    String userName;

    String vehicleNO ;
    String regNO   ;
    String brand  ;
    String model      ;
    String picture ;
    String firstName ;
    String lastName ;

    TextView textViewVehicleNO ;
    TextView textViewRegNO;
    TextView textViewBrand  ;
    TextView textViewModel   ;
    TextView textViewPicture ;
    TextView textViewFirstName ;
    TextView textViewLastName ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_details);

        VehicleDetailsActivity.EmpDetailsRequest empDetailsRequest = new VehicleDetailsActivity.EmpDetailsRequest();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userName = sharedPreferences.getString("appUser", "");

        textViewVehicleNO=(TextView) findViewById(R.id.textViewVehicleNO);
        textViewFirstName=(TextView) findViewById(R.id.textViewFirstName);
        textViewLastName=(TextView) findViewById(R.id.textViewLastName);
        textViewRegNO=(TextView) findViewById(R.id.textViewRegNO);
        textViewBrand=(TextView) findViewById(R.id.textViewBrand);
        textViewModel=(TextView) findViewById(R.id.textViewModel);
        textViewPicture=(TextView) findViewById(R.id.textViewPicture);


        empDetailsRequest.setUrl("http://35.188.127.20/mobileApp/getVehicleDetails.php");
        String[] keyWords = {"userName"};
        String[] values = {userName};
        empDetailsRequest.setKeywords(keyWords);
        empDetailsRequest.setValues(values);
        empDetailsRequest.execute();
    }

    class EmpDetailsRequest extends SendPostRequest {

        @Override
        protected void onPostExecute(String result) {
            Log.d("empDetails",result);
            try {
                JSONObject empDetails=new JSONObject(result);
                vehicleNO=empDetails.getString("vehicleNO");
                regNO=empDetails.getString("regNO");
                brand=empDetails.getString("brand");
                model=empDetails.getString("model");
                picture=empDetails.getString("picture");
                firstName=empDetails.getString("firstName");
                lastName=empDetails.getString("lastName");


                textViewVehicleNO.setText(vehicleNO);
                textViewFirstName.setText(firstName);
                textViewLastName.setText(lastName);
                textViewRegNO.setText(regNO);
                textViewBrand.setText(brand);
                textViewModel.setText(model);
                textViewPicture.setText(picture);



            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}

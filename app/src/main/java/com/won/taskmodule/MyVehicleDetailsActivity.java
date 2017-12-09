package com.won.taskmodule;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;

public class MyVehicleDetailsActivity extends AppCompatActivity {
    TextView textViewVehicleNo ;
    TextView textViewRegNo ;
    TextView textViewBrand ;
    TextView textViewModel ;
    TextView textViewName ;
    Button buttonLocation;
    Button buttonLiveDetails;


    String vehicleNo;
    String regNo;
    String brand;
    String model;
    String pic;
    String firstName;
    String lastName;
    SharedPreferences sharedPreferences ;
    String userName;
    String empUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_vehicle_details);
        Intent intent = getIntent();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userName = sharedPreferences.getString("appUser", "");

        textViewVehicleNo = (TextView) findViewById(R.id.textViewVehicleNo);
        textViewRegNo = (TextView) findViewById(R.id.textViewRegNo);
        textViewBrand = (TextView) findViewById(R.id.textViewBrand);
        textViewModel = (TextView) findViewById(R.id.textViewModel);
        textViewName = (TextView) findViewById(R.id.textViewFirstName);
        buttonLocation=(Button) findViewById(R.id.buttonLocation);
        buttonLiveDetails=(Button) findViewById(R.id.buttonLiveDetails);
        buttonLocation.setEnabled(false);

        vehicleNo = intent.getStringExtra("vehicleNo");
        regNo = intent.getStringExtra("regNo");
        brand = intent.getStringExtra("brand");
        model = intent.getStringExtra("model");
        pic = intent.getStringExtra("pic");

        textViewVehicleNo.setText(vehicleNo);
        textViewRegNo.setText(regNo);
        textViewBrand.setText(brand);
        textViewModel.setText(model);

        MyVehicleDetailsActivity.MyVehiclesDetailsRequest MyVehicleDetailsRequest = new MyVehicleDetailsActivity.MyVehiclesDetailsRequest();

        MyVehicleDetailsRequest.setUrl("http://35.188.127.20/mobileApp/vehicleOwnerGetInchargePersonDetails.php");
        String[] keyWords = {"vehicleNo"};
        String[] values = {vehicleNo};
        MyVehicleDetailsRequest.setKeywords(keyWords);
        MyVehicleDetailsRequest.setValues(values);
        MyVehicleDetailsRequest.execute();

    }

    void goToLocationActivity(View view) {
        Intent goToAcceptedTasks = new Intent(MyVehicleDetailsActivity.this, MapsActivity.class);
        goToAcceptedTasks.putExtra("inchargeUserName", empUserName);
        goToAcceptedTasks.putExtra("regNo", regNo);
        startActivity(goToAcceptedTasks);

    }

    void goToLiveDetailsActivity(View view) {
        Intent goToAcceptedTasks = new Intent(MyVehicleDetailsActivity.this, LiveDetailsActivity.class);
        goToAcceptedTasks.putExtra("vehicleNo", vehicleNo);
        Log.d("SendingVehicleNo",vehicleNo);
        startActivity(goToAcceptedTasks);

    }

    class MyVehiclesDetailsRequest extends SendPostRequest {

        @Override
        protected void onPostExecute(String result) {
            Log.d("MyVehiclesDetails",result);

            if(result.equals("noAllocation")){
                textViewName.setText("not Allocated");
            }

            else{
                try {
                    JSONObject inchargeDetails=new JSONObject(result);
                    empUserName= inchargeDetails.getString("userName");
                    textViewName.setText("Allocated");
                    buttonLocation.setEnabled(true);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


        }
    }

}

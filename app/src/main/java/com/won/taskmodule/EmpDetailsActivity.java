package com.won.taskmodule;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class EmpDetailsActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    String userName;

    String NIC;
    String firstName;
    String lastName;
    String picture;
    String address;
    String gender;
    String availability;
    String managerID;
    String number;

    TextView textViewNIC;
    TextView textViewFirstName;
    TextView textViewLastName;
    TextView textViewAddress;
    TextView textViewGender;
    TextView textViewAvailability;
    TextView textViewManagerID;
    TextView textViewNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp_details);

        EmpDetailsActivity.EmpDetailsRequest empDetailsRequest = new EmpDetailsActivity.EmpDetailsRequest();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userName = sharedPreferences.getString("appUser", "");

        textViewNIC=(TextView) findViewById(R.id.textViewNIC);
        textViewFirstName=(TextView) findViewById(R.id.textViewFirstName);
        textViewLastName=(TextView) findViewById(R.id.textViewLastName);
        textViewAddress=(TextView) findViewById(R.id.textViewAddress);
        textViewGender=(TextView) findViewById(R.id.textViewGender);
        textViewAvailability=(TextView) findViewById(R.id.textViewAvailability);
        textViewManagerID=(TextView) findViewById(R.id.textViewManagerID);
        textViewNumber=(TextView) findViewById(R.id.textViewNumber);


        empDetailsRequest.setUrl("http://35.188.127.20/mobileApp/getEmpDetails.php");
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
                NIC=empDetails.getString("NIC");
                firstName=empDetails.getString("firstName");
                lastName=empDetails.getString("lastName");
                picture=empDetails.getString("picture");
                address=empDetails.getString("address");
                gender=empDetails.getString("gender");
                availability=empDetails.getString("availability");
                managerID=empDetails.getString("managerID");
                number=empDetails.getString("number");

                textViewNIC.setText(NIC);
                textViewFirstName.setText(firstName);
                textViewLastName.setText(lastName);
                textViewAddress.setText(address);
                textViewGender.setText(gender);
                textViewAvailability.setText(availability);
                textViewManagerID.setText(managerID);
                textViewNumber.setText(number);



            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}

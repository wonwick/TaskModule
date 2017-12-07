package com.won.taskmodule;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
public class MyVehicleActivity extends AppCompatActivity {
    ListView listViewTask;
    List<Vehicle> vehicles;
    SharedPreferences sharedPreferences;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_vehicle);

        listViewTask = (ListView) findViewById(R.id.listViewTasks);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userName = sharedPreferences.getString("appUser", "");
        vehicles = new ArrayList<>();

        MyVehicleActivity.MyVehiclesDetailsRequest MyVehicleDetailsRequest = new MyVehicleActivity.MyVehiclesDetailsRequest();

        MyVehicleDetailsRequest.setUrl("http://35.188.127.20/mobileApp/vehicleOwnerGetVehicleDetails.php");
        String[] keyWords = {"userName"};
        String[] values = {userName};
        MyVehicleDetailsRequest.setKeywords(keyWords);
        MyVehicleDetailsRequest.setValues(values);

        MyVehicleDetailsRequest.execute();



    }



    public void onBackPressed() {
        startActivity(new Intent(com.won.taskmodule.MyVehicleActivity.this, VehicleOwnerActivity.class));
        finish();

    }




    class MyVehiclesDetailsRequest extends SendPostRequest {

        @Override
        protected void onPostExecute(String result) {
            Log.d("MyVehicles",result);
            try {
                JSONArray vehicleDetails=new JSONArray (result);

                for (int i = 0; i < vehicleDetails.length(); ++i) {
                    JSONObject vehicle = vehicleDetails.getJSONObject(i);
                    int vehicleNo = vehicle.getInt("vehicleNo");
                    String regNo = vehicle.getString("regNo");
                    String brand = vehicle.getString("brand");
                    String model = vehicle.getString("model");
                    String pic = vehicle.getString("pic");
                    vehicles.add( new Vehicle(vehicleNo,regNo,brand,model,pic));

                }


                VehicleList taskAdapter = new VehicleList(com.won.taskmodule.MyVehicleActivity.this, vehicles);

                listViewTask.setAdapter(taskAdapter);


                listViewTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        //getting the selected
                        Vehicle vehicle = vehicles.get(i);

                        //creating an intent
                        Intent intent = new Intent(getApplicationContext(), MyVehicleDetailsActivity.class);
                        intent.putExtra("vehicleNo", ""+vehicle.getVehicleNo());
                        intent.putExtra("regNo", vehicle.getRegNo());
                        intent.putExtra("brand", vehicle.getBrand());
                        intent.putExtra("model", vehicle.getModel());
                        intent.putExtra("pic",vehicle.getPicture());

                        startActivity(intent);




                    }
                });



            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}

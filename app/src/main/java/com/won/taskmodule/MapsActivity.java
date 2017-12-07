package com.won.taskmodule;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String regNo;
    String empUserName;
    double lat;
    double lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Intent intent = getIntent();
        regNo = intent.getStringExtra("regNo");
        empUserName=intent.getStringExtra("inchargeUserName");

        MapsActivity.LocationRequest locationPostRequest = new MapsActivity.LocationRequest();


        locationPostRequest.setUrl("http://35.188.127.20/mobileApp/vehicleOwnerGetVehicleLocation.php");
        String[] keyWords = {"empUserName"};
        String[] values = {empUserName};
        locationPostRequest.setKeywords(keyWords);
        locationPostRequest.setValues(values);
        locationPostRequest.execute();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title(sydney));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    class LocationRequest extends SendPostRequest {

        @Override
        protected void onPostExecute(String result) {
            Log.d("MyVehiclesDetails",result);

            try {
                JSONObject jLocation=new JSONObject(result);
                String timeStamp=jLocation.getString("timeStamp");
                lon=Double.parseDouble(jLocation.getString("lon"));
                lat=Double.parseDouble(jLocation.getString("lat"));

                LatLng LocationLastknown = new LatLng(lat, lon);
                float zoomLevel = 12.0f;
                mMap.addMarker(new MarkerOptions().position(LocationLastknown).title(regNo+" at "+timeStamp));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LocationLastknown,zoomLevel));


            } catch (JSONException e) {
                e.printStackTrace();
            }




        }
    }
}

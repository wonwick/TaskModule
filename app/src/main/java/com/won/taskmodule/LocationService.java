package com.won.taskmodule;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by Nimesha on 8/26/2017.
 */
public class LocationService extends Service implements LocationListener {
    SharedPreferences sharedPreferences ;
    String userName;
    int tripNo;
    LocationManager m_locationManager;
    LocationService.SendLocation sendLocation;

    @Override
    public void onCreate() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userName = sharedPreferences.getString("appUser", "");
        tripNo=129;
        this.m_locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        sendLocation = new LocationService.SendLocation();
        Toast.makeText(getApplicationContext(), "Location Service starts", Toast.LENGTH_SHORT).show();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //TODO do something useful
        Toast.makeText(getApplicationContext(), "Service starts", Toast.LENGTH_SHORT).show();


        //  Here I offer two options: either you are using satellites or the Wi-Fi services to get user's location
        //  this.m_locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 0, this); //  User's location is retrieve every 3 seconds
        this.m_locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

        }
        this.m_locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, this);
        return START_STICKY;
    }


    @Override
    public IBinder onBind(Intent intent) {
        //TODO for communication return IBinder implementation
        return null;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onDestroy() {
        super.onDestroy();
        this.m_locationManager.removeUpdates(this);
        Toast.makeText(getApplicationContext(), "Service Task destroyed", Toast.LENGTH_LONG).show();
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        this.m_locationManager.removeUpdates(this);
    }


    @Override
    public void onLocationChanged(Location loc) {
        if (loc == null)    //  Filtering out null values
            return;

        Double lat = loc.getLatitude();
        Double lon = loc.getLongitude();
        java.util.Date dt = new java.util.Date();

        sendLocation = new LocationService.SendLocation();
        if(!(sendLocation.getStatus()== AsyncTask.Status.RUNNING)){
            sendLocation = new LocationService.SendLocation();
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            String currentTime = sdf.format(dt);
            sendLocation.setUrl("http://35.188.127.20/mobileApp/addLocation.php");
            String[] keyWords = {"userName","tripNo","lon","lat","timeStamp"};
            String[] values = {userName,Integer.toString(tripNo),lon.toString(),lat.toString() ,currentTime};
            sendLocation.setKeywords(keyWords);
            sendLocation.setValues(values);
            sendLocation.execute();
        }

        Log.d("GPSloc", "" + lat + " " + lon);
        Toast.makeText(getApplicationContext(), "Latitude = " + lat + "\nLongitude = " + lon, Toast.LENGTH_SHORT).show();

        Intent i = new Intent("loc");
        Bundle bundle = new Bundle();
        bundle.putDouble("lat", lat);
        bundle.putDouble("lon", lon);
        i.putExtras(bundle);
        sendBroadcast(i);


        //   new UpdateLatitudeLongitude(LocationService.this, lat, lon,).execute();

//Calling AsyncTask for upload latitude and longitude
        /*java.util.Date dt = new java.util.Date();

        java.text.SimpleDateFormat sdf =
                new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String currentTime = sdf.format(dt);
        sendLocation.setUrl("http://35.188.127.20/mobileApp/addLocation.php");
        String[] keyWords = {"userName","tripNo","lon","lat","timeStamp"};
        String[] values = {userName,Integer.toString(tripNo),lon.toString(),lat.toString() ,currentTime};
        sendLocation.setKeywords(keyWords);
        sendLocation.setValues(values);
        sendLocation.execute();*/


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
    class SendLocation extends SendPostRequest {

        @Override
        protected void onPostExecute(String result) {
            Log.d("empDetails",result);

        }
    }

}

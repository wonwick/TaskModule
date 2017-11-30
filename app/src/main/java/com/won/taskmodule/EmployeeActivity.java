package com.won.taskmodule;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class EmployeeActivity extends AppCompatActivity {
    private Boolean exit = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.Logout) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("appUserType", 0);
            editor.commit();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    void goToAvailableActivity(View view) {
        Intent intent = new Intent(EmployeeActivity.this, AvailableTasksListActivity.class);
        startActivity(intent);

    }

    void goToAcceptedTaskList(View view) {
        Intent intent = new Intent(EmployeeActivity.this, AcceptedTasksListActivity.class);
        startActivity(intent);

    }

    void goToEmpDetailsActivity(View view) {
        Intent intent = new Intent(EmployeeActivity.this, EmpDetailsActivity.class);
        startActivity(intent);

    }

    void goToEmpVehicleDetailsActivity(View view) {
        Intent intent = new Intent(EmployeeActivity.this, VehicleDetailsActivity.class);
        startActivity(intent);

    }




    @Override
    public void onBackPressed() {
        moveTaskToBack(true);



    }
    void psudoAction(View view) {
        //do nothing

    }
}

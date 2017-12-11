package com.won.taskmodule;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;

public class TaskActivity extends AppCompatActivity {
    DatabaseReference AvailableTask;
    DatabaseReference unavailableTask;
    DatabaseReference unavailableTaskOverview;
    TextView textViewContactName ;
    TextView textViewContactNumber ;
    TextView textViewDeadline ;
    TextView textViewDescription ;
    TextView textViewTaskName ;
    TextView textViewTaskID ;
    TextView textViewTaskArea ;

    String taskId;
    String taskName;
    String taskArea;
    String contactName;
    String contactNumber;
    String deadline;
    String description;
    SharedPreferences sharedPreferences ;
    String userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        Intent intent = getIntent();
        textViewTaskName = (TextView) findViewById(R.id.textViewTaskName);
        textViewTaskID = (TextView) findViewById(R.id.textViewTaskID);
        textViewTaskArea = (TextView) findViewById(R.id.textViewTaskArea);

        textViewContactName = (TextView) findViewById(R.id.textViewContactName);
        textViewContactNumber = (TextView) findViewById(R.id.textViewContactNumber);
        textViewDeadline = (TextView) findViewById(R.id.textViewDeadline);
        textViewDescription = (TextView) findViewById(R.id.textViewDescription);

        taskId = intent.getStringExtra("TASK_ID");
        taskName = intent.getStringExtra("TASK_NAME");
        taskArea = intent.getStringExtra("TASK_Area");


        textViewTaskID.setText(taskId);
        textViewTaskName.setText(taskName);
        textViewTaskArea.setText(taskArea);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userName = sharedPreferences.getString("appUser", "");
        AvailableTask = FirebaseDatabase.getInstance().getReference("availableTasks").child(taskId);

    }

    @Override
    protected void onStart() {
        super.onStart();
        //attaching value event listener
        AvailableTask.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                DetailedTask detailedTask = dataSnapshot.getValue(DetailedTask.class);
                Log.d("contactName", detailedTask.getContactName());
                contactName = detailedTask.getContactName();
                contactNumber = detailedTask.getContactNumber();
                deadline = detailedTask.getDeadline();
                description = detailedTask.getDescription();

                textViewContactName.setText(contactName);
                textViewContactNumber.setText(contactNumber);
                textViewDeadline.setText(deadline);
                    textViewDescription.setText(description);
                } catch (Exception e) {
                    Log.e("nullValueException", "this happens becouse of onDataChange triggers after delete");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    void goToAcceptedTaskView(View view) {
        Intent goToAcceptedTasks = new Intent(TaskActivity.this, AcceptedTaskActivity.class);
        goToAcceptedTasks.putExtra("TASK_ID", taskId);
        startActivity(goToAcceptedTasks);
        addToAcceptedTasks();
        removeFromAvailableTasks();



    }

    void addToAcceptedTasks(){
        DatabaseReference newAcceptedTask = FirebaseDatabase.getInstance().getReference("acceptedTasks").child(userName).child(taskId);
        newAcceptedTask.child("id").setValue(taskId);
        newAcceptedTask.child("name").setValue(taskName);
        newAcceptedTask.child("area").setValue(taskArea);
        newAcceptedTask.child("contactName").setValue(contactName);
        newAcceptedTask.child("contactNumber").setValue(contactNumber);
        newAcceptedTask.child("deadline").setValue(deadline);
        newAcceptedTask.child("description").setValue(description);
        newAcceptedTask.child("acceptedDate").setValue(DateFormat.getDateTimeInstance().format(new Date()));





    }

    void removeFromAvailableTasks() {
        unavailableTask.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot detailsSnapshot : dataSnapshot.getChildren()) {
                    detailsSnapshot.getRef().removeValue();
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        unavailableTaskOverview.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot detailsSnapshot : dataSnapshot.getChildren()) {
                    detailsSnapshot.getRef().removeValue();
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

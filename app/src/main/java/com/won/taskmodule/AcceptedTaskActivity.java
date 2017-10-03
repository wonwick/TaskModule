package com.won.taskmodule;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AcceptedTaskActivity extends AppCompatActivity {
    String taskId;
    String taskName;
    String taskArea;
    String contactName;
    String contactNumber;
    String deadline;
    String description;
    String acceptedDate;

    SharedPreferences sharedPreferences ;
    String userName;
    DatabaseReference acceptedTask;

    TextView textViewContactName ;
    TextView textViewContactNumber ;
    TextView textViewDeadline ;
    TextView textViewDescription ;
    TextView textViewTaskName ;
    TextView textViewTaskID ;
    TextView textViewTaskArea ;
    TextView textViewAcceptedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceptd_task);

        Intent intent = getIntent();

        taskId = intent.getStringExtra("TASK_ID");
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userName = sharedPreferences.getString("appUser", "");
        acceptedTask = FirebaseDatabase.getInstance().getReference("acceptedTasks").child(userName).child(taskId);

        textViewTaskName = (TextView) findViewById(R.id.textViewTaskName);
        textViewTaskID = (TextView) findViewById(R.id.textViewTaskID);
        textViewTaskArea = (TextView) findViewById(R.id.textViewTaskArea);

        textViewContactName = (TextView) findViewById(R.id.textViewContactName);
        textViewContactNumber = (TextView) findViewById(R.id.textViewContactNumber);
        textViewDeadline = (TextView) findViewById(R.id.textViewDeadline);
        textViewDescription = (TextView) findViewById(R.id.textViewDescription);
        textViewAcceptedDate= (TextView) findViewById(R.id.textViewAcceptedDate);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //attaching value event listener
        acceptedTask.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                AcceptedTask task = dataSnapshot.getValue(AcceptedTask.class);

                taskId = task.getId();
                taskName = task.getName();
                taskArea = task.getArea();
                contactName = task.getContactName();
                contactNumber = task.getContactNumber();
                deadline = task.getDeadline();
                description = task.getDescription();
                acceptedDate=task.getAcceptedDate();

                textViewTaskID.setText(taskId);
                textViewTaskName.setText(taskName);
                textViewTaskArea.setText(taskArea);
                textViewContactName.setText(contactName);
                textViewContactNumber.setText(contactNumber);
                textViewDeadline.setText(deadline);
                textViewDescription.setText(description);
                textViewAcceptedDate.setText(acceptedDate);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

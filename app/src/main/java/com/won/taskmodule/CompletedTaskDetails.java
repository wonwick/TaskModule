package com.won.taskmodule;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class CompletedTaskDetails extends AppCompatActivity {
    TextView textViewContactName ;
    TextView textViewContactNumber ;
    TextView textViewDeadline ;
    TextView textViewDescription ;
    TextView textViewTaskName ;
    TextView textViewTaskID ;
    TextView textViewTaskArea ;
    TextView textViewAcceptedDate;
    TextView textViewCompletedDate;

    String taskId;
    String taskName;
    String taskArea;
    String contactName;
    String contactNumber;
    String deadline;
    String description;
    String acceptedDate;
    String completedDate;
    SharedPreferences sharedPreferences ;
    String userName;
    DatabaseReference completedTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_task_details);

        textViewTaskName = findViewById(R.id.textViewTaskName);
        textViewTaskID = findViewById(R.id.textViewTaskID);
        textViewTaskArea = findViewById(R.id.textViewTaskArea);

       /* textViewContactName = findViewById(R.id.textViewContactName);
        textViewContactNumber = findViewById(R.id.textViewContactNumber);
        textViewDeadline = findViewById(R.id.textViewDeadline);
        textViewDescription = findViewById(R.id.textViewDescription);
        textViewAcceptedDate= findViewById(R.id.textViewAcceptedDate);*/
        textViewCompletedDate=findViewById(R.id.textViewAcceptedDate);

        Intent intent = getIntent();

        taskId = intent.getStringExtra("TASK_ID");
        taskName = intent.getStringExtra("TASK_NAME");
        taskArea = intent.getStringExtra("TASK_Area");

        textViewTaskName.setText(taskName);
        textViewTaskArea.setText(taskArea);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userName = sharedPreferences.getString("appUser", "");
        completedTask = FirebaseDatabase.getInstance().getReference("completedTasks").child(taskId);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //attaching value event listener
        completedTask.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                CompletedTask task = dataSnapshot.getValue(CompletedTask.class);
                try {
                    //taskId = task.getId();
                    taskName = task.getName();
                    taskArea = task.getArea();
                    contactName = task.getContactName();
                    contactNumber = task.getContactNumber();
                    deadline = task.getDeadline();
                    description = task.getDescription();
                    acceptedDate = task.getAcceptedDate();

                    //textViewTaskID.setText(taskId);
                    textViewTaskName.setText(taskName);
                    textViewTaskArea.setText(taskArea);
                    textViewContactName.setText(contactName);
                    textViewContactNumber.setText(contactNumber);
                    textViewDeadline.setText(deadline);
                    textViewDescription.setText(description);
                    textViewAcceptedDate.setText(acceptedDate);
                }
                catch (Exception e){
                    Log.d("AcceptedTaskError","onStart,accptedTaskVAlueListner");

                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

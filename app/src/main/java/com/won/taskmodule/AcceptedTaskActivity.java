package com.won.taskmodule;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;

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

    DatabaseReference unavailableTask;
    DatabaseReference unavailableTaskOverview;
    ScrollView scrolview;
    TextView textViewContactName ;
    TextView textViewContactNumber ;
    TextView textViewDeadline ;
    TextView textViewDescription ;
    TextView textViewTaskName ;
    TextView textViewTaskID ;
    TextView textViewTaskArea ;
    TextView textViewAcceptedDate;
    EditText editTextMessage;
    private static FirebaseDatabase fbdb;
    TableLayout tableLayoutChat;
    DatabaseReference acceptedTask;
    static boolean calledAlready = false;
//     acceptedTask;
//    DatabaseReference chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceptd_task);

        Intent intent = getIntent();
        taskId = intent.getStringExtra("TASK_ID");
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userName = sharedPreferences.getString("appUser", "");
        unavailableTask = FirebaseDatabase.getInstance().getReference("availableTasks").child(taskId);
        unavailableTaskOverview = FirebaseDatabase.getInstance().getReference("OverviewAvailableTask").child(taskId);


        if (fbdb==null)
        {
            fbdb=FirebaseDatabase.getInstance();
        }
        scrolview=(ScrollView) findViewById(R.id.ScrollViewChat);

        textViewTaskName = (TextView) findViewById(R.id.textViewTaskName);
        textViewTaskID = (TextView) findViewById(R.id.textViewTaskID);
        textViewTaskArea = (TextView) findViewById(R.id.textViewTaskArea);

        textViewContactName = (TextView) findViewById(R.id.textViewContactName);
        textViewContactNumber = (TextView) findViewById(R.id.textViewContactNumber);
        textViewDeadline = (TextView) findViewById(R.id.textViewDeadline);
        textViewDescription = (TextView) findViewById(R.id.textViewDescription);
        textViewAcceptedDate= (TextView) findViewById(R.id.textViewAcceptedDate);

        tableLayoutChat=(TableLayout) findViewById(R.id.tableLayoutChat);

        editTextMessage=(EditText) findViewById(R.id.editTextMessage);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //attaching value event listener
        acceptedTask = fbdb.getReference("acceptedTasks").child(userName).child(taskId);
        acceptedTask.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                AcceptedTask task = dataSnapshot.getValue(AcceptedTask.class);
                try {
                    taskId = task.getId();
                    taskName = task.getName();
                    taskArea = task.getArea();
                    contactName = task.getContactName();
                    contactNumber = task.getContactNumber();
                    deadline = task.getDeadline();
                    description = task.getDescription();
                    acceptedDate = task.getAcceptedDate();

                    textViewTaskID.setText(taskId);
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
        DatabaseReference chat=fbdb.getReference("chat").child(taskId);

        chat.addValueEventListener(new ValueEventListener() {
            //chat is handled here
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tableLayoutChat.removeAllViews();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting artist;
                    ChatMessage chatMessage = postSnapshot.getValue(ChatMessage.class);
                    String theMessege=chatMessage.getMessage();
                    String theSender=chatMessage.getSender();
                    String theTime=chatMessage.getTime();


                    TableRow header=new TableRow(AcceptedTaskActivity.this);
                    ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    header.setLayoutParams(params);

                    TextView sender = new TextView(AcceptedTaskActivity.this);
                    sender.setGravity(Gravity.LEFT);
                    if (theSender.equals(userName)){
                        sender.setTextColor(Color.BLUE);
                    }
                    else{
                        sender.setTextColor(Color.GREEN);
                    }
                    sender.setText(theSender);

                    TextView time = new TextView(AcceptedTaskActivity.this);
                    time.setGravity(Gravity.LEFT);
                    time.setTextColor(Color.GRAY);
                    time.setText(theTime);
                    //Log.d("theTime",theTime);


                    TableRow body=new TableRow(AcceptedTaskActivity.this);
                    body.setLayoutParams(params);
                    TextView message = new TextView(AcceptedTaskActivity.this);
                    message.setGravity(Gravity.LEFT);

                    message.setText(theMessege);

                    TableRow spacing=new TableRow(AcceptedTaskActivity.this);
                    spacing.setLayoutParams(params);
                    TextView space = new TextView(AcceptedTaskActivity.this);


                    header.addView(sender);
                    header.addView(time);
                    body.addView(message);
                    spacing.addView(space);

                    tableLayoutChat.addView(header, params);
                    tableLayoutChat.addView(body, params);
                    tableLayoutChat.addView(spacing,params);
                    scrolview.fullScroll(ScrollView.FOCUS_DOWN);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(AcceptedTaskActivity.this, AcceptedTasksListActivity.class));
        finish();

    }

    void CompleteTask(View view) {
        DatabaseReference chat=FirebaseDatabase.getInstance().getReference("chat").child(taskId).push();
        chat.child("sender").setValue(userName);
        chat.child("message").setValue("----- C O M P L E T E D -----");
        chat.child("time").setValue(DateFormat.getDateTimeInstance().format(new Date()));
        startActivity(new Intent(AcceptedTaskActivity.this, EmployeeActivity.class));
        addToCompletedTasks();
        removeFromAcceptedTasks();


    }

    void sendMessage(View view) {
        String textMessage=editTextMessage.getText().toString();
        DatabaseReference chat=FirebaseDatabase.getInstance().getReference("chat").child(taskId).push();
        chat.child("sender").setValue(userName);
        chat.child("message").setValue(textMessage);
        chat.child("time").setValue(DateFormat.getDateTimeInstance().format(new Date()));
        editTextMessage.setText("");

        scrolview.fullScroll(ScrollView.FOCUS_DOWN);

    }
    void addToCompletedTasks(){
        DatabaseReference newAcceptedTask = FirebaseDatabase.getInstance().getReference("completedTasks").child(userName).child(taskId);
        newAcceptedTask.child("id").setValue(taskId);
        newAcceptedTask.child("name").setValue(taskName);
        newAcceptedTask.child("area").setValue(taskArea);
        newAcceptedTask.child("contactName").setValue(contactName);
        newAcceptedTask.child("contactNumber").setValue(contactNumber);
        newAcceptedTask.child("deadline").setValue(deadline);
        newAcceptedTask.child("description").setValue(description);
        newAcceptedTask.child("acceptedDate").setValue(acceptedDate);
        newAcceptedTask.child("CompletedDate").setValue(DateFormat.getDateTimeInstance().format(new Date()));





    }

    void removeFromAcceptedTasks() {
        acceptedTask.addValueEventListener(new ValueEventListener() {
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

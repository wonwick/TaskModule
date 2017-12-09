package com.won.taskmodule;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.util.Size;
import android.util.TypedValue;
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
        DatabaseReference acceptedTask = fbdb.getReference("acceptedTasks").child(userName).child(taskId);
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
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                    header.setLayoutParams(params);
                    header.setBackgroundColor(Color.WHITE);

                    TextView sender = new TextView(AcceptedTaskActivity.this);
                    sender.setGravity(Gravity.START);
                    sender.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
                    sender.setTypeface(Typeface.DEFAULT_BOLD);
                    sender.setPadding(8,0,0,0);
                    if (theSender.equals(userName)){
                        sender.setTextColor(Color.parseColor("#304FFE"));
                        sender.setText("Me");
                    }
                    else{
                        sender.setTextColor(Color.parseColor("#1B5E20"));
                        sender.setText(theSender);
                    }


                    TextView time = new TextView(AcceptedTaskActivity.this);
                    time.setGravity(Gravity.END);
                    time.setTextColor(Color.GRAY);
                    time.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
                    time.setPadding(0,0,8,0);
                    time.setText(theTime);

                    //Log.d("theTime",theTime);


                    TableRow body=new TableRow(AcceptedTaskActivity.this);
                    body.setLayoutParams(params);
                    body.setBackgroundColor(Color.WHITE);
                    TextView message = new TextView(AcceptedTaskActivity.this);
                    message.setGravity(Gravity.START);
                    message.setTextColor(Color.parseColor("#424242"));
                    message.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
                    message.setText(theMessege);
                    message.setPadding(8,0,0,0);

                    TableRow spacing=new TableRow(AcceptedTaskActivity.this);
                    spacing.setLayoutParams(params);
                    spacing.setBackgroundColor(Color.WHITE);
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

    void psudoAction(View view) {
        //do nothing
        /*Oct 3, 2017 12:06:36 PMaddclose
        sherlock:
        "first chatEntry"
        Oct 3, 2017 12:08:36 PM
        admin:
        "reply for the first Entry"
        Oct 3, 2017 12:11:36 PM:
        "second chat entry"*/

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

}

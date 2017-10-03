package com.won.taskmodule;

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

import java.util.ArrayList;
import java.util.List;

public class AcceptedTasksListActivity extends AppCompatActivity {

    ListView listViewTask;
    DatabaseReference databaseTasks;
    List<Task> tasks;
    SharedPreferences sharedPreferences;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_tasks);
        listViewTask = (ListView) findViewById(R.id.listViewTasks);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userName = sharedPreferences.getString("appUser", "");
        databaseTasks = FirebaseDatabase.getInstance().getReference("acceptedTasks").child(userName);
        tasks = new ArrayList<>();

        listViewTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //getting the selected artist
                Task task = tasks.get(i);

                //creating an intent
                Intent intent = new Intent(getApplicationContext(), AcceptedTaskActivity.class);

                //putting artist name and id to intent
                intent.putExtra("TASK_ID", task.getId());
                intent.putExtra("TASK_NAME", task.getName());
                intent.putExtra("TASK_Area", task.getArea());

                //starting the activity with intent
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //attaching value event listener
        databaseTasks.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clearing the previous artist list
                tasks.clear();
                Log.d("asdasd", "this is it 1");

                //iterating through all the nodes
                Log.d("asdasd", "" + dataSnapshot.getChildrenCount());
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting artist
                    Log.d("asdasd", "this is it");
                    Task task = postSnapshot.getValue(Task.class);

                    //adding artist to the list
                    tasks.add(task);
                }

                //creating adapter
                TaskList taskAdapter = new TaskList(AcceptedTasksListActivity.this, tasks);
                //attaching adapter to the listview
                listViewTask.setAdapter(taskAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void onBackPressed() {
        startActivity(new Intent(AcceptedTasksListActivity.this, EmployeeActivity.class));
        finish();

    }
}

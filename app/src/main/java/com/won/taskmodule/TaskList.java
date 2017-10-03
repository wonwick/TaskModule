package com.won.taskmodule;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Oshan Wickramaratne on 2017-09-07.
 */

public class TaskList extends ArrayAdapter<Task> {
    List<Task> Tasks;
    private Activity context;

    public TaskList(Activity context, List<Task> Tasks) {
        super(context, R.layout.layout_task_list, Tasks);
        this.context = context;
        this.Tasks = Tasks;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_task_list, null, true);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        TextView textViewGenre = (TextView) listViewItem.findViewById(R.id.textViewArea);

        Task Task = Tasks.get(position);
        textViewName.setText(Task.getName());
        textViewGenre.setText(Task.getArea());

        return listViewItem;
    }
}

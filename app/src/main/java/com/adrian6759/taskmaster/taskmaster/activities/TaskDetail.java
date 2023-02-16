package com.adrian6759.taskmaster.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.adrian6759.taskmaster.R;
import com.adrian6759.taskmaster.taskmaster.adapter.TasksRecyclerViewAdapter;

public class TaskDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        Intent callingIntent = getIntent();
        String taskTitle = null;
        String taskBody = null;
        String taskState = null;
        if (callingIntent != null) {
            taskTitle = callingIntent.getStringExtra(TasksRecyclerViewAdapter.TASKS_TITLE_TAG);
            taskBody = callingIntent.getStringExtra(TasksRecyclerViewAdapter.TASKS_BODY_TAG);
            taskState = callingIntent.getStringExtra(TasksRecyclerViewAdapter.TASKS_STATE_TAG);
        }
        ((TextView)findViewById(R.id.activityTaskTitleViewText)).setText(taskTitle);
        ((TextView)findViewById(R.id.activityTaskBodyViewText)).setText(taskBody);
        ((TextView)findViewById(R.id.activityTaskStateViewText)).setText(taskState);

    }
}
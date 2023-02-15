package com.adrian6759.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.adrian6759.taskmaster.R;

public class TaskDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        Intent callingIntent = getIntent();
        String userTaskString = null;
        if (callingIntent != null) {
            userTaskString = callingIntent.getStringExtra(MainActivity.TASKS_ADD_EXTRA_TAG);
            TextView userTaskTextView = (TextView)findViewById(R.id.activityTaskDetailViewText);
            if (userTaskString != null) {
                userTaskTextView.setText(userTaskString);
            }else {
                userTaskTextView.setText(R.string.no_input);
            }
        }
    }
}
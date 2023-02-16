package com.adrian6759.taskmaster.taskmaster.activities;

import static com.adrian6759.taskmaster.taskmaster.activities.SettingsActivity.USER_USERNAME_TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.adrian6759.taskmaster.R;
import com.adrian6759.taskmaster.taskmaster.adapter.TasksRecyclerViewAdapter;
import com.adrian6759.taskmaster.taskmaster.models.Task;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String TASKS_ADD_EXTRA_TAG = "taskDetail";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupRecyclerView();

        //Add Event Listener for Click a Button
        //Step 1: Get element by ID
        Button addTaskIntentButton = (Button) findViewById(R.id.addTaskIntentButton);
        //Step 2: Set on Click Listener
        addTaskIntentButton.setOnClickListener(v -> {
            //Step:3 Define Our onClick() Callback
            Intent goToAddTaskIntent = new Intent(this, AddTaskActivity.class);
            //Step 4: Define logic to be run
            startActivity(goToAddTaskIntent);
        });
        Button allTasksIntentButton = (Button) findViewById(R.id.allTasksIntentButton);
        //Step 2: Set on Click Listener
        allTasksIntentButton.setOnClickListener(v -> {
            //Step:3 Define Our onClick() Callback
            Intent goToAllTasksIntent = new Intent(this, AllTasksActivity.class);
            //Step 4: Define logic to be run
            startActivity(goToAllTasksIntent);
        });

        ImageView settingsButton = (ImageView) findViewById(R.id.activityMainSettingsButton);
        //Step 2: Set on Click Listener
        settingsButton.setOnClickListener(v -> {
            //Step:3 Define Our onClick() Callback
            Intent goToSettingsIntent = new Intent(this, SettingsActivity.class);
            //Step 4: Define logic to be run
            startActivity(goToSettingsIntent);
        });

        }
    //Grab the recyclerview
    public void setupRecyclerView() {

        List<Task> tasksList = new ArrayList<>();
        Task task1 = new Task("Task1", "Task 1  Description", "state");
        Task task2 = new Task("Task2", "Task 2  Description", "state");
        Task task3 = new Task("Task3", "Task 3  Description", "state");
        Task task4 = new Task("Task4", "Task 4  Description", "state");
        Task task5 = new Task("Task5", "Task 5  Description", "state");
        Task task6 = new Task("Task6", "Task 6  Description", "state");
        Task task7 = new Task("Task7", "Task 7  Description", "state");
        Task task8 = new Task("Task8", "Task 8  Description", "state");
        tasksList.add(task1);
        tasksList.add(task2);
        tasksList.add(task3);
        tasksList.add(task4);
        tasksList.add(task5);
        tasksList.add(task6);
        tasksList.add(task7);
        tasksList.add(task8);

        RecyclerView tasksRecyclerView = findViewById(R.id.activityMainRecycleViewTasks);
        //Set the layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        tasksRecyclerView.setLayoutManager(layoutManager);
        //Create and attach the RecyclerView.Adapter
        //Hand in some data
        TasksRecyclerViewAdapter adapter = new TasksRecyclerViewAdapter(tasksList, this);
        tasksRecyclerView.setAdapter(adapter);

    }
        @Override
        protected void onResume() {
            super.onResume();

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

            String userName = preferences.getString(USER_USERNAME_TAG, "no username");
            ((TextView)findViewById(R.id.activityMainUsernameDisplay)).setText(userName);

        }
    }
//    TextView taskDetailButtonOne = (TextView) findViewById(R.id.activityMainTaskOneDetailButton);
////Step 2: Set on Click Listener
//        taskDetailButtonOne.setOnClickListener(v -> {
//                String taskDetail = ((TextView) findViewById(R.id.activityMainTaskOneDetailButton)).getText().toString();
//                //Step:3 Define Our onClick() Callback
//                Intent goToTaskDetailIntent = new Intent(this, TaskDetail.class);
//        goToTaskDetailIntent.putExtra(TASKS_ADD_EXTRA_TAG, taskDetail);
//        //Step 4: Define logic to be run
//        startActivity(goToTaskDetailIntent);
//        });
//        TextView taskDetailButtonTwo = (TextView) findViewById(R.id.activityMainTaskTwoDetailButton);
//        //Step 2: Set on Click Listener
//        taskDetailButtonTwo.setOnClickListener(v -> {
//        String taskDetail = ((TextView) findViewById(R.id.activityMainTaskTwoDetailButton)).getText().toString();
//        //Step:3 Define Our onClick() Callback
//        Intent goToTaskDetailIntent = new Intent(this, TaskDetail.class);
//        goToTaskDetailIntent.putExtra(TASKS_ADD_EXTRA_TAG, taskDetail);
//        //Step 4: Define logic to be run
//        startActivity(goToTaskDetailIntent);
//        });
//        TextView taskDetailButtonThree = (TextView) findViewById(R.id.activityMainTaskThreeDetailButton);
//        //Step 2: Set on Click Listener
//        taskDetailButtonThree.setOnClickListener(v -> {
//        String taskDetail = ((TextView) findViewById(R.id.activityMainTaskThreeDetailButton)).getText().toString();
//        //Step:3 Define Our onClick() Callback
//        Intent goToTaskDetailIntent = new Intent(this, TaskDetail.class);
//        goToTaskDetailIntent.putExtra(TASKS_ADD_EXTRA_TAG, taskDetail);
//        //Step 4: Define logic to be run
//        startActivity(goToTaskDetailIntent);
//        });

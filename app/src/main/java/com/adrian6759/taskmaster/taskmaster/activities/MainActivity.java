package com.adrian6759.taskmaster.taskmaster.activities;

import static com.adrian6759.taskmaster.taskmaster.activities.SettingsActivity.USER_USERNAME_TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.adrian6759.taskmaster.R;
import com.adrian6759.taskmaster.taskmaster.adapter.TasksRecyclerViewAdapter;
import com.adrian6759.taskmaster.taskmaster.database.TaskMasterDatabase;
import com.adrian6759.taskmaster.taskmaster.models.Task;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String TASKS_ADD_EXTRA_TAG = "taskDetail";
    TaskMasterDatabase taskMasterDatabase;
    public static final String DATABASE_NAME = "TaskMaster";
        List<Task> tasksList;
        TasksRecyclerViewAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupRecyclerView();
        taskMasterDatabase = Room.databaseBuilder(
                        getApplicationContext(),
                        TaskMasterDatabase.class,
                        DATABASE_NAME)
                .fallbackToDestructiveMigration() //Don't use this in production
                .allowMainThreadQueries()
                .build();

        tasksList = taskMasterDatabase.taskDao().findAll();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
//
            String userName = preferences.getString(USER_USERNAME_TAG, "no username");
            ((TextView)findViewById(R.id.activityMainUsernameDisplay)).setText(userName);

        setupRecyclerView();
        setupButtons();
    }
        @Override
        protected void onResume() {
            super.onResume();
            tasksList.clear();
            tasksList.addAll(taskMasterDatabase.taskDao().findAll());
            adapter.notifyDataSetChanged();

//

        }

    //Grab the recyclerview
    public void setupRecyclerView() {


        RecyclerView tasksRecyclerView = findViewById(R.id.activityMainRecycleViewTasks);
        //Set the layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        tasksRecyclerView.setLayoutManager(layoutManager);
        //Create and attach the RecyclerView.Adapter
        //Hand in some data
        adapter = new TasksRecyclerViewAdapter(tasksList, this);
        tasksRecyclerView.setAdapter(adapter);

    }

    public void setupButtons(){

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

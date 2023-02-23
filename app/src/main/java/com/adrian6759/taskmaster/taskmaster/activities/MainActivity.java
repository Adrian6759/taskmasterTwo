package com.adrian6759.taskmaster.taskmaster.activities;

import static com.adrian6759.taskmaster.taskmaster.activities.SettingsActivity.USER_USERNAME_TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.adrian6759.taskmaster.R;
import com.adrian6759.taskmaster.taskmaster.adapter.TasksRecyclerViewAdapter;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.TaskStateEnum;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String TASKS_ADD_EXTRA_TAG = "taskDetail";
    public static final String DATABASE_NAME = "TaskMaster";
    public static final String TAG = "mainActivity";
        List<Task> tasksList;
        TasksRecyclerViewAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //HARDCODE Test for RecycleView
//        tasksList = new ArrayList<>();
//        Task newTask = Task.builder()
//                .title("Task 1")
//                .body("Do the task")
//                .state(TaskStateEnum.Assigned)
//                .build();

//        tasksList.add(newTask);

        setupRecyclerView();
        setupButtons();
    }
        @Override
        protected void onResume() {
            super.onResume();
            Amplify.API.query(
                    ModelQuery.list(Task.class),
                    success -> {
                        tasksList.clear();
                        Log.i(TAG, "Read tasks successfully!");
                        for (Task databaseTask : success.getData()) {
//                            String selectedTaskTeamStringName = "TaskTeam1";
                            if (databaseTask.getTaskTeam() != null){

//                            if(databaseTask.getTaskTeam().getName().equals(selectedTaskTeamStringName))
                            tasksList.add(databaseTask);
                            }
                        }
                        runOnUiThread(() -> adapter.notifyDataSetChanged());
                    },
                        failure -> Log.e(TAG, "Failed to read task from the Database")

            );
//            tasksList.addAll(taskMasterDatabase.taskDao().findAll());


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

            String userName = preferences.getString(USER_USERNAME_TAG, "no username");
            ((TextView)findViewById(R.id.activityMainUsernameDisplay)).setText(userName);


        }

    //Grab the recyclerview
    public void setupRecyclerView() {
        tasksList = new ArrayList<>();

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

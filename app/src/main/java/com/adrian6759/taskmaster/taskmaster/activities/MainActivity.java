package com.adrian6759.taskmaster.taskmaster.activities;

import static com.adrian6759.taskmaster.taskmaster.activities.SettingsActivity.CHOOSE_TEAM_TAG;
import static com.adrian6759.taskmaster.taskmaster.activities.SettingsActivity.USER_USERNAME_TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.adrian6759.taskmaster.R;
import com.adrian6759.taskmaster.taskmaster.activities.authActivities.LoginActivity;
import com.adrian6759.taskmaster.taskmaster.activities.authActivities.SignUpActivity;
import com.adrian6759.taskmaster.taskmaster.adapter.TasksRecyclerViewAdapter;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;



import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "mainActivity";
        List<Task> tasksList;
        TasksRecyclerViewAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupRecyclerView();
        setupButtons();

//        Amplify.Auth.signUp(
//                "adrianosohernandez@gmail.com",
//                "password",
//                AuthSignUpOptions.builder()
//                        .userAttribute(AuthUserAttributeKey.email(), "adrianosohernandez@gmail.com")
//                        .userAttribute(AuthUserAttributeKey.nickname(), "Oso")
//                        .build(),
//                success -> Log.i(TAG, "Signed Up Successfully!"),
//                failure -> Log.e(TAG, "Failed to signup with email: adrianosohernandez@gmail.com")
//
//        );
//        Amplify.Auth.confirmSignUp(
//                "adrianosohernandez@gmail.com",
//                "697277",
//                success -> Log.i(TAG, "Confirmed Sign Up Success!"),
//                failure -> Log.e(TAG, "Confirmed sign up failure" + failure)
//                );

//        Amplify.Auth.signIn(
//                "adrianosohernandez@gmail.com",
//                "password",
//                success -> Log.i(TAG, "Signed In Successfully!"),
//                failure -> Log.e(TAG, "Failed to sign in with email: adrianosohernandez@gmail.com")
//        );
//        Amplify.Auth.fetchAuthSession(
//                success -> Log.i(TAG, "Current Auth Session!"),
//                failure -> Log.e(TAG, "Failed to fetch auth session" + failure)
//        );

    }
        @Override
        protected void onResume() {
            super.onResume();
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            String teamChosen = preferences.getString(CHOOSE_TEAM_TAG, "no team chosen");
            Amplify.API.query(
                    ModelQuery.list(Task.class),
                    success -> {
                        tasksList.clear();
                        Log.i(TAG, "Read tasks successfully!");
                        for (Task databaseTask : success.getData()) {
                            String selectedTaskTeamStringName = teamChosen;
                            if (databaseTask.getTaskTeam() != null){
                                if(databaseTask.getTaskTeam().getName().equals(selectedTaskTeamStringName))
                                tasksList.add(databaseTask);
                            }
                        }
                        runOnUiThread(() -> adapter.notifyDataSetChanged());
                    },
                        failure -> Log.e(TAG, "Failed to read task from the Database")

            );




            String userName = preferences.getString(USER_USERNAME_TAG, "no username");
            ((TextView)findViewById(R.id.activityMainUsernameDisplay)).setText(userName);
            ((TextView)findViewById(R.id.taskTeamChosenTexttView)).setText(teamChosen);
        }


    public void setupRecyclerView() {
        tasksList = new ArrayList<>();

        RecyclerView tasksRecyclerView = findViewById(R.id.activityMainRecycleViewTasks);
        //Set the layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        tasksRecyclerView.setLayoutManager(layoutManager);

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
        allTasksIntentButton.setOnClickListener(v -> {
            Intent goToAllTasksIntent = new Intent(this, AllTasksActivity.class);
            startActivity(goToAllTasksIntent);
        });

        ImageView settingsButton = (ImageView) findViewById(R.id.activityMainSettingsButton);
        settingsButton.setOnClickListener(v -> {
            Intent goToSettingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(goToSettingsIntent);
        });
        Button mainLoginIntentButton = (Button) findViewById(R.id.MainLoginButton);
        mainLoginIntentButton.setOnClickListener(v -> {
            Intent goToLoginIntent = new Intent(this, LoginActivity.class);
            startActivity(goToLoginIntent);
        });
        Button mainSignupIntentButton  = (Button) findViewById(R.id.MainSignupButton);
        mainSignupIntentButton.setOnClickListener(v -> {
            Intent goToSignUpIntent = new Intent(this, SignUpActivity.class);
            startActivity(goToSignUpIntent);
        });
    }
}

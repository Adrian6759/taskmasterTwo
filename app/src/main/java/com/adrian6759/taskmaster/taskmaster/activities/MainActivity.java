package com.adrian6759.taskmaster.taskmaster.activities;

import static com.adrian6759.taskmaster.taskmaster.activities.SettingsActivity.CHOOSE_TEAM_TAG;
import static com.adrian6759.taskmaster.taskmaster.activities.SettingsActivity.USER_USERNAME_TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.adrian6759.taskmaster.R;
import com.adrian6759.taskmaster.taskmaster.activities.authActivities.LoginActivity;
import com.adrian6759.taskmaster.taskmaster.activities.authActivities.SignUpActivity;
import com.adrian6759.taskmaster.taskmaster.adapter.TasksRecyclerViewAdapter;
import com.amplifyframework.analytics.AnalyticsEvent;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "mainActivity";
        List<Task> tasksList;
        TasksRecyclerViewAdapter adapter;
        private final MediaPlayer mp = new MediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int dateNow = new Date().getDate();

        AnalyticsEvent appStartedEvent = AnalyticsEvent.builder()
                .name("Application started!")
                .addProperty("Username", "")
                .addProperty("TimeOfLaunch", dateNow)
                .build();

        Amplify.Analytics.recordEvent(appStartedEvent);

        Amplify.Predictions.convertTextToSpeech(
                "I like to eat spaghetti",
                result -> playAudio(result.getAudioData()),
                error -> Log.e("MyAmplifyApp", "Conversion failed", error)
        );
//         manual file upload to S3
//        File exampleFile = new File(getApplicationContext().getFilesDir(), "ExampleKey");
//
//        try {
//                BufferedWriter writer = new BufferedWriter(new FileWriter(exampleFile));
//                writer.append("Example file contents");
//                writer.close();
//            } catch (Exception exception) {
//                Log.e("MyAmplifyApp", "Upload failed", exception);
//            }
//
//        Amplify.Storage.uploadFile(
//                "ExampleKey",
//                exampleFile,
//                success -> Log.i(TAG, "FILE SUCCESSFULLY UPLOADED TO S3"),
//                failure -> Log.e(TAG, "FAILED TO UPLOAD FILE" + failure)
//        );

        setupRecyclerView();
        setupButtons();

    }

    private void playAudio(InputStream data) {
        File mp3File = new File(getCacheDir(), "audio.mp3");

        try (OutputStream out = new FileOutputStream(mp3File)) {
            byte[] buffer = new byte[8 * 1_024];
            int bytesRead;
            while ((bytesRead = data.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            mp.reset();
            mp.setOnPreparedListener(MediaPlayer::start);
            mp.setDataSource(new FileInputStream(mp3File).getFD());
            mp.prepareAsync();
        } catch (IOException error) {
            Log.e("MyAmplifyApp", "Error writing audio file", error);
        }
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

        AtomicReference<String> username = new AtomicReference<>("");
        // we need to get access to current auth user
        Amplify.Auth.getCurrentUser(
                success -> {
                    Log.i(TAG, "Got current user");
                    username.set(success.getUsername());
                },
                failure -> Log.i(TAG, "Did not get a current user" + failure)
        );

//        if (username.toString().equals("")) {
//            // if auth user is null
//            // show sign up and login buttons
//            ((Button)findViewById(R.id.MainSignupButton)).setVisibility(View.VISIBLE);
//            ((Button)findViewById(R.id.MainLoginButton)).setVisibility(View.VISIBLE);
//            // hide logout bttn
//        } else {
//            ((Button)findViewById(R.id.MainSignupButton)).setVisibility(View.INVISIBLE);
//            ((Button)findViewById(R.id.MainLoginButton)).setVisibility(View.INVISIBLE);
//            // show logout bttn
//        }
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
        Button mainLogoutButton = (Button) findViewById(R.id.LogoutButton);
        mainLogoutButton.setOnClickListener(v->{
            Intent goToLoginIntent =  new Intent(this, LoginActivity.class);
            startActivity(goToLoginIntent);
                });
    }
}
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

package com.adrian6759.taskmaster.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


import com.adrian6759.taskmaster.R;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.TaskStateEnum;
import com.amplifyframework.datastore.generated.model.TaskTeam;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class SettingsActivity extends AppCompatActivity {
    SharedPreferences preferences;
    public static final String USER_USERNAME_TAG = "userName";
    public static final String CHOOSE_TEAM_TAG = "ChooseTeam";
    public static final String TAG = "SettingsActivity";
    CompletableFuture<List<TaskTeam>> taskTeamFuture = new CompletableFuture<>();
    Spinner chooseTeamSpinner;
    ArrayList<String> teamNames;
    ArrayList<TaskTeam> taskTeam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        chooseTeamSpinner = findViewById(R.id.ChooseTeamSpinner);
        teamNames = new ArrayList<>();
        taskTeam = new ArrayList<>();

        //Completeable Future
        Amplify.API.query(
                ModelQuery.list(TaskTeam.class),
                success -> {
                    Log.i(TAG, "Read TaskTeam successfully!");
                    for(TaskTeam databaseTaskTeam : success.getData()) {
                        teamNames.add(databaseTaskTeam.getName());
                        taskTeam.add(databaseTaskTeam);
                    }
                    taskTeamFuture.complete(taskTeam);
                    runOnUiThread(this::setupTaskSpinners);
                },
                failure -> {
                    taskTeamFuture.complete(null);
                    Log.e(TAG, "Failed to read TaskTeam", failure);
                }

        );

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        String userName = preferences.getString(USER_USERNAME_TAG, "");
        EditText editUsernameText = findViewById(R.id.activitySettingsEditTextUsername);
        editUsernameText.setText(userName);
        setupSaveButton();

    }
    public void setupTaskSpinners() {
        chooseTeamSpinner.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                teamNames
        ));

    }
    public void setupSaveButton() {
        Button saveButton = findViewById(R.id.activitySettingsSaveButton);
        saveButton.setOnClickListener(v -> {
            EditText editUsernameText = findViewById(R.id.activitySettingsEditTextUsername);
            String selectedTaskTeamStringName = chooseTeamSpinner.getSelectedItem().toString();
            SharedPreferences.Editor preferencesEditor = preferences.edit();
            String usernameString = editUsernameText.getText().toString();

            preferencesEditor.putString(USER_USERNAME_TAG, usernameString);
            preferencesEditor.putString(CHOOSE_TEAM_TAG, selectedTaskTeamStringName);
            preferencesEditor.apply();

            Snackbar.make(findViewById(R.id.activitySettingsPage), "Preferences Updated!", Snackbar.LENGTH_SHORT).show();
        });
    }
}
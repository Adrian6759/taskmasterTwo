package com.adrian6759.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.EditText;

import com.adrian6759.taskmaster.R;
import com.google.android.material.snackbar.Snackbar;

public class SettingsActivity extends AppCompatActivity {
    SharedPreferences preferences;
    public static final String USER_USERNAME_TAG = "userName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        String userName = preferences.getString(USER_USERNAME_TAG, "");
        EditText editUsernameText = findViewById(R.id.activitySettingsEditTextUsername);
        editUsernameText.setText(userName);


        Button saveButton = findViewById(R.id.activitySettingsSaveButton);
        saveButton.setOnClickListener(v -> {
            SharedPreferences.Editor preferencesEditor = preferences.edit();
            String usernameString = editUsernameText.getText().toString();

            preferencesEditor.putString(USER_USERNAME_TAG, usernameString);
            preferencesEditor.apply();

            Snackbar.make(findViewById(R.id.activitySettingsPage), "Username updated!", Snackbar.LENGTH_SHORT).show();
        });
    }
}
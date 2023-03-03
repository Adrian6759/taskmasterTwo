package com.adrian6759.taskmaster.taskmaster.activities.authActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.adrian6759.taskmaster.R;
import com.adrian6759.taskmaster.taskmaster.activities.MainActivity;
import com.amplifyframework.core.Amplify;

public class LoginActivity extends AppCompatActivity {
    public static final String TAG = "login_activity";
    Intent callingActivity;
    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
       callingActivity = getIntent();

        setupLoginButton();
        setupButton();
    }

    public void setupLoginButton(){
        findViewById(R.id.LoginButton).setOnClickListener(v -> {


            if (callingActivity !=  null) {
                userEmail = callingActivity.getStringExtra(SignUpActivity.USER_EMAIL);
                ((EditText) findViewById(R.id.editTextTextEmailAddressLogin)).setText(userEmail);
            } else {
                userEmail = ((EditText) findViewById(R.id.editTextTextEmailAddressLogin)).getText().toString();
            }
            String userPassword = ((EditText) findViewById(R.id.editTextTextPasswordLogin)).getText().toString();

            Amplify.Auth.signIn(
                    userEmail,
                    userPassword,
                    success -> Log.i(TAG, "Signed In Successfully!"),
                    failure -> Log.e(TAG, "Failed to sign in with email: " + userEmail + "with error code: " + failure )
            );

            Intent goToMainActivityIntent = new Intent(this, MainActivity.class);
            startActivity(goToMainActivityIntent);


        });

    }
    public void setupButton(){
        Button mainSignupIntentButton  = (Button) findViewById(R.id.LoginSignUpButton);
        mainSignupIntentButton.setOnClickListener(v -> {
            Intent goToSignUpIntent = new Intent(this, SignUpActivity.class);
            startActivity(goToSignUpIntent);
    });
}
}
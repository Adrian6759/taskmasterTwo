package com.adrian6759.taskmaster.taskmaster.activities.authActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.adrian6759.taskmaster.R;
import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;

public class SignUpActivity extends AppCompatActivity {
    public static final String TAG = "sign_up_activity";
    public static final String USER_EMAIL = "user_email";
    public static final String USER_PASSWORD = "user_password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        setupSignUpButton();
    }

    public void setupSignUpButton(){
        findViewById(R.id.SignUpButton).setOnClickListener(view -> {

       String userEmail = ((EditText)findViewById(R.id.editTextTextEmailAddress)).getText().toString();
        String userPassword = ((EditText)findViewById(R.id.editTextTextPassword)).getText().toString();

            Amplify.Auth.signUp(
                userEmail,
                userPassword,
                AuthSignUpOptions.builder()
                        .userAttribute(AuthUserAttributeKey.email(), userEmail)
                        .userAttribute(AuthUserAttributeKey.nickname(), "Oso")
                        .build(),
                success -> Log.i(TAG, "Signed Up Successfully!"),
                failure -> Log.e(TAG, "Failed to signup with email: " + userEmail + failure)

        );
            Intent goToConfirmSignUpActivity = new Intent(this, VerifySignUp.class);
            goToConfirmSignUpActivity.putExtra(USER_EMAIL, userEmail);
            goToConfirmSignUpActivity.putExtra(USER_PASSWORD, userPassword);
            startActivity(goToConfirmSignUpActivity);
        });

    }
}
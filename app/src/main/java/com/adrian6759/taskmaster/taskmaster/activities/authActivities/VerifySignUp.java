package com.adrian6759.taskmaster.taskmaster.activities.authActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.adrian6759.taskmaster.R;
import com.adrian6759.taskmaster.taskmaster.adapter.TasksRecyclerViewAdapter;
import com.amplifyframework.core.Amplify;

public class VerifySignUp extends AppCompatActivity {
    public static final String TAG = "verify_signup";
    Intent callingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_sign_up);
        callingIntent = getIntent();

        Intent callingIntent = getIntent();
        //TODO: If signing up do i need this code below?
//        String taskTitle = null;
//        String taskBody = null;
//        String taskState = null;
//        if (callingIntent != null) {
//            taskTitle = callingIntent.getStringExtra(TasksRecyclerViewAdapter.TASKS_TITLE_TAG);
//            taskBody = callingIntent.getStringExtra(TasksRecyclerViewAdapter.TASKS_BODY_TAG);
//            taskState = callingIntent.getStringExtra(TasksRecyclerViewAdapter.TASKS_STATE_TAG);
//        }
        setupConfirmationButton();
    }

    public void setupConfirmationButton(){
        findViewById(R.id.SubmitConfirmationButton).setOnClickListener(view ->{

            String userEmail = "";
            if (callingIntent != null){
                userEmail = callingIntent.getStringExtra(SignUpActivity.USER_EMAIL);
            }
            String confirmationCode = ((EditText) findViewById(R.id.editTextConfirmationCode)).getText().toString();

            Amplify.Auth.confirmSignUp(
                userEmail,
                confirmationCode,
                success -> Log.i(TAG, "Confirmed Successfully!"),
                failure -> Log.e(TAG, "FAILED to verify confirmation code" + confirmationCode + "for user: with failure" + failure)
                );
            Intent goToLoginActivityIntent = new Intent(this, LoginActivity.class);
            goToLoginActivityIntent.putExtra(SignUpActivity.USER_EMAIL, userEmail);
            startActivity(goToLoginActivityIntent);
                });
    }
}
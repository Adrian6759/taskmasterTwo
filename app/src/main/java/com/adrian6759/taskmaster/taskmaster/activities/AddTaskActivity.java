package com.adrian6759.taskmaster.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.adrian6759.taskmaster.R;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.TaskStateEnum;

public class AddTaskActivity extends AppCompatActivity {
    public final static String TAG = "AddATaskActivity";
    Spinner taskStateSpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        taskStateSpinner = findViewById(R.id.TaskStateSpinner);

                setupTaskSpinner();
                setupSaveButton();

    }
    //Setup Spinner for enum
    public void setupTaskSpinner(){
        taskStateSpinner.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                TaskStateEnum.values()
        ));
    }
    public void setupSaveButton(){

        findViewById(R.id.AddTaskButton).setOnClickListener(v -> {
            Task newTask = Task.builder()
                    .title(((EditText)findViewById(R.id.editTextTaskTitle)).getText().toString())
                    .body(((EditText)findViewById(R.id.editTextTaskBody)).getText().toString())
                    .state((TaskStateEnum) taskStateSpinner.getSelectedItem())
                            .build();
            Amplify.API.mutate(
                    ModelMutation.create(newTask),
                    success ->
                        Log.i(TAG, ".onCreate(): created a task successfully"),
                    failure -> Log.e(TAG,"Failed to create a task", failure)
            );

            Toast.makeText(this, "New task saved!", Toast.LENGTH_SHORT).show();
        });
    }
}


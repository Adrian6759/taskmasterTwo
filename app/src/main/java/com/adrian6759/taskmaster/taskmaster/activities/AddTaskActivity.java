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
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.TaskStateEnum;
import com.amplifyframework.datastore.generated.model.TaskTeam;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class AddTaskActivity extends AppCompatActivity {
    public final static String TAG = "AddATaskActivity";
    Spinner taskStateSpinner;
    Spinner taskTeamSpinner;
    CompletableFuture<List<TaskTeam>> taskTeamFuture = new CompletableFuture<>();
    ArrayList<String> teamNames;
    ArrayList<TaskTeam> taskTeam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        taskStateSpinner = findViewById(R.id.TaskStateSpinner);
        taskTeamSpinner = findViewById(R.id.TaskTeamSpinner);

        teamNames = new ArrayList<>();
        taskTeam = new ArrayList<>();


       // CompleteableFuture
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
                setupSaveButton();

    }
    //Setup Spinner for enum
    public void setupTaskSpinners(){
        taskStateSpinner.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                TaskStateEnum.values()
        ));
        taskTeamSpinner.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                teamNames
        ));
    }
    public void setupSaveButton(){

        findViewById(R.id.AddTaskButton).setOnClickListener(v -> {
            String selectedTaskTeamStringName = taskTeamSpinner.getSelectedItem().toString();
            try {
                taskTeam = (ArrayList<TaskTeam>) taskTeamFuture.get();
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            } catch (ExecutionException ee) {
                ee.printStackTrace();
            }

            TaskTeam selectedTeam = taskTeam.stream().filter(team -> team.getName().equals(selectedTaskTeamStringName)).findAny().orElseThrow(RuntimeException::new);

            Task newTask = Task.builder()
                    .title(((EditText)findViewById(R.id.editTextTaskTitle)).getText().toString())
                    .body(((EditText)findViewById(R.id.editTextTaskBody)).getText().toString())
                    .state((TaskStateEnum) taskStateSpinner.getSelectedItem())
                    .taskTeam(selectedTeam)
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

        //Builder Pattern
//        TaskTeam taskTeam1 = TaskTeam.builder()
//                .name("TaskTeam1")
//                .build();
//        TaskTeam taskTeam2 = TaskTeam.builder()
//                .name("TaskTeam2")
//                .build();
//        TaskTeam taskTeam3 = TaskTeam.builder()
//                .name("TaskTeam3")
//                .build();
//        Amplify.API.mutate(
//                ModelMutation.create(taskTeam1),
//                success -> {},
//                failure -> {});

//        );

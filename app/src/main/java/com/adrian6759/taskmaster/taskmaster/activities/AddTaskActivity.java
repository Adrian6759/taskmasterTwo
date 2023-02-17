package com.adrian6759.taskmaster.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.adrian6759.taskmaster.R;
import com.adrian6759.taskmaster.taskmaster.database.TaskMasterDatabase;
import com.adrian6759.taskmaster.taskmaster.models.Task;

public class AddTaskActivity extends AppCompatActivity {
    TaskMasterDatabase taskMasterDatabase;
    Spinner taskStateSpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        taskStateSpinner = findViewById(R.id.TaskStateSpinner);

        taskMasterDatabase = Room.databaseBuilder(
                        getApplicationContext(),
                        TaskMasterDatabase.class,
                        MainActivity.DATABASE_NAME)
                .fallbackToDestructiveMigration() //Don't use this in production
                .allowMainThreadQueries()
                .build();
                setupTaskSpinner();
                setupSaveButton();

    }
    //Setup Spinner for enum
    public void setupTaskSpinner(){
        taskStateSpinner.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                Task.TaskStateEnum.values()
        ));
    }
    public void setupSaveButton(){

        findViewById(R.id.AddTaskButton).setOnClickListener(v -> {
            Task newTask = new Task(
                    ((EditText)findViewById(R.id.editTextTaskTitle)).getText().toString(),
                    ((EditText)findViewById(R.id.editTextTaskBody)).getText().toString(),
                    Task.TaskStateEnum.fromString(taskStateSpinner.getSelectedItem().toString())
            );

                    taskMasterDatabase.taskDao().insertATask(newTask);
            Toast.makeText(this, "New task saved!", Toast.LENGTH_SHORT).show();
        });
    }
}
//        Button addTaskButton = (Button) findViewById(R.id.AddTaskButton);
//        addTaskButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                TextView textViewAddTaskActivitySubmitted = (TextView) findViewById(R.id.textViewAddTaskActivitySubmitted);
//                textViewAddTaskActivitySubmitted.setVisibility(View.VISIBLE);
//            }
//        });

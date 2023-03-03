package com.adrian6759.taskmaster.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.adrian6759.taskmaster.R;
import com.adrian6759.taskmaster.taskmaster.adapter.TasksRecyclerViewAdapter;
import com.amplifyframework.core.Amplify;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class TaskDetail extends AppCompatActivity {
    public final static String TAG = "task_details";
        String taskTitle;
        String taskBody;
        String taskState;
        String taskS3ImageKey = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        consumeExtras();

        if (taskS3ImageKey != null && !taskS3ImageKey.isEmpty()){
        Amplify.Storage.downloadFile(
                taskS3ImageKey,
                new File(getApplication().getFilesDir(), taskS3ImageKey),
                success -> {
                    Log.i(TAG, "SUCCESS! Got the image with key: " + taskS3ImageKey);
                    ImageView taskImageView = findViewById(R.id.TaskDetailsImageView);
                    taskImageView.setImageBitmap(BitmapFactory.decodeFile(success.getFile().getPath()));
                },
                failure -> {
                    Log.e(TAG, "FAILED to get timage from S3 with key: " + taskS3ImageKey + "with error" + failure);
                }
        );

        }

    }
    public void consumeExtras(){

    Intent callingIntent = getIntent();
        if (callingIntent != null) {
            taskTitle = callingIntent.getStringExtra(TasksRecyclerViewAdapter.TASKS_TITLE_TAG);
            taskBody = callingIntent.getStringExtra(TasksRecyclerViewAdapter.TASKS_BODY_TAG);
            taskState = callingIntent.getStringExtra(TasksRecyclerViewAdapter.TASKS_STATE_TAG);
            taskS3ImageKey = callingIntent.getStringExtra(TasksRecyclerViewAdapter.TASK_IMAGE_KEY_TAG);
        }
        ((TextView)findViewById(R.id.activityTaskTitleViewText)).setText(taskTitle);
        ((TextView)findViewById(R.id.activityTaskBodyViewText)).setText(taskBody);
        ((TextView)findViewById(R.id.activityTaskStateViewText)).setText(taskState);

    }
}
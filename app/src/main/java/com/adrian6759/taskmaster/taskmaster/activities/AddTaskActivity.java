package com.adrian6759.taskmaster.taskmaster.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.adrian6759.taskmaster.R;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.TaskStateEnum;
import com.amplifyframework.datastore.generated.model.TaskTeam;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class AddTaskActivity extends AppCompatActivity {
    public final static String TAG = "AddATaskActivity";
    private String s3ImageKey = "";
    Spinner taskStateSpinner;
    Spinner taskTeamSpinner;
    CompletableFuture<List<TaskTeam>> taskTeamFuture = new CompletableFuture<>();
    ArrayList<String> teamNames;
    ArrayList<TaskTeam> taskTeam;
    ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        activityResultLauncher = getImagePickingActivityResultLauncher();
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
        setupAddImageButton();

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
   public void setupSaveButton() {
       findViewById(R.id.AddTaskButton).setOnClickListener(v -> {
           saveTask();
       });
   }
    public void saveTask() {
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
                    .s3ImageKey(s3ImageKey)
                    .build();

            Amplify.API.mutate(
                    ModelMutation.create(newTask),
                    success -> Log.i(TAG, "Created a task successfully"),
                    failure -> Log.e(TAG,"Failed to create a task", failure)
            );

            Toast.makeText(this, "New task saved!", Toast.LENGTH_SHORT).show();
    }
    public void setupAddImageButton(){
        findViewById(R.id.ChooseImageView).setOnClickListener(v -> {
            launchImageSelectionIntent();
        });

    }
    public void launchImageSelectionIntent(){
        Intent imageFilePickingIntent = new Intent(Intent.ACTION_GET_CONTENT);
        imageFilePickingIntent.setType("*/*");
        imageFilePickingIntent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/jpeg", "image/png"});
        activityResultLauncher.launch(imageFilePickingIntent);
    }
    private ActivityResultLauncher<Intent> getImagePickingActivityResultLauncher(){
        ActivityResultLauncher<Intent> imagePickingActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {

                    Uri pickedImageFileUri = result.getData().getData();
                    try {
                        InputStream pickedImageInputStream = getContentResolver().openInputStream(pickedImageFileUri);
                        String pickedImageFileName = getFileNameFromUri(pickedImageFileUri);
                        Log.i(TAG, "Succeeded in getting input stream! Filename is: " + pickedImageFileName);
                        uploadInputStreamToS3(pickedImageInputStream, pickedImageFileName, pickedImageFileUri);
                    } catch (FileNotFoundException fnfe) {
                        Log.e(TAG, "Could not get file from file picker" + fnfe.getMessage());
                    }
                    }

                }
        );
                return imagePickingActivityResultLauncher;
    }
    public void uploadInputStreamToS3(InputStream pickedImageInputStream, String pickedImageFileName, Uri pickedImageFileUri){
        Amplify.Storage.uploadInputStream(
                pickedImageFileName,
                pickedImageInputStream,
                success ->{
                    Log.i(TAG, "SUCCESS! Uploaded file to S3. Filename is: " + success.getKey());
                    s3ImageKey = pickedImageFileName;
                    ImageView taskImageView = findViewById(R.id.ChooseImageView);
                    InputStream pickedImageInputStreamCopy = null;
                    try {
                        pickedImageInputStreamCopy = getContentResolver().openInputStream(pickedImageFileUri);
                    } catch (FileNotFoundException fnfe) {
                        Log.e(TAG, "Could not get file stream from URI! " + fnfe.getMessage(), fnfe);
                    }
                    taskImageView.setImageBitmap(BitmapFactory.decodeStream(pickedImageInputStreamCopy));
                },
                failure ->{
                    Log.e(TAG, "Failed to upload File to S3 with filename: " + pickedImageFileName + "with error" + failure);
                }
        );
    }
    // Taken from https://stackoverflow.com/a/25005243/16889809
    @SuppressLint("Range")
    public String getFileNameFromUri(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
}



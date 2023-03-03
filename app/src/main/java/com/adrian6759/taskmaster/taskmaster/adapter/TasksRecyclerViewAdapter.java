package com.adrian6759.taskmaster.taskmaster.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adrian6759.taskmaster.R;
import com.adrian6759.taskmaster.taskmaster.activities.TaskDetail;
import com.amplifyframework.datastore.generated.model.Task;

import java.util.List;

//Make a class whose sole purpose is to manage RecyclerViews: a recyclerView adapter
public class TasksRecyclerViewAdapter extends RecyclerView.Adapter<TasksRecyclerViewAdapter.TasksViewHolder> {
    public static final String TASKS_TITLE_TAG = "tasksTitle";
    public static final String TASKS_BODY_TAG = "tasksBody";
    public static final String TASKS_STATE_TAG = "tasksState";
    public static final String TASK_IMAGE_KEY_TAG = "task_image_key";
    //In the RecyclerViewAdapter class, at the top:
    Context callingActivity;

    //Hand in some data items
    List<Task> tasksList;

    public TasksRecyclerViewAdapter(List<Task> tasksList, Context callingActivity) {
        this.tasksList = tasksList;
        this.callingActivity = callingActivity;
    }

    @NonNull
    @Override
    //In RecyclerViewAdapter.onCreateViewHolder()) Inflate Fragment
    public TasksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View tasksFragment = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_tasks, parent, false);
        //Attach fragment to ViewHolder
        return new TasksViewHolder(tasksFragment);
    }

    @Override
    //Bind data items to fragments inside of ViewHolders
    public void onBindViewHolder(@NonNull TasksViewHolder holder, int position) {
        TextView taskFragTitleView = holder.itemView.findViewById(R.id.TasksFragmentTextViewTitle);
        TextView tasksFragNameViewBody = holder.itemView.findViewById(R.id.TasksFragmentTextViewBody);
        TextView tasksFragNameViewState = holder.itemView.findViewById(R.id.TasksFragmentTextViewState);
        String tasksTitle = tasksList.get(position).getTitle();
        String tasksBody = tasksList.get(position).getBody();
        String tasksState = String.valueOf(tasksList.get(position).getState());
        String tasksS3ImageKey = tasksList.get(position).getS3ImageKey();
        taskFragTitleView.setText((position+1) + ". " + tasksTitle);
        tasksFragNameViewBody.setText(tasksBody);
        tasksFragNameViewState.setText(tasksState);
        View taskViewHolder = holder.itemView;

        taskViewHolder.setOnClickListener(v -> {
            Intent goToTaskDetailsIntent = new Intent(callingActivity, TaskDetail.class);
            goToTaskDetailsIntent.putExtra(TASKS_TITLE_TAG, tasksTitle);
            goToTaskDetailsIntent.putExtra(TASKS_BODY_TAG, tasksBody);
            goToTaskDetailsIntent.putExtra(TASKS_STATE_TAG, tasksState);
            goToTaskDetailsIntent.putExtra(TASK_IMAGE_KEY_TAG, tasksS3ImageKey);
            callingActivity.startActivity(goToTaskDetailsIntent);
        });
    }

    @Override
    public int getItemCount() {
//        return 10;
        //Make the size of the list dynamic
        return tasksList.size();
    }


    //Make a viewHolder class to hold a Fragment
    public static class TasksViewHolder extends RecyclerView.ViewHolder {

        public TasksViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}


package com.adrian6759.taskmaster.taskmaster.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

//Make a data class
@Entity
public class Task {
    @PrimaryKey(autoGenerate = true)
    public Long id;
    private String title;
    private String body;
    private TaskStateEnum state;

    public Task(String title, String body, TaskStateEnum state) {
        this.title = title;
        this.body = body;
        this.state = state;

    }

    public Long getId() {return id;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public TaskStateEnum getState() {
        return state;
    }

    public void setState(TaskStateEnum state) {
        this.state = state;
    }

    public enum TaskStateEnum {
        NEW("New"),
        ASSIGNED("Assigned"),
        INPROGRESS("In Progress"),
        COMPLETE("Complete");

        private final String taskState;

        TaskStateEnum(String taskState) {
            this.taskState = taskState;
        }

        public static TaskStateEnum fromString(String possibleTaskState) {
            for (TaskStateEnum state : TaskStateEnum.values()) {
                if (state.taskState.equals(possibleTaskState)){
                    return state;
                }
            }
            return null;
        }
        @Override
        public String toString() {
            return "TaskStateEnum{" +
                    "taskState='" + taskState + '\'' +
                    '}';
        }
    }
}

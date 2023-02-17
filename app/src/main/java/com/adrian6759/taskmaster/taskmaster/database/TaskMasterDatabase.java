package com.adrian6759.taskmaster.taskmaster.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.adrian6759.taskmaster.taskmaster.dao.TaskDao;
import com.adrian6759.taskmaster.taskmaster.models.Task;

//Setup the database and make it abstract
@Database(entities = {Task.class}, version = 1)
public abstract class TaskMasterDatabase extends RoomDatabase {
    //Add the Dao(s)
public abstract TaskDao taskDao();
}

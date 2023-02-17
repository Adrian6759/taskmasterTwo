package com.adrian6759.taskmaster.taskmaster.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.adrian6759.taskmaster.taskmaster.models.Task;

import java.util.List;

@Dao
public interface TaskDao {
    @Insert
    public void insertATask(Task task);

    //find all
    @Query("SELECT * FROM Task")
    public List<Task> findAll();
    //findById
    @Query("SELECT * FROM Task WHERE id = :id")
    public Task findById(Long id);

    //findAllByState
    @Query("SELECT * FROM Task WHERE state = :state")
    public Task findAllByState(Task.TaskStateEnum state);

    //delete
    @Delete
    public void delete(Task task);

    //update
    @Update
    public void update(Task task);
}

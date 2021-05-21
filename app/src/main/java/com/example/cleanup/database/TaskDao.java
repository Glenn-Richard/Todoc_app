package com.example.cleanup.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.cleanup.model.Task;

import java.util.List;

@Dao
public interface TaskDao {

    @Insert
    void insertTask(Task task);

    @Delete
    void deleteTask(Task task);

    @Query("SELECT * FROM Task WHERE projectId = :projectId")
    LiveData<List<Task>> getByProjectId(long projectId);

    @Query("SELECT * FROM Task")
    LiveData<List<Task>> getTasks();

    @Query("SELECT  * FROM Task ORDER BY id DESC")
    LiveData<List<Task>> getTaskByDesc();
}

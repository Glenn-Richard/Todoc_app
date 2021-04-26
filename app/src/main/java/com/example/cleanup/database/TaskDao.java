package com.example.cleanup.database;

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

    @Query("SELECT * FROM Task")
    List<Task> getTasks();
}

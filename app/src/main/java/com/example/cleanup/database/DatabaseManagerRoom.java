package com.example.cleanup.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.cleanup.model.Project;
import com.example.cleanup.model.Task;

@Database(entities = {Project.class, Task.class},version = 1,exportSchema = false)
public abstract class DatabaseManagerRoom extends RoomDatabase {

    private static volatile  DatabaseManagerRoom INSTANCE;


    public abstract ProjectDao projectDao();
    public abstract TaskDao taskDao();
}

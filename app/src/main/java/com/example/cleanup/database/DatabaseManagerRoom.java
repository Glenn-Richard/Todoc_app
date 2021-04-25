package com.example.cleanup.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.cleanup.model.Project;
import com.example.cleanup.model.Task;

@Database(entities = {Project.class, Task.class},version = 1,exportSchema = false)
public abstract class DatabaseManagerRoom extends RoomDatabase {

    private static volatile  DatabaseManagerRoom INSTANCE;

    public DatabaseManagerRoom getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (DatabaseManagerRoom.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            DatabaseManagerRoom.class, "MyDatabase.db")
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}

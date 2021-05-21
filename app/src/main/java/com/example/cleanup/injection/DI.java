package com.example.cleanup.injection;

import android.content.Context;

import com.example.cleanup.database.DatabaseManagerRoom;
import com.example.cleanup.repository.ProjectRepository;
import com.example.cleanup.repository.TaskRepository;
import com.example.cleanup.view_model.ViewModelFactory;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DI {

    public static TaskRepository provideTaskRepository(Context context){
        DatabaseManagerRoom database = DatabaseManagerRoom.getInstance(context);
        return new TaskRepository(database.taskDao());
    }

    public static ProjectRepository provideProjectRepository(Context context){
        DatabaseManagerRoom database = DatabaseManagerRoom.getInstance(context);
        return  new ProjectRepository(database.projectDao());
    }

    public static Executor provideExecutor(){
        return Executors.newSingleThreadExecutor();
    }

    public static ViewModelFactory provideViewModelFactory(Context context){
        return new ViewModelFactory(
                provideTaskRepository(context),
                provideProjectRepository(context),
                provideExecutor());
    }
}

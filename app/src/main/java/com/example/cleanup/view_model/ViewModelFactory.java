package com.example.cleanup.view_model;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.example.cleanup.repository.ProjectRepository;
import com.example.cleanup.repository.TaskRepository;

import java.util.concurrent.Executor;

public class ViewModelFactory implements ViewModelProvider.Factory {

    TaskRepository taskRepository;
    ProjectRepository projectRepository;
    Executor executor;

    public ViewModelFactory(TaskRepository taskRepository, ProjectRepository projectRepository, Executor executor) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.executor = executor;
    }

    @NonNull
    @Override
    public <T extends androidx.lifecycle.ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ViewModel.class)) {
            return (T) new ViewModel(projectRepository,taskRepository,executor);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}

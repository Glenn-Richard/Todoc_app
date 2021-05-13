package com.example.cleanup.view_model;

import androidx.lifecycle.LiveData;

import com.example.cleanup.model.Project;
import com.example.cleanup.model.Task;
import com.example.cleanup.repository.ProjectRepository;
import com.example.cleanup.repository.TaskRepository;

import java.util.List;
import java.util.concurrent.Executor;

public class ViewModel extends androidx.lifecycle.ViewModel {

    ProjectRepository projectRepository;
    TaskRepository taskRepository;
    Executor executor;

    public ViewModel(ProjectRepository projectRepository, TaskRepository taskRepository, Executor executor) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.executor = executor;
    }

    //GETTERS

    public LiveData<List<Project>> getProjects(){
        return projectRepository.getProjects();
    }
    public LiveData<List<Task>> getTask(){
        return taskRepository.getTasks();
    }

    //INSERT

    public void insertProject(Project project){
        executor.execute(() ->
                projectRepository.insertProject(project));
    }

    public void insertTask(Task task){
        executor.execute(() ->
                taskRepository.insertTask(task));
    }

    //DELETE

    public void deleteProject(Project project){
        executor.execute(() ->
                projectRepository.deleteProject(project));
    }

    public void deleteTask(Task task){
        executor.execute(() ->
                taskRepository.deleteTask(task));
    }
}

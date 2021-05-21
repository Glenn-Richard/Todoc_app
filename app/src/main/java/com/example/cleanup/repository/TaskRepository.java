package com.example.cleanup.repository;

import androidx.lifecycle.LiveData;

import com.example.cleanup.database.TaskDao;
import com.example.cleanup.model.Task;

import java.util.List;

public class TaskRepository {

    TaskDao taskDao;

    public TaskRepository(TaskDao taskRepository) {
        this.taskDao = taskRepository;
    }

    public void insertTask(Task task) {
        taskDao.insertTask(task);
    }

    public void deleteTask(Task task) {
        taskDao.deleteTask(task);
    }

    public LiveData<List<Task>> getByIdProject(int projectId) {
        return taskDao.getByProjectId(projectId);
    }

    public LiveData<List<Task>> getTasks() {
        return taskDao.getTasks();
    }

    public LiveData<List<Task>> getTasksByDesc() {
        return taskDao.getTaskByDesc();
    }
}

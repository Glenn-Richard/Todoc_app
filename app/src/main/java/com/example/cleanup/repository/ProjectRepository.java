package com.example.cleanup.repository;

import androidx.lifecycle.LiveData;

import com.example.cleanup.database.ProjectDao;
import com.example.cleanup.model.Project;

import java.util.List;

public class ProjectRepository {

    private ProjectDao projectDao;

    public ProjectRepository(ProjectDao projectDao) {
        this.projectDao = projectDao;
    }

    public void insertProject(Project project){ projectDao.insertProject(project);}

    public void deleteProject(Project project){ projectDao.deleteProject(project);}

    public LiveData<List<Project>> getProjects(){ return this.projectDao.getProjects();}
}

package com.example.cleanup.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.cleanup.model.Project;

import java.util.List;

@Dao
public interface ProjectDao {

    @Insert
    void insertProject(Project project);

    @Delete
    void deleteProject(Project project);

    @Query("SELECT * FROM Project")
    List<Project> getProjects();
}

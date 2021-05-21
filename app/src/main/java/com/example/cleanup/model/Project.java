package com.example.cleanup.model;


import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.cleanup.R;

import java.util.Arrays;
import java.util.List;

/**
 * <p>Models for project in which tasks are included.</p>
 *
 * @author Gaëtan HERFRAY
 */

@Entity(tableName = "Project")
public class Project {



    @PrimaryKey()
    private long id;

    @NonNull
    private String name;

    @ColorInt
    private int color;

    public Project(long id, @NonNull String name, int color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    @NonNull
    public String getName() {
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public int getColor() {
        return color;
    }
    public void setColor(int color){
        this.color = color;
    }

    public Project() {
    }

    @NonNull
    public static List<Project> getAllProjects = Arrays.asList(
            new Project(1, "Projet Tartampion", R.mipmap.projet_tartampion),
            new Project(2, "Projet Lucidia", R.mipmap.projet_lucidia),
            new Project(3, "Projet Circus", R.mipmap.projet_circus)
    );





    @Nullable
    public static Project getProjectById(long id) {
        Project project_ = new Project();
        for (Project project : getAllProjects) {
            if (project.getId() == id){
                project_ =  project;
            }
        }
        return project_;
    }

    @Override
    @NonNull
    public String toString() {
        return getName();
    }


}

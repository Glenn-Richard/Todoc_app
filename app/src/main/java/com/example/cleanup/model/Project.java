package com.example.cleanup.model;


import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.cleanup.R;

/**
 * <p>Models for project in which tasks are included.</p>
 *
 * @author Gaëtan HERFRAY
 */

@Entity(tableName = "Project")
public class Project {

    public void setId(long id) {
        this.id = id;
    }

    @PrimaryKey(autoGenerate = true)
    private long id;

    public Project(@NonNull String name, int color) {
        this.name = name;
        this.color = color;
    }

    @NonNull
    private String name;


    @ColorInt
    private int color;


    public long getId() {
        return id;
    }
    @NonNull
    public String getName() {
        return name;
    }
    public int getColor() {
        return color;
    }

    public void setName(String name){
        this.name = name;
    }
    public void setColor(int color){
        this.color = color;
    }


    @NonNull
    public static Project[] getAllProjects() {
        return new Project[]{
                new Project( "Projet Tartampion", R.mipmap.projet_tartampion),
                new Project( "Projet Lucidia", R.mipmap.projet_lucidia),
                new Project( "Projet Circus", R.mipmap.projet_circus),
        };
    }


    @Nullable
    public static Project getProjectById(long id) {
        for (Project project : getAllProjects()) {
            if (project.id == id)
                return project;
        }
        return null;
    }

    @Override
    @NonNull
    public String toString() {
        return getName();
    }


}

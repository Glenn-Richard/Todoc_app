package com.example.cleanup.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.Nullable;

import java.util.Comparator;

/**
 * <p>Model for the tasks of the application.</p>
 *
 * @author Gaëtan HERFRAY
 */

@Entity(tableName = "Task",
        foreignKeys = @ForeignKey(
                entity = Project.class,
                parentColumns = "id",
                childColumns = "projectId"))
public class Task {

    @PrimaryKey(autoGenerate = true)
    private long id;

    public void setId(long id) {
        this.id = id;
    }

    private long projectId;

    @NonNull
    private String name;

    private long creationTimestamp;

    public Task(long projectId, @NonNull String name, long creationTimestamp) {
        this.setProjectId(projectId);
        this.setName(name);
        this.setCreationTimestamp(creationTimestamp);
    }

    /**
     * Returns the unique identifier of the task.
     *
     * @return the unique identifier of the task
     */
    public long getId() {
        return id;
    }

    private void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    public long getProjectId() { return projectId; }

    @Nullable
    public Project getProject() {
        return Project.getProjectById(projectId);
    }


    @NonNull
    public String getName() {
        return name;
    }
    private void setName(@NonNull String name) {
        this.name = name;
    }


    private void setCreationTimestamp(long creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }
    public long getCreationTimestamp() {
        return creationTimestamp;
    }


    public static class TaskProjectComparator implements Comparator<Task> {
        @Override
        public int compare(Task left, Task right) {
            return left.name.compareTo(right.name);
        }
    }

    /**
     * Comparator to sort task from Z to A
     */
    public static class TaskZAComparator implements Comparator<Task> {
        @Override
        public int compare(Task left, Task right) {
            return right.name.compareTo(left.name);
        }
    }

    /**
     * Comparator to sort task from last created to first created
     */
    public static class TaskRecentComparator implements Comparator<Task> {
        @Override
        public int compare(Task left, Task right) {
            return (int) (right.creationTimestamp - left.creationTimestamp);
        }
    }

    /**
     * Comparator to sort task from first created to last created
     */
    public static class TaskOldComparator implements Comparator<Task> {
        @Override
        public int compare(Task left, Task right) {
            return (int) (left.creationTimestamp - right.creationTimestamp);
        }
    }
}

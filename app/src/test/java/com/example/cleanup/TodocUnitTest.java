package com.example.cleanup;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.cleanup.database.DatabaseManagerRoom;
import com.example.cleanup.model.Task;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;


public class TodocUnitTest extends Assert {

    DatabaseManagerRoom database;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setup() throws Exception{
         database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().getContext(),
                 DatabaseManagerRoom.class)
                 .allowMainThreadQueries()
                 .build();
    }
    @After
    public void closeDb(){
        database.close();
    }
    @Test
    public void getTasksWithSuccess(){
        assertEquals(0, Objects.requireNonNull(database.taskDao().getTasks().getValue()).size());
        List<Task> tasks = Arrays.asList(
                new Task(Objects.requireNonNull(database.projectDao().getProjects().getValue()).get(0).getId(),"task1",new Date().getTime()),
                new Task(Objects.requireNonNull(database.projectDao().getProjects().getValue()).get(0).getId(),"task2",new Date().getTime()),
                new Task(Objects.requireNonNull(database.projectDao().getProjects().getValue()).get(0).getId(),"task3",new Date().getTime())
        );
        int i = 0;
        while(i<tasks.size()){
            database.taskDao().insertTask(tasks.get(i));
            i++;
        }
        Assert.assertEquals(3, Objects.requireNonNull(database.taskDao().getTasks().getValue()).size());
        database.clearAllTables();
    }

    @Test
    public void insertTaskWithSuccess(){
        assertEquals(0, Objects.requireNonNull(database.taskDao().getTasks().getValue()).size());
        long timeCreation = new Date().getTime();
        Task task = new Task(Objects.requireNonNull(database.projectDao().getProjects().getValue()).get(0).getId(),"nettoyer cuisine",timeCreation);
        database.taskDao().insertTask(task);
        assertEquals(1, Objects.requireNonNull(database.taskDao().getTasks().getValue()).size());
        assertTrue(Objects.requireNonNull(database.taskDao().getTasks().getValue()).contains(task));
    }
}
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
        assertEquals(0,database.taskDao().getTasks().size());
        List<Task> tasks = Arrays.asList(
                new Task(database.projectDao().getProjects().get(0).getId(),"task1",new Date().getTime()),
                new Task(database.projectDao().getProjects().get(0).getId(),"task2",new Date().getTime()),
                new Task(database.projectDao().getProjects().get(0).getId(),"task3",new Date().getTime())
        );
        int i = 0;
        while(i<tasks.size()){
            database.taskDao().insertTask(tasks.get(i));
            i++;
        }
        Assert.assertEquals(3,database.taskDao().getTasks().size());
        database.clearAllTables();
    }

    @Test
    public void insertTaskWithSuccess(){
        assertEquals(0,database.taskDao().getTasks().size());
        long timeCreation = new Date().getTime();
        Task task = new Task(database.projectDao().getProjects().get(0).getId(),"nettoyer cuisine",timeCreation);
        database.taskDao().insertTask(task);
        assertEquals(1,database.taskDao().getTasks().size());
        assertTrue(database.taskDao().getTasks().contains(task));
    }
}
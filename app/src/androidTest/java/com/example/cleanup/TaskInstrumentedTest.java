package com.example.cleanup;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.cleanup.database.DatabaseManagerRoom;
import com.example.cleanup.model.Project;
import com.example.cleanup.model.Task;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class TaskInstrumentedTest {
    // FOR DATA
    private DatabaseManagerRoom database;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void initDb() throws Exception {
        this.database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().getContext(),
                 DatabaseManagerRoom.class)
                .allowMainThreadQueries()
                .build();
    }

    @After
    public void closeDb() throws Exception {
        database.close();
    }

    @Test
    public void checkInsertAndReadTask(){
        long timeCreation = new Date().getTime();
        Project project = new Project("toto",R.mipmap.projet_circus);
        database.projectDao().insertProject(project);
        Task task = new Task(database.projectDao().getProjects().get(0).getId(),"nettoyer cuisine",timeCreation);
        database.taskDao().insertTask(task);
        Assert.assertTrue(this.database.taskDao().getTasks().get(0).getName().contains("nettoyer cuisine")&&
                this.database.taskDao().getTasks().get(0).getCreationTimestamp()==timeCreation);
        database.clearAllTables();
    }

    @Test
    public void checkTaskIsDelete(){
        long timeCreation = new Date().getTime();
        int initSize = database.taskDao().getTasks().size();
        Project project = new Project("project 2",R.mipmap.projet_circus);
        database.projectDao().insertProject(project);
        Task task = new Task(database.projectDao().getProjects().get(0).getId(),"laver carreaux",timeCreation);
        database.taskDao().insertTask(task);
        Assert.assertTrue(database.taskDao().getTasks().size()!=initSize
        && database.taskDao().getTasks().get(0).getCreationTimestamp()==timeCreation);
        database.taskDao().deleteTask(database.taskDao().getTasks().get(0));
        Assert.assertTrue(database.taskDao().getTasks().size()==initSize);
        Assert.assertFalse(database.taskDao().getTasks().contains(task));
        database.clearAllTables();
    }

    @Test
    public void checkGetAllTasks(){
        Project project = new Project("Projet 1",R.mipmap.projet_circus);
        database.projectDao().insertProject(project);
        Assert.assertEquals(0,database.taskDao().getTasks().size());
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

}
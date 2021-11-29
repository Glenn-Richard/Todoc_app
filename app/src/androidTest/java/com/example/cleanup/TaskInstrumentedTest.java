package com.example.cleanup;

import android.content.Context;
import android.util.Log;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.cleanup.database.DatabaseManagerRoom;
import com.example.cleanup.model.Project;
import com.example.cleanup.model.Task;
import com.example.cleanup.utils.LiveDataTestUtil;

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
    public void initDb() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        this.database = Room.databaseBuilder(context.getApplicationContext(),
                DatabaseManagerRoom.class, "database.db")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
    }

    @After
    public void closeDb() {
        database.clearAllTables();
        database.close();
    }

    @Test
    public void checkInsertAndReadTask() throws InterruptedException {
        long timeCreation = new Date().getTime();
        Project project = new Project(1,"projet 1",R.mipmap.projet_circus);
        database.projectDao().insertProject(project);
        Task task = new Task(project.getId(),"nettoyer cuisine",timeCreation);
        database.taskDao().insertTask(task);
        Assert.assertEquals("nettoyer cuisine", LiveDataTestUtil.getValue(database.taskDao().getTasks()).get(0).getName());
    }

    @Test
    public void checkTaskIsDelete() throws InterruptedException {
        long timeCreation = new Date().getTime();
        Project project = new Project(1,"projet 1",R.mipmap.projet_circus);
        database.projectDao().insertProject(project);
        int initSize = LiveDataTestUtil.getValue(database.taskDao().getTasks()).size();
        Assert.assertEquals(0,initSize);
        Task task = new Task(project.getId(),"laver carreaux",timeCreation);
        database.taskDao().insertTask(task);
        Assert.assertEquals("laver carreaux", LiveDataTestUtil.getValue(database.taskDao().getTasks()).get(0).getName());
        Assert.assertTrue(LiveDataTestUtil.getValue(database.taskDao().getTasks()).size()!=initSize);
        Assert.assertEquals(1,LiveDataTestUtil.getValue(database.taskDao().getTasks()).size());
        database.taskDao().deleteTask(LiveDataTestUtil.getValue(database.taskDao().getTasks()).get(0));
        Assert.assertEquals(initSize, LiveDataTestUtil.getValue(database.taskDao().getTasks()).size());
        database.clearAllTables();
    }
//simpleumlce
    @Test
    public void checkGetAllTasks() throws InterruptedException {

        Assert.assertEquals(0,LiveDataTestUtil.getValue(database.taskDao().getTasks()).size());

        Project project = new Project(1,"projet 1",R.mipmap.projet_circus);
        database.projectDao().insertProject(project);
        database.taskDao().insertTask(new Task(project.getId(),"task1",new Date().getTime()));
        database.taskDao().insertTask(new Task(project.getId(),"task2",new Date().getTime()));
        database.taskDao().insertTask(new Task(project.getId(),"task3",new Date().getTime()));

        Assert.assertEquals(3,LiveDataTestUtil.getValue(database.taskDao().getTasks()).size());
        database.clearAllTables();
    }

}
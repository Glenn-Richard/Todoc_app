package com.example.cleanup;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.cleanup.database.DatabaseManagerRoom;
import com.example.cleanup.model.Project;
import com.example.cleanup.utils.LiveDataTestUtil;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class ProjectInstrumentedTest {
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
    public void checkProjectIsInserted() throws InterruptedException {
        Project project = new Project(0,"Projet 1",R.mipmap.projet_lucidia);
        Assert.assertEquals(0, LiveDataTestUtil.getValue(database.projectDao().getProjects()).size());
        database.projectDao().insertProject(project);
        Assert.assertEquals(1,LiveDataTestUtil.getValue(database.projectDao().getProjects()).size());
        Assert.assertEquals("Projet 1", LiveDataTestUtil.getValue(database.projectDao().getProjects()).get(0).getName());
    }

    @Test
    public void checkProjectIsDeleted() throws InterruptedException {
        Project project = new Project(0,"Projet 1",R.mipmap.projet_lucidia);
        Assert.assertEquals(0,LiveDataTestUtil.getValue(database.projectDao().getProjects()).size());
        database.projectDao().insertProject(project);
        Assert.assertEquals(1,LiveDataTestUtil.getValue(database.projectDao().getProjects()).size());
        Assert.assertEquals("Projet 1", LiveDataTestUtil.getValue(database.projectDao().getProjects()).get(0).getName());
        database.projectDao().deleteProject(project);
        Assert.assertTrue(LiveDataTestUtil.getValue(database.projectDao().getProjects()).isEmpty());
    }

}

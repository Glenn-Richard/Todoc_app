package com.example.cleanup;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.cleanup.database.DatabaseManagerRoom;
import com.example.cleanup.model.Project;

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
    public void checkProjectIsInserted(){
        Project project = new Project("Projet 1",R.mipmap.projet_lucidia);
        Assert.assertEquals(0,database.projectDao().getProjects().size());
        database.projectDao().insertProject(project);
        Assert.assertEquals(1,database.projectDao().getProjects().size());
        Assert.assertTrue(database.projectDao().getProjects().get(0).getName().equals("Projet 1")
        && database.projectDao().getProjects().get(0).getColor()==R.mipmap.projet_lucidia);
    }
    @Test
    public void checkProjectIsDeleted(){
        Project project = new Project("Projet 1",R.mipmap.projet_lucidia);
        Assert.assertEquals(0,database.projectDao().getProjects().size());
        database.projectDao().insertProject(project);
        Assert.assertEquals(1,database.projectDao().getProjects().size());
        Assert.assertTrue(database.projectDao().getProjects().get(0).getName().equals("Projet 1")
                && database.projectDao().getProjects().get(0).getColor()==R.mipmap.projet_lucidia);
        database.projectDao().deleteProject(database.projectDao().getProjects().get(0));
        Assert.assertEquals(0,database.projectDao().getProjects().size());
    }

}

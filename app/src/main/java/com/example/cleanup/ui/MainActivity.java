package com.example.cleanup.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cleanup.R;
import com.example.cleanup.database.DatabaseManagerRoom;
import com.example.cleanup.model.Project;
import com.example.cleanup.model.Task;
import com.example.cleanup.repository.ProjectRepository;
import com.example.cleanup.repository.TaskRepository;
import com.example.cleanup.view_model.ViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * <p>Home activity of the application which is displayed when the user opens the app.</p>
 * <p>Displays the list of tasks.</p>
 *
 * @author Gaëtan HERFRAY
 */
public class MainActivity extends AppCompatActivity implements TasksAdapter.DeleteTaskListener {

    private List<Project> PROJECTS = new ArrayList<>();

    private ArrayList<Task>  tasks = new ArrayList<>();

    ArrayList<Task> projects_sorted = new ArrayList<>();

    public static DatabaseManagerRoom database;

    private TasksAdapter adapter;

    ViewModel viewModel;

    private ProjectRepository projectRepository;
    private TaskRepository taskRepository;
    private Executor executor;

    @NonNull
    private SortMethod sortMethod = SortMethod.NONE;

    @Nullable
    public AlertDialog dialog = null;

    @Nullable
    private EditText dialogEditText = null;

    @Nullable
    private Spinner dialogSpinner = null;

    @NonNull
    private RecyclerView listTasks;

    @NonNull
    private TextView lblNoTasks;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        lblNoTasks = findViewById(R.id.lbl_no_task);

        setViewModel();
        setCurrentProject();
        setTasks();
        setRecyclerView();

        findViewById(R.id.fab_add_task).setOnClickListener(view -> showAddTaskDialog());
        updateTasks();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actions, menu);
        return true;
    }

    //FILTER
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        setTasks();
        projects_sorted.addAll(tasks);

        if (id == R.id.projet_tartampion||id == R.id.projet_Lucidia||id == R.id.projet_Circus) {
            adapter.updateTasks(filterTasksByProject(tasks,id));
        } else if (id == R.id.filter_oldest_first) {
            sortMethod = SortMethod.OLD_FIRST;
            updateTasks();
        } else if (id == R.id.filter_recent_first) {
            sortMethod = SortMethod.RECENT_FIRST;
            updateTasks();
        }


        return super.onOptionsItemSelected(item);
    }
    /**
     * List of all possible sort methods for task
     */
    private enum SortMethod {
        /**
         * Lastly created first
         */
        RECENT_FIRST,
        /**
         * First created first
         */
        OLD_FIRST,
        /**
         * No sort
         */
        NONE
    }
    //SETTERS

    private void setRecyclerView() {
        listTasks = findViewById(R.id.list_tasks);

        adapter = new TasksAdapter(tasks, this);

        listTasks.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        listTasks.setAdapter(adapter);
    }

   private void setViewModel() {
        database = DatabaseManagerRoom.getInstance(this);
        projectRepository = new ProjectRepository(database.projectDao());
        taskRepository = new TaskRepository(database.taskDao());
        executor = Executors.newSingleThreadExecutor();
        viewModel = ViewModelProviders.of(this,
                (ViewModelProvider.Factory) new ViewModel(projectRepository,taskRepository,executor))
                .get(ViewModel.class);
    }

    private void setCurrentProject() {
        viewModel.getProjects().observe(this, projects -> setProjects(projects));
    }

    private void setProjects(List<Project> projects) {
        PROJECTS.clear();
        PROJECTS.addAll(projects);
    }

    private void setTasks() {
        viewModel.getTask().observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                MainActivity.this.tasks.addAll(tasks);
            }
        });
    }

    //DATA MANAGEMENT
    private void addTask(@NonNull Task task) {
        viewModel.insertTask(task);
        tasks.add(task);
        updateTasks();
    }

    /**
     * Updates the list of tasks in the UI
     */
    private void updateTasks() {
        if (tasks.size() == 0) {
            lblNoTasks.setVisibility(View.VISIBLE);
            listTasks.setVisibility(View.GONE);
        } else {
            lblNoTasks.setVisibility(View.GONE);
            listTasks.setVisibility(View.VISIBLE);
            switch (sortMethod) {
                case RECENT_FIRST:
                    Collections.sort(tasks, new Task.TaskRecentComparator());
                    break;
                case OLD_FIRST:
                    Collections.sort(tasks, new Task.TaskOldComparator());
                    break;
            }
            adapter.updateTasks(tasks);
        }
    }

    @Override
    public void onDeleteTask(Task task) {
        deleteTask(task);
        updateTasks();
    }

    private void deleteTask(Task task) {
        viewModel.deleteTask(task);
        tasks.remove(task);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        database.close();
    }

    //ALERT DIALOG
    @NonNull
    private AlertDialog getAddTaskDialog() {
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this, R.style.Dialog);

        alertBuilder.setTitle(R.string.add_task);
        alertBuilder.setView(R.layout.dialog_add_task);
        alertBuilder.setPositiveButton(R.string.add, null);
        alertBuilder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                dialogEditText = null;
                dialogSpinner = null;
                dialog = null;
            }
        });

        dialog = alertBuilder.create();

        // This instead of listener to positive button in order to avoid automatic dismiss
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        onPositiveButtonClick(dialog);
                    }
                });
            }
        });

        return dialog;
    }

    /**
     * Sets the data of the Spinner with projects to associate to a new task
     */
    private void populateDialogSpinner() {
        final ArrayAdapter<Project> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, PROJECTS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (dialogSpinner != null) {
            dialogSpinner.setAdapter(adapter);
        }
    }


    /**
     * Called when the user clicks on the positive button of the Create Task Dialog.
     *
     * @param dialogInterface the current displayed dialog
     */
    private void onPositiveButtonClick(DialogInterface dialogInterface) {
        // If dialog is open
        if (dialogEditText != null && dialogSpinner != null) {
            // Get the name of the task
            String taskName = dialogEditText.getText().toString();

            // Get the selected project to be associated to the task
            Project taskProject = null;
            if (dialogSpinner.getSelectedItem() instanceof Project) {
                taskProject = (Project) dialogSpinner.getSelectedItem();
            }

            // If a name has not been set
            if (taskName.trim().isEmpty()) {
                dialogEditText.setError(getString(R.string.empty_task_name));
            }
            // If both project and name of the task have been set
            else if (taskProject != null) {

                Task task = new Task(
                        taskProject.getId(),
                        taskName,
                        new Date().getTime()
                );

                addTask(task);

                dialogInterface.dismiss();
            }
            // If name has been set, but project has not been set (this should never occur)
            else{
                dialogInterface.dismiss();
            }
        }
        // If dialog is already closed
        else {
            dialogInterface.dismiss();
        }
    }

    /**
     * Shows the Dialog for adding a Task
     */
    private void showAddTaskDialog() {
        final AlertDialog dialog = getAddTaskDialog();

        dialog.show();

        dialogEditText = dialog.findViewById(R.id.txt_task_name);
        dialogSpinner = dialog.findViewById(R.id.project_spinner);

        populateDialogSpinner();
    }

    public ArrayList<Task> filterTasksByProject(List<Task> taskList, int id){
        ArrayList<Task> sorted_tasks = new ArrayList<>();
        if (id == R.id.projet_tartampion){
            for (int i=0;i<taskList.size();i++){
                if (taskList.get(i).getProjectId()==1){
                    sorted_tasks.add(taskList.get(i));
                }
            }
            return sorted_tasks;
        }
        if (id == R.id.projet_Lucidia){
            for (int i=0;i<taskList.size();i++){
                if (taskList.get(i).getProjectId()==2){
                    sorted_tasks.add(taskList.get(i));
                }
            }
            return sorted_tasks;
        }
        if (id == R.id.projet_Circus){
            for (int i=0;i<taskList.size();i++){
                if (taskList.get(i).getProjectId()==3){
                    sorted_tasks.add(taskList.get(i));
                }
            }
            return sorted_tasks;
        }
        return sorted_tasks;
    }
}

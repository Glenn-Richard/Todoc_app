package com.example.cleanup.ui;

import android.annotation.SuppressLint;
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
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cleanup.R;
import com.example.cleanup.injection.DI;
import com.example.cleanup.model.Project;
import com.example.cleanup.model.Task;
import com.example.cleanup.view_model.ViewModel;
import com.example.cleanup.view_model.ViewModelFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.cleanup.R.id.projet_tartampion;

/**
 * <p>Home activity of the application which is displayed when the user opens the app.</p>
 * <p>Displays the list of tasks.</p>
 *
 * @author Gaëtan HERFRAY
 */
public class MainActivity extends AppCompatActivity implements TasksAdapter.DeleteTaskListener {

    @Nullable
    public AlertDialog dialog = null;
    ViewModel viewModel;
    private final List<Project> projects = new ArrayList<>();
    private TasksAdapter adapter;
    @Nullable
    private EditText dialogEditText = null;

    @Nullable
    private Spinner dialogSpinner = null;

    private RecyclerView listTasks;

    private TextView lblNoTasks;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        lblNoTasks = findViewById(R.id.lbl_no_task);

        setViewModel();
        setCurrentProject();
        setObservers();
        setRecyclerView();

        findViewById(R.id.fab_add_task).setOnClickListener(v -> showAddTaskDialog()
        );

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


        SortMethod sortMethod;
        if (id == projet_tartampion || id == R.id.projet_Lucidia || id == R.id.projet_Circus) {
            filterByProject(id);
        } else if (id == R.id.filter_oldest_first) {
            sortMethod = SortMethod.OLD_FIRST;
            filterMethod(sortMethod);
        } else if (id == R.id.filter_recent_first) {
            sortMethod = SortMethod.RECENT_FIRST;
            filterMethod(sortMethod);
        }


        return super.onOptionsItemSelected(item);
    }

    private void setRecyclerView() {
        listTasks = findViewById(R.id.list_tasks);
        adapter = new TasksAdapter(new ArrayList<>(), this);

        listTasks.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        listTasks.setAdapter(adapter);
    }
    //SETTERS

    private void setViewModel() {

        ViewModelFactory viewModelFactory = DI.provideViewModelFactory(this);
        viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(ViewModel.class);
    }

    private void setCurrentProject() {
        viewModel.getProjects().observe(this, this::setProjects);
    }

    private void setProjects(List<Project> projects) {
        this.projects.clear();
        this.projects.addAll(projects);
    }

    private void setObservers() {

        viewModel.getTask().observe(this, tasks -> {
            if (tasks == null) {
                tasks = new ArrayList<>();
            }
            adapter.updateTasks(tasks);
            updateTasks();
        });
    }

    //DATA MANAGEMENT
    private void addTask(@NonNull Task task) {
        viewModel.insertTask(task);
    }

    /**
     * Updates the list of tasks in the UI
     */
    private void updateTasks() {
        if (adapter.getItemCount() == 0) {
            lblNoTasks.setVisibility(View.VISIBLE);
            listTasks.setVisibility(View.GONE);
        } else {
            lblNoTasks.setVisibility(View.GONE);
            listTasks.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDeleteTask(Task task) {
        deleteTask(task);
    }

    private void deleteTask(Task task) {
        viewModel.deleteTask(task);
    }

    //ALERT DIALOG
    @NonNull
    private AlertDialog getAddTaskDialog() {
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this, R.style.Dialog);

        alertBuilder.setTitle(R.string.add_task);
        alertBuilder.setView(R.layout.dialog_add_task);
        alertBuilder.setPositiveButton(R.string.add, null);
        alertBuilder.setOnDismissListener(dialogInterface -> {
            dialogEditText = null;
            dialogSpinner = null;
            dialog = null;
        });

        dialog = alertBuilder.create();

        // This instead of listener to positive button in order to avoid automatic dismiss
        dialog.setOnShowListener(dialogInterface -> {

            Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            button.setOnClickListener(view -> onPositiveButtonClick(dialog));
        });

        return dialog;
    }

    /**
     * Sets the data of the Spinner with projects to associate to a new task
     */
    private void populateDialogSpinner() {
        final ArrayAdapter<Project> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, projects);
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
            else {
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
    private void  showAddTaskDialog() {
        final AlertDialog dialog = getAddTaskDialog();

        dialog.show();

        dialogEditText = dialog.findViewById(R.id.txt_task_name);
        dialogSpinner = dialog.findViewById(R.id.project_spinner);

        populateDialogSpinner();
    }

    public void filterMethod(MainActivity.SortMethod name) {

        switch (name) {
            case RECENT_FIRST:
                viewModel.getTasksByDesc().observe(this, tasks -> {
                    if (tasks == null) {
                        tasks = new ArrayList<>();
                    }
                    adapter.updateTasks(tasks);
                    updateTasks();
                });
                break;
            case OLD_FIRST:
                setObservers();
                break;
        }
    }

    @SuppressLint("NonConstantResourceId")
    public void filterByProject(int id) {
        switch (id) {
            case projet_tartampion:
                viewModel.getProjectById(1).observe(this, tasks -> {
                    if (tasks == null) {
                        tasks = new ArrayList<>();
                    }
                    adapter.updateTasks(tasks);
                    updateTasks();
                });
                break;

            case R.id.projet_Lucidia:
                viewModel.getProjectById(2).observe(this, tasks -> {
                    if (tasks == null) {
                        tasks = new ArrayList<>();
                    }
                    adapter.updateTasks(tasks);
                    updateTasks();
                });
                break;

            case R.id.projet_Circus:
                viewModel.getProjectById(3).observe(this, tasks -> {
                    if (tasks == null) {
                        tasks = new ArrayList<>();
                    }
                    adapter.updateTasks(tasks);
                    updateTasks();
                });
                break;
        }
    }

    /**
     * List of all possible sort methods for task
     */
    public enum SortMethod {
        /**
         * Lastly created first
         */
        RECENT_FIRST,
        /**
         * First created first
         */
        OLD_FIRST
    }
}

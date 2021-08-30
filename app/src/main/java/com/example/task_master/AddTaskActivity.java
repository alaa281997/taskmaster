package com.example.task_master;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Team;

import java.util.ArrayList;
import java.util.List;


public class AddTaskActivity extends AppCompatActivity {

    public static final String Task_database = "Task_database";

    private static final String TAG = "AddTaskActivity";

    private TaskDao taskDao;
    private final List<Team> teams = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_task);

        setTitle("Add Task");
        getDataFromAPI();

        Spinner statesList = findViewById(R.id.listItems);
        String[] states = new String[]{"New", "Assigned", "In progress", "Complete"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, states);
        statesList.setAdapter(adapter);

        Spinner teamsList = findViewById(R.id.teamId);
        String[] teams = new String[]{"Team 1", "Team 2", "Team 3"};
        ArrayAdapter<String> TeamsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, teams);
        teamsList.setAdapter(TeamsAdapter);


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor preferenceEditor = sharedPreferences.edit();


        TaskDatabase database = Room.databaseBuilder(getApplicationContext(), TaskDatabase.class, "Task_database")
                .allowMainThreadQueries().build();
        taskDao = database.taskDao();


        findViewById(R.id.button2).setOnClickListener(view -> {
            String taskTitle = ((EditText) findViewById(R.id.inputTitle)).getText().toString();
            String taskBody = ((EditText) findViewById(R.id.inputBody)).getText().toString();

            Spinner stateSpinner = (Spinner) findViewById(R.id.listItems);
            String taskState = stateSpinner.getSelectedItem().toString();

            Spinner teamSpinner = (Spinner) findViewById(R.id.teamId);
            String teamName = teamSpinner.getSelectedItem().toString();

            preferenceEditor.putString("teamName", teamName);
            preferenceEditor.apply();


            StatusItems newTask = new StatusItems(taskTitle, taskBody, taskState);
            taskDao.insertOne(newTask);

            addTaskToDynamoDB(taskTitle,
                    taskBody,
                    taskState,
                    new Team(getId(teamName), teamName));

        });
    }
    public void addTaskToDynamoDB(String taskTitle, String taskBody, String taskState, Team team) {
        com.amplifyframework.datastore.generated.model.Task task = com.amplifyframework.datastore.generated.model.Task.builder()
                .title(taskTitle)
                .body(taskBody)
                .status(taskState)
                .team(team)
                .build();

        Amplify.API.mutate(ModelMutation.create(task),
                success -> Log.i(TAG, "Saved item: " + task.getTitle()),
                error -> Log.e(TAG, "Could not save item to API", error));
        Toast toast = Toast.makeText(this, "Task saved", Toast.LENGTH_LONG);
        toast.show();

    }

    private void getDataFromAPI() {
        Amplify.API.query(ModelQuery.list(Team.class),
                response -> {
                    for (Team team : response.getData()) {
                        teams.add(team);
                        Log.i(TAG, "the team id DynamoDB are => " + team.getTeamName() + "  " + team.getId());
                    }
                },
                error -> Log.e(TAG, "onCreate: Failed to get team from DynamoDB => " + error.toString())
        );
    }

    public String getId(String teamName) {
        for (Team team : teams) {
            if (team.getTeamName().equals(teamName)) {
                return team.getId();
            }
        }
        return "";
    }

}
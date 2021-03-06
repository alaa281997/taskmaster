package com.example.task_master;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;
import com.example.task_master.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private List<StatusItems> statusItems;

    private statusAdapter adapter;

    private TaskDao taskDao;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler = new Handler(message -> {
            notifyDataSetChanged();
            return false;
        });

        configureAmplify();


        TaskDatabase database = Room.databaseBuilder(getApplicationContext(), TaskDatabase.class, "task")
                .allowMainThreadQueries().build();
        taskDao = database.taskDao();

        findViewById(R.id.button).setOnClickListener(view -> {
            Intent goToAddTask = new Intent(MainActivity.this, AddTaskActivity.class);
            startActivity(goToAddTask);
        });

        findViewById(R.id.button3).setOnClickListener(view -> {
            Intent goToAllTask = new Intent(MainActivity.this, AllTaskActivity.class);
            startActivity(goToAllTask);
        });

        saveTeam("Team 1");
        saveTeam("Team 2");
        saveTeam("Team 3");

    }
    private void listItemDeleted() {
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String team = sharedPreferences.getString("teamName", "");
        statusItems = new ArrayList<>();
        if (team.equals("")) {
//            getDataFromAPI();
        } else {
            ((TextView) findViewById(R.id.team)).setText(team + " Tasks");
//            getDataFromAPI(team);
        }


        RecyclerView statusList = findViewById(R.id.IdList);

        TaskDatabase  database = Room.databaseBuilder(getApplicationContext(),
                TaskDatabase.class, AddTaskActivity.Task_database).allowMainThreadQueries().build();
        taskDao = database.taskDao();
        statusItems = taskDao.findAll();

        adapter = new statusAdapter(statusItems, new statusAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(int position) {
                Intent goToDetailsIntent = new Intent(getApplicationContext(), TaskDetailActivity.class);
                goToDetailsIntent.putExtra("taskTitle", statusItems.get(position).getTitle());
                goToDetailsIntent.putExtra("taskBody", statusItems.get(position).getBody());
                goToDetailsIntent.putExtra("taskStatus", statusItems.get(position).getStatus());
                startActivity(goToDetailsIntent);
            }

            @Override
            public void onDeleteItem(int position) {

                taskDao.delete(statusItems.get(position));
                statusItems.remove(position);

                Toast.makeText(MainActivity.this, "Item deleted", Toast.LENGTH_SHORT).show();


            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false);
        statusList.setLayoutManager(linearLayoutManager);
        statusList.setAdapter(adapter);
    }

    private void configureAmplify() {

        try {
            Amplify.addPlugin(new AWSDataStorePlugin());
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.configure(getApplicationContext());
            Log.i(TAG, "onCreate: Successfully initialized Amplify plugins");
        } catch (AmplifyException exception) {
            Log.e(TAG, "onCreate: Failed to initialize Amplify plugins => " + exception.toString());
        }
    }

    private void getTasksDataFromAPI() {
        Amplify.API.query(ModelQuery.list(com.amplifyframework.datastore.generated.model.Task.class),
                response -> {
                    for (com.amplifyframework.datastore.generated.model.Task task : response.getData()) {
                        statusItems.add(new StatusItems(task.getTitle(), task.getBody(), task.getStatus()));
                        Log.i(TAG, "onCreate: the Tasks DynamoDB are => " + task.getTitle());
                    }
                    handler.sendEmptyMessage(1);
                },
                error -> Log.e(TAG, "onCreate: Failed to get Tasks from DynamoDB => " + error.toString())
        );
    }

    @SuppressLint("NotifyDataSetChanged")
    private void notifyDataSetChanged() {
        adapter.notifyDataSetChanged();
    }


    public void saveTeam(String teamName) {
        Team team = Team.builder().teamName(teamName).build();

        Amplify.API.query(ModelQuery.list(Team.class, Team.TEAM_NAME.contains(teamName)),
                response -> {
                    List<Team> teams = (List<Team>) response.getData().getItems();

                    if (teams.isEmpty()) {
                        Amplify.API.mutate(ModelMutation.create(team),
                                success -> Log.i(TAG, "Saved Team => " + team.getTeamName()),
                                error -> Log.e(TAG, "Could not save Team to API => ", error));
                    }
                },
                error -> Log.e(TAG, "Failed to get Team from DynamoDB => " + error.toString())
        );

    }

    private void getDataFromAPI(String teamName) {
        Amplify.API.query(ModelQuery.list(com.amplifyframework.datastore.generated.model.Task.class),
                response -> {
                    for (com.amplifyframework.datastore.generated.model.Task task : response.getData()) {

                        if ((task.getTeam().getTeamName()).equals(teamName)) {
                            statusItems.add(new StatusItems(task.getTitle(), task.getBody(), task.getStatus()));
                            Log.i(TAG, "onCreate: the Tasks DynamoDB are => " + task.getTitle());
                            Log.i(TAG, "onCreate: the Team DynamoDB are => " + task.getTeam().getTeamName());
                        }
                    }
                    handler.sendEmptyMessage(1);
                },
                error -> Log.e(TAG, "onCreate: Failed to get Tasks from DynamoDB => " + error.toString())
        );
    }

    public void goToSetting(View view) {
        Intent goToSettings = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(goToSettings);
    }

}
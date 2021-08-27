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
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.example.task_master.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private RecyclerView statusList;
    private List<StatusItems> statusItems;
    private TextView para;
    private TaskDao taskDao;
    private TaskDatabase database;
    private statusAdapter adapter;
    private List<StatusItems> tasksList;

    private Handler handler;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configureAmplify();
        statusList = findViewById(R.id.IdList);

        handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public boolean handleMessage(@NonNull Message message) {
//                        TaskRecyclerView.getAdapter().notifyDataSetChanged();
                Objects.requireNonNull(statusList.getAdapter()).notifyDataSetChanged();
                return false;
            }
        });

        tasksList = new ArrayList<>();
        getTaskDataFromAPI();

         adapter = new statusAdapter(tasksList, new statusAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(int position) {
                Intent goToDetailsIntent = new Intent(getApplicationContext(), TaskDetailActivity.class);
                goToDetailsIntent.putExtra("title", statusItems.get(position).getTitle());
                goToDetailsIntent.putExtra("body", statusItems.get(position).getBody());
                goToDetailsIntent.putExtra("status", statusItems.get(position).getStatus());
                startActivity(goToDetailsIntent);
            }
            @Override
            public void onDeleteItem(int position) {
                taskDao.delete(statusItems.get(position));
                statusItems.remove(position);
                Toast.makeText(MainActivity.this, "Item deleted", Toast.LENGTH_SHORT).show();
            }
        });


        Button goToAddTask = MainActivity.this.findViewById(R.id.button);

        goToAddTask.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent goToAddIntent = new Intent(MainActivity.this, AddTaskActivity.class);
                MainActivity.this.startActivity(goToAddIntent);
            }
        });

        Button goToAllTask = MainActivity.this.findViewById(R.id.button3);
        goToAllTask.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent goToAllIntent = new Intent(MainActivity.this, AllTaskActivity.class);
                MainActivity.this.startActivity(goToAllIntent);
            }
        });


//        statusList = findViewById(R.id.IdList);
//        para = findViewById(R.id.textView8);
//        statusItems = new ArrayList<>();
//        statusItems.add(new StatusItems("Math", String.valueOf(para),"New"));
//        statusItems.add(new StatusItems("It", String.valueOf(para),"complete"));
//        statusItems.add(new StatusItems("psychics", String.valueOf(para),"in progress"));

//        database = Room.databaseBuilder(getApplicationContext(),
//                TaskDatabase.class, AddTaskActivity.Task_database).allowMainThreadQueries().build();
//        taskDao = database.taskDao();
//        statusItems = taskDao.findAll();




        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false);

        statusList.setLayoutManager(linearLayoutManager);
        statusList.setAdapter(adapter);


    }
    @SuppressLint("NotifyDataSetChanged")
    private void notifyDataSetChanged() {
        adapter.notifyDataSetChanged();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void goButton1(View view) {
        Intent shopDetail = new Intent(this,TaskDetailActivity.class);
        shopDetail.putExtra("title", "IT");
        startActivity(shopDetail);
    }
    public void goButton2(View view) {
        Intent shopDetail = new Intent(this,TaskDetailActivity.class);
        shopDetail.putExtra("title", "Math");
        startActivity(shopDetail);
    }
    public void goButton3(View view) {
        Intent shopDetail = new Intent(this,TaskDetailActivity.class);
        shopDetail.putExtra("title", "physics");
        startActivity(shopDetail);
    }
    public void goSettings(MenuItem item) {
        Intent goToSettings = new Intent(MainActivity.this, SettingsActivity.class);
        MainActivity.this.startActivity(goToSettings);
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        TextView address = findViewById(R.id.textView6);
        address.setText(preferences.getString("nameKey", "Save") + "'s Tasks");
    }


    public void goTostt(View view) {
        Intent goToSettings = new Intent(MainActivity.this, SettingsActivity.class);
        MainActivity.this.startActivity(goToSettings);
    }

    private void configureAmplify() {
        // configure Amplify plugins
        try {
            Amplify.addPlugin(new AWSDataStorePlugin());
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.configure(getApplicationContext());
            Log.i(TAG, "onCreate: Successfully initialized Amplify plugins");
        } catch (AmplifyException exception) {
            Log.e(TAG, "onCreate: Failed to initialize Amplify plugins => " + exception.toString());
        }
    }

    private void getTaskDataFromAPI() {
        List<StatusItems> taskItemLists = new ArrayList<>();
        Amplify.API.query(ModelQuery.list(com.amplifyframework.datastore.generated.model.Task.class),
                response -> {
                    for (com.amplifyframework.datastore.generated.model.Task task : response.getData()) {
                        tasksList.add(new StatusItems(task.getTitle(), task.getDescription(), task.getStatus()));
                        Log.i(TAG, "onCreate: the tasks are => " + task.getTitle());
                    }
                    handler.sendEmptyMessage(1);
                },
                error -> {
                    Log.e(TAG, "onCreate: Failed to get tasks => " + error.toString());
                    tasksList = showTasksSavedInDataBase();
                    handler.sendEmptyMessage(1);
                });
    }

    private List<StatusItems> showTasksSavedInDataBase() {
        TaskDatabase taskDatabase = Room.databaseBuilder(this, TaskDatabase.class, "tasks")
                .allowMainThreadQueries().build();
         taskDao = taskDatabase.taskDao();
        return taskDao.findAll();

    }
}

//package com.example.task_master;
//
//import android.annotation.SuppressLint;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Handler;
//import android.os.Looper;
//import android.os.Message;
//import android.preference.PreferenceManager;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import androidx.room.Room;
//
//import android.os.Bundle;
//import android.provider.Settings;
//import android.util.Log;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.Button;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.amplifyframework.AmplifyException;
//import com.amplifyframework.api.aws.AWSApiPlugin;
//import com.amplifyframework.api.graphql.model.ModelQuery;
//import com.amplifyframework.core.Amplify;
//import com.amplifyframework.datastore.AWSDataStorePlugin;
//import com.amplifyframework.datastore.generated.model.Task;
//import com.example.task_master.databinding.ActivityMainBinding;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Objects;
//
//public class MainActivity extends AppCompatActivity {
//    private List<StatusItems>  tasksList;
//    private ActivityMainBinding binding;
//    private RecyclerView statusList;
//    private List<StatusItems> statusItems;
//    private TextView para;
//    private TaskDao taskDao;
//    private TaskDatabase database;
//    private statusAdapter adapter;
//    public static final String TAG = "MainActivity";
//    private Handler handler;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        Button goToAddTask = MainActivity.this.findViewById(R.id.button);
//
//        statusList = findViewById(R.id.IdList);
//
//        configureAmplify();
//
//        statusList = findViewById(R.id.IdList);
//
//        handler = new Handler(new Handler.Callback() {
//            @Override
//            public boolean handleMessage(@NonNull Message message) {
//                notifyDataSetChanged();
//                return false;
//            }
//        });
////        handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
////            @SuppressLint("NotifyDataSetChanged")
////            @Override
////            public boolean handleMessage(@NonNull Message message) {
//////                statusList.getAdapter().notifyDataSetChanged();
////                notifyDataSetChanged();
////                return false;
////            }
////        });
//
//        tasksList = new ArrayList<>();
//        getTaskDataFromAPI();
//
////        statusList = findViewById(R.id.IdList);
////        para = findViewById(R.id.textView8);
////        statusItems = new ArrayList<>();
////        statusItems.add(new StatusItems("Math", String.valueOf(para),"New"));
////        statusItems.add(new StatusItems("It", String.valueOf(para),"complete"));
////        statusItems.add(new StatusItems("psychics", String.valueOf(para),"in progress"));
//
////        database = Room.databaseBuilder(getApplicationContext(),
////                TaskDatabase.class, AddTaskActivity.Task_database).allowMainThreadQueries().build();
////        taskDao = database.taskDao();
////        tasksList = taskDao.findAll();
////
//           adapter = new statusAdapter(statusItems, new statusAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClicked(int position) {
//                Intent goToDetailsIntent = new Intent(getApplicationContext(), TaskDetailActivity.class);
//                goToDetailsIntent.putExtra("title", statusItems.get(position).getTitle());
//                goToDetailsIntent.putExtra("body", statusItems.get(position).getBody());
//                goToDetailsIntent.putExtra("status", statusItems.get(position).getStatus());
//                startActivity(goToDetailsIntent);
//            }
//            @Override
//            public void onDeleteItem(int position) {
//                taskDao.delete(statusItems.get(position));
//                statusItems.remove(position);
//
//                Toast.makeText(MainActivity.this, "Item deleted", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
//        statusList.setLayoutManager(linearLayoutManager);
//        statusList.setAdapter(adapter);
//
//
//
//        goToAddTask.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Intent goToAddIntent = new Intent(MainActivity.this, AddTaskActivity.class);
//                MainActivity.this.startActivity(goToAddIntent);
//            }
//        });
//
//
//        Button goToAllTask = MainActivity.this.findViewById(R.id.button3);
//        goToAllTask.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Intent goToAllIntent = new Intent(MainActivity.this, AllTaskActivity.class);
//                MainActivity.this.startActivity(goToAllIntent);
//            }
//        });
//}
//
//
//    @SuppressLint("NotifyDataSetChanged")
//    private void notifyDataSetChanged() {
//        adapter.notifyDataSetChanged();
//    }
//    private void getTaskDataFromAPI() {
//        List<StatusItems> taskItemLists = new ArrayList<>();
//        Amplify.API.query(ModelQuery.list(Task.class),
//                response -> {
//                    for (Task task : response.getData()) {
//                        tasksList.add(new StatusItems(task.getTitle(), task.getDescription(), task.getStatus()));
//                        Log.i(TAG, "onCreate: the tasks are => " + task.getTitle());
//                    }
//                    handler.sendEmptyMessage(1);
//                },
//                error -> {
//                    Log.e(TAG, "onCreate: Failed to get tasks => " + error.toString());
//                    tasksList = showTasksSavedInDataBase();
//                    handler.sendEmptyMessage(1);
//                });
//    }
//
//    private List<StatusItems> showTasksSavedInDataBase() {
//        TaskDatabase taskDatabase = Room.databaseBuilder(this, TaskDatabase.class, "tasks")
//                .allowMainThreadQueries().build();
//        TaskDao taskDao = taskDatabase.taskDao();
//        return taskDao.findAll();
//
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//
//    public void goButton1(View view) {
//        Intent shopDetail = new Intent(this,TaskDetailActivity.class);
//        shopDetail.putExtra("title", "IT");
//        startActivity(shopDetail);
//    }
//    public void goButton2(View view) {
//        Intent shopDetail = new Intent(this,TaskDetailActivity.class);
//        shopDetail.putExtra("title", "Math");
//        startActivity(shopDetail);
//    }
//    public void goButton3(View view) {
//        Intent shopDetail = new Intent(this,TaskDetailActivity.class);
//        shopDetail.putExtra("title", "physics");
//        startActivity(shopDetail);
//    }
//    public void goSettings(MenuItem item) {
//        Intent goToSettings = new Intent(MainActivity.this, SettingsActivity.class);
//        MainActivity.this.startActivity(goToSettings);
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
//        TextView address = findViewById(R.id.textView6);
//        address.setText(preferences.getString("nameKey", "Save") + "'s Tasks");
//    }
//    private void configureAmplify() {
//        try {
//            Amplify.addPlugin(new AWSDataStorePlugin());
//            Amplify.addPlugin(new AWSApiPlugin());
//            Amplify.configure(getApplicationContext());
//            Log.i(TAG, "onCreate: Successfully initialized Amplify plugins");
//        } catch (AmplifyException exception) {
//            Log.e(TAG, "onCreate: Failed to initialize Amplify plugins => " + exception.toString());
//        }
//    }
//
//    public void goTostt(View view) {
//        Intent goToSettings = new Intent(MainActivity.this, SettingsActivity.class);
//        MainActivity.this.startActivity(goToSettings);
//    }
//
//
//}
package com.example.task_master;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.core.Amplify;

import java.util.ArrayList;
import java.util.List;


public class AddTaskActivity extends AppCompatActivity {
    public static final String Task_database = "Task_database";
    private static final String TAG = "AddTaskActivity";

    private TaskDao taskDao;
    private TaskDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_task);
        getActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        database = Room.databaseBuilder(getApplicationContext(), TaskDatabase.class, Task_database).allowMainThreadQueries().build();
        taskDao = database.taskDao();

        Button save = findViewById(R.id.button2);


        String[] statusOfTask = {"New", "In progress", "Complete", "assigned"};
        Spinner listView = findViewById(R.id.listItems);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, statusOfTask);
        listView.setAdapter(adapter);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText inputTitle = findViewById(R.id.inputTitle);
                EditText inputBody = findViewById(R.id.inputBody);
                String title = inputTitle.getText().toString();
                String body = inputBody.getText().toString();

                // list
                Spinner spinner = (Spinner) findViewById(R.id.listItems);
                String status = spinner.getSelectedItem().toString();

                com.amplifyframework.datastore.generated.model.Task taskItem = com.amplifyframework.datastore.generated.model.Task.builder()
                        .title(title)
                        .description(body)
                        .status(status)
                        .build();

                if(isNetworkAvailable(getApplicationContext())){
                    Log.i(TAG, "onClick: the network is available");
                }else{
                    Log.i(TAG, "onClick: net down");
                }

                saveTaskToAPI(taskItem);
                TaskDataManger.getInstance().getData().add(new StatusItems(taskItem.getTitle() , taskItem.getDescription(),taskItem.getStatus()));
                Toast.makeText(AddTaskActivity.this, "Task saved", Toast.LENGTH_SHORT).show();


                // save data
                StatusItems statusItems = new StatusItems(title, body, status);

                taskDao.insertOne(statusItems);
               // Toast.makeText(AddTaskActivity.this, "Item added", Toast.LENGTH_SHORT).show();

                Intent mainIntent = new Intent(AddTaskActivity.this, MainActivity.class);
                startActivity(mainIntent);


            }
        });

//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
//                this,
//                LinearLayoutManager.VERTICAL,
//                false);

//
//        getActionBar();
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    public com.amplifyframework.datastore.generated.model.Task saveTaskToAPI(com.amplifyframework.datastore.generated.model.Task taskItem) {
        Amplify.API.mutate(ModelMutation.create(taskItem),
                success -> Log.i(TAG, "Saved item: " + taskItem.getTitle()),
                error -> Log.e(TAG, "Could not save item to API/dynamodb" + taskItem.getTitle()));
        return taskItem;

    }
    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager =
                ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager
                .getActiveNetworkInfo().isConnected();
    }
}

//package com.example.task_master;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.room.Room;
//
//import android.content.Context;
//import android.content.Intent;
//import android.net.ConnectivityManager;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ListView;
//import android.widget.Spinner;
//import android.widget.Toast;
//
//import com.amplifyframework.api.graphql.model.ModelMutation;
//import com.amplifyframework.core.Amplify;
//import com.amplifyframework.datastore.generated.model.Task;
//
//import java.util.ArrayList;
//import java.util.List;
//
//
//public class AddTaskActivity extends AppCompatActivity {
//
//    public static final String TASK_ITEM = "task-item";
//    private static final String TAG = "AddTask";
//
//    private TaskDao taskDao;
//    private TaskDatabase database;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.add_task);
//
//        getActionBar();
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        database = Room.databaseBuilder(getApplicationContext(),TaskDatabase.class,TASK_ITEM).allowMainThreadQueries().build();
//        taskDao = database.taskDao();
//
//
//        EditText inputTitle = findViewById(R.id.inputTitle);
//        EditText inputBody = findViewById(R.id.inputBody);
//        Spinner inputState = findViewById(R.id.listItems);
//
//        String title = inputTitle.getText().toString();
//
//        String body = inputBody.getText().toString();
//        String state = inputState.getTransitionName();
//
//        Button save = findViewById(R.id.button2);
//
//        String[] statusOfTask = {"New","In progress","Complete","assigned"};
//        Spinner listView = findViewById(R.id.listItems);
//
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,statusOfTask);
//        listView.setAdapter(adapter);
//
//        save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                EditText inputTitle = findViewById(R.id.inputTitle);
//                EditText inputBody = findViewById(R.id.inputBody);
//                Spinner spinner = (Spinner) findViewById(R.id.listItems);
//
//                String title = inputTitle.getText().toString();
//                String body = inputBody.getText().toString();
//                String status = spinner.getSelectedItem().toString();
//
//                Task taskItem = Task.builder()
//                        .title(title)
//                        .description(body)
//                        .status(status)
//                        .build();
//
//                if(isNetworkAvailable(getApplicationContext())){
//                    Log.i(TAG, "onClick: the network is available");
//                }else{
//                    Log.i(TAG, "onClick: net down");
//                }
//
//                saveTaskToAPI(taskItem);
//                TaskDataManger.getInstance().getData().add(new StatusItems(taskItem.getTitle() , taskItem.getDescription(),taskItem.getStatus()));
//                Toast.makeText(AddTaskActivity.this, "Task saved", Toast.LENGTH_SHORT).show();
//
//
//
//                // save data
//                StatusItems statusItems = new StatusItems(title, body, status);
//
//                taskDao.insertOne(statusItems);
//
//                Toast.makeText(AddTaskActivity.this, "Item added", Toast.LENGTH_SHORT).show();
//
//                Intent mainIntent = new Intent(AddTaskActivity.this, MainActivity.class);
//                startActivity(mainIntent);
//
//
//            }
//        });
//
//}
//
//
//    public Task saveTaskToAPI(Task taskItem) {
//        Amplify.API.mutate(ModelMutation.create(taskItem),
//                success -> Log.i(TAG, "Saved item: " + taskItem.getTitle()),
//                error -> Log.e(TAG, "Could not save item to API/dynamodb" + taskItem.getTitle()));
//        return taskItem;
//
//    }
//
//    public boolean isNetworkAvailable(Context context) {
//        ConnectivityManager connectivityManager =
//                ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
//        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager
//                .getActiveNetworkInfo().isConnected();
//    }
//
////
////    public void ShowAddTask(View view) {
////        Toast.makeText(AddTaskActivity.this,"submitted!",Toast.LENGTH_SHORT).show();
////        System.out.println("Toast");
////
////
////    }
//}

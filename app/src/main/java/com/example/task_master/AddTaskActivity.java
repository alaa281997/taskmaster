package com.example.task_master;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class AddTaskActivity extends AppCompatActivity {
    public static final String Task_database = "Task_database";

    private TaskDao taskDao;
    private TaskDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_task);

        database = Room.databaseBuilder(getApplicationContext(),TaskDatabase.class,Task_database).allowMainThreadQueries().build();
        taskDao = database.taskDao();

        Button save = findViewById(R.id.button2);


        String[] statusOfTask = {"New","In progress","Complete","assigned"};
        Spinner listView = findViewById(R.id.listItems);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,statusOfTask);
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


                // save data
                StatusItems statusItems = new StatusItems(title, body, status);

                taskDao.insertOne(statusItems);
                Toast.makeText(AddTaskActivity.this, "Item added", Toast.LENGTH_SHORT).show();
                Intent mainIntent = new Intent(AddTaskActivity.this, MainActivity.class);
                startActivity(mainIntent);

                
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false);


        getActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

}

//
//    public void ShowAddTask(View view) {
//        Toast.makeText(AddTaskActivity.this,"submitted!",Toast.LENGTH_SHORT).show();
//        System.out.println("Toast");
//
//
//    }
}

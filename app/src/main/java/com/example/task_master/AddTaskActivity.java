package com.example.task_master;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class AddTaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_task);

        getActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

}


    public void ShowAddTask(View view) {
        Toast.makeText(AddTaskActivity.this,"submitted!",Toast.LENGTH_SHORT).show();
        System.out.println("Toast");


    }
}

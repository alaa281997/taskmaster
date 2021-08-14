package com.example.task_master;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class AllTaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_task);


        getActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
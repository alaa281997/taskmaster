package com.example.task_master;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

import android.widget.TextView;



import java.net.URL;

public class TaskDetailActivity extends AppCompatActivity {
    private static final String TAG = "TaskDetail";
    private URL url =null;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_detail);
        Intent intent = getIntent();

        String taskName = intent.getExtras().getString("title");
        String taskBody = intent.getExtras().getString("body");
        String taskState = intent.getExtras().getString("status");

        ((TextView)findViewById(R.id.textView7)).setText(taskName);
        ((TextView)findViewById(R.id.textView8)).setText(taskBody);
        ((TextView)findViewById(R.id.textView11)).setText(taskState);



    }
    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        TextView fileName = findViewById(R.id.textView12);

        fileName.setText(preferences.getString("FileName","File Name"));

    }
}
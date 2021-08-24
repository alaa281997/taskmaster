package com.example.task_master;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class TaskDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_detail);
        Intent intent = getIntent();
//      ((TextView) findViewById(R.id.textView7)).setText(intent.getExtras().getString("title"));

        String taskName = intent.getExtras().getString("title");
        String taskBody = intent.getExtras().getString("body");
        String taskState = intent.getExtras().getString("status");

        ((TextView)findViewById(R.id.textView7)).setText(taskName);
        ((TextView)findViewById(R.id.textView8)).setText(taskBody);
        ((TextView)findViewById(R.id.textView11)).setText(taskState);

    }
}
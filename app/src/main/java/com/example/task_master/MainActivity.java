package com.example.task_master;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



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


}
}
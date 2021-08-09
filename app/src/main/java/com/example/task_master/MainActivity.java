package com.example.task_master;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.navigation.ui.AppBarConfiguration;

import com.example.task_master.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;


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
        address.setText(preferences.getString("nameKey", "Saved") + "'s Tasks");
    }



}
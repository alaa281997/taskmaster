package com.example.task_master;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.EditText;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);


      SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);// getter
        SharedPreferences.Editor preferenceEditor = preferences.edit();


    findViewById(R.id.button7).setOnClickListener((view) -> {
        EditText address = findViewById(R.id.editTextTextPersonName3);
        preferenceEditor.putString("nameKey", address.getText().toString());
        preferenceEditor.apply();
        System.out.println(address);
        Toast toast = Toast.makeText(this, "Name Saved!", Toast.LENGTH_LONG);
        toast.show();
        Intent mainIntent = new Intent(SettingsActivity.this, MainActivity.class);
        startActivity(mainIntent);
     });

    }

}
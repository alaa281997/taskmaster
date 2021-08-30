package com.example.task_master;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);


        Spinner teamsList = findViewById(R.id.teamsId);
        String[] teams = new String[]{"Team 1", "Team 2", "Team 3"};
        ArrayAdapter<String> TeamsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, teams);
        teamsList.setAdapter(TeamsAdapter);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor preferenceEditor = sharedPreferences.edit();

            findViewById(R.id.button7).setOnClickListener(view -> {
                String username = ((EditText) findViewById(R.id.editTextTextPersonName3)).getText().toString();


                Spinner teamSpinner = (Spinner) findViewById(R.id.teamsId);
                String teamName = teamSpinner.getSelectedItem().toString();

                preferenceEditor.putString("userName", username);
                preferenceEditor.putString("teamName", teamName);
                preferenceEditor.apply();

                Toast toast = Toast.makeText(this, "Saved", Toast.LENGTH_LONG);
                toast.show();

                Intent mainIntent = new Intent(this, MainActivity.class);
                startActivity(mainIntent);
            });

        }

}




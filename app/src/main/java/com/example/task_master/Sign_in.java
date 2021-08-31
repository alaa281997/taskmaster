package com.example.task_master;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.*;

import com.amplifyframework.core.Amplify;

public class Sign_in extends AppCompatActivity {
    private static final String TAG = "Sign_in";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor preferenceEditor = preferences.edit();

        Button signIn = findViewById(R.id.loginId);
        EditText username = findViewById(R.id.emailId);
        EditText password = findViewById(R.id.passId);
        TextView createNewAccount = findViewById(R.id.goSignUp);

        signIn.setOnClickListener(view -> {
            signIn(username.getText().toString(), password.getText().toString());
            preferenceEditor.putString("UserNameLog", username.getText().toString());
            preferenceEditor.apply();
        });

        createNewAccount.setOnClickListener(view -> {
            Intent goToSignUp = new Intent(this, Sign_up.class);
            startActivity(goToSignUp);
        });
    }
    void signIn(String username, String password) {
        Amplify.Auth.signIn(
                username,
                password,
                success -> {
                    Log.i(TAG, "signIn: worked " + success.toString());
                    Intent goToMain = new Intent(this, MainActivity.class);
                    startActivity(goToMain);
                },
                error -> Log.e(TAG, "signIn: failed" + error.toString()));


    }
}
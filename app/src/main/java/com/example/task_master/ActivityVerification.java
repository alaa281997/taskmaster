package com.example.task_master;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.amplifyframework.core.Amplify;

public class ActivityVerification extends AppCompatActivity {


    private static final String TAG = "ActivityVerification";
    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        EditText editText = findViewById(R.id.codeId);
        Button verify = findViewById(R.id.submitId);

        Intent intent = getIntent();
        username = intent.getExtras().getString("username", "");
        password = intent.getExtras().getString("password", "");

        verify.setOnClickListener(view -> verification(username, editText.getText().toString()));
    }

    void verification(String username, String confirmationNumber) {
        Amplify.Auth.confirmSignUp(
                username,
                confirmationNumber,
                success -> {
                    Log.i(TAG, "verification: succeeded" + success.toString());
                    Intent goToSignIn = new Intent(ActivityVerification.this, Sign_in.class);
                    goToSignIn.putExtra("username", username);
                    startActivity(goToSignIn);
                    },
                error -> {
                    Log.e(TAG, "verification: failed" + error.toString());
                });
    }

}
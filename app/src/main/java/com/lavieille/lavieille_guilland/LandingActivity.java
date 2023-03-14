package com.lavieille.lavieille_guilland;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.lavieille.lavieille_guilland.signin.AuthSuccess;
import com.lavieille.lavieille_guilland.signin.FirebaseConnection;

public class LandingActivity extends AppCompatActivity implements AuthSuccess {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        findViewById(R.id.logInButton).setOnClickListener(view -> {
            FirebaseConnection connection = new FirebaseConnection(this);

            String email = ((EditText) findViewById(R.id.userEmail)).getText().toString();
            String password = ((EditText) findViewById(R.id.userPassword)).getText().toString();

            connection.connectAccount(email, password);
        });

        findViewById(R.id.registerNowButton).setOnClickListener(view -> switchToRegister());
    }

    private void switchToRegister() {
        RegisterActivity activity = new RegisterActivity();
        Intent intent = new Intent(this, RegisterActivity.class);

        startActivity(intent);
        finish();
    }

    @Override
    public void authSuccess() {
        // TODO SWITCH TO THE RIGHT ACTIVITY AFTER LOGIN
    }

    @Override
    public void authFailure() {
        Toast.makeText(
                this, "Email or password incorrect.", Toast.LENGTH_LONG
        ).show();
    }
}
package com.lavieille.lavieille_guilland;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lavieille.lavieille_guilland.signin.FirebaseConnection;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseConnection connection = new FirebaseConnection(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        findViewById(R.id.register_button).setOnClickListener(view -> {
            boolean creationSuccess = connection.createAccount(
                    ((TextView) findViewById(R.id.userEmail)).getText().toString(),
                    ((TextView) findViewById(R.id.userPassword)).getText().toString()
            );

            if (creationSuccess) {
                // TODO Switch activity
            } else {
                Toast.makeText(
                                this, "Erreur lors de l'authentification !", Toast.LENGTH_LONG)
                        .show();
            }
        });

        findViewById(R.id.switch_to_login).setOnClickListener(view -> {
            LandingActivity activity = new LandingActivity();
            Intent intent = new Intent(this, LandingActivity.class);

            startActivity(intent);
        });
    }
}
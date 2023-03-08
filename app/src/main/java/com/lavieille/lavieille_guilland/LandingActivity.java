package com.lavieille.lavieille_guilland;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lavieille.lavieille_guilland.signin.FirebaseConnection;

public class LandingActivity extends AppCompatActivity {

    FirebaseConnection connection = new FirebaseConnection(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        findViewById(R.id.fab).setOnClickListener(view -> {
            connection.createAccount("test@gmail.com", "ouaiouai");
        });
    }
}
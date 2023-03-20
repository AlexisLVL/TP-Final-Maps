package com.lavieille.lavieille_guilland;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.lavieille.lavieille_guilland.utils.CheckConnection;
import com.lavieille.lavieille_guilland.utils.signin.AuthSuccess;
import com.lavieille.lavieille_guilland.utils.signin.FirebaseConnection;

public class RegisterActivity extends AppCompatActivity implements AuthSuccess {
    private final FirebaseConnection connection = new FirebaseConnection(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        findViewById(R.id.register_button).setOnClickListener(view -> connection.createAccount(
                ((TextView) findViewById(R.id.userEmail)).getText().toString(),
                ((TextView) findViewById(R.id.userPassword)).getText().toString()
        ));

        findViewById(R.id.switch_to_login).setOnClickListener(view -> switchToLogin());
    }

    private void switchToLogin() {
        finish();
    }

    @Override
    public void authSuccess() {
        switchToLogin();
    }

    @Override
    public void authFailure() {
        String message = "Erreur lors de la création du compte.";
        if (!CheckConnection.isNetworkConnected(this)) {
            message = "Pas de connection à internet.";
        }
        Toast.makeText(
                this, message, Toast.LENGTH_LONG
        ).show();
    }
}
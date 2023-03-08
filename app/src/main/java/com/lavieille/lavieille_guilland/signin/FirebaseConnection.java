package com.lavieille.lavieille_guilland.signin;

import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.lavieille.lavieille_guilland.LandingActivity;

public class FirebaseConnection {
    private LandingActivity activity;
    private FirebaseAuth mAuth;

    public FirebaseConnection(LandingActivity activity) {
        this.activity = activity;
        mAuth = FirebaseAuth.getInstance();
    }

    public void createAccount(String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        //TODO switch activity and where do we need to keep datas (UID of user etc) ?
                    } else {
                        Toast.makeText(
                                        activity, "Erreur lors de l'authentification !", Toast.LENGTH_LONG)
                                .show();
                    }
                });
    }
}

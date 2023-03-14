package com.lavieille.lavieille_guilland.signin;

import android.app.Activity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.atomic.AtomicBoolean;

public class FirebaseConnection {
    private AuthSuccess activity;
    private FirebaseAuth mAuth;
    private static FirebaseUser user = null;

    public FirebaseConnection(AuthSuccess activity) {
        this.activity = activity;
        mAuth = FirebaseAuth.getInstance();
    }

    public void createAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) activity, task -> {
                    if (task.isSuccessful()) {
                        activity.authSuccess();
                    } else {
                        activity.authFailure();
                    }
                });
    }

    public void connectAccount(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) activity, task -> {
                    if (task.isSuccessful()) {
                        activity.authSuccess();
                    } else {
                        activity.authFailure();
                    }
                });
    }

    public static FirebaseUser getUser() {
        return user;
    }
}

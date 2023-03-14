package com.lavieille.lavieille_guilland.signin;

import android.app.Activity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.atomic.AtomicBoolean;

public class FirebaseConnection {
    private Activity activity;
    private FirebaseAuth mAuth;
    private static FirebaseUser user = null;

    public FirebaseConnection(Activity activity) {
        this.activity = activity;
        mAuth = FirebaseAuth.getInstance();
    }

    public boolean createAccount(String email, String password) {

        AtomicBoolean success = new AtomicBoolean(false);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        success.set(true);
                        //TODO switch activity and where do we need to keep datas (UID of user etc) ?
                    } else {
                        success.set(false);
                    }
                });

        return success.get();
    }

    public boolean connectAccount(String email, String password) {
        AtomicBoolean success = new AtomicBoolean(false);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        user = mAuth.getCurrentUser();
                        success.set(true);
                    } else {
                        user = null;
                        success.set(false);
                    }
                });

        return success.get();
    }

    public FirebaseUser getUser() {
        return user;
    }
}

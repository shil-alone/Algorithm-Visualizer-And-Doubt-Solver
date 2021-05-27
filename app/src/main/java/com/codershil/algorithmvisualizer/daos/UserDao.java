package com.codershil.algorithmvisualizer.daos;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.codershil.algorithmvisualizer.activities.DoubtActivity;
import com.codershil.algorithmvisualizer.models.Post;
import com.codershil.algorithmvisualizer.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserDao {
    FirebaseFirestore database;
    FirebaseAuth auth;
    Activity activity;
    User user;

    public UserDao(Activity activity) {
        this.activity = activity;
        database = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    public void addUser(User user) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                database.collection("users")
                        .document(user.getUid())
                        .set(user)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(activity, "profile created successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(activity, "unable to create profile", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
            }
        }).start();


    }

    public User getUser(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                database.collection("users")
                        .document(auth.getCurrentUser().getUid())
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                user = documentSnapshot.toObject(User.class);
                            }
                        });
            }
        });
        return user;
    }




}

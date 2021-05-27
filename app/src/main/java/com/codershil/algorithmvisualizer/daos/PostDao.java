package com.codershil.algorithmvisualizer.daos;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.codershil.algorithmvisualizer.activities.DoubtActivity;
import com.codershil.algorithmvisualizer.models.Post;
import com.codershil.algorithmvisualizer.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.Objects;

public class PostDao {
    FirebaseFirestore database;
    FirebaseAuth auth;
    Activity activity;
    public PostDao(Activity activity) {
        this.activity = activity;
        database = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();
    }

    public void addPost(Post post){

        new Thread(new Runnable() {
            @Override
            public void run() {
                database.collection("posts")
                        .add(post)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(activity, "post added successfully", Toast.LENGTH_SHORT).show();
                                        activity.startActivity(new Intent(activity,DoubtActivity.class));
                                        activity.finish();
                                    }
                                });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(activity, "unable to add post", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
            }
        }).start();

    }

}

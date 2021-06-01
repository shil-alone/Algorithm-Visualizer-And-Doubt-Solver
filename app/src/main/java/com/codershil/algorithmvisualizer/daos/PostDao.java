package com.codershil.algorithmvisualizer.daos;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.codershil.algorithmvisualizer.activities.DoubtActivity;
import com.codershil.algorithmvisualizer.adapters.CommentAdapter;
import com.codershil.algorithmvisualizer.models.Comment;
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


import java.lang.reflect.Array;
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

    public void addPost(Post post) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                String id = database.collection("posts").document().getId();
                post.setDocumentId(id);
                database.collection("posts")
                        .document(id)
                        .set(post)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(activity, "post added successfully", Toast.LENGTH_SHORT).show();
                                        activity.startActivity(new Intent(activity, DoubtActivity.class));
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

    public void likePost(Post post) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (post.getLikedBy().contains(auth.getCurrentUser().getUid())) {
                    post.getLikedBy().remove(auth.getCurrentUser().getUid());
                } else {
                    post.getLikedBy().add(auth.getCurrentUser().getUid());
                }
                database.collection("posts")
                        .document(post.getDocumentId())
                        .update("likedBy", post.getLikedBy());
            }
        }).start();
    }

    public void addComment(Comment comment) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                database.collection("posts")
                        .document(comment.getPostId())
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                Post post = documentSnapshot.toObject(Post.class);
                                post.getCommentedBy().add(auth.getCurrentUser().getUid());
                                database.collection("posts")
                                        .document(post.getDocumentId())
                                        .update("commentedBy", post.getCommentedBy());

                                String id = database.collection("posts")
                                        .document().getId();
                                comment.setDocumentId(id);
                                database.collection("posts")
                                        .document(comment.getPostId())
                                        .collection("comments")
                                        .document(id)
                                        .set(comment)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                activity.runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(activity, "comment added successfully", Toast.LENGTH_SHORT).show();
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
                                                        Toast.makeText(activity, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        });

                            }
                        });

            }
        }).start();

    }

    public void deleteComment(Comment comment) {
        database.collection("posts")
                .document(comment.getPostId())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Post post = documentSnapshot.toObject(Post.class);
                        post.getCommentedBy().remove(auth.getCurrentUser().getUid());
                        database.collection("posts")
                                .document(post.getDocumentId())
                                .update("commentedBy", post.getCommentedBy());
                    }
                });
    }


}

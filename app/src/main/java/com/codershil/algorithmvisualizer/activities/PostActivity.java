package com.codershil.algorithmvisualizer.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.codershil.algorithmvisualizer.R;
import com.codershil.algorithmvisualizer.daos.PostDao;
import com.codershil.algorithmvisualizer.models.Post;
import com.codershil.algorithmvisualizer.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class PostActivity extends AppCompatActivity {
    EditText edtPostContent;
    Button btnPost;
    PostDao postDao;
    FirebaseAuth auth;
    FirebaseFirestore database;
    User user;
    Post post;
    LottieAnimationView progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        initialize();
        addClicks();
    }

    private void initialize() {
        edtPostContent = findViewById(R.id.edtPostContent);
        progressBar = findViewById(R.id.progressBarPost);
        btnPost = findViewById(R.id.btnPost);
        progressBar.setVisibility(View.GONE);
        getSupportActionBar().setTitle("Post");
        postDao = new PostDao(PostActivity.this);
        auth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();
        user = new User();
        post = new Post();
    }

    private void addClicks() {
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String postContent = edtPostContent.getText().toString().trim();
                if (postContent.isEmpty()) {
                    edtPostContent.setError("please enter doubt");
                    progressBar.setVisibility(View.GONE);
                    return;
                }

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
                                        post = new Post(user.getName(), postContent, user.getUid());
                                        postDao.addPost(post);
                                        progressBar.setVisibility(View.GONE);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(PostActivity.this, "unable to get user data", Toast.LENGTH_SHORT).show();
                                                progressBar.setVisibility(View.GONE);

                                            }
                                        });
                                    }
                                });
                    }
                }).start();

            }
        });
    }
}
package com.codershil.algorithmvisualizer.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.codershil.algorithmvisualizer.R;
import com.codershil.algorithmvisualizer.adapters.CommentAdapter;
import com.codershil.algorithmvisualizer.adapters.PostAdapter;
import com.codershil.algorithmvisualizer.daos.PostDao;
import com.codershil.algorithmvisualizer.models.Comment;
import com.codershil.algorithmvisualizer.models.Post;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.type.PostalAddress;

import java.util.ArrayList;
import java.util.Collection;

public class DoubtActivity extends AppCompatActivity implements PostAdapter.OnItemClickListener {
    RecyclerView postRV;
    FloatingActionButton floatingActionButton;
    PostAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    CollectionReference postRef = database.collection("posts");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doubt);
        initialize();
        setUpRecyclerView();
        addOnClicks();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.post_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.myPosts) {
            startActivity(new Intent(DoubtActivity.this, MyPostsActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private void initialize() {
        postRV = findViewById(R.id.postRV);
        floatingActionButton = findViewById(R.id.btnFloat);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        getSupportActionBar().setTitle("Doubt Solver");
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#3F51B5"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);
        // changing the color of status bar
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.status_bar_login));
        }
    }

    private void setUpRecyclerView() {
        Query query = postRef.orderBy("createdAt", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Post> options = new FirestoreRecyclerOptions.Builder<Post>()
                .setQuery(query, Post.class)
                .build();
        adapter = new PostAdapter(options);
        postRV.setHasFixedSize(true);
        postRV.setLayoutManager(new LinearLayoutManager(DoubtActivity.this));
        postRV.setAdapter(adapter);
        adapter.onItemClickListener(this);
    }

    private void addOnClicks() {
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DoubtActivity.this, PostActivity.class));
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }


    @Override
    public void onLikeClick(DocumentSnapshot documentSnapshot, int position) {
        PostDao postDao = new PostDao(DoubtActivity.this);
        Post post = documentSnapshot.toObject(Post.class);
        postDao.likePost(post);
    }

    @Override
    public void onCommentClick(DocumentSnapshot documentSnapshot, int position) {
        Post post = documentSnapshot.toObject(Post.class);
        Intent intent = new Intent(DoubtActivity.this, CommentActivity.class);
        intent.putExtra("POST", post.getDocumentId());
        startActivity(intent);
    }
}
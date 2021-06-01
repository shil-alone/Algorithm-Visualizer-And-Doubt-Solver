package com.codershil.algorithmvisualizer.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.codershil.algorithmvisualizer.R;
import com.codershil.algorithmvisualizer.adapters.PostAdapter;
import com.codershil.algorithmvisualizer.daos.PostDao;
import com.codershil.algorithmvisualizer.models.Post;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MyPostsActivity extends AppCompatActivity implements PostAdapter.OnItemClickListener {
    RecyclerView postRV;
    PostAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    CollectionReference postRef = database.collection("posts");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_posts);
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

    private void initialize() {
        postRV = findViewById(R.id.myPostsRV);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout1);
        getSupportActionBar().setTitle("My Posts");
    }

    private void setUpRecyclerView() {
        Query query = postRef.orderBy("createdAt", Query.Direction.DESCENDING).whereEqualTo("userUid", FirebaseAuth.getInstance().getCurrentUser().getUid());
        FirestoreRecyclerOptions<Post> options = new FirestoreRecyclerOptions.Builder<Post>()
                .setQuery(query, Post.class)
                .build();
        adapter = new PostAdapter(options);
        postRV.setHasFixedSize(true);
        postRV.setLayoutManager(new LinearLayoutManager(MyPostsActivity.this));
        postRV.setAdapter(adapter);
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                adapter.deletePost(viewHolder.getAdapterPosition());
                Toast.makeText(MyPostsActivity.this, "post deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(postRV);

        adapter.onItemClickListener(this);
    }


    private void addOnClicks() {
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
        PostDao postDao = new PostDao(MyPostsActivity.this);
        Post post = documentSnapshot.toObject(Post.class);
        postDao.likePost(post);
    }

    @Override
    public void onCommentClick(DocumentSnapshot documentSnapshot, int position) {
        Post post = documentSnapshot.toObject(Post.class);
        Intent intent = new Intent(MyPostsActivity.this, CommentActivity.class);
        intent.putExtra("POST", post.getDocumentId());
        startActivity(intent);
    }
}
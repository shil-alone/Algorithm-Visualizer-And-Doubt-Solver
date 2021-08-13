package com.codershil.algorithmvisualizer.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codershil.algorithmvisualizer.R;
import com.codershil.algorithmvisualizer.adapters.MyPostAdapter;
import com.codershil.algorithmvisualizer.adapters.PostAdapter;
import com.codershil.algorithmvisualizer.daos.PostDao;
import com.codershil.algorithmvisualizer.models.Post;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MyPostsActivity extends AppCompatActivity implements MyPostAdapter.OnItemsClickListener {
    RecyclerView postRV;
    MyPostAdapter adapter;
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
        Query query = postRef.orderBy("createdAt", Query.Direction.DESCENDING).whereEqualTo("userUid", FirebaseAuth.getInstance().getCurrentUser().getUid());
        FirestoreRecyclerOptions<Post> options = new FirestoreRecyclerOptions.Builder<Post>()
                .setQuery(query, Post.class)
                .build();
        adapter = new MyPostAdapter(options);
        postRV.setHasFixedSize(true);
        postRV.setLayoutManager(new LinearLayoutManager(MyPostsActivity.this));
        postRV.setAdapter(adapter);
        adapter.onItemClickListener(this);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                adapter.deletePost(viewHolder.getAbsoluteAdapterPosition());
                Toast.makeText(MyPostsActivity.this, "post deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(postRV);


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
    public void onEditClick(DocumentSnapshot documentSnapshot, int position) {
        Post post = documentSnapshot.toObject(Post.class);
        View view = LayoutInflater.from(this).inflate(R.layout.edit_post_dialog, null);
        EditText edtEditPost = view.findViewById(R.id.edtEditPost);
        edtEditPost.setText(post.getPostContent());

        AlertDialog dialog = new AlertDialog.Builder(MyPostsActivity.this)
                .setView(view)
                .setCancelable(false)
                .create();
        dialog.show();

        Button btnCancel = view.findViewById(R.id.btnCancelEdit);
        Button btnSave = view.findViewById(R.id.btnSaveEditPost);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String updatedPostContent = edtEditPost.getText().toString().trim();
                if (updatedPostContent.isEmpty()) {
                    edtEditPost.setError("content should not be empty");
                    return;
                }
                post.setPostContent(edtEditPost.getText().toString());
                PostDao postDao = new PostDao(MyPostsActivity.this);
                postDao.editPost(post, updatedPostContent);
            }
        });

    }

    @Override
    public void onCommentClick(DocumentSnapshot documentSnapshot, int position) {
        Post post = documentSnapshot.toObject(Post.class);
        Intent intent = new Intent(MyPostsActivity.this, CommentActivity.class);
        intent.putExtra("POST", post.getDocumentId());
        startActivity(intent);
    }
}
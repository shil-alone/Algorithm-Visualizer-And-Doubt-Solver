package com.codershil.algorithmvisualizer.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.codershil.algorithmvisualizer.R;
import com.codershil.algorithmvisualizer.adapters.CommentAdapter;
import com.codershil.algorithmvisualizer.daos.PostDao;
import com.codershil.algorithmvisualizer.models.Comment;
import com.codershil.algorithmvisualizer.models.Post;
import com.codershil.algorithmvisualizer.models.User;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class CommentActivity extends AppCompatActivity {
    String postId;
    RecyclerView commentRV;
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    CollectionReference commentRef;
    ImageView btnSendComment;
    EditText edtComment;
    SwipeRefreshLayout swipeRefreshLayout;
    User user;
    CommentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        initialize();
        setUpRecyclerView();
        addClicks();
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
        commentRV = findViewById(R.id.commentRV);
        btnSendComment = findViewById(R.id.btnSendComment);
        edtComment = findViewById(R.id.edtComment);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout2);
        getSupportActionBar().setTitle("Comments");
    }

    private void setUpRecyclerView() {
        postId = getIntent().getStringExtra("POST");
        commentRef = database.collection("posts").document(postId).collection("comments");
        Query query = commentRef.orderBy("createdAt", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Comment> options = new FirestoreRecyclerOptions.Builder<Comment>()
                .setQuery(query, Comment.class)
                .build();
        adapter = new CommentAdapter(options);
        commentRV.setHasFixedSize(true);
        commentRV.setLayoutManager(new LinearLayoutManager(CommentActivity.this));
        commentRV.setAdapter(adapter);


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                DocumentSnapshot documentSnapshot = adapter.getSnapshots().getSnapshot(viewHolder.getAdapterPosition());
                Comment comment = documentSnapshot.toObject(Comment.class);
                if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(comment.getUserUid())) {
                    adapter.deleteComment(viewHolder.getAbsoluteAdapterPosition());
                    PostDao postDao = new PostDao(CommentActivity.this);
                    postDao.deleteComment(comment);
                }

            }
        }).attachToRecyclerView(commentRV);


    }

    private void addClicks() {
        btnSendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = edtComment.getText().toString().trim();
                if (comment.isEmpty()) {
                    edtComment.setError("please enter comment first");
                    return;
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        database.collection("users")
                                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        user = documentSnapshot.toObject(User.class);
                                        Comment commentObj = new Comment(user.getName(), comment, user.getUid(), postId);
                                        PostDao postDao = new PostDao(CommentActivity.this);
                                        postDao.addComment(commentObj);
                                        edtComment.setText("");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(CommentActivity.this, "unable to get user data", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                });
                    }
                }).start();

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

}
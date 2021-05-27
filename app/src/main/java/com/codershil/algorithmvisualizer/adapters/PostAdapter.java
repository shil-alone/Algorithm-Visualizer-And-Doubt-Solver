package com.codershil.algorithmvisualizer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codershil.algorithmvisualizer.R;
import com.codershil.algorithmvisualizer.models.Post;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

public class PostAdapter extends FirestoreRecyclerAdapter<Post, PostAdapter.PostHolder> {
    private OnItemClickListener listener;

    public PostAdapter(@NonNull FirestoreRecyclerOptions<Post> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull PostHolder postHolder, int i, @NonNull Post post) {
        postHolder.txtName.setText(post.getUserName());
        postHolder.txtPostContent.setText(post.getPostContent());
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new PostHolder(view);
    }


    public void deletePost(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void onItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    class PostHolder extends RecyclerView.ViewHolder {

        TextView txtName, txtPostContent;

        public PostHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtPostContent = itemView.findViewById(R.id.txtPostContent);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener!=null ) {
                        listener.onItemClick(getSnapshots().getSnapshot(position),position);
                    }
                }
            });
        }

    }



}



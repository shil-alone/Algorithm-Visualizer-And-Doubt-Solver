package com.codershil.algorithmvisualizer.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codershil.algorithmvisualizer.R;
import com.codershil.algorithmvisualizer.models.Post;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

public class PostAdapter extends FirestoreRecyclerAdapter<Post, PostAdapter.PostHolder> {
    private OnItemClickListener listener;

    public PostAdapter(@NonNull FirestoreRecyclerOptions<Post> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull PostHolder postHolder, int i, @NonNull Post post) {
        postHolder.txtName.setText(post.getUserName());
        postHolder.txtPostContent.setText(post.getPostContent());
        postHolder.txtLikeCount.setText(String.valueOf(post.getLikedBy().size()));
        postHolder.txtCommentCount.setText(String.valueOf(post.getCommentedBy().size()));

        if (post.getLikedBy().contains(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            postHolder.btnLike.setImageResource(R.drawable.ic_liked);
        } else {
            postHolder.btnLike.setImageResource(R.drawable.ic_baseline_thumb_up_alt_24);
        }

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
        void onLikeClick(DocumentSnapshot documentSnapshot, int position);

        void onCommentClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void onItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    class PostHolder extends RecyclerView.ViewHolder {

        TextView txtName, txtPostContent, txtLikeCount, txtCommentCount;
        ImageView btnLike, btnComment;


        public PostHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtUserName);
            txtPostContent = itemView.findViewById(R.id.txtComment);
            btnLike = itemView.findViewById(R.id.imgLike);
            btnComment = itemView.findViewById(R.id.imgComments);
            txtLikeCount = itemView.findViewById(R.id.txtLikeCount);
            txtCommentCount = itemView.findViewById(R.id.txtCommentCount);


            btnLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onLikeClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });

            btnComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onCommentClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });

        }

    }


}



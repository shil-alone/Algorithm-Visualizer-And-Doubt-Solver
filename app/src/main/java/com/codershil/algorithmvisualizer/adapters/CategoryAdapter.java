package com.codershil.algorithmvisualizer.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codershil.algorithmvisualizer.R;
import com.codershil.algorithmvisualizer.activities.SearchingActivity;
import com.codershil.algorithmvisualizer.activities.SortingActivity;
import com.codershil.algorithmvisualizer.models.Category;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryHolder> {
    Context context;
    ArrayList<Category> categoryArrayList;

    public CategoryAdapter(Context context, ArrayList<Category> categoryArrayList) {
        this.context = context;
        this.categoryArrayList = categoryArrayList;
    }

    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        return new CategoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {
        holder.categoryImage.setImageResource(categoryArrayList.get(position).getImageUrl());
        holder.txtCategoryName.setText(categoryArrayList.get(position).getCategoryName());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.txtCategoryName.getText().equals("Searching")) {
                    context.startActivity(new Intent(context, SearchingActivity.class));
                } else if (holder.txtCategoryName.getText().equals("Sorting")) {
                    context.startActivity(new Intent(context, SortingActivity.class));
                }
                else {
                    Toast.makeText(context, "coming soon...", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryArrayList.size();
    }

}

class CategoryHolder extends RecyclerView.ViewHolder {
    ImageView categoryImage;
    TextView txtCategoryName;

    public CategoryHolder(@NonNull View itemView) {
        super(itemView);
        categoryImage = itemView.findViewById(R.id.imgCategory);
        txtCategoryName = itemView.findViewById(R.id.txtCategoryName);
    }
}
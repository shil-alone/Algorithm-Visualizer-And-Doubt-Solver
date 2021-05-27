package com.codershil.algorithmvisualizer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codershil.algorithmvisualizer.R;
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
        View view = LayoutInflater.from(context).inflate(R.layout.category_item, parent, false);
        return new CategoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {
        holder.categoryImage.setImageResource(categoryArrayList.get(position).getImageUrl());
        holder.txtCategoryName.setText(categoryArrayList.get(position).getCategoryName());
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
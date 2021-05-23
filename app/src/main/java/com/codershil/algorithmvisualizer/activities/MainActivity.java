package com.codershil.algorithmvisualizer.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.codershil.algorithmvisualizer.R;
import com.codershil.algorithmvisualizer.adapters.CategoryAdapter;
import com.codershil.algorithmvisualizer.models.Category;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView categoryRV;
    ArrayList<Category> categoryArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        categoryRV = findViewById(R.id.categoryRV);
        categoryArrayList.add(new Category(R.drawable.searching, "Searching"));
        categoryArrayList.add(new Category(R.drawable.sorting, "Sorting"));
        categoryRV.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        categoryRV.setAdapter(new CategoryAdapter(MainActivity.this, categoryArrayList));
    }

}
package com.codershil.algorithmvisualizer.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.codershil.algorithmvisualizer.R;
import com.codershil.algorithmvisualizer.adapters.CategoryAdapter;
import com.codershil.algorithmvisualizer.models.Category;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView categoryRV;
    CardView cardView;
    ArrayList<Category> categoryArrayList = new ArrayList<>();
    Boolean authFlag = false ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
        setOnClicks();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout){

            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.logout_dialog,null);
            Button btnCancel = view.findViewById(R.id.btnCancel);
            Button btnLogout = view.findViewById(R.id.btnLogout);

            AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                    .setView(view)
                    .create();
            dialog.show();

            btnLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ProgressDialog dialog = new ProgressDialog(MainActivity.this);
                    dialog.setMessage("signing out...");
                    dialog.setCancelable(false);
                    dialog.show();
                    FirebaseAuth auth ;
                    FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
                        @Override
                        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                            if (firebaseAuth.getCurrentUser() == null){
                                if (!authFlag) {
                                    authFlag = true ;
                                    dialog.dismiss();
                                    Toast.makeText(MainActivity.this, "logged out successfully", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                }

                            }
                        }
                    };
                    auth = FirebaseAuth.getInstance();
                    auth.addAuthStateListener(authStateListener);
                    auth.signOut();
                }
            });
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

        }

        return super.onOptionsItemSelected(item);
    }

    public void initialize(){
        categoryRV = findViewById(R.id.categoryRV);
        cardView = findViewById(R.id.cardView);
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#3F51B5"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);
        // changing the color of status bar
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.status_bar_login));
        }
        categoryArrayList.add(new Category(R.drawable.searching, "Searching"));
        categoryArrayList.add(new Category(R.drawable.searching, "Searching"));
        categoryArrayList.add(new Category(R.drawable.searching, "Searching"));
        categoryArrayList.add(new Category(R.drawable.searching, "Searching"));
        categoryArrayList.add(new Category(R.drawable.sorting, "Sorting"));
        categoryArrayList.add(new Category(R.drawable.sorting, "Sorting"));
        categoryArrayList.add(new Category(R.drawable.sorting, "Sorting"));
        categoryArrayList.add(new Category(R.drawable.sorting, "Sorting"));
        categoryRV.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        categoryRV.setAdapter(new CategoryAdapter(MainActivity.this, categoryArrayList));
    }

    public void setOnClicks(){
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,DoubtActivity.class));
            }
        });
    }
}
package com.codershil.algorithmvisualizer.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.SeekBar;

import com.codershil.algorithmvisualizer.R;
import com.codershil.algorithmvisualizer.algorithms.BinarySearch;
import com.codershil.algorithmvisualizer.algorithms.LinearSearch;
import com.codershil.algorithmvisualizer.utilities.DataUtils;
import com.codershil.algorithmvisualizer.visualizer.SortingVisualizer;

public class SearchingActivity extends AppCompatActivity {
    FrameLayout frameLayout;
    Button btnRandom, btnSearch;
    SeekBar seekBarSize, seekBarTime, seekBarIndex;
    int sizeOfArray = 40;
    SortingVisualizer sortingVisualizer;
    LinearSearch linearSearch;
    BinarySearch binarySearch;
    String algorithmName = "LINEAR";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searching);
        initialize();
        setClicks();
    }

    private void initialize() {
        frameLayout = findViewById(R.id.frameLayout1);
        btnRandom = findViewById(R.id.btnRandom);
        btnSearch = findViewById(R.id.btnSearch);
        seekBarSize = findViewById(R.id.seekBarSize);
        seekBarTime = findViewById(R.id.seekBarT);
        seekBarIndex = findViewById(R.id.seekBarIndex);
        sortingVisualizer = new SortingVisualizer(SearchingActivity.this);
        binarySearch = new BinarySearch(sortingVisualizer, sizeOfArray, SearchingActivity.this);
        linearSearch = new LinearSearch(sortingVisualizer, sizeOfArray, SearchingActivity.this);

        getSupportActionBar().setTitle("Searching Visualizer");
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#3F51B5"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);
        // changing the color of status bar
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.status_bar_login));
        }

        frameLayout.addView(sortingVisualizer);
        seekBarSize.setMax(100);
        seekBarIndex.setMax(sizeOfArray);
        seekBarSize.setProgress(this.sizeOfArray);
        seekBarTime.setMax(700);
        seekBarTime.setProgress(linearSearch.getTime());

    }


    private void setClicks() {
        btnRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sortingVisualizer.setRandomArray(DataUtils.generateRandomArray(sizeOfArray));
                btnSearch.setEnabled(true);
                switch (algorithmName) {
                    case "LINEAR":
                        linearSearch.setSorting(true);
                        seekBarTime.setProgress(linearSearch.getTime());
                        linearSearch = new LinearSearch(sortingVisualizer, sizeOfArray, SearchingActivity.this);
                        break;


                    case "BINARY":
                        binarySearch.setSorting(true);
                        seekBarTime.setProgress(binarySearch.getTime());
                        binarySearch = new BinarySearch(sortingVisualizer, sizeOfArray, SearchingActivity.this);
                        break;
                }
                sortingVisualizer.invalidate();

            }
        });


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSearch.setEnabled(false);
                switch (algorithmName) {
                    case "LINEAR":
                        linearSearch.setSorting(false);
                        linearSearch.search();
                        break;

                    case "BINARY":
                        binarySearch.setSorting(false);
                        binarySearch.search();
                        break;
                }
            }
        });


        seekBarSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                btnSearch.setEnabled(true);
                sizeOfArray = progress;
                switch (algorithmName) {
                    case "LINEAR":
                        seekBarIndex.setMax(progress);
                        linearSearch.setSorting(true);
                        linearSearch = new LinearSearch(sortingVisualizer, sizeOfArray, SearchingActivity.this);
                        sortingVisualizer.colComp(-1, -1);

                        break;

                    case "BINARY":
                        seekBarIndex.setMax(progress);
                        binarySearch.setSorting(true);
                        binarySearch = new BinarySearch(sortingVisualizer, sizeOfArray, SearchingActivity.this);
                        sortingVisualizer.colComp(-1, -1);
                        break;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        seekBarTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                switch (algorithmName) {
                    case "LINEAR":
                        linearSearch.setTime(Math.max(progress, 0));
                        break;

                    case "BINARY":
                        binarySearch.setTime(Math.max(progress, 0));
                        break;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBarIndex.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                btnSearch.setEnabled(true);
                sortingVisualizer.colComp(progress, -1);
                linearSearch.setSearchElement(progress);
                binarySearch.setSearchElement(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        btnSearch.setEnabled(true);
        linearSearch.setSorting(true);

        switch (item.getItemId()) {
            case R.id.linearSearch:
                linearSearch = new LinearSearch(sortingVisualizer, sizeOfArray, SearchingActivity.this);
                binarySearch.setSorting(true);
                sortingVisualizer.invalidate();
                btnSearch.setText("Linear Search");
                algorithmName = "LINEAR";
                break;
            case R.id.binarySearch:
                binarySearch = new BinarySearch(sortingVisualizer, sizeOfArray, SearchingActivity.this);
                binarySearch.setSorting(true);
                sortingVisualizer.invalidate();
                btnSearch.setText("Binary Search");
                algorithmName = "BINARY";
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
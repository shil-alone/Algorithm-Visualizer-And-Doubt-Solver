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

import com.codershil.algorithmvisualizer.algorithms.InsertionSort;
import com.codershil.algorithmvisualizer.algorithms.MergeSort;
import com.codershil.algorithmvisualizer.algorithms.QuickSort;
import com.codershil.algorithmvisualizer.algorithms.SelectionSort;
import com.codershil.algorithmvisualizer.utilities.DataUtils;
import com.codershil.algorithmvisualizer.R;
import com.codershil.algorithmvisualizer.algorithms.BubbleSort;
import com.codershil.algorithmvisualizer.visualizer.SortingVisualizer;

public class SortingActivity extends AppCompatActivity {

    FrameLayout frameLayout;
    Button btnShuffle,btnSort;
    SeekBar seekBar,seekBarTime;
    int sizeOfArray = 40;
    BubbleSort bubbleSort;
    SelectionSort selectionSort;
    InsertionSort insertionSort;
    QuickSort quickSort;
    MergeSort mergeSort;
    SortingVisualizer sortingVisualizer;
    String algorithmName = "BUBBLE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sorting);
        initialize();
        addOnClicks();
    }

    private void initialize() {
        frameLayout = findViewById(R.id.frameLayout);
        btnShuffle = findViewById(R.id.btnShuffle);
        btnSort = findViewById(R.id.btnBubbleSort);
        seekBar = findViewById(R.id.seekBar);
        seekBarTime = findViewById(R.id.seekBarTime);

        getSupportActionBar().setTitle("Sorting Visualizer");
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#3F51B5"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);
        // changing the color of status bar
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.status_bar_login));
        }

        // initializing sorting objects
        sortingVisualizer = new SortingVisualizer(SortingActivity.this);
        bubbleSort = new BubbleSort(sortingVisualizer, sizeOfArray, SortingActivity.this);

        selectionSort = new SelectionSort(sortingVisualizer, sizeOfArray, SortingActivity.this);
        insertionSort = new InsertionSort(sortingVisualizer, sizeOfArray, SortingActivity.this);
        quickSort = new QuickSort(sortingVisualizer, sizeOfArray, SortingActivity.this);
        mergeSort = new MergeSort(sortingVisualizer, sizeOfArray, SortingActivity.this);
        bubbleSort.setSorting(true);
        seekBarTime.setProgress(bubbleSort.getTime());
        bubbleSort = new BubbleSort(sortingVisualizer, sizeOfArray, SortingActivity.this);

        frameLayout.addView(sortingVisualizer);
        seekBar.setMax(200);
        seekBar.setProgress(this.sizeOfArray);
        seekBarTime.setMax(1000);
        seekBarTime.setProgress(bubbleSort.getTime());
    }

    private void addOnClicks() {
        btnShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortingVisualizer.setRandomArray(DataUtils.generateRandomArray(sizeOfArray));
                btnSort.setEnabled(true);
                switch (algorithmName) {
                    case "BUBBLE":
                        bubbleSort.setSorting(true);
                        seekBarTime.setProgress(bubbleSort.getTime());
                        bubbleSort = new BubbleSort(sortingVisualizer, sizeOfArray, SortingActivity.this);
                        break;

                    case "SELECTION":
                        selectionSort.setSorting(true);
                        seekBarTime.setProgress(selectionSort.getTime());
                        selectionSort = new SelectionSort(sortingVisualizer, sizeOfArray, SortingActivity.this);
                        break;
                    case "INSERTION":
                        insertionSort.setSorting(true);
                        seekBarTime.setProgress(insertionSort.getTime());
                        insertionSort = new InsertionSort(sortingVisualizer, sizeOfArray, SortingActivity.this);
                        break;
                    case "QUICK":
                        quickSort.setSorting(true);
                        seekBarTime.setProgress(quickSort.getTime());
                        quickSort = new QuickSort(sortingVisualizer, sizeOfArray, SortingActivity.this);
                        break;
                    case "MERGE":
                        mergeSort.setSorting(true);
                        seekBarTime.setProgress(mergeSort.getTime());
                        mergeSort = new MergeSort(sortingVisualizer, sizeOfArray, SortingActivity.this);
                        break;
                }
                sortingVisualizer.invalidate();
            }
        });

        btnSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSort.setEnabled(false);
                switch (algorithmName) {
                    case "BUBBLE":
                        bubbleSort.setSorting(false);
                        bubbleSort.sort();
                        break;

                    case "SELECTION":
                        selectionSort.setSorting(false);
                        selectionSort.sort();
                        break;
                    case "INSERTION":
                        insertionSort.setSorting(false);
                        insertionSort.sort();
                        break;
                    case "QUICK":
                        quickSort.setSorting(false);
                        quickSort.sort();
                        break;
                    case "MERGE":
                        mergeSort.setSorting(false);
                        mergeSort.sort();
                        break;
                }

            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                btnSort.setEnabled(true);
                sizeOfArray = progress;
                switch (algorithmName) {
                    case "BUBBLE":
                        bubbleSort.setSorting(true);
                        bubbleSort = new BubbleSort(sortingVisualizer, sizeOfArray, SortingActivity.this);
                        break;
                    case "SELECTION":
                        selectionSort.setSorting(true);
                        selectionSort = new SelectionSort(sortingVisualizer, sizeOfArray, SortingActivity.this);
                        break;
                    case "INSERTION":
                        insertionSort.setSorting(true);
                        insertionSort = new InsertionSort(sortingVisualizer, sizeOfArray, SortingActivity.this);
                        break;
                    case "QUICK":
                        quickSort.setSorting(true);
                        quickSort = new QuickSort(sortingVisualizer, sizeOfArray, SortingActivity.this);
                        break;
                    case "MERGE":
                        mergeSort.setSorting(true);
                        mergeSort = new MergeSort(sortingVisualizer, sizeOfArray, SortingActivity.this);
                        break;

                }
                sortingVisualizer.invalidate();
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
                    case "BUBBLE":
                        bubbleSort.setTime(Math.max(progress, 0));
                        break;
                    case "SELECTION":
                        selectionSort.setTime(Math.max(progress, 0));
                        break;
                    case "INSERTION":
                        insertionSort.setTime(Math.max(progress, 0));
                        break;
                    case "QUICK":
                        quickSort.setTime(Math.max(progress, 0));
                        break;
                    case "MERGE":
                        mergeSort.setTime(Math.max(progress, 0));
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

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sort_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        btnSort.setEnabled(true);
        bubbleSort.setSorting(true);
        insertionSort.setSorting(true);
        selectionSort.setSorting(true);
        quickSort.setSorting(true);
        mergeSort.setSorting(true);
        switch (item.getItemId()) {
            case R.id.bubbleSort:
                bubbleSort = new BubbleSort(sortingVisualizer, sizeOfArray, SortingActivity.this);
                sortingVisualizer.invalidate();
                btnSort.setText("Bubble Sort");
                algorithmName = "BUBBLE";
                break;
            case R.id.insertionSort:
                insertionSort = new InsertionSort(sortingVisualizer, sizeOfArray, SortingActivity.this);
                sortingVisualizer.invalidate();
                btnSort.setText("Insertion Sort");
                algorithmName = "INSERTION";
                break;
            case R.id.selectionSort:
                selectionSort = new SelectionSort(sortingVisualizer, sizeOfArray, SortingActivity.this);
                sortingVisualizer.invalidate();
                btnSort.setText("Selection Sort");
                algorithmName = "SELECTION";
                break;
            case R.id.quickSort:
                quickSort = new QuickSort(sortingVisualizer, sizeOfArray, SortingActivity.this);
                sortingVisualizer.invalidate();
                btnSort.setText("Quick Sort");
                algorithmName = "QUICK";
                break;
            case R.id.mergeSort:
                mergeSort = new MergeSort(sortingVisualizer, sizeOfArray, SortingActivity.this);
                sortingVisualizer.invalidate();
                btnSort.setText("Merge Sort");
                algorithmName = "MERGE";
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
package com.codershil.algorithmvisualizer.algorithms;

import android.app.Activity;

import com.codershil.algorithmvisualizer.utilities.DataUtils;
import com.codershil.algorithmvisualizer.visualizer.SortingVisualizer;

public class QuickSort extends SortingAlgorithm {

    public QuickSort(SortingVisualizer visualizer, int sizeOfArray, Activity activity) {
        this.visualizer = visualizer;
        this.activity = activity;
        randomArray = DataUtils.generateRandomArray(sizeOfArray);
        visualizer.setRandomArray(randomArray);
    }

    public void sort() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                quickSort(0, randomArray.length - 1);
                colComp(-1, -1);
                colSwap(-1, -1);
            }
        }).start();
    }

    private int partition(int low, int high) {
        int pivot = randomArray[high];
        colSwap(high, -1);
        delay(time);
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (!isSorting) {
                colComp(j, -1);
                colSwap(high, -1);
                delay(time);
                if (randomArray[j] <= pivot) {
                    i++;
                    swapIndex(randomArray, i, j);
                    colComp(i, j);
                    delay(time);
                }
            }

        }
        swapIndex(randomArray, i + 1, high);
        colSwap(i + 1, high);
        delay(time);
        colComp(-1, -1);
        colSwap(-1, -1);
        return i + 1;
    }


    private void quickSort(int low, int high) {
        if (low < high) {
            if (!isSorting) {
                int pivot = partition(low, high);
                quickSort(low, pivot - 1);
                quickSort(pivot + 1, high);
            }
        }
    }


}

package com.codershil.algorithmvisualizer.algorithms;

import android.app.Activity;
import android.util.Log;

import com.codershil.algorithmvisualizer.activities.SortingActivity;
import com.codershil.algorithmvisualizer.utilities.DataUtils;
import com.codershil.algorithmvisualizer.visualizer.SortingVisualizer;
import com.google.api.LogDescriptor;

public class InsertionSort extends SortingAlgorithm {

    int i, j;

    public InsertionSort(SortingVisualizer visualizer, int sizeOfArray, Activity activity) {
        this.visualizer = visualizer;
        this.activity = activity;
        randomArray = DataUtils.generateRandomArray(sizeOfArray);
        visualizer.setRandomArray(randomArray);
    }

    public void sort() {
        i = 0;
        j = 0;
        new Thread(new Runnable() {
            @Override
            public void run() {

                for (i = 1; i < randomArray.length; i++) {
                    int temp = randomArray[i];
                    j = i - 1;
                    while (j >= 0 && randomArray[j] > temp) {
                        if (!isSorting) {
                            colSwap(i,-1);
                            colComp(j,-1);
                            delay(time);
                            time = getTime();
                            randomArray[j + 1] = randomArray[j];
                            colComp(j,j+1);
                            delay(time);
                            j--;
                        }
                    }
                    randomArray[j + 1] = temp;
                    colSwap(i,j+1);
                    delay(time);
                }
                colComp(-1, -1);
                colSwap(-1, -1);
            }
        }).start();
    }


}

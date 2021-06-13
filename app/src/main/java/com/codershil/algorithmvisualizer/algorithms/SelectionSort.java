package com.codershil.algorithmvisualizer.algorithms;

import android.app.Activity;

import com.codershil.algorithmvisualizer.utilities.DataUtils;
import com.codershil.algorithmvisualizer.visualizer.SortingVisualizer;

public class SelectionSort extends SortingAlgorithm {

    int i, j;
    int high1 = -1;
    int high2 = -1;

    public SelectionSort(SortingVisualizer visualizer, int sizeOfArray, Activity activity) {
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

                for (i = 0; i < randomArray.length; i++) {
                    int min = i;
                    for (j = i + 1; j < randomArray.length; j++) {
                        if (!isSorting) {
                            time = getTime();
                            colComp(min,j);
                            delay(time);
                            if (randomArray[j] < randomArray[min]) {
                                high1 = j;
                                high2 = min;
                                swapIndex(randomArray,j,min);
                                colSwap(high1, high2);
                                delay(time);
                            }
                        }
                    }

                }
                colComp(-1, -1);
                colSwap(-1, -1);
            }
        }).start();
    }


}

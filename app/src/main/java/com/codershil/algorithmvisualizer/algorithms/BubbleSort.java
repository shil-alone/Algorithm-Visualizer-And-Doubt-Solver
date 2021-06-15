package com.codershil.algorithmvisualizer.algorithms;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;

import com.codershil.algorithmvisualizer.activities.SortingActivity;
import com.codershil.algorithmvisualizer.utilities.DataUtils;
import com.codershil.algorithmvisualizer.visualizer.SortingVisualizer;


public class BubbleSort extends SortingAlgorithm {
    int i, j;
    int high1 = -1;
    int high2 = -1;

    public BubbleSort(SortingVisualizer visualizer, int sizeOfArray, Activity activity) {
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
                    for (j = 0; j < randomArray.length -i- 1; j++) {
                        if (!isSorting) {
                            colComp(j,j+1);
                            delay(time);
                            if (randomArray[j] > randomArray[j + 1]) {
                                high1 = j;
                                high2 = j + 1;
                                swapIndex(randomArray,j,j+1);
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

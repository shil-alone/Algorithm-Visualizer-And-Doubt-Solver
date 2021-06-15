package com.codershil.algorithmvisualizer.algorithms;

import android.app.Activity;
import android.widget.Toast;

import com.codershil.algorithmvisualizer.utilities.DataUtils;
import com.codershil.algorithmvisualizer.visualizer.SortingVisualizer;

public class LinearSearch extends SortingAlgorithm {
    int searchElement=5;
    boolean isPresent;
    int i = 0;

    public LinearSearch(SortingVisualizer visualizer, int sizeOfArray, Activity activity) {
        this.visualizer = visualizer;
        this.activity = activity;
        randomArray = DataUtils.generateRandomArray(sizeOfArray);
        visualizer.setRandomArray(randomArray);
    }

    public void search() {
        i = 0;
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (i = 0; i < randomArray.length; i++) {
                    colComp(i,-1);
                    delay(time);
                    if (randomArray[i] == randomArray[searchElement]) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(activity, "element found at index " + i, Toast.LENGTH_SHORT).show();
                            }
                        });
                        colSwap(i,-1);
                        delay(time*3);
                        isPresent = true;
                        break;
                    }
                }
                if (!isPresent){
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity, "element not present in array", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
//                colSwap(-1,-1);
//                colComp(-1,-1);
            }
        }).start();
    }

    public void setSearchElement(int searchElement) {
        this.searchElement = searchElement;
    }

}

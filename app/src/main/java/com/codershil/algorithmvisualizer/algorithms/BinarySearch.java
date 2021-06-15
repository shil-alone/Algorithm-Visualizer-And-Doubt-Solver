package com.codershil.algorithmvisualizer.algorithms;

import android.app.Activity;
import android.widget.Toast;

import com.codershil.algorithmvisualizer.utilities.DataUtils;
import com.codershil.algorithmvisualizer.visualizer.SortingVisualizer;

import java.util.Collections;

public class BinarySearch extends SortingAlgorithm {
    int searchElement = 5;
    boolean isPresent;

    public BinarySearch(SortingVisualizer visualizer, int sizeOfArray, Activity activity) {
        this.visualizer = visualizer;
        this.activity = activity;
        randomArray = DataUtils.generateSortedArray(sizeOfArray);
        visualizer.setRandomArray(randomArray);
    }

    public void search() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int low = 0;
                int high = randomArray.length;
                delay(time * 2);

                while (low < high) {
                    int mid = (low + high) / 2;
                    colComp(mid, -1);
                    delay(time*2);
                    // checking at mid position
                    if (randomArray[mid] == randomArray[searchElement]) {
                        isPresent = true;
                        colSwap(mid,-1);
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(activity, "element found at index " + mid, Toast.LENGTH_SHORT).show();
                            }
                        });
                        delay(time * 3);
                        break;
                    }

                    // if element to search is greater than mid then search in right sub array
                    if (randomArray[mid] < randomArray[searchElement]) {
                        low = mid + 1;
                    } else {
                        high = mid - 1;
                    }
                    colComp(low, high);
                    delay(time * 2);

                }
                if (!isPresent) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity, "element not present in array", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    public void setSearchElement(int searchElement) {
        this.searchElement = searchElement;
    }


}
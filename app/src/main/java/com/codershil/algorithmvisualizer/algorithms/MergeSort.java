package com.codershil.algorithmvisualizer.algorithms;

import android.app.Activity;

import com.codershil.algorithmvisualizer.utilities.DataUtils;
import com.codershil.algorithmvisualizer.visualizer.SortingVisualizer;

public class MergeSort extends SortingAlgorithm {

    public MergeSort(SortingVisualizer visualizer, int sizeOfArray, Activity activity) {
        this.visualizer = visualizer;
        this.activity = activity;
        randomArray = DataUtils.generateRandomArray(sizeOfArray);
        visualizer.setRandomArray(randomArray);
    }

    public void sort() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mergeSort(0, randomArray.length - 1);
                colComp(-1, -1);
                colSwap(-1, -1);
            }
        }).start();
    }

    private void merge(int left, int mid, int right) {
        // calculating numbers of elements in right and left sub array
        int n1 = mid - left + 1;
        int n2 = right - mid;
        // creating left and right sub arrays
        int[] leftArray = new int[n1];
        int[] rightArray = new int[n2];

        // copying data into left  sub array
        for (int i = 0; i < n1; i++) {
            leftArray[i] = randomArray[left + i];
        }
        // copying data into left  sub array
        for (int j = 0; j < n2; j++) {
            rightArray[j] = randomArray[mid + j + 1];
        }
        // starting index of left array
        int i = 0;
        // starting index of right array
        int j = 0;
        // starting index of merged array
        int k = left;

        // merging left and right sub array into main array
        while (i < n1 && j < n2) {

            if (!isSorting) {
                colComp(left+i,mid+j);
                delay(time);
                if (leftArray[i] <= rightArray[j]) {
                    randomArray[k] = leftArray[i];
                    colSwap(k,left+i);
                    delay(time);
                    i++;
                } else {
                    randomArray[k] = rightArray[j];
                    colSwap(k,mid+j);
                    delay(time);
                    j++;
                }
                k++;
            }
        }

        // copy remaining elements of left array if any into  main array
        while (i < n1) {
            if (!isSorting) {
                randomArray[k] = leftArray[i];
                colSwap(k,left+i);
                delay(time);
                i++;
                k++;
            }
        }
        // copy remaining elements of right array if any into  main array
        while (j < n2) {
            if (!isSorting) {
                randomArray[k] = rightArray[j];
                colSwap(k,mid+j);
                delay(time);
                j++;
                k++;
            }
        }
        colComp(-1, -1);
        colSwap(-1, -1);
    }

    private void mergeSort(int left, int right) {
        if (left < right) {
            if (!isSorting) {
                int mid = left + (right - left) / 2;
                mergeSort(left, mid);
                mergeSort(mid + 1, right);
                merge(left, mid, right);
            }
        }
    }

}

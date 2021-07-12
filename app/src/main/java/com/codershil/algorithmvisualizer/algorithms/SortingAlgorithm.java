package com.codershil.algorithmvisualizer.algorithms;

import android.app.Activity;

import com.codershil.algorithmvisualizer.visualizer.SortingVisualizer;

public class SortingAlgorithm {
    SortingVisualizer visualizer;
    Activity activity;
    boolean isSorting = false;
    int[] randomArray;
    int time = 100;

    public void colSwap(int col1, int col2) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                visualizer.colSwap(col1, col2);
            }
        });
    }

    public void colIndex(int index) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                visualizer.colIndex(index);
            }
        });
    }

    public void colComp(int comp1, int comp2) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                visualizer.colComp(comp1, comp2);
            }
        });
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getTime() {
        return time;
    }

    public void setSorting(boolean sorting) {
        isSorting = sorting;
    }

    public boolean isSorting() {
        return isSorting;
    }

    public void swapIndex(int[] randomArray, int ind1, int ind2) {
        int temp = randomArray[ind1];
        randomArray[ind1] = randomArray[ind2];
        randomArray[ind2] = temp;
    }

    public void delay(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
    }

}

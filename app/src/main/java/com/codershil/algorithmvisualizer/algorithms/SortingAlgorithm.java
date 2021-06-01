package com.codershil.algorithmvisualizer.algorithms;

import android.app.Activity;

import com.codershil.algorithmvisualizer.visualizer.SortingVisualizer;

public class SortingAlgorithm {
    SortingVisualizer visualizer;
    Activity activity;
    boolean isSorting = false;
    int[] randomArray;
    int time = 100;

    public void colSwap(int col1,int col2){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                visualizer.colSwap(col1,col2);
            }
        });
    }
    public void colIndex(int high1,int high2){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                visualizer.colIndex(high1,high2);
            }
        });
    }

    public void setTime(int time){
        this.time = time;
    }

    public int getTime(){
        return time;
    }

    public void setSorting(boolean sorting) {
        isSorting = sorting;
    }

}

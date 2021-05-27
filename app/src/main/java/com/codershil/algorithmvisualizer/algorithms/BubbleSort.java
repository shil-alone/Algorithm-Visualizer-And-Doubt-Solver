package com.codershil.algorithmvisualizerdemo.algorithms;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import com.codershil.algorithmvisualizerdemo.utilities.DataUtils;
import com.codershil.algorithmvisualizerdemo.visualilzer.SortingVisualizer;

import java.util.Timer;
import java.util.TimerTask;


public class BubbleSort {
    SortingVisualizer visualizer;
    Activity activity;
    Thread thread;
    ExampleRunnable runnable;
    boolean isSorting = false;
    int[] randomArray;
    int i ,j;
    int high1 = -1 ;
    int high2 = -1;
    int time = 100;

    public BubbleSort(SortingVisualizer visualizer,int sizeOfArray, Activity activity){
        this.visualizer = visualizer;
        this.activity = activity;
        randomArray = DataUtils.generateRandomArray(sizeOfArray);
        visualizer.setRandomArray(randomArray);
    }
    
    public void colSwap(int col1,int col2){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                visualizer.colSwap(col1,col2);
            }
        });
    }
    public void colComp(int col1,int col2){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                visualizer.colComp(col1,col2);
            }
        });
    }


    
    public void sort(){
        i = 0 ; j = 0;
        runnable = new ExampleRunnable(time);
        thread = new Thread(runnable);
        thread.start();
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

    class ExampleRunnable implements Runnable{
        int timeInMillis;
        Handler mainHandler;
        public ExampleRunnable(int timeInMillis){
            this.timeInMillis = timeInMillis;
            mainHandler = new Handler(Looper.getMainLooper());
        }
        @Override
        public void run() {

                for (i = 0; i < randomArray.length; i++) {
                    for (j = 0; j < randomArray.length - 1; j++) {
                        if (!isSorting) {
                            timeInMillis = getTime();
                            if (randomArray[j] > randomArray[j + 1]) {
                                high1 = j;
                                high2 = j + 1;
                                colSwap(high1, high2);
                                int temp = randomArray[j];
                                randomArray[j] = randomArray[j + 1];
                                randomArray[j + 1] = temp;
                                try {
                                    Thread.sleep(timeInMillis);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                    }
                }
                colSwap(-1, -1);

        }
    }

    
}

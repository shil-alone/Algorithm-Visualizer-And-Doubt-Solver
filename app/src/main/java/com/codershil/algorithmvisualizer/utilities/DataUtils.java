package com.codershil.algorithmvisualizer.utilities;


import java.util.Random;

public class DataUtils {


    public static int[] generateRandomArray(int size) {
        int[] randomArray = new int[size];
        Random rnd = new Random();

        for (int i = 0; i < size; i++) {
            int randomNumber = rnd.nextInt(size);
            if (randomNumber == 0) {
                randomArray[i] = 1;
            } else if (randomNumber == size - 1) {
                if (randomNumber / 2 > 0) {
                    int newRandom = randomNumber - rnd.nextInt(randomNumber / 2);
                    if (newRandom < 0) {
                        randomArray[i] = randomNumber;
                    } else {
                        randomArray[i] = newRandom;
                    }
                } else {
                    randomArray[i] = randomNumber;
                }
            } else {
                randomArray[i] = randomNumber + 1;
            }
        }
        return randomArray;
    }


}

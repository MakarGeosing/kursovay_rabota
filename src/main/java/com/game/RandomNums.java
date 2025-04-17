package com.game;

import java.util.Random;

public class RandomNums {

    public static int randomCost(){
        Random rndCost = new Random();
        return rndCost.nextInt(1,5);
    }

    public static int randomNum(){
        Random rndNum = new Random();
        return rndNum.nextInt(14);
    }
}

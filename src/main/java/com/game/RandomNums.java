package com.game;

import java.util.Random;


public class RandomNums {

    public static int randomCost(){
        Random rndCost = new Random();
        return rndCost.nextInt(5,15);
    }

    public static int randomNum(int bound){
        Random rndNum = new Random();
        return rndNum.nextInt(bound);
    }
    public static int rndDmg(){
        Random rndDmg = new Random();
        return rndDmg.nextInt(1, 12);
    }
}

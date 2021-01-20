package com.scc363.hospitalproject.datamodels;

import java.util.Random;

public class MultiFactorAuthCodeGen {
    Random rand;

    public float getCode(){
        rand  = new Random();
        return rand.nextInt((9999 - 100) + 1) + 10;
    }

}

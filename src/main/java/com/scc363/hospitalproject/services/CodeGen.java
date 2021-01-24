package com.scc363.hospitalproject.services;

import java.util.Random;

public class CodeGen {
    Random rand = new Random();

    public int generateCode(){
        return rand.nextInt((9999 - 100) + 1) + 10;
    }
}

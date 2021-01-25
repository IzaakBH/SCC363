package com.scc363.hospitalproject.utils;

import java.util.Random;

public class CodeGen {

    Random rand = new Random();

    //It returns a randomly generated 4 digits number
    public int generateCode() {
        return rand.nextInt((9999 - 100) + 1) + 10;
    }
}

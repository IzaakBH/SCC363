package com.scc363.hospitalproject.services;

public class CodeGen {

    //It returns a randomly generated 4 digits number
    public int generateCode() {
        return rand.nextInt((9999 - 100) + 1) + 10;;
    }
}

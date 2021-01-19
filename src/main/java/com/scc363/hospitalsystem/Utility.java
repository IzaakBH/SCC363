package com.scc363.hospitalsystem;


public class Utility {

    public double evaluatePassword(String pass){
        double score = 0;
        char temp;
        int numOfSym = 0;
        int numOfNum = 0;
        int numOfUpp = 0;
        int numOfLow = 0;
        double lengthWeight = 30;
        double symbolWeight = 25;
        double numberWeight = 25;
        double letterWeight = 20;

        if(pass.length() < 11){
            return 0;
        }
        else if (pass.length() > 20){
            score += lengthWeight;
        }
        else{
            score += ((double)pass.length())/20 * lengthWeight;
        }

        for (int i = 0; i < pass.length(); i++) {
            temp = pass.charAt(i);

            if (Character.isDigit(temp)){
                numOfNum++;
            }
            else if (Character.isUpperCase(temp)){
                numOfUpp++;
            } 
            else if (Character.isLowerCase(temp)){
                numOfLow++;
            }
            else{
                numOfSym++;
            } 
        }

        if (numOfNum == 0 || numOfUpp == 0 || numOfLow == 0 || numOfSym < 2){
            return 0;
        }

        if (numOfUpp > 5){
            score += letterWeight/2;
        }
        else{
            score += (double)numOfUpp/5 * (letterWeight/2);
        }

        if (numOfLow > 5){
            score += letterWeight/2;
        }
        else{
            score += (double)numOfLow/5 * (letterWeight/2);
        }

        if (numOfNum > 6){
            score += numberWeight;
        }
        else{
            score += (double)numOfNum/6 * numberWeight;
        }

        if (numOfSym > 4){
            score += symbolWeight;
        }
        else{
            score += (double)numOfSym/4 * symbolWeight;
        }

        return score/100;
    }

}
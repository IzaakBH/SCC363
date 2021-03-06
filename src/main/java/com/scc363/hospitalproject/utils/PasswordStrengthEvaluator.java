package com.scc363.hospitalproject.utils;


public class PasswordStrengthEvaluator {
    /*
     Return password strength score between 0 and 1
     return 0 if password length is smaller than 11
            -1 if a sequence/repeat of 4 or more consecutive character is detected
            -2 if password doesn't contain number
            -3 if password doesn't contain uppercase
            -4 if password doesn't contain lowercase
            -5 if password doesn't contain at least 2 symbols
     */
    public static double evaluatePassword(String pass){
        double score = 0;
        char temp;
        int comp1;
        int comp2;
        int comp3;
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
            if (i > 2){
                comp1 = Character.compare(pass.charAt(i-3), pass.charAt(i-2));
                comp2 = Character.compare(pass.charAt(i-2), pass.charAt(i-1));
                comp3 = Character.compare(pass.charAt(i-1), pass.charAt(i));
                if (comp1 == -1 && comp2 == -1 && comp3 == -1 ||
                        comp1 == 1 && comp2 == 1 && comp3 == 1 ||
                        comp1 == 0 && comp2 == 0 && comp3 == 0){
                    return -1;
                }

            }

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

        if (numOfNum == 0){
            return -2;
        }
        else if(numOfUpp == 0){
            return -3;
        }
        else if(numOfLow == 0){
            return -4;
        }
        else if(numOfSym < 2){
            return -5;
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
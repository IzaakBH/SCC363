

function evaluatePassword(pass){
    var score = 0;
    var numOfSym = 0;
    var numOfNum = 0;
    var numOfUpp = 0;
    var numOfLow = 0;
    var lengthWeight = 30;
    var symbolWeight = 25;
    var numberWeight = 25;
    var letterWeight = 20;
    var spCharCheckFail = 0;
    var rating = "";
    var length = pass.value.length;
    if (length < 11) {
        document.getElementById("rating").innerHTML = "Password must be at least 11 in length";
        document.getElementById("submit").disabled = true;
        return;
    }
    else if (length > 20){
        score += lengthWeight;
    }
    else{
        score += length/20 * lengthWeight;
    }

    numOfUpp = (pass.value.match(/[A-Z]/g) || []).length;
    numOfLow = (pass.value.match(/[a-z]/g) || []).length;
    numOfNum = (pass.value.match(/[0-9]/g) || []).length;
    numOfSym = pass.value.length -numOfUpp - numOfLow - numOfNum;

    if(numOfNum == 0){
        rating = rating.concat("Number");
        spCharCheckFail++;
    }
    if(numOfUpp == 0){
        if(spCharCheckFail != 0){
            rating = rating.concat(", Uppercase ");
        }
        else{
            rating = rating.concat("Uppercase");
        }
        spCharCheckFail++;
    }
    if(numOfLow == 0){
        if(spCharCheckFail != 0){
            rating = rating.concat(", Lowercase");
        }
        else{
            rating = rating.concat("Lowercase");
        }
        spCharCheckFail++;
    }
    if(numOfSym < 2){
        if(spCharCheckFail != 0){
            rating = rating.concat(", 2 Symbols");
        }
        else{
            rating = rating.concat("2 Symbols");
        }
        spCharCheckFail++;
    }
    if(spCharCheckFail !=0){
        rating = "Password must contain ".concat(rating);
        document.getElementById("rating").innerHTML = rating;
        document.getElementById("submit").disabled = true;
        return;
    }


    if (numOfUpp > 5){
        score += letterWeight/2;
    }
    else{
        score += numOfUpp/5 * (letterWeight/2);
    }

    if (numOfLow > 5){
        score += letterWeight/2;
    }
    else{
        score += numOfLow/5 * (letterWeight/2);
    }

    if (numOfNum > 6){
        score += numberWeight;
    }
    else{
        score += numOfNum/6 * numberWeight;
    }

    if (numOfSym > 4){
        score += symbolWeight;
    }
    else{
        score += numOfSym/4 * symbolWeight;
    }


    if (score < 50 && score != 0){
        rating = "Weak Password";
    }
    else if(score <70){
        rating = "Good Password";
    }
    else{
        rating = "Strong Password"
    }
    document.getElementById("rating").innerHTML = rating;
    if (score > 50){
        document.getElementById("submit").disabled = false;
    }
    else{
        document.getElementById("submit").disabled = true;
    }
}


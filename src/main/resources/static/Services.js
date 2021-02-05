class CredentialManager
{

    isSupported()
    {
        if (typeof(sessionStorage) !== "undefined")
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    getCredentials()
    {
        let sessionID = sessionStorage.getItem("sessionID");
        let privateKey = sessionStorage.getItem("privateKey");
        let username = sessionStorage.getItem("username");

        return {"sessionID": sessionID !== null ? sessionID : "null", "privateKey": privateKey !== null ? privateKey : "null", "username": username !== null ? username : "null"};
    }


    setCredentials(sessionID, privateKey, username)
    {
        sessionStorage.setItem("sessionID", sessionID);
        sessionStorage.setItem("privateKey", privateKey);
        sessionStorage.setItem("username", username);
        alert("credentials set");
    }
}

class CommunicationManager
{

    constructor(target, data)
    {
        this.target = target;
        this.data = data;
    }


    makePostRequest(callback)
    {
        var xHttpHandler = new XMLHttpRequest();
        xHttpHandler.onreadystatechange = function()
        {
            if (this.readyState === 4 && this.status === 200)
            {
                let dataObject = JSON.parse(this.responseText);
                callback(dataObject)
            }
        };
        xHttpHandler.open("POST", this.target, true);
        xHttpHandler.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        xHttpHandler.send("data=" + this.data);
    }


}


class StateManger
{
    constructor(stateTarget, dataObj)
    {
        this.stateTarget = stateTarget;
        this.dataObj = dataObj;
    }


    changeState()
    {
        const form = document.createElement("form");
        form.method = "post";
        form.action = this.stateTarget;

        let dataObj = JSON.stringify([new CredentialManager().getCredentials(), this.dataObj]);
        const field = document.createElement("input");
        field.type = "hidden";
        field.name = "data";
        field.value = dataObj;
        form.appendChild(field);

        document.body.appendChild(form);
        form.submit();
    }
}


/*
function login()
{
    let username = document.getElementById("username");
    let password = document.getElementById("password");

    if (username.length > 0 && password.length > 0)
    {
        let requestObjectString = JSON.stringify([{"userName":username, "password":password}]);
        new CommunicationManager("http://localhost:8089/signin", requestObjectString).makePostRequest(handleResult());
    }
}

function handleResult(obj)
{
    if (obj.sessionID !== "undefined")
    {
        new CredentialManager().setCredentials(obj.sessionID, obj.privateKey, obj.username);

    }
}

 */
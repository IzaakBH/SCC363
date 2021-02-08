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
                callback(this.responseText);
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



function login(username, password)
{
    if (username.length > 0 && password.length > 0)
    {
        let requestObjectString = JSON.stringify([{"username":username, "password":password}]);
        new CommunicationManager("http://localhost:8080/createSession", requestObjectString).makePostRequest(handleResult);
    }
}

function handleResult(response)
{
    if (response !== undefined)
    {
    /*
        let dataObj = JSON.parse(response);
        if (dataObj.result === undefined)
        {
            new CredentialManager().setCredentials(dataObj.sessionID, dataObj.privateKey, dataObj.username);
            new StateManger("http://localhost:8080/controlPanel", {}).changeState();
        }
        else
        {
            alert("login failed");
        }
        */
        if (response == "success")
        {
            window.location.assign('http://localhost:8080/controlPanel');
        }
        else
        {
            alert("login failed");
        }
    }
    else
    {
        alert("login failed");
    }
}

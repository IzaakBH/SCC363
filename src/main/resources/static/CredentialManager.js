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

        return JSON.stringify([{"sessionID": sessionID !== null ? sessionID : "null", "privateKey": privateKey !== null ? privateKey : "null", "username": username !== null ? username : "null"}]);
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

// tests
function requestNewSession()
{
    new CommunicationManager("http://localhost:8080/auth", JSON.stringify([{"userName":"xavier", "password":"passwprd"}])).makePostRequest(setCredentials)
}

function setCredentials(obj)
{
    new CredentialManager().setCredentials(obj.sessionID, obj.privateKey, obj.username);
}

//tests
function testAuthentication()
{
    new CommunicationManager("http://localhost:8080/isAuth", new CredentialManager().getCredentials()).makePostRequest(showResult)
}

function showResult(obj)
{
    alert(obj.result);
}
class CommunicationManager
{

    constructor(target, data)
    {
        this.target = target;
        this.data = data;
    }


    makePostRequest()
    {
        var xHttpHandler = new XMLHttpRequest();
        xHttpHandler.onreadystatechange = function()
        {
            if (this.readyState === 4 && this.status === 200)
            {
                let dataObject = JSON.parse(this.responseText);
                localStorage.setItem("sessionID", sessionID);
                localStorage.setItem("publicKey", publicKey);
                window.location.assign('CheckSession.html');

            }
        };
        xHttpHandler.open("POST", this.target, true);
        xHttpHandler.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        xHttpHandler.send("data=" + this.data);
    }


}

class User
{
    constructor(userName, password)
    {
        this.userName = userName;
        this.password = password;
    }
}


new CommunicationManager("http://localhost:8080/auth", JSON.stringify(new User("xavier", "password"))).makePostRequest();
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
        if (response == "success")
        {
            window.location.assign('http://localhost:8080/home');
        }
        else
        {
            alert("login failed");
        }
}

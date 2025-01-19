//Auteur(s) JS : Maxime VALLET
//Version : 2.0


//Retrieve login and rights from session storage
login = sessionStorage.getItem("login");
Rights = sessionStorage.getItem("droits");

//S'ils n'existent pas, on attend la fin de l'auth
if(!login){
    document.addEventListener("TokenCheckFinished", (event) => {
        //Une fois l'auth terminée, on les récupèrent à nouveau
        login = sessionStorage.getItem("login");
        Rights = sessionStorage.getItem("droits");

        document.getElementById("id").innerHTML = login;
        document.getElementById("Rights").innerHTML = Rights;

        //Event listner boutton déconnexion
        DeleteToken();
    })
}
//S'ils existent, on les affichent
else{
    document.getElementById("id").innerHTML = login;
    document.getElementById("Rights").innerHTML = Rights; 

    //Event listner boutton déconnexion
    DeleteToken();
}


//Déconnexion de l'utilisateur
function DeleteToken(){
    login = sessionStorage.getItem("login");
    token = sessionStorage.getItem("token");

    //On cache le boutton si la personne n'est pas connectée
    if(login === "Pas connecté"){
        document.getElementById("submitDisconnect").style.display = 'none';
    }
    else{
        document.getElementById("submitDisconnect").style.display = 'block';
    }

    //Event listner boutton disconnect
    document.getElementById("submitDisconnect").onclick = function () {

        //Vérification auprès du Servlet
        fetch(`https://${window.ServerIP}:8443/SAE51/DeleteToken`, {
            method: "POST",
            headers: { "Content-Type": "application/json; charset=UTF-8" },
            body: JSON.stringify({ login: login, token: token, Test: false })
        }).then(response => response.json())
        .then(DeleteTokenResult);
    };
}

        

//Vérification de la réponse du servlet DeleteToken
function DeleteTokenResult(response){
    if(response.erreur === "none"){
        //Suppression des données dans sessionStorage
        sessionStorage.setItem("login", "");
        sessionStorage.setItem("rights", "");
        sessionStorage.setItem("token", "");
        sessionStorage.setItem("AccessiblePages", "");
        
        //Redirection vers login.html
        window.location.href = 'login.html';
    }
    else{
        console.log("Banner.js => DeleteTokenResult() => Erreur : "+response.erreur);
    }
}
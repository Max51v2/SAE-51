//Auteur(s) JS : Maxime VALLET
//Version : 1.6


//Retrieve login and rights from session storage
id = sessionStorage.getItem("login");
Rights = sessionStorage.getItem("droits");

//If they don't exist, the script wait for TokenCheck.js to finish running
if(!id){
    document.addEventListener("TokenCheckFinished", (event) => {
        //Once done, we retrieve them again and add them to the page
        id = sessionStorage.getItem("login");
        Rights = sessionStorage.getItem("droits");

        document.getElementById("id").innerHTML = id;
        document.getElementById("Rights").innerHTML = Rights; 
    })
}
//If they already exist, we display them directly
else{
    document.getElementById("id").innerHTML = id;
    document.getElementById("Rights").innerHTML = Rights; 
}


//Déconnexion de l'utilisateur
document.getElementById("submitDisconnect").onclick = function () {
    login = sessionStorage.getItem("login");
    token = sessionStorage.getItem("token");
        
    //Vérification auprès du Servlet
    fetch(`https://${window.ServerIP}:8443/SAE51/DeleteToken`, {
        method: "POST",
        headers: { "Content-Type": "application/json; charset=UTF-8" },
        body: JSON.stringify({ login: login, token: token, Test: false })
    }).then(response => response.json())
    .then(DeleteTokenResult);
};
        

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
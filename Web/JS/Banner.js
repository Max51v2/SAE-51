//Auteur(s) JS : Maxime VALLET
//Version : 1.2


document.getElementById("id").innerHTML = sessionStorage.getItem("login");
document.getElementById("Rights").innerHTML = sessionStorage.getItem("droits");
    
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
    
        //Redirection vers login.html
        window.location.href = 'login.html';
    }
    else{
        console.log("Banner.js => DeleteTokenResult() => Erreur : "+response.erreur);
    }
}
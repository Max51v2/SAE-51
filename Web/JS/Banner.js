//Auteur(s) JS : Maxime VALLET
//Version : 1.0

document.addEventListener("DOMContentLoaded", (event) => {

    //Ne pas toucher hors bug ou modif esthétique
    
    
    //Définition du contenu de la banière quand TokenCheck a fini de charger
    document.addEventListener("TomcatTestFinished", function() {
        document.getElementById("id").innerHTML = sessionStorage.getItem("login");
        document.getElementById("Rights").innerHTML = sessionStorage.getItem("droits");
    });
    
    
    //Redirection vers la page d'aide
    document.getElementById("submitHelp").onclick = function () {
        //Récuppèration de la page d'origine (bouton retour)
        var currentUrl = window.location.href;
        var url = new URL(currentUrl);
        PreviousPage = url.pathname.split('/').pop();
        sessionStorage.setItem("PreviousPage", PreviousPage);

        window.location.href = 'help.html';
    };
    
    //Déconnexion de l'utilisateur
    document.getElementById("submitDisconnect").onclick = function () {
        login = sessionStorage.getItem("login");
        token = sessionStorage.getItem("token");
    
        //Vérification auprès du Servlet
        fetch(`https://${window.ServerIP}:8443/SAE51/DeleteToken`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
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
            console.log("accueil => DeleteTokenResult => Erreur : "+response.erreur);
        }
    }
});    
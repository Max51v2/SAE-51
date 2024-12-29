//Auteur : Maxime VALLET
//Version : 2.3


//Token
let token = sessionStorage.getItem('token');

// Attend que le test de Tomcat soit terminé
document.addEventListener("TomcatTestFinished", function() {

    //Si le serveur Tomcat est actif alors on vérifie le token
    if (window.TomcatOK === true) {
        TokenCheck();
    } 
    else{
        //Si Tomcat est down et que l'on édit pas en local on renvoi l'utilisateur vers la page de login
        if(window.localEditing === false){
            //Pas de redirection si sur la page d'accueil
            if(window.currentPage === "help.html"){
                //Rien
            }
            else{
                //Redirection vers la page de login
                goToLogin();
            }
        }
        else{
            console.log("TokenCheck.js => Info : l'authentification et la redirection sont désactivés")
        }
    }
});



//Vérification de l'existance du token dans sessionStorage
function TokenCheck(){
    if (token) {
        //Vérification du token auprès du Servlet
        fetch(`https://${window.ServerIP}:8443/SAE51/CheckToken`, {
            method: "POST",
            headers: { "Content-Type": "application/json; charset=UTF-8" },
            body: JSON.stringify({ token: token, Test: false })
        })
        .then(response => response.json())
        .then(CheckTokenResult);
    } 
    else {
        console.log("TokenCheck.js => TokenCheck() => Info : token inexistant (sessionStorage)");

        //Redirection
        GetRedirection();
    }
}


//Vérification du résultat fourni par le servlet CheckToken
function CheckTokenResult(response) {
    //Vérification d'erreur
    if(response.erreur === "none"){
        //Stockage du login et des droits (uniquement à titre indicatif car les servlets revérifient !)
        sessionStorage.setItem("login", response.login);
        sessionStorage.setItem("droits", response.droits);

        //Redirection page
        GetRedirection();
    }
    else{
        //Suppression du token stocké s'il est érroné
        console.log("TokenCheck.js => CheckTokenResult() => Info : suppression token");
        sessionStorage.setItem("token", "");

        //Redireciton vers login.html afin de se reconnecter
        goToLogin();
    }
}


//Redirection page
function GetRedirection(){
    fetch(`https://${window.ServerIP}:8443/SAE51/GetRedirection`, {
        method: "POST",
        headers: { "Content-Type": "application/json; charset=UTF-8" },
        body: JSON.stringify({ token: token, currentPage: window.currentPage, Test: false })
        }).then(response => response.json())
        .then(GetRedirectionResult);
}


//Vérification du résultat du Sevlet GetRedirection
function GetRedirectionResult(response){
    //Vérification d'erreur
    if(response.erreur === "none"){
        console.log("tokenCheck.js => GetRedirectionResult() => Info : redirection vers \""+response.redirect+"\"");

        //Redirection
        if(response.redirect === "none"){
            //événement qui lance les scripts de la page
            document.dispatchEvent(new Event("TokenCheckFinished"));
        }
        else{
            window.location.href = response.redirect;
        }
    }
    else{
        //On affiche l'erreur
        console.log("tokenCheck.js => GetRedirectionResult() => Erreur : "+response.erreur);

        //Redirection vers le login s'il n'y pas de règle de redirection dans la BD
        if(response.erreur === "Pas de redirection (BD)"){
            //Redirection vers login.html qui en théorie en a bien une
            goToLogin();
        }
    }
}


//Retour au login
function goToLogin(){
    if(window.currentPage === "login.html"){
        //Rien
    }
    else{
        //Redireciton vers login.html
        window.location.href = 'login.html';
    }
}
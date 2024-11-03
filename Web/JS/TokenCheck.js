//Auteur : Maxime VALLET
//Version : 2.0


//Token
let token = sessionStorage.getItem('token');

// Attend que le test de Tomcat soit terminé
document.addEventListener("TomcatTestFinished", function() {
    let TomcatOK = sessionStorage.getItem("TomcatOK");

    //Si le serveur Tomcat est actif alors on vérifie le token
    if (TomcatOK === "true") {
        TokenCheck();
    } 
    else {
        console.log("TokenCheck => Info : l'authentification et la redirection sont désactivés")
        
        //Si Tomcat est down et que l'on édit pas en local on renvoi l'utilisateur vers la page de login
        if(window.localEditing === false){
            //Redirection vers la page de login
            goToLogin();
        }
        else{
            //Rien
        }
    }
});



//Vérification de l'existance du token dans sessionStorage
function TokenCheck(){
    if (token) {
        if(token === ""){
            

            console.log("redirection")
        }
        else{
            console.log("TokenCheck => token : "+token);
    
            //Vérification du token auprès du Servlet
            fetch(`https://${ServerIP}:8443/SAE51/CheckToken`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ token: token, Test: false })
            })
            .then(response => response.json())
            .then(CheckTokenResult);
        }
    } 
    else {
        console.log("TokenCheck => erreur : token inexistant");
    
        token = "";

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
        console.log("TokenCheck => CheckTokenResult => suppression token");
        sessionStorage.setItem("token", "");

        //Redireciton vers login.html afin de se reconnecter
        goToLogin();
    }
}


//Redirection page
function GetRedirection(){
    fetch(`https://${window.ServerIP}:8443/SAE51/GetRedirection`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ token: token, currentPage: window.currentPage, Test: false })
        }).then(response => response.json())
        .then(GetRedirectionResult);
}


//Vérification du résultat du Sevlet GetRedirection
function GetRedirectionResult(response){
    //Vérification d'erreur
    if(response.erreur === "none"){
        console.log("tokenCheck => GetRedirectionResult => redirection : "+response.redirect);

        //Redirection
        if(response.redirect === "none"){
            //Rien
        }
        else{
            window.location.href = response.redirect;
        }
    }
    else{
        //On affiche l'erreur
        console.log("tokenCheck => GetRedirectionResult => erreur : "+response.erreur);

        //Redirection vers le login s'il n'y pas de règle de redirection dans la BD
        if(response.erreur === "Pas de redirection (BD)"){
            //Redireciton vers login.html qui en théorie en a bien une
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
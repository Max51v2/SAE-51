//Auteur : Maxime VALLET
//Version : 1.0


//Warning : URL.js doit être démarré avant TokenCheck.js (fait dans le template)


//Vérification du token
let token = sessionStorage.getItem('token');

//Vérification de l'existance du token dans sessionStorage
if (token) {
    if(token === ""){
        //Redireciton vers login.html afin de se reconnecter
        goToLogin();
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

    //Redireciton vers login.html afin de se reconnecter
    goToLogin();
}


//Vérification du résultat fourni par le servlet CheckToken
function CheckTokenResult(response) {
    console.log("TokenCheck => CheckTokenResult => resultat req :");
    console.log(response);

    //Vérification d'erreur
    if(response.erreur === "none"){
        //Stockage du login et des droits (uniquement à titre indicatif car les servlets revérifient !)
        sessionStorage.setItem("login", response.login);
        sessionStorage.setItem("droits", response.droits);

        //Redirection page
        fetch(`https://${ServerIP}:8443/SAE51/GetRedirection`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ token: token, currentPage: window.currentPage, Test: false })
        }).then(response => response.json())
        .then(GetRedirectionResult);
    }
    else{
        //Suppression du token stocké s'il est érroné
        console.log("TokenCheck => CheckTokenResult => suppression token");
        sessionStorage.setItem("token", "");

        //Redireciton vers login.html afin de se reconnecter
        goToLogin();
    }
}


//Vérification du résultat du Sevlet GetRedirection
function GetRedirectionResult(response){
    console.log("TokenCheck => GetRedirectionResult => resultat req :");
    console.log(response);

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
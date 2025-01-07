//Auteur JS : Maxime VALLET
//Version : 1.0

//Code ici
document.addEventListener("TokenCheckFinished", (event) => {

    //Redirection vers la page d'aide
    document.getElementById("submitHelp").onclick = function () {
        window.location.href = 'help.html';

        var currentUrl = window.location.href;
        var url = new URL(currentUrl);
        PreviousPage = url.pathname.split('/').pop();
        if(PreviousPage == ""){
            PreviousPage = "login.html";
        }
        sessionStorage.setItem("PreviousPage", PreviousPage);
    };
    
    //Vérification du MDP
    document.getElementById("submit").onclick = function () { 

        //Données fournies par l'utilisateur
        var login = document.getElementById("login").value;
        var password = document.getElementById("password").value;

        //Reset form
        document.getElementById("loginForm").reset();

        //Vérification auprès du Servlet
        fetch(`https://${window.ServerIP}:8443/SAE51/CheckPassword`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ login: login, password: password, Test: false })
        }).then(response => response.json())
        .then(CheckPasswordResult);
    };
        

    //Vérification de la réponse du Servlet CheckPassword
    function CheckPasswordResult(response){
        if(response.erreur === "none"){
            //Stockage du token, login et des droits (uniquement à titre indicatif car les servlets revérifient !)
            sessionStorage.setItem("login", response.login);
            sessionStorage.setItem("droits", response.droits);
            sessionStorage.setItem("token", response.token);

            //Redirection page
            currentToken = sessionStorage.getItem("token");
            fetch(`https://${window.ServerIP}:8443/SAE51/GetRedirection`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ token: currentToken, currentPage: window.currentPage, Test: false })
            }).then(response => response.json())
            .then(GetRedirectionResult);
        }
        else{
            //On affiche l'erreur
            console.log("login => CheckPasswordResult => Erreur : "+response.erreur);
        }
    }


    //Vérification du résultat du Sevlet GetRedirection
    function GetRedirectionResult(response){
        if(response.erreur === "none"){
            if(response.redirect === "none"){
                //Rien
            }
            else{
                window.location.href = response.redirect;
            }
        }
        else{
            //On affiche l'erreur
            console.log("login => GetRedirectionResult => Erreur : "+response.erreur);

            //Redirection vers le login s'il n'y pas de règle de redirection dans la BD
            if(response.erreur === "Pas de redirection (BD)"){
                //Redireciton vers login.html qui en théorie en a bien une
                window.location.href = 'login.html';
            }
        }
    }
});
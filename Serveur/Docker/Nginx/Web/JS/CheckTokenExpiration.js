//Auteur(s) JS : Maxime VALLET
//Version : 1.0

document.addEventListener("TokenCheckFinished", (event) => {

    //Durée d'un cycle de vérification des tokens
    let CheckIntervall = sessionStorage.getItem('CheckIntervall');

    //Valeur par défaut s'il n'y a pas de token (raisons)
    if(!token){
        token = "rien"
        login = "rien"
    }

    //On ne renvoi pas l'utilisateur vers le login s'il n'a pas de token et qu'il est sur la page d'aide car ça ne sert à rien
    if (window.currentPage === "help.html" & token === "rien"){
        skipGoToLogin = true
    }
    else{
        skipGoToLogin = false
    }

    notify = true
    tokenStatus = "valid"

    //Si on possède déjà la valeur de CheckIntervall
    if (CheckIntervall) {
        //Lancement de la vérification d'expiration de session
        TokenStatusCheck()
    } 
    else {
        //Récupération de CheckIntervall
        fetch(`https://${window.ServerIP}:8443/SAE51/GetCheckIntervall`, {
            method: "POST",
            headers: { "Content-Type": "application/json; charset=UTF-8" },
            body: JSON.stringify({ token: token, Test: false })
        })
        .then(response => response.json())
        .then(GetCheckIntervallResult);
    }


    function GetCheckIntervallResult(response){
        //S'il n'y a pas d'erreur
        if(response.erreur === "none"){
            //Stockage de CheckIntervall
            sessionStorage.setItem('CheckIntervall', response.CheckIntervall)
            CheckIntervall = response.CheckIntervall

            //Lancement de la vérification d'expiration de session
            TokenStatusCheck()
        }
        else{
            //Affichage de l'erreur
            console.log("CheckTokenExpiration.js => GetCheckIntervallResult() => Erreur : "+response.erreur)
        }
    }


    //Vérifie si le token a atteint le seuil d'alerte de token sur le point d'expirer
    async function TokenStatusCheck(){

        console.log("CheckTokenExpiration.js => TokenStatusCheck() => Info : Vérification de la validité de la session lancée (vérification toutes les "+(CheckIntervall/1000)+"s)")

        while(tokenStatus !== "expired"){
            //On vérifie le status du token à chaque intervalle d'expiration de ceux-ci
            await sleep(CheckIntervall)

            //Récupération de CheckIntervall
            fetch(`https://${window.ServerIP}:8443/SAE51/GetTokenStatus`, {
                method: "POST",
                headers: { "Content-Type": "application/json; charset=UTF-8" },
                body: JSON.stringify({ token: token, Test: false })
            })
            .then(response => response.json())
            .then(GetTokenStatusResult);
        }
    }


    function sleep(ms) {
        return new Promise(resolve => setTimeout(resolve, ms));
    }


    function GetTokenStatusResult(response){

        //S'il n'y a pas d'erreur
        if(response.erreur === "none"){

            tokenStatus = response.tokenStatus
            
            if(tokenStatus === "valid"){
                notify = true
            }
            else if(tokenStatus === "expired"){
                //Suppression du token
                DeleteToken();
            }
            else if(tokenStatus === "almostExpired"){
                if(notify == true){
                    const reponse = confirm("Votre session va expirer dans "+response.timeLeft+"s. Souhaitez-vous la prolonger ?");
    
                    if (reponse) {
                        //Renouvellement du token
                        TokenCheck();
                    } 
                    else {
                        //Suppression du token
                        DeleteToken();
                    }

                    notify = false
                }
            }
        }
        else{
            //Affichage de l'erreur
            console.log("CheckTokenExpiration.js => GetTokenStatusResult() => Erreur : "+response.erreur)
        }
    }


    function DeleteToken(){
        if (skipGoToLogin == true){
            //On ne fait rien si l'utilisateur est sur la page d'aide et qu'il n'a pas de token
        }
        else{
            login = sessionStorage.getItem("login");
            token = sessionStorage.getItem("token");

            //Vérification auprès du Servlet
            fetch(`https://${window.ServerIP}:8443/SAE51/DeleteToken`, {
                method: "POST",
                headers: { "Content-Type": "application/json; charset=UTF-8" },
                body: JSON.stringify({ login: login, token: token, Test: false })
            }).then(response => response.json())
            .then(DeleteTokenResult);
        }
    }


    //Vérification de la réponse du servlet DeleteToken
    function DeleteTokenResult(response){
        if(response.erreur === "none"){
            //Suppression des données dans sessionStorage
            sessionStorage.setItem("login", "");
            sessionStorage.setItem("rights", "");
            sessionStorage.setItem("token", "");
        
            //Redirection vers login.html
            goToLogin();
        }
        else{
            console.log("CheckTokenExpiration.js => DeleteTokenResult() => Erreur : "+response.erreur);

            //Redirection vers login.html
            goToLogin();
        }
    }


    //Vérification de l'existance du token dans sessionStorage
    //Ce servlet reset TokenLifeCycle (plus rapide que de refaire un servlet juste pour ça)
    function TokenCheck(){
        //Vérification du token auprès du Servlet
        fetch(`https://${window.ServerIP}:8443/SAE51/CheckToken`, {
            method: "POST",
            headers: { "Content-Type": "application/json; charset=UTF-8" },
            body: JSON.stringify({ token: token, Test: false })
        })
        .then(response => response.json())
        .then(CheckTokenResult);
    }


    //Vérification du résultat fourni par le servlet CheckToken
    function CheckTokenResult(response) {
        //Vérification d'erreur
        if(response.erreur === "none"){
            //Stockage du login et des droits (uniquement à titre indicatif car les servlets revérifient !)
            sessionStorage.setItem("login", response.login);
            sessionStorage.setItem("droits", response.droits);
        }
        else{
            //Suppression du token stocké s'il est érroné
            console.log("CheckTokenExpiration.js => CheckTokenResult() => Info : suppression token");
            sessionStorage.setItem("token", "");

            //Redireciton vers login.html afin de se reconnecter
            goToLogin();
        }
    }


    //Retour au login
    function goToLogin(){
        if(window.currentPage === "login.html"){
            //Rien
        }
        else if(skipGoToLogin == true){
            //Rien
        }
        else{
            //Redireciton vers login.html
            window.location.href = 'login.html';
        }
    }
})
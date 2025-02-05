//Auteur JS : Maxime VALLET
//Version : 1.0


ScriptIndex=1;

//Sert à relancer le test Tomcat et l'authentification quand Tomcat ne répond pas
function reloadScript(scriptName) {
    //Retrait des anciens scripts si applicable
    const existingScript = document.querySelector(`script[src="JS/${scriptName}.js?t=${ScriptIndex-1}"]`);
    if (existingScript) {
        existingScript.remove();
    }

    //Ajout des scripts
    let script = document.createElement("script");
    script.src = "JS/" + scriptName + ".js?t="+ScriptIndex;
    script.async = true;
    document.body.appendChild(script);

    ScriptIndex = ScriptIndex+1;
}

async function Refresh(){
    //délais entre 2 requêtes
    await Wait(5000);

    //clear console car sinon les erreurs et logs s'accumulent
    console.clear()

    //Rechargement des scripts
    console.log("login => Event : TomcatTestFinished => Reload TokenCheck.js et URL.js");
    reloadScript("TokenCheck");
    reloadScript("URL");
}

function Wait(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}


document.addEventListener("TomcatTestFinished", (event) => {

    //Si le test du serveur Tomcat a échoué
    if (window.TomcatOK == false) {
        //Afichage de la bannière
        document.getElementById("helpBan").style.visibility = "visible";
        
        //Ajout du texte de la bannière
        document.getElementById('helpP').innerHTML = `Impossible d'accéder au serveur Tomcat. <br>Merci de vérifier qu'il est allumé et que la clé SSL est validée : <a href="https://${window.ServerIP}:8443">https://${window.ServerIP}:8443</a>`;

        //Rechargement des scripts
        Refresh();
    }
    else{
        //Retrait de la bannière
        document.getElementById("helpBan").style.visibility = "hidden";
    }
});


document.addEventListener("TokenCheckFinished", (event) => {
    
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
        headers: { "Content-Type": "application/json; charset=UTF-8" },
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
            headers: { "Content-Type": "application/json; charset=UTF-8" },
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
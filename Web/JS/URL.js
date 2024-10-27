//Auteur : Maxime VALLET
//Version : 1.0


//Refresh la page quand on reviens en arrière sinon le script ne tourne pas
window.onunload = function(){reload()};



// Adresse IP des serveurs (vu que l'adresse du serveur change et que les deux serveurs on la mm adresse, on récupere celle entrée pour acceder au serv Nginx)
var currentUrl = window.location.href;
var url = new URL(currentUrl);
window.ServerIP = url.hostname;
window.currentPage = url.pathname.split('/').pop();
window.localEditing = false;



//Si ouvert en local => pas d'adresse IP
if(window.ServerIP === ""){
    console.log("URL => Info : vous éditez le fichier en local")

    //Définition d'un dresse par défaut
    window.ServerIP = "localhost";

    window.localEditing = true;
}



//Test de l'accès à Tomcat
TomcatTest();

async function TomcatTest() {
    try {
        await fetch(`https://${window.ServerIP}:8443/SAE51/TestTomcat`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({})
        })
        .then(response => response.json())
        .then(TestTomcatResult)
        .catch(error => {
            console.log("URL => Erreur => Le serveur Tomcat ne répond pas");
            sessionStorage.setItem("TomcatOK", "false");
        });
    } catch (error) {
        //Rien
    } finally {
        //Déclenche un événement personnalisé pour notifier que le test est terminé
        document.dispatchEvent(new Event("TomcatTestFinished"));
    }
}





//Vérification du résultat fourni par le servlet
function TestTomcatResult(response){
    if(response.erreur === "none"){
        console.log("URL => Info => Tomcat OK")
        sessionStorage.setItem("TomcatOK", "true");
    }
}
//Auteur : Maxime VALLET
//Version : 1.8


//Refresh la page quand on reviens en arrière sinon le script ne tourne pas
window.onunload = function(){reload()};

// Adresse IP des serveurs
var currentUrl = window.location.href;
var url = new URL(currentUrl);
window.ServerIP = url.hostname;
window.localEditing = false;
window.currentPage = url.pathname.split('/').pop();


//Vérification de la page act (si vide on est dans / aka login.html)
if(window.currentPage == ""){
    window.currentPage = "login.html";
}


//Si ouvert en local => pas d'adresse IP
if(window.ServerIP === ""){
    console.log("URL.js => Info : vous éditez le fichier en local")

    //Définition d'un dresse par défaut
    window.ServerIP = "192.168.1.31";

    window.localEditing = true;
}
else{
    //Rien
}

//Test Tomcat
TomcatTest();


//Test de l'accès à Tomcat
async function TomcatTest() {
    try {
        await fetch(`https://${window.ServerIP}:8443/SAE51/TestTomcat`, {
            method: "POST",
            headers: { "Content-Type": "application/json; charset=UTF-8" },
            body: JSON.stringify({})
        })
        .then(response => response.json())
        .then(TestTomcatResult)
        .catch(error => {
            console.log("URL.js => TomcatTest() => Erreur : le serveur Tomcat ne répond pas");
            window.TomcatOK = false;
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
        console.log("URL.js => TestTomcatResult() => Info : Tomcat OK")
        window.TomcatOK = true;
    }
}
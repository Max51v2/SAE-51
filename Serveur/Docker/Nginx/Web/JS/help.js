//Auteur(s) JS : Maxime VALLET
//Version : 1.2

    
document.addEventListener("TokenCheckFinished", (event) => {

    //Redirection vers la page contenant le javadoc
    document.getElementById("submitJavadoc").onclick = function () {
        window.location.href = "./Javadoc/index.html";
    };

    //Redirection vers la page contenant la doc VM
    document.getElementById("submitVMDoc").onclick = function () {
        window.location.href = "./documentation/Doc_VM.txt";
    };

    //Redirection vers la page contenant la doc serveur
    document.getElementById("submitServerDoc").onclick = function () {
        window.location.href = "./documentation/Doc_Serveur.txt";
    };

    //Redirection vers la page contenant la doc client
    document.getElementById("submitClientDoc").onclick = function () {
        window.location.href = "./documentation/Doc_Client.txt";
    };

    //Redirection vers la page contenant la doc web
    document.getElementById("submitWebDoc").onclick = function () {
        window.location.href = "./documentation/Doc_Web.txt";
    };

    //Redirection vers la page contenant la doc serveur
    document.getElementById("submitProjectDoc").onclick = function () {
        window.location.href = "./documentation/Doc_Projet.txt";
    };
});
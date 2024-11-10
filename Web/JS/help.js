//Auteur(s) JS : Maxime VALLET
//Version : 0.2

    
document.addEventListener("DOMContentLoaded", (event) => {

    //Redirection vers la page de login
    document.getElementById("submitLogin").onclick = function () {
        //Récuppération de la page précédente
        PreviousPage = sessionStorage.getItem("PreviousPage");

        //Vérification du contenu de la variable (si null => login.html)
        if(PreviousPage){
            window.location.href = PreviousPage;
        }
        else{
            window.location.href = "login.html";
        }
    };


    //Redirection vers la page contenant le javadoc
    document.getElementById("submitJavadoc").onclick = function () {
        window.location.href = "./Javadoc/index.html";
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
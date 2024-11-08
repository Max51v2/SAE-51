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


    // CODE ICI

});
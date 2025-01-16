//Auteur(s) JS : Maxime VALLET
//Version : 1.0


//Dictionnaire contenant les bouttons à ajouter selon le nom des pages renvoyés par le servlet
var ButtonDict = {
    "alerts.html": '<a href="./alerts.html" class="active"><i>🚨</i> Alertes & Seuils</a>',
    "ChangePassword.html": '<a href="./ChangePassword.html"><i>🔑</i> Modification du MDP</a>',
    "control.html": '<a href="./control.html"><i>⚙️</i> Allumage & Extinction</a>',
    "help.html": '<a href="./help.html"><i>❓</i> Page d\'aide</a>',
    "monitoring.html": '<a href="./monitoring.html"><i>📊</i> Surveillance en Direct</a>',
    "users.html": '<a href="./users.html"><i>👥</i> Gestion des Utilisateurs</a>',
    "login.html": '<a href="./login.html"><i>↩</i> Retour à la page login</a>',
    "logs.html": '<a href="./login.html"><i>❓</i> Logs</a>',
    "notifications.html": '<a href="./login.html"><i>❓</i> Notifications</a>'
};





//Récupération des pages
let AccessiblePages = sessionStorage.getItem("AccessiblePages");

//Si il n'y a pas de valeur
if(!AccessiblePages){
    document.addEventListener("TokenCheckFinished", (event) => {
        //Once done, we retrieve them again
        FecthAccessiblePages();
    })
}
//Si la valeur existe, on rempli le tableau
else{
    GetAccessiblePages();

    AddButtons(AccessiblePages);
}


//Récupération des pages auprès du servlet
function FecthAccessiblePages(){
    token = sessionStorage.getItem("token");

    //si le token est vide, le servlet ne fera rien donc on met nimp dedans
    if(!token){
        token = "placeholder"
    }
        
    //Récupération des pages
        fetch(`https://${window.ServerIP}:8443/SAE51/GetAccessiblePages`, {
            method: "POST",
            headers: { "Content-Type": "application/json; charset=UTF-8" },
            body: JSON.stringify({ token: token, Test: false })
        }).then(response => response.json())
        .then(FecthAccessiblePagesResult);
    }


    //Vérification de ce que renvoi le servlet
    function FecthAccessiblePagesResult(response){
        if(response.erreur === undefined){
            //Stockage de la réponse du serveur
            sessionStorage.setItem("AccessiblePages", JSON.stringify(response));

            GetAccessiblePages();

            AddButtons(AccessiblePages);
        }
        else{
            //On affiche l'erreur
            console.log("AddNavButtons.js => FecthAccessiblePagesResult() => Erreur : "+response.erreur);
        }
    }


    //Récuperation et convertion de la var "AccessiblePages" en JSON
    function GetAccessiblePages(){
        //retrieve data from session storage
        AccessiblePages = sessionStorage.getItem("AccessiblePages");

        //Parse the JSON
        AccessiblePages = JSON.parse(AccessiblePages);
    }


    //Ajout des bouttons
    function AddButtons(AccessiblePages){
        //Récupération des droits de l'utilisateur 
        login = sessionStorage.getItem("login");

        const userTableBody = document.getElementById("navBar");
        userTableBody.innerHTML = ""; // Vide le tableau avant d'ajouter les nouvelles données

        AccessiblePages.forEach(page => {
            if(window.currentPage === "help.html" && page.page === "help.html" && login === "Pas connecté"){
                //On ne met pas le bouton qui emmène l'utilisateur vers help.html s'il est déjà dessus
            }
            else{
                const row = document.createElement("li");
                row.innerHTML = `
                    ${ButtonDict[page.page]}
                `;
                userTableBody.appendChild(row);
            }
        });
    }

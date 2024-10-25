//Auteur : Maxime VALLET
//Version : 1.0


//Refresh la page quand on reviens en arrière sinon le script ne tourne pas
window.onunload = function(){reload()};

// Adresse IP des serveurs (vu que l'adresse du serveur change et que les deux serveurs on la mm adresse, on récupere celle entrée pour acceder au serv Nginx)
var currentUrl = window.location.href;
var url = new URL(currentUrl);
window.ServerIP = url.hostname;
window.currentPage = url.pathname.split('/').pop();
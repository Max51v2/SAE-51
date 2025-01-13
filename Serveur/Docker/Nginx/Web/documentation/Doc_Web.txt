Auteur : Maxime VALLET
Version : 1.1


Si vous souhaitez modifier l'organisation de ce dossier, merci de modifier les chemins d'accès ainsi que les liens dans : les pages, la configuration de Nginx et la BD  (selon les cas)
=> mieux vaut éviter afin de rien casser
==> il est préférable de maintenir l'organisation actuelle


Si vous créez une nouvelle page Web, merci d'ajouter votre page dans la table "web_pages_access" située dans la BD sae_51 en première partie du doc (Serveur/ConfigProjet/PostgreSQL.sql) 
=> Sinon TokenCheck va automatiquement vous renvoyer vers "login.html"


Un template qui contient les fonctionnalités de base est dispo ici : Web/template
=> Merci de bien mettre les pages que vous développez dans le dossier Web directement
==> Les liens vers les différentes ressources ne sont valides QUE lorsque la page est dans le dossier Web (aucune ne sera accessible si vous ouvrez la template là ou elle se situe)


!!! Développement Web !!!
Afin d'éviter de devoir lancer Start.sh à chaque modification, ouvrez la page HTML depuis le projet (double clique sur la page > afficher)
=> j'ai modifié les scripts URL.js et TokenCheck.js afin de permettre la modification en local avec ou sans serveur tomcat
==> Si vous développez sur la machine qui contient le serveur Tomcat (VM ou installation manuelle), tout devrait marcher sans soucis sinon vous n'aurez pas accès aux servlets

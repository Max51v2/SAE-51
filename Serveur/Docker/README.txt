#Auteur : Maxime VALLET
#Version : 1.5

# à faire : Monter le contenu de la BD PostgreSQL sur un volume partagé afin qu'il ne soit pas effacé à chaque fois que le conteneur est recréé
# !!! Le contenu de ce dossier a été ajouté après la date de remis du projet !!!

I) Installer Docker :
Debian : 
    Vous pouvez utiliser les scripts en dessous
        => les scripts suivants s'occupent d'installer les dépendances

Windows :
    1) Installer WSL
    2) Installer un dérivé de Debian (microsoft store)
    3) Lire les étapes pour Debian (point au dessus)


II) Commandes à exécuter
a) Clonage du répertoire sur le Bureau
sudo apt install git
git clone https://github.com/Max51v2/SAE-51.git /home/$USER/Bureau/SAE-51

b) Ouverture des ports du pare-feu
=> 8443(Tomcat) et 50000 (Apache)
sudo ufw allow 50000
sudo ufw allow 8443

c) Ajout de l'utilisateur au groupe docker
Taper la commande puis déconnectez/reconnectez-vous :
sudo usermod -aG docker $USER


III) Conteneurs
Créer les conteneurs Docker :
    !!! ATTENTION !!! => si vous mettez à jour le projet java il faut build le projet dans NetBeans (icône avec le marteau et le balai)
    Debian : 
        sudo /home/$USER/Bureau/SAE-51/Serveur/Docker/Bash_Scripts/DockerBuild.sh "$USER"

Lancement des conteneurs Docker :
    !!! ATTENTION !!! => si un port est déjà utilisé, le conteneur ne se lancera pas
    Debian :
        sudo /home/$USER/Bureau/SAE-51/Serveur/Docker/Bash_Scripts/DockerStart.sh "$USER"

Arrêt des conteneurs Docker :
    Debian :
        sudo /home/$USER/Bureau/SAE-51/Serveur/Docker/Bash_Scripts/DockerStop.sh "$USER"
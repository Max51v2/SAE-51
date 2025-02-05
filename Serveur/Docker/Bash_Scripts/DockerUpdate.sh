#!/bin/bash
#Auteur : Maxime VALLET
#Version 1.0

clear

#Vérification du packet manager de la distribution (APT)
if [ -f "/usr/bin/apt" ]
then
    #Rien
    :
else
    echo "Distribution incompatible (nécéssite APT)"

    #Arrêt du script
    exit 1
fi

#Arrêt et suppression des conteneurs
sudo /home/$1/Bureau/SAE-51/Serveur/Docker/Bash_Scripts/DockerStop.sh "$1"
    
echo "MAJ des images Docker :"
sudo docker pull ghcr.io/max51v2/tomcat_sae51:latest
echo
sudo docker pull ghcr.io/max51v2/psql_sae51:latest
echo
sudo docker pull ghcr.io/max51v2/nginx_sae51:latest

#Démarrage des conteneurs
sudo /home/$1/Bureau/SAE-51/Serveur/Docker/Bash_Scripts/DockerStart.sh "$1"
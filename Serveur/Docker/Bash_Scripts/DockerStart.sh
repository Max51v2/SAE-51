#!/bin/bash
#Merci d'aussi appliquer les modifs dans DockerDeploy.sh
#Auteur : Maxime VALLET
#Version 1.2


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

#Démarrage des conteneurs
echo "Démarrage des conteneurs :"
cd /home/$1/Bureau/SAE-51/Serveur/Docker
sudo -u $1 docker compose -f ./Dockercompose.yml up -d

echo

#Conteneurs
echo "Conteneurs existants :"
sleep 2
docker ps -a
echo
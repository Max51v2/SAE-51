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

#Arrêt des conteneurs
echo "Arrêt et suppression des conteneurs existants :"
docker stop tomcat_sae51
docker stop nginx_sae51
docker stop psql_sae51
docker rmi tomcat_sae51
docker rmi nginx_sae51
docker rmi psql_sae51

clear

#Conteneurs
echo "Conteneurs existants :"
docker ps -a
echo
#!/bin/bash
#Auteur : Maxime VALLET
#Version 1.0

clear

#Arrêt et suppression des conteneurs
sudo /home/$1/Bureau/SAE-51/Serveur/Docker/Bash_Scripts/DockerStop.sh "$1"

#Démarrage des conteneurs
cd /home/$1/Bureau/SAE-51/Serveur/Docker
sudo -u $1 docker compose -f ./Dockercompose.yml up -d

clear 

#Réseau docker
docker network create my_network
docker network connect my_network psql
docker network connect my_network tomcat
docker network connect my_network nginx

clear

#Conteneurs
echo "Conteneurs existants :"
sleep 2
docker ps -a
echo
#!/bin/bash
#Auteur : Maxime VALLET
#Version 1.0

clear

#Arrêt des conteneurs
echo "Arrêt et suppression des conteneurs existants :"
sudo docker stop $(docker ps -a -q)
sudo docker rm $(docker ps -a -q)

clear

#Conteneurs
echo "Conteneurs existants :"
docker ps -a
echo
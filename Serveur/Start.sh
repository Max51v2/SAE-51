#!/bin/bash
# Auteur original : Maxime VALLET (SAE 52)
# Modifications : Maxime VALLET
# Version : 0.8

#à modifier :
#Nginx
#NetBeans


clear
echo "Veuillez patienter"


#Récupération du status du daemon postgresql
PostgreSQL=`systemctl status postgresql | grep -o -E "Active: [A-Za-z]+" | sed 's/.*: //'`

#Demarrage de PostgreSQL si il est éteint
if [ "$PostgreSQL" = "inactive" ]
then
    #demarrage
    echo "demarrage de PostgreSQL"
    systemctl start postgresql

    echo
fi


#Copie des fichiers dans le répertoire de Nginx (NE PAS TOUCHER)
GitRep="/home/"$USER"/Bureau/SAE-51/Web/"
NginxRep="/var/www/sae-51"

#Vidage du Répertoire Nginx
sudo rm -rf $NginxRep"/"*

#Création répertoire Javadoc
#sudo mkdir -p /var/www/sae-51/Javadoc


#Copie des fichiers
sudo cp -r $GitRep* $NginxRep"/"


#Récupération du status du daemon Nginx
Nginx=`systemctl status nginx | grep -o -E "Active: [A-Za-z]+" | sed 's/.*: //'`

#Demarrage de Nginx si il est éteint
if [ "$Nginx" = "inactive" ]
then
    #demarrage
    echo "demarrage de Nginx"
    sudo systemctl start nginx

    echo
fi

#On recharge Nginx car le contenu du rep a changé
sudo systemctl reload nginx

clear

echo "Souhaitez-vous reconstruire la Base de Données ? [O/N]
*Cela va effacer le contenu de la BD"

echo

read  -n 1 -p "Option :" option

clear

#Reconstruction BD
if [ "$option" = "o" ] || [ "$option" = "O" ]
then
    #Connexion à la base
    psql -h localhost -U postgres -d template1 -c "DROP DATABASE sae_51;" -f "/home/"$USER"/Bureau/SAE-51/Serveur/Configuration/PostgreSQL_config.sql"
fi



#Affichage du status des opérations
clear
echo "Compte rendu exécution :"

#Récupération du status du daemon postgresql
PostgreSQL=`systemctl status postgresql | grep -o -E "Active: [A-Za-z]+" | sed 's/.*: //'`
echo "Status PostgreSQL : "$PostgreSQL

echo

#Récupération du status du daemon Nginx
Nginx=`systemctl status nginx | grep -o -E "Active: [A-Za-z]+" | sed 's/.*: //'`
echo "Status Nginx : "$Nginx

echo

#Statut DB
if [ "$option" = "o" ] || [ "$option" = "O" ]
then
    echo "Base de données SQL reconstruite"
    echo
fi


#echo "Javadoc disponible ici : https://[@IP VM]/Javadoc/index.html"

#echo
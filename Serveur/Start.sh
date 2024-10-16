#!/bin/bash
# Auteur original : Maxime VALLET (SAE 52)
# Modifications : Maxime VALLET
# Version : 0.1

#à modifier :
#Nginx
#NetBeans

#Récupperation de la version de Java
clear
cd /usr/java
Java_version=`ls | head -n 1`


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


#Copie des fichiers dans le répertoire d'Apache2 (NE PAS TOUCHER)
GitRep="/home/"$USER"/Bureau/SAE-51/Web/"
ApacheRep="/var/www/gmao"

#Vidage du Répertoire Apache
sudo rm -rf $ApacheRep"/"*

#Création répertoire Javadoc
sudo mkdir -p /var/www/gmao/Javadoc

#Copie des fichiers
sudo cp -r $GitRep* $ApacheRep"/"

# Fin NE PAS TOUCHER


#Récupération du status du daemon apache2
#apache2=`systemctl status apache2 | grep -o -E "Active: [A-Za-z]+" | sed 's/.*: //'`

#Demarrage de apache2 si il est éteint
#if [ "$apache2" = "inactive" ]
#then
    #demarrage
#    echo "demarrage de apache2"
#    sudo systemctl start apache2

#    echo
#fi

#On recharge apache2 car le contenu du rep a changé
#sudo systemctl daemon-reload


#Section reconstruction BD
#Recupération option utilisateur
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
    psql -h localhost -U postgres -d template1 -c "DROP DATABASE sae_51;" -f "/home/"$USER"/Bureau/SAE-51/Serveur/PostgreSQL_config.sql"
fi


#Affichage du status des opérations
clear
echo "Compte rendu exécution :"

#Récupération du status du daemon postgresql
PostgreSQL=`systemctl status postgresql | grep -o -E "Active: [A-Za-z]+" | sed 's/.*: //'`
echo "Status PostgreSQL : "$PostgreSQL

echo

#Récupération du status du daemon apache2
apache2=`systemctl status apache2 | grep -o -E "Active: [A-Za-z]+" | sed 's/.*: //'`
echo "Status apache2 : "$apache2

echo

#Récupération du status du daemon tomcat
#tomcat=`systemctl status tomcat | grep -o -E "Active: [A-Za-z]+" | sed 's/.*: //'`
#echo "Status tomcat : "$tomcat

#echo

#Statut DB
if [ "$option" = "o" ] || [ "$option" = "O" ]
then
    echo "Base de données SQL reconstruite"
    echo
fi


#echo "Javadoc disponible ici : https://[@IP VM]/Javadoc/index.html"

#echo
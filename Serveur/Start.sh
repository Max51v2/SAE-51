#!/bin/bash
# Auteur original : Maxime VALLET (SAE 52)
# Modifications : Maxime VALLET
#    => Remplacement Apache par Nginx
#    => Dossier lib qui contient les librairies (afin d'avoir un chemin commun dans ou en dehors de la VM)
# Version : 1.0



#Récupperation de la version de Java (lancement NetBEANS)
sudo clear
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
    sudo systemctl start postgresql

    echo
fi


#Répertoires
GitRep="/home/"$USER"/Bureau/SAE-51/Web/"
NginxRep="/var/www/sae-51/"
LibRep="/home/"$USER"/Bureau/SAE-51/NetBEANS/lib/"
ConfFile="/home/"$USER"/Bureau/SAE-51/Serveur/Configuration/sae_51.conf"

#Vidage du Répertoire Nginx
sudo rm -rf $NginxRep*

#Création répertoire Javadoc
sudo mkdir -p /var/www/sae-51/Javadoc
sudo mkdir -p /var/www/sae-51/JavadocClient

#Création répertoire Netbeans (librairies)
sudo mkdir -p /Netbeans
sudo mkdir -p /Netbeans/conf


#Copie des fichiers
sudo cp -r $GitRep* $NginxRep
sudo cp -r "/home/"$USER"/Bureau/SAE-51/NetBEANS/SAE51/dist/javadoc/"* $NginxRep"/Javadoc"
sudo cp -n $LibRep* /Netbeans
sudo cp -r $ConfFile /Netbeans/conf



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


#Demande de lancement NetBEANS s'il n'est pas en train de tourner
ProcNetBEANS=`ps -ef | grep -v grep | grep -o -E "sudo netbeans --jdkhome /usr/java/"$Java_version | head -n 1`

#Démarrage NetBEANS
if [ "$ProcNetBEANS" = "" ]
then
    #Recupération option utilisateur
    clear
    echo "Souhaitez-vous lancer NetBeans ? [O/N]"

    echo

    read  -n 1 -p "Option :" optionNetbeans

    #Lancement NetBEANS
    if [ "$optionNetbeans" = "o" ] || [ "$optionNetbeans" = "O" ]
    then
        clear

        #Lancement de NetBEANS dans un nouvel onglet
        gnome-terminal --tab -- /bin/sh -c 'echo "!!! Ne pas fermer cette fenêtre !!!"; echo; sudo netbeans --jdkhome /usr/java/'$Java_version

        sleep 1
    fi

    echo
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


#Affichage status NetBEANS
ProcNetBEANS=`ps -ef | grep -v grep | grep -o -E "sudo netbeans --jdkhome /usr/java/openjdk-22.0.2" | head -n 1`
if [ "$ProcNetBEANS" = "" ]
then
    echo "Status NetBEANS : inactive"
else
    echo "Status NetBEANS : active"
fi

echo

echo "Javadoc disponible ici : https://[@IP VM]/Javadoc/index.html"

echo
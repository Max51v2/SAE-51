#!/bin/bash
# Auteur original : Maxime VALLET (SAE 52)
# Version : 1.5
# Modifications : Maxime VALLET
#    => Remplacement Apache par Nginx
#    => Dossier lib qui contient les librairies (afin d'avoir un chemin commun dans ou en dehors de la VM)
#    => Dossier documentation
#    => Vérification de la bonne execution du script SQL
#    => Vérification de l'entrée utilisateur (redemande l'entrée tant qu'elle n'est pas valide)



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
ConfFile="/home/"$USER"/Bureau/SAE-51/Serveur/ConfigProjet/sae_51.conf"
projectRep="/home/"$USER"/Bureau/SAE-51/"
docRep=$NginxRep"documentation/"

#Vidage du Répertoire Nginx
sudo rm -rf $NginxRep*

#Création répertoire Javadoc
sudo mkdir -p /var/www/sae-51/Javadoc

#Création répertoire documentation
sudo mkdir -p /var/www/sae-51/documentation

#Création répertoire Netbeans (librairies)
sudo mkdir -p /Netbeans
sudo mkdir -p /Netbeans/conf


#Copie des fichiers
sudo cp -r $GitRep* $NginxRep
sudo cp -r "/home/"$USER"/Bureau/SAE-51/NetBEANS/SAE51/dist/javadoc/"* $NginxRep"/Javadoc"
sudo cp -n $LibRep* /Netbeans
sudo cp -r $ConfFile /Netbeans/conf
sudo cp -r $projectRep"Serveur/README_Java.txt" $docRep"Doc_Serveur.txt"
sudo cp -r $projectRep"Serveur/README_VM.txt" $docRep"Doc_VM.txt"
sudo cp -r $projectRep"Client/README.txt" $docRep"Doc_Client.txt"
sudo cp -r $projectRep"Web/_README.txt" $docRep"Doc_Web.txt"
sudo cp -r $projectRep"README.md" $docRep"Doc_Projet.txt"



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


option="undetermined"
c=0

#Tant que l'on a pas une option valide, on redemande à l'utilisateur d'en saisir une
while [ "$option" != "o" ] && [ "$option" != "O" ] && [ "$option" != "n" ] && [ "$option" != "N" ]
do
    clear

    if [ "$c" -ge 1 ]
    then
        echo "Mauvaise saisie \"$option\", merci de resaisir votre option"

        echo
    fi

    echo "Souhaitez-vous reconstruire la Base de Données ? [O/N]"
    echo "Cela va effacer le contenu de la BD"

    echo

    sleep 1

    #Lecture de l'entrée utilisateur
    read  -n 1 -p "Option :" option

    c=$(($c+1)) 
done

clear

#Reconstruction BD
if [ "$option" = "o" ] || [ "$option" = "O" ]
then
    #Connexion à la base
    psql -h localhost -U postgres -d template1 -c "DROP DATABASE sae_51;" -f "/home/"$USER"/Bureau/SAE-51/Serveur/ConfigProjet/PostgreSQL_config.sql" > /tmp/trace_Start_sh.txt
fi


#Demande de lancement NetBEANS s'il n'est pas en train de tourner
ProcNetBEANS=`ps -ef | grep -v grep | grep -o -E "sudo netbeans --jdkhome /usr/java/"$Java_version | head -n 1`

#Démarrage NetBEANS
if [ "$ProcNetBEANS" = "" ]
then
    #Recupération option utilisateur
    clear

    optionNetbeans="undetermined"
    c=0

    #Tant que l'on a pas une option valide, on redemande à l'utilisateur d'en saisir une
    while [ "$optionNetbeans" != "o" ] && [ "$optionNetbeans" != "O" ] && [ "$optionNetbeans" != "n" ] && [ "$optionNetbeans" != "N" ]
    do
        clear

        if [ "$c" -ge 1 ]
        then
            echo "Mauvaise saisie \"$optionNetbeans\", merci de resaisir votre option"

            echo
        fi

        echo "Souhaitez-vous lancer NetBeans ? [O/N]"

        echo

        sleep 1

        #Lecture de l'entrée utilisateur
        read  -n 1 -p "Option :" optionNetbeans

        c=$(($c+1)) 
    done

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
    if grep -q "#####Fait#####" /tmp/trace_Start_sh.txt
    then
        echo "Base de données SQL reconstruite"
    else
        echo "Erreur lors de la reconstruction de la Base de données"
    fi

    rm /tmp/trace_Start_sh.txt

    echo
fi


#Affichage status NetBEANS
ProcNetBEANS=`ps -ef | grep -v grep | grep -o -E "sudo netbeans --jdkhome /usr/java/"$Java_version | head -n 1`
if [ "$ProcNetBEANS" = "" ]
then
    echo "Status NetBEANS : inactive"
else
    echo "Status NetBEANS : active"
fi

echo

echo "Javadoc disponible ici : https://[@IP VM]/Javadoc/index.html"

echo
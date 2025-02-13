#!/bin/bash
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

#Arrêt et suppression des conteneurs
/home/$1/Bureau/SAE-51/Serveur/Docker/Bash_Scripts/DockerStop.sh "$1"

#SSL
mkdir /certs
cd /certs
if [ -e /certs/SAE51.crt ]; then
    echo "SSL ok"
else
    openssl req -x509 -nodes -days 10000 -newkey rsa:4096 -keyout /certs/SAE51.key -out /certs/SAE51.crt -subj "/C=FR/ST=Meurthe-et-moselle/L=Nancy/O=VIM/OU=Meurthe-et-moselle/CN=www.tkt.fr/emailAddress=max.vallet@outlook.fr"
fi
if [ -e /certs/SAE51.p12 ]; then
    echo "keystore ok"
else
    openssl pkcs12 -export -in SAE51.crt -inkey SAE51.key -out SAE51.p12 -name tomcat -passout pass:leffe
fi
chmod 755 /certs/*


#nginx
clear
echo "Nginx"

DockerFilePath="/home/$1/Bureau/SAE-51/Serveur/Docker/Nginx"
cd $DockerFilePath

GitRep="/home/"$1"/Bureau/SAE-51/Web/"
NginxRep="/var/www/sae-51/"
projectRep="/home/"$1"/Bureau/SAE-51/"
docRep=$DockerFilePath"/Web/documentation/" 

mkdir -p $DockerFilePath/Web
mkdir -p $DockerFilePath/Web/documentation
mkdir -p $DockerFilePath/Web/Javadoc

cp -r $GitRep* $DockerFilePath/Web
cp -r "/home/"$1"/Bureau/SAE-51/NetBEANS/SAE51/dist/javadoc/"* $DockerFilePath"/Web/Javadoc/"
cp -r $projectRep"Serveur/README_Java.txt" $docRep"Doc_Serveur.txt"
cp -r $projectRep"Serveur/README_VM.txt" $docRep"Doc_VM.txt"
cp -r $projectRep"app/README.txt" $docRep"Doc_Client.txt"
cp -r $projectRep"Web/_README.txt" $docRep"Doc_Web.txt"
cp -r $projectRep"README.md" $docRep"Doc_Projet.txt"
cp /home/$1/Bureau/SAE-51/Serveur/Docker/Config/Nginx.txt $DockerFilePath/Nginx.txt
cp /certs/SAE51.crt $DockerFilePath/SAE51.crt
cp /certs/SAE51.key $DockerFilePath/SAE51.key

docker build -t nginx_sae51 /home/$1/Bureau/SAE-51/Serveur/Docker/Nginx

rm $DockerFilePath/Nginx.txt
rm $DockerFilePath/SAE51.crt
rm $DockerFilePath/SAE51.key


#PostgreSQL
clear
echo "PostgreSQL"

DockerFilePath="/home/$1/Bureau/SAE-51/Serveur/Docker/PostgreSQL"
cd $DockerFilePath

cp /home/$1/Bureau/SAE-51/Serveur/Docker/Config/postgresql.conf $DockerFilePath/postgresql.conf
cp /home/$1/Bureau/SAE-51/Serveur/Docker/Config/pg_hba.conf $DockerFilePath/pg_hba.conf
cp /home/$1/Bureau/SAE-51/Serveur/Docker/Config/StartPSQL.sh $DockerFilePath/StartPSQL.sh
cp /home/$1/Bureau/SAE-51/Serveur/ConfigProjet/PostgreSQL_config.sql $DockerFilePath/PostgreSQL_config.sql

docker build -t psql_sae51 /home/$1/Bureau/SAE-51/Serveur/Docker/PostgreSQL

rm $DockerFilePath/pg_hba.conf
rm $DockerFilePath/postgresql.conf
rm $DockerFilePath/PostgreSQL_config.sql
rm $DockerFilePath/StartPSQL.sh


#Tomcat
clear
echo "Tomcat"

DockerFilePath="/home/$1/Bureau/SAE-51/Serveur/Docker/Tomcat"
cd $DockerFilePath

cp /certs/SAE51.crt $DockerFilePath/SAE51.crt
cp /certs/SAE51.key $DockerFilePath/SAE51.key
cp /certs/SAE51.p12 $DockerFilePath/SAE51.p12
cp /home/$1/Bureau/SAE-51/Serveur/Docker/Config/Tomcat.sh $DockerFilePath/Tomcat.sh
cp /home/$1/Bureau/SAE-51/Serveur/Docker/Config/StartTomcat.sh $DockerFilePath/StartTomcat.sh
cp /home/$1/Bureau/SAE-51/Serveur/Docker/Config/Tomcat.xml $DockerFilePath/Tomcat.xml
cp /home/$1/Bureau/SAE-51/Serveur/Docker/Config/tomcat-users.xml $DockerFilePath/tomcat-users.xml
cp /home/$1/Bureau/SAE-51/NetBEANS/SAE51/dist/SAE51.war $DockerFilePath/SAE51.war
cp /home/$1/Bureau/SAE-51/Serveur/ConfigProjet/sae_51.conf $DockerFilePath/sae_51.conf

docker build -t tomcat_sae51 /home/$1/Bureau/SAE-51/Serveur/Docker/Tomcat

rm $DockerFilePath/SAE51.crt
rm $DockerFilePath/SAE51.key
rm $DockerFilePath/SAE51.p12
rm $DockerFilePath/Tomcat.sh
rm $DockerFilePath/StartTomcat.sh
rm $DockerFilePath/Tomcat.xml
rm $DockerFilePath/tomcat-users.xml
rm $DockerFilePath/SAE51.war
rm $DockerFilePath/sae_51.conf

#!/bin/bash
#Auteur : Maxime VALLET
#Version 1.0


#Déploiement du .war sur le serveur Tomcat
/opt/tomcat/bin/catalina.sh run &
curl -u admin:leffe -T /conf/SAE51.war "http://localhost:8080/manager/text/deploy?path=/SAE51&update=true"

#Boucle infinie pour garder le conteneur actif
while true
do
sleep 60
done

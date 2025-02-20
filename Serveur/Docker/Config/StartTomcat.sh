#!/bin/bash
#Auteur : Maxime VALLET
#Version 2.0


#On attend que le serveur psql soit démarré
while true
do
  #Tentative de connexion au serveur PostgreSQL
  if pg_isready -h "localhost" -p "5432" > /dev/null 2>&1
  then
    #Si serveur actif, on arrête le while
    break
  fi

  #Attend avant la prochaine tentative
  echo "Waiting for PostgreSQL server"
  sleep 1
done

echo "PostgreSQL server ON"

#Démarrage du serveur tomcat en arrière plan
/opt/tomcat/bin/catalina.sh run &

#On attend que le serveur tomcat soit démarré
while true
do
  #Tentative de connexion au serveur PostgreSQL
  if curl -s http://localhost:8080 | grep -q "Apache Tomcat"
  then
    #Si serveur actif, on arrête le while
    break
  fi

  #Attend avant la prochaine tentative
  echo "Waiting for Tomcat server"
  sleep 1
done

echo "Tomcat server ON"

echo "Deploying project"

#Déploiement du .war sur le serveur Tomcat 
curl -u admin:leffe -T /conf/SAE51.war "http://localhost:8080/manager/text/deploy?path=/SAE51&update=true"

#Boucle infinie pour garder le conteneur actif
while true
do
sleep 60
done

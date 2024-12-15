#OS : Debian 12 (dérivés normalement fonctionnels)
#NE PAS METTRE DEBIAN / J'ai perdu 3H!!! à cause de ça
FROM ubuntu:latest

#Créé et maintenu par Maxime VALLET
LABEL maintainer="max.vallet@outlook.fr"

#MAJ OS, installation wget et curl
RUN apt update && apt upgrade && apt install -y wget curl libapr1 libapr1-dev libssl-dev libtcnative-1 && apt-get clean

#Changement du fuseau horaire
RUN rm -f /etc/localtime && \
    ln -s /usr/share/zoneinfo/Europe/Paris /etc/localtime

#Copie des clés SSL
COPY SAE51.key /certs/SAE51.key
COPY SAE51.crt /certs/SAE51.crt
COPY SAE51.p12 /certs/SAE51.p12

#Copie du script de configuration de JDK et Tomcat et données de conf
COPY Tomcat.sh /conf/Tomcat.sh
COPY StartTomcat.sh /conf/StartTomcat.sh
COPY Tomcat.xml /conf/Tomcat.xml
COPY tomcat-users.xml /conf/tomcat-users.xml
COPY SAE51.war /conf/SAE51.war
COPY sae_51.conf /Netbeans/conf/sae_51.conf

#VAR env
ENV JAVA_HOME=/usr/lib/jvm/openjdk
ENV CATALINA_HOME=/opt/tomcat
ENV LD_LIBRARY_PATH=/usr/lib/x86_64-linux-gnu:$LD_LIBRARY_PATH

#Execution du script de config
RUN chmod 777 /conf/Tomcat.sh && \
    /conf/Tomcat.sh && \
    chmod 777 /conf/StartTomcat.sh

#Ajout des répertoire de tomcat
RUN chmod 777 /opt/tomcat && \
    chmod 777 /conf && \
    chmod 777 /certs && \
    chmod 777 /Netbeans

#Ports
EXPOSE 8443
EXPOSE 8080

USER tomcat

#Commande par défaut
CMD ["/conf/StartTomcat.sh"]
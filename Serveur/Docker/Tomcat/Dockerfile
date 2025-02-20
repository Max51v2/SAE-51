#OS
FROM ubuntu:22.04

#Créé et maintenu par Maxime VALLET
LABEL maintainer="max.vallet@outlook.fr"
LABEL org.opencontainers.image.source=https://ghcr.io/max51v2/tomcat_sae51

#Installation des dépendances
RUN apt-get update &&\
    apt-get install -y wget curl libapr1 libapr1-dev libssl-dev libtcnative-1 postgresql-client postgresql-client-common && \
    apt-get clean && \
#Changement du fuseau horaire
    rm -f /etc/localtime && \
    ln -s /usr/share/zoneinfo/Europe/Paris /etc/localtime

#Copie des clés SSL
COPY SAE51.key SAE51.crt SAE51.p12 /certs/

#Copie du script de configuration de JDK et Tomcat et données de conf
COPY Tomcat.sh StartTomcat.sh Tomcat.xml tomcat-users.xml SAE51.war sae_51.conf /conf/

#VAR env
ENV JAVA_HOME=/usr/lib/jvm/openjdk
ENV CATALINA_HOME=/opt/tomcat

#Modification des droits d'accès
RUN chmod 755 /conf/Tomcat.sh && \
    chmod 755 /conf/StartTomcat.sh && \
#Installation de Tomcat
    /conf/Tomcat.sh && \
#Modification des droits d'accès
    chown -R tomcat:tomcat /opt/tomcat && \
    chown -R tomcat:tomcat /conf && \
    chown -R tomcat:tomcat /certs && \
    chown -R tomcat:tomcat /Netbeans && \
#Nettoyage des daemons
    apt-get remove -y wget && \
    apt-get clean && \
    apt-get autoremove -y

#Ports
EXPOSE 8443
EXPOSE 12345

USER tomcat

#Commande par défaut
CMD ["/conf/StartTomcat.sh"]
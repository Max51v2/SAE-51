#OS
FROM ubuntu:22.04

#Créé et maintenu par Maxime VALLET
LABEL maintainer="max.vallet@outlook.fr"

#Installation des dépendances
RUN apt-get update && \
    apt-get install -y nginx && \
#Changement du fuseau horaire
    rm -f /etc/localtime && \
    ln -s /usr/share/zoneinfo/Europe/Paris /etc/localtime

#Copie des pages Web
COPY Web/ /var/www/sae-51
COPY Nginx.txt /conf/Nginx.txt
COPY SAE51.key SAE51.crt /certs/

#Créeation du rep contenant les pages web et retrait du site par défaut
RUN chmod -R 755 /var/www/sae-51 && \
    chmod -R 755 /var/www/sae-51/* && \
    cp /conf/Nginx.txt /etc/nginx/sites-available/sae-51 && \
    rm /etc/nginx/sites-available/default && \
    ln -s /etc/nginx/sites-available/sae-51 /etc/nginx/sites-enabled/ && \
    rm /etc/nginx/sites-enabled/default && \
    #Nettoyage des daemons
    apt-get clean && \
    apt-get autoremove

#Ports
EXPOSE 443

#Commande par défaut
CMD ["nginx", "-g", "daemon off;"]
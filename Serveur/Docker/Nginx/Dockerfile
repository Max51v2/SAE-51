#OS : Debian 12
FROM debian:12

#Créé et maintenu par Maxime VALLET
LABEL maintainer="max.vallet@outlook.fr"

#Changement du fuseau horaire
RUN rm -f /etc/localtime && \
    ln -s /usr/share/zoneinfo/Europe/Paris /etc/localtime

#MAJ OS et installation de Nginx
RUN apt update && apt upgrade && apt install -y nginx && apt-get clean

#Installation d'Apache
RUN apt install -y apache2

#Copie des pages Web
COPY Web/ /var/www/sae-51
COPY Nginx.txt /conf/Nginx.txt
COPY SAE51.key /certs/SAE51.key
COPY SAE51.crt /certs/SAE51.crt

RUN chmod -R 755 /var/www/sae-51 && \
    chmod -R 755 /var/www/sae-51/* && \
    cp /conf/Nginx.txt /etc/nginx/sites-available/sae-51 && \
    rm /etc/nginx/sites-available/default && \
    ln -s /etc/nginx/sites-available/sae-51 /etc/nginx/sites-enabled/ && \
    rm /etc/nginx/sites-enabled/default

#Ports
EXPOSE 443

#Commande par défaut
CMD ["nginx", "-g", "daemon off;"]
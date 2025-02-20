#!/bin/bash
#Auteur : Maxime VALLET
#Version 1.0 

cd /conf

if test -f "/conf/restart"; then
    #C'est le seul moyen qui fonctionne pour que psql écoute sur toutes les adresses :/
    #Il y'a 2 postgresql.conf : le premier est correct mais "listen_addresses" n'est pas pris en compte
    #Le deuxième donne des erreurs quand il est modifié (il doit prendre ce param ici mais le reste au 1er)
    postgres -c listen_addresses='localhost'
else
    # Copie des fichiers de config (pour une raison obscure ils sont reset après le build de l'image)
    cp pg_hba.conf /var/lib/postgresql/data/pg_hba.conf
    cp postgresql.conf /etc/postgresql/15/main/postgresql.conf

    #Redémarrage de psql => conteneur entier
    touch /conf/restart
    pg_ctl restart
fi
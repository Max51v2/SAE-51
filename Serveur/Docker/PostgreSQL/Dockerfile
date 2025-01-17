#OS
FROM ubuntu:22.04

#Créé et maintenu par Maxime VALLET
LABEL maintainer="max.vallet@outlook.fr"

#Variables d'environnement
ENV PG_MAJOR=15
ENV PGDATA=/var/lib/postgresql/data
ENV PATH=$PATH:/usr/lib/postgresql/$PG_MAJOR/bin
ARG DEBIAN_FRONTEND=noninteractive

#Installation des dépendances
RUN apt-get update && \
    apt-get install -y gnupg gnupg1 gnupg2 lsb-release && \
    apt-get install -y wget ca-certificates && \
    wget --quiet -O - https://www.postgresql.org/media/keys/ACCC4CF8.asc | apt-key add - && \
    sh -c 'echo "deb https://apt.postgresql.org/pub/repos/apt $(lsb_release -cs)-pgdg main" > /etc/apt/sources.list.d/pgdg.list' && \
    apt-get update && \
    apt-get install -y postgresql-$PG_MAJOR postgresql-common gosu && \
#Changement du fuseau horaire
    rm -f /etc/localtime && \
    ln -s /usr/share/zoneinfo/Europe/Paris /etc/localtime

#Copie des fichiers de configuration
COPY postgresql.conf /etc/postgresql/$PG_MAJOR/main/postgresql.conf
COPY postgresql.conf pg_hba.conf /var/lib/postgresql/data/
COPY postgresql.conf pg_hba.conf PostgreSQL_config.sql StartPSQL.sh /conf/

#Supprimer le répertoire de données s'il existe déjà
RUN rm -rf /var/lib/postgresql/data

#Création du répertoire de données et définition des droits d'accès
RUN mkdir -p /var/lib/postgresql/data && \
    chown -R postgres:postgres /var/lib/postgresql && \
    chown -R postgres:postgres /etc/postgresql/$PG_MAJOR && \
    chown -R postgres:postgres /usr/local/bin && \
    chown -R postgres:postgres /conf && \
    chown -R postgres:postgres /var/lib/postgresql/data && \
    chmod 777 /conf/StartPSQL.sh && \
#Initialisation la base de données
    su - postgres -c "/usr/lib/postgresql/$PG_MAJOR/bin/initdb -D $PGDATA" && \
#Démarrer PostgreSQL temporairement et exécuter les commandes SQL
    gosu postgres /usr/lib/postgresql/$PG_MAJOR/bin/pg_ctl start -D $PGDATA && \
    psql -U postgres -d template1 -c "ALTER USER postgres WITH ENCRYPTED PASSWORD 'leffe';" && \
    PGPASSWORD='leffe' psql -U postgres -d template1 -c "DROP DATABASE sae_51;" -f "/conf/PostgreSQL_config.sql" && \
    gosu postgres /usr/lib/postgresql/$PG_MAJOR/bin/pg_ctl stop -D $PGDATA && \
#Nettoyage des daemons
    apt-get remove -y gosu gnupg gnupg1 gnupg2 lsb-release wget ca-certificates && \
    apt-get clean && \
    apt-get autoremove -y

#Exposer le port PostgreSQL
EXPOSE 5432

#Commande par défaut
USER postgres
CMD ["/conf/StartPSQL.sh"]

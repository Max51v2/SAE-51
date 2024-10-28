Auteur original : Maxime VALLET (SAE 52)
Modifications : Maxime VALLET
    => Remplacement Ubuntu par Debian
    => Installation des programmes sans snapd
    => Remplacement Apache par Nginx
Version : 1.2



+--------------------------VM-----------------------------
|
|    +-------------------------Général-------------------------  
|    |
|    |   Lien VM : https://drive.google.com/drive/folders/1Q-eVKhEwMFV0cG51km6JPJSZJaPCn71p?usp=sharing
|    |   login : sae-51
|    |   MDP : leffe
|    |
|    |   Une fois connecté sur la VM, merci de suivre le README sur le Burea
|    |
|    |   *Démarrer les daemons + actualiser BD + Web
|    |   /home/$USER/Bureau/SAE-51/Serveur/Start.sh
|    |
|    |   *Le copier-coller est supporté entre la VM et l'hôte et vise-versa
|    |
|    +---------------------------------------------------------
|
|    +-------------------------VSCode--------------------------
|    |
|    |   *Modifier le nom et l'@ mail
|    |   git config --global user.name "[Prenom Nom]"
|    |   git config --global user.email "[@ Mail]"
|    |
|    +---------------------------------------------------------
|
|    +------------------------NetBEANS------------------------- 
|    |
|    |   *NetBEANS est lancé par Start.sh sur demande
|    |
|    |   *Commande
|    |   => sudo netbeans --jdkhome /usr/java/[version JDK]
|    |
|    |   *MDP projet : "leffe"
|    |
|    |   *Interface administration (accessible depuis localhost uniquement)
|    |   login : "admin"
|    |   MDP : "leffe"
|    |
|    |   *Importer des librairies au projet (marche uniquement pour un projet Web Ant et pas le projet du client)
|    |   => aller sur "https://mvnrepository.com/" et chercher la librairie => version dans le projet => files => view all => dl la jar => la mettre dans /NetBEANS/lib
|    |   ==> File > projet properties > libraries > add library
|    |
|    +---------------------------------------------------------
|
|    +-----------------------PostgreSQL-----------------------  
|    |
|    |   *Connexion à la BD
|    |   sudo -u postgres psql sae_51
|    |
|    |   *Aide commandes 
|    |   \?
|    |
|    |   *Tutos JDBC
|    |   https://www.javaguides.net/p/jdbc-tutorial.html
|    |
|    |   *Script reconstruction BD
|    |   ./Start.sh a une option pour reconstruire la base à partir du script "PostgreSQL_config.sql"
|    |   => !!! toute modification de la BD doit se faire dans ce script sql (il faut refaire tourner Start.sh) !!!
|    |
|    +---------------------------------------------------------
|
|    +-------------------------Tomcat-------------------------- 
|    |
|    |   Tomcat se lance lorsqu'on lance le projet dans NetBEANS (ne JAMAIS le lancer manuellement sinon le serveur Tomcat ne fonctonnera PAS)
|    |
|    |   *Interface administration (accessible depuis localhost uniquement)
|    |   login : "admin"
|    |   MDP : "leffe"
|    |
|    +---------------------------------------------------------
|
|    +-------------------CONCLUSION A LIRE--------------------- 
|    |
|    |   Pour lancer les daemons, actualiser les fichiers Web, reconstruire la DB et démarrer NetBeans lancez Start.sh (cf. section VM > Général)
|    |   => les identifiants et MDP pour NetBEANS sont dispo dans VM > NetNEANS
|    |
|    |   Se connecter à GitHub dans VSCode :
|    |   Cliquer sur l'onglet "Compte" (en bas à gauche) et sélectionner l'option pour se connecter à Github
|    |
|    |   Cloner un répertoire Github sur le BUREAU (obligatoire avant de commencer) :
|    |   Cliquer sur l'onglet "Explorer" (pages), cliquer sur "Clone repository" > "Clone from Github" > "Max51v2/SAE-51" > Bureau NetBEANS
|    |
|    |   Remplacer le répertoire Github local par celui en ligne (si tu veux reset les modifs du projet)
|    |   => icon source control (branche à gauche) > survoler menu déroulant "Source control graph" > cliquer sur l'icon pull
|    |
|    |   Pour sauvegarder le projet > VSCode
|    |   => icon source control (branche à gauche) > survoler menu déroulant "Changes" > cliquer sur le + pour ajouter tous les fichiers (tt dans être dans "staged changes")
|    |   => menu détaillé bouton commit > commit and push > Ajouter un commentaire (non commenté) > valider (en haut à droite)
|    |
|    |   *Certificat de l'authorité de certification
|    |   => *Même après ajout, le navigateur affiche toujours que la connexion n'est pas sécurisé car le certificat est auto-signé
|    |
|    |   Mis à part la partie Web (gérée par Start.sh), tous les autres fichiers sont placés correctement
|    |   => Il n'a pas besoin de toucher au contenu du répertoire Github local et tout est sauvegardé en faisant un "commit and push"
|    |   => Web et Serveur => VSCode | Servlets et Client => NetBEANS
|    |   => Il n'y a besoin du terminal que pour lancer Start.sh
|    |
|    |   Adresses serveurs (@IP VM peut être remplacé par "localhost" si connexion sur le navigateur de la VM) :
|    |   => Nginx : https://[@IP VM]/[NomPage]
|    |   => Tomcat (administration) : http://[@IP VM]:8443
|    |   => Tomcat (servlets) : https://[@IP VM]:8443/SAE51/[NomServlet]    (IMPORTANT : pour accès servlet > voir exemple login.html)
|    |   => Javadoc : https://[@IP VM]/Javadoc/index.html
|    |   ==> déjà enregistré dans les marques page sur la VM
|    |
|    |   *Cartes réseau :
|    |   => il y'a deux cartes réseaux : une en mode bridge et une en mode NAT
|    |   => dans le cas ou la première fonctionne (enp0s3), les serveurs sont accessibles à partir de l'IP de l'OS hôte (donc accessible au réseau local)
|    |   => dans le cas ou la deuxième est la seule qui fonctionne (enp0s8), les serveurs sont accessibles à partir de l'IP de la carte virtuelle VirtualBox (donc accessible à l'OS hôte uniquement)
|    |   => aucune modif requise/ raison : impossible d'utiliser le mode bridge sur eduroam
|    | 
|    +---------------------------------------------------------
|
+---------------------------------------------------------




+--------Procédure d'installation Debian 12 (sauf VM)--------
|
|    +-------------------------Debian------------------------
|    |
|    |   *Guest additions
|    |   => sudo apt install make gcc dkms linux-source linux-headers-$(uname -r)
|    |   => monter l'iso Virtualbox (vbox > périphériques > iso guest additions)
|    |   => cd /media/cdrom0
|    |   => sudo sh VBoxLinuxAdditions.run
|    |   => *redémarrer
|    |
|    |   *ufw
|    |   => sudo apt install ufw
|    |   => sudo systemctl enable ufw
|    |   => sudo ufw enable
|    |
|    +--------------------------------------------------------
|
|    +-------------------------Général------------------------
|    |
|    |   sudo apt update
|    |   sudo apt upgrade
|    |   sudo apt-get install git
|    |
|    |   *Installer les Guest additions
|    |
|    |   *Script de demarrage des daemons
|    |   chmod u+x /home/$USER/Bureau/SAE-51/Serveur/Start.sh
|    |   
|    |   *Demarrage deamons (une fois installation terminée): voir section VM > Général
|    |
|    |   !!! Merci de vérifier que les liens de téléchargement des paquets .deb sont toujours d'actualité !!!
|    |
|    +--------------------------------------------------------
|
|    +--------------------------JDK----------------------------  
|    |   
|    |   sudo mkdir /usr/java
|    |   cd /usr/java
|    |   sudo mkdir ./openjdk-22.0.2
|    |
|    |   sudo wget -c https://download.java.net/java/GA/jdk22.0.2/c9ecb94cd31b495da20a27d4581645e8/9/GPL/openjdk-22.0.2_linux-x64_bin.tar.gz
|    |   sudo tar xzvf ./openjdk-22.0.2_linux-x64_bin.tar.gz -C ./openjdk-22.0.2 --strip-components=1
|    |   sudo rm openjdk-22.0.2_linux-x64_bin.tar.gz
|    |
|    |   sudo nano /etc/profile
|    |   => JAVA_HOME=/usr/java/[version JDK]
|    |   => PATH=$PATH:$HOME/bin:$JAVA_HOME/bin
|    |   => export JAVA_HOME
|    |   => export JRE_HOME
|    |   => export PATH
|    |
|    +---------------------------------------------------------
|
|    +-------------------------VSCode--------------------------  
|    |   
|    |   cd ~/Téléchargements
|    |   sudo wget -c https://go.microsoft.com/fwlink/?LinkID=760868 -O vscode.deb
|    |
|    |   sudo apt install ./vscode.deb
|    |   sudo rm ./vscode.deb 
|    |
|    +---------------------------------------------------------
|
|    +-----------------------PostgreSQL------------------------  
|    |
|    |   !!! En cas d'utilisation en dehors du cadre de ce projet, remplacez les MDP !!!
|    |
|    |   Installation (ubuntu):
|    |   sudo apt install postgresql
|    |
|    |   cd /etc/postgresql/[Version PostgreSQL]/main/
|    |   sudo nano postgresql.conf
|    |
|    |   *remplacer " #listen_addresses = 'localhost' " par " listen_addresses = 'localhost' "
|    |   *sauvegarder et fermer le fichier
|    |
|    |   sudo -u postgres psql template1
|    |
|    |   ALTER USER postgres with encrypted password 'leffe';
|    |   
|    |   \q
|    |
|    |   sudo nano /etc/postgresql/[Version PostgreSQL]/main/pg_hba.conf
|    |
|    |   *En dessous de cette ligne "# TYPE  DATABASE        USER            ADDRESS                 METHOD"
|    |   *=> ajouter "host all all localhost scram-sha-256"
|    |
|    |   sudo systemctl restart postgresql.service
|    |   
|    |   *Test du fontionnement (MDP : "leffe")
|    |   => sudo apt install postgresql-client
|    |   => psql -h localhost -U postgres -d template1
|    |   *Si vous accedez à la Bd "Template1", cela fontionne sinon reprenez les étapes précédentes
|    |
|    |   \q
|    |
|    |   psql -U postgres -h localhost -d template1
|    |   
|    |   create role Administrateur WITH LOGIN PASSWORD 'Administrateur';
|    |   create role Utilisateur WITH LOGIN PASSWORD 'Utilisateur';
|    |
|    |   \i /home/[nom session]/Bureau/SAE-51/Serveur/Configuration/PostgreSQL_config.sql
|    |
|    |   \q
|    |
|    +---------------------------------------------------------
|
|    +---------------------Certificat SSL---------------------- 
|    |
|    |   sudo mkdir /certs
|    |   cd /certs
|    |
|    |   *Entrer un MDP (ici leffe) et les informations demandées (peu importe le contenu)
|    |   sudo openssl req -x509 -nodes -days 10000 -newkey rsa:4096 -keyout /certs/SAE51.key -out /certs/SAE51.crt
|    |
|    +---------------------------------------------------------
|
|    +-------------------------Nginx---------------------------  
|    |   
|    |   sudo apt install nginx
|    |
|    |   sudo mkdir /var/www/sae-51
|    |
|    |   sudo chown -R $USER:$USER /var/www/sae-51
|    |   sudo chmod -R 755 /var/www/sae-51
|    |
|    |   sudo cp /home/$USER/Bureau/SAE-51/Serveur/Configuration/Nginx.txt /etc/nginx/sites-available/sae-51
|    |   sudo rm /etc/nginx/sites-available/default
|    |
|    |   sudo ln -s /etc/nginx/sites-available/sae-51 /etc/nginx/sites-enabled/
|    |   sudo rm /etc/nginx/sites-enabled/default
|    |
|    |   systemctl restart nginx
|    |
|    |   sudo ufw allow 'Nginx HTTPS'
|    |
|    +---------------------------------------------------------
|
|    +-------------------------Tomcat--------------------------  
|    |
|    |   !!! Ne pas utiliser une version de tomcat supérieure à 9 car Spring 5 ne supporte pas Jakarta
|    |
|    |   sudo groupadd tomcat
|    |   sudo useradd -s /bin/false -g tomcat -d /opt/tomcat tomcat
|    |   cd /tmp
|    |   wget -c https://dlcdn.apache.org/tomcat/tomcat-9/v9.0.96/bin/apache-tomcat-9.0.96.tar.gz
|    |
|    |   sudo mkdir /opt/tomcat
|    |   cd /opt/tomcat
|    |   sudo tar xzvf /tmp/apache-tomcat-9.0.96.tar.gz -C /opt/tomcat --strip-components=1
|    |
|    |   cd /opt
|    |   sudo chown -R tomcat: tomcat
|    |   cd ./tomcat
|    |   sudo chown -R tomcat webapps/ work/ temp/ logs/ conf/
|    |   sudo chmod o+x /opt/tomcat/bin/
|    |
|    |   sudo cp /home/$USER/Bureau/SAE-51/Serveur/Configuration/tomcat.service /etc/systemd/system/tomcat.service
|    |   sudo nano /etc/systemd/system/tomcat.service
|    |   *Modifier cette ligne "Environment=JAVA_HOME=/usr/java/[VERSION JDK]" en changeant "Environment=JAVA_HOME=/usr/java/[VERSION JDK]"
|    |   => vous pouvez trouver la version en tapant les commandes suivantes :
|    |   ==> cd /usr/java
|    |   ==> ls                                                               (prendre le nom du dossier)
|    |
|    |   sudo ufw allow 8080
|    |   sudo ufw allow 8443
|    |
|    |   sudo systemctl daemon-reload
|    |
|    |   sudo systemctl start tomcat
|    |   *Test fonctionnement "localhost:8080"
|    |
|    |   sudo nano /opt/tomcat/conf/tomcat-users.xml
|    |
|    |   *Ajouter "<role rolename="admin-gui"/><role rolename="manager-gui"/><user username="admin" password="leffe" roles="admin-gui,manager-gui,manager-script"/>"
|    |   => entre les deux balises "<tomcat-users>"
|    |
|    |   sudo systemctl disable tomcat 
|    |   sudo systemctl stop tomcat                                                     (une fois les test terminés)
|    |   
|    |   *Il est nécéssaire de passer par "localhost:8080" afin d'accéder à l'interface admin
|    |
|    |   *SSL
|    |   cd /certs
|    |   sudo openssl pkcs12 -export -in SAE51.crt -inkey SAE51.key -out SAE51.p12 -name tomcat
|    |   => MDP : leffe
|    |   
|    |   sudo /usr/java/[version JDK]/bin/keytool -importkeystore -deststorepass administrateur -destkeystore /opt/tomcat/conf/tomcat.keystore -srckeystore SAE51.p12 -srcstoretype PKCS12 -srcstorepass leffe -alias tomcat
|    |   => MDP keytool "administrateur"
|    |
|    |   sudo cp /home/$USER/Bureau/SAE-51/Serveur/Configuration/Tomcat.xml /opt/tomcat/conf/server.xml
|    |
|    +---------------------------------------------------------
|
|    +------------------------NetBEANS------------------------- 
|    |
|    |   cd ~/Téléchargements
|    |   sudo wget -c https://archive.apache.org/dist/netbeans/netbeans-installers/22/apache-netbeans_22-1_all.deb -O netbeans.deb
|    |   sudo chmod 777 ./netbeans.deb
|    |
|    |   sudo apt install ./netbeans.deb
|    |   sudo rm ./netbeans.deb
|    |
|    |   *Ouvrir le projet (il faut cloner le projet avant) : /home/$USER/Bureau/SAE-52/NetBEANS/SAE51  
|    |
|    |   *Ajout serveur Tomcat
|    |   Tools > Server > Apache Tomcat or TomEE > Server location : "/opt/tomcat/" | username : "admin | login : "leffe"
|    |
|    |   *Lancer NetBEANS (obligatoire)
|    |   => "sudo netbeans --jdkhome /usr/java/[version JDK]" ou Start.sh
|    |   
|    +---------------------------------------------------------
|
|    +---------------------Ajout Certificat-------------------- 
|    |
|    |   Il faut se connecter aux sites suivants et "Avancé" > "Accepter le risque et poursuivre" (si ce n'est pas fait, il y aura une erreur CORS !!!) :
|    |   => Nginx : https://[@IP VM]/
|    |   => Tomcat (servlets) : https://[@IP VM]:8443/SAE51/
|    |
|    +---------------------------------------------------------
|
|    +----------------------------VM--------------------------- 
|    |
|    |   *Diminuer la taille de la VM
|    |   => Vider le cache snapd et journaux (VM)
|    |   ==> sudo rm -rf /var/lib/snapd/cache/*
|    |   ==> sudo journalctl --vacuum-time=1d       (remettre la date souhaitée par la suite)
|    |   ==> sudo rm -rf /tmp/*
|    |   ==> sudo rm -rf /var/tmp/*
|    |   ==> 
|    |   ==> *Ces commandes prennent du temps à s'executer
|    |   ==> sudo dd if=/dev/zero of=/zero bs=1M
|    |   ==> sudo rm /zero
|    |   ==> sudo sync
|    |   ==> 
|    |   ==> sudo apt-get autoremove
|    |   ==> sudo apt-get clean
|    |   ==> sudo apt-get autoclean
|    |
|    |   => VirtualBox (Windows)
|    |   ==> ouvrir un cmd
|    |   ==> cd C:\Program Files\Oracle\VirtualBox
|    |   ==> VBoxManage modifyvm SAE-51 --nested-hw-virt on
|    |   ==> VBoxManage modifymedium "chemin-du-fichier.vdi" --compact
|    |
|    +---------------------------------------------------------
|
+---------------------------------------------------------
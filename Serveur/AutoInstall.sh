#Auteur : Maxime VALLET
#Version 1.1

sudo clear

echo "Script d'installation du projet pour Debian 12"

sleep 2


optionInstall="undetermined"
c=0

#Tant que l'on a pas une option valide, on redemande à l'utilisateur d'en saisir une
while [ "$optionInstall" != "o" ] && [ "$optionInstall" != "O" ] && [ "$optionInstall" != "n" ] && [ "$optionInstall" != "N" ]
do
    clear

    if [ "$c" -ge 1 ]
    then
        echo "Mauvaise saisie \"$optionInstall\", merci de resaisir votre option"

        echo
    fi

    echo "Êtes-vous sûr de vouloir procéder à l'installation de : VSCode, git, ufw, curl, NetBeans, PostgreSQL, Tomcat et Nginx ? [O/N]"
    echo "Votre OS ne doit comporter aucun de ces programmes"

    echo

    sleep 1

    #Lecture de l'entrée utilisateur
    read  -n 1 -p "Option :" optionInstall

    c=$(($c+1)) 
done


clear

#Si l'utilisateur a répondu oui, on installe les programmes
if [ "$optionInstall" = "o" ] || [ "$optionInstall" = "O" ]
then
    optionGA="undetermined"
    c=0

    #Tant que l'on a pas une option valide, on redemande à l'utilisateur d'en saisir une
    while [ "$optionGA" != "o" ] && [ "$optionGA" != "O" ] && [ "$optionGA" != "n" ] && [ "$optionGA" != "N" ]
    do
        clear

        if [ "$c" -ge 1 ]
        then
            echo "Mauvaise saisie \"$optionGA\", merci de resaisir votre option"

            echo
        fi

        echo "Êtes-vous sûr de vouloir procéder à l'installation des guest addition (VM) [O/N]"
        echo "Merci de monter l'ISO avant d'entrer l'option !!!"

        echo

        sleep 1
        
        #Lecture de l'entrée utilisateur
        read  -n 1 -p "Option :" optionGA

        c=$(($c+1)) 
    done


    clear


    #Installation des guest addition
    if [ "$optionGA" = "o" ] || [ "$optionGA" = "O" ]
    then
        echo "Installation des Guest Addition"
        echo
        sudo apt install make gcc dkms linux-source linux-headers-$(uname -r)
        cd /media/cdrom0
        sudo sh VBoxLinuxAdditions.run
    fi

    clear

    echo "Mise à jour du système"
    echo
    sudo apt update
    sudo apt upgrade

    clear

    echo "Installation d'ufw"
    echo
    sudo apt install ufw
    sudo systemctl enable ufw
    sudo ufw enable

    clear

    echo "Installation de curl"
    echo
    sudo apt install curl

    clear

    echo "Installation de Git"
    echo
    sudo apt-get install git

    clear

    echo "Installation du JDK"
    echo
    sudo mkdir /usr/java
    cd /usr/java
    sudo mkdir ./openjdk-22.0.2
    sudo wget -c https://download.java.net/java/GA/jdk22.0.2/c9ecb94cd31b495da20a27d4581645e8/9/GPL/openjdk-22.0.2_linux-x64_bin.tar.gz
    sudo tar xzvf ./openjdk-22.0.2_linux-x64_bin.tar.gz -C ./openjdk-22.0.2 --strip-components=1
    sudo rm openjdk-22.0.2_linux-x64_bin.tar.gz

    clear

    echo "Installation de VSCode"
    echo
    cd ~/Téléchargements
    #Pas sûr que ce lien soit statique (n'a pas changé en 1 mois malgré des MAJ)
    sudo wget -c https://go.microsoft.com/fwlink/?LinkID=760868 -O vscode.deb
    sudo apt install ./vscode.deb
    sudo rm ./vscode.deb 

    clear

    echo "Installation de PostgreSQL"
    echo
    sudo apt install postgresql
    clear
    cd /etc/postgresql/
    postgreSQLVersion=`ls | head -n 1`
    cd /etc/postgresql/$postgreSQLVersion/main/
    sudo cp /home/sae-51/Bureau/SAE-51/Serveur/Configuration/postgresql.conf /etc/postgresql/$postgreSQLVersion/main/postgresql.conf
    sudo cp /home/sae-51/Bureau/SAE-51/Serveur/Configuration/pg_hba.conf /etc/postgresql/$postgreSQLVersion/main/pg_hba.conf
    sudo systemctl disable postgresql
    sudo systemctl restart postgresql.service
    sudo apt install postgresql-client

    clear

    echo "Création des clés SSL"
    echo
    sudo mkdir /certs
    cd /certs
    echo
    echo "Merci d'entrer \"leffe\" pour le MDP et peu importe pour le reste)"
    sudo openssl req -x509 -nodes -days 10000 -newkey rsa:4096 -keyout /certs/SAE51.key -out /certs/SAE51.crt

    clear

    echo "Installation de Nginx"
    echo
    sudo apt install nginx
    sudo mkdir /var/www/sae-51
    sudo chown -R $USER:$USER /var/www/sae-51
    sudo chmod -R 755 /var/www/sae-51
    sudo cp /home/$USER/Bureau/SAE-51/Serveur/Configuration/Nginx.txt /etc/nginx/sites-available/sae-51
    sudo rm /etc/nginx/sites-available/default
    sudo ln -s /etc/nginx/sites-available/sae-51 /etc/nginx/sites-enabled/
    sudo rm /etc/nginx/sites-enabled/default
    sudo systemctl daemon-reload
    sudo systemctl disable nginx
    sudo systemctl restart nginx
    sudo ufw allow 'Nginx HTTPS'
    sudo ufw allow 'Nginx HTTP'
    
    clear

    echo "Installation de Tomcat"
    echo
    sudo groupadd tomcat
    sudo useradd -s /bin/false -g tomcat -d /opt/tomcat tomcat
    cd /tmp
    #Liens de téléchargement tomcat car il change à chaque version
    TOMCAT_URL=$(curl -sS https://tomcat.apache.org/download-90.cgi | grep \  '>tar.gz</a>' | head -1 | grep -E -o 'https://[a-z0-9:./-]+.tar.gz') TOMCAT_NAME=$(echo $TOMCAT_URL | grep -E -o 'apache-tomcat-[0-9.]+[0-9]')
    wget -c $TOMCAT_URL
    sudo mkdir /opt/tomcat
    cd /opt/tomcat
    sudo tar xzvf /tmp/apache-tomcat-9.0.96.tar.gz -C /opt/tomcat --strip-components=1
    cd /opt
    sudo chown -R tomcat: tomcat
    cd ./tomcat
    sudo chown -R tomcat webapps/ work/ temp/ logs/ conf/
    sudo chmod o+x /opt/tomcat/bin/
    sudo cp /home/$USER/Bureau/SAE-51/Serveur/Configuration/tomcat.service /etc/systemd/system/tomcat.service
    sed -i 's/\[VERSION JDK\]/openjdk-22.0.2/g' /etc/systemd/system/tomcat.service
    sudo ufw allow 8080
    sudo ufw allow 8443
    sudo cp /home/sae-51/Bureau/SAE-51/Serveur/Configuration/tomcat-users.xml /opt/tomcat/conf/tomcat-users.xml
    cd /certs
    clear
    echo "Veuillez saisir \"leffe\""
    sudo openssl pkcs12 -export -in SAE51.crt -inkey SAE51.key -out SAE51.p12 -name tomcat
    clear
    echo "Veuillez saisir \"administrateur\" si demandé"
    sudo /usr/java/[version JDK]/bin/keytool -importkeystore -deststorepass administrateur -destkeystore /opt/tomcat/conf/tomcat.keystore -srckeystore SAE51.p12 -srcstoretype PKCS12 -srcstorepass leffe -alias tomcat
    sudo cp /home/$USER/Bureau/SAE-51/Serveur/Configuration/Tomcat.xml /opt/tomcat/conf/server.xml
    sudo systemctl daemon-reload
    sudo systemctl disable tomcat 
    sudo systemctl stop tomcat 

    clear

    echo "Installation de NetBeans"
    echo
    cd ~/Téléchargements
    sudo wget -c https://archive.apache.org/dist/netbeans/netbeans-installers/22/apache-netbeans_22-1_all.deb -O netbeans.deb
    sudo chmod 777 ./netbeans.deb
    sudo apt install ./netbeans.deb
    sudo rm ./netbeans.deb
    sudo systemctl disable netbeans


    #Fin
    if [ "$optionGA" = "o" ] || [ "$optionGA" = "O" ]
    then
        echo "Merci de redémarrer votre ordinateur afin de terminer l'installation"
    fi

else
    echo "Les programmes ne seront pas installés"

    echo
fi

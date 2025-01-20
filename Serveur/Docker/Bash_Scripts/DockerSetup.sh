#!/bin/bash
#Auteur : Maxime VALLET
#Version 2.0

clear

#Vérification du packet manager de la distribution (APT)
if [ -f "/usr/bin/apt" ]
then
    #Rien
    :
else
    echo "Distribution incompatible (nécéssite APT)"

    #Arrêt du script
    exit 1
fi


#Ajout des alias afin de raccourcir les commandes
TestAllias=$(cat ~/.bashrc | grep "#Alias pour les conteneurs docker de la SAE51")
if [ "$TestAllias" = "$null" ]
then
    echo "Ajout des alias :"
    echo "" >> ~/.bashrc
    echo "#Alias pour les conteneurs docker de la SAE51" >> ~/.bashrc
    
    if [ "$2" = "Dev" ]
    then
        echo "alias StartSAE51='sudo /home/\$USER/Bureau/SAE-51/Serveur/Docker/Bash_Scripts/DockerStart.sh '\$USER''" >> ~/.bashrc
        echo "alias StopSAE51='sudo /home/\$USER/Bureau/SAE-51/Serveur/Docker/Bash_Scripts/DockerStop.sh '\$USER''" >> ~/.bashrc
        echo "alias BuildSAE51='sudo /home/\$USER/Bureau/SAE-51/Serveur/Docker/Bash_Scripts/DockerBuild.sh '\$USER''" >> ~/.bashrc

    elif [ "$2" = "Deploy" ]
    then
        echo "alias StartSAE51='sudo /DockerStart.sh '\$USER' 'Deploy''" >> ~/.bashrc
        echo "alias StopSAE51='sudo /DockerStop.sh '\$USER''" >> ~/.bashrc
        echo "alias UpdateSAE51='sudo docker pull ghcr.io/max51v2/tomcat_sae51:latest && sudo docker pull ghcr.io/max51v2/psql_sae51:latest && docker pull ghcr.io/max51v2/nginx_sae51:latest'" >> ~/.bashrc
    fi
    
else
    echo "Alias existants :"
fi


clear

#Ouverture des ports du pare-feu
echo "Installation d'ufw et ouverture des ports utilisés par Tomcat et Nginx :"
sudo apt-get install ufw > /tmp/DockerSetupLogs.txt
clear
echo "Ajout des règles du pare-feu :"
sudo ufw allow 443 >> /tmp/DockerSetupLogs.txt
sudo ufw allow 8443 >> /tmp/DockerSetupLogs.txt

clear

#Installation de docker
echo "Installation des dépendances (apt-transport-https / curl / ca-certificates / software-properties-common) :"
sudo apt-get install -y apt-transport-https ca-certificates curl software-properties-common >> /tmp/DockerSetupLogs.txt
echo "Ajout des clés du répertoire de docker :"
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add - >> /tmp/DockerSetupLogs.txt
echo "Ajout du répertoire de Docker :"
sudo add-apt-repository -y "deb [arch=amd64] https://download.docker.com/linux/ubuntu focal stable" >> /tmp/DockerSetupLogs.txt
apt-cache policy docker-ce >> /tmp/DockerBuildLogs.txt >> /tmp/DockerSetupLogs.txt
echo "Installation de docker-ce :"
sudo apt-get install -y docker-ce >> /tmp/DockerBuildLogs.txt >> /tmp/DockerSetupLogs.txt

clear

#Ajout de l'utilisateur au groupe docker
echo "Ajout de l'uilisateur au groupe docker :"
sudo addgroup docker >> /tmp/DockerSetupLogs.txt
sudo usermod -aG docker $1 >> /tmp/DockerSetupLogs.txt
if [ "$2" = "Dev" ]
then
    sudo systemctl disable docker >> /tmp/DockerSetupLogs.txt
elif [ "$2" = "Deploy" ]
then
    sudo systemctl enable docker >> /tmp/DockerSetupLogs.txt
fi
sudo systemctl restart docker >> /tmp/DockerSetupLogs.txt


#Téléchargement des images
if [ "$2" = "Deploy" ]
then
    clear
    
    echo "Téléchargmement des images Docker :"
    sudo docker pull ghcr.io/max51v2/tomcat_sae51:latest
    echo
    sudo docker pull ghcr.io/max51v2/psql_sae51:latest
    echo
    sudo docker pull ghcr.io/max51v2/nginx_sae51:latest
fi


#Nettoyage
if [ "$2" = "Deploy" ]
then
    #Copie des scripts
    sudo cp /home/$USER/Bureau/SAE-51/Serveur/Docker/Bash_Scripts/DockerStart.sh /DockerStart.sh
    sudo cp /home/$USER/Bureau/SAE-51/Serveur/Docker/Bash_Scripts/DockerStop.sh /DockerStop.sh
    sudo cp /home/$USER/Bureau/SAE-51/Serveur/Docker/Dockercompose.yml /Dockercompose.yml

    #Droits scripts
    chmod 775 /DockerStart.sh
    chmod 775 /DockerStop.sh

    #Retrait du projet
    sudo rm -rf /home/$USER/Bureau/SAE-51/

    #Retrait de git et gh
    gh auth logout
    sudo apt-get remove -y git gh

    #Nettoyage dépendances
    sudo apt-get clean
    sudo apt-get -y autoremove
fi
    

clear

echo
echo "Configuration terminée"
echo "Logs disponibles ici : /tmp/DockerSetupLogs.txt"
echo "Merci de redémarrer votre ordinateur"
echo

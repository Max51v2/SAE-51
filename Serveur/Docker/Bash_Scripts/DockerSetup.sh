#!/bin/bash
#Auteur : Maxime VALLET
#Version 1.2

clear

#Ajout des alias afin de raccourcir les commandes
TestAllias=$(cat ~/.bashrc | grep "#Alias pour les conteneurs docker de la SAE51")
if [ "$TestAllias" = "$null" ]
then
    echo "Ajout des alias :"
    echo "" >> ~/.bashrc
    echo "#Alias pour les conteneurs docker de la SAE51" >> ~/.bashrc
    echo "alias StartSAE51='sudo /home/\$USER/Bureau/SAE-51/Serveur/Docker/Bash_Scripts/DockerStart.sh '\$USER''" >> ~/.bashrc
    echo "alias BuildSAE51='sudo /home/\$USER/Bureau/SAE-51/Serveur/Docker/Bash_Scripts/DockerBuild.sh '\$USER''" >> ~/.bashrc
    echo "alias StopSAE51='sudo /home/\$USER/Bureau/SAE-51/Serveur/Docker/Bash_Scripts/DockerStop.sh '\$USER''" >> ~/.bashrc
else
    echo "Alias existants :"
fi


clear

#Ouverture des ports du pare-feu
echo "Installation d'ufw et ouverture des ports utilisés par Tomcat et Nginx :"
sudo apt install ufw >> /tmp/DockerSetupLogs.txt
clear
echo "Ajout des règles du pare-feu :"
sudo ufw allow 443 >> /tmp/DockerSetupLogs.txt
sudo ufw allow 8443 >> /tmp/DockerSetupLogs.txt

clear

#Installation de docker
echo "Installation des dépendances (apt-transport-https / curl / ca-certificates / software-properties-common) :"
sudo apt install -y apt-transport-https ca-certificates curl software-properties-common >> /tmp/DockerSetupLogs.txt
echo "Ajout des clés du répertoire de docker :"
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add - >> /tmp/DockerSetupLogs.txt
echo "Ajout du répertoire de Docker :"
sudo add-apt-repository -y "deb [arch=amd64] https://download.docker.com/linux/ubuntu focal stable" >> /tmp/DockerSetupLogs.txt
apt-cache policy docker-ce >> /tmp/DockerBuildLogs.txt >> /tmp/DockerSetupLogs.txt
echo "Installation de docker-ce :"
sudo apt install -y docker-ce >> /tmp/DockerBuildLogs.txt >> /tmp/DockerSetupLogs.txt

clear

#Ajout de l'utilisateur au groupe docker
echo "Ajout de l'uilisateur au groupe docker :"
sudo addgroup docker >> /tmp/DockerSetupLogs.txt
sudo usermod -aG docker $1 >> /tmp/DockerSetupLogs.txt
sudo systemctl disable docker >> /tmp/DockerSetupLogs.txt
sudo systemctl restart docker >> /tmp/DockerSetupLogs.txt

clear

echo
echo "Configuration terminée"
echo "Logs disponibles ici : /tmp/DockerSetupLogs.txt"
echo "Merci de redémarrer votre ordinateur"
echo

#Auteur : Maxime VALLET
#Version 1.6


sudo clear

optionJDK="undetermined"
c=0
#Tant que l'on a pas une option valide, on redemande à l'utilisateur d'en saisir une
while [ "$optionJDK" != "o" ] && [ "$optionJDK" != "O" ] && [ "$optionJDK" != "n" ] && [ "$optionJDK" != "N" ]
do
    clear

    if [ "$c" -ge 1 ]
    then
        echo "Mauvaise saisie \"$optionJDK\", merci de resaisir votre option"

        echo
    fi

    echo "Souhaitez vous retirer le JDK ? [O/N]"

    echo

    sleep 1

    #Lecture de l'entrée utilisateur
    read  -n 1 -p "Option :" optionJDK

    c=$(($c+1)) 
done

optionCode="undetermined"
c=0
#Tant que l'on a pas une option valide, on redemande à l'utilisateur d'en saisir une
while [ "$optionCode" != "o" ] && [ "$optionCode" != "O" ] && [ "$optionCode" != "n" ] && [ "$optionCode" != "N" ]
do
    clear

    if [ "$c" -ge 1 ]
    then
        echo "Mauvaise saisie \"$optionCode\", merci de resaisir votre option"

        echo
    fi

    echo "Souhaitez vous retirer VSCode ? [O/N]"

    echo

    sleep 1

    #Lecture de l'entrée utilisateur
    read  -n 1 -p "Option :" optionCode

    c=$(($c+1)) 
done

#Retrait programmes
clear
cd /usr/java
Java_version=`ls | head -n 1`
clear

sudo apt-get -y --purge remove postgresql postgresql-*
sudo apt-get -y purge nginx nginx-common
sudo rm -rf /opt/tomcat/*
sudo rmdir /tomcat
if [ "$optionJDK" = "o" ] || [ "$optionJDK" = "O" ]
then
    sudo rm -rf /usr/java/$Java_version/*
    sudo rmdir /usr/java/$Java_version/
fi
if [ "$optionCode" = "o" ] || [ "$optionCode" = "O" ]
then
    sudo apt-get -y remove code
fi
sudo apt autoremove -y --purge apache-netbeans
sudo rm -rf /Netbeans/*
sudo rmdir /Netbeans
sudo rm -rf /certs/*
sudo rmdir /certs/
sudo apt autoremove -y

echo "Suppression achevée"

#Suppression du projet
sudo rm -rf /home/$USER/Bureau/SAE-51/*
sudo rm -rf /home/$USER/Bureau/SAE-51/

#Auteur : Maxime VALLET
#Version 1.3


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
sudo apt-get --purge remove postgresql postgresql-*
sudo apt-get purge nginx nginx-common
sudo rm -rf /opt/tomcat/*
sudo rmdir /tomcat
if [ "$optionJDK" = "o" ] || [ "$optionJDK" = "O" ]
then
    sudo rm -rf /usr/java/openjdk-22.0.2/*
    sudo rmdir /usr/java/openjdk-22.0.2/
fi
if [ "$optionCode" = "o" ] || [ "$optionCode" = "O" ]
then
    sudo apt-get remove code
fi
sudo apt autoremove --purge apache-netbeans
sudo rm -rf /Netbeans/*
sudo rmdir /Netbeans
sudo rm -rf /Certs/*
sudo rmdir /Certs/

echo "Suppression achevée"

#Suppression du projet
sudo rm -rf /home/$USER/Bureau/SAE-51/*
sudo rmdir /home/$USER/Bureau/SAE-51/

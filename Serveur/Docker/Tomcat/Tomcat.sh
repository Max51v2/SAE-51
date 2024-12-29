#!/bin/bash
#Auteur : Maxime VALLET
#Version 1.0

#Info : Faites ATTENTION à l'emplacement de l'installation du JDK car tomcat vérifie la validité de celui-ci selon le chemin où il se situe (c'est débile.......)
#utilisé ici : emplacement où apt installe openjdk
mkdir /usr/lib/jvm/
cd /usr/lib/jvm/
mkdir ./openjdk
JDK="openjdk-22.0.2_linux-x64_bin.tar.gz"
wget -c https://download.java.net/java/GA/jdk22.0.2/c9ecb94cd31b495da20a27d4581645e8/9/GPL/$JDK
tar xzvf ./$JDK -C ./openjdk --strip-components=1
rm $JDK

#Tomcat
groupadd tomcat
useradd -s /bin/false -g tomcat -d /opt/tomcat tomcat
cd /tmp
TOMCAT_URL=$(curl -sS https://tomcat.apache.org/download-90.cgi | grep '>tar.gz</a>' | head -1 | grep -E -o 'https://[a-z0-9:./-]+.tar.gz')
TOMCAT_NAME=$(echo $TOMCAT_URL | grep -E -o 'apache-tomcat-[0-9.]+[0-9]')
wget -c $TOMCAT_URL
mkdir /opt/tomcat
tar xzvf /tmp/$TOMCAT_NAME".tar.gz" -C /opt/tomcat --strip-components=1
rm /tmp/$TOMCAT_NAME".tar.gz"
cd /opt
chown -R tomcat tomcat
cd ./tomcat
chown -R tomcat webapps/ work/ temp/ logs/ conf/
chmod o+x /opt/tomcat/bin/
/usr/lib/jvm/openjdk/bin/keytool -importkeystore -deststorepass administrateur -destkeystore /opt/tomcat/conf/tomcat.keystore -srckeystore /certs/SAE51.p12 -srcstoretype PKCS12 -srcstorepass leffe -alias tomcat
cp /conf/tomcat-users.xml /opt/tomcat/conf/tomcat-users.xml
cp /conf/Tomcat.xml /opt/tomcat/conf/server.xml
mkdir /Netbeans
mkdir /Netbeans/conf
cp /conf/sae_51.conf /Netbeans/conf/sae_51.conf
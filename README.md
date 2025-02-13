Auteur : Maxime VALLET
Version : 4.2

En cas de problèmes d'affichage, merci d'ouvrir la doc depuis le projet et de séléctionner l'affichage de type "Raw"

I) Présentation
 Ce projet consistait à la réalisation d'un projet en 150h à 3 personnes.
 Nous avons choisi de réaliser un outil de supervision et de contrôle des machines accessible par le biais d'un site Web.
 Il est axé autour de 3 points :
  - un client qui envoi des informations sur la machine et qui exécute des tâches envoyées par le serveur
  - une collection de serveurs qui vont faire de la collecte et du traitement d'informations
  - un site Web qui représente la partie interactions utilisateur


II) Comment installer le projet ?
 1) Méthodes d'installation du Backend
  Merci de choisir le medium d'installation approprié et de vous référencer à la doc dans le tableau.

 +----------------------------------+---------------+----------------------------+---------------+----------------+-----------------+--------------------+
 |                                  |     VM V1     |           VM V2            |    Manuel     | AutoInstall.sh | DockerSetup Dev | DockerSetup Deploy |
 +----------------------------------+---------------+----------------------------+---------------+----------------+-----------------+--------------------+
 |           I - Méthode            |               |                            |               |                |                 |                    |
 +----------------------------------+---------------+----------------------------+---------------+----------------+-----------------+--------------------+
 |                VM                |       x       |             x              |               |                |                 |                    |
 +----------------------------------+---------------+----------------------------+---------------+----------------+-----------------+--------------------+
 |              Manuel              |               |                            |       x       |                |                 |                    |
 +----------------------------------+---------------+----------------------------+---------------+----------------+-----------------+--------------------+
 |           Automatique            |               |                            |               |       x        |        x        |                    |
 +----------------------------------+---------------+----------------------------+---------------+----------------+-----------------+--------------------+
 |         II - Propriétés          |               |                            |               |                |                 |                    |
 +----------------------------------+---------------+----------------------------+---------------+----------------+-----------------+--------------------+
 |               GUI                |       x       |                            |       x       |       x        |        x        |         x          |
 +----------------------------------+---------------+----------------------------+---------------+----------------+-----------------+--------------------+
 |               CLI                |               |             x              |       x       |                |        x        |         x          |
 +----------------------------------+---------------+----------------------------+---------------+----------------+-----------------+--------------------+
 |             Env Dev              |       x       |                            |       x       |       x        |     Docker      |                    |
 +----------------------------------+---------------+----------------------------+---------------+----------------+-----------------+--------------------+
 |              Rapide              |               |             x              |               |       x        |        x        |         x          |
 +----------------------------------+---------------+----------------------------+---------------+----------------+-----------------+--------------------+
 |        Env pré-téléchargé        |       x       |             x              |               |                |                 |         x          |
 +----------------------------------+---------------+----------------------------+---------------+----------------+-----------------+--------------------+
 |              Taille              |     10Go      |            7Go             |      1Go      |      1Go       |      1,5Go      |       1,5Go        |
 +----------------------------------+---------------+----------------------------+---------------+----------------+-----------------+--------------------+
 |          III - Objectif          | Dev FullStack | Dev FrontEnd / Déploiement | Dev FullStack | Dev FullStack  |   Dev Docker    |    Déploiement     |
 +----------------------------------+---------------+----------------------------+---------------+----------------+-----------------+--------------------+
 | IV - Instructions d'installation |       1       |             1              |       1       |       1        |        2        |         2          |
 +----------------------------------+---------------+----------------------------+---------------+----------------+-----------------+--------------------+
 |                                  |               |                            |               |                |                 |                    |
 +----------------------------------+---------------+----------------------------+---------------+----------------+-----------------+--------------------+
 |    1 = /Serveur/README_VM.txt    |               |                            |               |                |                 |                    |
 +----------------------------------+---------------+----------------------------+---------------+----------------+-----------------+--------------------+
 |  2 = /Serveur/Docker/README.txt  |               |                            |               |                |                 |                    |
 +----------------------------------+---------------+----------------------------+---------------+----------------+-----------------+--------------------+


 2) Installation du client
  Merci de consulter la doc dans "/app/README.txt"

 3) Utilisation du site Web
  Merci de vous connecter à : https://[IP Serveur]
    => Acceptez les risques (certificat SSL auto-signé) et recommencez pour Tomcat (cf bannière)


III) Qui contacter en cas de question ?
 - Maxime VALLET : 
   - Installation du Backend : VM / Docker / Scripts d'installation
   - Backend : Authentification et droits utilisateur / info statiques PC / logs / Notifications / Bouttons de Nav / Tests Unitaires des points précédents / AnswerPing (déprécié)
   - Documentation + Templates
   - FrontEnd : Login.html / Help.html / Bouttons de Nav / Authentification / Expiration de session
 
 - Gabin PETITCOLAS
   - FrontEnd : HTML / CSS / JS

 - Erwann MADEC :
   - Client
   - Backend : Serveur et Client TCP sécurisés


IV) Modification VM
- sudo ufw allow 50000
- ajouter la redirection du port 50000 pour du TCP sur la carte NAT de la VM


V) Avancement du projet
IMPORTANT : merci de suivre les templates pour le projet SAE51 afin de pouvoir intégrer les features pré-existantes.
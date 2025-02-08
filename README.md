Auteur : Maxime VALLET
Version : 4.0

En cas de problèmes d'affichage, merci d'ouvrir la doc depuis le projet et de séléctionner l'affichage de type "Raw"

I) Présentation
 à faire


II) Comment installer le projet ?
 1) Méthodes d'installation

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


 2) Installation et configuration des programmes
  -- Tout --
    Se référencer aux documentations indiquées dans le tableau précédent

 3) Démarrer le projet
  -- Si environnement de dev (hors Docker) --
    - Lancez Start.sh puis saisissez "o" pour reconstruire la BD (à faire lors du premier démarrage ou à chaque commit) puis "o" pour lancer NetBeans
    - Lancez le projet Java ouvert dans NetBeans et saisissez les logins+MDP si besoin (voir doc "/Serveur/README.txt")
    - Ouvrir le navigateur de la VM ou le navigateur de l'OS hôte (nécéssite d'ajouter les certificats > voir doc "/Serveur/README.txt")

  -- Autre --
    Se référencer aux documentations indiquées dans le tableau précédent


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
IMPORTANT : merci de suivre les templates pour le projet SAE51 afin de pouvoir intégrer les features

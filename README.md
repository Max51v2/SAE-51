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




######################################################################################################
à faire (liste non exhaustive) :
Frontend :
  => Ajouter des messages d'erreur (erreurs envoyées par les servlets => voir Javadoc Servlets)
  => Page web gestion des utilisateurs : affichage + suppression + modif MDP
  => Page d'affichage des logs
  => Page web gestion des machines : affichage + suppression + modification des droits
  => Page web qui donne des informations sur la machine (CDC info statiques) + définition des seuils d'alerte des différentes métriques surveillées (CDC partie dynamique) + allumage/extinction/redémarrage machine
  => Page web notifications
  => JS pages Web mentionnées précédement
  
- Backend
  => Gestion des droits d'utilisateur par ordinateur : Ajout d'un utilisateur à une machine
  => Récupération info de suivi machines en continu > lancé avec OnStart (récupération en deux parties : statique (1 fois) et dynamique (en continu + mise en buffer))
  => Le servlet viendra recup les informations de la classe précédente et donnera le temps depuis la dernière act
  => Suivi des métriques renvoyées + log et notif mail si pb détecté
  => Tests unitaires des fonctionnalités mentionnées précédemment (hors chiffrement client/serveur TCP)
    => ListUsersWithAccess et méthodes utilisées

- Client :
  => Script Pshell lancé au démarrage
  => Récupération des informations (voir CDC) en Java ou Pshell (ref ici si Pshell https://github.com/Max51v2/InfSys/tree/main/InfSys_CLI)
  => Gestion de l'extinction/redémarrage de la machine par le biais de l'interface Web (Web => servlet => client TCP côté serveur => Serveur TCP côté client)
  => Serveur TCP chiffré



fait :
- Docker
- VM
    => Retrait de snapcraft de la VM
    => Remplacement d'Apache par Nginx
    => Utilisation de Debian au lieu d'Ubuntu
- Backend
    => Identification login + MDP / token
    => Expiration des tokens après une période d'inactivité
    => Dervlets et DAO utilisateurs
    => Gestion des permissions par page (+redirection)
    => Serveur TCP non chiffré
    => Ajout + listage + suppression pc
    => Récupération des informations via le fichier de config
    => Tests unitaires : servlets utilisateurs + DAOusers (partiel) + serveur TCP + expiration des tokens
    => Droits d'accès aux servlets
    => Système de log (erreurs ou toutes les actions (fichier de config))
    => Gestion des droits d'utilisateur par ordinateur : sauf ajout utilisateur
    => ajout + suppression + listage données statiques pc
    => Renouvelement de session
    => Système de notifications
    => Client/serveur TCP chiffré
    => Bouttons de nav dynamiques
- Frontend :
    => Page de login + aide + Gestion utilisateurs
    => Génération de la nav bar selon les droits d'accès
    => Templates
    => Vérification de l'éxpiration de la session (+pop up pour la renouveler)
    => Bouttons de nav dynamiques
- Client
    => Client TCP non chiffré
    => Récupération ID+IP et envoi au serv
    => Récupération des informations via fichier de config



abandon :
- Arch linux : acceleration 3D non fonctionnelle sur la VM => repli sur Debian
- Let's encrypt : nécéssite un serveur accessible depuis internet et l'enregistrer auprès d'un DNS => incompatible avec les méthodes de dev act
    => Clés auto-signées => requiert une validation dans le navigateur
- Remplacement de Netbeans par VSCode : trop compliqué à utiliser (lignes de commande etc) pour que ça vaille le coût à mettre en place
    => Netbeans



Organisation du projet :
- Documentation : 
    => Documentation : schémas / CDC / présentations
- Serveur :
    => Serveur/README : document utilisation (et configuration du backend si pas de VM)
    => Serveur/ConfigProjet : fichiers de configuration du projet + script de la BD PostgreSQL
    => Serveur/Configuration : fichiers de configuration des serveurs
    => Serveur/Docker : Scripts de configuration, build, démarrage et arrêt des conteurs Docker (+ressources : config serveurs, scripts conteneurs...)
- NetBEANS : 
    => NetBEANS/SAE51 : backend du projet en Java (servlets, serveurs TCP, ...)
    => NetBEANS/SAE51_Client : projet Java en cours de déplacement vers /app/Client (Erwann) 
    => NetBEANS/lib : librairies du projet Java (non inclus dans NetBeans par défaut)
    => NetBEANS/Templates : exemples de classes préfaites (DAO et Servlet)
- Web :
    => Web : HTML
    => Web/JS : scripts JS (scripts présents sur plusieurs pages)
    => Web/images : logos, icones...
    => Web/template : page Web par défaut (celle à utiliser (copier-coller) lors de la création d'une nouvelle page web)
- App :
    => app/Client : Enregistrement de la machine auprès du serveur / récupération des infos statiques et dynamiques et envoi au serveur



#heures :
- maxime : 114h
- erwann : 10h
- gabin :

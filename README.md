Auteur : Maxime VALLET
Version : 2.4


Afin d'installer la VM, merci de vous référer à la partie "VM" du fichier "README_VM.txt" dans le dossier "Serveur"
  => les instructions d'installation sont disponibles ce fichier



#Projet
à faire (liste non exhaustive) :
Frontend :
  => Ajouter des messages d'erreur (erreurs envoyées par les servlets => voir Javadoc Servlets)
  => Page web gestion des utilisateurs : affichage + suppression + modif MDP
  => Page web gestion des machines : affichage + suppression + modification des droits
  => Page web qui donne des informations sur la machine (CDC info statiques) + définition des seuils d'alerte des différentes métriques surveillées (CDC partie dynamique) + allumage/extinction/redémarrage machine
  => Page web notifications
  => JS pages Web mentionnées précédement
  
- Backend
  => Client/serveur TCP chiffré
  => Gestion des droits d'utilisateur par ordinateur
  => Récupération info de suivi machines en continu > lancé avec OnStart (récupération en deux parties : statique (1 fois) et dynamique (en continu + mise en buffer))
  => Le servlet viendra recup les informations de la classe précédente et donnera le temps depuis la dernière act
  => Suivi des métriques renvoyées + log et notif mail si pb détecté
  => Tests unitaires des fonctionnalités mentionnées précédemment (hors chiffrement client/serveur TCP)

- Client :
  => Script Pshell lancé au démarrage
  => Récupération des informations (voir CDC) en Java ou Pshell (ref ici si Pshell https://github.com/Max51v2/InfSys/tree/main/InfSys_CLI)
  => Gestion de l'extinction/redémarrage de la machine par le biais de l'interface Web (Web => servlet => client TCP côté serveur => Serveur TCP côté client)
  => Serveur TCP chiffré



fait :
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
- Frontend :
    => Page de login + aide
    => Template de page Web
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
- Client :
    => Client/Client.ps1 : script qui lance le projet java
    => Client/README : documentation
- Serveur :
    => Serveur/README : document utilisation (et configuration du backend si pas de VM)
    => Serveur/ConfigProjet : fichiers de configuration du projet + script de la BD PostgreSQL
    => Serveur/Configuration : fichiers de configuration des serveurs
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



#heures (point michaël) :
- maxime : 60h
- erwann : 10h
- gabin :

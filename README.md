Auteur : Maxime VALLET
Version : 3.0

I) Présentation
 à faire

II) Comment installer le projet ?
 1) Installation et configuration des programmes
  a) Utiliser la VM contenant tous les programmes configurés :
    - Vous retrouverez les informations utiles dans la section "VM" du document situé ici : "/Serveur/README.txt"
    - Il est recommandé de procéder dans cet ordre : télécharger la VM > suivre les README donnés à chaque partie de l'installation > lire la partie "VM" du document suivant : "/Serveur/README.txt"
  b) Installer les programmes manuellement :
    - Vous retrouverez les informations dans la section "VM" du document situé ici : "/Serveur/README.txt"
  c) Installer les programmes automatiquement :
    - Vous retrouverez les informations dans la section "Procédure d'installation automatique Debian 12" du document situé ici : "/Serveur/README.txt"
  d)  Conteneurs Docker :
    - Vous retrouverez les informations dans le README situé ici : "/Serveur/Docker/README.txt"

 2) Démarrer le projet (hors Docker)
    - Lancez Start.sh puis saisissez "o" pour reconstruire la BD (à faire lors du premier démarrage ou à chaque commit) puis "o" pour lancer NetBeans
    - Lancez le projet Java ouvert dans NetBeans et saisissez les logins+MDP si besoin (voir doc "/Serveur/README.txt")
    - Ouvrir le navigateur de la VM ou le navigateur de l'OS hôte (nécéssite d'ajouter les certificats > voir doc "/Serveur/README.txt")

III) Qui contacter en cas de question ?
 - Serveurs : VM, script d'installation et Docker / Backend : Authentification, info statiques PC, logs et tests unitaires / HTML, CSS et JS pages de login, aide et template : Maxime VALLET
 - HTML et CSS : Gabin PETITCOLAS
 - Client : Erwann MADEC

IV) Modification VM
MODIFICATIONS VM : sudo ufw allow 50000 / ajouter la redirection du port 50000 pour du TCP sur la carte NAT de la VM

V) Avancement du projet
IMPORTANT : merci de suivre les templates pour le projet SAE51 afin de pouvoir intégrer les features

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
  => Client/serveur TCP chiffré
  => Gestion des droits d'utilisateur par ordinateur : Ajout d'un utilisateur à une machine
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



#heures (point michaël) :
- maxime : 85h
- erwann : 10h
- gabin :

Afin d'installer la VM, merci de vous réferer la partie "VM" du fichier "README.txt" dans le dossier "Serveur"=> les instructions d'installation sont disponibles dans le "README" dossier "Serveur" si vous ne souhaitez pas utiliser de VM


Infos :
- Si vous créez une nouvelle page Web, merci d'ajouter votre page dans la table "web_pages_access" située dans la BD sae_51 en première partie du doc (Serveur/Configuration/PostgreSQL.sql)
    => Sinon TokenCheck va automatiquement vous renvoyer vers "login.html"



#Projet
à faire (liste non exhaustive) :
Frontend :
  => page web gestion des utilisateurs (tableau contenant les utilisateurs)
  => page web qui référence les machines enregistrées + onglet pour séléctionner le réseau à scanner afin d'ajouter des machines
  => page web qui donne des info sur la machine (CDC info statiques) + définition des seuils d'alerte des différentes métriques surveillées (CDC partie dynamique)
  => page web notifications
  => JS pages Web
  
- Backend
  => tests Servlets + DAO users
  => ajout + suppression de pc + liste des pc
  => gestion des droits utilisateur par ordinateur
  => récuppération info de suivi machines en continu > lancé avec OnStart (récupération en deux parties : statique (1 fois) et dynamique (en continu + mise en buffer))
  => le servlet viendra recup les infos  de la classe précédente et donnera le temps depuis la dernière act
  => suivi des métriques renvoyées + log et notif mail si pb détecté
  => client TCP chiffré
  
- Client :
  => script Pshell lancé au démarrage
  => recupération des infos (voir CDC) en Java ou Pshell (ref ici si Pshell https://github.com/Max51v2/InfSys/tree/main/InfSys_CLI)
  => gestion de l'extinction/redémarrage de la machine par le biais de l'int Web (Web => servlet => client TCP côté serveur => Serveur TCP côté client)
  => serveur TCP chiffré
  
- BD (schémas voir script sql (dossier Serveur/Configuration))



fait :
- VM
    => retrait de snapcraft de la VM
    => remplacement d'Apache par Nginx
    => utilisation de Debian au lieu d'Ubuntu
- Backend
    => identification login + MDP / token
    => expiration des tokens après une periode d'inactivité
    => Ajout + retrait + liste utilisateur(s)
    => suppression token
    => Gestion des permissions par page (+redirection)
    => Serveur TCP non chiffré
    => Enregistrement PC
- Frontend :
    => page de login
    => template de page Web
-Client
    => Client TCP non chiffré
    => recup ID+IP et envoi au serv



abandon :
- let's encrypt : nécéssite un serveur accessible depuis internet et l'enregistrer auprès d'un DNS => incompatible avec les méthodes de dev act
    => clés autosignées => requiert une validation dans le navigateur
- remplacement de Netbeans par VSCode : trop compliqué à utiliser (lignes de commande etc) pour que ça vaille le coût à mettre en place
    => Netbeans



Organisation du projet :
- Documentation : 
    => Documentation : schémas / CDC / présentations
- Client ;
    => Client.ps1 : script qui lance le projet java
    => SAE51_Client : projet Java qui récuppère les données sur la machine / Serveurs TCP : découverte et envoi des informations
- Serveur :
    => Serveur/README : document utilisation (et configuration du backend si pas de VM)
    => Serveur/Configuration : fichiers de config des serveurs + script de la BD PostgreSQL
- NetBEANS : 
    => NetBEANS/SAE51 : backend du projet en Java (servlets, serveurs TCP, ...)
    => NetBEANS/lib : librairies du projet Java (non inclus dans NetBeans par défaut)
- Web :
    => Web : HTML
    => Web/JS : scripts JS (scripts présents sur plusieurs pages)
    => Web /images : logos, icones...
    => Web /template : page Web par défault (celle à utiliser (copier-coller) quand tu crée une nouvelle page web)



#heures (point michaël) :
- maxime : 37h
- erwann :
- gabin :

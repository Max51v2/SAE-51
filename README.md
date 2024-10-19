Afin d'installer la VM, merci de vous réferer la partie "VM" du fichier "README.txt" dans le dossier "Serveur"
=> !!! Le projet doit être cloné sur le Bureau de la VM !!!



#Projet
à faire :
- page web connexion
- page web gestion des utilisateurs (tableau contenant les utilisateurs)
- système d'authentification : JS + Servlets (maxime)
- VM (Maxime)
- BD (schémas voir script sql (dossier Serveur/Configuration))



fait :
- VM
    => retrait de snapcraft de la VM
    => remplacement d'Apache par Nginx
    => utilisation de Debian au lieu d'Ubuntu



abandon :
- let's encrypt : nécéssite un serveur accessible depuis internet et l'enregistrer auprès d'un DNS => incompatible avec les méthodes de dev act
    => clés autosignées => requiert une validation dans le naviguateur
- remplacement de Netbeans par VSCode : trop compliqué à utiliser (lignes de commande etc) pour que ça vaille le coût à mettre en place
    => Netbeans



Organisation du projet :
- Documentation : schémas / CDC / présentations
- Serveur : informations + configurations de la VM
    => README utilisation (et configuration du backend si pas de VM)
- NetBEANS : backend du projet en Java (servlets, serveurs TCP, ...) + librairies (/NetBEANS/lib)
- Web : HTML + CSS + JS du projet


#heures (point michaël) :
- maxime : 12h
- erwann :
- gabin :

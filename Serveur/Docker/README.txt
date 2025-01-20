#Auteur : Maxime VALLET
#Version : 2.3

# à faire : Monter le contenu de la BD PostgreSQL sur un volume partagé afin qu'il ne soit pas effacé à chaque fois que le conteneur est recréé

################################ Déploiement ################################

gh api -H "Accept: application/vnd.github.v3.raw" /repos/username/repo/contents/path/to/file > path/to/directory/filename.ext


############################### Développement ###############################

Attention :
Cette section est dédié au Développement des conteneurs docker :
- Build des Conteneurs
- Modification des conteneurs


Prérequis :
Assurez-vous d'avoir au moins 1,5 Go de stockage disponible pour héberger les conteneurs Docker


I) Installation des dépendances :
Debian ou dérivé : 
    CF Partie II

Windows :
    1) Installer WSL
    2) Installer Debian ou un dérivé (microsoft store)
    3) CF Partie II


II) Mise en place de l'environnement
a) Clonage du répertoire sur le Bureau (s'il ne l'est pas déjà)
sudo apt install gh
sudo apt install git
gh auth login
=> github.com > https > y > web browser
gh repo clone Max51v2/SAE-51 /home/$USER/Bureau/SAE-51


b) Script de configuration
/home/$USER/Bureau/SAE-51/Serveur/Docker/Bash_Scripts/DockerSetup.sh "$USER"


III) Commandes des conteneurs
Créer les conteneurs Docker :
    !!! ATTENTION !!! => si vous mettez à jour le projet java il faut build le projet dans NetBeans (icône avec le marteau et le balai) et ensuite génération de la Javadoc
    BuildSAE51

Lancement des conteneurs Docker :
    !!! ATTENTION !!! => si un port est déjà utilisé, il peux y avoir des conflits
    StartSAE51

Arrêt des conteneurs Docker :
    StopSAE51


IV) Publication des conteneurs
a) Installez le plugin "Docker" sur vscode
b) RDV sur GitHub > clique gauche sur pfp > settings > Developper settings > Personnal access token > Tokens (classic) > Generate new token > ajouter tous les droits et copier le token
c) Cliquez sur le logo Docker > section registries > connect registry > Github > taper votre pseudo > entrez votre token
d) Onglet Docker > section images > clique gauche sur l'image > push > GitHub > ghcr.io/max51v2/[Nom image]:latest

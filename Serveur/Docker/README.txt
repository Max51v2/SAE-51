#Auteur : Maxime VALLET
#Version : 2.0

# à faire : Monter le contenu de la BD PostgreSQL sur un volume partagé afin qu'il ne soit pas effacé à chaque fois que le conteneur est recréé
# !!! Le contenu de ce dossier a été ajouté après la date de remis du projet !!!

I) Installer Docker :
Debian : 
    Vous pouvez utiliser les scripts en dessous
        => les scripts suivants s'occupent d'installer les dépendances

Windows :
    1) Installer WSL
    2) Installer un dérivé de Debian (microsoft store)
    3) Lire les étapes pour Debian (point au dessus)


II) Commandes à exécuter
a) Clonage du répertoire sur le Bureau (s'il ne l'est pas déjà)
sudo apt install gh
sudo apt install git
gh auth login
=> github.com > https > y > web browser
gh repo clone Max51v2/SAE-51 /home/$USER/Bureau/SAE-51

b) Script de configuration
/home/$USER/Bureau/SAE-51/Serveur/Docker/Bash_Scripts/DockerSetup.sh "$USER"

c) Attention : si vous n'avez pas un ordinateur assez puissant, les requêtes peuvent ne pas aboutir (backend)


III) Conteneurs (nécéssite une distribution basée sur Debian)
Créer les conteneurs Docker :
    !!! ATTENTION !!! => si vous mettez à jour le projet java il faut build le projet dans NetBeans (icône avec le marteau et le balai)
    BuildSAE51

Lancement des conteneurs Docker :
    !!! ATTENTION !!! => si un port est déjà utilisé, le conteneur ne se lancera pas (443 / 5432 / 8443 / 8080)
    StartSAE51

Arrêt des conteneurs Docker :
    StopSAE51

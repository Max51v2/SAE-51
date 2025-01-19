Auteur : Maxime VALLET
Version : 1.1


Des templates permettant de créer les méthodes de la DAO ainsi que des servlets sont disponibles dans "/NetBEANS/Templates"


Servlets :
    => Merci d'ajouter vos nouveaux servlets dans la table "servlet_access" de la BD "sae_51" dans le document "/Serveur/ConfigProjet/PostgreSQL_config.sql"
        => afin que les droits fonctionnent, il faut lancer Start.sh ("/home/$USER/Bureau/SAE-51/Serveur/Start.sh") afin de reconstruire la BD


Test unitaires :
    => afin que vos tests se lancent au lancement de RunTests.java, il faut ajouter votre classe de test dans la classe RunTests.java (dans "@Suite.SuiteClasses")


Lancement de classes au démarrage :
    => la classe OnStart est prévue à cet effet
    => merci de faire attention à la façon dont vous démarrez et arrêtez les classes qui possèdent une boucle infinie (exemple : TokenExpiration.java)
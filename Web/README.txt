Si vous souhaitez déplacer le contenu de ce dossier, merci de modifier les liens dans les pages, la config de Nginx et la BD  (selon les cas)
=> mieux vaut éviter afin de rien casser
==> il est préférable de maintenir l'organisation actuelle


Si vous créez une nouvelle page Web, merci d'ajouter votre page dans la table "web_pages_access" située dans la BD sae_51 en première partie du doc (Serveur/Configuration/PostgreSQL.sql) => Sinon TokenCheck va automatiquement vous renvoyer vers "login.html"


Si vous souhaitez développer la partie HTML, vous pouvez commenter TokenCheck afin de ne pas être gêné par l'authentification
=> Merci de vous réferer au commentaire dans la template Web (là ou sont les scripts)

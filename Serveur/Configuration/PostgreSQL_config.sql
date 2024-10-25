--Auteur original : Maxime VALLET (SAE 52)
--Modifications : Maxime VALLET
--Version : 0.3

-- à modifier
-- type hash

--####################### BD sae_51 #######################

--Base de données du projet
CREATE DATABASE sae_51;
\c sae_51



--Table contenant les informations sur les utilisateurs
CREATE TABLE users (
    login text PRIMARY KEY,
    nom text,
    prenom text,
    droits text,
    hash text,
    token text,
    tokenlifecycle integer
);



--MDP hashés avec Bcrypt (12 passes)
--Compte admin par défaut (MDP "Admin")
INSERT INTO users (login, nom, prenom, droits, hash, token, tokenlifecycle) VALUES ('Admin1', 'Originel', 'Admin', 'Admin', '$2a$12$7bJpdP/8n6Yn.2MdqQoSUO/wEAlzkYpySZGr5NShaDUjfLx3uyUAC','', 0);
--Comptes utilisateur par défaut (MDP "Utilisateur")
INSERT INTO users (login, nom, prenom, droits, hash, token, tokenlifecycle) VALUES ('Utilisateur1', 'Originel', 'Utilisateur', 'Utilisateur', '$2a$12$eEmBwtSF27lOZVL6KbgI9udTtnsEucsEvOJgzEGNISh.IatJtyWbi','', 0);
--utilisateurs pour faire des test de latence
INSERT INTO users (login, nom, prenom, droits, hash, token, tokenlifecycle) VALUES ('Utilisateur2', 'Originel', 'Utilisateur', 'Utilisateur', '$2a$12$eEmBwtSF27lOZVL6KbgI9udTtnsEucsEvOJgzEGNISh.IatJtyWbi','', 0);
INSERT INTO users (login, nom, prenom, droits, hash, token, tokenlifecycle) VALUES ('Utilisateur3', 'Originel', 'Utilisateur', 'Utilisateur', '$2a$12$eEmBwtSF27lOZVL6KbgI9udTtnsEucsEvOJgzEGNISh.IatJtyWbi','', 0);
INSERT INTO users (login, nom, prenom, droits, hash, token, tokenlifecycle) VALUES ('Utilisateur4', 'Originel', 'Utilisateur', 'Utilisateur', '$2a$12$eEmBwtSF27lOZVL6KbgI9udTtnsEucsEvOJgzEGNISh.IatJtyWbi','', 0);
INSERT INTO users (login, nom, prenom, droits, hash, token, tokenlifecycle) VALUES ('Utilisateur5', 'Originel', 'Utilisateur', 'Utilisateur', '$2a$12$eEmBwtSF27lOZVL6KbgI9udTtnsEucsEvOJgzEGNISh.IatJtyWbi','', 0);
INSERT INTO users (login, nom, prenom, droits, hash, token, tokenlifecycle) VALUES ('Utilisateur6', 'Originel', 'Utilisateur', 'Utilisateur', '$2a$12$eEmBwtSF27lOZVL6KbgI9udTtnsEucsEvOJgzEGNISh.IatJtyWbi','', 0);
INSERT INTO users (login, nom, prenom, droits, hash, token, tokenlifecycle) VALUES ('Utilisateur7', 'Originel', 'Utilisateur', 'Utilisateur', '$2a$12$eEmBwtSF27lOZVL6KbgI9udTtnsEucsEvOJgzEGNISh.IatJtyWbi','', 0);
INSERT INTO users (login, nom, prenom, droits, hash, token, tokenlifecycle) VALUES ('Utilisateur8', 'Originel', 'Utilisateur', 'Utilisateur', '$2a$12$eEmBwtSF27lOZVL6KbgI9udTtnsEucsEvOJgzEGNISh.IatJtyWbi','', 0);
INSERT INTO users (login, nom, prenom, droits, hash, token, tokenlifecycle) VALUES ('Utilisateur9', 'Originel', 'Utilisateur', 'Utilisateur', '$2a$12$eEmBwtSF27lOZVL6KbgI9udTtnsEucsEvOJgzEGNISh.IatJtyWbi','', 0);
INSERT INTO users (login, nom, prenom, droits, hash, token, tokenlifecycle) VALUES ('Utilisateur10', 'Originel', 'Utilisateur', 'Utilisateur', '$2a$12$eEmBwtSF27lOZVL6KbgI9udTtnsEucsEvOJgzEGNISh.IatJtyWbi','', 0);
INSERT INTO users (login, nom, prenom, droits, hash, token, tokenlifecycle) VALUES ('Utilisateur11', 'Originel', 'Utilisateur', 'Utilisateur', '$2a$12$eEmBwtSF27lOZVL6KbgI9udTtnsEucsEvOJgzEGNISh.IatJtyWbi','', 0);
INSERT INTO users (login, nom, prenom, droits, hash, token, tokenlifecycle) VALUES ('Utilisateur12', 'Originel', 'Utilisateur', 'Utilisateur', '$2a$12$eEmBwtSF27lOZVL6KbgI9udTtnsEucsEvOJgzEGNISh.IatJtyWbi','', 0);
INSERT INTO users (login, nom, prenom, droits, hash, token, tokenlifecycle) VALUES ('Utilisateur13', 'Originel', 'Utilisateur', 'Utilisateur', '$2a$12$eEmBwtSF27lOZVL6KbgI9udTtnsEucsEvOJgzEGNISh.IatJtyWbi','', 0);
INSERT INTO users (login, nom, prenom, droits, hash, token, tokenlifecycle) VALUES ('Utilisateur14', 'Originel', 'Utilisateur', 'Utilisateur', '$2a$12$eEmBwtSF27lOZVL6KbgI9udTtnsEucsEvOJgzEGNISh.IatJtyWbi','', 0);
INSERT INTO users (login, nom, prenom, droits, hash, token, tokenlifecycle) VALUES ('Utilisateur15', 'Originel', 'Utilisateur', 'Utilisateur', '$2a$12$eEmBwtSF27lOZVL6KbgI9udTtnsEucsEvOJgzEGNISh.IatJtyWbi','', 0);
INSERT INTO users (login, nom, prenom, droits, hash, token, tokenlifecycle) VALUES ('Utilisateur16', 'Originel', 'Utilisateur', 'Utilisateur', '$2a$12$eEmBwtSF27lOZVL6KbgI9udTtnsEucsEvOJgzEGNISh.IatJtyWbi','', 0);
INSERT INTO users (login, nom, prenom, droits, hash, token, tokenlifecycle) VALUES ('Utilisateur17', 'Originel', 'Utilisateur', 'Utilisateur', '$2a$12$eEmBwtSF27lOZVL6KbgI9udTtnsEucsEvOJgzEGNISh.IatJtyWbi','', 0);
INSERT INTO users (login, nom, prenom, droits, hash, token, tokenlifecycle) VALUES ('Utilisateur18', 'Originel', 'Utilisateur', 'Utilisateur', '$2a$12$eEmBwtSF27lOZVL6KbgI9udTtnsEucsEvOJgzEGNISh.IatJtyWbi','', 0);
INSERT INTO users (login, nom, prenom, droits, hash, token, tokenlifecycle) VALUES ('Utilisateur19', 'Originel', 'Utilisateur', 'Utilisateur', '$2a$12$eEmBwtSF27lOZVL6KbgI9udTtnsEucsEvOJgzEGNISh.IatJtyWbi','', 0);



--Table contenant les informations sur les droits d'accès aux pages
CREATE TABLE web_pages_access (
    name text,
    droits text,
    redirect text
);



--Droits d'accès aux pages : 
-- name (ex : 'login.html')
-- droits : droit du type d'utilisateur => à faire pour chaque type d'utilisateur (ex : 'Admin' ou 'Utilisateur')
-- redirect : ou l'utilisateur doit être redirigé => 'none' : pas de redirection car il a les droits / 'nomPage.html'

-- login.html
INSERT INTO web_pages_access (name, droits, redirect) VALUES ('login.html', 'Admin', 'accueil.html');
INSERT INTO web_pages_access (name, droits, redirect) VALUES ('login.html', 'Utilisateur', 'accueil.html');

-- accueil.html
INSERT INTO web_pages_access (name, droits, redirect) VALUES ('accueil.html', 'Admin', 'none');
INSERT INTO web_pages_access (name, droits, redirect) VALUES ('accueil.html', 'Utilisateur', 'none');



--######################## BD test ########################
DROP DATABASE test;

--identique à sae_51 hormis qu'il n'y a pas de contenu dans les tables !!!

--Base de données test
CREATE DATABASE test;
\c test

-- contenu de la BD sae_51

--Table contenant les informations sur les utilisateurs
CREATE TABLE users (
    login text PRIMARY KEY,
    nom text,
    prenom text,
    droits text,
    hash text,
    token text,
    tokenlifecycle integer
);

-- fin contenu de la BD sae_51

--########################## FIN ##########################

--Msg fin
\echo
\echo
\echo Fait
--Auteur original : Maxime VALLET (SAE 52)
--Modifications : Maxime VALLET
--  => modification table users : expiration des tokens
--  => Ajout tables : web_pages_access / pc / notifications / servlet_access
--Version : 0.8


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
-- droits : droit du type d'utilisateur => à faire pour chaque type d'utilisateur (ex : 'Admin' ou 'Utilisateur' ou "Aucun" (si "Aucun" non présent => redirection vers login.html))
-- redirect : ou l'utilisateur doit être redirigé => 'none' : pas de redirection car il a les droits / 'nomPage.html'

-- TestsBackend.html
INSERT INTO web_pages_access (name, droits, redirect) VALUES ('TestsBackend.html', 'Admin', 'none');
INSERT INTO web_pages_access (name, droits, redirect) VALUES ('TestsBackend.html', 'Utilisateur', 'accueil.html');
INSERT INTO web_pages_access (name, droits, redirect) VALUES ('TestsBackend.html', 'Aucun', 'login.html');

-- help.html
INSERT INTO web_pages_access (name, droits, redirect) VALUES ('help.html', 'Admin', 'none');
INSERT INTO web_pages_access (name, droits, redirect) VALUES ('help.html', 'Utilisateur', 'none');
INSERT INTO web_pages_access (name, droits, redirect) VALUES ('help.html', 'Aucun', 'none');

-- login.html
INSERT INTO web_pages_access (name, droits, redirect) VALUES ('login.html', 'Admin', 'accueil.html');
INSERT INTO web_pages_access (name, droits, redirect) VALUES ('login.html', 'Utilisateur', 'accueil.html');
INSERT INTO web_pages_access (name, droits, redirect) VALUES ('login.html', 'Aucun', 'none');

-- accueil.html
INSERT INTO web_pages_access (name, droits, redirect) VALUES ('accueil.html', 'Admin', 'none');
INSERT INTO web_pages_access (name, droits, redirect) VALUES ('accueil.html', 'Utilisateur', 'none');
INSERT INTO web_pages_access (name, droits, redirect) VALUES ('accueil.html', 'Aucun', 'login.html');




--Table contenant les pc à monitorer
CREATE TABLE pc (
    id text PRIMARY KEY,
    ip text
);




--Table contenant les notifications de problèmes
-- id : identifiant du pc (table pc)
CREATE TABLE notifications (
    id text PRIMARY KEY,
    problem text,
    date DATE DEFAULT CURRENT_DATE
);




--Table contenant les droits d'accès des différents servlets
-- name : nom du servlet (ex : "CheckToken")
-- role : droits de l'utilisateur ("Admin" | "Utilisateur" | "Aucun")
-- access : droit d'accès ("true" | "false")
CREATE TABLE servlet_access (
    name text,
    role text,
    access text
);

--CheckToken (inutile)

--TestTomcat (inutile)

--CheckPassword (inutile)

--AddUser
INSERT INTO servlet_access (name, role, access) VALUES ('AddUser', 'Admin', 'true');
INSERT INTO servlet_access (name, role, access) VALUES ('AddUser', 'Utilisateur', 'false');
INSERT INTO servlet_access (name, role, access) VALUES ('AddUser', 'Aucun', 'false');

--DeleteUser
INSERT INTO servlet_access (name, role, access) VALUES ('DeleteUser', 'Admin', 'true');
INSERT INTO servlet_access (name, role, access) VALUES ('DeleteUser', 'Utilisateur', 'false');
INSERT INTO servlet_access (name, role, access) VALUES ('DeleteUser', 'Aucun', 'false');

--ListUsers
INSERT INTO servlet_access (name, role, access) VALUES ('ListUsers', 'Admin', 'true');
INSERT INTO servlet_access (name, role, access) VALUES ('ListUsers', 'Utilisateur', 'false');
INSERT INTO servlet_access (name, role, access) VALUES ('ListUsers', 'Aucun', 'false');

--SetPassword
INSERT INTO servlet_access (name, role, access) VALUES ('SetPassword', 'Admin', 'true');
INSERT INTO servlet_access (name, role, access) VALUES ('SetPassword', 'Utilisateur', 'true');
INSERT INTO servlet_access (name, role, access) VALUES ('SetPassword', 'Aucun', 'false');

--DeleteToken
INSERT INTO servlet_access (name, role, access) VALUES ('DeleteToken', 'Admin', 'true');
INSERT INTO servlet_access (name, role, access) VALUES ('DeleteToken', 'Utilisateur', 'true');
INSERT INTO servlet_access (name, role, access) VALUES ('DeleteToken', 'Aucun', 'false');

--GetRedirection
INSERT INTO servlet_access (name, role, access) VALUES ('GetRedirection', 'Admin', 'true');
INSERT INTO servlet_access (name, role, access) VALUES ('GetRedirection', 'Utilisateur', 'true');
INSERT INTO servlet_access (name, role, access) VALUES ('GetRedirection', 'Aucun', 'true');




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


--Table contenant les informations sur les droits d'accès aux pages
CREATE TABLE web_pages_access (
    name text,
    droits text,
    redirect text
);

INSERT INTO web_pages_access (name, droits, redirect) VALUES ('login.html', 'Admin', 'accueil.html');


--Table contenant les pc à monitorer
CREATE TABLE pc (
    id text PRIMARY KEY,
    ip text
);


--Table contenant les notifications de problèmes
-- id : identifiant du pc (table pc)
CREATE TABLE notifications (
    id text PRIMARY KEY,
    problem text,
    date DATE DEFAULT CURRENT_DATE
);


--Table contenant les droits d'accès des différents servlets
-- name : nom du servlet (ex : "CheckToken")
-- role : droits de l'utilisateur ("Admin" | "Utilisateur" | "Aucun")
-- access : droit d'accès ("true" | "false")
CREATE TABLE servlet_access (
    name text,
    role text,
    access text
);

--Test
INSERT INTO servlet_access (name, role, access) VALUES ('Test', 'Admin', 'true');
INSERT INTO servlet_access (name, role, access) VALUES ('Test', 'Utilisateur', 'false');
INSERT INTO servlet_access (name, role, access) VALUES ('Test', 'Aucun', 'false');


-- fin contenu de la BD sae_51

--########################## FIN ##########################

--Msg fin
\echo
\echo
\echo #####Fait#####
--Auteur original : Maxime VALLET (SAE 52)
--Modifications : Maxime VALLET
--  => modification table users : expiration des tokens
--  => Ajout tables : web_pages_access / pc / notifications / servlet_access
--Version : 1.0


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
--Les droits sont stockés de la façon suivant : "login1/.../loginN"
CREATE TABLE pc (
    id text PRIMARY KEY,
    ip text,
    droits text
);



--Table contenant les pc à monitorer
CREATE TABLE pc_static_info (
    id text PRIMARY KEY,
    cpu_model text,
    cores integer,
    threads integer,
    maximum_frequency text,
    ram_quantity text,
    dimm_quantity integer,
    dimm_speed text,
    storage_device_number integer,
    storage_space text,
    network_int_number integer,
    network_int_speed text,
    os text,
    version text
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
INSERT INTO servlet_access (name, role, access) VALUES ('DeleteToken', 'Aucun', 'true');

--GetRedirection
INSERT INTO servlet_access (name, role, access) VALUES ('GetRedirection', 'Admin', 'true');
INSERT INTO servlet_access (name, role, access) VALUES ('GetRedirection', 'Utilisateur', 'true');
INSERT INTO servlet_access (name, role, access) VALUES ('GetRedirection', 'Aucun', 'true');

--GetLogs
INSERT INTO servlet_access (name, role, access) VALUES ('GetLogs', 'Admin', 'true');
INSERT INTO servlet_access (name, role, access) VALUES ('GetLogs', 'Utilisateur', 'false');
INSERT INTO servlet_access (name, role, access) VALUES ('GetLogs', 'Aucun', 'false');

--DeletePC
INSERT INTO servlet_access (name, role, access) VALUES ('DeletePC', 'Admin', 'true');
INSERT INTO servlet_access (name, role, access) VALUES ('DeletePC', 'Utilisateur', 'false');
INSERT INTO servlet_access (name, role, access) VALUES ('DeletePC', 'Aucun', 'false');

--ListPC
INSERT INTO servlet_access (name, role, access) VALUES ('ListPC', 'Admin', 'true');
INSERT INTO servlet_access (name, role, access) VALUES ('ListPC', 'Utilisateur', 'true');
INSERT INTO servlet_access (name, role, access) VALUES ('ListPC', 'Aucun', 'false');

--ListPCStaticInfo
INSERT INTO servlet_access (name, role, access) VALUES ('ListPCStaticInfo', 'Admin', 'true');
INSERT INTO servlet_access (name, role, access) VALUES ('ListPCStaticInfo', 'Utilisateur', 'true');
INSERT INTO servlet_access (name, role, access) VALUES ('ListPCStaticInfo', 'Aucun', 'false');

--GetTokenStatus
INSERT INTO servlet_access (name, role, access) VALUES ('GetTokenStatus', 'Admin', 'true');
INSERT INTO servlet_access (name, role, access) VALUES ('GetTokenStatus', 'Utilisateur', 'true');
INSERT INTO servlet_access (name, role, access) VALUES ('GetTokenStatus', 'Aucun', 'true');

--GetCheckIntervall
INSERT INTO servlet_access (name, role, access) VALUES ('GetCheckIntervall', 'Admin', 'true');
INSERT INTO servlet_access (name, role, access) VALUES ('GetCheckIntervall', 'Utilisateur', 'true');
INSERT INTO servlet_access (name, role, access) VALUES ('GetCheckIntervall', 'Aucun', 'false');




--Table contenant les logs d'utilisation des servlets
CREATE TABLE logs (
    id SERIAL PRIMARY KEY,
    servlet text,
    ip text,
    login text,
    droits text,
    error text,
    date text
);







--#########################################################
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
    ip text,
    droits text
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



--Table contenant les logs d'utilisation des servlets
CREATE TABLE logs (
    id SERIAL PRIMARY KEY,
    servlet text,
    ip text,
    login text,
    droits text,
    error text,
    date text
);

--Test
INSERT INTO logs (servlet, ip, login, droits, error, date) VALUES ('Test', '1.1.1.1', 'Hell walker', 'Admin', 'none', '19931210_000001');
INSERT INTO logs (servlet, ip, login, droits, error, date) VALUES ('Test2', '2.2.2.2', 'Maxime', 'Admin', 'erreur', '20241124_012900');



--Table contenant les pc à monitorer
CREATE TABLE pc_static_info (
    id text PRIMARY KEY,
    cpu_model text,
    cores integer,
    threads integer,
    maximum_frequency text,
    ram_quantity text,
    dimm_quantity integer,
    dimm_speed text,
    storage_device_number integer,
    storage_space text,
    network_int_number integer,
    network_int_speed text,
    os text,
    version text
);

-- fin contenu de la BD sae_51

--########################## FIN ##########################
--#########################################################



--Msg fin
\echo
\echo
\echo #####Fait#####
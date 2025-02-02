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
-- !!! METTRE DANS L'ORDRE DANS LEQUEL LES PAGES APPARAISSENT DANS LA BARRE LATTERALLE (bouttons changement de page) !!!

-- login.html
INSERT INTO web_pages_access (name, droits, redirect) VALUES ('login.html', 'Admin', 'alerts.html');
INSERT INTO web_pages_access (name, droits, redirect) VALUES ('login.html', 'Utilisateur', 'alerts.html');
INSERT INTO web_pages_access (name, droits, redirect) VALUES ('login.html', 'Aucun', 'none');

-- alerts.html
INSERT INTO web_pages_access (name, droits, redirect) VALUES ('alerts.html', 'Admin', 'none');
INSERT INTO web_pages_access (name, droits, redirect) VALUES ('alerts.html', 'Utilisateur', 'none');
INSERT INTO web_pages_access (name, droits, redirect) VALUES ('alerts.html', 'Aucun', 'login.html');

-- control.html
INSERT INTO web_pages_access (name, droits, redirect) VALUES ('control.html', 'Admin', 'none');
INSERT INTO web_pages_access (name, droits, redirect) VALUES ('control.html', 'Utilisateur', 'none');
INSERT INTO web_pages_access (name, droits, redirect) VALUES ('control.html', 'Aucun', 'login.html');

-- monitoring.html
INSERT INTO web_pages_access (name, droits, redirect) VALUES ('monitoring.html', 'Admin', 'none');
INSERT INTO web_pages_access (name, droits, redirect) VALUES ('monitoring.html', 'Utilisateur', 'none');
INSERT INTO web_pages_access (name, droits, redirect) VALUES ('monitoring.html', 'Aucun', 'login.html');

-- users.html
INSERT INTO web_pages_access (name, droits, redirect) VALUES ('users.html', 'Admin', 'none');
INSERT INTO web_pages_access (name, droits, redirect) VALUES ('users.html', 'Utilisateur', 'ChangePassword.html');
INSERT INTO web_pages_access (name, droits, redirect) VALUES ('users.html', 'Aucun', 'login.html');

-- ChangePassword.html
INSERT INTO web_pages_access (name, droits, redirect) VALUES ('ChangePassword.html', 'Admin', 'users.html');
INSERT INTO web_pages_access (name, droits, redirect) VALUES ('ChangePassword.html', 'Utilisateur', 'none');
INSERT INTO web_pages_access (name, droits, redirect) VALUES ('ChangePassword.html', 'Aucun', 'login.html');

-- logs.html
INSERT INTO web_pages_access (name, droits, redirect) VALUES ('logs.html', 'Admin', 'none');
INSERT INTO web_pages_access (name, droits, redirect) VALUES ('logs.html', 'Utilisateur', 'alerts.html');
INSERT INTO web_pages_access (name, droits, redirect) VALUES ('logs.html', 'Aucun', 'login.html');

-- notifications.html
INSERT INTO web_pages_access (name, droits, redirect) VALUES ('notifications.html', 'Admin', 'none');
INSERT INTO web_pages_access (name, droits, redirect) VALUES ('notifications.html', 'Utilisateur', 'none');
INSERT INTO web_pages_access (name, droits, redirect) VALUES ('notifications.html', 'Aucun', 'login.html');

-- help.html (laisser en dernier)
INSERT INTO web_pages_access (name, droits, redirect) VALUES ('help.html', 'Admin', 'none');
INSERT INTO web_pages_access (name, droits, redirect) VALUES ('help.html', 'Utilisateur', 'none');
INSERT INTO web_pages_access (name, droits, redirect) VALUES ('help.html', 'Aucun', 'none');



--Table contenant les pc à monitorer
--Les droits sont stockés de la façon suivant : "login1/.../loginN"
CREATE TABLE pc (
    id integer PRIMARY KEY,
    ip text,
    droits text
);



--Table contenant les pc à monitorer
CREATE TABLE pc_static_info (
    id integer PRIMARY KEY,
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

--GetAccessiblePages
INSERT INTO servlet_access (name, role, access) VALUES ('GetAccessiblePages', 'Admin', 'true');
INSERT INTO servlet_access (name, role, access) VALUES ('GetAccessiblePages', 'Utilisateur', 'true');
INSERT INTO servlet_access (name, role, access) VALUES ('GetAccessiblePages', 'Aucun', 'true');

--GetNotifications
INSERT INTO servlet_access (name, role, access) VALUES ('GetNotifications', 'Admin', 'true');
INSERT INTO servlet_access (name, role, access) VALUES ('GetNotifications', 'Utilisateur', 'true');
INSERT INTO servlet_access (name, role, access) VALUES ('GetNotifications', 'Aucun', 'false');

--ListUsersWithAccess
INSERT INTO servlet_access (name, role, access) VALUES ('ListUsersWithAccess', 'Admin', 'true');
INSERT INTO servlet_access (name, role, access) VALUES ('ListUsersWithAccess', 'Utilisateur', 'false');
INSERT INTO servlet_access (name, role, access) VALUES ('ListUsersWithAccess', 'Aucun', 'false');

--AddUserToPC
INSERT INTO servlet_access (name, role, access) VALUES ('AddUserToPC', 'Admin', 'true');
INSERT INTO servlet_access (name, role, access) VALUES ('AddUserToPC', 'Utilisateur', 'false');
INSERT INTO servlet_access (name, role, access) VALUES ('AddUserToPC', 'Aucun', 'false');

--DeleteUserFromPC
INSERT INTO servlet_access (name, role, access) VALUES ('DeleteUserFromPC', 'Admin', 'true');
INSERT INTO servlet_access (name, role, access) VALUES ('DeleteUserFromPC', 'Utilisateur', 'false');
INSERT INTO servlet_access (name, role, access) VALUES ('DeleteUserFromPC', 'Aucun', 'false');

--ListPCStatus
INSERT INTO servlet_access (name, role, access) VALUES ('ListPCStatus', 'Admin', 'true');
INSERT INTO servlet_access (name, role, access) VALUES ('ListPCStatus', 'Utilisateur', 'true');
INSERT INTO servlet_access (name, role, access) VALUES ('ListPCStatus', 'Aucun', 'false');

--ChangePCState
INSERT INTO servlet_access (name, role, access) VALUES ('ChangePCState', 'Admin', 'true');
INSERT INTO servlet_access (name, role, access) VALUES ('ChangePCState', 'Utilisateur', 'true');
INSERT INTO servlet_access (name, role, access) VALUES ('ChangePCState', 'Aucun', 'false');

--IsDynamicInfoUpToDate
INSERT INTO servlet_access (name, role, access) VALUES ('IsDynamicInfoUpToDate', 'Admin', 'true');
INSERT INTO servlet_access (name, role, access) VALUES ('IsDynamicInfoUpToDate', 'Utilisateur', 'true');
INSERT INTO servlet_access (name, role, access) VALUES ('IsDynamicInfoUpToDate', 'Aucun', 'false');

--ListPCDynInfo
INSERT INTO servlet_access (name, role, access) VALUES ('ListPCDynInfo', 'Admin', 'true');
INSERT INTO servlet_access (name, role, access) VALUES ('ListPCDynInfo', 'Utilisateur', 'true');
INSERT INTO servlet_access (name, role, access) VALUES ('ListPCDynInfo', 'Aucun', 'false');



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



--Table contenant les notifications à aficher à l'utilisateur
CREATE TABLE notification (
    description text,
    content text,
    users text,
    date text
);



--Table contenant le status des PC
CREATE TABLE pc_status (
    id integer,
    status text
);



--Table contenant les messages à envoyer aux pc
CREATE TABLE pc_messages (
    id integer,
    message text
);


--ATTENTION : les données qui peuvent être sous forme de liste sont au format texte (ex : stockage)
CREATE TABLE pc_dynamic_info (
    id integer PRIMARY KEY,
    date text,
    time text,
    cpu_utilization integer,
    cpu_temp integer,
    cpu_consumption integer,
    ram_utilization integer,
    storage_name text,
    storage_load text,
    storage_left text,
    storage_temp text,
    storage_errors text,
    network_name text,
    network_latency text,
    network_bandwith text,
    fan_speed text
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
INSERT INTO web_pages_access (name, droits, redirect) VALUES ('accueil.html', 'Admin', 'none');


--Table contenant les pc à monitorer
CREATE TABLE pc (
    id integer PRIMARY KEY,
    ip text,
    droits text
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

--GetAccessiblePages
INSERT INTO servlet_access (name, role, access) VALUES ('GetAccessiblePages', 'Admin', 'true');
INSERT INTO servlet_access (name, role, access) VALUES ('GetAccessiblePages', 'Utilisateur', 'true');
INSERT INTO servlet_access (name, role, access) VALUES ('GetAccessiblePages', 'Aucun', 'false');

--GetNotifications
INSERT INTO servlet_access (name, role, access) VALUES ('GetNotifications', 'Admin', 'true');
INSERT INTO servlet_access (name, role, access) VALUES ('GetNotifications', 'Utilisateur', 'true');
INSERT INTO servlet_access (name, role, access) VALUES ('GetNotifications', 'Aucun', 'false');



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
    id integer PRIMARY KEY,
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


--Table contenant les notifications à aficher à l'utilisateur
CREATE TABLE notification (
    description text,
    content text,
    users text,
    date text
);


CREATE TABLE pc_dynamic_info (
    id integer PRIMARY KEY,
    date text,
    time text
);




-- fin contenu de la BD sae_51

--########################## FIN ##########################
--#########################################################



--Msg fin
\echo
\echo
\echo #####Fait#####
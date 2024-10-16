--Auteur original : Maxime VALLET (SAE 52)
--Modifications : Maxime VALLET
--Version : 0.1

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
    role text,
    hash text,
    token text
);


--MDP hashés avec Bcrypt (12 passes)
--Compte admin par défaut (MDP "Admin")
INSERT INTO users (login, nom, prenom, role, hash, token) VALUES ('Admin1', 'Originel', 'Admin', 'Admin', '$2a$12$7bJpdP/8n6Yn.2MdqQoSUO/wEAlzkYpySZGr5NShaDUjfLx3uyUAC','');
--Compte admin par défaut (MDP "Technicien")
INSERT INTO users (login, nom, prenom, role, hash, token) VALUES ('Technicien1', 'Originel', 'Technicien', 'Technicien', '$2a$12$qBymfjA8iwr45xmhyMdwWO9Ax1Kzl5EAjqwioXJHGKI3AKLxsSkiW','');
--Compte admin par défaut (MDP "Utilisateur")
INSERT INTO users (login, nom, prenom, role, hash, token) VALUES ('Utilisateur1', 'Originel', 'Utilisateur', 'Utilisateur', '$2a$12$eEmBwtSF27lOZVL6KbgI9udTtnsEucsEvOJgzEGNISh.IatJtyWbi','');



--######################## BD test ########################
DROP DATABASE test;

--identique à sae_52 hormis qu'il n'y a pas de contenu dans les tables !!!

--Base de données test
CREATE DATABASE test;
\c test

-- contenu de la BD sae_51

-- fin contenu de la BD sae_51

--########################## FIN ##########################

--Msg fin
\echo
\echo
\echo Fait
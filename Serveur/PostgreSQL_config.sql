--Auteur original : Maxime VALLET (SAE 52)
--Modifications : Maxime VALLET
--Version : 0.1

-- à modifier
-- type hash

--####################### BD sae_51 #######################

--Base de données du projet
CREATE DATABASE sae_51;
\c sae_51

--Table contenant les informations sur les utilisateurs (MDP hash : MD5)
CREATE TABLE users (
    login text PRIMARY KEY,
    nom text,
    prenom text,
    role text,
    hash text,
    token text
);

--Compte admin par défaut (MDP "Admin")
INSERT INTO users (login, nom, prenom, role, hash, token) VALUES ('Admin1', 'Originel', 'Admin', 'Admin', 'e3afed0047b08059d0fada10f400c1e5','');
--Compte admin par défaut (MDP "Technicien")
INSERT INTO users (login, nom, prenom, role, hash, token) VALUES ('Technicien1', 'Originel', 'Technicien', 'Technicien', '61c42e9e5647205c90235b3361be8ad7','');
--Compte admin par défaut (MDP "Utilisateur")
INSERT INTO users (login, nom, prenom, role, hash, token) VALUES ('Utilisateur1', 'Originel', 'Utilisateur', 'Utilisateur', 'f628ae7eac054bc61babf042677832ee','');



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
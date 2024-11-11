package DAO;

import Tests.DAOTest;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Maxime VALLET
 * @version 0.7
 */
public class DAOusersTest {
    DAOusers DAO = new DAOusers();
    DAOTest DAOtest = new DAOTest();
    
    
    /**
     * Test of getUserPasswordHash method, of class DAOusers.
     */
    @Test
    public void testGetUserPasswordHash() {
        
        //Ajout d'un utilisateur ayant les droits d'admin
        String login = "Admin1";
        String nom = "Admin";
        String prenom = "Originel";
        String role = "Admin";
        String hashedPassword = "$2a$12$l3MjhFmfr7VGoL0uPX2VKuEmXxboZzyABhjVNqH9.TnrCD2hEvfmm"; // MDP = "leffe"
        Integer tokenLifeCycle = 999;
        Boolean Test = true;
        String token = "$2a$08$VOtVbubOhyXhjEffToT.n.F9d8t9kwY0ulySEMKKoGZDisi4ni1s."; //token : "10101010101010101010101010101010"
        DAOtest.addUserWithToken(login, nom, prenom, role, hashedPassword, tokenLifeCycle, token, Test);
        
        //Récupération du MDP hashé
        String result = DAO.getUserPasswordHash(login, Test);
        
        //Suppression de l'utilisateur
        DAO.deleteUser(login, Test);
        
        //Résultat attendu
        String ExpResult = hashedPassword;
        
        //Résultat
        System.out.println("resultat testGetUserPasswordHash : "+result+" | exp : "+ExpResult);
        
        //Vérification du résultat
        assertEquals(ExpResult, result);
    }

    /**
     * Test of getUserRightsFromLogin method, of class DAOusers.
     */
    @Test
    public void testGetUserRightsFromLogin() {
        
        //Ajout d'un utilisateur ayant les droits d'admin
        String login = "Admin1";
        String nom = "Admin";
        String prenom = "Originel";
        String role = "Admin";
        String hashedPassword = "$2a$12$l3MjhFmfr7VGoL0uPX2VKuEmXxboZzyABhjVNqH9.TnrCD2hEvfmm"; // MDP = "leffe"
        Integer tokenLifeCycle = 999;
        String token = "$2a$08$VOtVbubOhyXhjEffToT.n.F9d8t9kwY0ulySEMKKoGZDisi4ni1s."; //token : "10101010101010101010101010101010"
        Boolean Test = true;
        DAOtest.addUserWithToken(login, nom, prenom, role, hashedPassword, tokenLifeCycle, token, Test);
        
        //Récupération des droits de l'utilisateur
        String result = DAO.getUserRightsFromLogin(login, Test);
        
        //Suppression de l'utilisateur
        DAO.deleteUser(login, Test);
        
        //Résultat attendu
        String ExpResult = role;
        
        //Résultat
        System.out.println("resultat testGetUserRightsFromLogin : "+result+" | exp : "+ExpResult);
        
        //Vérification du résultat
        assertEquals(ExpResult, result);
    }

    /**
     * Test of getUserRightsFromToken method, of class DAOusers.
     */
    @Test
    public void testGetUserRightsFromToken() {
        
        //Ajout d'un utilisateur ayant les droits d'admin
        String login = "Admin1";
        String nom = "Admin";
        String prenom = "Originel";
        String role = "Admin";
        String hashedPassword = "$2a$12$l3MjhFmfr7VGoL0uPX2VKuEmXxboZzyABhjVNqH9.TnrCD2hEvfmm"; // MDP = "leffe"
        Integer tokenLifeCycle = 999;
        String token = "$2a$08$VOtVbubOhyXhjEffToT.n.F9d8t9kwY0ulySEMKKoGZDisi4ni1s."; //token : "10101010101010101010101010101010"
        Boolean Test = true;
        DAOtest.addUserWithToken(login, nom, prenom, role, hashedPassword, tokenLifeCycle, token, Test);
        
        //Récupération des droits de l'utilisateur
        String result = DAO.getUserRightsFromToken("10101010101010101010101010101010", Test);
        
        //Suppression de l'utilisateur
        DAO.deleteUser(login, Test);
        
        //Résultat attendu
        String ExpResult = role;
        
        //Résultat
        System.out.println("resultat testGetUserRightsFromToken : "+result+" | exp : "+ExpResult);
        
        //Vérification du résultat
        assertEquals(ExpResult, result);
    }

    /**
     * Test of setToken method, of class DAOusers.
     */
    @Test
    public void testSetToken() {
        //Ajout d'un utilisateur ayant les droits d'admin
        String login = "Admin1";
        String nom = "Admin";
        String prenom = "Originel";
        String role = "Admin";
        String hashedPassword = "$2a$12$l3MjhFmfr7VGoL0uPX2VKuEmXxboZzyABhjVNqH9.TnrCD2hEvfmm"; // MDP = "leffe"
        Integer tokenLifeCycle = 999;
        String token = "$2a$08$VOtVbubOhyXhjEffToT.n.F9d8t9kwY0ulySEMKKoGZDisi4ni1s."; //token : "10101010101010101010101010101010"
        Boolean Test = true;
        DAOtest.addUserWithToken(login, nom, prenom, role, hashedPassword, tokenLifeCycle, "", Test);
        
        //Définition du token de l'utilisateur
        DAO.setToken(token, login, tokenLifeCycle, Test);
        
        //Récupération du token de l'utilisateur
        String result = DAO.getToken(login, Test);
        
        //Suppression de l'utilisateur
        DAO.deleteUser(login, Test);
        
        //Résultat attendu
        String ExpResult = token;
        
        //Résultat
        System.out.println("resultat testSetToken : "+result+" | exp : "+ExpResult);
        
        //Vérification du résultat
        assertEquals(ExpResult, result);
    }

    /**
     * Test of getUsers method, of class DAOusers.
     */
    @Test
    public void testGetUsers() {
        
        //Ajout d'un utilisateur ayant les droits d'admin
        String login = "Admin1";
        String nom = "Admin";
        String prenom = "Originel";
        String role = "Admin";
        String hashedPassword = "$2a$12$l3MjhFmfr7VGoL0uPX2VKuEmXxboZzyABhjVNqH9.TnrCD2hEvfmm"; // MDP = "leffe"
        Integer tokenLifeCycle = 999;
        String token = "$2a$08$VOtVbubOhyXhjEffToT.n.F9d8t9kwY0ulySEMKKoGZDisi4ni1s."; //token : "10101010101010101010101010101010"
        Boolean Test = true;
        DAOtest.addUserWithToken(login, nom, prenom, role, hashedPassword, tokenLifeCycle, token, Test);
        
        //Récupération des utilisateurs
        String result = DAO.getUsers(Test);
        
        //Suppression de l'utilisateur
        DAO.deleteUser(login, Test);
        
        //Résultat attendu
        String ExpResult = "[{\"login\":\"Admin1\", \"prenom\":\"Originel\", \"nom\":\"Admin\", \"droits\":\"Admin\"}]";
        
        //Résultat
        System.out.println("resultat testGetUsers : "+result+" | exp : "+ExpResult);
        
        //Vérification du résultat
        assertEquals(ExpResult, result);
    }

    /**
     * Test of getTokenLifeCycle method, of class DAOusers.
     */
    @Test
    public void testGetTokenLifeCycle() {
        
        //Ajout d'un utilisateur ayant les droits d'admin
        String login = "Admin1";
        String nom = "Admin";
        String prenom = "Originel";
        String role = "Admin";
        String hashedPassword = "$2a$12$l3MjhFmfr7VGoL0uPX2VKuEmXxboZzyABhjVNqH9.TnrCD2hEvfmm"; // MDP = "leffe"
        Integer tokenLifeCycle = 999;
        Boolean Test = true;
        String token = "$2a$08$VOtVbubOhyXhjEffToT.n.F9d8t9kwY0ulySEMKKoGZDisi4ni1s."; //token : "10101010101010101010101010101010"
        DAOtest.addUserWithToken(login, nom, prenom, role, hashedPassword, tokenLifeCycle, token, Test);
        
        //Récupération de la durée de vie du token
        Integer result = DAO.getTokenLifeCycle(login, Test);
        
        //Suppression de l'utilisateur
        DAO.deleteUser(login, Test);
        
        //Résultat attendu
        Integer ExpResult = tokenLifeCycle;
        
        //Résultat
        System.out.println("resultat testGetTokenLifeCycle : "+result+" | exp : "+ExpResult);
        
        //Vérification du résultat
        assertEquals(ExpResult, result);
    }

    /**
     * Test of deleteToken method, of class DAOusers.
     */
    @Test
    public void testDeleteToken() {
        
        //Ajout d'un utilisateur ayant les droits d'admin
        String login = "Admin1";
        String nom = "Admin";
        String prenom = "Originel";
        String role = "Admin";
        String hashedPassword = "$2a$12$l3MjhFmfr7VGoL0uPX2VKuEmXxboZzyABhjVNqH9.TnrCD2hEvfmm"; // MDP = "leffe"
        Integer tokenLifeCycle = 999;
        String token = "$2a$08$VOtVbubOhyXhjEffToT.n.F9d8t9kwY0ulySEMKKoGZDisi4ni1s."; //token : "10101010101010101010101010101010"
        Boolean Test = true;
        DAOtest.addUserWithToken(login, nom, prenom, role, hashedPassword, tokenLifeCycle, token, Test);
        
        //Suppression du token de l'utilisateur
        DAO.deleteToken(login, Test);
        
        //Récupération du token de l'utilisateur
        String result = DAO.getToken(login, Test);
        
        //Suppression de l'utilisateur
        DAO.deleteUser(login, Test);
        
        //Résultat attendu
        String ExpResult = "";
        
        //Résultat
        System.out.println("resultat testDeleteToken : "+result+" | exp : "+ExpResult);
        
        //Vérification du résultat
        assertEquals(ExpResult, result);
    }

    /**
     * Test of getToken method, of class DAOusers.
     */
    @Test
    public void testGetToken() {
        
        //Ajout d'un utilisateur ayant les droits d'admin
        String login = "Admin1";
        String nom = "Admin";
        String prenom = "Originel";
        String role = "Admin";
        String hashedPassword = "$2a$12$l3MjhFmfr7VGoL0uPX2VKuEmXxboZzyABhjVNqH9.TnrCD2hEvfmm"; // MDP = "leffe"
        Integer tokenLifeCycle = 999;
        Boolean Test = true;
        String token = "$2a$08$VOtVbubOhyXhjEffToT.n.F9d8t9kwY0ulySEMKKoGZDisi4ni1s."; //token : "10101010101010101010101010101010"
        DAOtest.addUserWithToken(login, nom, prenom, role, hashedPassword, tokenLifeCycle, token, Test);
        
        //Récupération du token hashé
        String result = DAO.getToken(login, Test);
        
        //Suppression de l'utilisateur
        DAO.deleteUser(login, Test);
        
        //Résultat attendu
        String ExpResult = token;
        
        //Résultat
        System.out.println("resultat testGetToken : "+result+" | exp : "+ExpResult);
        
        //Vérification du résultat
        assertEquals(ExpResult, result);
    }

    /**
     * Test of setLifeCycle method, of class DAOusers.
     */
    @Test
    public void testSetLifeCycle() {
        
        //Ajout d'un utilisateur ayant les droits d'admin
        String login = "Admin1";
        String nom = "Admin";
        String prenom = "Originel";
        String role = "Admin";
        String hashedPassword = "$2a$12$l3MjhFmfr7VGoL0uPX2VKuEmXxboZzyABhjVNqH9.TnrCD2hEvfmm"; // MDP = "leffe"
        Integer tokenLifeCycle = 999;
        Boolean Test = true;
        String token = "$2a$08$VOtVbubOhyXhjEffToT.n.F9d8t9kwY0ulySEMKKoGZDisi4ni1s."; //token : "10101010101010101010101010101010"
        DAOtest.addUserWithToken(login, nom, prenom, role, hashedPassword, tokenLifeCycle, token, Test);
        
        //Récupération de la durée de vie du token
        Integer result = DAO.getTokenLifeCycle(login, Test);
        
        //Suppression de l'utilisateur
        DAO.deleteUser(login, Test);
        
        //Résultat attendu
        Integer ExpResult = tokenLifeCycle;
        
        //Résultat
        System.out.println("resultat testGetTokenLifeCycle : "+result+" | exp : "+ExpResult);
        
        //Vérification du résultat
        assertEquals(ExpResult, result);
    }

    /**
     * Test of checkToken method, of class DAOusers.
     */
    @Test
    public void testCheckToken1() {
        
        //Ajout d'un utilisateur ayant les droits d'admin
        String login = "Admin1";
        String nom = "Admin";
        String prenom = "Originel";
        String role = "Admin";
        String hashedPassword = "$2a$12$l3MjhFmfr7VGoL0uPX2VKuEmXxboZzyABhjVNqH9.TnrCD2hEvfmm"; // MDP = "leffe"
        Integer tokenLifeCycle = 999;
        String token = "$2a$08$VOtVbubOhyXhjEffToT.n.F9d8t9kwY0ulySEMKKoGZDisi4ni1s."; //token : "10101010101010101010101010101010"
        Boolean Test = true;
        DAOtest.addUserWithToken(login, nom, prenom, role, hashedPassword, tokenLifeCycle, token, Test);
        
        //Récupération du des infos de l'utilisateur
        String result = DAO.checkToken("10101010101010101010101010101010", Test);
        
        //Suppression de l'utilisateur
        DAO.deleteUser(login, Test);
        
        //Résultat attendu
        String ExpResult = "{\"login\":\"" + login + "\", \"droits\":\"" + role + "\", \"erreur\":\"none\"}";
        
        //Résultat
        System.out.println("resultat testCheckToken2 : "+result+" | exp : "+ExpResult);
        
        //Vérification du résultat
        assertEquals(ExpResult, result);
    }
    
    /**
     * Test of checkToken method, of class DAOusers.
     */
    @Test
    public void testCheckToken2() {
        
        //Ajout d'un utilisateur ayant les droits d'admin
        String login = "Admin1";
        String nom = "Admin";
        String prenom = "Originel";
        String role = "Admin";
        String hashedPassword = "$2a$12$l3MjhFmfr7VGoL0uPX2VKuEmXxboZzyABhjVNqH9.TnrCD2hEvfmm"; // MDP = "leffe"
        Integer tokenLifeCycle = 999;
        String token = "$2a$08$VOtVbubOhyXhjEffToT.n.F9d8t9kwY0ulySEMKKoGZDisi4ni1s."; //token : "10101010101010101010101010101010"
        Boolean Test = true;
        DAOtest.addUserWithToken(login, nom, prenom, role, hashedPassword, tokenLifeCycle, token, Test);
        
        //Récupération du des infos de l'utilisateur
        String result = DAO.checkToken("01010101010101010101010101010101", Test); //mauvais token
        
        //Suppression de l'utilisateur
        DAO.deleteUser(login, Test);
        
        //Résultat attendu
        String ExpResult = "{\"erreur\":\"pas de token (DB)\"}";
        
        //Résultat
        System.out.println("resultat testCheckToken2 : "+result+" | exp : "+ExpResult);
        
        //Vérification du résultat
        assertEquals(ExpResult, result);
    }

    /**
     * Test of deleteUser method, of class DAOusers.
     */
    @Test
    public void testDeleteUser() {
        
        //Ajout d'un utilisateur ayant les droits d'admin
        String login = "Admin1";
        String nom = "Admin";
        String prenom = "Originel";
        String role = "Admin";
        String hashedPassword = "$2a$12$l3MjhFmfr7VGoL0uPX2VKuEmXxboZzyABhjVNqH9.TnrCD2hEvfmm"; // MDP = "leffe"
        Integer tokenLifeCycle = 999;
        String token = "$2a$08$VOtVbubOhyXhjEffToT.n.F9d8t9kwY0ulySEMKKoGZDisi4ni1s."; //token : "10101010101010101010101010101010"
        Boolean Test = true;
        DAOtest.addUserWithToken(login, nom, prenom, role, hashedPassword, tokenLifeCycle, token, Test);
        
        //Suppression de l'utilisateur
        DAO.deleteUser(login, Test);
        
        //Listage de l'utilisateur
        String result = DAO.getUsers(Test);
        
        //Résultat attendu
        String ExpResult = "[]";
        
        //Résultat
        System.out.println("resultat testDeleteUser : "+result+" | exp : "+ExpResult);
        
        //Vérification du résultat
        assertEquals(ExpResult, result);
    }

    /**
     * Test of getActiveUsers method, of class DAOusers.
     */
    @Test
    public void testGetActiveUsers() {
        //Ajout d'un utilisateur ayant les droits d'admin
        String login = "Admin1";
        String nom = "Admin";
        String prenom = "Originel";
        String role = "Admin";
        String hashedPassword = "$2a$12$l3MjhFmfr7VGoL0uPX2VKuEmXxboZzyABhjVNqH9.TnrCD2hEvfmm"; // MDP = "leffe"
        Integer tokenLifeCycle = 0;
        String token = ""; //token vide car lifecycle expiré
        Boolean Test = true;
        DAOtest.addUserWithToken(login, nom, prenom, role, hashedPassword, tokenLifeCycle, token, Test);
        
        //Ajout d'un utilisateur ayant les droits d'admin
        login = "Admin2";
        nom = "Admin";
        prenom = "Originel";
        role = "Admin";
        hashedPassword = "$2a$12$l3MjhFmfr7VGoL0uPX2VKuEmXxboZzyABhjVNqH9.TnrCD2hEvfmm"; // MDP = "leffe"
        tokenLifeCycle = 999;
        token = "$2a$12$tMlZWS3Zd/XR7vW/xRYpGOPemuc0Plu6RuyJivFf5o/S.I/0Z4.Py"; //token : "01010101010101010101010101010101"
        DAOtest.addUserWithToken(login, nom, prenom, role, hashedPassword, tokenLifeCycle, token, Test);
        
        //Listage des utilisateurs actifs
        String result = DAO.getActiveUsers(Test);
        
        //Résultat attendu
        String ExpResult = "[{\"login\":\"Admin2\", \"prenom\":\"Originel\", \"nom\":\"Admin\", \"droits\":\"Admin\"}]";
        
        //Suppression des utilisateurs
        DAO.deleteUser("Admin1", Test);
        DAO.deleteUser(login, Test);
        
        //Résultat
        System.out.println("resultat testGetActiveUsers : "+result+" | exp : "+ExpResult);
        
        //Vérification du résultat
        assertEquals(ExpResult, result);
    }

    /**
     * Test of doLoginExist method, of class DAOusers.
     */
    @Test
    public void testDoLoginExist1() {
        
        //Ajout d'un utilisateur ayant les droits d'admin
        String login = "Admin1";
        String nom = "Admin";
        String prenom = "Originel";
        String role = "Admin";
        String hashedPassword = "$2a$12$l3MjhFmfr7VGoL0uPX2VKuEmXxboZzyABhjVNqH9.TnrCD2hEvfmm"; // MDP = "leffe"
        Integer tokenLifeCycle = 999;
        String token = "$2a$08$VOtVbubOhyXhjEffToT.n.F9d8t9kwY0ulySEMKKoGZDisi4ni1s."; //token : "10101010101010101010101010101010"
        Boolean Test = true;
        DAOtest.addUserWithToken(login, nom, prenom, role, hashedPassword, tokenLifeCycle, token, Test);
        
        //Vérification de l'existance du login dans la BD
        Boolean result = DAO.doLoginExist(login, Test);
        
        //Suppression de l'utilisateur
        DAO.deleteUser(login, Test);
        
        //Résultat attendu
        Boolean ExpResult = true;
        
        //Résultat
        System.out.println("resultat testDoLoginExist1 : "+result+" | exp : "+ExpResult);
        
        //Vérification du résultat
        assertEquals(ExpResult, result);
    }
    
    /**
     * Test of doLoginExist method, of class DAOusers.
     */
    @Test
    public void testDoLoginExist2() {
        
        //Ajout d'un utilisateur ayant les droits d'admin
        String login = "Admin1";
        String nom = "Admin";
        String prenom = "Originel";
        String role = "Admin";
        String hashedPassword = "$2a$12$l3MjhFmfr7VGoL0uPX2VKuEmXxboZzyABhjVNqH9.TnrCD2hEvfmm"; // MDP = "leffe"
        Integer tokenLifeCycle = 999;
        String token = "$2a$08$VOtVbubOhyXhjEffToT.n.F9d8t9kwY0ulySEMKKoGZDisi4ni1s."; //token : "10101010101010101010101010101010"
        Boolean Test = true;
        DAOtest.addUserWithToken(login, nom, prenom, role, hashedPassword, tokenLifeCycle, token, Test);
        
        //Vérification de l'existance du login dans la BD
        Boolean result = DAO.doLoginExist("Jeff", Test);
        
        //Suppression de l'utilisateur
        DAO.deleteUser(login, Test);
        
        //Résultat attendu
        Boolean ExpResult = false;
        
        //Résultat
        System.out.println("resultat testDoLoginExist2 : "+result+" | exp : "+ExpResult);
        
        //Vérification du résultat
        assertEquals(ExpResult, result);
    }

    /**
     * Test of addUser method, of class DAOusers.
     */
    @Test
    public void testAddUser() {
        
        //Ajout d'un utilisateur ayant les droits d'admin
        String login = "Admin1";
        String nom = "Admin";
        String prenom = "Originel";
        String role = "Admin";
        String hashedPassword = "$2a$12$l3MjhFmfr7VGoL0uPX2VKuEmXxboZzyABhjVNqH9.TnrCD2hEvfmm"; // MDP = "leffe"
        Integer tokenLifeCycle = 999;
        String token = "$2a$08$VOtVbubOhyXhjEffToT.n.F9d8t9kwY0ulySEMKKoGZDisi4ni1s."; //token : "10101010101010101010101010101010"
        Boolean Test = true;
        DAO.addUser(login, nom, prenom, role, hashedPassword, Test);
        
        //Vérification de l'existance de l'utilisateur dans la BD
        String result = DAO.getUsers(Test);
        
        //Suppression de l'utilisateur
        DAO.deleteUser(login, Test);
        
        //Résultat attendu
        String ExpResult = "[{\"login\":\"Admin1\", \"prenom\":\"Originel\", \"nom\":\"Admin\", \"droits\":\"Admin\"}]";
        
        //Résultat
        System.out.println("resultat testAddUser : "+result+" | exp : "+ExpResult);
        
        //Vérification du résultat
        assertEquals(ExpResult, result);
    }

    /**
     * Test of getRedirection method, of class DAOusers.
     */
    @Test
    public void testGetRedirection1() {
        
        //Vérification de l'existance de l'utilisateur dans la BD
        String result = DAO.getRedirection("Admin", "accueil.html", false); //pas de modif donc pas besoin de dupliquer le contenu de la table
        
        //Résultat attendu
        String ExpResult = "none";
        
        //Résultat
        System.out.println("resultat testGetRedirection1 : "+result+" | exp : "+ExpResult);
        
        //Vérification du résultat
        assertEquals(ExpResult, result);
    }
    
    /**
     * Test of getRedirection method, of class DAOusers.
     */
    @Test
    public void testGetRedirection2() {
        
        //Vérification de l'existance de l'utilisateur dans la BD
        String result = DAO.getRedirection("Admin", "inexistant.html", false); //pas de modif donc pas besoin de dupliquer le contenu de la table
        
        //Résultat attendu
        String ExpResult = "No redirect";
        
        //Résultat
        System.out.println("resultat testGetRedirection2 : "+result+" | exp : "+ExpResult);
        
        //Vérification du résultat
        assertEquals(ExpResult, result);
    }
    
    /**
     * Test of setPassword method, of class DAOusers.
     */
    @Test
    public void testSetPassword() {
        
        //Ajout d'un utilisateur ayant les droits d'admin
        String login = "Admin1";
        String nom = "Admin";
        String prenom = "Originel";
        String role = "Admin";
        String hashedPassword = "$2a$12$l3MjhFmfr7VGoL0uPX2VKuEmXxboZzyABhjVNqH9.TnrCD2hEvfmm"; // MDP = "leffe"
        Integer tokenLifeCycle = 999;
        String token = "$2a$08$VOtVbubOhyXhjEffToT.n.F9d8t9kwY0ulySEMKKoGZDisi4ni1s."; //token : "10101010101010101010101010101010"
        Boolean Test = true;
        DAO.addUser(login, nom, prenom, role, hashedPassword, Test);
        
        //Modification du MDP
        hashedPassword = "$2a$12$SoV5pJfWez6/QL/ohIEHzOXjH/Ydo3ewEFMtINv.7CaCSR/qqimtC"; //MPD : heinenken
        DAO.setPassword(login, hashedPassword, Test);
        
        //Vérification du MDP dans la BD
        String result = DAOtest.getPassword(login, Test);
        
        //Suppression de l'utilisateur
        DAO.deleteUser(login, Test);
        
        //Résultat attendu
        String ExpResult = hashedPassword;
        
        //Résultat
        System.out.println("resultat testSetPassword : "+result+" | exp : "+ExpResult);
        
        //Vérification du résultat
        assertEquals(ExpResult, result);
    }
    
    /**
     * Test of getUserLoginFromToken method, of class DAOusers.
     */
    @Test
    public void testGetUserLoginFromToken() {
        
        //Ajout d'un utilisateur ayant les droits d'admin
        String login = "Admin1";
        String nom = "Admin";
        String prenom = "Originel";
        String role = "Admin";
        String hashedPassword = "$2a$12$l3MjhFmfr7VGoL0uPX2VKuEmXxboZzyABhjVNqH9.TnrCD2hEvfmm"; // MDP = "leffe"
        Integer tokenLifeCycle = 999;
        String token = "$2a$08$VOtVbubOhyXhjEffToT.n.F9d8t9kwY0ulySEMKKoGZDisi4ni1s."; //token : "10101010101010101010101010101010"
        Boolean Test = true;
        DAOtest.addUserWithToken(login, nom, prenom, role, hashedPassword, tokenLifeCycle, token, Test);
        
        //Récupération des droits de l'utilisateur
        String result = DAO.getUserLoginFromToken("10101010101010101010101010101010", Test);
        
        //Suppression de l'utilisateur
        DAO.deleteUser(login, Test);
        
        //Résultat attendu
        String ExpResult = login;
        
        //Résultat
        System.out.println("resultat testGetUserLoginFromToken : "+result+" | exp : "+ExpResult);
        
        //Vérification du résultat
        assertEquals(ExpResult, result);
    }
    
    /**
     * Test of getServletRights method, of class DAOusers.
     */
    @Test
    public void testGetServletRights1() {
        
        //Récupération des droits de l'utilisateur
        String result = DAO.getServletRights("ListUsers", "Admin", false);
        
        //Résultat attendu
        String ExpResult = "true";
        
        //Résultat
        System.out.println("resultat testGetServletRights1 : "+result+" | exp : "+ExpResult);
        
        //Vérification du résultat
        assertEquals(ExpResult, result);
    }
    
    /**
     * Test of getServletRights method, of class DAOusers.
     */
    public void testGetServletRights2() {
        
        //Récupération des droits de l'utilisateur
        String result = DAO.getServletRights("ListUsers", "Utilisateur", false);
        
        //Résultat attendu
        String ExpResult = "true";
        
        //Résultat
        System.out.println("resultat testGetServletRights2 : "+result+" | exp : "+ExpResult);
        
        //Vérification du résultat
        assertEquals(ExpResult, result);
    }
}

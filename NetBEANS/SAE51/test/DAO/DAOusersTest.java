package DAO;

import Tests.DAOTest;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Maxime VALLET
 * @version 0.1
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
        String token = "$2a$08$VOtVbubOhyXhjEffToT.n.F9d8t9kwY0ulySEMKKoGZDisi4ni1s."; //token : "10101010101010101010101010101010"
        DAOtest.addUserWithToken(login, nom, prenom, role, hashedPassword, tokenLifeCycle, token, true);
        
        //Récuppération du MDP hashé
        String result = DAO.getUserPasswordHash(login, true);
        
        //Suppression de l'utilisateur
        DAO.deleteUser(login, true);
        
        //Résultat attendu
        String ExpResult = hashedPassword;
        
        //Résultat
        System.out.println("resultat testGetUserPasswordHash : "+result+" | exp : "+ExpResult);
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
        DAOtest.addUserWithToken(login, nom, prenom, role, hashedPassword, tokenLifeCycle, token, true);
        
        //Récuppération des droits de l'utilisateur
        String result = DAO.getUserRightsFromLogin(token, Test);
        
        //Suppression de l'utilisateur
        DAO.deleteUser(login, true);
        
        //Résultat attendu
        String ExpResult = role;
        
        //Résultat
        System.out.println("resultat testGetUserRightsFromLogin : "+result+" | exp : "+ExpResult);
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
        DAOtest.addUserWithToken(login, nom, prenom, role, hashedPassword, tokenLifeCycle, token, true);
        
        //Récuppération des droits de l'utilisateur
        String result = DAO.getUserRightsFromToken(token, Test);
        
        //Suppression de l'utilisateur
        DAO.deleteUser(login, true);
        
        //Résultat attendu
        String ExpResult = role;
        
        //Résultat
        System.out.println("resultat testGetUserRightsFromToken : "+result+" | exp : "+ExpResult);
    }

    /**
     * Test of setToken method, of class DAOusers.
     */
    @Test
    public void testSetToken() {
        
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
        DAOtest.addUserWithToken(login, nom, prenom, role, hashedPassword, tokenLifeCycle, token, true);
        
        //Récuppération des utilisateurs
        String result = DAO.getUsers(Test);
        
        //Suppression de l'utilisateur
        DAO.deleteUser(login, true);
        
        //Résultat attendu
        String ExpResult = "[{\"login\":\"Admin1\", \"prenom\":\"Originel\", \"nom\":\"Admin\", \"droits\":\"Admin\"}]";
        
        //Résultat
        System.out.println("resultat testGetUsers : "+result+" | exp : "+ExpResult);
    }

    /**
     * Test of getTokenLifeCycle method, of class DAOusers.
     */
    @Test
    public void testGetTokenLifeCycle() {
        
    }

    /**
     * Test of deleteToken method, of class DAOusers.
     */
    @Test
    public void testDeleteToken() {
        
    }

    /**
     * Test of getToken method, of class DAOusers.
     */
    @Test
    public void testGetToken() {
        
    }

    /**
     * Test of setLifeCycle method, of class DAOusers.
     */
    @Test
    public void testSetLifeCycle() {
        
    }

    /**
     * Test of checkToken method, of class DAOusers.
     */
    @Test
    public void testCheckToken() {
        
    }

    /**
     * Test of deleteUser method, of class DAOusers.
     */
    @Test
    public void testDeleteUser() {
        
    }

    /**
     * Test of getActiveUsers method, of class DAOusers.
     */
    @Test
    public void testGetActiveUsers() {
        
    }

    /**
     * Test of doLoginExist method, of class DAOusers.
     */
    @Test
    public void testDoLoginExist() {
        
    }

    /**
     * Test of addUser method, of class DAOusers.
     */
    @Test
    public void testAddUser() {
        
    }

    /**
     * Test of getRedirection method, of class DAOusers.
     */
    @Test
    public void testGetRedirection() {
        
    }
    
}

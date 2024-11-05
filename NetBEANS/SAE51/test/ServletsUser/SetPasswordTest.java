package ServletsUser;

import DAO.DAOusers;
import Tests.DAOTest;
import Tests.POSTRequest;
import java.io.IOException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Maxime VALLET
 * @version 1.0
 */
public class SetPasswordTest {
    
    /**
     * Test modification du MDP d'un utilisateur
     * 
     * Conditions de test :
     *  - champs envoyés : ok
     *  - droits d'accès : Admin
     *      => modification de => pour : Admin1 => Utilisateur1
     */
    @Test
    public void testAddUser1() throws IOException {
        DAOTest DAO = new DAOTest();
        DAOusers DAO2 = new DAOusers();
        POSTRequest req = new POSTRequest();
        
        //Ajout d'un utilisateur ayant les droits d'admin
        String login = "Admin1";
        String nom = "Admin";
        String prenom = "Originel";
        String role = "Admin";
        String hashedPassword = "$2a$12$l3MjhFmfr7VGoL0uPX2VKuEmXxboZzyABhjVNqH9.TnrCD2hEvfmm"; // MDP = "leffe"
        Integer tokenLifeCycle = 999;
        String token = "$2a$08$VOtVbubOhyXhjEffToT.n.F9d8t9kwY0ulySEMKKoGZDisi4ni1s."; //token : "10101010101010101010101010101010"
        DAO.addUserWithToken(login, nom, prenom, role, hashedPassword, tokenLifeCycle, token, true);
        
        //Ajout d'un utilisateur ayant les droits d'utilisateur
        login = "Utilisateur1";
        nom = "Utilisateur";
        prenom = "Originel";
        role = "Utilisateur";
        hashedPassword = "$2a$12$l3MjhFmfr7VGoL0uPX2VKuEmXxboZzyABhjVNqH9.TnrCD2hEvfmm"; // MDP = "leffe"
        tokenLifeCycle = 999;
        token = "$2a$12$q9zj9dmn92yKM8Qh1Vb98.0zQe1uMdfXVqjvjaF32fwTRq1BLmrgW"; //token : "01010101010101010101010101010101"
        DAO.addUserWithToken(login, nom, prenom, role, hashedPassword, tokenLifeCycle, token, true);
        
        
        //JSON qui contient tous les paramètres à envoyer au servlet
        String jsonPayload = "{\"target\":\"Utilisateur1\", \"password\":\"Heineken\", \"token\":\"10101010101010101010101010101010\", \"Test\":\"true\"}";
        
        //Requête au servlet
        req.doRequest("http://localhost:8080/SAE51/SetPassword", jsonPayload);
        
        //JSON qui contient tous les paramètres à envoyer au servlet (test de connexion avec le nouveau MDP)
        jsonPayload = "{\"login\":\"Utilisateur1\", \"password\":\"Heineken\", \"Test\":\"true\"}";
        
        //Résultat obtenu
        String result = req.doRequest("http://localhost:8080/SAE51/CheckPassword", jsonPayload);
        
        //Résultat attendu
        String ExpResult = "{\"droits\":\"Utilisateur\", \"token\":\"10101010101010101010101010101010\", \"login\":\"Utilisateur1\", \"erreur\":\"none\"}";
        
        //Retrait des utilisateurs
        DAO2.deleteUser("Admin1", true);
        DAO2.deleteUser("Utilisateur1", true);
        
        //Résultat
        System.out.println("resultat testPasswordTest1 : "+result+" | exp : "+ExpResult);
        
        //Vérification du résultat
        assertEquals(ExpResult, result);
    }
    
    
    
    /**
     * Test modification du MDP d'un utilisateur
     * 
     * Conditions de test :
     *  - champs envoyés : target manquant
     *  - droits d'accès : Admin
     *      => modification de => pour : Admin1 => Utilisateur1
     */
    @Test
    public void testAddUser2() throws IOException {
        DAOTest DAO = new DAOTest();
        DAOusers DAO2 = new DAOusers();
        POSTRequest req = new POSTRequest();
        
        //Ajout d'un utilisateur ayant les droits d'admin
        String login = "Admin1";
        String nom = "Admin";
        String prenom = "Originel";
        String role = "Admin";
        String hashedPassword = "$2a$12$l3MjhFmfr7VGoL0uPX2VKuEmXxboZzyABhjVNqH9.TnrCD2hEvfmm"; // MDP = "leffe"
        Integer tokenLifeCycle = 999;
        String token = "$2a$08$VOtVbubOhyXhjEffToT.n.F9d8t9kwY0ulySEMKKoGZDisi4ni1s."; //token : "10101010101010101010101010101010"
        DAO.addUserWithToken(login, nom, prenom, role, hashedPassword, tokenLifeCycle, token, true);
        
        //Ajout d'un utilisateur ayant les droits d'utilisateur
        login = "Utilisateur1";
        nom = "Utilisateur";
        prenom = "Originel";
        role = "Utilisateur";
        hashedPassword = "$2a$12$l3MjhFmfr7VGoL0uPX2VKuEmXxboZzyABhjVNqH9.TnrCD2hEvfmm"; // MDP = "leffe"
        tokenLifeCycle = 999;
        token = "$2a$12$q9zj9dmn92yKM8Qh1Vb98.0zQe1uMdfXVqjvjaF32fwTRq1BLmrgW"; //token : "01010101010101010101010101010101"
        DAO.addUserWithToken(login, nom, prenom, role, hashedPassword, tokenLifeCycle, token, true);
        
        
        //JSON qui contient tous les paramètres à envoyer au servlet
        String jsonPayload = "{\"password\":\"Heineken\", \"token\":\"10101010101010101010101010101010\", \"Test\":\"true\"}";
        
        //Requête au servlet
        String result = req.doRequest("http://localhost:8080/SAE51/SetPassword", jsonPayload);
        
        //Résultat attendu
        String ExpResult = "{\"erreur\":\"champ(s) manquant (req)\"}";
        
        //Retrait des utilisateurs
        DAO2.deleteUser("Admin1", true);
        DAO2.deleteUser("Utilisateur1", true);
        
        //Résultat
        System.out.println("resultat testPasswordTest2 : "+result+" | exp : "+ExpResult);
        
        //Vérification du résultat
        assertEquals(ExpResult, result);
    }
    
    
    
    /**
     * Test modification du MDP d'un utilisateur
     * 
     * Conditions de test :
     *  - champs envoyés : ok
     *  - droits d'accès : Aucun
     *      => modification de => pour : Admin1 => Utilisateur1
     */
    @Test
    public void testAddUser3() throws IOException {
        DAOTest DAO = new DAOTest();
        DAOusers DAO2 = new DAOusers();
        POSTRequest req = new POSTRequest();
        
        //Ajout d'un utilisateur ayant les droits d'admin
        String login = "Admin1";
        String nom = "Admin";
        String prenom = "Originel";
        String role = "Aucun";
        String hashedPassword = "$2a$12$l3MjhFmfr7VGoL0uPX2VKuEmXxboZzyABhjVNqH9.TnrCD2hEvfmm"; // MDP = "leffe"
        Integer tokenLifeCycle = 999;
        String token = "$2a$08$VOtVbubOhyXhjEffToT.n.F9d8t9kwY0ulySEMKKoGZDisi4ni1s."; //token : "10101010101010101010101010101010"
        DAO.addUserWithToken(login, nom, prenom, role, hashedPassword, tokenLifeCycle, token, true);
        
        //Ajout d'un utilisateur ayant les droits d'utilisateur
        login = "Utilisateur1";
        nom = "Utilisateur";
        prenom = "Originel";
        role = "Utilisateur";
        hashedPassword = "$2a$12$l3MjhFmfr7VGoL0uPX2VKuEmXxboZzyABhjVNqH9.TnrCD2hEvfmm"; // MDP = "leffe"
        tokenLifeCycle = 999;
        token = "$2a$12$q9zj9dmn92yKM8Qh1Vb98.0zQe1uMdfXVqjvjaF32fwTRq1BLmrgW"; //token : "01010101010101010101010101010101"
        DAO.addUserWithToken(login, nom, prenom, role, hashedPassword, tokenLifeCycle, token, true);
        
        
        //JSON qui contient tous les paramètres à envoyer au servlet
        String jsonPayload = "{\"target\":\"Utilisateur1\", \"password\":\"Heineken\", \"token\":\"10101010101010101010101010101010\", \"Test\":\"true\"}";
        
        //Requête au servlet
        String result = req.doRequest("http://localhost:8080/SAE51/SetPassword", jsonPayload);
        
        //Résultat attendu
        String ExpResult = "{\"erreur\":\"accès refusé\"}";
        
        //Retrait des utilisateurs
        DAO2.deleteUser("Admin1", true);
        DAO2.deleteUser("Utilisateur1", true);
        
        //Résultat
        System.out.println("resultat testPasswordTest3 : "+result+" | exp : "+ExpResult);
        
        //Vérification du résultat
        assertEquals(ExpResult, result);
    }
    
    
    
    /**
     * Test modification du MDP d'un utilisateur
     * 
     * Conditions de test :
     *  - champs envoyés : ok
     *  - droits d'accès : Utilisateur
     *      => modification de => pour : Utilisateur1 => Admin1
     */
    @Test
    public void testAddUser4() throws IOException {
        DAOTest DAO = new DAOTest();
        DAOusers DAO2 = new DAOusers();
        POSTRequest req = new POSTRequest();
        
        //Ajout d'un utilisateur ayant les droits d'admin
        String login = "Admin1";
        String nom = "Admin";
        String prenom = "Originel";
        String role = "Admin";
        String hashedPassword = "$2a$12$l3MjhFmfr7VGoL0uPX2VKuEmXxboZzyABhjVNqH9.TnrCD2hEvfmm"; // MDP = "leffe"
        Integer tokenLifeCycle = 999;
        String token = "$2a$08$VOtVbubOhyXhjEffToT.n.F9d8t9kwY0ulySEMKKoGZDisi4ni1s."; //token : "10101010101010101010101010101010"
        DAO.addUserWithToken(login, nom, prenom, role, hashedPassword, tokenLifeCycle, token, true);
        
        //Ajout d'un utilisateur ayant les droits d'utilisateur
        login = "Utilisateur1";
        nom = "Utilisateur";
        prenom = "Originel";
        role = "Utilisateur";
        hashedPassword = "$2a$12$l3MjhFmfr7VGoL0uPX2VKuEmXxboZzyABhjVNqH9.TnrCD2hEvfmm"; // MDP = "leffe"
        tokenLifeCycle = 999;
        token = "$2a$12$q9zj9dmn92yKM8Qh1Vb98.0zQe1uMdfXVqjvjaF32fwTRq1BLmrgW"; //token : "01010101010101010101010101010101"
        DAO.addUserWithToken(login, nom, prenom, role, hashedPassword, tokenLifeCycle, token, true);
        
        
        //JSON qui contient tous les paramètres à envoyer au servlet
        String jsonPayload = "{\"target\":\"Admin1\", \"password\":\"Heineken\", \"token\":\"01010101010101010101010101010101\", \"Test\":\"true\"}";
        
        //Requête au servlet
        String result = req.doRequest("http://localhost:8080/SAE51/SetPassword", jsonPayload);
        
        //Résultat attendu
        String ExpResult = "{\"erreur\":\"permission refusée\"}";
        
        //Retrait des utilisateurs
        DAO2.deleteUser("Admin1", true);
        DAO2.deleteUser("Utilisateur1", true);
        
        //Résultat
        System.out.println("resultat testPasswordTest4 : "+result+" | exp : "+ExpResult);
        
        //Vérification du résultat
        assertEquals(ExpResult, result);
    }
}

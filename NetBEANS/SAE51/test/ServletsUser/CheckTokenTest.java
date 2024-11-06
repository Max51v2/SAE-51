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
public class CheckTokenTest {
    
    /**
     * Test de vérification du token
     * 
     * Conditions de test :
     *  - champs envoyés : ok
     *  - token DB : ok
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
        
        
        //JSON qui contient tous les paramètres à envoyer au servlet
        String jsonPayload = "{\"token\":\"10101010101010101010101010101010\", \"Test\":\"true\"}";
        
        //Requête au servlet
        String result = req.doRequest("http://localhost:8080/SAE51/CheckToken", jsonPayload);
        
        //Résultat attendu
        String ExpResult = "{\"login\":\"" + login + "\", \"droits\":\"" + role + "\", \"erreur\":\"none\"}";
        
        //Retrait des utilisateurs
        DAO2.deleteUser(login, true);
        
        //Résultat
        System.out.println("resultat testCheckToken1 : "+result+" | exp : "+ExpResult);
        
        //Vérification du résultat
        assertEquals(ExpResult, result);
    }
    
    
    
    /**
     * Test de vérification du token
     * 
     * Conditions de test :
     *  - champs envoyés : token inexistant
     *  - token DB : ok
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
        
        
        //JSON qui contient tous les paramètres à envoyer au servlet
        String jsonPayload = "{\"Test\":\"true\"}";
        
        //Requête au servlet
        String result = req.doRequest("http://localhost:8080/SAE51/CheckToken", jsonPayload);
        
        //Résultat attendu
        String ExpResult = "{\"erreur\":\"pas de token (req)\"}";
        
        //Retrait des utilisateurs
        DAO2.deleteUser(login, true);
        
        //Résultat
        System.out.println("resultat testCheckToken2 : "+result+" | exp : "+ExpResult);
        
        //Vérification du résultat
        assertEquals(ExpResult, result);
    }
    
    
    
    /**
     * Test de vérification du token
     * 
     * Conditions de test :
     *  - champs envoyés : ok
     *  - token DB : inexistant
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
        String role = "Admin";
        String hashedPassword = "$2a$12$l3MjhFmfr7VGoL0uPX2VKuEmXxboZzyABhjVNqH9.TnrCD2hEvfmm"; // MDP = "leffe"
        Integer tokenLifeCycle = 999;
        String token = ""; //token : "10101010101010101010101010101010"
        DAO.addUserWithToken(login, nom, prenom, role, hashedPassword, tokenLifeCycle, token, true);
        
        
        //JSON qui contient tous les paramètres à envoyer au servlet
        String jsonPayload = "{\"token\":\"10101010101010101010101010101010\", \"Test\":\"true\"}";
        
        //Requête au servlet
        String result = req.doRequest("http://localhost:8080/SAE51/CheckToken", jsonPayload);
        
        //Résultat attendu
        String ExpResult = "{\"erreur\":\"pas de token (DB)\"}";
        
        //Retrait des utilisateurs
        DAO2.deleteUser(login, true);
        
        //Résultat
        System.out.println("resultat testCheckToken3 : "+result+" | exp : "+ExpResult);
        
        //Vérification du résultat
        assertEquals(ExpResult, result);
    }
}

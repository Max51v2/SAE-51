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
public class DeleteTokenTest {
    
    /**
     * Test de suppression des tokens
     * 
     * Conditions de test :
     *  - champs envoyés : ok
     *  - droits : ok
     *  - login : existe dans la BD
     */
    @Test
    public void testDeleteToken1() throws IOException {
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
        String jsonPayload = "{\"token\":\"10101010101010101010101010101010\", \"login\":\""+login+"\", \"test\":\"true\"}";
        
        //Requête au servlet
        req.doRequest("http://localhost:8080/SAE51/DeleteToken", jsonPayload);
        
        //Résultat obtenu
        String result = DAO.getToken(login, true);
        
        //Résultat attendu
        String ExpResult = "";
        
        //Retrait des utilisateurs
        DAO2.deleteUser(login, true);
        
        //Résultat
        System.out.println("resultat testDeleteToken1 : "+result+" | exp : "+ExpResult);
        
        //Vérification du résultat
        assertEquals(ExpResult, result);
    }
    
    
    
    /**
     * Test de suppression des tokens
     * 
     * Conditions de test :
     *  - champs envoyés : login inexistant
     *  - droits : ok
     *  - login : existe dans la BD
     */
    @Test
    public void testDeleteToken2() throws IOException {
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
        String jsonPayload = "{\"token\":\"10101010101010101010101010101010\", \"test\":\"true\"}";
        
        //Requête au servlet
        String result = req.doRequest("http://localhost:8080/SAE51/DeleteToken", jsonPayload);
        
        //Résultat attendu
        String ExpResult = "{\"erreur\":\"pas de login ou token (req)\"}";
        
        //Retrait des utilisateurs
        DAO2.deleteUser(login, true);
        
        //Résultat
        System.out.println("resultat testDeleteToken2 : "+result+" | exp : "+ExpResult);
        
        //Vérification du résultat
        assertEquals(ExpResult, result);
    }
    
    
    
    /**
     * Test de suppression des tokens
     * 
     * Conditions de test :
     *  - champs envoyés : ok
     *  - droits : non
     *  - login : existe dans la BD
     */
    @Test
    public void testDeleteToken3() throws IOException {
        DAOTest DAO = new DAOTest();
        DAOusers DAO2 = new DAOusers();
        POSTRequest req = new POSTRequest();
        
        //Ajout d'un utilisateur ayant les droits d'admin
        String login = "Admin1";
        String nom = "Admin";
        String prenom = "Originel";
        String role = "none";
        String hashedPassword = "$2a$12$l3MjhFmfr7VGoL0uPX2VKuEmXxboZzyABhjVNqH9.TnrCD2hEvfmm"; // MDP = "leffe"
        Integer tokenLifeCycle = 999;
        String token = "$2a$08$VOtVbubOhyXhjEffToT.n.F9d8t9kwY0ulySEMKKoGZDisi4ni1s."; //token : "10101010101010101010101010101010"
        DAO.addUserWithToken(login, nom, prenom, role, hashedPassword, tokenLifeCycle, token, true);
        
        
        //JSON qui contient tous les paramètres à envoyer au servlet
        String jsonPayload = "{\"token\":\"10101010101010101010101010101010\", \"login\":\""+login+"\", \"test\":\"true\"}";
        
        //Requête au servlet
        String result = req.doRequest("http://localhost:8080/SAE51/DeleteToken", jsonPayload);
        
        //Résultat attendu
        String ExpResult = "{\"erreur\":\"accès refusé\"}";
        
        //Retrait des utilisateurs
        DAO2.deleteUser(login, true);
        
        //Résultat
        System.out.println("resultat testDeleteToken3 : "+result+" | exp : "+ExpResult);
        
        //Vérification du résultat
        assertEquals(ExpResult, result);
    }
    
    
    
    /**
     * Test de suppression des tokens
     * 
     * Conditions de test :
     *  - champs envoyés : ok
     *  - droits : ok
     *  - login : inexistant dans la BD
     */
    @Test
    public void testDeleteToken4() throws IOException {
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
        String jsonPayload = "{\"token\":\"10101010101010101010101010101010\", \"login\":\"Xx_Jeff_xX\", \"test\":\"true\"}";
        
        //Requête au servlet
        String result = req.doRequest("http://localhost:8080/SAE51/DeleteToken", jsonPayload);
        
        //Résultat attendu
        String ExpResult = "{\"erreur\":\"login inexistant (DB)\"}";
        
        //Retrait des utilisateurs
        DAO2.deleteUser(login, true);
        
        //Résultat
        System.out.println("resultat testDeleteToken4 : "+result+" | exp : "+ExpResult);
        
        //Vérification du résultat
        assertEquals(ExpResult, result);
    }
}

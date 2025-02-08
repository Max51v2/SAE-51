package ServletsUser;

import DAO.DAOusers;
import Tests.DAOTest;
import Tests.POSTRequest;
import java.io.IOException;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Maxime VALLET
 * @version 1.0
 */
public class AddUserTest {
    
    /**
     * Test ajout d'utilisateur
     * 
     * Conditions de test :
     *  - champs envoyés : ok
     *  - droits d'accès : ok
     *  - login DB : pas de doublon
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
        String jsonPayload = "{\"nom\":\"Blakowicz\", \"prenom\":\"Stan\", \"login\":\"Hell walker\", \"droits\":\"Admin\", \"password\":\"666\", \"token\":\"10101010101010101010101010101010\", \"test\":\"true\"}";
        
        //Requête au servlet
        req.doRequest("http://localhost:8080/SAE51/AddUser", jsonPayload);
        
        //Résultat obtenu
        String result = DAO2.getUsers(true);
        
        //Résultat attendu
        String ExpResult = "[{\"login\":\"Admin1\", \"prenom\":\"Originel\", \"nom\":\"Admin\", \"droits\":\"Admin\"},{\"login\":\"Hell walker\", \"prenom\":\"Stan\", \"nom\":\"Blakowicz\", \"droits\":\"Admin\"}]";
        
        //Retrait des utilisateurs
        DAO2.deleteUser(login, true);
        DAO2.deleteUser("Hell walker", true);
        
        //Résultat
        System.out.println("resultat testAddUser1 : "+result+" | exp : "+ExpResult);
        
        //Vérification du résultat
        assertEquals(ExpResult, result);
    }
    
    
    
    /**
     * Test ajout d'utilisateur
     * 
     * Conditions de test :
     *  - champs envoyés : login manquant
     *  - droits d'accès : ok
     *  - login DB : pas de doublon
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
        String jsonPayload = "{\"nom\":\"Blakowicz\", \"prenom\":\"Stan\", \"droits\":\"Admin\", \"password\":\"666\", \"token\":\"10101010101010101010101010101010\", \"test\":\"true\"}";
        
        //Requête au servlet
        String result = req.doRequest("http://localhost:8080/SAE51/AddUser", jsonPayload);
        
        //Résultat attendu
        String ExpResult = "{\"erreur\":\"champ(s) manquant (req)\"}";
        
        //Retrait des utilisateurs
        DAO2.deleteUser(login, true);
        
        //Résultat
        System.out.println("resultat testAddUser2 : "+result+" | exp : "+ExpResult);
        
        //Vérification du résultat
        assertEquals(ExpResult, result);
    }
    
    
    
    /**
     * Test ajout d'utilisateur
     * 
     * Conditions de test :
     *  - champs envoyés : ok
     *  - droits d'accès : non
     *  - login DB : pas de doublon
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
        String role = "Utilisateur";
        String hashedPassword = "$2a$12$l3MjhFmfr7VGoL0uPX2VKuEmXxboZzyABhjVNqH9.TnrCD2hEvfmm"; // MDP = "leffe"
        Integer tokenLifeCycle = 999;
        String token = "$2a$08$VOtVbubOhyXhjEffToT.n.F9d8t9kwY0ulySEMKKoGZDisi4ni1s."; //token : "10101010101010101010101010101010"
        DAO.addUserWithToken(login, nom, prenom, role, hashedPassword, tokenLifeCycle, token, true);
        
        
        //JSON qui contient tous les paramètres à envoyer au servlet
        String jsonPayload = "{\"nom\":\"Blakowicz\", \"prenom\":\"Stan\", \"login\":\"Hell walker\", \"droits\":\"Admin\", \"password\":\"666\", \"token\":\"10101010101010101010101010101010\", \"test\":\"true\"}";
        
        //Requête au servlet
        String result = req.doRequest("http://localhost:8080/SAE51/AddUser", jsonPayload);
        
        //Résultat attendu
        String ExpResult = "{\"erreur\":\"accès refusé\"}";
        
        //Retrait des utilisateurs
        DAO2.deleteUser(login, true);
        
        //Résultat
        System.out.println("resultat testAddUser3 : "+result+" | exp : "+ExpResult);
        
        //Vérification du résultat
        assertEquals(ExpResult, result);
    }
    
    
    
    /**
     * Test ajout d'utilisateur
     * 
     * Conditions de test :
     *  - champs envoyés : ok
     *  - droits d'accès : ok
     *  - login DB : doublon
     */
    @Test
    public void testAddUser4() throws IOException {
        DAOTest DAO = new DAOTest();
        DAOusers DAO2 = new DAOusers();
        POSTRequest req = new POSTRequest();
        
        //Ajout d'un utilisateur ayant les droits d'admin
        String login = "Hell walker";
        String nom = "Admin";
        String prenom = "Originel";
        String role = "Admin";
        String hashedPassword = "$2a$12$l3MjhFmfr7VGoL0uPX2VKuEmXxboZzyABhjVNqH9.TnrCD2hEvfmm"; // MDP = "leffe"
        Integer tokenLifeCycle = 999;
        String token = "$2a$08$VOtVbubOhyXhjEffToT.n.F9d8t9kwY0ulySEMKKoGZDisi4ni1s."; //token : "10101010101010101010101010101010"
        DAO.addUserWithToken(login, nom, prenom, role, hashedPassword, tokenLifeCycle, token, true);
        
        
        //JSON qui contient tous les paramètres à envoyer au servlet
        String jsonPayload = "{\"nom\":\"Blakowicz\", \"prenom\":\"Stan\", \"login\":\"Hell walker\", \"droits\":\"Admin\", \"password\":\"666\", \"token\":\"10101010101010101010101010101010\", \"test\":\"true\"}";
        
        //Requête au servlet
        String result = req.doRequest("http://localhost:8080/SAE51/AddUser", jsonPayload);
        
        //Résultat attendu
        String ExpResult = "{\"erreur\":\"login existe (DB)\"}";
        
        //Retrait des utilisateurs
        DAO2.deleteUser(login, true);
        
        //Résultat
        System.out.println("resultat testAddUser4 : "+result+" | exp : "+ExpResult);
        
        //Vérification du résultat
        assertEquals(ExpResult, result);
    }
}
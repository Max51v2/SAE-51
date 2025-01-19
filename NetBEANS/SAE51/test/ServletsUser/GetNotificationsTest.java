package ServletsUser;

import DAO.DAONotifications;
import DAO.DAOusers;
import Tests.DAOTest;
import Tests.POSTRequest;
import java.io.IOException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Maxime VALLET
 */
public class GetNotificationsTest {
    
    DAONotifications DAONotifications = new DAONotifications();
    
    @Before
    public void setUp() {
        //Supression des notifs
        DAONotifications.deleteNotifications(true);
        
        //Ajout de notifications
        DAONotifications.addNotification("test1", "test content 1", "Hell Wallker/Admin1", true);
        DAONotifications.addNotification("test2", "test content 2", "Hell Wallker", true);
    }
    
    @After
    public void tearDown() {
        //Supression des notifs
        DAONotifications.deleteNotifications(true);
    }

    /**
     * Test d'obtention des notifications
     * 
     * Conditions de test :
     *  - champs envoyés : ok
     *  - droits : admin
     */
    @Test
    public void testGetNotifications1() throws IOException {
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
        String result = req.doRequest("http://localhost:8080/SAE51/GetNotifications", jsonPayload);
        
        //Retrait des utilisateurs
        DAO2.deleteUser(login, true);
        
        //Résultat attendu
        String ExpResult = "[{\"description\":\"test1\", \"content\":\"test content 1\"},{\"description\":\"test2\", \"content\":\"test content 2\"}]";
        
        //Résultat
        System.out.println("resultat testGetNotifications1 : "+result+" | exp : "+ExpResult);
        
        //Vérification du résultat
        assertEquals(ExpResult, result);
    }
    
    
    
    /**
     * Test d'obtention des notifications
     * 
     * Conditions de test :
     *  - champs envoyés : ok
     *  - droits : tous droits (hors admin)
     */
    @Test
    public void testGetNotifications2() throws IOException {
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
        String jsonPayload = "{\"token\":\"10101010101010101010101010101010\", \"Test\":\"true\"}";
        
        //Requête au servlet
        String result = req.doRequest("http://localhost:8080/SAE51/GetNotifications", jsonPayload);
        
        //Retrait des utilisateurs
        DAO2.deleteUser(login, true);
        
        //Résultat attendu
        String ExpResult = "[{\"description\":\"test1\", \"content\":\"test content 1\"}]";
        
        //Résultat
        System.out.println("resultat testGetNotifications2 : "+result+" | exp : "+ExpResult);
        
        //Vérification du résultat
        assertEquals(ExpResult, result);
    }
    
    
    
    /**
     * Test d'obtention des notifications
     * 
     * Conditions de test :
     *  - champs envoyés : ok
     *  - droits : none
     */
    @Test
    public void testGetNotifications3() throws IOException {
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
        String jsonPayload = "{\"token\":\"10101010101010101010101010101010\", \"Test\":\"true\"}";
        
        //Requête au servlet
        String result = req.doRequest("http://localhost:8080/SAE51/GetNotifications", jsonPayload);
        
        //Retrait des utilisateurs
        DAO2.deleteUser(login, true);
        
        //Résultat attendu
        String ExpResult = "{\"erreur\":\"accès refusé\"}";
        
        //Résultat
        System.out.println("resultat testGetNotifications3 : "+result+" | exp : "+ExpResult);
        
        //Vérification du résultat
        assertEquals(ExpResult, result);
    }
    
    
    
    /**
     * Test d'obtention des notifications
     * 
     * Conditions de test :
     *  - champs envoyés : token manquant
     *  - droits : admin
     */
    @Test
    public void testGetNotifications4() throws IOException {
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
        String result = req.doRequest("http://localhost:8080/SAE51/GetNotifications", jsonPayload);
        
        //Retrait des utilisateurs
        DAO2.deleteUser(login, true);
        
        //Résultat attendu
        String ExpResult = "{\"erreur\":\"champ(s) manquant (req)\"}";
        
        //Résultat
        System.out.println("resultat testGetNotifications4 : "+result+" | exp : "+ExpResult);
        
        //Vérification du résultat
        assertEquals(ExpResult, result);
    }
}

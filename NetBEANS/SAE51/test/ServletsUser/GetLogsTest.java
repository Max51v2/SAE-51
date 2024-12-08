package ServletsUser;

import DAO.DAOusers;
import Tests.DAOTest;
import Tests.POSTRequest;
import java.io.IOException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author root
 */
public class GetLogsTest {
    
    /**
     * Test de listage des logs
     * 
     * Conditions de test :
     *  - champs envoyés : ok
     *  - période : ok
     *  - droits : ok
     *  - niveau de rapport : All
     */
    @Test
    public void testGetLogs1() throws IOException {
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
        String jsonPayload = "{\"beginDate\":\"00000000_000000\", \"endDate\":\"99999999_235959\", \"logLevelReq\":\"All\", \"token\":\"10101010101010101010101010101010\", \"Test\":\"true\"}";
        
        //Requête au servlet
        String result = req.doRequest("http://localhost:8080/SAE51/GetLogs", jsonPayload);
        
        //Retrait des utilisateurs
        DAO2.deleteUser(login, true);
        
        //Résultat attendu
        String ExpResult = "[{\"id\":\"1\", \"servlet\":\"Test\", \"ip\":\"1.1.1.1\", \"login\":\"Hell walker\", \"droits\":\"Admin\", \"date\":\"10/12/1993 | 00:00:01\"},{\"id\":\"2\", \"servlet\":\"Test2\", \"ip\":\"2.2.2.2\", \"login\":\"Maxime\", \"droits\":\"Admin\", \"date\":\"24/11/2024 | 01:29:00\"}]";
        
        //Résultat
        System.out.println("resultat testGetLogs1 : "+result+" | exp : "+ExpResult);
        
        //Vérification du résultat
        assertEquals(ExpResult, result);
    }
    
    
    
    /**
     * Test de listage des logs
     * 
     * Conditions de test :
     *  - champs envoyés : beginDate manquant
     *  - période : ok
     *  - droits : ok
     *  - niveau de rapport : All
     */
    @Test
    public void testGetLogs2() throws IOException {
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
        String jsonPayload = "{\"endDate\":\"99999999_235959\", \"logLevelReq\":\"All\", \"token\":\"10101010101010101010101010101010\", \"Test\":\"true\"}";
        
        //Requête au servlet
        String result = req.doRequest("http://localhost:8080/SAE51/GetLogs", jsonPayload);
        
        //Retrait des utilisateurs
        DAO2.deleteUser(login, true);
        
        //Résultat attendu
        String ExpResult = "{\"erreur\":\"champ(s) manquant (req)\"}";
        
        //Résultat
        System.out.println("resultat testGetLogs2 : "+result+" | exp : "+ExpResult);
        
        //Vérification du résultat
        assertEquals(ExpResult, result);
    }
    
    
    
    /**
     * Test de listage des logs
     * 
     * Conditions de test :
     *  - champs envoyés : ok
     *  - période : début après fin
     *  - droits : ok
     *  - niveau de rapport : All
     */
    @Test
    public void testGetLogs3() throws IOException {
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
        String jsonPayload = "{\"beginDate\":\"99999999_235959\", \"endDate\":\"00000000_000000\", \"logLevelReq\":\"All\", \"token\":\"10101010101010101010101010101010\", \"Test\":\"true\"}";
        
        //Requête au servlet
        String result = req.doRequest("http://localhost:8080/SAE51/GetLogs", jsonPayload);
        
        //Retrait des utilisateurs
        DAO2.deleteUser(login, true);
        
        //Résultat attendu
        String ExpResult = "{\"erreur\":\"mauvaise période (req)\"}";
        
        //Résultat
        System.out.println("resultat testGetLogs3 : "+result+" | exp : "+ExpResult);
        
        //Vérification du résultat
        assertEquals(ExpResult, result);
    }
    
    
    
    /**
     * Test de listage des logs
     * 
     * Conditions de test :
     *  - champs envoyés : ok
     *  - période : ok
     *  - droits : pas bon
     *  - niveau de rapport : All
     */
    @Test
    public void testGetLogs4() throws IOException {
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
        String jsonPayload = "{\"beginDate\":\"00000000_000000\", \"endDate\":\"99999999_235959\", \"logLevelReq\":\"All\", \"token\":\"10101010101010101010101010101010\", \"Test\":\"true\"}";
        
        //Requête au servlet
        String result = req.doRequest("http://localhost:8080/SAE51/GetLogs", jsonPayload);
        
        //Retrait des utilisateurs
        DAO2.deleteUser(login, true);
        
        //Résultat attendu
        String ExpResult = "{\"erreur\":\"accès refusé\"}";
        
        //Résultat
        System.out.println("resultat testGetLogs4 : "+result+" | exp : "+ExpResult);
        
        //Vérification du résultat
        assertEquals(ExpResult, result);
    }
    
    
    
    /**
     * Test de listage des logs
     * 
     * Conditions de test :
     *  - champs envoyés : ok
     *  - période : ok
     *  - droits : ok
     *  - niveau de rapport : ErrorsOnly
     */
    @Test
    public void testGetLogs5() throws IOException {
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
        String jsonPayload = "{\"beginDate\":\"00000000_000000\", \"endDate\":\"99999999_235959\", \"logLevelReq\":\"ErrorsOnly\", \"token\":\"10101010101010101010101010101010\", \"Test\":\"true\"}";
        
        //Requête au servlet
        String result = req.doRequest("http://localhost:8080/SAE51/GetLogs", jsonPayload);
        
        //Retrait des utilisateurs
        DAO2.deleteUser(login, true);
        
        //Résultat attendu
        String ExpResult = "[{\"id\":\"2\", \"servlet\":\"Test2\", \"ip\":\"2.2.2.2\", \"login\":\"Maxime\", \"droits\":\"Admin\", \"date\":\"24/11/2024 | 01:29:00\"}]";
        
        //Résultat
        System.out.println("resultat testGetLogs5 : "+result+" | exp : "+ExpResult);
        
        //Vérification du résultat
        assertEquals(ExpResult, result);
    }
}

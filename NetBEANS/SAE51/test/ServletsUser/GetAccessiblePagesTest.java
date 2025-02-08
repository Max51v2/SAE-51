package ServletsUser;

import Autre.ProjectConfig;
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
public class GetAccessiblePagesTest {
    
    /**
     * Test de la récupération des pages accessibles à l'utilisateur
     * 
     * Conditions de test :
     *  - champs envoyés : ok
     *  - droits : ok
     */
    @Test
    public void testGetAccessiblePages1() throws IOException {
        DAOTest DAO = new DAOTest();
        DAOusers DAO2 = new DAOusers();
        POSTRequest req = new POSTRequest();
        ProjectConfig conf = new ProjectConfig();
        
        //Ajout d'un utilisateur ayant les droits d'admin
        String login = "Admin1";
        String nom = "Admin";
        String prenom = "Originel";
        String role = "Admin";
        String hashedPassword = "$2a$12$l3MjhFmfr7VGoL0uPX2VKuEmXxboZzyABhjVNqH9.TnrCD2hEvfmm"; // MDP = "leffe"
        Integer tokenLifeCycle = 10;
        String token = "$2a$08$VOtVbubOhyXhjEffToT.n.F9d8t9kwY0ulySEMKKoGZDisi4ni1s."; //token : "10101010101010101010101010101010"
        DAO.addUserWithToken(login, nom, prenom, role, hashedPassword, tokenLifeCycle, token, true);
        
        //JSON qui contient tous les paramètres à envoyer au servlet
        String jsonPayload = "{\"token\":\"10101010101010101010101010101010\", \"test\":\"true\"}";
        
        //Requête au servlet
        String result = req.doRequest("http://localhost:8080/SAE51/GetAccessiblePages", jsonPayload);
        
        //Retrait des utilisateurs
        DAO2.deleteUser(login, true);
        
        //Résultat attendu
        Integer CheckIntervall = conf.getIntValue("CheckIntervall");
        String ExpResult = "[{\"page\":\"accueil.html\"}]";
        
        //Résultat
        System.out.println("resultat testGetAccessiblePages1 : "+result+" | exp : "+ExpResult);
        
        //Vérification du résultat
        assertEquals(ExpResult, result);
    }
    
    
    
    /**
     * Test de la récupération des pages accessibles à l'utilisateur
     * 
     * Conditions de test :
     *  - champs envoyés : token manquant
     *  - droits : ok
     */
    @Test
    public void testGetAccessiblePages2() throws IOException {
        DAOTest DAO = new DAOTest();
        DAOusers DAO2 = new DAOusers();
        POSTRequest req = new POSTRequest();
        ProjectConfig conf = new ProjectConfig();
        
        //Ajout d'un utilisateur ayant les droits d'admin
        String login = "Admin1";
        String nom = "Admin";
        String prenom = "Originel";
        String role = "Admin";
        String hashedPassword = "$2a$12$l3MjhFmfr7VGoL0uPX2VKuEmXxboZzyABhjVNqH9.TnrCD2hEvfmm"; // MDP = "leffe"
        Integer tokenLifeCycle = 10;
        String token = "$2a$08$VOtVbubOhyXhjEffToT.n.F9d8t9kwY0ulySEMKKoGZDisi4ni1s."; //token : "10101010101010101010101010101010"
        DAO.addUserWithToken(login, nom, prenom, role, hashedPassword, tokenLifeCycle, token, true);
        
        //JSON qui contient tous les paramètres à envoyer au servlet
        String jsonPayload = "{\"test\":\"true\"}";
        
        //Requête au servlet
        String result = req.doRequest("http://localhost:8080/SAE51/GetAccessiblePages", jsonPayload);
        
        //Retrait des utilisateurs
        DAO2.deleteUser(login, true);
        
        //Résultat attendu
        Integer CheckIntervall = conf.getIntValue("CheckIntervall");
        String ExpResult = "{\"erreur\":\"champ(s) manquant (req)\"}";
        
        //Résultat
        System.out.println("resultat testGetAccessiblePages2 : "+result+" | exp : "+ExpResult);
        
        //Vérification du résultat
        assertEquals(ExpResult, result);
    }
    
    
    
    /**
     * Test de la récupération des pages accessibles à l'utilisateur
     * 
     * Conditions de test :
     *  - champs envoyés : ok
     *  - droits : ok
     */
    @Test
    public void testGetAccessiblePages3() throws IOException {
        DAOTest DAO = new DAOTest();
        DAOusers DAO2 = new DAOusers();
        POSTRequest req = new POSTRequest();
        ProjectConfig conf = new ProjectConfig();
        
        //Ajout d'un utilisateur ayant les droits d'admin
        String login = "Admin1";
        String nom = "Admin";
        String prenom = "Originel";
        String role = "none";
        String hashedPassword = "$2a$12$l3MjhFmfr7VGoL0uPX2VKuEmXxboZzyABhjVNqH9.TnrCD2hEvfmm"; // MDP = "leffe"
        Integer tokenLifeCycle = 10;
        String token = "$2a$08$VOtVbubOhyXhjEffToT.n.F9d8t9kwY0ulySEMKKoGZDisi4ni1s."; //token : "10101010101010101010101010101010"
        DAO.addUserWithToken(login, nom, prenom, role, hashedPassword, tokenLifeCycle, token, true);
        
        //JSON qui contient tous les paramètres à envoyer au servlet
        String jsonPayload = "{\"token\":\"10101010101010101010101010101010\", \"test\":\"true\"}";
        
        //Requête au servlet
        String result = req.doRequest("http://localhost:8080/SAE51/GetAccessiblePages", jsonPayload);
        
        //Retrait des utilisateurs
        DAO2.deleteUser(login, true);
        
        //Résultat attendu
        Integer CheckIntervall = conf.getIntValue("CheckIntervall");
        String ExpResult = "{\"erreur\":\"accès refusé\"}";
        
        //Résultat
        System.out.println("resultat testGetAccessiblePages3 : "+result+" | exp : "+ExpResult);
        
        //Vérification du résultat
        assertEquals(ExpResult, result);
    }
}

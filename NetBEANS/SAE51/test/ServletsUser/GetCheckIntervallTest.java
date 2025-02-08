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
 */
public class GetCheckIntervallTest {
    
    /**
     * Test de la récupération de l'intervalle de verif des tokens
     * 
     * Conditions de test :
     *  - champs envoyés : ok
     *  - droits : ok
     */
    @Test
    public void testGetCheckIntervall1() throws IOException {
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
        String result = req.doRequest("http://localhost:8080/SAE51/GetCheckIntervall", jsonPayload);
        
        //Retrait des utilisateurs
        DAO2.deleteUser(login, true);
        
        //Résultat attendu
        Integer CheckIntervall = conf.getIntValue("CheckIntervall");
        String ExpResult = "{\"erreur\":\"none\", \"CheckIntervall\":\""+CheckIntervall+"\"}";
        
        //Résultat
        System.out.println("resultat testGetCheckIntervall1 : "+result+" | exp : "+ExpResult);
        
        //Vérification du résultat
        assertEquals(ExpResult, result);
    }
    
    
    
    
    /**
     * Test de la récupération de l'intervalle de verif des tokens
     * 
     * Conditions de test :
     *  - champs envoyés : token manquant
     *  - droits : ok
     */
    @Test
    public void testGetCheckIntervall2() throws IOException {
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
        String result = req.doRequest("http://localhost:8080/SAE51/GetCheckIntervall", jsonPayload);
        
        //Retrait des utilisateurs
        DAO2.deleteUser(login, true);
        
        //Résultat attendu
        Integer CheckIntervall = conf.getIntValue("CheckIntervall");
        String ExpResult = "{\"erreur\":\"champ manquant (req)\"}";
        
        //Résultat
        System.out.println("resultat testGetCheckIntervall2 : "+result+" | exp : "+ExpResult);
        
        //Vérification du résultat
        assertEquals(ExpResult, result);
    }
    
    
    
    
    /**
     * Test de la récupération de l'intervalle de verif des tokens
     * 
     * Conditions de test :
     *  - champs envoyés : ok
     *  - droits : aucun
     */
    @Test
    public void testGetCheckIntervall3() throws IOException {
        DAOTest DAO = new DAOTest();
        DAOusers DAO2 = new DAOusers();
        POSTRequest req = new POSTRequest();
        ProjectConfig conf = new ProjectConfig();
        
        //JSON qui contient tous les paramètres à envoyer au servlet
        String jsonPayload = "{\"token\":\"0000000000000000000000000000000\", \"test\":\"true\"}";
        
        //Requête au servlet
        String result = req.doRequest("http://localhost:8080/SAE51/GetCheckIntervall", jsonPayload);
        
        //Résultat attendu
        Integer CheckIntervall = conf.getIntValue("CheckIntervall");
        String ExpResult = "{\"erreur\":\"accès refusé\"}";
        
        //Résultat
        System.out.println("resultat testGetCheckInterval3 : "+result+" | exp : "+ExpResult);
        
        //Vérification du résultat
        assertEquals(ExpResult, result);
    }
}

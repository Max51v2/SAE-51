package ServletsPC;

import DAO.DAOPC;
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
public class ListUsersWithAccessTest {
    
    /**
     * Test de récupération des utilisateurs selon leur droits d'accès à un PC
     * 
     * Conditions de test :
     *  - champs envoyés : ok
     *  - droits d'accès BD : Admin
     *  - accès au PC : oui
     */
    @Test
    public void testListUsersWithAccess1() throws IOException {
        DAOTest DAO = new DAOTest();
        DAOusers DAO2 = new DAOusers();
        DAOPC DAO3 = new DAOPC();
        POSTRequest req = new POSTRequest();
        Boolean Test = true;
        
        //Ajout d'un utilisateur ayant les droits d'admin
        String login = "Admin1";
        String nom = "Admin";
        String prenom = "Originel";
        String role = "Admin";
        String hashedPassword = "$2a$12$ckHLlD/dOl57oFJLl/n7geOh1Zvu2LtcWeV6S/3ayhO1RNJidaLg6"; // MDP = "bob"
        Integer tokenLifeCycle = 999;
        String token = "$2a$06$7sG7wu8xqfT2yZfQqrDxFOCa5kJzRxWPkRc4TYS8x0O/9Ex.rnYJq"; //token : "01010101010101010101010101010101"
        DAO.addUserWithToken(login, nom, prenom, role, hashedPassword, tokenLifeCycle, token, Test);
        
        //Ajout d'un utilisateur ayant les droits d'utilisateur
        login = "Utilisateur1";
        nom = "Utilisateur";
        prenom = "Originel";
        role = "Utilisateur";
        hashedPassword = "$2a$12$l3MjhFmfr7VGoL0uPX2VKuEmXxboZzyABhjVNqH9.TnrCD2hEvfmm"; // MDP = "leffe"
        tokenLifeCycle = 999;
        token = "$2a$08$VOtVbubOhyXhjEffToT.n.F9d8t9kwY0ulySEMKKoGZDisi4ni1s."; //token : "10101010101010101010101010101010"
        DAO.addUserWithToken(login, nom, prenom, role, hashedPassword, tokenLifeCycle, token, Test);
        
        //Ajout d'un pc
        String IP = "localhost";
        Integer id1 = 1;
        DAO3.addPC(id1, IP, "Pierre/Paul/Admin1/Jacques", Test);
        
        //JSON qui contient tous les paramètres à envoyer au servlet
        String jsonPayload = "{\"token\":\"01010101010101010101010101010101\", \"id\":\""+id1+"\", \"hasAccess\":\"true\", \"Test\":\""+Test+"\"}";
        
        //Résultat obtenu
        String result = req.doRequest("http://localhost:8080/SAE51/ListUsersWithAccess", jsonPayload);
        
        //Résultat attendu
        String ExpResult = "[{\"user\":\"Admin1\", \"canBeDeleted\":\"false\"}]";
        
        //Retrait des utilisateurs
        DAO2.deleteUser("Admin1", true);
        DAO2.deleteUser("Utilisateur1", true);
        
        //Retrait des pc
        DAO3.deletePC(1, Test);
        
        //Résultat
        System.out.println("resultat testListUsersWithAccess1 : "+result+" | exp : "+ExpResult);
        
        //Vérification du résultat
        assertEquals(ExpResult, result);
    }
    
    
    
    
    /**
     * Test de récupération des utilisateurs selon leur droits d'accès à un PC
     * 
     * Conditions de test :
     *  - champs envoyés : ok
     *  - droits d'accès BD : Admin
     *  - accès au PC : non
     */
    @Test
    public void testListUsersWithAccess2() throws IOException {
        DAOTest DAO = new DAOTest();
        DAOusers DAO2 = new DAOusers();
        DAOPC DAO3 = new DAOPC();
        POSTRequest req = new POSTRequest();
        Boolean Test = true;
        
        //Ajout d'un utilisateur ayant les droits d'admin
        String login = "Admin1";
        String nom = "Admin";
        String prenom = "Originel";
        String role = "Admin";
        String hashedPassword = "$2a$12$ckHLlD/dOl57oFJLl/n7geOh1Zvu2LtcWeV6S/3ayhO1RNJidaLg6"; // MDP = "bob"
        Integer tokenLifeCycle = 999;
        String token = "$2a$06$7sG7wu8xqfT2yZfQqrDxFOCa5kJzRxWPkRc4TYS8x0O/9Ex.rnYJq"; //token : "01010101010101010101010101010101"
        DAO.addUserWithToken(login, nom, prenom, role, hashedPassword, tokenLifeCycle, token, Test);
        
        //Ajout d'un utilisateur ayant les droits d'utilisateur
        login = "Utilisateur1";
        nom = "Utilisateur";
        prenom = "Originel";
        role = "Utilisateur";
        hashedPassword = "$2a$12$l3MjhFmfr7VGoL0uPX2VKuEmXxboZzyABhjVNqH9.TnrCD2hEvfmm"; // MDP = "leffe"
        tokenLifeCycle = 999;
        token = "$2a$08$VOtVbubOhyXhjEffToT.n.F9d8t9kwY0ulySEMKKoGZDisi4ni1s."; //token : "10101010101010101010101010101010"
        DAO.addUserWithToken(login, nom, prenom, role, hashedPassword, tokenLifeCycle, token, Test);
        
        //Ajout d'un pc
        String IP = "localhost";
        Integer id1 = 1;
        DAO3.addPC(id1, IP, "Pierre/Paul/Admin1/Jacques", Test);
        
        //JSON qui contient tous les paramètres à envoyer au servlet
        String jsonPayload = "{\"token\":\"01010101010101010101010101010101\", \"id\":\""+id1+"\", \"hasAccess\":\"false\", \"Test\":\""+Test+"\"}";
        
        //Résultat obtenu
        String result = req.doRequest("http://localhost:8080/SAE51/ListUsersWithAccess", jsonPayload);
        
        //Résultat attendu
        String ExpResult = "[{\"user\":\"Utilisateur1\", \"canBeDeleted\":\"true\"}]";
        
        //Retrait des utilisateurs
        DAO2.deleteUser("Admin1", true);
        DAO2.deleteUser("Utilisateur1", true);
        
        //Retrait des pc
        DAO3.deletePC(1, Test);
        
        //Résultat
        System.out.println("resultat testListUsersWithAccess2 : "+result+" | exp : "+ExpResult);
        
        //Vérification du résultat
        assertEquals(ExpResult, result);
    }
    
    
    
    
    /**
     * Test de récupération des utilisateurs selon leur droits d'accès à un PC
     * 
     * Conditions de test :
     *  - champs envoyés : ok
     *  - droits d'accès BD : Utilisateur
     *  - accès au PC : oui
     */
    @Test
    public void testListUsersWithAccess3() throws IOException {
        DAOTest DAO = new DAOTest();
        DAOusers DAO2 = new DAOusers();
        DAOPC DAO3 = new DAOPC();
        POSTRequest req = new POSTRequest();
        Boolean Test = true;
        
        //Ajout d'un utilisateur ayant les droits d'utilisateur
        String login = "Utilisateur1";
        String nom = "Utilisateur";
        String prenom = "Originel";
        String role = "Utilisateur";
        String hashedPassword = "$2a$12$l3MjhFmfr7VGoL0uPX2VKuEmXxboZzyABhjVNqH9.TnrCD2hEvfmm"; // MDP = "leffe"
        Integer tokenLifeCycle = 999;
        String token = "$2a$08$VOtVbubOhyXhjEffToT.n.F9d8t9kwY0ulySEMKKoGZDisi4ni1s."; //token : "10101010101010101010101010101010"
        DAO.addUserWithToken(login, nom, prenom, role, hashedPassword, tokenLifeCycle, token, Test);
        
        //Ajout d'un pc
        String IP = "localhost";
        Integer id1 = 1;
        DAO3.addPC(id1, IP, "Pierre/Paul/Admin1/Jacques", Test);
        
        //JSON qui contient tous les paramètres à envoyer au servlet
        String jsonPayload = "{\"token\":\"01010101010101010101010101010101\", \"id\":\""+id1+"\", \"hasAccess\":\"false\", \"Test\":\""+Test+"\"}";
        
        //Résultat obtenu
        String result = req.doRequest("http://localhost:8080/SAE51/ListUsersWithAccess", jsonPayload);
        
        //Résultat attendu
        String ExpResult = "{\"erreur\":\"accès refusé\"}";
        
        //Retrait des utilisateurs
        DAO2.deleteUser("Utilisateur1", true);
        
        //Retrait des pc
        DAO3.deletePC(1, Test);
        
        //Résultat
        System.out.println("resultat testListUsersWithAccess3 : "+result+" | exp : "+ExpResult);
        
        //Vérification du résultat
        assertEquals(ExpResult, result);
    }
    
    
    
    
    /**
     * Test de récupération des utilisateurs selon leur droits d'accès à un PC
     * 
     * Conditions de test :
     *  - champs envoyés : token manquant
     *  - droits d'accès BD : Utilisateur
     *  - accès au PC : oui
     */
    @Test
    public void testListUsersWithAccess4() throws IOException {
        DAOTest DAO = new DAOTest();
        DAOusers DAO2 = new DAOusers();
        DAOPC DAO3 = new DAOPC();
        POSTRequest req = new POSTRequest();
        Boolean Test = true;
        
        //Ajout d'un utilisateur ayant les droits d'utilisateur
        String login = "Utilisateur1";
        String nom = "Utilisateur";
        String prenom = "Originel";
        String role = "Utilisateur";
        String hashedPassword = "$2a$12$l3MjhFmfr7VGoL0uPX2VKuEmXxboZzyABhjVNqH9.TnrCD2hEvfmm"; // MDP = "leffe"
        Integer tokenLifeCycle = 999;
        String token = "$2a$08$VOtVbubOhyXhjEffToT.n.F9d8t9kwY0ulySEMKKoGZDisi4ni1s."; //token : "10101010101010101010101010101010"
        DAO.addUserWithToken(login, nom, prenom, role, hashedPassword, tokenLifeCycle, token, Test);
        
        //Ajout d'un pc
        String IP = "localhost";
        Integer id1 = 1;
        DAO3.addPC(id1, IP, "Pierre/Paul/Admin1/Jacques", Test);
        
        //JSON qui contient tous les paramètres à envoyer au servlet
        String jsonPayload = "{\"id\":\""+id1+"\", \"hasAccess\":\"false\", \"Test\":\""+Test+"\"}";
        
        //Résultat obtenu
        String result = req.doRequest("http://localhost:8080/SAE51/ListUsersWithAccess", jsonPayload);
        
        //Résultat attendu
        String ExpResult = "{\"erreur\":\"champ(s) manquant (req)\"}";
        
        //Retrait des utilisateurs
        DAO2.deleteUser("Utilisateur1", true);
        
        //Retrait des pc
        DAO3.deletePC(1, Test);
        
        //Résultat
        System.out.println("resultat testListUsersWithAccess4 : "+result+" | exp : "+ExpResult);
        
        //Vérification du résultat
        assertEquals(ExpResult, result);
    }
}

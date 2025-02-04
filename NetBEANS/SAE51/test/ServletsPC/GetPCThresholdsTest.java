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
 */
public class GetPCThresholdsTest {
    
    /**
     * Test du listage des infos statiques d'un pc
     * 
     * Conditions de test :
     *  - champs envoyés : ok
     *  - droits d'accès : Admin
     *  - données dans la DB : oui
     */
    @Test
    public void GetPCThresholdsTest1() throws IOException {
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
        String hashedPassword = "$2a$12$l3MjhFmfr7VGoL0uPX2VKuEmXxboZzyABhjVNqH9.TnrCD2hEvfmm"; // MDP = "leffe"
        Integer tokenLifeCycle = 999;
        String token = "$2a$08$VOtVbubOhyXhjEffToT.n.F9d8t9kwY0ulySEMKKoGZDisi4ni1s."; //token : "10101010101010101010101010101010"
        DAO.addUserWithToken(login, nom, prenom, role, hashedPassword, tokenLifeCycle, token, Test);
        
        //Ajout des pc
        String IP = "localhost";
        Integer id = 1;
        DAO3.addPC(id, IP, "Pierre/Paul/Admin1/Jacques", Test);
        
        //Ajout seuils
        Integer CPUUtilization = 2;
        Integer CPUTemp = 3;
        Integer CPUConsumption = 4;
        Integer RAMUtilization = 5;
        Integer storageLoad = 6;
        Integer storageLeft = 7;
        Integer storageTemp = 8;
        Integer storageErrors = 9;
        Integer networkLatency = 10;
        Integer networkBandwith = 11;
        Integer fanSpeed = 12;

        DAO3.addThresholds(id, CPUUtilization, CPUTemp, CPUConsumption, RAMUtilization, storageLoad, storageLeft, storageTemp, storageErrors, networkLatency, networkBandwith, fanSpeed, Test);
        
        //JSON qui contient tous les paramètres à envoyer au servlet
        String jsonPayload = "{\"token\":\"10101010101010101010101010101010\", \"id\":\""+id+"\", \"Test\":\""+Test+"\"}";
        
        //Résultat obtenu
        String result = req.doRequest("http://localhost:8080/SAE51/GetPCThresholds", jsonPayload);
        
        //Résultat attendu
        String ExpResult = "{\"erreur\":\"none\",\"CPUUtilization\":\"2\",\"CPUTemp\":\"3\",\"CPUConsumption\":\"4\",\"RAMUtilization\":\"5\",\"storageLoad\":\"6\",\"storageLeft\":\"7\",\"storageTemp\":\"8\",\"storageErrors\":\"9\",\"networkLatency\":\"10\",\"networkBandwith\":\"11\",\"fanSpeed\":\"12\"}";
        
        //Retrait des utilisateurs
        DAO2.deleteUser("Admin1", Test);
        
        //Retrait des pc
        DAO3.deletePC(1, Test);
        
        //Résultat
        System.out.println("resultat GetPCThresholdsTest1 : "+result+" | exp : "+ExpResult);
        
        //Vérification du résultat
        assertEquals(ExpResult, result);
    }
    
    
    
    /**
     * Test du listage des infos statiques d'un pc
     * 
     * Conditions de test :
     *  - champs envoyés : non
     *  - droits d'accès : Admin
     *  - données dans la DB : oui
     */
    @Test
    public void GetPCThresholdsTest2() throws IOException {
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
        String hashedPassword = "$2a$12$l3MjhFmfr7VGoL0uPX2VKuEmXxboZzyABhjVNqH9.TnrCD2hEvfmm"; // MDP = "leffe"
        Integer tokenLifeCycle = 999;
        String token = "$2a$08$VOtVbubOhyXhjEffToT.n.F9d8t9kwY0ulySEMKKoGZDisi4ni1s."; //token : "10101010101010101010101010101010"
        DAO.addUserWithToken(login, nom, prenom, role, hashedPassword, tokenLifeCycle, token, Test);
        
        //Ajout des pc
        String IP = "localhost";
        Integer id = 1;
        DAO3.addPC(id, IP, "Pierre/Paul/Admin1/Jacques", Test);
        
        //Ajout seuils
        Integer CPUUtilization = 2;
        Integer CPUTemp = 3;
        Integer CPUConsumption = 4;
        Integer RAMUtilization = 5;
        Integer storageLoad = 6;
        Integer storageLeft = 7;
        Integer storageTemp = 8;
        Integer storageErrors = 9;
        Integer networkLatency = 10;
        Integer networkBandwith = 11;
        Integer fanSpeed = 12;

        DAO3.addThresholds(id, CPUUtilization, CPUTemp, CPUConsumption, RAMUtilization, storageLoad, storageLeft, storageTemp, storageErrors, networkLatency, networkBandwith, fanSpeed, Test);
        
        //JSON qui contient tous les paramètres à envoyer au servlet
        String jsonPayload = "{\"token\":\"10101010101010101010101010101010\", \"Test\":\""+Test+"\"}";
        
        //Résultat obtenu
        String result = req.doRequest("http://localhost:8080/SAE51/GetPCThresholds", jsonPayload);
        
        //Résultat attendu
        String ExpResult = "{\"erreur\":\"champ(s) manquant (req)\"}";
        
        //Retrait des utilisateurs
        DAO2.deleteUser("Admin1", Test);
        
        //Retrait des pc
        DAO3.deletePC(1, Test);
        
        //Résultat
        System.out.println("resultat GetPCThresholdsTest2 : "+result+" | exp : "+ExpResult);
        
        //Vérification du résultat
        assertEquals(ExpResult, result);
    }
    
    
    
    
    /**
     * Test du listage des infos statiques d'un pc
     * 
     * Conditions de test :
     *  - champs envoyés : ok
     *  - droits d'accès : Utilisateur (pas d'accès au pc dans ce cas)
     *  - données dans la DB : oui
     */
    @Test
    public void GetPCThresholdsTest3() throws IOException {
        DAOTest DAO = new DAOTest();
        DAOusers DAO2 = new DAOusers();
        DAOPC DAO3 = new DAOPC();
        POSTRequest req = new POSTRequest();
        Boolean Test = true;
        
        //Ajout d'un utilisateur ayant les droits d'admin
        String login = "Admin1";
        String nom = "Admin";
        String prenom = "Originel";
        String role = "Utilisateur";
        String hashedPassword = "$2a$12$l3MjhFmfr7VGoL0uPX2VKuEmXxboZzyABhjVNqH9.TnrCD2hEvfmm"; // MDP = "leffe"
        Integer tokenLifeCycle = 999;
        String token = "$2a$08$VOtVbubOhyXhjEffToT.n.F9d8t9kwY0ulySEMKKoGZDisi4ni1s."; //token : "10101010101010101010101010101010"
        DAO.addUserWithToken(login, nom, prenom, role, hashedPassword, tokenLifeCycle, token, Test);
        
        //Ajout des pc
        String IP = "localhost";
        Integer id = 1;
        DAO3.addPC(id, IP, "Pierre/Paul/Jacques", Test);
        
        //Ajout seuils
        Integer CPUUtilization = 2;
        Integer CPUTemp = 3;
        Integer CPUConsumption = 4;
        Integer RAMUtilization = 5;
        Integer storageLoad = 6;
        Integer storageLeft = 7;
        Integer storageTemp = 8;
        Integer storageErrors = 9;
        Integer networkLatency = 10;
        Integer networkBandwith = 11;
        Integer fanSpeed = 12;

        DAO3.addThresholds(id, CPUUtilization, CPUTemp, CPUConsumption, RAMUtilization, storageLoad, storageLeft, storageTemp, storageErrors, networkLatency, networkBandwith, fanSpeed, Test);
        
        //JSON qui contient tous les paramètres à envoyer au servlet
        String jsonPayload = "{\"token\":\"10101010101010101010101010101010\", \"id\":\""+id+"\", \"Test\":\""+Test+"\"}";
        
        //Résultat obtenu
        String result = req.doRequest("http://localhost:8080/SAE51/GetPCThresholds", jsonPayload);
        
        //Résultat attendu
        String ExpResult = "{\"erreur\":\"accès refusé\"}";
        
        //Retrait des utilisateurs
        DAO2.deleteUser("Admin1", Test);
        
        //Retrait des pc
        DAO3.deletePC(1, Test);
        
        //Résultat
        System.out.println("resultat GetPCThresholdsTest3 : "+result+" | exp : "+ExpResult);
        
        //Vérification du résultat
        assertEquals(ExpResult, result);
    }
    
    
    
    /**
     * Test du listage des infos statiques d'un pc
     * 
     * Conditions de test :
     *  - champs envoyés : ok
     *  - droits d'accès : none
     *  - données dans la DB : oui
     */
    @Test
    public void GetPCThresholdsTest4() throws IOException {
        DAOTest DAO = new DAOTest();
        DAOusers DAO2 = new DAOusers();
        DAOPC DAO3 = new DAOPC();
        POSTRequest req = new POSTRequest();
        Boolean Test = true;
        
        //Ajout d'un utilisateur ayant les droits d'admin
        String login = "Admin1";
        String nom = "Admin";
        String prenom = "Originel";
        String role = "none";
        String hashedPassword = "$2a$12$l3MjhFmfr7VGoL0uPX2VKuEmXxboZzyABhjVNqH9.TnrCD2hEvfmm"; // MDP = "leffe"
        Integer tokenLifeCycle = 999;
        String token = "$2a$08$VOtVbubOhyXhjEffToT.n.F9d8t9kwY0ulySEMKKoGZDisi4ni1s."; //token : "10101010101010101010101010101010"
        DAO.addUserWithToken(login, nom, prenom, role, hashedPassword, tokenLifeCycle, token, Test);
        
        //Ajout des pc
        String IP = "localhost";
        Integer id = 1;
        DAO3.addPC(id, IP, "Pierre/Paul/Admin1/Jacques", Test);
        
        //Ajout seuils
        Integer CPUUtilization = 2;
        Integer CPUTemp = 3;
        Integer CPUConsumption = 4;
        Integer RAMUtilization = 5;
        Integer storageLoad = 6;
        Integer storageLeft = 7;
        Integer storageTemp = 8;
        Integer storageErrors = 9;
        Integer networkLatency = 10;
        Integer networkBandwith = 11;
        Integer fanSpeed = 12;

        DAO3.addThresholds(id, CPUUtilization, CPUTemp, CPUConsumption, RAMUtilization, storageLoad, storageLeft, storageTemp, storageErrors, networkLatency, networkBandwith, fanSpeed, Test);
        
        //JSON qui contient tous les paramètres à envoyer au servlet
        String jsonPayload = "{\"token\":\"10101010101010101010101010101010\", \"id\":\""+id+"\", \"Test\":\""+Test+"\"}";
        
        //Résultat obtenu
        String result = req.doRequest("http://localhost:8080/SAE51/GetPCThresholds", jsonPayload);
        
        //Résultat attendu
        String ExpResult = "{\"erreur\":\"accès refusé\"}";
        
        //Retrait des utilisateurs
        DAO2.deleteUser("Admin1", Test);
        
        //Retrait des pc
        DAO3.deletePC(1, Test);
        
        //Résultat
        System.out.println("resultat GetPCThresholdsTest4 : "+result+" | exp : "+ExpResult);
        
        //Vérification du résultat
        assertEquals(ExpResult, result);
    }
    
    
    
    
    /**
     * Test du listage des infos statiques d'un pc
     * 
     * Conditions de test :
     *  - champs envoyés : ok
     *  - droits d'accès : Admin
     *  - données dans la DB : non
     */
    @Test
    public void GetPCThresholdsTest5() throws IOException {
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
        String hashedPassword = "$2a$12$l3MjhFmfr7VGoL0uPX2VKuEmXxboZzyABhjVNqH9.TnrCD2hEvfmm"; // MDP = "leffe"
        Integer tokenLifeCycle = 999;
        String token = "$2a$08$VOtVbubOhyXhjEffToT.n.F9d8t9kwY0ulySEMKKoGZDisi4ni1s."; //token : "10101010101010101010101010101010"
        DAO.addUserWithToken(login, nom, prenom, role, hashedPassword, tokenLifeCycle, token, Test);
        
        //Ajout des pc
        String IP = "localhost";
        Integer id = 1;
        DAO3.addPC(id, IP, "Pierre/Paul/Admin1/Jacques", Test);
        
        //Ajout seuils
        Integer CPUUtilization = 2;
        Integer CPUTemp = 3;
        Integer CPUConsumption = 4;
        Integer RAMUtilization = 5;
        Integer storageLoad = 6;
        Integer storageLeft = 7;
        Integer storageTemp = 8;
        Integer storageErrors = 9;
        Integer networkLatency = 10;
        Integer networkBandwith = 11;
        Integer fanSpeed = 12;

        DAO3.addThresholds(id, CPUUtilization, CPUTemp, CPUConsumption, RAMUtilization, storageLoad, storageLeft, storageTemp, storageErrors, networkLatency, networkBandwith, fanSpeed, Test);
        DAO3.deleteDuplicateThresholds(id, Test);
        
        //JSON qui contient tous les paramètres à envoyer au servlet
        String jsonPayload = "{\"token\":\"10101010101010101010101010101010\", \"id\":\""+id+"\", \"Test\":\""+Test+"\"}";
        
        //Résultat obtenu
        String result = req.doRequest("http://localhost:8080/SAE51/GetPCThresholds", jsonPayload);
        
        //Résultat attendu
        String ExpResult = "{\"erreur\":\"Pas d'informations dans la table\"}";
        
        //Retrait des utilisateurs
        DAO2.deleteUser("Admin1", Test);
        
        //Retrait des pc
        DAO3.deletePC(1, Test);
        
        //Résultat
        System.out.println("resultat GetPCThresholdsTest5 : "+result+" | exp : "+ExpResult);
        
        //Vérification du résultat
        assertEquals(ExpResult, result);
    }
}

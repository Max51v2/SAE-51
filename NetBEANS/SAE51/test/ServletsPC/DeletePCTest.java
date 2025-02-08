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
public class DeletePCTest {
    
    /**
     * Test de suppression d'un pc
     * 
     * Conditions de test :
     *  - champs envoyés : ok
     *  - droits d'accès : Admin
     */
    @Test
    public void DeletePCTest1() throws IOException {
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
        
        //Ajout du pc
        String IP = "localhost";
        Integer id = 123456789;
        DAO3.addPC(id, IP, "", Test);
        
        //Ajout des infos statiques du pc
        String cpu_model = "Intel potato inside";
        Integer cores = 2;
        Integer threads = 2;
        String maximum_frequency = "2 GHz";
        String ram_quantity = "4Go";
        Integer dimm_quantity = 1;
        String dimm_speed = "2400MT/s";
        Integer storage_device_number = 1;
        String storage_space = "128Go";
        Integer network_int_number = 1;
        String network_int_speed = "100Mb/S";
        String os = "Windows 11";
        String version = "123456.1234";
        DAO3.addPCStaticInfo(id, cpu_model, cores, threads, maximum_frequency, ram_quantity, dimm_quantity, dimm_speed, storage_device_number, storage_space, network_int_number, network_int_speed, os, version, Test);
        
        //JSON qui contient tous les paramètres à envoyer au servlet
        String jsonPayload = "{\"id\":\""+id+"\", \"token\":\"10101010101010101010101010101010\", \"test\":\""+Test+"\"}";
        
        //Requête au servlet
        req.doRequest("http://localhost:8080/SAE51/DeletePC", jsonPayload);
        
        //Résultat obtenu
        String result1 = DAO.getAllPC(Test);
        String result2 = DAO.getPCStaticInfo(Test);
        String result = "";
        
        //Résultat attendu
        String ExpResult1 = "[]";
        String ExpResult2 = "[]";
        String ExpResult = "OK";
        
        if(result1.equals(ExpResult1) & result2.equals(ExpResult2)){
            result = "OK";
        }
        
        //Retrait des utilisateurs
        DAO2.deleteUser("Admin1", true);
        
        //Résultat
        System.out.println("resultat DeletePCTest1 : "+result+" | exp : "+ExpResult);
        
        //Vérification du résultat
        assertEquals(ExpResult, result);
    }
    
    
    
    /**
     * Test de suppression d'un pc
     * 
     * Conditions de test :
     *  - champs envoyés : token inexistant
     *  - droits d'accès : Admin
     */
    @Test
    public void DeletePCTest2() throws IOException {
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
        
        //Ajout du pc
        String IP = "localhost";
        Integer id = 123456789;
        DAO3.addPC(id, IP, "", Test);
        
        //Ajout des infos statiques du pc
        String cpu_model = "Intel potato inside";
        Integer cores = 2;
        Integer threads = 2;
        String maximum_frequency = "2 GHz";
        String ram_quantity = "4Go";
        Integer dimm_quantity = 1;
        String dimm_speed = "2400MT/s";
        Integer storage_device_number = 1;
        String storage_space = "128Go";
        Integer network_int_number = 1;
        String network_int_speed = "100Mb/S";
        String os = "Windows 11";
        String version = "123456.1234";
        DAO3.addPCStaticInfo(id, cpu_model, cores, threads, maximum_frequency, ram_quantity, dimm_quantity, dimm_speed, storage_device_number, storage_space, network_int_number, network_int_speed, os, version, Test);
        
        //JSON qui contient tous les paramètres à envoyer au servlet
        String jsonPayload = "{\"id\":\""+id+"\", \"test\":\""+Test+"\"}";
        
        //Requête au servlet
        String result = req.doRequest("http://localhost:8080/SAE51/DeletePC", jsonPayload);
        
        //Résultat attendu
        String ExpResult = "{\"erreur\":\"champ(s) manquant (req)\"}";
        
        //Retrait des utilisateurs
        DAO2.deleteUser("Admin1", true);
        
        //Retrait de l'ordinateur
        DAO3.deletePC(id, Test);
        
        //Résultat
        System.out.println("resultat testDeletePCTest2 : "+result+" | exp : "+ExpResult);
        
        //Vérification du résultat
        assertEquals(ExpResult, result);
    }
    
    
    
    /**
     * Test de suppression d'un pc
     * 
     * Conditions de test :
     *  - champs envoyés : token inexistant
     *  - droits d'accès : Admin
     */
    @Test
    public void DeletePCTest3() throws IOException {
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
        
        //Ajout du pc
        String IP = "localhost";
        Integer id = 123456789;
        DAO3.addPC(id, IP, "", Test);
        
        //Ajout des infos statiques du pc
        String cpu_model = "Intel potato inside";
        Integer cores = 2;
        Integer threads = 2;
        String maximum_frequency = "2 GHz";
        String ram_quantity = "4Go";
        Integer dimm_quantity = 1;
        String dimm_speed = "2400MT/s";
        Integer storage_device_number = 1;
        String storage_space = "128Go";
        Integer network_int_number = 1;
        String network_int_speed = "100Mb/S";
        String os = "Windows 11";
        String version = "123456.1234";
        DAO3.addPCStaticInfo(id, cpu_model, cores, threads, maximum_frequency, ram_quantity, dimm_quantity, dimm_speed, storage_device_number, storage_space, network_int_number, network_int_speed, os, version, Test);
        
        //JSON qui contient tous les paramètres à envoyer au servlet
        String jsonPayload = "{\"id\":\""+id+"\", \"token\":\"10101010101010101010101010101010\", \"test\":\""+Test+"\"}";
        
        //Requête au servlet
        String result = req.doRequest("http://localhost:8080/SAE51/DeletePC", jsonPayload);
        
        //Résultat attendu
        String ExpResult = "{\"erreur\":\"accès refusé\"}";
        
        //Retrait des utilisateurs
        DAO2.deleteUser("Admin1", true);
        
        //Retrait de l'ordinateur
        DAO3.deletePC(id, Test);
        
        //Résultat
        System.out.println("resultat DeletePCTest3 : "+result+" | exp : "+ExpResult);
        
        //Vérification du résultat
        assertEquals(ExpResult, result);
    }
}

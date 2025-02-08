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
public class ListPCTest {
    
    /**
     * Test du listage des pc
     * 
     * Conditions de test :
     *  - champs envoyés : ok
     *  - droits d'accès : Admin
     */
    @Test
    public void testListPC1() throws IOException {
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
        Integer id1 = 123456789;
        DAO3.addPC(id1, IP, "Pierre/Paul/Admin1/Jacques", Test);
        Integer id2 = 987654321;
        DAO3.addPC(id2, IP, "Pierre/Paul/Admin1/Jacques", Test);
        
        //Ajout des infos statiques des pc
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
        DAO3.addPCStaticInfo(id1, cpu_model, cores, threads, maximum_frequency, ram_quantity, dimm_quantity, dimm_speed, storage_device_number, storage_space, network_int_number, network_int_speed, os, version, Test);
        cpu_model = "Intel potato inside 2";
        threads = 4;
        DAO3.addPCStaticInfo(id2, cpu_model, cores, threads, maximum_frequency, ram_quantity, dimm_quantity, dimm_speed, storage_device_number, storage_space, network_int_number, network_int_speed, os, version, Test);
        
        //JSON qui contient tous les paramètres à envoyer au servlet
        String jsonPayload = "{\"token\":\"10101010101010101010101010101010\", \"test\":\""+Test+"\"}";
        
        //Résultat obtenu
        String result = req.doRequest("http://localhost:8080/SAE51/ListPC", jsonPayload);
        
        //Résultat attendu
        String ExpResult = "[{\"IP\":\"localhost\", \"id\":\"123456789\"},{\"IP\":\"localhost\", \"id\":\"987654321\"}]";
        
        //Retrait des utilisateurs
        DAO2.deleteUser("Admin1", true);
        
        //Retrait des pc
        DAO3.deletePC(123456789, Test);
        DAO3.deletePC(987654321, Test);
        
        //Résultat
        System.out.println("resultat ListPCTest1 : "+result+" | exp : "+ExpResult);
        
        //Vérification du résultat
        assertEquals(ExpResult, result);
    }
    
    
    /**
     * Test du listage des pc
     * 
     * Conditions de test :
     *  - champs envoyés : ok
     *  - droits d'accès : Utilisateur (autorisé à accéder à 1 machine)
     */
    @Test
    public void testListPC2() throws IOException {
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
        Integer id1 = 123456789;
        DAO3.addPC(id1, IP, "Pierre/Paul/Admin1/Jacques", Test);
        Integer id2 = 987654321;
        DAO3.addPC(id2, IP, "Pierre/Paul/Jacques", Test);
        
        //Ajout des infos statiques des pc
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
        DAO3.addPCStaticInfo(id1, cpu_model, cores, threads, maximum_frequency, ram_quantity, dimm_quantity, dimm_speed, storage_device_number, storage_space, network_int_number, network_int_speed, os, version, Test);
        cpu_model = "Intel potato inside 2";
        threads = 4;
        DAO3.addPCStaticInfo(id2, cpu_model, cores, threads, maximum_frequency, ram_quantity, dimm_quantity, dimm_speed, storage_device_number, storage_space, network_int_number, network_int_speed, os, version, Test);
        
        //JSON qui contient tous les paramètres à envoyer au servlet
        String jsonPayload = "{\"token\":\"10101010101010101010101010101010\", \"test\":\""+Test+"\"}";
        
        //Résultat obtenu
        String result = req.doRequest("http://localhost:8080/SAE51/ListPC", jsonPayload);
        
        //Résultat attendu
        String ExpResult = "[{\"IP\":\"localhost\", \"id\":\"123456789\"}]";
        
        //Retrait des utilisateurs
        DAO2.deleteUser("Admin1", true);
        
        //Retrait des pc
        DAO3.deletePC(123456789, Test);
        DAO3.deletePC(987654321, Test);
        
        //Résultat
        System.out.println("resultat testListPC2 : "+result+" | exp : "+ExpResult);
        
        //Vérification du résultat
        assertEquals(ExpResult, result);
    }
    
    
    
    /**
     * Test du listage des pc
     * 
     * Conditions de test :
     *  - champs envoyés : ok
     *  - droits d'accès : Utilisateur (autorisé à accéder à 0 machine)
     */
    @Test
    public void testListPC3() throws IOException {
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
        Integer id1 =123456789;
        DAO3.addPC(id1, IP, "Pierre/Paul/Jacques", Test);
        Integer id2 = 987654321;
        DAO3.addPC(id2, IP, "Pierre/Paul/Jacques", Test);
        
        //Ajout des infos statiques des pc
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
        DAO3.addPCStaticInfo(id1, cpu_model, cores, threads, maximum_frequency, ram_quantity, dimm_quantity, dimm_speed, storage_device_number, storage_space, network_int_number, network_int_speed, os, version, Test);
        cpu_model = "Intel potato inside 2";
        threads = 4;
        DAO3.addPCStaticInfo(id2, cpu_model, cores, threads, maximum_frequency, ram_quantity, dimm_quantity, dimm_speed, storage_device_number, storage_space, network_int_number, network_int_speed, os, version, Test);
        
        //JSON qui contient tous les paramètres à envoyer au servlet
        String jsonPayload = "{\"token\":\"10101010101010101010101010101010\", \"test\":\""+Test+"\"}";
        
        //Résultat obtenu
        String result = req.doRequest("http://localhost:8080/SAE51/ListPC", jsonPayload);
        
        //Résultat attendu
        String ExpResult = "[]";
        
        //Retrait des utilisateurs
        DAO2.deleteUser("Admin1", true);
        
        //Retrait des pc
        DAO3.deletePC(123456789, Test);
        DAO3.deletePC(987654321, Test);
        
        //Résultat
        System.out.println("resultat testListPC3 : "+result+" | exp : "+ExpResult);
        
        //Vérification du résultat
        assertEquals(ExpResult, result);
    }
    
    
    
    /**
     * Test du listage des pc
     * 
     * Conditions de test :
     *  - champs envoyés : ok
     *  - droits d'accès : Aucun
     */
    @Test
    public void testListPC4() throws IOException {
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
        Integer id1 = 123456789;
        DAO3.addPC(id1, IP, "Pierre/Paul/Jacques", Test);
        Integer id2 = 987654321;
        DAO3.addPC(id2, IP, "Pierre/Paul/Jacques", Test);
        
        //Ajout des infos statiques des pc
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
        DAO3.addPCStaticInfo(id1, cpu_model, cores, threads, maximum_frequency, ram_quantity, dimm_quantity, dimm_speed, storage_device_number, storage_space, network_int_number, network_int_speed, os, version, Test);
        cpu_model = "Intel potato inside 2";
        threads = 4;
        DAO3.addPCStaticInfo(id2, cpu_model, cores, threads, maximum_frequency, ram_quantity, dimm_quantity, dimm_speed, storage_device_number, storage_space, network_int_number, network_int_speed, os, version, Test);
        
        //JSON qui contient tous les paramètres à envoyer au servlet
        String jsonPayload = "{\"token\":\"10101010101010101010101010101010\", \"test\":\""+Test+"\"}";
        
        //Résultat obtenu
        String result = req.doRequest("http://localhost:8080/SAE51/ListPC", jsonPayload);
        
        //Résultat attendu
        String ExpResult = "{\"erreur\":\"accès refusé\"}";
        
        //Retrait des utilisateurs
        DAO2.deleteUser("Admin1", true);
        
        //Retrait des pc
        DAO3.deletePC(123456789, Test);
        DAO3.deletePC(987654321, Test);
        
        //Résultat
        System.out.println("resultat testListPC4 : "+result+" | exp : "+ExpResult);
        
        //Vérification du résultat
        assertEquals(ExpResult, result);
    }
}

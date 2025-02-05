package DAO;

import Tests.DAOTest;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Maxime VALLET
 * @version 1.0
 */
public class DAONotificationsTest {
    DAOusers DAO = new DAOusers();
    DAONotifications DAO2 = new DAONotifications();
    DAOTest DAOtest = new DAOTest();
    
    
    /**
     * Test of AddNotification
     */
    @Test
    public void testAddNotification1() {
        
        //Ajout d'un utilisateur ayant les droits d'admin
        String login = "Admin1";
        String nom = "Admin";
        String prenom = "Originel";
        String role = "Admin";
        String hashedPassword = "$2a$12$l3MjhFmfr7VGoL0uPX2VKuEmXxboZzyABhjVNqH9.TnrCD2hEvfmm"; // MDP = "leffe"
        Integer tokenLifeCycle = 999;
        Boolean Test = true;
        String token = "$2a$08$VOtVbubOhyXhjEffToT.n.F9d8t9kwY0ulySEMKKoGZDisi4ni1s."; //token : "10101010101010101010101010101010"
        DAOtest.addUserWithToken(login, nom, prenom, role, hashedPassword, tokenLifeCycle, token, Test);
        
        //Supression des notifs
        DAO2.deleteNotifications(Test);
        
        //Ajout de notifications
        DAO2.addNotification("test1", "test content 1", "Hell Wallker/Admin1",1, Test);
        DAO2.addNotification("test2", "test content 2", "Hell Wallker",1, Test);
        
        //Récupération des notifications
        String result = DAO2.getNotifications(login, false, Test);
        
        //Suppression de l'utilisateur
        DAO.deleteUser(login, Test);
        
        //Résultat attendu
        String ExpResult = "[{\"description\":\"test1\", \"content\":\"test content 1\"}]";
        
        //Résultat
        System.out.println("resultat testAddNotification1 : "+result+" | exp : "+ExpResult);
        
        //Vérification du résultat
        assertEquals(ExpResult, result);
    }
    
    
    
    /**
     * Test of AddNotification
     */
    @Test
    public void testAddNotification2() {
        
        //Ajout d'un utilisateur ayant les droits d'admin
        String login = "Admin1";
        String nom = "Admin";
        String prenom = "Originel";
        String role = "Admin";
        String hashedPassword = "$2a$12$l3MjhFmfr7VGoL0uPX2VKuEmXxboZzyABhjVNqH9.TnrCD2hEvfmm"; // MDP = "leffe"
        Integer tokenLifeCycle = 999;
        Boolean Test = true;
        String token = "$2a$08$VOtVbubOhyXhjEffToT.n.F9d8t9kwY0ulySEMKKoGZDisi4ni1s."; //token : "10101010101010101010101010101010"
        DAOtest.addUserWithToken(login, nom, prenom, role, hashedPassword, tokenLifeCycle, token, Test);
        
        //Supression des notifs
        DAO2.deleteNotifications(Test);
        
        //Ajout de notifications
        DAO2.addNotification("test1", "test content 1", "Hell Wallker/Admin1",1, Test);
        DAO2.addNotification("test2", "test content 2", "Hell Wallker",1, Test);
        
        //Récupération des notifications
        String result = DAO2.getNotifications(login, true, Test);
        
        //Suppression de l'utilisateur
        DAO.deleteUser(login, Test);
        
        //Résultat attendu
        String ExpResult = "[{\"description\":\"test1\", \"content\":\"test content 1\"},{\"description\":\"test2\", \"content\":\"test content 2\"}]";
        
        //Résultat
        System.out.println("resultat testAddNotification2 : "+result+" | exp : "+ExpResult);
        
        //Vérification du résultat
        assertEquals(ExpResult, result);
    }
    
    
    
    /**
     * Test of AddNotification
     */
    @Test
    public void testAddNotification3() {
        
        //Ajout d'un utilisateur ayant les droits d'admin
        String login = "Admin1";
        String nom = "Admin";
        String prenom = "Originel";
        String role = "Admin";
        String hashedPassword = "$2a$12$l3MjhFmfr7VGoL0uPX2VKuEmXxboZzyABhjVNqH9.TnrCD2hEvfmm"; // MDP = "leffe"
        Integer tokenLifeCycle = 999;
        Boolean Test = true;
        String token = "$2a$08$VOtVbubOhyXhjEffToT.n.F9d8t9kwY0ulySEMKKoGZDisi4ni1s."; //token : "10101010101010101010101010101010"
        DAOtest.addUserWithToken(login, nom, prenom, role, hashedPassword, tokenLifeCycle, token, Test);
        
        //Supression des notifs
        DAO2.deleteNotifications(Test);
        
        //Ajout de notifications
        DAO2.addNotification("test1", "test content 1", "Hell Wallker",1, Test);
        DAO2.addNotification("test2", "test content 2", "Hell Wallker",1, Test);
        
        //Récupération des notifications
        String result = DAO2.getNotifications(login, false, Test);
        
        //Suppression de l'utilisateur
        DAO.deleteUser(login, Test);
        
        //Résultat attendu
        String ExpResult = "[]";
        
        //Résultat
        System.out.println("resultat testAddNotification3 : "+result+" | exp : "+ExpResult);
        
        //Vérification du résultat
        assertEquals(ExpResult, result);
    }
}

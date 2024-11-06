package Autre;

import DAO.DAOusers;
import Tests.DAOTest;
import static org.junit.Assert.assertEquals;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 *
 * @author Maxime VALLET
 * @version 1.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //Lancement des méthodes dans l'ordre
public class TokenExpirationTest {
    
    TokenExpiration run = new TokenExpiration();
    DAOTest DAO = new DAOTest();
    DAOusers DAO2 = new DAOusers();
   

    /**
     * Vérification de l'expiration des tokens
     * @throws java.lang.InterruptedException
     */
    @Test
    public void testTokenExpiration1() throws InterruptedException {
        
        //Ajout d'un utilisateur
        String login = "Admin1";
        String nom = "Admin";
        String prenom = "Originel";
        String role = "Admin";
        String hashedPassword = "$2a$12$l3MjhFmfr7VGoL0uPX2VKuEmXxboZzyABhjVNqH9.TnrCD2hEvfmm"; // MDP = "leffe"
        Integer tokenLifeCycle = 10;
        String token = "$2a$08$VOtVbubOhyXhjEffToT.n.F9d8t9kwY0ulySEMKKoGZDisi4ni1s."; //token : "10101010101010101010101010101010"
        DAO.addUserWithToken(login, nom, prenom, role, hashedPassword, tokenLifeCycle, token, true);
        
        //Ajout d'un utilisateur
        login = "Utilisateur1";
        nom = "Utilisateur";
        prenom = "Originel";
        role = "Utilisateur";
        hashedPassword = "$2a$12$l3MjhFmfr7VGoL0uPX2VKuEmXxboZzyABhjVNqH9.TnrCD2hEvfmm"; // MDP = "leffe"
        tokenLifeCycle = 3;
        token = "$2a$08$VOtVbubOhyXhjEffToT.n.F9d8t9kwY0ulySEMKKoGZDisi4ni1s."; //token : "10101010101010101010101010101010"
        DAO.addUserWithToken(login, nom, prenom, role, hashedPassword, tokenLifeCycle, token, true);
        
        //Lancement de la vérification des tokens
        run.start(500, true);
        
        //Pause
        Thread.sleep(1750);
        
        //Arrêt de la vérification des tokens
        run.stop();
        
        //Récupération de la durée de vie des token des utilisateurs
        Integer result1 = DAO2.getTokenLifeCycle("Admin1", true);
        Integer result2 = DAO2.getTokenLifeCycle("Utilisateur1", true);
        
        //récupération du token de l'utilisateur qui a vu son token invalidé
        String result3 = DAO.getToken("Utilisateur1", true);
        
        //Vérification des résultats
        String result = "";
        if(result1 == 7 & result2 == 0 & result3.equals("")){
            result = "OK";
        }
        else{
            result = "erreur";
        }
        
        String ExpResult = "OK";
        
        //Suppression des utilisateurs
        DAO2.deleteUser("Utilisateur1", true);
        DAO2.deleteUser("Admin1", true);
        
        //Résultat
        System.out.println("resultat testTokenExpiration1 : "+result+" | exp : "+ExpResult);
        
        //Vérification du résultat
        assertEquals(ExpResult, result);
    }
    
    
    
    
    /**
     * Vérification de l'expiration des tokens
     * @throws java.lang.InterruptedException
     */
    @Test
    public void testTokenExpiration2() throws InterruptedException {
        
        //Ajout d'un utilisateur
        String login = "Admin1";
        String nom = "Admin";
        String prenom = "Originel";
        String role = "Admin";
        String hashedPassword = "$2a$12$l3MjhFmfr7VGoL0uPX2VKuEmXxboZzyABhjVNqH9.TnrCD2hEvfmm"; // MDP = "leffe"
        Integer tokenLifeCycle = 10;
        String token = "$2a$08$VOtVbubOhyXhjEffToT.n.F9d8t9kwY0ulySEMKKoGZDisi4ni1s."; //token : "10101010101010101010101010101010"
        DAO.addUserWithToken(login, nom, prenom, role, hashedPassword, tokenLifeCycle, token, true);
        
        //Lancement de la vérification des tokens
        run.start(500, true);
        
        //Pause
        Thread.sleep(1750);
        
        //Arrêt de la vérification des tokens
        run.stop();
        
        //Vérification du token (vérification reset cycle de vie du token
        DAO2.checkToken("10101010101010101010101010101010", true);
        
        //Récupération de la durée de vie des token de l'utilisateur
        Integer result1 = DAO2.getTokenLifeCycle(login, true);
        
        
        //Vérification des résultats
        String result = "";
        if(result1 == 24 ){
            result = "OK";
        }
        else{
            result = "erreur";
        }
        
        String ExpResult = "OK";
        
        //Suppression des utilisateurs
        DAO2.deleteUser(login, true);
        
        //Résultat
        System.out.println("resultat testTokenExpiration2 : "+result+" | exp : "+ExpResult);
        
        //Vérification du résultat
        assertEquals(ExpResult, result);
    }
}

package TCP_Server;

import DAO.DAOPC;
import Tests.TCP_Client;
import java.io.IOException;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 *
 * @author Maxime VALLET
 * @version 1.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //Lancement des méthodes dans l'ordre
public class AnswerPingTest {
    AnswerPing server = new AnswerPing();
    TCP_Client client = new TCP_Client("localhost", 5555);
    DAOPC DAO = new DAOPC();

    
     @Before
    public void setUp() throws IOException, InterruptedException {
        //Démarrage du serveur TCP
        server.start(5555);
        
        //Attente du lancement du serveur
        Thread.sleep(500); 
    }

    @After
    public void tearDown() throws InterruptedException {
        //Arrêt du serveur
        server.stop();
    }
    
    
    /**
     * Vérification du fonctionnement de l'enregistrement des clients
     * @throws java.io.IOException
     */
    @Test
    public void testAddClient1() throws IOException {
        
        //Requête AnswerPing
        String jsonString = "{\"id\":\"1111111111111111\", \"IP\":\"localhost\", \"Test\":\"true\"}";
        
        //Envoi de la requête (message = ID machine)
        String resultREQ = client.run(jsonString);
        
        //Récuppération des entrées dans la BD des pc
        String resultDB = DAO.getPC(true);
        
        //Résultat attendu
        String ExpResultREQ = "OK";
        String ExpResultDB = "[{\"IP\":\"localhost\", \"id\":\"1111111111111111\"}]";
        
        String result;
        String ExpResult = "OK";
        
        //Vérification de la réponse du serveur et du contenu de la BD pc
        if(resultREQ.equals(ExpResultREQ) & resultDB.equals(ExpResultDB)){
            result = "OK";
        }
        else{
            result = "erreur";
        }
        
        //Résultat
        System.out.println("resultat testAddClient1 : "+result+" | exp : "+ExpResult);
        
        //Vérification du résultat
        assertEquals(ExpResult, result);
    }
    
    
    
    
    /**
     * Vérification du fonctionnement de l'enregistrement des clients
     * @throws java.io.IOException
     */
    @Test
    public void testAddClient2() throws IOException {
        
        //Requête AnswerPing
        String jsonString = "{\"id\":\"1111111111111111\", \"IP\":\"localhost\", \"Test\":\"true\"}";
        
        //Envoi de la requête (message = ID machine)
        String resultREQ = client.run(jsonString);
        
        //Récuppération des entrées dans la BD des pc
        String resultDB = DAO.getPC(true);
        
        //Résultat attendu
        String ExpResultREQ = "OK";
        String ExpResultDB = "[{\"IP\":\"localhost\", \"id\":\"1111111111111111\"}]";
        
        String result;
        String ExpResult = "OK";
        
        //Vérification de la réponse du serveur et du contenu de la BD pc
        if(resultREQ.equals(ExpResultREQ) & resultDB.equals(ExpResultDB)){
            result = "OK";
        }
        else{
            result = "erreur";
        }
        
        //Suppression du pc de la BD
        DAO.deletePC("1111111111111111", true);
        
        //Résultat
        System.out.println("resultat testAddClient2 : "+result+" | exp : "+ExpResult);
        
        //Vérification du résultat 
        assertEquals(ExpResult, result);
    }
    
    
    
    /**
     * Vérification du fonctionnement de l'enregistrement des clients
     * @throws java.io.IOException
     */
    @Test
    public void testAddClient3() throws IOException {
        
        //Requête AnswerPing
        String jsonString = "{\"IP\":\"localhost\", \"Test\":\"true\"}";
        
        //Envoi de la requête (message = ID machine)
        String resultREQ = client.run(jsonString);
        
        //Récuppération des entrées dans la BD des pc
        String resultDB = DAO.getPC(true);
        
        //Résultat attendu
        String ExpResultREQ = "champ(s) inexistant";
        String ExpResultDB = "[]";
        
        String result;
        String ExpResult = "OK";
        
        //Vérification de la réponse du serveur et du contenu de la BD pc
        if(resultREQ.equals(ExpResultREQ) & resultDB.equals(ExpResultDB)){
            result = "OK";
        }
        else{
            result = "erreur";
        }
        
        //Résultat
        System.out.println("resultat testAddClient3 : "+result+" | exp : "+ExpResult);
        
        //Vérification du résultat
        assertEquals(ExpResult, result);
    }
}

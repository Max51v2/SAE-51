package Autre;

import DAO.DAOusers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Gère la vérification et la suppression des tokens expirés (démarré par OnStart)
 * 
 * @author Maxime VALLET
 * @version 1.1
 */
public class TokenExpiration implements Runnable {
    private Thread thread;
    private volatile boolean running = true;
    private Integer cycleDuration = 10000; //par défaut
    private Integer userCycleDuration;
    private Integer ListSize;
    private Boolean Test;
    
    
    //Démarre la vérification des tokens expirés
    public void start(Integer CheckIntervall, Boolean Test) {
        this.Test = Test;
        
        //Changement de la valeur de temps entre chaque verif des tokens
        this.cycleDuration = CheckIntervall;
        
        // Crée un nouveau thread seulement si aucun thread n'est déjà actif
        thread = new Thread(this);
        thread.setDaemon(true);
        running = true;
        thread.start(); 
    }
    
    //Arrête proprement la vérification des tokens
    public void stop() {
        running = false;
        
        if (thread != null) {
            try {
                thread.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(TokenExpiration.class.getName()).log(Level.SEVERE, null, ex);
                Thread.currentThread().interrupt();
            }
        }
    }
    
    //Fonction exécutée par le thread, vérifie les tokens toutes les 10 secondes
    @Override
    public void run() {
        while (running) {
            
            //vérification des tokens
            tokenCheck();
        }
    }
    
    //Vérification des tokens de la BD
    public void tokenCheck() {
        DAOusers DAO = new DAOusers();
        
        //Récuperation du JSON contenant tous les utilisateurs possédant un token
        String JSON = DAO.getActiveUsers(Test);
        ObjectMapper objectMapper = new ObjectMapper();
        
        try {
            //Conversion des données du JSON dans une liste d'objets Java
            List<JSON.GetTHEJSON> users = objectMapper.readValue(JSON, new TypeReference<List<JSON.GetTHEJSON>>() {});
            
            //Taille de la liste
            if(users.isEmpty()){
                ListSize = 0;
            }
            else{
                ListSize = users.size();
            }
            
            if(Test == false){
                System.out.println("Tokens actifs ("+ListSize+") :");
            }
            
            String login;
            Integer lifeCycle;
            
            //Pour tous les utilisateurs de la liste
            for (JSON.GetTHEJSON user : users) {
                //Récuppération du nombre d'itérations avant suppresion du token
                login = user.getLogin();
                lifeCycle = DAO.getTokenLifeCycle(login, Test);
                
                //si dernier cycle, suppression token
                if(lifeCycle == 1){
                    //Suppression du token
                    DAO.deleteToken(login, Test);
                    
                    if(Test == false){
                        System.out.println("Suppression token => login : "+login+" | lifeCycle : "+lifeCycle);
                    }
                    
                    //Retrait d'un cycle
                    lifeCycle = lifeCycle - 1;
                    DAO.setLifeCycle(login, lifeCycle, Test);
                }
                else{
                    if(Test == false){
                        System.out.println("login : "+login+" | lifeCycle : "+lifeCycle);
                    }
                    
                    //Retrait d'un cycle
                    lifeCycle = lifeCycle - 1;
                    DAO.setLifeCycle(login, lifeCycle, Test);
                }
                
                //Durée de pause entre chaque utilisateur
                userCycleDuration = cycleDuration/ListSize;
                
                //Pause à chaque utilisateur (étalement des requêtes sur la période d'actualisation)
                try {
                    thread.sleep(userCycleDuration);
                } catch (InterruptedException ex) {
                    Logger.getLogger(TokenExpiration.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            //Si aucun utilisateur n'a de token actif
            if(ListSize == 0){
                //Durée cycle
                userCycleDuration = cycleDuration;
                    
                //Pause
                try {
                    thread.sleep(userCycleDuration);
                } catch (InterruptedException ex) {
                    Logger.getLogger(TokenExpiration.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else{
                //Rien
            }
            
            if(Test == false){
                System.out.println("##########################################");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

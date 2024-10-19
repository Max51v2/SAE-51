package Autre;

import DAO.DAOusers;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Gère la vérification et la suppression des tokens expirés
 */
public class TokenExpiration implements Runnable {
    private Thread thread;
    private volatile boolean running = true;
    private Integer cycleDuration = 5000;
    private Integer userCycleDuration = 0;
    
    //Démarre la vérification des tokens expirés
    public void start() {
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
                Thread.currentThread().interrupt();  // Restaure l'état d'interruption
            }
        }
    }
    
    //Fonction exécutée par le thread, vérifie les tokens toutes les 5 secondes
    @Override
    public void run() {
        while (running) {
            
            //vérification des tokens
            tokenCheck();
        }
    }
    
    
    public void tokenCheck() {
        System.out.println("Tokens actifs :");
        
        DAOusers DAO = new DAOusers();
        
        //Récuperation du JSON envoyé
        String JSON = DAO.getUsers(false);
        Gson gsonRequest = new Gson();
        
        //Définir le type pour la conversion de JSON en liste d'utilisateurs
        java.lang.reflect.Type userListType = new TypeToken<List<Autre.GetJSONInfo>>() {}.getType();

        //Conversion des données du JSON dans une liste d'objets Java
        List<Autre.GetJSONInfo> users = gsonRequest.fromJson(JSON, userListType);
        
        String login;
        String tokenHash;
        Integer lifeCycle;
                
        // Récupération de tous les logins
        for (Autre.GetJSONInfo user : users) {
            //Récuppération du nombre d'itérations avant suppresion du token
            login = user.getLogin();
            lifeCycle = DAO.getTokenLifeCycle(login, false);
            
            //Token déjà expiré
            if(lifeCycle == 0){
                //Rien
            }
            //si dernier cycle, suppression token
            else if(lifeCycle == 1){
                //Données utilisateur
                tokenHash = DAO.getToken(login, false);
            
                DAO.deleteToken(login, false);
                System.out.println("Suppression token => login : "+login+" | lifeCycle : "+lifeCycle+" | tokenHash : "+tokenHash);
                
                //Retrait d'un cycle
                lifeCycle = lifeCycle - 1;
                DAO.setLifeCycle(login, lifeCycle, false);
            }
            else{
                //Données utilisateur
                tokenHash = DAO.getToken(login, false);
            
                System.out.println("login : "+login+" | lifeCycle : "+lifeCycle+" | tokenHash : "+tokenHash);
                
                //Retrait d'un cycle
                lifeCycle = lifeCycle - 1;
                DAO.setLifeCycle(login, lifeCycle, false);
            }
            
            //Pause à chaque utilisateur (étalement des requêtes sur la période d'actualisation
            userCycleDuration = cycleDuration/users.size();
            try {
                thread.sleep(userCycleDuration);
            } catch (InterruptedException ex) {
                Logger.getLogger(TokenExpiration.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        System.out.println("##########################################");
    }
}
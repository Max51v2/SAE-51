package Autre;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Gère la vérification et la suppression des tokens expirés
 */
public class TokenExpiration implements Runnable {
    private Thread thread;
    private volatile boolean running = true;
    
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
            try {
                //vérification des tokens
                tokenCheck();
                
                //Pause de 5 secondes entre chaque vérification
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                Logger.getLogger(TokenExpiration.class.getName()).log(Level.SEVERE, null, ex);
                Thread.currentThread().interrupt();  // Réinitialise l'état d'interruption
            }
        }
    }
    
    
    public void tokenCheck() {
        System.out.println("Vérification des tokens en cours...");
        // Ajoute ici la logique pour vérifier si un token est expiré ou non
    }
}
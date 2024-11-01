package TCP_Server;

import DAO.DAOPC;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe AnswerPing qui lance un serveur TCP permettant la vérification des connexions client
 * Enregistre les pc qui la contactent
 * 
 * @author Maxime VALLET
 * @version 1.0
 */
public class AnswerPing implements Runnable {
    private Thread thread;
    private volatile boolean running = false; // Indicateur d'exécution du serveur
    private ServerSocket serverSocket; // Socket du serveur
    DAOPC DAO = new DAOPC(); // Instance de DAO

    //Démarre la vérification des tokens expirés
    public synchronized void start() {
        //Crée un nouveau thread seulement si aucun thread n'est déjà actif
        if (thread == null || !running) {
            thread = new Thread(this);
            thread.setDaemon(true);
            running = true;
            thread.start();
        }
    }

    //Arrête proprement le serveur TCP
    public synchronized void stop() {
        running = false; // Interrompt la boucle d'exécution

        //Ferme le ServerSocket si nécessaire
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(AnswerPing.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Attend la fin du thread
        if (thread != null) {
            try {
                thread.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(AnswerPing.class.getName()).log(Level.SEVERE, null, ex);
                Thread.currentThread().interrupt();
            }
            thread = null;
        }
    }

    //Fonction exécutée par le thread, lance le serveur TCP
    @Override
    public void run() {
        int port = 4444;

        try {
            serverSocket = new ServerSocket(port);
            String id;
            String IP;
            String Test;
            Boolean TestBoolean;

            // Tant que le serveur est en cours d'exécution
            while (running) {
                try {
                    // Accepter une connexion client
                    Socket clientSocket = serverSocket.accept();
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                    //Message reçu du client
                    String message = in.readLine();

                    //Récuperation du JSON envoyé
                    Gson gsonRequest = new Gson();
                    // Convertion des données du JSON dans un objet Java
                    JSON.GetJSONInfoUsers ping = gsonRequest.fromJson(message, JSON.GetJSONInfoUsers.class);
        
                    //Données envoyées
                    id = ping.getId();
                    IP = ping.getIP();
                    TestBoolean = Boolean.valueOf(ping.getTest());
                    
                    //Vérirication de la présence du pc dans la BD
                    Boolean idExist = DAO.doIDExist(id, TestBoolean);
                    
                    //Enregistrement du pc s'il n'existe pas dans la BD
                    if(idExist == true){
                        //Rien
                    }
                    else{
                        DAO.addPC(id, IP, TestBoolean);
                    }
                    
                    //Envoi de la réponse au client
                    out.println("OK");

                    //Fermeture de la connexion client
                    clientSocket.close();
                    
                } catch (IOException e) {
                    if (running) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            if (running) {
                e.printStackTrace();
            }
        } finally {
            // Réinitialise `running` et ferme le ServerSocket au cas où
            running = false;
            if (serverSocket != null && !serverSocket.isClosed()) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    Logger.getLogger(AnswerPing.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        }
    }
}

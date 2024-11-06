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
 * @version 2.0
 */
public class AnswerPing {
    
    private volatile boolean running = false;
    private ServerSocket serverSocket;
    private Integer port;

    
    //Démarre le serveur TCP
    public synchronized void start(Integer port) {
        this.port = port;
        running = true;
        
        //Lancement du serveur
        new Thread(this::run).start();
    }

    
    //Arrête le serveur TCP
    public synchronized void stop() {
        //Interrompt la boucle d'exécution
        running = false; 

        //Ferme le ServerSocket si nécessaire
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(AnswerPing.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    //Serveur TCP
    private void run() {
        try {
            serverSocket = new ServerSocket(port);
            serverSocket.setReuseAddress(true);

            while (running) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    
                    new Thread(new ClientHandler(clientSocket)).start();
                } 
                catch (IOException e) {
                    if (running) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            stop();
        }
    }
    
    
    //Thread qui prend en charge le client
    private static class ClientHandler implements Runnable { 
        private final Socket clientSocket; 
        DAOPC DAO = new DAOPC();
  
        public ClientHandler(Socket socket){
            this.clientSocket = socket; 
        } 
  
        @Override
        public void run(){ 
            String id;
            String IP;
            Boolean TestBoolean;
            String result = "";
            
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                
                //Message reçu du client
                String message = in.readLine();
                
                //Récuperation du JSON envoyé
                Gson gsonRequest = new Gson();
                // Convertion des données du JSON dans un objet Java
                JSON.GetJSONInfoPC ping = gsonRequest.fromJson(message, JSON.GetJSONInfoPC.class);

                //Données envoyées
                id = ping.getId();
                IP = ping.getIP();
                TestBoolean = Boolean.valueOf(ping.getTest());

                //Vérification du contenu envoyé
                if(id == null | IP == null){
                    result = "champ(s) inexistant";
                }
                else{
                    //Vérification du contenu envoyé
                    if(id.equals("") | IP.equals("")){
                        result = "champ(s) inexistant";
                    }
                    else{
                        //Vérification de la présence du pc dans la BD
                        Boolean idExist = DAO.doIDExist(id, TestBoolean);

                        //Enregistrement du pc s'il n'existe pas dans la BD
                        if(idExist == true){
                            //Rien
                        }
                        else{
                            DAO.addPC(id, IP, TestBoolean);
                        }
                
                        result = "OK";
                    }
                }

                //Envoi de la réponse au client
                out.println(result);
                
                //Fermeture de la connexion client
                clientSocket.close();
                
            } 
            catch (IOException ex) {
                Logger.getLogger(AnswerPing.class.getName()).log(Level.SEVERE, null, ex);
            }
        } 
    } 
}

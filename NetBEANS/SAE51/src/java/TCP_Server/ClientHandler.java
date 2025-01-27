package TCP_Server;

import javax.net.ssl.SSLSocket;
import java.io.*;
import java.util.Arrays;

public class ClientHandler extends Thread {
    private SSLSocket clientSocket;
    DAO.DAOPC daoPC = new DAO.DAOPC();

    public ClientHandler(SSLSocket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            out.println("Bienvenue sur le serveur sécurisé ! Tapez vos messages ou 'exit' pour quitter.");

            String message;
           // message = in.readLine();
 
                
            while ((message = in.readLine()) != null) {
                System.out.println("Message reçu : " + message);

                // Gestion des différents types de messages
                if ("exit".equalsIgnoreCase(message)) {
                    out.println("Déconnexion demandée. Au revoir !");
                    break;
                } else if (message.startsWith("array:")) {
                    // Traitement des tableaux envoyés sous forme de chaînes (séparés par des virgules)
                    String arrayData = message.substring(6).trim();
                    String[] elements = arrayData.split(",");
                    out.println("Tableau reçu : " + Arrays.toString(elements));
                    System.out.println(elements[3]);
                    
                    daoPC.addPCStaticInfo(1, elements[3], Integer.valueOf(elements[6].strip()), Integer.valueOf(elements[5].strip()), elements[4], elements[7], 2, "3200", 6543 , elements[8], 1, "12", elements[0], elements[1], false);
                } else {
                    // Gestion des messages simples
                    out.println("Message reçu : " + message);
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur avec le client : " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
                System.out.println("Connexion fermée pour le client.");
            } catch (IOException e) {
                System.err.println("Erreur lors de la fermeture de la connexion : " + e.getMessage());
            }
        }
    }
    public void action(int i) {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            if(i == 1) {
                out.println("action");
                out.println("1");
            } else if (i == 2) {
                out.println("action");
                
            } else if (i == 3) {
                out.println("action");
            }
        } catch (IOException e) {
            System.err.println("Erreur avec le client : " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
                System.out.println("Connexion fermée pour le client.");
            } catch (IOException e) {
                System.err.println("Erreur lors de la fermeture de la connexion : " + e.getMessage());
            }
        }
    }
}

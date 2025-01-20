package TCP_Server;

import javax.net.ssl.SSLSocket;
import java.io.*;

public class ClientHandler extends Thread {
    private SSLSocket clientSocket;

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
            while ((message = in.readLine()) != null) {
                System.out.println("Message reçu : " + message);
                

                // Réponse du serveur
                if ("exit".equalsIgnoreCase(message)) {
                    out.println("Déconnexion demandée. Au revoir !");
                    break; // Quitter la boucle et fermer la connexion
                } else {
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
}
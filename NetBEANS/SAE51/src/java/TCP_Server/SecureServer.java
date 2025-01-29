package TCP_Server;

import javax.net.ssl.*;
import java.io.*;
import java.net.Socket;
import java.security.KeyStore;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.concurrent.ConcurrentHashMap;

public class SecureServer implements Runnable {
    private int port;
    private boolean running = true;
    private SSLServerSocket serverSocket;
    private static final String UserPostgres="postgres";
    private static final String PasswordPostgres="leffe";
    private final ConcurrentHashMap<Integer, SSLSocket> clientMap = new ConcurrentHashMap<>();

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/sae_51";

    public synchronized void start(int port) {
        this.port = port;
        new Thread(this).start();
    }

    @Override
    public void run() {
        String keystorePath = "/certs/server.keystore"; // Chemin du keystore
        String keystorePassword = "password";   // Mot de passe du keystore

        try {
            // Charger le keystore contenant le certificat SSL
            KeyStore keyStore = KeyStore.getInstance("JKS");
            try (FileInputStream keyStoreStream = new FileInputStream(keystorePath)) {
                keyStore.load(keyStoreStream, keystorePassword.toCharArray());
            }

            // Initialiser le KeyManagerFactory
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, keystorePassword.toCharArray());

            // Initialiser le SSLContext
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagerFactory.getKeyManagers(), null, null);

            // Créer un SSLServerSocket
            SSLServerSocketFactory socketFactory = sslContext.getServerSocketFactory();
            serverSocket = (SSLServerSocket) socketFactory.createServerSocket(port);

            System.out.println("Serveur sécurisé démarré sur le port " + port);

            while (running) {
                SSLSocket clientSocket = (SSLSocket) serverSocket.accept();
                System.out.println("Connexion sécurisée établie avec un client : " + clientSocket.getInetAddress());

                // Gérer le client dans un nouveau thread
                new ClientHandler(clientSocket).start();
            }
        } catch (Exception e) {
            System.err.println("Erreur du serveur sécurisé : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public synchronized void stop() {
        this.running = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
                System.out.println("Serveur arrêté proprement.");
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de l'arrêt du serveur : " + e.getMessage());
        }
    }

    // Méthode pour attribuer un ID unique à un nouveau client
    private int assignClientId() {
        int clientId = -1;

        try (Connection connection = DriverManager.getConnection(DB_URL, UserPostgres, PasswordPostgres);
             Statement statement = connection.createStatement()) {

            // Vérifier s'il existe un ID réutilisable (lacune)
            ResultSet gaps = statement.executeQuery("SELECT id + 1 AS next_id " +
                "FROM pc_static_info t1 " +
                "WHERE NOT EXISTS (SELECT 1 FROM pc_static_info t2 WHERE t2.id = t1.id + 1) " +
                "AND id < (SELECT MAX(id) FROM pc_static_info) " +
                "LIMIT 1;");

            if (gaps.next()) {
                clientId = gaps.getInt("next_id");
            } else {
                // Si aucun ID réutilisable, prendre le prochain ID disponible
                ResultSet maxIdResult = statement.executeQuery("SELECT COALESCE(MAX(id), 0) + 1 AS next_id FROM pc_static_info;");
                if (maxIdResult.next()) {
                    clientId = maxIdResult.getInt("next_id");
                }
            }

            // Insérer le nouvel ID dans la table
            try (PreparedStatement insertStmt = connection.prepareStatement("INSERT INTO pc_static_info (id) VALUES (?);");) {
                insertStmt.setInt(1, clientId);
                insertStmt.executeUpdate();
            }

        } catch (Exception e) {
            System.err.println("Erreur lors de l'attribution d'un ID client : " + e.getMessage());
            e.printStackTrace();
        }

        return clientId;
    }
    public void sendMessageToClient(int clientId, int action) {
        SSLSocket clientSocket = clientMap.get(clientId);
        if (clientSocket != null && !clientSocket.isClosed()) {
            try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
                out.println("action");
                if (action == 1) {
                    out.println("1");
                } else if (action == 2) {
                    out.println("2");
                    
                } else if (action == 3) {
                    out.println("3");
                    
                }
            } catch (IOException e) {
                System.err.println("Erreur lors de l'envoi du message au client " + clientId + " : " + e.getMessage());
            }
        } else {
            System.err.println("Client avec l'ID " + clientId + " introuvable ou déconnecté.");
        }
    }

    // Gérer un client
    private class ClientHandler extends Thread {
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
                int clientId = assignClientId();
                clientMap.put(clientId, clientSocket);
                out.println("Votre ID client est : " + clientId);

                out.println("Bienvenue sur le serveur sécurisé ! Tapez vos messages ou 'exit' pour quitter.");

                String message = in.readLine();
                if (message.equalsIgnoreCase("password")) {
                    out.println("connexion réussi");
                while ((message = in.readLine()) != null) {
                    System.out.println("Message reçu de l'ID " + clientId + " : " + message);

                    if ("exit".equalsIgnoreCase(message)) {
                        out.println("Déconnexion demandée. Au revoir !");
                        break;
                    } else if (message.startsWith("Array :")) {
                        
                        out.println("Message reçu : " + message);
                    } else {
                        out.print(message);
                    }
                }
                } else {
                    out.print("mot de passe incorect");
                    clientSocket.close();
                }

            } catch (IOException e) {
                System.err.println("Erreur avec le client : " + e.getMessage());
            } finally {
                try {
                    clientMap.values().remove(clientSocket); // Retirer du map des clients
                    clientSocket.close();
                    System.out.println("Connexion fermée pour le client.");
                } catch (IOException e) {
                    System.err.println("Erreur lors de la fermeture de la connexion : " + e.getMessage());
                }
            }
        }
    }
}

package TCP_Server;

import javax.net.ssl.*;
import java.io.*;
import java.net.Socket;
import java.security.KeyStore;

public class SecureServer implements Runnable {
    private int port;
    private boolean running = true;
    private SSLServerSocket serverSocket;

    
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
            if (serverSocket != null || !serverSocket.isClosed()) {
                serverSocket.close();
                System.out.println("Serveur arrêté proprement.");
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de l'arrêt du serveur : " + e.getMessage());
        }
    }
}
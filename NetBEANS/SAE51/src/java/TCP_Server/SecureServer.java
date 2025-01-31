package TCP_Server;

import DAO.DAOClient;
import DAO.DAOPC;
import javax.net.ssl.*;
import java.io.*;
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
    private ConcurrentHashMap<Integer, SSLSocket> clientMap = new ConcurrentHashMap<>();
    DAOClient DAOclient = new DAOClient();
    DAOPC daoPC = new DAOPC();

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/sae_51";
    
    //Demarrage du driver postgresql
    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            // Si le driver PostgreSQL n'est pas trouvé dans le classpath
            e.printStackTrace();
        }
    }
    
    

    public synchronized void start(int port) {
        this.port = port;
        new Thread(this).start();
    }

    @Override
    public void run() {
        String keystorePath = "/opt/tomcat/conf/tomcat.keystore"; // Chemin du keystore
        String keystorePassword = "administrateur";   // Mot de passe du keystore

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
                System.out.println("Connexion sécurisée établie avec un client : " + clientSocket.getInetAddress().getHostAddress());

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
    private int assignClientId(SSLSocket clientSocket) {
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
            try (PreparedStatement insertStmt = connection.prepareStatement("INSERT INTO pc_static_info (id) VALUES (?);" );) {
                insertStmt.setInt(1, clientId);
                insertStmt.executeUpdate();
            }
            try (PreparedStatement insertStmt = connection.prepareStatement("INSERT INTO pc (id,ip,droits) VALUES (?,?,?);" );) {
                insertStmt.setInt(1, clientId);
                insertStmt.setString(2, clientSocket.getInetAddress().getHostAddress());
                insertStmt.setString(3, "");
                insertStmt.executeUpdate();
            }

        } catch (Exception e) {
            System.err.println("Erreur lors de l'attribution d'un ID client : " + e.getMessage());
            e.printStackTrace();
        }

        return clientId;
    }
    
    
    public void sendMessageToClient(int clientId, String action) {
        SSLSocket client = clientMap.get(clientId);
        System.out.println("tes" + clientId);
        
        if (client != null && !client.isClosed()) {
        try {
            OutputStream outputStream = client.getOutputStream();
            PrintWriter out = new PrintWriter(outputStream, true); // Ne pas utiliser try-with-resources
            out.println("action");
            out.println(action);  // Envoyer directement l'action

            System.out.println("Message envoyé avec succès au client " + clientId + " : " + action);
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
                int clientId = 0;

                out.println("Bienvenue sur le serveur sécurisé ! Tapez vos messages ou 'exit' pour quitter.");

                String message = in.readLine();
                if (message.equalsIgnoreCase("leffe")) {
                    out.println("connexion réussi");
                    message = in.readLine();
                    System.out.println(message);
                    if (message.startsWith("ID : ")) {
                        clientId = Integer.parseInt(message.replace("ID : ", "").trim());
                        clientMap.put(clientId, clientSocket);
                        
                        //Actualisation des clients connectés
                        DAOclient.addPCs(clientMap, false);

                        //On vérifie si l'id est déjà dans la base (zebi ça ma cassé la tête)
                        Boolean idExist = DAOclient.doIDExist(clientId, "1", false);
                        if(idExist == false){
                            DAOclient.addPCToPC(clientId, clientSocket.getInetAddress().getHostAddress(), false);
                        }
                        idExist = DAOclient.doIDExist(clientId, "2", false);
                        if(idExist == false){
                            DAOclient.addPCToPCSI(clientId, false);
                        }
                        
                        out.println("l'ID : " + clientId + "est connecté");
                    } else {
                    
                    clientId = assignClientId(clientSocket);
                    clientMap.put(clientId, clientSocket);
                    
                    //Actualisation des clients connectés
                    DAOclient.addPCs(clientMap, false);
                    
                    out.println("Votre ID client est : " + clientId);
                    out.println("continue");
                    }
                    while ((message = in.readLine()) != null) {
                        System.out.println("Message reçu de l'ID " + clientId + " : " + message);

                        if ("exit".equalsIgnoreCase(message)) {
                            out.println("Déconnexion demandée. Au revoir !");
                            break;
                        } else if (message.startsWith("Array :")) {
                            System.out.println("1");
                            String arrayData = message.substring(9).trim();
                            String elements[] = arrayData.split(",\\s*");
                            String E8 = String.valueOf(elements[8]);
                            E8 = E8.substring(0, E8.indexOf("]")-1);
                            daoPC.addPCStaticInfo(clientId, elements[3], Integer.valueOf(elements[6].strip()), Integer.valueOf(elements[5].strip()), elements[4], elements[7], 2, "3200", 6543 , E8, 1, "12", elements[0], elements[1], false);

                        } else {
                            System.out.println("2");
                        }
                    }
                } else {
                    out.println("mot de passe incorect");
                    clientSocket.close();   

                }

            } catch (IOException e) {
                System.err.println("Erreur avec le client : " + e.getMessage());
            } finally {
                try {
                    clientMap.values().remove(clientSocket); // Retirer du map des clients
                    clientSocket.close();
                    
                    //Actualisation des clients connectés
                    DAOclient.addPCs(clientMap, false);
                    
                    System.out.println("Connexion fermée pour le client.");
                } catch (IOException e) {
                    System.err.println("Erreur lors de la fermeture de la connexion : " + e.getMessage());
                }
            }
        }
    }
}

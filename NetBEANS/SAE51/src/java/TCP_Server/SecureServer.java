package TCP_Server;

import DAO.DAOClient;
import DAO.DAOPC;
import javax.net.ssl.*;
import java.io.*;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SecureServer implements Runnable {
    private int port;
    private boolean running = true;
    private SSLServerSocket serverSocket;
    private ConcurrentHashMap<Integer, SSLSocket> clientMap = new ConcurrentHashMap<>();
    DAOClient DAOclient = new DAOClient();
    DAOPC daoPC = new DAOPC();
    private final Boolean Test = false;

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
        
        //Actualisation des clients (sinon cela se fait uniquement quand il y'a un changement lol)
        DAOclient.addPCs(clientMap, Test);
  
        
        //Vérifie les messages ajoutés dans la BD en arrière plan
        Thread threadMessages = new Thread(){
            public void run(){
                long pause = 500;
                ArrayList<String> list;
                String message = "0";
                
                //Tant que le la classe tourne, le thread tourne
                while(running){
                    try {
                        Thread.sleep(pause);

                        //Récupération d'un message
                        list = daoPC.getMessage(Test);
                        
                        //S'il n'y a pas de message
                        if(list.get(0).equals("-1")){
                            //Rien
                        }
                        else{
                            //Envoi du message au client
                            System.out.println("Envoi du message \""+list.get(1)+"\" au PC "+list.get(0));
                            
                            //Numéro action
                            if(list.get(1).equals("shutdown")){message = "3";}
                            if(list.get(1).equals("restart")){message = "2";}
                            if(list.get(1).equals("update")){message = "1";}
                            
                            //Suppression du message dans la BD
                            daoPC.deleteMessage(Integer.valueOf(list.get(0)), Test);
                            
                            //Envoi du message
                            sendMessageToClient(Integer.valueOf(list.get(0)), message);
                        }
                        
                    } 
                    catch (InterruptedException ex) {
                        Logger.getLogger(SecureServer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        };

      threadMessages.start();
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
                
                clientMap.values().clear(); // Retirer du map des clients
                
                //Actualisation des clients connectés
                DAOclient.addPCs(clientMap, Test);
                        
                System.out.println("Serveur arrêté proprement.");
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de l'arrêt du serveur : " + e.getMessage());
        }
    }
    

    
    // Méthode pour attribuer un ID unique à un nouveau client
    private int assignClientId(SSLSocket clientSocket) {
        int clientId = DAOclient.assignClientId(clientSocket);
        
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

                        //On vérifie si l'id est déjà dans la base (zebi ça ma cassé la tête)
                        Boolean idExist = DAOclient.doIDExist(clientId, "1", Test);
                        if(idExist == false){
                            DAOclient.addPCToPC(clientId, clientSocket.getInetAddress().getHostAddress(), Test);
                        }
                        idExist = DAOclient.doIDExist(clientId, "2", Test);
                        if(idExist == false){
                            DAOclient.addPCToPCSI(clientId, Test);
                        }
                        
                        out.println("l'ID : " + clientId + "est connecté");
                    } else {
                    
                    clientId = assignClientId(clientSocket);
                    clientMap.put(clientId, clientSocket);
                    
                    out.println("Votre ID client est : " + clientId);
                    out.println("continue");
                    }
                    
                    //Actualisation des clients connectés
                    DAOclient.addPCs(clientMap, Test);
                    
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
                            daoPC.addPCStaticInfo(clientId, elements[3], Integer.valueOf(elements[6].strip()), Integer.valueOf(elements[5].strip()), elements[4], elements[7], 2, "3200", 6543 , E8, 1, "12", elements[0], elements[1], Test);
                            
                            //TEST//
                            //Données à envoyer
                            Integer id = clientId;
                            Integer CPUUtilization = 10;
                            Integer CPUTemp = 50;
                            Integer CPUConsumption = 60;
                            Integer RAMUtilization = 70;
                            String storageName = "Stockage1/Stockage2/Stockage3";
                            String storageLoad = "39/48/20";
                            String storageLeft = "2048/231/1024";
                            String storageTemp = "45/55/50";
                            String storageErrors = "2/0/1";
                            String networkName = "NIC1/NIC2";
                            String networkLatency = "20/10";
                            String networkBandwith = "10/60";
                            String fanSpeed = "0/90/100/80";
                            Boolean Test = false;

                            daoPC.addPCDynamicInfo(id, CPUUtilization, CPUTemp, CPUConsumption, RAMUtilization, storageName, storageLoad, storageLeft, storageTemp, storageErrors, networkName, networkLatency, networkBandwith, fanSpeed, Test);
                            daoPC.checkThresholds(clientId, Test);

                        } else {
                            System.out.println("2");
                        }
                    }
                } else {
                    out.println("mot de passe incorect");
                    clientSocket.close();   

                }

            } 
            catch (IOException e) {
                System.err.println("Erreur avec le client (IGNOREZ java.sql.SQLException) : " + e.getMessage());
                
                clientMap.values().remove(clientSocket); // Retirer du map des clients
                    
                //Actualisation des clients connectés
                DAOclient.addPCs(clientMap, Test);
            } 
            finally {
                try {
                    clientMap.values().remove(clientSocket); // Retirer du map des clients
                    
                    clientSocket.close();
                    
                    System.out.println("Connexion fermée pour le client.");
                } catch (IOException e) {
                    System.err.println("Erreur lors de la fermeture de la connexion : " + e.getMessage());
                    
                    clientMap.values().remove(clientSocket); // Retirer du map des clients
                }
                
                try{
                    //Actualisation des clients connectés
                    DAOclient.addPCs(clientMap, Test);
                }catch (Exception e){
                    System.err.println("Erreur addPCs : " + e.getMessage());
                }
            }
        }
    }
}
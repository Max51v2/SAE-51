package DAO;

import Autre.ProjectConfig;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import javax.net.ssl.SSLSocket;

/**
 * 
 * @author Maxime VALLET
 * @version 0.1
 */
public class DAOClient {
    ProjectConfig conf = new ProjectConfig();
    private static final String UserPostgres="postgres";
    private static final String PasswordPostgres="leffe";
    private static String UrlBD="";
    private String loginLog = "Aucun";
    
    
    //Défini la DB sur laquelle on se connecte
    private void changeConnection(Boolean Test){
        if(Test == false){
            UrlBD="jdbc:postgresql://localhost:5432/sae_51";
        }
        else{
            UrlBD="jdbc:postgresql://localhost:5432/test";
        }
    }
    
    //Connection utilisateur postgres
        private static Connection getConnectionPostgres() throws SQLException {
            return DriverManager.getConnection(UrlBD, UserPostgres, PasswordPostgres);
    }
        
    
    //Demarrage du driver postgresql
    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            // Si le driver PostgreSQL n'est pas trouvé dans le classpath
            e.printStackTrace();
        }
    }
    
    
    
    /**
     * Suppression du contenu de la table pc_status
     * 
     * @param Test     Utilisation de la BD test (true si test sinon false !!!)
     */
    public void deletePCStatus(Boolean Test){
        String RequeteSQL="TRUNCATE TABLE pc_status";
        
        //Selection de la BD
        changeConnection(Test);
        
        //Connection BD en tant que postgres
        try (Connection connection =
            DAOClient.getConnectionPostgres();
                
            //Requête SQL
            PreparedStatement preparedStatement = connection.prepareStatement(RequeteSQL)) {
            
            // Exécution de la requête
            int affectedRows = preparedStatement.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    
    /**
     * Ajout du status des PC
     * 
     * @param ClientMap     liste des sockets des clients connectés
     * @param Test     Utilisation de la BD test (true si test sinon false !!!)
     */
    public void addPCs(ConcurrentHashMap<Integer, SSLSocket> ClientMap, Boolean Test){
        //Requête SQL
        String RequeteSQL="SELECT id FROM pc";
        
        Integer id = null;
        
        //Suppression des entrées précédentes
        deletePCStatus(Test);
        
        //Selection de la BD
        changeConnection(Test);
        
        //Connection BD en tant que postgres
        try (Connection connection =
            DAOClient.getConnectionPostgres();
                
            //Requête SQL
            PreparedStatement preparedStatement = connection.prepareStatement(RequeteSQL)) {
            
            // Exécution de la requête
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    id = resultSet.getInt("id");
                    
                    //Si le socket est null alors le pc est hors ligne
                    if(ClientMap.get(id) == null){
                        addPCToPCStatus(id, "Hors Ligne", Test);
                    }
                    else{
                        addPCToPCStatus(id, "En Ligne", Test);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    
    /**
     * Ajout du status des PC
     * 
     * @param id     id du PC
     * @param status        "En ligne" | "Hors Ligne"
     * @param Test     Utilisation de la BD test (true si test sinon false !!!)
     */
    public void addPCToPCStatus(Integer id, String status, Boolean Test){
        //Requête SQL
        String RequeteSQL="INSERT INTO pc_status (id, status) VALUES (?, ?)";
        
        //Selection de la BD
        changeConnection(Test);
        
        //Connection BD en tant que postgres
        try (Connection connection =
            DAOClient.getConnectionPostgres();
                
            //Requête SQL
            PreparedStatement preparedStatement = connection.prepareStatement(RequeteSQL)) {
            
            //Remplacement des "?" par les variables d'entrée (pour éviter les injections SQL !!!)
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, status);
            
            // Exécution de la requête
            int affectedRows = preparedStatement.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    
    /**
     * Ajout du status des PC
     * 
     * @param id     id utilisateur
     * @param option        "1" BD pc | "2" BD pc_static_info
     * @param Test     Utilisation de la BD test (true si test sinon false !!!)
     * @return doIDExist        Boolean : existance de l'id ou non
     */
    public Boolean doIDExist(Integer id, String option, Boolean Test){
        //Requête SQL
        String RequeteSQL ="";
        if(option.equals("1")){
            RequeteSQL="SELECT id FROM pc";
        }
        else{
            RequeteSQL="SELECT id FROM pc_static_info";
        }
        
        
        Boolean idExist = false;
        Integer idDB = null;
        
        //Selection de la BD
        changeConnection(Test);
        
        //Connection BD en tant que postgres
        try (Connection connection =
            DAOClient.getConnectionPostgres();
                
            //Requête SQL
            PreparedStatement preparedStatement = connection.prepareStatement(RequeteSQL)) {
            
            // Exécution de la requête
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    idDB = resultSet.getInt("id");
                    
                    //Si le socket est null alors le pc est hors ligne
                    if(Objects.equals(idDB, id)){
                        idExist = true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return idExist;
    }
    
    
    
    /**
     * Ajout id
     * 
     * @param id     id du PC
     * @param IP        IP machine
     * @param Test     Utilisation de la BD test (true si test sinon false !!!)
     */
    public void addPCToPC(Integer id, String IP, Boolean Test){
        //Requête SQL
        String RequeteSQL="INSERT INTO pc (id, ip, droits) VALUES (?, ?, ?)";
        
        //Selection de la BD
        changeConnection(Test);
        
        //Connection BD en tant que postgres
        try (Connection connection =
            DAOClient.getConnectionPostgres();
                
            //Requête SQL
            PreparedStatement preparedStatement = connection.prepareStatement(RequeteSQL)) {
            
            //Remplacement des "?" par les variables d'entrée (pour éviter les injections SQL !!!)
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, IP);
            preparedStatement.setString(3, "");
            
            // Exécution de la requête
            int affectedRows = preparedStatement.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    /**
     * Ajout id
     * 
     * @param id     id du PC
     * @param Test     Utilisation de la BD test (true si test sinon false !!!)
     */
    public void addPCToPCSI(Integer id, Boolean Test){
        //Requête SQL
        String RequeteSQL="INSERT INTO pc_static_info (id) VALUES (?)";
        
        //Selection de la BD
        changeConnection(Test);
        
        //Connection BD en tant que postgres
        try (Connection connection =
            DAOClient.getConnectionPostgres();
                
            //Requête SQL
            PreparedStatement preparedStatement = connection.prepareStatement(RequeteSQL)) {
            
            //Remplacement des "?" par les variables d'entrée (pour éviter les injections SQL !!!)
            preparedStatement.setInt(1, id);
            
            // Exécution de la requête
            int affectedRows = preparedStatement.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    
    /**
     * Renvoi les logs contenu dans la BD
     * 
     * @param login     login utilisateur
     * @param Test     Utilisation de la BD test (true si test sinon false !!!)
     * @return JSONString       contenu de la table au format JSON (login/prenom/nom/droits)
     */
    public String getOnlinePC(String login, Boolean Test){
        String RequeteSQL="SELECT id, status FROM pc_status ORDER BY id ASC";
        Integer id=null;
        String JSONString="";
        String status = "";
        DAOPC DAO2 = new DAOPC();
        Boolean access = null;
        
        //Selection de la BD
        changeConnection(Test);
        
        //Connection BD en tant que postgres
        try (Connection connection =
            DAOClient.getConnectionPostgres();
                
            //Requête SQL
            PreparedStatement preparedStatement = connection.prepareStatement(RequeteSQL)) {
            
            // Exécution de la requête
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                Integer c = 1;
                
                // Ouvrir le tableau JSON
                JSONString += "[";

                while (resultSet.next()) {
                    id = resultSet.getInt("id");
                    
                    access = DAO2.getUserPCAccess(id, login, Test);
                    
                    if(access == true){
                        status = resultSet.getString("status");
                        
                        // Ajouter une virgule avant chaque entrée sauf la première
                        if (c > 1) {
                            JSONString += ",";
                        }

                        // Ajouter l'objet JSON
                        JSONString += "{\"id\":\""+id+"\", \"status\":\""+status+"\"}";

                        c += 1;
                    }
                }
                
                // Fermer le tableau JSON
                JSONString += "]";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return JSONString;
    }
    
    
    
    // Méthode pour attribuer un ID unique à un nouveau client
    private int assignClientId(SSLSocket clientSocket) {
        int clientId = -1;

        try (Connection connection = DriverManager.getConnection(UrlBD, UserPostgres, PasswordPostgres);
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
}
package DAO;

import Autre.ProjectConfig;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

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
    public void addPCs(ConcurrentHashMap ClientMap, Boolean Test){
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
     * @param Test     Utilisation de la BD test (true si test sinon false !!!)
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
}

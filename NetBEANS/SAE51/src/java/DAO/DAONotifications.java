package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

/**
 *
 * @author Maxime VALLET
 * @version 1.2
 */
public class DAONotifications {
    private static final String UserPostgres="postgres";
    private static final String PasswordPostgres="leffe";
    private static String UrlBD="";
    
    
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
     * Ajout d'un log
     * 
     * @param description     Description courte du type de notif
     * @param content       contenu de la notification
     * @param idPC      id du pc
     * @param Test     Utilisation de la BD test (true si test sinon false !!!)
     */
    public void addNotification(String description, String content, Integer idPC, Boolean Test){
        String RequeteSQL="INSERT INTO notification (description, content, date, idpc) VALUES (?, ?, ?, ?)";
        
        Boolean exist = doNotifExist(content, idPC, Test);
        
        if(exist == true){
            return;
        }
        
        //Selection de la BD
        changeConnection(Test);
        
        //Connection BD en tant que postgres
        try (Connection connection =
            DAONotifications.getConnectionPostgres();
                
            //Requête SQL
            PreparedStatement preparedStatement = connection.prepareStatement(RequeteSQL)) {
            
            //Récupération de la date actuelle
            String date = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
            
            //Remplacement des "?" par les variables d'entrée (pour éviter les injections SQL !!!)
            preparedStatement.setString(1, description);
            preparedStatement.setString(2, content);
            preparedStatement.setString(3, date);
            preparedStatement.setInt(4, idPC);
            
            // Exécution de la requête
            int affectedRows = preparedStatement.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    
    /**
     * Suppression des logs
     * 
     * @param Test     Utilisation de la BD test (true si test sinon false !!!)
     */
    public void deleteNotifications(Boolean Test){
        String RequeteSQL="TRUNCATE notification";
        
        //Selection de la BD
        changeConnection(Test);
        
        //Connection BD en tant que postgres
        try (Connection connection =
            DAONotifications.getConnectionPostgres();
                
            //Requête SQL
            PreparedStatement preparedStatement = connection.prepareStatement(RequeteSQL)) {
            
            // Exécution de la requête
            int affectedRows = preparedStatement.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    
    /**
     * Renvoi les notifications contenues dans la BD
     * 
     * @param userLogin     Login des notifs de l'utilisateur donné
     * @param BypassAuth        bypass l'auth et renvoi toutes les notifs
     * @param Test     Utilisation de la BD test (true si test sinon false !!!)
     * @return JSONString       contenu de la table au format JSON (login/prenom/nom/droits)
     */
    public String getNotifications(String userLogin, Boolean BypassAuth, Boolean Test){
        String RequeteSQL="SELECT description, content, idpc, date FROM notification ORDER BY idmsg ASC";
        DAOPC daoPC = new DAOPC();
        String content="";
        String description="";
        Boolean access = false;
        Integer idPC = -1;
        String date = "";
        String JSONString="";
        
        //Selection de la BD
        changeConnection(Test);
        
        //Connection BD en tant que postgres
        try (Connection connection =
            DAONotifications.getConnectionPostgres();
                
            //Requête SQL
            PreparedStatement preparedStatement = connection.prepareStatement(RequeteSQL)) {
            
            // Exécution de la requête
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                Integer c = 1;
                
                // Ouvrir le tableau JSON
                JSONString += "[";

                while (resultSet.next()) {
                    //Récupération de la liste d'utilisateurs ayant accès à la notif
                    idPC = resultSet.getInt("idpc");
                    access = daoPC.getUserPCAccess(idPC, userLogin, Test);
                    
                    
                    //Si l'utilisateur se situe dans la liste des utilisateurs qui ont accès au pc (et bypass false)
                    if(access == true && BypassAuth == false){
                        
                        //Récupération de la notification
                        description = resultSet.getString("description");
                        content = resultSet.getString("content");
                        date = resultSet.getString("date");
                    
                        // Ajouter une virgule avant chaque entrée sauf la première
                        if (c > 1) {
                            JSONString += ",";
                        }

                        //On ajoute pas la date si c'est un test
                        if(Test == true){
                            // Ajouter l'objet JSON
                            JSONString += "{\"description\":\""+description+"\", \"content\":\""+content+"\"}";
                        }
                        else{
                            // Ajouter l'objet JSON
                            JSONString += "{\"description\":\""+description+"\", \"content\":\""+content+"\", \"date\":\""+date+"\"}";
                        }

                        c += 1;
                    }
                    if(BypassAuth == true){
                        //Récupération de la notification
                        description = resultSet.getString("description");
                        content = resultSet.getString("content");
                        date = resultSet.getString("date");
                    
                        // Ajouter une virgule avant chaque entrée sauf la première
                        if (c > 1) {
                            JSONString += ",";
                        }

                        //On ajoute pas la date si c'est un test
                        if(Test == true){
                            // Ajouter l'objet JSON
                            JSONString += "{\"description\":\""+description+"\", \"content\":\""+content+"\"}";
                        }
                        else{
                            // Ajouter l'objet JSON
                            JSONString += "{\"description\":\""+description+"\", \"content\":\""+content+"\", \"date\":\""+date+"\"}";
                        }
                        

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
    
    
    
    
    public void cleanNotifications(Integer idPC, ArrayList<String> messages, Boolean Test){
        String RequeteSQL="SELECT idmsg, content FROM notification WHERE idpc = ?";
        Integer idMsg = -1; //Par défaut, renvoyé si table vide
        Integer c = 0;
        Boolean DeleteNotification = false;
        String message = "";
        
        //Selection de la BD
        changeConnection(Test);
        
        
        //Connection BD en tant que postgres
        try (Connection connection =
            DAONotifications.getConnectionPostgres();
                
            //Requête SQL
            PreparedStatement preparedStatement = connection.prepareStatement(RequeteSQL)) {
            
            //Remplacement des "?" par les variables d'entrée (pour éviter les injections SQL !!!)
            preparedStatement.setInt(1, idPC);
            
            // Exécution de la requête
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                
                //On prend le résultat de la 1ere itération si elle existe
                while (resultSet.next()) {
                    //Récupération des info souhaitées
                    idMsg = resultSet.getInt("idmsg");
                    message = resultSet.getString("content");
                    
                    c=0;
                    DeleteNotification = true;
                    while(c < messages.size()){
                        if(messages.get(c).equals(message)){
                            DeleteNotification = false;
                        }
                        
                        c += 1;
                    }
                    
                    if(DeleteNotification == true){
                        deleteNotif(idMsg, Test);
                    }
                }
            } 
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    
    /**
     * Suppression d'une notif dans la BD
     * 
     * @param idMsg     id msg
     * @param Test     Utilisation de la BD test (true si test sinon false !!!)
     */
    public void deleteNotif(Integer idMsg, Boolean Test){
        String RequeteSQL="DELETE FROM notification WHERE idmsg = ?";
        
        //Selection de la BD
        changeConnection(Test);
        
        //Connection BD en tant que postgres
        try (Connection connection =
            DAONotifications.getConnectionPostgres();
                
            //Requête SQL
            PreparedStatement preparedStatement = connection.prepareStatement(RequeteSQL)) {
            
            //Remplacement des "?" par les variables d'entrée (pour éviter les injections SQL !!!)
            preparedStatement.setInt(1, idMsg);
            
            // Exécution de la requête
            int affectedRows = preparedStatement.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    
    
    
    public Boolean doNotifExist(String content, Integer idPC, Boolean Test){
        String RequeteSQL="SELECT content FROM notification WHERE content = ? AND idpc = ?";
        
        Boolean idExist = false;
        
        //Selection de la BD
        changeConnection(Test);
        
        //Connection BD en tant que postgres
        try (Connection connection =
            DAONotifications.getConnectionPostgres();
                
            //Requête SQL
            PreparedStatement preparedStatement = connection.prepareStatement(RequeteSQL)) {
            
            //Remplacement de "?" par le login (pour éviter les injections SQL !!!)
            preparedStatement.setString(1, content);
            preparedStatement.setInt(2, idPC);
            
            
            // Exécution de la requête
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    idExist = true;
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return idExist;
    }
}

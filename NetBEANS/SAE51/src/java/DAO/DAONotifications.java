package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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
     * @param users     utilisateurs qui ont accès à la notif (même format que pour l'accès aux pc "login1/.../loginN")
     * @param Test     Utilisation de la BD test (true si test sinon false !!!)
     */
    public void addNotification(String description, String content, String users, Boolean Test){
        String RequeteSQL="INSERT INTO notification (description, content, users, date) VALUES (?, ?, ?, ?)";
        
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
            preparedStatement.setString(3, users);
            preparedStatement.setString(4, date);
            
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
     * Renvoi les logs contenu dans la BD
     * 
     * @param userLogin     Login des notifs de l'utilisateur donné
     * @param BypassAuth        bypass l'auth et renvoi toutes les notifs
     * @param Test     Utilisation de la BD test (true si test sinon false !!!)
     * @return JSONString       contenu de la table au format JSON (login/prenom/nom/droits)
     */
    public String getNotifications(String userLogin, Boolean BypassAuth, Boolean Test){
        String RequeteSQL="SELECT description, content, users, date FROM notification";
        String content="";
        String description="";
        String users="";
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
                    users = resultSet.getString("users");
                    
                    
                    //Si l'utilisateur se situe dans la liste des utilisateurs qui ont accès au pc (et bypass false)
                    if(users.contains(userLogin) == true && BypassAuth == false){
                        
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
}

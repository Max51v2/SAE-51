package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;

/**
 *
 * @author Maxime VALLET
 */
public class DAOusers {
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
     * Récupération du hash dans la table users en fonction du login donné par l'utilisateur
     * 
     * @param login       &emsp;&emsp;        login donné par l'utilisateur
     * @param Test       &emsp;&emsp;        Utilisation de la BD test (true si test sinon false !!!)
     * @return        hash stocké dans la table
     */
    public String getUserPasswordHash(String login, Boolean Test){
        String RequeteSQL="SELECT * FROM users WHERE login = ?";
        String hashDB="";
        
        //Selection de la BD
        changeConnection(Test);
        
        //Connection BD en tant que postgres
        try (Connection connection =
                DAOusers.getConnectionPostgres();
                
            //Requête SQL
            PreparedStatement preparedStatement = connection.prepareStatement(RequeteSQL)) {
            
            //Remplacement de "?" par le login (pour éviter les injections SQL !!!)
            preparedStatement.setString(1, login);
            
            // Exécution de la requête
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    hashDB = resultSet.getString("hash");
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return hashDB;
    }
    
    
     /**
     * Récupération des droits de l'utilisateur à partir du login
     * 
     * @param login       &emsp;&emsp;        login donné par l'utilisateur
     * @param Test       &emsp;&emsp;        Utilisation de la BD test (true si test sinon false !!!)
     * @return        droits de l'utilisateur
     */
    public String getUserRightsFromLogin(String login, Boolean Test){
        String RequeteSQL="SELECT * FROM users WHERE login = ?";
        String rights="";
        
        //Selection de la BD
        changeConnection(Test);
        
        //Connection BD en tant que postgres
        try (Connection connection =
                DAOusers.getConnectionPostgres();
                
            //Requête SQL
            PreparedStatement preparedStatement = connection.prepareStatement(RequeteSQL)) {
            
            //Remplacement de "?" par le login (pour éviter les injections SQL !!!)
            preparedStatement.setString(1, login);
            
            // Exécution de la requête
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    rights = resultSet.getString("droits");
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return rights;
    }
    
    
    
    /**
     * Récupération des droits de l'utilisateur à partir du token
     * 
     * @param token       &emsp;&emsp;        token stocké dans le navigateur
     * @param Test       &emsp;&emsp;        Utilisation de la BD test (true si test sinon false !!!)
     * @return        droits de l'utilisateur
     */
    public String getUserRightsFromToken(String token, Boolean Test){
        String RequeteSQL="SELECT * FROM users WHERE token = ?";
        String rights="";
        
        //Selection de la BD
        changeConnection(Test);
        
        //Connection BD en tant que postgres
        try (Connection connection =
                DAOusers.getConnectionPostgres();
                
            //Requête SQL
            PreparedStatement preparedStatement = connection.prepareStatement(RequeteSQL)) {
            
            //Remplacement de "?" par le login (pour éviter les injections SQL !!!)
            preparedStatement.setString(1, token);
            
            // Exécution de la requête
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    rights = resultSet.getString("droits");
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return rights;
    }
    
    
    
    
    /**
     * Enregistrement du token
     * 
     * @param login       &emsp;&emsp;        login donné par l'utilisateur
     * @param token       &emsp;&emsp;        token généré par le servlet
     * @param Test       &emsp;&emsp;        Utilisation de la BD test (true si test sinon false !!!)
     */
    public void setToken(String token, String login, Boolean Test){
        String RequeteSQL="UPDATE users SET token = ? WHERE login = ?";
        
        //Selection de la BD
        changeConnection(Test);
        
        //Connection BD en tant que postgres
        try (Connection connection =
                DAOusers.getConnectionPostgres();
                
            //Requête SQL
            PreparedStatement preparedStatement = connection.prepareStatement(RequeteSQL)) {
            
            //Remplacement de "?" n°1 par le token et n°2 par le login (pour éviter les injections SQL !!!)
            preparedStatement.setString(1, token);
            preparedStatement.setString(2, login);
            
            // Exécution de la requête
            int affectedRows = preparedStatement.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

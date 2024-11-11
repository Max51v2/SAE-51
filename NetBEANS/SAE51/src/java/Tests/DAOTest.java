package Tests;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Classe contenant toutes les intéractions avec la BD pour les tests
 * 
 * @author Maxime VALLET
 * @version 0.1
 */
public class DAOTest {
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
     * Ajout d'un utilisateur
     * 
     * @param login     login de l'utilisateur
     * @param nom     nom de l'utilisateur
     * @param prenom     prenom de l'utilisateur
     * @param role     droits de l'utilisateur
     * @param hashedPassword      MDP hashé de l'utilisateur
     * @param token     token de l'utilisateur
     * @param tokenLifeCycle        cycle de vie du token
     * @param Test     Utilisation de la BD test (true si test sinon false !!!)
     */
    public void addUserWithToken(String login, String nom, String prenom, String role, String hashedPassword, Integer tokenLifeCycle, String token, Boolean Test){
        String RequeteSQL="INSERT INTO users (login, nom, prenom, droits, hash, token, tokenlifecycle) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        //Selection de la BD
        changeConnection(Test);
        
        //Connection BD en tant que postgres
        try (Connection connection =
            DAOTest.getConnectionPostgres();
                
            //Requête SQL
            PreparedStatement preparedStatement = connection.prepareStatement(RequeteSQL)) {
            
            //Remplacement des "?" par les variables d'entrée (pour éviter les injections SQL !!!)
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, nom);
            preparedStatement.setString(3, prenom);
            preparedStatement.setString(4, role);
            preparedStatement.setString(5, hashedPassword);
            preparedStatement.setString(6, token);
            preparedStatement.setInt(7, tokenLifeCycle);
            
            // Exécution de la requête
            int affectedRows = preparedStatement.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    
    /**
     * Récupération du token d'un utilisateur
     * 
     * @param login     login de l'utilisateur
     * @param Test     Utilisation de la BD test (true si test sinon false !!!)
     * 
     * @return token
     */
    public String getToken(String login, Boolean Test){
        String RequeteSQL="SELECT token FROM users WHERE login = ?";
        String token = "";
        
        //Selection de la BD
        changeConnection(Test);
        
        //Connection BD en tant que postgres
        try (Connection connection =
            DAOTest.getConnectionPostgres();
                
            //Requête SQL
            PreparedStatement preparedStatement = connection.prepareStatement(RequeteSQL)) {
            
            //Remplacement des "?" par les variables d'entrée (pour éviter les injections SQL !!!)
            preparedStatement.setString(1, login);
            
            // Exécution de la requête
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    token = resultSet.getString("token");
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return token;
    }
    
    
    
    
    /**
     * Récupération du MDP hashé d'un utilisateur
     * 
     * @param login     login de l'utilisateur
     * @param Test     Utilisation de la BD test (true si test sinon false !!!)
     * 
     * @return token
     */
    public String getPassword(String login, Boolean Test){
        String RequeteSQL="SELECT hash FROM users WHERE login = ?";
        String password = "";
        
        //Selection de la BD
        changeConnection(Test);
        
        //Connection BD en tant que postgres
        try (Connection connection =
            DAOTest.getConnectionPostgres();
                
            //Requête SQL
            PreparedStatement preparedStatement = connection.prepareStatement(RequeteSQL)) {
            
            //Remplacement des "?" par les variables d'entrée (pour éviter les injections SQL !!!)
            preparedStatement.setString(1, login);
            
            // Exécution de la requête
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    password = resultSet.getString("hash");
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return password;
    }
}

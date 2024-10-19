package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import org.mindrot.jbcrypt.BCrypt;

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
            
            //Remplacement des "?" par les variables d'entrée (pour éviter les injections SQL !!!)
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
            
            //Remplacement des "?" par les variables d'entrée (pour éviter les injections SQL !!!)
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
            
            //Remplacement des "?" par les variables d'entrée (pour éviter les injections SQL !!!)
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
     * @param lifeCycle       &emsp;&emsp;        durée de vie du token
     * @param Test       &emsp;&emsp;        Utilisation de la BD test (true si test sinon false !!!)
     */
    public void setToken(String token, String login, Integer lifeCycle, Boolean Test){
        String RequeteSQL="UPDATE users SET token = ?, tokenLifeCycle = ? WHERE login = ?";
        
        //Selection de la BD
        changeConnection(Test);
        
        //Connection BD en tant que postgres
        try (Connection connection =
                DAOusers.getConnectionPostgres();
                
            //Requête SQL
            PreparedStatement preparedStatement = connection.prepareStatement(RequeteSQL)) {
            
            //Remplacement des "?" par les variables d'entrée (pour éviter les injections SQL !!!)
            preparedStatement.setString(1, token);
            preparedStatement.setInt(2, lifeCycle);
            preparedStatement.setString(3, login);
            
            // Exécution de la requête
            int affectedRows = preparedStatement.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    
     /**
     * Renvoi les utilisateurs contenu dans la BD
     * 
     * @param Test     Utilisation de la BD test (true si test sinon false !!!)
     * @return JSONString       contenu de la table au format JSON (login/prenom/nom/droits)
     */
    public String getUsers(Boolean Test){
        String RequeteSQL="SELECT * FROM users ORDER BY droits ASC, login ASC";
        String login="";
        String prenom="";
        String nom="";
        String droits="";
        String JSONString="";
        
        //Selection de la BD
        changeConnection(Test);
        
        
        //Connection BD en tant que postgres
        try (Connection connection =
                DAOusers.getConnectionPostgres();
                
            //Requête SQL
            PreparedStatement preparedStatement = connection.prepareStatement(RequeteSQL)) {
            
            // Exécution de la requête
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                Integer c = 1;
                
                // Ouvrir le tableau JSON
                JSONString += "[";

                while (resultSet.next()) {
                    login = resultSet.getString("login");
                    prenom = resultSet.getString("prenom");
                    nom = resultSet.getString("nom");
                    droits = resultSet.getString("droits");

                    // Ajouter une virgule avant chaque entrée sauf la première
                    if (c > 1) {
                        JSONString += ",";
                    }

                    // Ajouter l'objet JSON
                    JSONString += "{\"login\":\"" + login + "\", \"prenom\":\"" + prenom + "\", \"nom\":\"" + nom + "\", \"droits\":\"" + droits + "\"}";

                    c += 1;
                }

                // Fermer le tableau JSON
                JSONString += "]";

            }
            
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return JSONString;
    }
    
    
    
    /**
     * Récupération du cycle de vie du token
     * 
     * @param login       &emsp;&emsp;        login de l'utilisateur
     * @param Test       &emsp;&emsp;        Utilisation de la BD test (true si test sinon false !!!)
     * @return        le cycle de vie du token de l'utilisateur
     */
    public Integer getTokenLifeCycle(String login, Boolean Test){
        String RequeteSQL="SELECT * FROM users WHERE login = ?";
        Integer lifeCycle = 0;
        
        //Selection de la BD
        changeConnection(Test);
        
        //Connection BD en tant que postgres
        try (Connection connection =
                DAOusers.getConnectionPostgres();
                
            //Requête SQL
            PreparedStatement preparedStatement = connection.prepareStatement(RequeteSQL)) {
            
            //Remplacement des "?" par les variables d'entrée (pour éviter les injections SQL !!!)
            preparedStatement.setString(1, login);
            
            // Exécution de la requête
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    lifeCycle = resultSet.getInt("tokenlifecycle");
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return lifeCycle;
    }
    
    
    
    /**
     * suppression du token
     * 
     * @param login       &emsp;&emsp;        login donné par l'utilisateur
     * @param Test       &emsp;&emsp;        Utilisation de la BD test (true si test sinon false !!!)
     */
    public void deleteToken(String login, Boolean Test){
        String RequeteSQL="UPDATE users SET token = ? WHERE login = ?";
        
        //Selection de la BD
        changeConnection(Test);
        
        //Connection BD en tant que postgres
        try (Connection connection =
                DAOusers.getConnectionPostgres();
                
            //Requête SQL
            PreparedStatement preparedStatement = connection.prepareStatement(RequeteSQL)) {
            
            //Remplacement des "?" par les variables d'entrée (pour éviter les injections SQL !!!)
            preparedStatement.setString(1, "");
            preparedStatement.setString(2, login);
            
            // Exécution de la requête
            int affectedRows = preparedStatement.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    
    
    /**
     * Récupération du token
     * 
     * @param login       &emsp;&emsp;        login de l'utilisateur
     * @param Test       &emsp;&emsp;        Utilisation de la BD test (true si test sinon false !!!)
     * @return        le token de l'utilisateur
     */
    public String getToken(String login, Boolean Test){
        String RequeteSQL="SELECT * FROM users WHERE login = ?";
        String token = "";
        
        //Selection de la BD
        changeConnection(Test);
        
        //Connection BD en tant que postgres
        try (Connection connection =
                DAOusers.getConnectionPostgres();
                
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
     * Changement du nombre de cycles du token
     * 
     * @param login       &emsp;&emsp;        login donné par l'utilisateur
     * @param lifeCycle       &emsp;&emsp;        nombre de cycles de vie restants
     * @param Test       &emsp;&emsp;        Utilisation de la BD test (true si test sinon false !!!)
     */
    public void setLifeCycle(String login, Integer lifeCycle, Boolean Test){
        String RequeteSQL="UPDATE users SET tokenlifecycle = ? WHERE login = ?";
        
        //Selection de la BD
        changeConnection(Test);
        
        //Connection BD en tant que postgres
        try (Connection connection =
                DAOusers.getConnectionPostgres();
                
            //Requête SQL
            PreparedStatement preparedStatement = connection.prepareStatement(RequeteSQL)) {
            
            //Remplacement des "?" par les variables d'entrée (pour éviter les injections SQL !!!)
            preparedStatement.setInt(1, lifeCycle);
            preparedStatement.setString(2, login);
            
            // Exécution de la requête
            int affectedRows = preparedStatement.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    
    /**
     * Renvoi les droits et le login de l'utilisateur correspondant au token
     * 
     * @param token     token de l'utilisateur
     * @param Test     Utilisation de la BD test (true si test sinon false !!!)
     * 
     * @return JSONString       contenu de la table au format JSON (login/droits)
     */
    public String checkToken(String token, Boolean Test){
        String RequeteSQL="SELECT * FROM users";
        String login="";
        String droits="";
        String hashedToken="";
        String JSONString="";
        Boolean isTokenOK = false;
        Integer match = 0;
        
        //Selection de la BD
        changeConnection(Test);
        
        
        //Connection BD en tant que postgres
        try (Connection connection =
                DAOusers.getConnectionPostgres();
                
            //Requête SQL
            PreparedStatement preparedStatement = connection.prepareStatement(RequeteSQL)) {
            
            // Exécution de la requête
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    login = resultSet.getString("login");
                    droits = resultSet.getString("droits");
                    hashedToken = resultSet.getString("token");

                    if(hashedToken.equals("")){
                        //Rien
                    }
                    else{
                        isTokenOK = BCrypt.checkpw(token, hashedToken);
                    
                        if(isTokenOK == true){
                            //JSON contenant les données souhaitées
                            JSONString = "{\"login\":\"" + login + "\", \"droits\":\"" + droits + "\", \"erreur\":\"none\"}";

                            //Reset du cycle de vie du token
                            setLifeCycle(login, 24, Test);

                            //Compteur
                            match += 1;
                        }
                    }
                }
            }
            
            // Fermer le tableau JSON
            if(match == 0){
                JSONString = "{\"erreur\":\"pas de token (DB)\"}";
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return JSONString;
    }
}

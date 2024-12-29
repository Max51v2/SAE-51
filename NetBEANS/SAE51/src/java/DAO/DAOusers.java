package DAO;

import Autre.ProjectConfig;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Classe contenant toutes les intéractions avec la BD partie utilisateur<br>
 * 
 * Originaires de la SAE-52 (dev par Maxime VALLET) : getUserRightsFromLogin / getUsers
 *  
 * @author Maxime VALLET
 * @version 1.5
 */
public class DAOusers {
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
     * Récupération du hash dans la table users en fonction du login donné par l'utilisateur
     * 
     * @param login       &emsp;&emsp;        login donné par l'utilisateur
     * @param Test       &emsp;&emsp;        Utilisation de la BD test (true si test sinon false !!!)
     * @return        hash stocké dans la table
     */
    public String getUserPasswordHash(String login, Boolean Test){
        String RequeteSQL="SELECT hash FROM users WHERE login = ?";
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
        String RequeteSQL="SELECT droits FROM users WHERE login = ? ORDER BY tokenlifecycle DESC";
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
        String RequeteSQL="SELECT token, droits, login FROM users WHERE token != '' ORDER BY tokenlifecycle DESC";
        String droits="Aucun";
        String hashedToken="";
        Boolean isTokenOK = false;
        loginLog = "Aucun";
        
        //Selection de la BD
        changeConnection(Test);
        
        
        //Connection BD en tant que postgres
        try (Connection connection =
            DAOusers.getConnectionPostgres();
                
            //Requête SQL
            PreparedStatement preparedStatement = connection.prepareStatement(RequeteSQL)) {
            
            // Exécution de la requête
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next() & isTokenOK == false) {
                    //Données BD
                    hashedToken = resultSet.getString("token");

                    //Comparaison token utilisateur et le token de la BD
                    isTokenOK = BCrypt.checkpw(token, hashedToken);

                    if(isTokenOK == true){
                        //Données BD
                        droits = resultSet.getString("droits");
                        loginLog = resultSet.getString("login");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return droits;
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
        String RequeteSQL="SELECT login, prenom, nom, droits FROM users ORDER BY droits ASC, login ASC";
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
        String RequeteSQL="SELECT tokenlifecycle FROM users WHERE login = ?";
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
        String RequeteSQL="SELECT token FROM users WHERE login = ?";
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
        String RequeteSQL="SELECT login, droits, token FROM users WHERE token != '' ORDER BY tokenlifecycle DESC";
        String login="";
        String droits="";
        String hashedToken="";
        String JSONString="";
        Boolean isTokenOK = false;
        Integer match = 0;
        loginLog = "Aucun";
        
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
                    //Si on a déjà trouvé le token dans la BD, on arrête de chercher
                    if(isTokenOK == true){
                        
                    }
                    else{
                        //Données BD
                        hashedToken = resultSet.getString("token");

                        //Comparaison token utilisateur et le token de la BD
                        isTokenOK = BCrypt.checkpw(token, hashedToken);

                        if(isTokenOK == true){
                            //Données BD
                            login = resultSet.getString("login");
                            droits = resultSet.getString("droits");
                            loginLog = login;
                        
                            //JSON contenant les données souhaitées
                            JSONString = "{\"login\":\"" + login + "\", \"droits\":\"" + droits + "\", \"erreur\":\"none\"}";

                            //Reset du cycle de vie du token
                            Integer TokenLifeCycle = conf.getIntValue("TokenLifeCycle");
                            setLifeCycle(login, TokenLifeCycle, Test);

                            //Compteur
                            match += 1;
                        }
 
                    }
                }
            }
            
            //Pas de token dans la BD
            if(match == 0){
                JSONString = "{\"erreur\":\"pas de token (DB)\"}";
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return JSONString;
    }
    
    
    
    
    /**
     * Suppression d'un utilisateur
     * 
     * @param login       &emsp;&emsp;        login donné par l'utilisateur
     * @param Test       &emsp;&emsp;        Utilisation de la BD test (true si test sinon false !!!)
     */
    public void deleteUser(String login, Boolean Test){
        String RequeteSQL="DELETE FROM users WHERE login = ?";
        
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
            int affectedRows = preparedStatement.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    
    
     /**
     * Renvoi les utilisateurs possédant un token contenu dans la BD
     * 
     * @param Test     Utilisation de la BD test (true si test sinon false !!!)
     * @return JSONString       contenu de la table au format JSON (login/prenom/nom/droits)
     */
    public String getActiveUsers(Boolean Test){
        String RequeteSQL="SELECT login, prenom, nom, droits FROM users WHERE token != '' ORDER BY droits ASC, login ASC";
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
     * Vérifie l'existance du login dans la base de données
     * 
     * @param login     login donné par l'utilisateur
     * @param Test     Utilisation de la BD test (true si test sinon false !!!)
     * @return loginExist       éxsitance du login (booléen)
     */
    public Boolean doLoginExist(String login, Boolean Test){
        String RequeteSQL="SELECT login FROM users WHERE login = ?";
        String loginDB="";
        Boolean loginExist = false;
        
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
                    loginDB = resultSet.getString("login");
                }
            }
            
            //Vérification du login renvoyé
            if(login.equals(loginDB)){
                loginExist = true;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return loginExist;
    }
    
    
    
    
    /**
     * Ajout d'un utilisateur
     * 
     * @param login     login de l'utilisateur
     * @param nom     nom de l'utilisateur
     * @param prenom     prenom de l'utilisateur
     * @param role     droits de l'utilisateur
     * @param hashedPassword      MDP hashé de l'utilisateur
     * @param Test     Utilisation de la BD test (true si test sinon false !!!)
     */
    public void addUser(String login, String nom, String prenom, String role, String hashedPassword, Boolean Test){
        String RequeteSQL="INSERT INTO users (login, nom, prenom, droits, hash, token, tokenlifecycle) VALUES (?, ?, ?, ?, ?,'', 0)";
        
        //Selection de la BD
        changeConnection(Test);
        
        //Connection BD en tant que postgres
        try (Connection connection =
            DAOusers.getConnectionPostgres();
                
            //Requête SQL
            PreparedStatement preparedStatement = connection.prepareStatement(RequeteSQL)) {
            
            //Remplacement des "?" par les variables d'entrée (pour éviter les injections SQL !!!)
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, nom);
            preparedStatement.setString(3, prenom);
            preparedStatement.setString(4, role);
            preparedStatement.setString(5, hashedPassword);
            
            // Exécution de la requête
            int affectedRows = preparedStatement.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    
    
    /**
     * Renvoi sur quelle page l'utilisateur doit être redirigé
     * 
     * @param droits     Droits de l'utilisateur connecté
     * @param currentPage     Page sur laquelle se situe l'utilisateur
     * @param Test     Utilisation de la BD test (true si test sinon false !!!)
     * @return JSONString       contenu de la table au format JSON (login/prenom/nom/droits)
     */
    public String getRedirection(String droits, String currentPage, Boolean Test){
        String RequeteSQL="SELECT redirect FROM web_pages_access WHERE name = ? AND droits = ?";
        String redirect="";
        
        //Selection de la BD
        changeConnection(Test);
        
        
        //Connection BD en tant que postgres
        try (Connection connection =
            DAOusers.getConnectionPostgres();
                
            //Requête SQL
            PreparedStatement preparedStatement = connection.prepareStatement(RequeteSQL)) {
            
            //Remplacement des "?" par les variables d'entrée (pour éviter les injections SQL !!!)
            preparedStatement.setString(1, currentPage);
            preparedStatement.setString(2, droits);
            
            // Exécution de la requête
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    redirect = resultSet.getString("redirect");
                }
                else{
                    redirect = "No redirect";
                }
            }
            
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return redirect;
    }
    
    
    
    
    /**
     * Modifie le MDP d'un utilisateur
     * 
     * @param hashedPassword      MDP hashé de l'utilisateur
     * @param login     login de l'utilisateur
     * @param Test     Utilisation de la BD test (true si test sinon false !!!)
     */
    public void setPassword(String login, String hashedPassword, Boolean Test){
        String RequeteSQL="UPDATE users SET hash = ? WHERE login = ?";
        
        //Selection de la BD
        changeConnection(Test);
        
        //Connection BD en tant que postgres
        try (Connection connection =
            DAOusers.getConnectionPostgres();
                
            //Requête SQL
            PreparedStatement preparedStatement = connection.prepareStatement(RequeteSQL)) {
            
            //Remplacement des "?" par les variables d'entrée (pour éviter les injections SQL !!!)
            preparedStatement.setString(1, hashedPassword);
            preparedStatement.setString(2, login);
            
            // Exécution de la requête
            int affectedRows = preparedStatement.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    
    
    /**
     * Renvoi le login de l'utilisateur correspondant au token
     * 
     * @param token     token de l'utilisateur
     * @param Test     Utilisation de la BD test (true si test sinon false !!!)
     * 
     * @return JSONString       contenu de la table au format JSON (login/droits)
     */
    public String getUserLoginFromToken(String token, Boolean Test){
        String RequeteSQL="SELECT login, token FROM users WHERE token != '' ORDER BY tokenlifecycle DESC";
        String login="";
        String hashedToken="";
        String JSONString="";
        Boolean isTokenOK = false;
        
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
                    //Si on a déjà trouvé le token dans la BD, on arrête de chercher
                    if(isTokenOK == true){
                        
                    }
                    else{
                        //Données BD
                        hashedToken = resultSet.getString("token");

                        //Comparaison token utilisateur et le token de la BD
                        isTokenOK = BCrypt.checkpw(token, hashedToken);

                        if(isTokenOK == true){
                            //Données BD
                            login = resultSet.getString("login");
                        }
                    }
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return login;
    }
    
    
    
    
    /**
     * Renvoi les droits d'accès au servlet
     * 
     * @param name     nom de l'utilisateur connecté
     * @param rights     droits de l'utilisateur
     * @param Test     Utilisation de la BD test (true si test sinon false !!!)
     * @return JSONString       contenu de la table au format JSON (login/prenom/nom/droits)
     */
    public String getServletRights(String name, String rights, Boolean Test){
        String RequeteSQL="SELECT access FROM servlet_access WHERE name = ? AND role = ?";
        String access="false";
        
        //Selection de la BD
        changeConnection(Test);
        
        
        //Connection BD en tant que postgres
        try (Connection connection =
            DAOusers.getConnectionPostgres();
                
            //Requête SQL
            PreparedStatement preparedStatement = connection.prepareStatement(RequeteSQL)) {
            
            //Remplacement des "?" par les variables d'entrée (pour éviter les injections SQL !!!)
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, rights);
            
            // Exécution de la requête
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    access = resultSet.getString("access");
                }
            }
            
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return access;
    }
    
    
    
    
    /**
     * Récupération du login pour le loger
     * 
     * @return loginLogBackup       login de l'utilisateur lors de la dernière vérification
     */
    public String getLogin(){
        String loginLogBackup = loginLog;
        loginLog = "Aucun";
        
        return loginLogBackup;
    }
}
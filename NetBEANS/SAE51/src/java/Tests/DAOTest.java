package Tests;

import DAO.DAOPC;
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
    
    
    
    /**
     * Renvoi les pc contenu dans la BD
     * 
     * @param Test     Utilisation de la BD test (true si test sinon false !!!)
     * @return JSONString       contenu de la table au format JSON (login/prenom/nom/droits)
     */
    public String getPC(Boolean Test){
        String RequeteSQL="SELECT ip, id FROM pc ORDER BY id ASC";
        String IP="";
        String id="";
        String JSONString="";
        
        //Selection de la BD
        changeConnection(Test);
        
        
        //Connection BD en tant que postgres
        try (Connection connection =
            DAOTest.getConnectionPostgres();
                
            //Requête SQL
            PreparedStatement preparedStatement = connection.prepareStatement(RequeteSQL)) {
            
            // Exécution de la requête
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                Integer c = 1;
                
                // Ouvrir le tableau JSON
                JSONString += "[";

                while (resultSet.next()) {
                    IP = resultSet.getString("ip");
                    id = resultSet.getString("id");

                    // Ajouter une virgule avant chaque entrée sauf la première
                    if (c > 1) {
                        JSONString += ",";
                    }

                    // Ajouter l'objet JSON
                    JSONString += "{\"IP\":\"" + IP + "\", \"id\":\"" + id + "\"}";

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
     * Renvoi les info statiques de tous les pc contenu dans la BD (pas testé)
     * 
     * @param Test     Utilisation de la BD test (true si test sinon false !!!)
     * @return JSONString       contenu de la table au format JSON (login/prenom/nom/droits)
     */
    public String getPCStaticInfo(Boolean Test){
        String RequeteSQL="SELECT * FROM pc_static_info ORDER BY id ASC";
        String id = "";
        String cpu_model = "";
        Integer cores = null;
        Integer threads = null;
        String maximum_frequency = "";
        String ram_quantity = "";
        Integer dimm_quantity = null;
        String dimm_speed = "";
        Integer storage_device_number = null;
        String storage_space = "";
        Integer network_int_number = null;
        String network_int_speed = "";
        String os = "";
        String version = "";
        String JSONString="";
        
        //Selection de la BD
        changeConnection(Test);
        
        
        //Connection BD en tant que postgres
        try (Connection connection =
            DAOTest.getConnectionPostgres();
                
            //Requête SQL
            PreparedStatement preparedStatement = connection.prepareStatement(RequeteSQL)) {
            
            // Exécution de la requête
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                Integer c = 1;
                
                // Ouvrir le tableau JSON
                JSONString += "[";

                while (resultSet.next()) {
                    id = resultSet.getString("id");
                    cpu_model = resultSet.getString("cpu_model");
                    cores = resultSet.getInt("cores");
                    threads = resultSet.getInt("threads");
                    maximum_frequency = resultSet.getString("maximum_frequency");
                    ram_quantity = resultSet.getString("ram_quantity");
                    dimm_quantity = resultSet.getInt("dimm_quantity");
                    dimm_speed = resultSet.getString("dimm_speed");
                    storage_device_number = resultSet.getInt("storage_device_number");
                    storage_space = resultSet.getString("storage_space");
                    network_int_number = resultSet.getInt("network_int_number");
                    network_int_speed = resultSet.getString("network_int_speed");
                    os = resultSet.getString("os");
                    version = resultSet.getString("version");

                    // Ajouter une virgule avant chaque entrée sauf la première
                    if (c > 1) {
                        JSONString += ",";
                    }

                    // Ajouter l'objet JSON
                    JSONString += "{"
                            + "\"id\":\""+id+"\","
                            + "\"cpu_model\":\""+cpu_model+"\","
                            + "\"cores\":\""+cores+"\","
                            + "\"threads\":\""+threads+"\","
                            + "\"maximum_frequency\":\""+maximum_frequency+"\","
                            + "\"ram_quantity\":\""+ram_quantity+"\","
                            + "\"dimm_quantity\":\""+dimm_quantity+"\","
                            + "\"dimm_speed\":\""+dimm_speed+"\","
                            + "\"storage_device_number\":\""+storage_device_number+"\","
                            + "\"storage_space\":\""+storage_space+"\","
                            + "\"network_int_number\":\""+network_int_number+"\","
                            + "\"network_int_speed\":\""+network_int_speed+"\","
                            + "\"os\":\""+os+"\","
                            + "\"version\":\""+version+"\""
                            + "}";

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
     * Renvoi les pc contenu dans la BD
     * 
     * @param Test     Utilisation de la BD test (true si test sinon false !!!)
     * @return JSONString       contenu de la table au format JSON (login/prenom/nom/droits)
     */
    public String getAllPC(Boolean Test){
        String RequeteSQL="SELECT * FROM pc ORDER BY id ASC";
        String IP="";
        String id="";
        String droits = "";
        String JSONString="";
        
        //Selection de la BD
        changeConnection(Test);
        
        
        //Connection BD en tant que postgres
        try (Connection connection =
            DAOTest.getConnectionPostgres();
                
            //Requête SQL
            PreparedStatement preparedStatement = connection.prepareStatement(RequeteSQL)) {
            
            // Exécution de la requête
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                Integer c = 1;
                
                // Ouvrir le tableau JSON
                JSONString += "[";

                while (resultSet.next()) {
                    IP = resultSet.getString("ip");
                    id = resultSet.getString("id");

                    // Ajouter une virgule avant chaque entrée sauf la première
                    if (c > 1) {
                        JSONString += ",";
                    }

                    // Ajouter l'objet JSON
                    JSONString += "{\"IP\":\"" + IP + "\", \"id\":\"" + id + "\"}";

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
}

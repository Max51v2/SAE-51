package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @author Maxime VALLET
 */
public class DAOLogs {
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
     * @param servlet     nom du servlet qui ajoute le log
     * @param ip     adresse IP de l'utilisateur
     * @param login     login de l'utilisateur
     * @param droits     droits de l'utilisateur
     * @param Test     Utilisation de la BD test (true si test sinon false !!!)
     */
    public void addLog(String servlet, String ip, String login, String droits, String error, Boolean Test){
        String RequeteSQL="INSERT INTO logs (servlet, ip, login, droits, error, date) VALUES (?, ?, ?, ?, ?, ?)";
        
        //Selection de la BD
        changeConnection(Test);
        
        //Connection BD en tant que postgres
        try (Connection connection =
            DAOLogs.getConnectionPostgres();
                
            //Requête SQL
            PreparedStatement preparedStatement = connection.prepareStatement(RequeteSQL)) {
            
            //Récupération de la date actuelle
            String date = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
            
            //Remplacement des "?" par les variables d'entrée (pour éviter les injections SQL !!!)
            preparedStatement.setString(1, servlet);
            preparedStatement.setString(2, ip);
            preparedStatement.setString(3, login);
            preparedStatement.setString(4, droits);
            preparedStatement.setString(5, error);
            preparedStatement.setString(6, date);
            
            // Exécution de la requête
            int affectedRows = preparedStatement.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

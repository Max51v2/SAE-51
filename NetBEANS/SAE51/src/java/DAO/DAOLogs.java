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
    
    
    
    
    /**
     * Renvoi les logs contenu dans la BD
     * 
     * @param Test     Utilisation de la BD test (true si test sinon false !!!)
     * @return JSONString       contenu de la table au format JSON (login/prenom/nom/droits)
     */
    public String getLogsFromPeriod(String beginDate, String endDate, String logLevelReq, Boolean Test){
        String RequeteSQL="SELECT * FROM logs ORDER BY id ASC";
        Integer id=null;
        String prenom="";
        String servlet="";
        String ip="";
        String login="";
        String droits="";
        String error="";
        String JSONString="";
        String Date="";
        
        //Selection de la BD
        changeConnection(Test);
        
        //Dates
        String beginDate1 = beginDate.substring(0,beginDate.indexOf("_"));
        String beginDate2 = beginDate.substring(beginDate.indexOf("_")+1, beginDate.length());
        Long beginDate3 = Long.valueOf(beginDate1+beginDate2);
        String endDate1 = endDate.substring(0,endDate.indexOf("_"));
        String endDate2 = endDate.substring(endDate.indexOf("_")+1, endDate.length());
        Long endDate3 = Long.valueOf(endDate1+endDate2);
        
        //Connection BD en tant que postgres
        try (Connection connection =
            DAOLogs.getConnectionPostgres();
                
            //Requête SQL
            PreparedStatement preparedStatement = connection.prepareStatement(RequeteSQL)) {
            
            // Exécution de la requête
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                Integer c = 1;
                
                // Ouvrir le tableau JSON
                JSONString += "[";

                while (resultSet.next()) {
                    //Date log
                    Date = resultSet.getString("date");
                    String Date1 = Date.substring(0,Date.indexOf("_"));
                    String Date2 = Date.substring(Date.indexOf("_")+1, Date.length());
                    Long Date3 = Long.valueOf(Date1+Date2);
                    
                    //Si le log se situe dans la période souhaitée
                    if(Date3 >= beginDate3 & Date3 <= endDate3){
                        id = resultSet.getInt("id");
                        servlet = resultSet.getString("servlet");
                        ip = resultSet.getString("ip");
                        login = resultSet.getString("login");
                        droits = resultSet.getString("droits");
                        error = resultSet.getString("error");
                        
                        //Vérification du champ error pour filtrer le type de req souhaitée
                        if((error.equals("none") & logLevelReq.equals("All")) | (!error.equals("none") & logLevelReq.equals("All")) | (!error.equals("none") & logLevelReq.equals("ErrorsOnly"))){
                            //Remise en ordre de la date
                            String year = Date1.substring(0,4);
                            String month = Date1.substring(4,6);
                            String day = Date1.substring(6,8);
                            String hour = Date2.substring(0,2);
                            String minute = Date2.substring(2,4);
                            String second = Date2.substring(4,6);
                            Date = day+"/"+month+"/"+year+" | "+hour+":"+minute+":"+second;

                            // Ajouter une virgule avant chaque entrée sauf la première
                            if (c > 1) {
                                JSONString += ",";
                            }

                            // Ajouter l'objet JSON
                            JSONString += "{\"id\":\""+id+"\", \"servlet\":\""+servlet+"\", \"ip\":\""+ip+"\", \"login\":\""+login+"\", \"droits\":\""+droits+"\", \"error\":\""+error+"\", \"date\":\""+Date+"\"}";

                            c += 1;
                        }
                        else{
                            //On ne log pas
                        }
                    }
                    //Si on la dépasse, on arrête le while
                    else if(Date3 > endDate3){
                        break;
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

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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe contenant toutes les intéractions avec la BD partie PC
 * 
 * @author Maxime VALLET
 * @version 0.6
 */
public class DAOPC {
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
     * Vérifie l'existance de l'ID dans la base de données
     * 
     * @param id     id de la machine
     * @param Test     Utilisation de la BD test (true si test sinon false !!!)
     * @return loginExist       éxsitance du login (booléen)
     */
    public Boolean doIDExist(Integer id, Boolean Test){
        String RequeteSQL="SELECT id FROM pc WHERE id = ?";
        
        Boolean idExist = false;
        
        //Selection de la BD
        changeConnection(Test);
        
        //Connection BD en tant que postgres
        try (Connection connection =
            DAOPC.getConnectionPostgres();
                
            //Requête SQL
            PreparedStatement preparedStatement = connection.prepareStatement(RequeteSQL)) {
            
            //Remplacement de "?" par le login (pour éviter les injections SQL !!!)
            preparedStatement.setInt(1, id);
            
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
    
    
    
    
    /**
     * Ajout d'un ordinateur dans la BD
     * 
     * @param id     id de la machine
     * @param IP     IP de la machine
     * @param rights        droits d'accès ("login1/.../loginN") / ne rien mettre hors test car droits ajoutés par l'admin via web
     * @param Test     Utilisation de la BD test (true si test sinon false !!!)
     */
    public void addPC(Integer id, String IP, String rights, Boolean Test){
        String RequeteSQL="INSERT INTO pc (id, ip, droits) VALUES (?, ?, ?)";
        
        //Selection de la BD
        changeConnection(Test);
        
        //Connection BD en tant que postgres
        try (Connection connection =
            DAOPC.getConnectionPostgres();
                
            //Requête SQL
            PreparedStatement preparedStatement = connection.prepareStatement(RequeteSQL)) {
            
            //Remplacement des "?" par les variables d'entrée (pour éviter les injections SQL !!!)
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, IP);
            preparedStatement.setString(3, rights);
            
            // Exécution de la requête
            int affectedRows = preparedStatement.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    
    
    /**
     * Suppression d'un ordinateur dans la BD
     * 
     * @param id     id de la machine
     * @param Test     Utilisation de la BD test (true si test sinon false !!!)
     */
    public void deletePC(Integer id, Boolean Test){
        String RequeteSQL="DELETE FROM pc WHERE id = ?";
        
        //Selection de la BD
        changeConnection(Test);
        
        //Connection BD en tant que postgres
        try (Connection connection =
            DAOPC.getConnectionPostgres();
                
            //Requête SQL
            PreparedStatement preparedStatement = connection.prepareStatement(RequeteSQL)) {
            
            //Remplacement des "?" par les variables d'entrée (pour éviter les injections SQL !!!)
            preparedStatement.setInt(1, id);
            
            // Exécution de la requête
            int affectedRows = preparedStatement.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        //Suppression de la table contenant les infos statiques
        RequeteSQL="DELETE FROM pc_static_info WHERE id = ?";
        
        //Selection de la BD
        changeConnection(Test);
            
        //Connection BD en tant que postgres
        try (Connection connection =
            DAOPC.getConnectionPostgres();
                
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
    
    
    
    
     /**
     * Renvoi les pc contenu dans la BD
     * 
     * @param login     login de l'utilisateur qui fait la demande
     * @param rights    droits de l'utilisateur qui fait la demande
     * @param Test     Utilisation de la BD test (true si test sinon false !!!)
     * @return JSONString       contenu de la table au format JSON (login/prenom/nom/droits)
     */
    public String getPC(String login, String rights, Boolean Test){
        String RequeteSQL="SELECT * FROM pc ORDER BY id ASC";
        String IP="";
        Integer id=null;
        String droits = "";
        String JSONString="";
        
        //Selection de la BD
        changeConnection(Test);
        
        
        //Connection BD en tant que postgres
        try (Connection connection =
            DAOPC.getConnectionPostgres();
                
            //Requête SQL
            PreparedStatement preparedStatement = connection.prepareStatement(RequeteSQL)) {
            
            // Exécution de la requête
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                Integer c = 1;
                
                // Ouvrir le tableau JSON
                JSONString += "[";

                while (resultSet.next()) {
                    droits = resultSet.getString("droits");
                    
                    //Vérification des droits de l'utilisateur (login utilisateur compris dans la liste des personnes autorisées ou admin)
                    if(droits.contains(login) | rights.equals("Admin")){
                        IP = resultSet.getString("ip");
                        id = resultSet.getInt("id");

                        // Ajouter une virgule avant chaque entrée sauf la première
                        if (c > 1) {
                            JSONString += ",";
                        }

                        // Ajouter l'objet JSON
                        JSONString += "{\"IP\":\"" + IP + "\", \"id\":\"" + id + "\"}";

                        c += 1;
                    }
                    else{
                        //Rien car il n'a pas les droits d'accès
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
    
    
    
    
    /**
     * Ajoute les info statiques d'un ordinateur à la BD
     * 
     * @param id     id de la machine
     * @param cpu_model     modèle du procésseur
     * @param cores     nombre de coeurs du CPU
     * @param threads    nombre de threads du CPu
     * @param maximum_frequency     fréquence max du procésseur (boost)
     * @param ram_quantity      capacitée totale de la RAM
     * @param dimm_quantity     nombre de barrettes de RAM
     * @param dimm_speed        vitesse des barrettes de RAM
     * @param storage_device_number     nombre de périphériques de stockages
     * @param storage_space     capacité de stockage total
     * @param network_int_number        nombre d'interfaces
     * @param network_int_speed     vitesse des interfaces
     * @param os        OS utilisé
     * @param version   version de l'OS
     * @param Test     Utilisation de la BD test (true si test sinon false !!!)
     */
    public void addPCStaticInfo(Integer id, String cpu_model, Integer cores, Integer threads, String maximum_frequency,
            String ram_quantity, Integer dimm_quantity, String dimm_speed, Integer storage_device_number, String storage_space,
            Integer network_int_number, String network_int_speed, String os, String version, Boolean Test){
        String RequeteSQL = "UPDATE pc_static_info SET cpu_model = ?, cores = ?, threads = ?, maximum_frequency = ?, ram_quantity = ?, dimm_quantity = ?, dimm_speed = ?, storage_device_number = ?, storage_space = ?, network_int_number = ?, network_int_speed = ?, os = ?, version = ? WHERE id = ?";
    
        // Sélection de la BD
        changeConnection(Test);
    
    // Connection à la BD en tant que postgres
        try (Connection connection = DAOPC.getConnectionPostgres();
            PreparedStatement preparedStatement = connection.prepareStatement(RequeteSQL)) {
        
            // Remplacement des "?" par les valeurs d'entrée
            preparedStatement.setString(1, cpu_model);
            preparedStatement.setInt(2, cores);
            preparedStatement.setInt(3, threads);
            preparedStatement.setString(4, maximum_frequency);
            preparedStatement.setString(5, ram_quantity);
            preparedStatement.setInt(6, dimm_quantity);
            preparedStatement.setString(7, dimm_speed);
            preparedStatement.setInt(8, storage_device_number);
            preparedStatement.setString(9, storage_space);
            preparedStatement.setInt(10, network_int_number);
            preparedStatement.setString(11, network_int_speed);
            preparedStatement.setString(12, os);
            preparedStatement.setString(13, version);
            preparedStatement.setInt(14, id); // Ajout de l'ID pour la condition WHERE

            // Exécution de la requête
            int affectedRows = preparedStatement.executeUpdate();
        
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    
    
    /**
     * Renvoi les info statiques d'un pc contenu dans la BD
     * 
     * @param idPC      id du pc
     * @param login     login de l'utilisateur qui aura accès à ces données
     * @param rights        droits de l'utilisateur en question
     * @param Test     Utilisation de la BD test (true si test sinon false !!!)
     * @return JSONString       contenu de la table au format JSON (login/prenom/nom/droits)
     */
    public String getPCStaticInfo(Integer idPC, String login, String rights, Boolean Test){
        
        String JSONString = "" ;
        
        //Vérification des droits d'accès au PC
        Boolean getInfo = getUserPCAccess(idPC, login, Test);
        
        //Si l'utilisateur a les droits d'accès au pc
        if(getInfo == true){
            //Récupération des infos statiques du PC
            String RequeteSQL="SELECT * FROM pc_static_info WHERE id = ?";
            Integer id = null;
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

            //Selection de la BD
            changeConnection(Test);


            //Connection BD en tant que postgres
            try (Connection connection =
                DAOPC.getConnectionPostgres();

                //Requête SQL
                PreparedStatement preparedStatement = connection.prepareStatement(RequeteSQL)) {
                
                //Remplacement des "?" par les variables d'entrée (pour éviter les injections SQL !!!)
                preparedStatement.setInt(1, idPC);

                // Exécution de la requête
                try (ResultSet resultSet = preparedStatement.executeQuery()) {

                    if (resultSet.next()) {
                        id = resultSet.getInt("id");
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



                        // Ajouter l'objet JSON
                        JSONString = "{"
                                + "\"erreur\":\"none\","
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
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else{
            JSONString = "{\"erreur\":\"accès refusé\"}";
        }
        
        return JSONString;
    }
    
    
    
    /**
     * Renvoi le droit d'accès d'un utilisateur à une machine
     * 
     * @param Test     Utilisation de la BD test (true si test sinon false !!!)
     * @return getInfo       droits d'accès
     */
    public Boolean getUserPCAccess(Integer idPC, String login, Boolean Test){
        
        //Vérification des droits d'accès au pc
        String RequeteSQL="SELECT droits FROM pc WHERE id = ?";
        String droits = "";
        Boolean getInfo = false;
        DAOusers DAO2 = new DAOusers();
        String rights= DAO2.getUserRightsFromLogin(login, Test);
        
        //Selection de la BD
        changeConnection(Test);
        
        
        //Connection BD en tant que postgres
        try (Connection connection =
            DAOPC.getConnectionPostgres();
                
            //Requête SQL
            PreparedStatement preparedStatement = connection.prepareStatement(RequeteSQL)) {
            
            //Remplacement des "?" par les variables d'entrée (pour éviter les injections SQL !!!)
            preparedStatement.setInt(1, idPC);
            
            // Exécution de la requête
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                
                if (resultSet.next()) {
                    droits = resultSet.getString("droits");
                    
                    //Vérification des droits de l'utilisateur (login utilisateur compris dans la liste des personnes autorisées ou admin)
                    if(droits.contains(login) | rights.equals("Admin")){
                        getInfo = true;
                    }
                }
            } 
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return getInfo;
    }
    
    
    
    
    /**
     * Renvoi les utilisateurs qui ont ou non le droit d'accéder à un pc
     * 
     * @param idPC      id du pc
     * @param hasAccess     défini si on renvoi les utilisateurs qui ont accès au pc ou non
     * @param Test     Utilisation de la BD test (true si test sinon false !!!)
     * @return JSONString       contenu de la table au format JSON (login/prenom/nom/droits)
     */
    public String getUsersDependingOnPermissions(Integer idPC,Boolean hasAccess, Boolean Test){
        
        //Requête SQL
        String RequeteSQL="SELECT login FROM users";
        
        String JSONString = "" ;
        String login = "";
        Boolean getInfo = null;
        String rights = "";
        
        //Selection de la BD
        changeConnection(Test);
        
        
        //Connection BD en tant que postgres
        try (Connection connection =
            DAOPC.getConnectionPostgres();
                
            //Requête SQL
            PreparedStatement preparedStatement = connection.prepareStatement(RequeteSQL)) {
            
            // Exécution de la requête
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                Integer c = 1;
                
                // Ouvrir le tableau JSON
                JSONString += "[";
                
                while (resultSet.next()) {
                    login = resultSet.getString("login");
                    
                    //Vérification des droits d'accès au PC par l'utilisateur actuel
                    getInfo = getUserPCAccess(idPC, login, Test);
                    
                    //Si les droits correspondent au type d'accès souhaité alors on ajoute l'utilisateur au JSON
                    if(Objects.equals(getInfo, hasAccess)){
                        
                        // Ajouter une virgule avant chaque entrée sauf la première
                        if (c > 1) {
                            JSONString += ",";
                        }
                        
                        //Vérification des droits utilisateur
                        DAOusers DAO2 = new DAOusers();
                        rights= DAO2.getUserRightsFromLogin(login, Test);
        
                        //On dit s'il faut ajouter un bouton (pour éviter le retrait de l'admin car il a les droits par défaut)
                        if(rights.equals("Admin")){
                            JSONString += "{\"user\":\""+login+"\", \"canBeDeleted\":\"false\"}";
                        }
                        else{
                            JSONString += "{\"user\":\""+login+"\", \"canBeDeleted\":\"true\"}";
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
    
    
    
    
    /**
     * Ajoute les droits d'accès à un utilisateur pour une machine donnée
     * 
     * @param idPC      id du PC
     * @param login     login de l'utilisateur concerné
     * @param Test     Utilisation de la BD test (true si test sinon false !!!)
     */
    public void addUserToPC(Integer idPC, String login, Boolean Test){
        
        //Vérification des droits d'accès au pc
        String RequeteSQL="UPDATE pc SET droits = ? WHERE id = ?";
        
        //modification de la liste des droits d'accès
        String loginList = getUsersWithPCAccess(idPC, Test);
        loginList = getRefactoredList(loginList, 1, login, Test);
        
        //Selection de la BD
        changeConnection(Test);
        
        
        //Connection BD en tant que postgres
        try (Connection connection =
            DAOPC.getConnectionPostgres();
                
            //Requête SQL
            PreparedStatement preparedStatement = connection.prepareStatement(RequeteSQL)) {
            
            //Remplacement des "?" par les variables d'entrée (pour éviter les injections SQL !!!)
            preparedStatement.setString(1, loginList);
            preparedStatement.setInt(2, idPC);
            
            // Exécution de la requête
            int affectedRows = preparedStatement.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    
    
    /**
     * Supprime les droits d'accès à un utilisateur pour une machine donnée
     * 
     * @param idPC      id du PC
     * @param login     login de l'utilisateur concerné
     * @param Test     Utilisation de la BD test (true si test sinon false !!!)
     */
    public void deleteUserFromPC(Integer idPC, String login, Boolean Test){
        
        //Vérification des droits d'accès au pc
        String RequeteSQL="UPDATE pc SET droits = ? WHERE id = ?";
        
        //modification de la liste des droits d'accès
        String loginList = getUsersWithPCAccess(idPC, Test);
        loginList = getRefactoredList(loginList, 2, login, Test);
        
        //Selection de la BD
        changeConnection(Test);
        
        
        //Connection BD en tant que postgres
        try (Connection connection =
            DAOPC.getConnectionPostgres();
                
            //Requête SQL
            PreparedStatement preparedStatement = connection.prepareStatement(RequeteSQL)) {
            
            //Remplacement des "?" par les variables d'entrée (pour éviter les injections SQL !!!)
            preparedStatement.setString(1, loginList);
            preparedStatement.setInt(2, idPC);
            
            // Exécution de la requête
            int affectedRows = preparedStatement.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    
    
    /**
     * Renvoi le droit d'accès d'un utilisateur à une machine
     * 
     * @param idPC      id du PC
     * @param Test     Utilisation de la BD test (true si test sinon false !!!)
     * @return droits       liste de login d'utilisateurs ayant accès au PC
     */
    public String getUsersWithPCAccess(Integer idPC, Boolean Test){
        
        //Vérification des droits d'accès au pc
        String RequeteSQL="SELECT droits FROM pc WHERE id = ?";
        
        String droits = "";
        
        //Selection de la BD
        changeConnection(Test);
        
        
        //Connection BD en tant que postgres
        try (Connection connection =
            DAOPC.getConnectionPostgres();
                
            //Requête SQL
            PreparedStatement preparedStatement = connection.prepareStatement(RequeteSQL)) {
            
            //Remplacement des "?" par les variables d'entrée (pour éviter les injections SQL !!!)
            preparedStatement.setInt(1, idPC);
            
            // Exécution de la requête
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                
                if (resultSet.next()) {
                    droits = resultSet.getString("droits");
                }
            } 
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return droits;
    }
    
    
    
    
    /**
     * Renvoi une liste avec un login en plus ou en moins
     * 
     * 
     * @param loginList     liste contenant les utilisateurs ayant accès au PC
     * @param login     login à retirer/ajouter
     * @param option        (1 = ajouter le login | 2 = retirer le login)
     * @param Test     Utilisation de la BD test (true si test sinon false !!!)
     * @return loginList       liste de login d'utilisateurs ayant accès au PC
     */
    public String getRefactoredList(String loginList, Integer option, String login, Boolean Test){
        
        String refactoredLoginList = "";
        
        if(option == 1){
            //Si le login existe déjà on ne fait rien
            if(loginList.contains(login)){
                //Rien
            }
            else{
                if(loginList.equals("")){
                    refactoredLoginList = login;
                }
                else{
                    refactoredLoginList = loginList+"/"+login;
                }
            }
        }
        else if(option == 2){
            //Si le login existe, on le retire
            if(loginList.contains(login)){
                if(!loginList.equals("")){
                    ArrayList<String> loginArraylist = new ArrayList<>();
                    Integer slashIndex = null;
                    String extractedLogin = "";
                    Boolean run = true;

                    if(loginList.equals("")){
                        run = false;
                    }
                    
                    //On ajoute tous les logins dans l'arraylist
                    while(run == true){
                        if(loginList.contains("/")){
                            slashIndex = loginList.indexOf("/");
                            
                            //Récupération d'un login (1er en partant de la gauche)
                            extractedLogin = loginList.substring(0, slashIndex);
                            loginList = loginList.substring(slashIndex+1);

                            //Ajout du login à l'arraylist
                            loginArraylist.add(extractedLogin);
                        }
                        else{
                            //Récupération d'un login (1er en partant de la gauche)
                            extractedLogin = loginList;
                            loginList = "";

                            //Ajout du login à l'arraylist
                            loginArraylist.add(extractedLogin);
                        }

                        
                        
                        if(loginList.equals("")){
                            run = false;
                        }
                    }
                    
                    //On reconstruit la liste
                    Integer c = 0;
                    Integer loginN = 0;
                    while (c < loginArraylist.size()){
                        if(loginArraylist.get(c).contains(login)){
                            //Rien
                        }
                        else{
                            //Ajout du séparateur si login > 1
                            if(loginN > 0){
                                refactoredLoginList += "/";
                            }
                            
                            //Ajout du login
                            refactoredLoginList += loginArraylist.get(c);
                            
                            loginN += 1;
                        }
                        
                        c += 1;
                    }
                }
                else{
                    refactoredLoginList = "";
                }
            }
            else{
                //Rien
            }
        }
        return refactoredLoginList;
    }
    
    
    
    
    /**
     * Vérifie l'existance de l'ID dans la base de données pc_messages
     * 
     * @param id     id de la machine
     * @param Test     Utilisation de la BD test (true si test sinon false !!!)
     * @return loginExist       éxsitance du login (booléen)
     */
    public Boolean doIDExistMessages(Integer id, Boolean Test){
        String RequeteSQL="SELECT id FROM pc_messages WHERE id = ?";
        
        Boolean idExist = false;
        
        //Selection de la BD
        changeConnection(Test);
        
        //Connection BD en tant que postgres
        try (Connection connection =
            DAOPC.getConnectionPostgres();
                
            //Requête SQL
            PreparedStatement preparedStatement = connection.prepareStatement(RequeteSQL)) {
            
            //Remplacement de "?" par le login (pour éviter les injections SQL !!!)
            preparedStatement.setInt(1, id);
            
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
    
    
    
    
    
    /**
     * Ajout d'un ordinateur dans la BD
     * 
     * @param id     id de la machine
     * @param action        restart | shutdown | update
     * @param login     login de l'utilisateur qui fait la demande
     * @param Test     Utilisation de la BD test (true si test sinon false !!!)
     */
    public void addStatusChange(Integer id, String action, String login, Boolean Test){
        String RequeteSQL="INSERT INTO pc_messages (id, message) VALUES (?, ?)";
        
        //Récupération des droits d'accès de l'utilisateur
        Boolean access = getUserPCAccess(id, login, Test);
        
        //Verif des droits
        if(access == true){
            
            //Vérification de l'existance d'un message (il ne peut pas y avoir plus d'un changement à la fois => au cas où l'utilisateur spam)
            Boolean exist = doIDExistMessages(id, Test);
            
            if(exist == false){
                //Selection de la BD
                changeConnection(Test);

                //Connection BD en tant que postgres
                try (Connection connection =
                    DAOPC.getConnectionPostgres();

                    //Requête SQL
                    PreparedStatement preparedStatement = connection.prepareStatement(RequeteSQL)) {

                    //Remplacement des "?" par les variables d'entrée (pour éviter les injections SQL !!!)
                    preparedStatement.setInt(1, id);
                    preparedStatement.setString(2, action);

                    // Exécution de la requête
                    int affectedRows = preparedStatement.executeUpdate();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            else{
                //Rien
            }
        }
    }
    
    
    
    /**
     * Vérifie que le status a bien été changé en regardant si le message est toujour présent
     * 
     * @param id     id de la machine
     * @param pause     pause en ms
     * @param Test     Utilisation de la BD test (true si test sinon false !!!)
     * @return executed     status de l'execution de la commande
     */
    public boolean checkStatusChange(Integer id, long pause, boolean Test) {
        try {
            //Pause
            Thread.sleep(pause);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt(); // Bonne pratique : réinterrompre le thread
            Logger.getLogger(DAOPC.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Vérification de la présence de l'id dans pc_messages
        return !doIDExistMessages(id, Test);
    }
    
    
    
     /**
     * Renvoi un message destiné à un pc
     * 
     * @param Test     Utilisation de la BD test (true si test sinon false !!!)
     * @return list       String Arraylist qui contient : 0 => id (ici -1 si pas de message)  et 1 => message
     */
    public ArrayList<String> getMessage(Boolean Test){
        String RequeteSQL="SELECT id, message FROM pc_messages ORDER BY id ASC";
        String message = "";
        Integer id= -1; //Par défaut, renvoyé si table vide
        
        //Selection de la BD
        changeConnection(Test);
        
        
        //Connection BD en tant que postgres
        try (Connection connection =
            DAOPC.getConnectionPostgres();
                
            //Requête SQL
            PreparedStatement preparedStatement = connection.prepareStatement(RequeteSQL)) {
            
            // Exécution de la requête
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                
                //On prend le résultat de la 1ere itération si elle existe
                if (resultSet.next()) {
                    //Récupération des info souhaitées
                    id = resultSet.getInt("id");
                    message = resultSet.getString("message");
                }
            } 
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        //Remplissage de l'arraylist renvoyée
        ArrayList<String> list = new ArrayList<>();
        list.add(String.valueOf(id));
        list.add(message);
        
        return list;
    }
    
    
    
    /**
     * Suppression d'un ordinateur dans la BD
     * 
     * @param id     id de la machine
     * @param Test     Utilisation de la BD test (true si test sinon false !!!)
     */
    public void deleteMessage(Integer id, Boolean Test){
        String RequeteSQL="DELETE FROM pc_messages WHERE id = ?";
        
        //Selection de la BD
        changeConnection(Test);
        
        //Connection BD en tant que postgres
        try (Connection connection =
            DAOPC.getConnectionPostgres();
                
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
    
    
    
    /**
     * Ajout des informations dynamiques d'un ordinateur dans la BD
     * 
     * @param id        id du PC
     * @param CPUUtilization        utilisation du CPU %
     * @param CPUTemp       température du CPU en °C
     * @param CPUConsumption        consommation du CPU en W
     * @param RAMUtilization        utilisation de la RAM en %
     * @param storageName       nom de l'appareil => format : "Nom1/.../NomN"
     * @param storageLoad       taux d'écritures des appareils en % => format : "Pourcentage1/.../PourcentageN"
     * @param storageLeft       capacité de stockage restante en Go => format : "Capacité1/.../CapacitéN"
     * @param storageTemp       température du périphérique en °C => format : "Température1/.../TempératureN"
     * @param storageErrors     nombre d'erreurs du périphérique Int => format : "Nb1/.../NbN"
     * @param networkName       nom du NIC str => format : "Nom1/.../NomN}"
     * @param networkLatency        latence du NIC (avec google par ex) en ms => format : "Nb1/.../NbN"
     * @param networkBandwith       taux utilisation débit sortant NIC en % => format : "Pourcentage1/.../PourcentageN"
     * @param fanSpeed      taux de vitesse de rotation en % => format : "Pourcentage1/.../PourcentageN"
     * @param Test     Utilisation de la BD test (true si test sinon false !!!)
     */
    public void addPCDynamicInfo(Integer id, Integer CPUUtilization, Integer CPUTemp, Integer CPUConsumption, 
            Integer RAMUtilization, String storageName, String storageLoad, String storageLeft, String storageTemp, String storageErrors, 
            String networkName, String networkLatency, String networkBandwith, String fanSpeed, Boolean Test){
        
        String RequeteSQL="INSERT INTO pc_dynamic_info (id, date, time, cpu_utilization, cpu_temp, cpu_consumption, ram_utilization, storage_name, storage_load, storage_left, storage_temp, storage_errors, network_name, network_latency, network_bandwith, fan_speed) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        //Suppression de l'ancienne entrée si elle existe
        deleteDuplicateDynInfo(id, Test);
        
        //Date dernière act (utilisé par IsDynamicInfoUpToDate
        String date = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
        String time = new SimpleDateFormat("HHmmss").format(Calendar.getInstance().getTime());
        
        //Selection de la BD
        changeConnection(Test);
        
        //Connection BD en tant que postgres
        try (Connection connection =
            DAOPC.getConnectionPostgres();
                
            //Requête SQL
            PreparedStatement preparedStatement = connection.prepareStatement(RequeteSQL)) {
            
            //Remplacement des "?" par les variables d'entrée (pour éviter les injections SQL !!!)
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, date);
            preparedStatement.setString(3, time);
            preparedStatement.setInt(4, CPUUtilization);
            preparedStatement.setInt(5, CPUTemp);
            preparedStatement.setInt(6, CPUConsumption);
            preparedStatement.setInt(7, RAMUtilization);
            preparedStatement.setString(8, storageName);
            preparedStatement.setString(9, storageLoad);
            preparedStatement.setString(10, storageLeft);
            preparedStatement.setString(11, storageTemp);
            preparedStatement.setString(12, storageErrors);
            preparedStatement.setString(13, networkName);
            preparedStatement.setString(14, networkLatency);
            preparedStatement.setString(15, networkBandwith);
            preparedStatement.setString(16, fanSpeed);
            
            
            // Exécution de la requête
            int affectedRows = preparedStatement.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    
    /**
     * Renvoi le droit d'accès d'un utilisateur à une machine
     * 
     * @param idPC      id du PC
     * @param dateReq       date de la dernière act de l'utilisateur ("yyyyddmm")
     * @param timeReq       date de la dernière act de l'utilisateur ("hhmmss")
     * @param Test     Utilisation de la BD test (true si test sinon false !!!)
     * @return upToDate       true | false
     */
    public String isDynamicInfoUpToDate(Integer idPC, String timeReq, String dateReq, Boolean Test){
        
        //Vérification des droits d'accès au pc
        String RequeteSQL="SELECT date, time FROM pc_dynamic_info WHERE id = ?";
        
        //S'il n'y a rien dans la DB on renvoi upToDate à true (car vide)
        String timeDB = "000000";
        String dateDB = "00000000";
        
        //Date Act
        String dateAct = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
        String timeAct = new SimpleDateFormat("HHmmss").format(Calendar.getInstance().getTime());
        
        String upToDate = "true";
        
        //Selection de la BD
        changeConnection(Test);
        
        
        //Connection BD en tant que postgres
        try (Connection connection =
            DAOPC.getConnectionPostgres();
                
            //Requête SQL
            PreparedStatement preparedStatement = connection.prepareStatement(RequeteSQL)) {
            
            //Remplacement des "?" par les variables d'entrée (pour éviter les injections SQL !!!)
            preparedStatement.setInt(1, idPC);
            
            // Exécution de la requête
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                
                if (resultSet.next()) {
                    dateDB = resultSet.getString("date");
                    timeDB = resultSet.getString("time");
                    
                    //Si la date Act est supérieure à celle où les infos ont étés enregistrées
                    if(Integer.valueOf(dateDB) >= Integer.valueOf(dateReq) && Integer.valueOf(timeDB) > Integer.valueOf(timeReq)){
                        upToDate = "false";
                    }
                }
            } 
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return upToDate;
    }
    
    
    
    /**
     * Vérifie l'existance de l'ID dans la base de données pc_dynamic_info et suppr si il existe
     * 
     * @param id     id de la machine
     * @param Test     Utilisation de la BD test (true si test sinon false !!!)
     */
    public void deleteDuplicateDynInfo(Integer id, Boolean Test){
        String RequeteSQL="SELECT id FROM pc_dynamic_info WHERE id = ?";
        
        Boolean idExist = false;
        
        //Selection de la BD
        changeConnection(Test);
        
        //Connection BD en tant que postgres
        try (Connection connection =
            DAOPC.getConnectionPostgres();
                
            //Requête SQL
            PreparedStatement preparedStatement = connection.prepareStatement(RequeteSQL)) {
            
            //Remplacement de "?" par le login (pour éviter les injections SQL !!!)
            preparedStatement.setInt(1, id);
            
            // Exécution de la requête
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    //Suppression des données précédentes
                    deleteDynInfo(id, Test);
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    /**
     * Supprime les données d'une machine dans la base de données pc_dynamic_info
     * 
     * @param id     id de la machine
     * @param Test     Utilisation de la BD test (true si test sinon false !!!)
     */
    private void deleteDynInfo(Integer id, Boolean Test){
        String RequeteSQL="DELETE FROM pc_dynamic_info WHERE id = ?";
        
        //Selection de la BD
        changeConnection(Test);
        
        //Connection BD en tant que postgres
        try (Connection connection =
            DAOPC.getConnectionPostgres();
                
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
    
    
    
    
    /**
     * Renvoi les info dynamiques d'un pc contenu dans la BD
     * 
     * @param idPC      id du pc
     * @param login     login de l'utilisateur qui aura accès à ces données
     * @param Test     Utilisation de la BD test (true si test sinon false !!!)
     * @return JSONString       contenu de la table au format JSON (login/prenom/nom/droits)
     */
    public String getPCDynInfo(Integer idPC, String login, Boolean Test){
        
        //Par défaut
        String JSONString = "{\"erreur\":\"Pas d'informations dans la table\"}" ;
        
        //Vérification de la présence d'une entrée
        Boolean idExist = doIDExistDynInfo(idPC, Test);
        if(idExist == false){
            return JSONString;
        }
        
        //Vérification des droits d'accès au PC
        Boolean getInfo = getUserPCAccess(idPC, login, Test);
        
        //Si l'utilisateur a les droits d'accès au pc
        if(getInfo == true){
            //Récupération des infos statiques du PC
            String RequeteSQL="SELECT * FROM pc_dynamic_info WHERE id = ?";
            Integer id = null;
            Integer CPUUtilization = null;
            Integer CPUTemp = null;
            Integer CPUConsumption = null;
            Integer RAMUtilization = null;
            String storageName = "";
            String storageLoad = "";
            String storageLeft = "";
            String storageTemp = "";
            String storageErrors = "";
            String networkName = "";
            String networkLatency = "";
            String networkBandwith = "";
            String fanSpeed = "";

            //Selection de la BD
            changeConnection(Test);


            //Connection BD en tant que postgres
            try (Connection connection =
                DAOPC.getConnectionPostgres();

                //Requête SQL
                PreparedStatement preparedStatement = connection.prepareStatement(RequeteSQL)) {
                
                //Remplacement des "?" par les variables d'entrée (pour éviter les injections SQL !!!)
                preparedStatement.setInt(1, idPC);

                // Exécution de la requête
                try (ResultSet resultSet = preparedStatement.executeQuery()) {

                    if (resultSet.next()) {
                        //Récupération des données dans la BD
                        id = resultSet.getInt("id");
                        String date = resultSet.getString("date");
                        String time = resultSet.getString("time");
                        CPUUtilization = resultSet.getInt("cpu_utilization");
                        CPUTemp = resultSet.getInt("cpu_temp");
                        CPUConsumption = resultSet.getInt("cpu_consumption");
                        RAMUtilization = resultSet.getInt("ram_utilization");
                        storageName = resultSet.getString("storage_name");
                        storageLoad = resultSet.getString("storage_load");
                        storageLeft = resultSet.getString("storage_left");
                        storageTemp = resultSet.getString("storage_temp");
                        storageErrors = resultSet.getString("storage_errors");
                        networkName = resultSet.getString("network_name");
                        networkLatency = resultSet.getString("network_latency");
                        networkBandwith = resultSet.getString("network_bandwith");
                        fanSpeed = resultSet.getString("fan_speed");

                        //Traitement des listes
                        ArrayList storageNameList = getArrayList(storageName);
                        ArrayList storageLoadList = getArrayList(storageLoad);
                        ArrayList storageLeftList = getArrayList(storageLeft);
                        ArrayList storageTempList = getArrayList(storageTemp);
                        ArrayList storageErrorsList = getArrayList(storageErrors);
                        ArrayList networkNameList = getArrayList(networkName);
                        ArrayList networkLatencyList = getArrayList(networkLatency);
                        ArrayList networkBandwithList = getArrayList(networkBandwith);
                        ArrayList fanSpeedList = getArrayList(fanSpeed);
                        
                        String JSONCPU = "{\"CPUUtilization\":\""+CPUUtilization+"\",\"CPUTemp\":\""+CPUTemp+"\",\"CPUConsumption\":\""+CPUConsumption+"\"}";
                        String JSONNetwork = getNetworkJSON(networkNameList, networkLatencyList, networkBandwithList);
                        String JSONRAM = "{\"RAMUtilization\":\""+RAMUtilization+"\"}";
                        String JSONStorage = getStorageJSON(storageNameList, storageLoadList, storageLeftList, storageTempList, storageErrorsList);
                        String JSONfanSpeed = getFanJSON(fanSpeedList);

                        // Ajouter l'objet JSON
                        JSONString = "{"
                                + "\"erreur\":\"none\","
                                + "\"date\":\""+date+"\","
                                + "\"time\":\""+time+"\","
                                + "\"CPU\": ["
                                + ""+JSONCPU+""
                                + "],"
                                + "\"RAM\": ["
                                + ""+JSONRAM+""
                                + "],"
                                + "\"Network\": ["
                                + ""+JSONNetwork+""
                                + "],"
                                + "\"Storage\": ["
                                + ""+JSONStorage+""
                                + "],"
                                + "\"Fans\": ["
                                + ""+JSONfanSpeed+""
                                + "]"
                                + "}";
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else{
            JSONString = "{\"erreur\":\"accès refusé\"}";
        }
        
        return JSONString;
    }
    
    
    
    
    /**
     * Vérifie l'existance de l'ID dans la base de données
     * 
     * @param id     id de la machine
     * @param Test     Utilisation de la BD test (true si test sinon false !!!)
     * @return loginExist       éxsitance du login (booléen)
     */
    public Boolean doIDExistDynInfo(Integer id, Boolean Test){
        String RequeteSQL="SELECT id FROM pc_dynamic_info WHERE id = ?";
        
        Boolean idExist = false;
        
        //Selection de la BD
        changeConnection(Test);
        
        //Connection BD en tant que postgres
        try (Connection connection =
            DAOPC.getConnectionPostgres();
                
            //Requête SQL
            PreparedStatement preparedStatement = connection.prepareStatement(RequeteSQL)) {
            
            //Remplacement de "?" par le login (pour éviter les injections SQL !!!)
            preparedStatement.setInt(1, id);
            
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
    
    
    
    
    //Un poil overkill mais on sera si le client foire mdr
    public ArrayList<String> getArrayList(String str){
        
        ArrayList<String> list = new ArrayList<>();
        
        //Si str vide
        if(str.equals("")){
            //Rien
        }
                
        //Si str ne contient pas de séparateur
        if(!str.contains("/")){
            list.add(str);
        }
        else{
            //S'il y'a un ou plusieurs séparateurs ou qu'il y'a du contenu
            while(str.contains("/") || (!str.equals(""))){
                //S'il y'a un double séparateur
                if(str.substring(0, 1).equals("/")){
                    //Ajout du champ à la liste
                    list.add("non défini");
                    
                    //Retrait du champ à la chaîne originale
                    str = str.substring(1);
                }
                
                //Dernier ne contient pas de séparateur
                if(!str.contains("/")){
                    //Ajout du champ à la liste
                    list.add(str);
                    
                    break;
                }
                else{
                    //Ajout du champ à la liste
                    list.add(str.substring(0, (str.indexOf("/"))));
                    
                    //Cas particulier => séparateur en fin
                    if(str.substring(str.indexOf("/")).equals("/")){
                        //Ajout du champ à la liste
                        list.add("non défini");

                        break;
                    }
                    else{
                        //Retrait du champ à la chaîne originale
                        str = str.substring(str.indexOf("/")+1);
                    }
                }
            }
        }
        
        return list;
    }
    
    
    
    private String getStorageJSON(ArrayList<String> storageNameList, ArrayList<String> storageLoadList, ArrayList<String> storageLeftList,ArrayList<String> storageTempList, ArrayList<String> storageErrorsList){
        Integer c = 0;
        String JSON = "";
        
        while(c < storageNameList.size()){
            if(!(c == 0)){
                JSON += ",";
            }
            
            JSON += "{";
            
            JSON += "\"storageName\":\""+storageNameList.get(c)+"\","+"\"storageLoad\":\""+storageLoadList.get(c)+"\","+"\"storageLeft\":\""+storageLeftList.get(c)+"\","+"\"storageTemp\":\""+storageTempList.get(c)+"\","+"\"storageErrors\":\""+storageErrorsList.get(c)+"\"";
            
            JSON += "}";
            
            c += 1;
        }
        
        return JSON;
    }
    
    
    
    private String getFanJSON(ArrayList<String> fanSpeedList){
        Integer c = 0;
        String JSON = "";
        
        while(c < fanSpeedList.size()){
            if(!(c == 0)){
                JSON += ",";
            }
            
            JSON += "{";
            
            JSON += "\"fanSpeed\":\""+fanSpeedList.get(c)+"\"";
            
            JSON += "}";
            
            c += 1;
        }
        
        return JSON;
    }
    
    
    
    private String getNetworkJSON(ArrayList<String> networkNameList, ArrayList<String> networkLatencyList, ArrayList<String> networkBandwithList){                
        Integer c = 0;
        String JSON = "";
        
        while(c < networkNameList.size()){
            if(!(c == 0)){
                JSON += ",";
            }
            
            JSON += "{";
            
            JSON += "\"networkName\":\""+networkNameList.get(c)+"\","+"\"networkLatency\":\""+networkLatencyList.get(c)+"\","+"\"networkBandwidth\":\""+networkBandwithList.get(c)+"\"";
            
            JSON += "}";
            
            c += 1;
        }
        
        return JSON;
    }
    
    
    
    /**
     * Récupération des seuils d'un PC
     * 
     * @param id        id du PC
     * @param CPUUtilization        utilisation du CPU %
     * @param CPUTemp       température du CPU en °C
     * @param CPUConsumption        consommation du CPU en W
     * @param RAMUtilization        utilisation de la RAM en %
     * @param storageLoad       taux d'écritures des appareils en %
     * @param storageLeft       capacité de stockage restante en Go
     * @param storageTemp       température du périphérique en °C
     * @param storageErrors     nombre d'erreurs du périphérique Int
     * @param networkLatency        latence du NIC (avec google par ex) en ms
     * @param networkBandwith       taux utilisation débit sortant NIC en %
     * @param fanSpeed      taux de vitesse de rotation en %
     * @param Test     Utilisation de la BD test (true si test sinon false !!!)
     */
    public void addThresholds(Integer id, Integer CPUUtilization, Integer CPUTemp, Integer CPUConsumption, 
            Integer RAMUtilization, Integer storageLoad, Integer storageLeft, Integer storageTemp, Integer storageErrors, 
            Integer networkLatency, Integer networkBandwith, Integer fanSpeed, Boolean Test){
        
        String RequeteSQL="INSERT INTO pc_thresholds (id, cpu_utilization, cpu_temp, cpu_consumption, ram_utilization, storage_load, storage_left, storage_temp, storage_errors, network_latency, network_bandwith, fan_speed) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        //Suppression de l'ancienne entrée si elle existe
        deleteDuplicateThresholds(id, Test);
        
        //Selection de la BD
        changeConnection(Test);
        
        //Connection BD en tant que postgres
        try (Connection connection =
            DAOPC.getConnectionPostgres();
                
            //Requête SQL
            PreparedStatement preparedStatement = connection.prepareStatement(RequeteSQL)) {
            
            //Remplacement des "?" par les variables d'entrée (pour éviter les injections SQL !!!)
            preparedStatement.setInt(1, id);
            preparedStatement.setInt(2, CPUUtilization);
            preparedStatement.setInt(3, CPUTemp);
            preparedStatement.setInt(4, CPUConsumption);
            preparedStatement.setInt(5, RAMUtilization);
            preparedStatement.setInt(6, storageLoad);
            preparedStatement.setInt(7, storageLeft);
            preparedStatement.setInt(8, storageTemp);
            preparedStatement.setInt(9, storageErrors);
            preparedStatement.setInt(10, networkLatency);
            preparedStatement.setInt(11, networkBandwith);
            preparedStatement.setInt(12, fanSpeed);
            
            
            // Exécution de la requête
            int affectedRows = preparedStatement.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    
    /**
     * Vérifie l'existance de l'ID dans la base de données pc_thresholds et suppr si il existe
     * 
     * @param id     id de la machine
     * @param Test     Utilisation de la BD test (true si test sinon false !!!)
     */
    public void deleteDuplicateThresholds(Integer id, Boolean Test){
        String RequeteSQL="SELECT id FROM pc_thresholds WHERE id = ?";
        
        Boolean idExist = false;
        
        //Selection de la BD
        changeConnection(Test);
        
        //Connection BD en tant que postgres
        try (Connection connection =
            DAOPC.getConnectionPostgres();
                
            //Requête SQL
            PreparedStatement preparedStatement = connection.prepareStatement(RequeteSQL)) {
            
            //Remplacement de "?" par le login (pour éviter les injections SQL !!!)
            preparedStatement.setInt(1, id);
            
            //Exécution de la requête
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    //Suppression des données précédentes
                    deleteThreshold(id, Test);
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    
    /**
     * Supprime les données d'une machine dans la base de données pc_thresholds
     * 
     * @param id     id de la machine
     * @param Test     Utilisation de la BD test (true si test sinon false !!!)
     */
    private void deleteThreshold(Integer id, Boolean Test){
        String RequeteSQL="DELETE FROM pc_thresholds WHERE id = ?";
        
        //Selection de la BD
        changeConnection(Test);
            
        //Connection BD en tant que postgres
        try (Connection connection =
            DAOPC.getConnectionPostgres();
                
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
    
    
    
    
    /**
     * Renvoi les seuils d'un pc contenu dans la BD
     * 
     * @param idPC      id du pc
     * @param login     login de l'utilisateur qui aura accès à ces données
     * @param Test     Utilisation de la BD test (true si test sinon false !!!)
     * @return JSONString       contenu de la table au format JSON (login/prenom/nom/droits)
     */
    public String getThresholds(Integer idPC, String login, Boolean Test){
        
        //Par défaut
        String JSONString = "{\"erreur\":\"Pas d'informations dans la table\"}" ;
        
        //Vérification de la présence d'une entrée
        Boolean idExist = doIDExistThresholds(idPC, Test);
        if(idExist == false){
            return JSONString;
        }
        
        //Vérification des droits d'accès au PC
        Boolean getInfo = getUserPCAccess(idPC, login, Test);
        
        //Si l'utilisateur a les droits d'accès au pc
        if(getInfo == true){
            //Récupération des infos statiques du PC
            String RequeteSQL="SELECT * FROM pc_thresholds WHERE id = ?";
            Integer id = null;
            Integer CPUUtilization = null;
            Integer CPUTemp = null;
            Integer CPUConsumption = null;
            Integer RAMUtilization = null;
            Integer storageLoad = null;
            Integer storageLeft = null;
            Integer storageTemp = null;
            Integer storageErrors = null;
            Integer networkLatency = null;
            Integer networkBandwith = null;
            Integer fanSpeed = null;

            //Selection de la BD
            changeConnection(Test);


            //Connection BD en tant que postgres
            try (Connection connection =
                DAOPC.getConnectionPostgres();

                //Requête SQL
                PreparedStatement preparedStatement = connection.prepareStatement(RequeteSQL)) {
                
                //Remplacement des "?" par les variables d'entrée (pour éviter les injections SQL !!!)
                preparedStatement.setInt(1, idPC);

                // Exécution de la requête
                try (ResultSet resultSet = preparedStatement.executeQuery()) {

                    if (resultSet.next()) {
                        //Récupération des données dans la BD
                        id = resultSet.getInt("id");
                        CPUUtilization = resultSet.getInt("cpu_utilization");
                        CPUTemp = resultSet.getInt("cpu_temp");
                        CPUConsumption = resultSet.getInt("cpu_consumption");
                        RAMUtilization = resultSet.getInt("ram_utilization");
                        storageLoad = resultSet.getInt("storage_load");
                        storageLeft = resultSet.getInt("storage_left");
                        storageTemp = resultSet.getInt("storage_temp");
                        storageErrors = resultSet.getInt("storage_errors");
                        networkLatency = resultSet.getInt("network_latency");
                        networkBandwith = resultSet.getInt("network_bandwith");
                        fanSpeed = resultSet.getInt("fan_speed");

                        

                        // Ajouter l'objet JSON
                        JSONString = "{"
                                + "\"erreur\":\"none\","
                                + "\"CPUUtilization\":\""+CPUUtilization+"\","
                                + "\"CPUTemp\":\""+CPUTemp+"\","
                                + "\"CPUConsumption\":\""+CPUConsumption+"\","
                                + "\"RAMUtilization\":\""+RAMUtilization+"\","
                                + "\"storageLoad\":\""+storageLoad+"\","
                                + "\"storageLeft\":\""+storageLeft+"\","
                                + "\"storageTemp\":\""+storageTemp+"\","
                                + "\"storageErrors\":\""+storageErrors+"\","
                                + "\"networkLatency\":\""+networkLatency+"\","
                                + "\"networkBandwith\":\""+networkBandwith+"\","
                                + "\"fanSpeed\":\""+fanSpeed+"\""
                                + "}";
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else{
            JSONString = "{\"erreur\":\"accès refusé\"}";
        }
        
        return JSONString;
    }
    
    
    
    /**
     * Vérifie l'existance de l'ID dans la base de données
     * 
     * @param id     id de la machine
     * @param Test     Utilisation de la BD test (true si test sinon false !!!)
     * @return idExist      éxsitance de l'id (booléen)
     */
    public Boolean doIDExistThresholds(Integer id, Boolean Test){
        String RequeteSQL="SELECT id FROM pc_thresholds WHERE id = ?";
        
        Boolean idExist = false;
        
        //Selection de la BD
        changeConnection(Test);
        
        //Connection BD en tant que postgres
        try (Connection connection =
            DAOPC.getConnectionPostgres();
                
            //Requête SQL
            PreparedStatement preparedStatement = connection.prepareStatement(RequeteSQL)) {
            
            //Remplacement de "?" par le login (pour éviter les injections SQL !!!)
            preparedStatement.setInt(1, id);
            
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
    
    
    
    
    /**
     * Vérifie que les seuils sont respectés, dans le cas échéant une notif est envoyée
     * 
     * @param idPC      id du pc
     * @param Test     Utilisation de la BD test (true si test sinon false !!!)
     */
    public ArrayList<String> checkThresholds(Integer idPC, Boolean Test){
        
        //Données
        Integer Tid = null;
        Integer TCPUUtilization = null;
        Integer TCPUTemp = null;
        Integer TCPUConsumption = null;
        Integer TRAMUtilization = null;
        Integer TstorageLoad = null;
        Integer TstorageLeft = null;
        Integer TstorageTemp = null;
        Integer TstorageErrors = null;
        Integer TnetworkLatency = null;
        Integer TnetworkBandwith = null;
        Integer TfanSpeed = null;
        Integer CPUUtilization = null;
        Integer CPUTemp = null;
        Integer CPUConsumption = null;
        Integer RAMUtilization = null;
        String storageName = "";
        String storageLoad = "";
        String storageLeft = "";
        String storageTemp = "";
        String storageErrors = "";
        String networkName = "";
        String networkLatency = "";
        String networkBandwith = "";
        String fanSpeed = "";
        ArrayList storageNameList = new ArrayList();
        ArrayList storageLoadList = new ArrayList();
        ArrayList storageLeftList = new ArrayList();
        ArrayList storageTempList = new ArrayList();
        ArrayList storageErrorsList = new ArrayList();
        ArrayList networkNameList = new ArrayList();
        ArrayList networkLatencyList = new ArrayList();
        ArrayList networkBandwithList = new ArrayList();
        ArrayList fanSpeedList = new ArrayList();
        DAONotifications DAON = new DAONotifications();
        
        ArrayList<String> messages = new ArrayList<>();
        
        //Vérification de la présence d'une entrée
        Boolean idExist = doIDExistThresholds(idPC, Test);
        if(idExist == false){
            return messages;
        }

        //Récupération des infos statiques du PC
        String RequeteSQL="SELECT * FROM pc_thresholds WHERE id = ?";

        //Selection de la BD
        changeConnection(Test);


        //Connection BD en tant que postgres
        try (Connection connection =
            DAOPC.getConnectionPostgres();

            //Requête SQL
            PreparedStatement preparedStatement = connection.prepareStatement(RequeteSQL)) {
                
            //Remplacement des "?" par les variables d'entrée (pour éviter les injections SQL !!!)
            preparedStatement.setInt(1, idPC);

            // Exécution de la requête
            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                if (resultSet.next()) {
                    //Récupération des données dans la BD
                    Tid = resultSet.getInt("id");
                    TCPUUtilization = resultSet.getInt("cpu_utilization");
                    TCPUTemp = resultSet.getInt("cpu_temp");
                    TCPUConsumption = resultSet.getInt("cpu_consumption");
                    TRAMUtilization = resultSet.getInt("ram_utilization");
                    TstorageLoad = resultSet.getInt("storage_load");
                    TstorageLeft = resultSet.getInt("storage_left");
                    TstorageTemp = resultSet.getInt("storage_temp");
                    TstorageErrors = resultSet.getInt("storage_errors");
                    TnetworkLatency = resultSet.getInt("network_latency");
                    TnetworkBandwith = resultSet.getInt("network_bandwith");
                    TfanSpeed = resultSet.getInt("fan_speed");

                }
            }
        } catch (SQLException e) {
                e.printStackTrace();
        }
        
        
        
        //Récupération des données à vérifier (plus simple de tout faire ici)//
        
        //Vérification de la présence d'une entrée
        idExist = doIDExistDynInfo(idPC, Test);
        if(idExist == false){
            return messages;
        }
        
        //Vérification des droits d'accès au PC
        Boolean getInfo = true ;//flemme de changer l'indentation
        
        //Si l'utilisateur a les droits d'accès au pc
        if(getInfo == true){
            //Récupération des infos statiques du PC
            RequeteSQL="SELECT * FROM pc_dynamic_info WHERE id = ?";

            //Connection BD en tant que postgres
            try (Connection connection =
                DAOPC.getConnectionPostgres();

                //Requête SQL
                PreparedStatement preparedStatement = connection.prepareStatement(RequeteSQL)) {
                
                //Remplacement des "?" par les variables d'entrée (pour éviter les injections SQL !!!)
                preparedStatement.setInt(1, idPC);

                // Exécution de la requête
                try (ResultSet resultSet = preparedStatement.executeQuery()) {

                    if (resultSet.next()) {
                        storageName = resultSet.getString("storage_name");
                        storageLoad = resultSet.getString("storage_load");
                        storageLeft = resultSet.getString("storage_left");
                        storageTemp = resultSet.getString("storage_temp");
                        storageErrors = resultSet.getString("storage_errors");
                        networkName = resultSet.getString("network_name");
                        networkLatency = resultSet.getString("network_latency");
                        networkBandwith = resultSet.getString("network_bandwith");
                        fanSpeed = resultSet.getString("fan_speed");

                        //Données à check
                        storageNameList = getArrayList(storageName);
                        storageLoadList = getArrayList(storageLoad);
                        storageLeftList = getArrayList(storageLeft);
                        storageTempList = getArrayList(storageTemp);
                        storageErrorsList = getArrayList(storageErrors);
                        networkNameList = getArrayList(networkName);
                        networkLatencyList = getArrayList(networkLatency);
                        networkBandwithList = getArrayList(networkBandwith);
                        fanSpeedList = getArrayList(fanSpeed);
                        CPUUtilization = resultSet.getInt("cpu_utilization");
                        CPUTemp = resultSet.getInt("cpu_temp");
                        CPUConsumption = resultSet.getInt("cpu_consumption");
                        RAMUtilization = resultSet.getInt("ram_utilization");
                    }
                }
                
            }catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        //Vérification des informations//
        //Liste qui contient les messages de dépassement des seuils précédents
        //Tout ce qui n'est pas dedans sera retiré de la BD et ce qui est déjà dans la BD ne bouge pas (comme ça on a les dépassements en cours avec un timestamp)
        
        //Simples
        messages = checkSimpleThreshold(messages, TCPUUtilization, CPUUtilization, "CPUUtilization", idPC);
        messages = checkSimpleThreshold(messages, TCPUTemp, CPUTemp, "CPUTemp", idPC);
        messages = checkSimpleThreshold(messages, TCPUConsumption, CPUConsumption, "CPUConsumption", idPC);
        messages = checkSimpleThreshold(messages, TRAMUtilization, RAMUtilization, "RAMUtilization", idPC);
        
        //Listes
        messages = checkListThreshold(messages, TstorageLoad, storageLoadList, storageNameList, "storageLoad", idPC);
        messages = checkListThreshold(messages, TstorageLeft, storageLeftList, storageNameList, "storageLeft", idPC);
        messages = checkListThreshold(messages, TstorageTemp, storageTempList, storageNameList, "storageTemp", idPC);
        messages = checkListThreshold(messages, TstorageErrors, storageErrorsList, storageNameList, "storageErrors", idPC);
        messages = checkListThreshold(messages, TnetworkLatency, networkLatencyList, networkNameList, "networkLatency", idPC);
        messages = checkListThreshold(messages, TnetworkBandwith, networkBandwithList, networkNameList, "networkBandwith", idPC);
        messages = checkListThreshold(messages, TfanSpeed, fanSpeedList, fanSpeedList, "fanSpeed", idPC);
        
        DAON.cleanNotifications(idPC, messages, Test);
        
        Integer c=0;
        String users = getUsersWithPCAccess(idPC, Test);
        while(c < messages.size()){
            
            DAON.addNotification("Alert", messages.get(c), users, idPC, Test);
            
            c += 1;
        }
        return messages;
    }
    
    
    private ArrayList<String> checkSimpleThreshold(ArrayList<String> messages, Integer threshold, Integer value, String metric, Integer id){
        if(value >= threshold){
            messages.add("Métrique ("+metric+") dépassée pour le PC n°"+id+" => valeur : "+value+" | seuil : "+threshold);
        }
        
        return messages;
    }
    
    private ArrayList<String> checkListThreshold(ArrayList<String> messages, Integer threshold, ArrayList<String> values, ArrayList<String> nameList, String metric, Integer id){
        Integer c = 0;
        
        while(c < values.size()){
            if(Integer.valueOf(values.get(c)) >= threshold){
                if(metric.equals("fanSpeed")){
                    messages.add("Métrique ("+metric+") de l'item nommé (ventilateur n°"+c+") dépassée pour le PC n°"+id+" => valeur : "+values.get(c)+" | seuil : "+threshold);
                }
                else{
                    messages.add("Métrique ("+metric+") de l'item nommé ("+nameList.get(c)+") dépassée pour le PC n°"+id+" => valeur : "+values.get(c)+" | seuil : "+threshold);
                }
            }
            
            c += 1;
        }
        
        return messages;
    }
}
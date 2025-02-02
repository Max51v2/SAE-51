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
     * Ajout d'un ordinateur dans la BD
     * 
     * @param id        id du PC
     * @param Test     Utilisation de la BD test (true si test sinon false !!!)
     */
    public void addPCDynamicInfo(Integer id, Boolean Test){
        String RequeteSQL="INSERT INTO pc_dynamic_info (id, date, time) VALUES (?, ?, ?)";
        
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
     * @return upToDate       true | false
     */
    public String isDynamicInfoUpToDate(Integer idPC, Boolean Test){
        
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
                    if(Integer.valueOf(dateAct) >= Integer.valueOf(dateDB) && Integer.valueOf(timeAct) > Integer.valueOf(timeDB)){
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
}
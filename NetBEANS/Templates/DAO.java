//Auteur : Maxime VALLET
//Version : 1.1


//Ce qui est entre crochets est à modifier ou retirer selon la situation


//Requête SQL qui renvoi une ligne de données :

    /**
     * Rôle de la méthode
     * 
     * @param ?       &emsp;&emsp;        ?
     *                  ...
     * @param Test       &emsp;&emsp;        Utilisation de la BD test (true si test sinon false !!!)
     * @return        hash stocké dans la table
     */
    public [?] [NomMéthode]([?, ...,] Boolean Test){
        String RequeteSQL="SELECT [?, ..., ?] FROM [?] [WHERE login = ? AND name = ?]";

        //Variables
        [Variables à initialiser]
        
        //Selection de la BD
        changeConnection(Test);
        
        //Connection BD en tant que postgres
        try (Connection connection =
            DAOusers.getConnectionPostgres();
                
            //Requête SQL
            PreparedStatement preparedStatement = connection.prepareStatement(RequeteSQL)) {
            
            //Remplacement des "?" par les variables d'entrée (pour éviter les injections SQL !!!) [si y'a des val à remplacer]
            preparedStatement.setString(1, [?]); // 1er ? => val
            preparedStatement.setString(2, [?]); // 2e ? => val
            
            
            // Exécution de la requête
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    ? = resultSet.getString("[?]");
                                    ...
                    ? = resultSet.getString("[?]");
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return [?];
    }




    //Requête SQL qui renvoi plusieurs lignes de données :

    /**
     * Rôle de la méthode
     * 
     * @param ?       &emsp;&emsp;        ?
     *                  ...
     * @param Test       &emsp;&emsp;        Utilisation de la BD test (true si test sinon false !!!)
     * @return        hash stocké dans la table
     */
    public [?] [NomMéthode]([?, ...,] Boolean Test){
        String RequeteSQL="SELECT [?, ..., ?] FROM [?] [WHERE login = ? AND name = ?]";

        //Variables
        String jsonString = "";
        [Variables à initialiser]
        
        //Selection de la BD
        changeConnection(Test);
        
        //Connection BD en tant que postgres
        try (Connection connection =
            DAOusers.getConnectionPostgres();
                
            //Requête SQL
            PreparedStatement preparedStatement = connection.prepareStatement(RequeteSQL)) {
            
            //Remplacement des "?" par les variables d'entrée (pour éviter les injections SQL !!!) [si y'a des val à remplacer]
            preparedStatement.setString(1, [?]); // 1er ? => val
            preparedStatement.setString(2, [?]); // 2e ? => val
            
            
            // Exécution de la requête
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    ? = resultSet.getString("[?]");
                                    ...
                    ? = resultSet.getString("[?]");


                    //mise au format JSON des données dans jsonString
                    //code ici
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return jsonString;
    }




    //Requête SQL qui ne renvoi pas de données :
    
    /**
     * Rôle de la méthode
     * 
     * @param ?       &emsp;&emsp;        ?
     *                  ...
     * @param Test       &emsp;&emsp;        Utilisation de la BD test (true si test sinon false !!!)
     * @return        hash stocké dans la table
     */
    public [NomMéthode]([?, ...,] Boolean Test){
        String RequeteSQL="SELECT [?, ..., ?] FROM [?] [WHERE login = ? AND name = ?]";

        //Variables
        [Variables à initialiser]
        
        //Selection de la BD
        changeConnection(Test);
        
        //Connection BD en tant que postgres
        try (Connection connection =
            DAOusers.getConnectionPostgres();
                
            //Requête SQL
            PreparedStatement preparedStatement = connection.prepareStatement(RequeteSQL)) {
            
            //Remplacement des "?" par les variables d'entrée (pour éviter les injections SQL !!!) [si y'a des val à remplacer]
            preparedStatement.setString(1, [?]); // 1er ? => val
            preparedStatement.setString(2, [?]); // 2e ? => val
            
            
            // Exécution de la requête
            int affectedRows = preparedStatement.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
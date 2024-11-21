package com.mycompany.sae51_client;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Maxime VALLET
 * version 1.0
 */
public class ProjectConfig {
    String pathSizeFromProject = "NetBEANS/SAE51_Client";
    String confFile = "";
    Map<String, String> map = new HashMap<String, String>();
    OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
    String configPathString;

    public  ProjectConfig(){
        //Chemin du projet Java
        String projectPath = System.getProperty("user.dir");

        //Racine du projet
        String rootPath = projectPath.substring(0,projectPath.indexOf("SAE-51")+6);

        //Chemin du fichier contenant l'id
        if(osBean.getName().contains("Windows")){
            configPathString = rootPath+"\\Serveur\\ConfigProjet\\sae_51.conf";
        }
        else{
            configPathString = rootPath+"/Serveur/ConfigProjet/sae_51.conf";
        }
        Path configPath = Paths.get(configPathString);
        
        // Vérifie si le fichier existe
        File file = new File(configPathString);
        
        //Vérification de l'existance du fichier contenant les données de configuration
        if (file.exists()) {
            //Lecture du fichier
            try {
                byte[] contenu = Files.readAllBytes(configPath);
            
                confFile = new String(contenu, StandardCharsets.UTF_8);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } 
        else {
            System.out.println("ProjectConfig => Erreur : il n'y a pas de fichier de config");
        }
        
        
        Boolean running = true;
        String line;
        String key;
        String val;
        
        //On récuppère les lignes tant qu'il y en reste
        while (running == true){
            //Si il y'a un retour à la ligne, on la récuppère
            if(this.confFile.contains("\n")){
                //Enregistrement de la ligne
                line = this.confFile.substring(0, this.confFile.indexOf("\n"));
                
                //Retrait de la ligne dans le fichier de conf
                this.confFile = this.confFile.substring(this.confFile.indexOf("\n")+1, this.confFile.length());
                
                //Si la ligne n'est pas un commentaire et contient un "="
                if(!line.contains("#") & line.contains("=")){
                    //Clé et val à mettre dans la hashmap
                    key = line.substring(0, line.indexOf("="));
                    val = line.substring(line.indexOf("=")+1, line.length());
                    
                    //Ajout de l'entrée dans la hashmap
                    map.put(key, val);
                }
                else{
                    //Rien (commentaire, saut de ligne...)
                }
            }
            else{
                running = false;
            }
        }
    }
    
    
    public String getStringValue(String parameter){
        String result = map.get(parameter);
        
        return result;
    }
    
    
    public Integer getIntValue(String parameter){
        Integer result = Integer.valueOf(map.get(parameter));
        
        return result;
    }
    
}

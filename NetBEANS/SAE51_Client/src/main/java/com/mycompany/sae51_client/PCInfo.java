package com.mycompany.sae51_client;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * Classe qui vérifie si il y'a un fichier u.id contenant l'id et le renvoi
 * Si le fichier n'existe pas alors il génère un id puis le fichier u.id
 * Info : le fichier contenant l'id est dans le gitingore (donc unique à la machine)
 * 
 * @author Maxime VALLET
 * @version 1.0
 */
public class PCInfo {
    String pathSizeFromProject = "NetBEANS/SAE51_Client";
    String id = "";
    OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
    String IDPathString;
    
    public String getPCID(){
        //Chemin du projet Java
        String projectPath = System.getProperty("user.dir");

        //Racine du projet
        String rootPath = projectPath.substring(0,projectPath.indexOf("SAE-51")+6);

        //Chemin du fichier contenant l'id
        //Chemin du fichier contenant l'id
        if(osBean.getName().contains("Windows")){
            IDPathString = rootPath+"\\Serveur\\Configuration\\sae_51.conf";
        }
        else{
            IDPathString = rootPath+"/Serveur/Configuration/sae_51.conf";
        }
        Path IDPath = Paths.get(IDPathString);
        
        // Vérifie si le fichier existe
        File file = new File(IDPathString);
        
        //Vérification de l'existance du fichier contenant l'id
        if (file.exists()) {
            //Lecture du fichier
            try {
                byte[] contenu = Files.readAllBytes(IDPath);
            
                id = new String(contenu, StandardCharsets.UTF_8);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } 
        else {
            //Génération d'un ID
            id = RandomStringUtils.randomAlphanumeric(16);
            
            //Stockage de l'id dans un fichier
            try {
            //Création du fichier
            Files.createFile(IDPath);
            
            //Ajout de l'id dans le fichier
            Files.writeString(IDPath, id, StandardOpenOption.APPEND);
            
            } 
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        return id;
    }
    
    public String getPCIP() throws UnknownHostException{
        String IP = Inet4Address.getLocalHost().getHostAddress();
        
        return IP;
    }
}

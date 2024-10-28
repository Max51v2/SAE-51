package com.mycompany.sae51_client;

import java.io.File;
import java.io.IOException;
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
 */
public class PCInfo {
    String pathSizeFromProject = "NetBEANS/SAE51_Client";
    String id = "";
    
    public String getPCID(){
        //Chemin du fichier qui contient l'ID du pc
        String projectPath = System.getProperty("user.dir");
        
        //Taille de la chaîne contenant le chemain
        Integer pathSize = projectPath.length();
        
        //Taille de la chaîne contenant le chemin en partant de la racine du peojet
        Integer pathSizeFromProject = this.pathSizeFromProject.length();
        
        //Chemin du fichier contenant l'id
        String IDPathString = projectPath.substring(0, pathSize - pathSizeFromProject) + "Client/u.id";
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

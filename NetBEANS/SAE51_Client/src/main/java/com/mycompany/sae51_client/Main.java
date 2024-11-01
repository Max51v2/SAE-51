package com.mycompany.sae51_client;

import java.io.IOException;

/**
 *
 * @author Maxime VALLET, Erwann MADEC
 * @version 0.3
 */
public class Main {

    public static void main(String[] args) throws IOException {
        System.out.println("##########################################");
        
        PCInfo pcInfo = new PCInfo();
        
        //Obtention de l'id de la machine
        String id = pcInfo.getPCID();
        
        //Obtention de l'IP de la machine
        String IP = pcInfo.getPCIP();
        
        //Obtention de l'IP du serveur
        ProjectConfig conf = new ProjectConfig();
        String ServerIP = conf.getStringValue("ServerIP");
        
        System.out.println("IP serveur : "+ServerIP);
        
        //Ajout du pc à la DB (serveur TCP PingAnswer : port 4444)
        //à faire : changer localhost par scanner
        TCP_Client tcp = new TCP_Client(ServerIP, 4444);
        String jsonString = "{\"id\":\""+id+"\", \"IP\":\""+IP+"\", \"Test\":\"false\"}";
        String result = tcp.run(jsonString);
        
        if(result.equals("OK")){
            System.out.println("AnswerPing : OK");
            
            //Récuppération des données + envoi à interval régulier
        }
        else{
            System.out.println("Impossible de joindre le serveur AnswerPing");
        }
        
        
        System.out.println("##########################################");
    }
}

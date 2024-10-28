package com.mycompany.sae51_client;

import java.io.IOException;

/**
 *
 * @author Maxime VALLET, Erwann MADEC
 * @version 0.2
 */
public class Main {

    public static void main(String[] args) throws IOException {
        PCInfo pcInfo = new PCInfo();
        
        //Obtention de l'id de la machine
        String id = pcInfo.getPCID();
        
        //Obtention de l'IP de la machine
        String IP = pcInfo.getPCIP();
        
        //Ajout du pc à la DB (serveur TCP PingAnswer : port 4444)
        //à faire : changer localhost par scanner
        TCP_Client tcp = new TCP_Client("localhost", 4444);
        String jsonString = "{\"id\":\""+id+"\", \"IP\":\""+IP+"\"}";
        String result = tcp.run(jsonString);
        
        if(result.equals("OK")){
            //Récuppération des données + envoi à interval régulier
        }
    }
}

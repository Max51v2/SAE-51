package com.mycompany.sae51_client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author Maxime VALLET
 * @version 1.0
 */
public class TCP_Client {
    private String IP;
    private Integer port;
    private PrintWriter out;
    private BufferedReader in;
    
    public TCP_Client(String IP, Integer port){
        this.IP = IP;
        this.port = port;
    }
    
    
    public String run(String message) throws IOException{
        //Ouverture socket
        Socket socket = new Socket(this.IP, this.port);
        
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        //Envoi du message
        out.println(message);
        
        //Message reçu
        String response = in.readLine();
        
        //Fermeture du socket
        in.close();
        out.close();
        socket.close();
        
        return response;
    }
}

package ServletsPC;

import Autre.AddLog;
import DAO.DAOPC;
import DAO.DAOusers;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Maxime VALLET
 * @version 1.0
 */
@WebServlet(name = "ChangePCState", urlPatterns = {"/ChangePCState"})
public class ChangePCState extends HttpServlet {

    /**
     * Liste les utilisateurs qui ont accès au pc<br><br>
     * 
     * Variables à envoyer au servlet (POST)<br>
     * Integer id       &emsp;&emsp;        id de l'ordinateur <br>
     * Integer message       &emsp;&emsp;        action à effectuer sur le PC (shutdown | restart | update) <br>
     * String token       &emsp;&emsp;        token de l'utilisateur qui fait la demande <br>
     * String Test       &emsp;&emsp;        BD à utiliser (true : test | false : sae_51) <br>
     * 
     * <br>
     * Variables renvoyées par le servlet (JSON)<br>
     * String erreur       &emsp;&emsp;        types d'erreur : champ(s) manquant (req) | accès refusé | SecureServer failure | none <br>
     * 
     * @param request       servlet request
     * @param response      servlet response
     * @throws      ServletException if a servlet-specific error occurs
     * @throws      IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //Type de la réponse
        response.setContentType("application/json;charset=UTF-8");
        
        DAO.DAOPC DAO2 = new DAOPC();
        DAO.DAOusers DAO = new DAOusers();

        //Nom du servlet
        String servletName = "ChangePCState";
        
        //Récuperation du JSON envoyé
        BufferedReader reader = request.getReader();
        Gson gsonRequest = new Gson();
        
        //Convertion des données du JSON dans un objet Java
        JSON.GetJSONInfoPC json = gsonRequest.fromJson(reader, JSON.GetJSONInfoPC.class);
        
        //Données envoyées par la requête
        Integer id = json.getId();
        String message = json.getMessage();
        String token = json.getToken();
        Boolean TestBoolean = Boolean.valueOf(json.getTest());

        //Données
        String rights = "Aucun";
        String jsonString = "{\"erreur\":\"SecureServer failure\"}";
        String loginLog = "Aucun";
        
        //Vérification du contenu envoyé
        if(token == null | id == null | message == null){
            //JSON renvoyé
            jsonString = "{\"erreur\":\"champ(s) manquant (req)\"}";
        }
        else{
            if(token.equals("") | message.equals("")){
                //JSON renvoyé
                jsonString = "{\"erreur\":\"champ(s) manquant (req)\"}";
            }
            else{
                //Récuppération des droits de l'utilisateur
                rights = DAO.getUserRightsFromToken(token, TestBoolean);
                
                //Récuppération des droits d'accès au servlet (Merci d'ajouter votre servlet à la BD => voir "README_Java.txt" dossier "/Serveur")
                String access = DAO.getServletRights(servletName, rights, false);

                //Si l'utilisateur a les droits
                if(access.equals("true")){

                    //Ajout du message qui sera exécuté par l'instance de SecureServer situé dans OnStart.java
                    String login = DAO.getLogin();
                    DAO2.addStatusChange(id, message, login, TestBoolean);
                    
                    //Vérification que la commande s'est bien effectuée
                    Integer timeout = 10;
                    long pause = 500;
                    Integer c = 0;
                    Boolean executed = null;
                    
                    //On effectue le vérification plusieurs fois pour prendre en compte la latence du serveur
                    while(c < timeout){
                        executed = DAO2.checkStatusChange(id, pause, TestBoolean);
                        
                        if(executed == true){
                            jsonString = "{\"erreur\":\"none\"}";
                            
                            break;
                        }
                        
                        c += 1;
                    }
                }
                else{
                    //JSON renvoyé
                    jsonString = "{\"erreur\":\"accès refusé\"}";
                }
            }
        }
        

        //Log
        loginLog = DAO.getLogin();
        AddLog addLog = new AddLog();
        addLog.addLog(gsonRequest, request, loginLog, jsonString, TestBoolean, servletName, rights);

        //Envoi des données
        try (PrintWriter out = response.getWriter()) {
            out.print(jsonString);
            out.flush();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}

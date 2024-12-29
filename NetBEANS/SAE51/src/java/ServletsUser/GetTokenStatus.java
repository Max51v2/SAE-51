package ServletsUser;

import Autre.AddLog;
import Autre.ProjectConfig;
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
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author Maxime VALLET
 * @version 1.0
 */
@WebServlet(name = "GetTokenStatus", urlPatterns = {"/GetTokenStatus"})
public class GetTokenStatus extends HttpServlet {

    /**
     * Récupère le status du token ainsi que sa durée de vie<br><br>
     * 
     * Variables à envoyer au servlet (POST)<br>
     * String token       &emsp;&emsp;        token de l'utilisateur qui fait la demande <br>
     * String Test       &emsp;&emsp;        BD à utiliser (true : test | false : sae_51) <br>
     * 
     * <br>
     * Variables renvoyées par le servlet (JSON)<br>
     * String erreur       &emsp;&emsp;        types d'erreur : champ manquant (req) | accès refusé | login existe (DB) | none <br>
     * String tokenStatus       &emsp;&emsp;        status du token : almostExpired | valid <br>
     * Integer timeLeft       &emsp;&emsp;        temps restant en secondes <br>
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
        
        //Nom du servlet
        String servletName = "GetTokenStatus";
        
        DAOusers DAO = new DAOusers();
        ProjectConfig conf = new ProjectConfig();
        
        //Récuperation du JSON envoyé
        BufferedReader reader = request.getReader();
        Gson gsonRequest = new Gson();
        
        // Convertion des données du JSON dans un objet Java
        JSON.GetJSONInfoUsers user = gsonRequest.fromJson(reader, JSON.GetJSONInfoUsers.class);
        
        //Données
        String token = user.getToken();
        Boolean TestBoolean = Boolean.valueOf(user.getTest());
        String rights = "Aucun";
        Boolean doLoginExist;
        String jsonString = "";
        String loginLog = "Aucun";
        Integer LifeCycleLeft = 0;
        String tokenStatus = "Expired";
        Integer TokenLifeCycleWarning = 0;
        Integer CheckIntervall = 0;
        
        
        //Vérification du contenu envoyé
        if(token == null ){
            //JSON renvoyé
            jsonString = "{\"erreur\":\"champ manquant (req)\"}";
        }
        else{
            //Vérification du contenu envoyé
            if(token.equals("")){
                //JSON renvoyé
                jsonString = "{\"erreur\":\"champ manquant (req)\"}";
            }
            else{
                //Récuppération des droits de l'utilisateur
                rights = DAO.getUserRightsFromToken(token, TestBoolean);
                
                //Récuppération des droits d'accès au servlet
                String access = DAO.getServletRights(servletName, rights, false);

                if(access.equals("true")){
                    //Récuppération du login de l'utilisateur
                    String login = DAO.getUserLoginFromToken(token, TestBoolean);
                    
                    //Récuppération du nombre de cycles de vie restant du token de l'utilisateur
                    LifeCycleLeft = DAO.getTokenLifeCycle(login, TestBoolean);
                    
                    //Vérification de l'éxpiration du token
                    if(TestBoolean == true){
                        TokenLifeCycleWarning = 2;
                    }
                    else{
                        TokenLifeCycleWarning = conf.getIntValue("TokenLifeCycleWarning");
                    }
                    if(LifeCycleLeft == 0){
                        tokenStatus = "expired";
                    }
                    else if(LifeCycleLeft <= TokenLifeCycleWarning){
                        tokenStatus = "almostExpired";
                    }
                    else{
                        tokenStatus = "valid";
                    }
                    
                    //Calcul du temps de vie restant du token
                    if(TestBoolean == true){
                        CheckIntervall = 10000;
                    }
                    else{
                        CheckIntervall = conf.getIntValue("CheckIntervall");
                    }
                    Integer timeLeft = Math.round(CheckIntervall/1000) * LifeCycleLeft;
                    
                    //JSON renvoyé
                    jsonString = "{\"erreur\":\"none\", \"tokenStatus\":\""+tokenStatus+"\", \"timeLeft\":\""+timeLeft+"\"}";
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

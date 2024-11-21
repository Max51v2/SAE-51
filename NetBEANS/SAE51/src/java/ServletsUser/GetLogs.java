package ServletsUser;

import Autre.ProjectConfig;
import DAO.DAOLogs;
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
 * @version 1.1
 */
@WebServlet(name = "GetLogs", urlPatterns = {"/GetLogs"})
public class GetLogs extends HttpServlet {

    /**
     * Renvoi les logs sur une période donnée<br><br>
     * 
     * Variables à envoyer au servlet (POST)<br>
     * String beginDate       &emsp;&emsp;        date de début (AAAAMMJJ_hhmmss) <br>
     * String endDate       &emsp;&emsp;        date de fin (AAAAMMJJ_hhmmss) <br>
     * String logLevelReq       &emsp;&emsp;        type de log ("ErrorsOnly" ou "All") <br>
     * String token       &emsp;&emsp;        token de l'utilisateur qui fait la demande <br>
     * String Test       &emsp;&emsp;        BD à utiliser (true : test | false : sae_51) <br>
     * 
     * <br>
     * Variables renvoyées par le servlet (JSON)<br>
     * String erreur       &emsp;&emsp;        types d'erreur : champ(s) manquant (req) | mauvaise période (req) | accès refusé <br>
     * OU <br>
     * String id       &emsp;&emsp;        id du log <br>
     * String servlet       &emsp;&emsp;        servlet concerné <br>
     * String ip       &emsp;&emsp;        ip de l'utilisateur <br>
     * String login       &emsp;&emsp;        login de l'utilisateur <br>
     * String droits       &emsp;&emsp;        droits de l'utilisateur <br>
     * String error       &emsp;&emsp;       erreur rencontrée par l'utilisateur ("none" si aucune erreur) <br>
     * String date       &emsp;&emsp;        date du log <br>
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
        String servletName = "GetLogs";
        
        DAO.DAOusers DAO = new DAOusers();
        DAOLogs log = new DAOLogs();
        
        //Récuperation du JSON envoyé
        BufferedReader reader = request.getReader();
        Gson gsonRequest = new Gson();
        
        //Convertion des données du JSON dans un objet Java
        JSON.GetJSONInfoUsers user = gsonRequest.fromJson(reader, JSON.GetJSONInfoUsers.class);
        
        //Données envoyées par la requête
        String token = user.getToken();
        String beginDate = user.getBeginDate();
        String endDate = user.getEndDate();
        String logLevelReq = user.getLogLevelReq();
        Boolean TestBoolean = Boolean.valueOf(user.getTest());

        //Données
        String rights = "Aucun";
        String jsonString = "";
        String loginLog = "Aucun";
        String error = "no error";
        
        //Vérification du contenu envoyé
        if(token == null | beginDate == null | endDate == null | logLevelReq == null){
            jsonString = "{\"erreur\":\"champ(s) manquant (req)\"}";
        }
        else{
            if(token.equals("") | beginDate.equals("") | endDate.equals("") | logLevelReq.equals("")){
                jsonString = "{\"erreur\":\"champ(s) manquant (req)\"}";
            }
            else{
                //Vérification de la période envoyée
                String beginDate1 = beginDate.substring(0,beginDate.indexOf("_"));
                String beginDate2 = beginDate.substring(beginDate.indexOf("_")+1, beginDate.length());
                Long beginDate3 = Long.valueOf(beginDate1+beginDate2);
                String endDate1 = endDate.substring(0,endDate.indexOf("_"));
                String endDate2 = endDate.substring(endDate.indexOf("_")+1, endDate.length());
                Long endDate3 = Long.valueOf(endDate1+endDate2);
                
                //Si la date max est se situe avant ou au même moment que celle de début
                if(endDate3 <= beginDate3){
                    jsonString = "{\"erreur\":\"mauvaise période (req)\"}";
                }
                else{
                    //Récuppération des droits de l'utilisateur
                    rights = DAO.getUserRightsFromToken(token, TestBoolean);

                    //Récuppération des droits d'accès au servlet (Merci d'ajouter votre servlet à la BD => voir "README_Java.txt" dossier "/Serveur")
                    String access = DAO.getServletRights(servletName, rights, false);

                    if(access.equals("true")){
                        //Récupération des logs
                        jsonString = log.getLogsFromPeriod(beginDate, endDate, logLevelReq, TestBoolean);
                        
                        error = "none";
                    }
                    else{
                        jsonString = "{\"erreur\":\"accès refusé\"}";
                    }
                }
            }
        }
        
        //Log
        loginLog = DAO.getLogin();
        if(!error.equals("none")){
            JSON.GetJSONInfoUsers JSONlog = gsonRequest.fromJson(jsonString, JSON.GetJSONInfoUsers.class);
            error = JSONlog.getErreur();
        }
        ProjectConfig conf = new ProjectConfig();
        String LogLevel = conf.getStringValue("LogLevel");
        //Enregistrement des logs
        if(LogLevel.equals("ErrorsOnly") & ! error.equals("none") & TestBoolean == false){
            log.addLog(servletName, request.getRemoteAddr(), loginLog, rights, error, TestBoolean);
        }
        else if(LogLevel.equals("All") & TestBoolean == false){
            log.addLog(servletName, request.getRemoteAddr(), loginLog, rights, error, TestBoolean);    
        }

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

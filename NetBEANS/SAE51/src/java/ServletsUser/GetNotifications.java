package ServletsUser;

import Autre.AddLog;
import DAO.DAONotifications;
import DAO.DAOusers;
import JSON.GetTHEJSON;
import JSON.Jackson;
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
@WebServlet(name = "GetNotifications", urlPatterns = {"/GetNotifications"})
public class GetNotifications extends HttpServlet {

    /**
     * Renvoi les notifications d'un utilisateur<br><br>
     * 
     * Variables à envoyer au servlet (POST)<br>
     * String token       &emsp;&emsp;        token utilisateur <br>
     * Integer id       &emsp;&emsp;        id du PC <br>
     * String Test       &emsp;&emsp;        BD à utiliser (true : test | false : sae_51) <br>
     * 
     * <br>
     * Variables renvoyées par le servlet (JSON)<br>
     * String erreur       &emsp;&emsp;        types d'erreur : champ(s) manquant (req) | accès refusé <br>
     * OU
     * String description       &emsp;&emsp;        description de la notification <br>
     * String content       &emsp;&emsp;        contenu de la notification <br>
     * String date       &emsp;&emsp;        date de la notification (format : "yyyyMMdd_HHmmss") <br>
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
        String servletName = request.getServletPath().substring(request.getServletPath().lastIndexOf("/")+1);
        
        DAO.DAOusers DAO = new DAOusers();
        DAONotifications DAONotifications = new DAONotifications();
        
        //Récuperation du JSON envoyé
        Jackson jack = new Jackson();
        GetTHEJSON json = jack.GetServletJSON(request);
        
        //Données envoyées par la requête
        String token = json.getToken();
        Boolean TestBoolean = Boolean.valueOf(json.getTest());

        //Données
        String rights = "Aucun";
        String jsonString = "";
        String loginLog = "Aucun";
        String userLogin = "";
        
        //Vérification du contenu envoyé
        if(token == null){
            //JSON renvoyé
            jsonString = "{\"erreur\":\"champ(s) manquant (req)\"}";
        }
        else{
            if(token.equals("")){
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
                    
                    //Récupération du login de l'utilisateur
                    userLogin = DAO.getUserLoginFromToken(token, TestBoolean);
                    
                    if(rights.equals("Admin")){
                        //Récupération de tous les logs
                        jsonString = DAONotifications.getNotifications(userLogin, true, TestBoolean);
                    }
                    else{
                        //Récupération des logs accessibles à l'utilisateur
                        jsonString = DAONotifications.getNotifications(userLogin, false, TestBoolean);
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
        addLog.addLog(jsonString, request, loginLog, TestBoolean, servletName, rights);

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

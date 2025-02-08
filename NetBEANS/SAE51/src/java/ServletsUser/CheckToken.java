package ServletsUser;

import Autre.AddLog;
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
 * @version 1.2
 */
@WebServlet(name = "CheckToken", urlPatterns = {"/CheckToken"})
public class CheckToken extends HttpServlet {

    /**
     * Vérifie si le token donné est dans la BD<br><br>
     * 
     * Variables à envoyer au servlet (POST)<br>
     * String token       &emsp;&emsp;        login de l'utilisateur <br>
     * String Test       &emsp;&emsp;        BD à utiliser (true : test | false : sae_51) <br>
     * 
     * <br>
     * Variables renvoyées par le servlet (JSON)<br>
     * String erreur       &emsp;&emsp;        types d'erreur : pas de token (req) | pas de token (DB) | none <br>
     * String droits       &emsp;&emsp;        droits de l'utilisateur <br>
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
        
        DAOusers DAO = new DAOusers();
        
        //Récuperation du JSON envoyé
        Jackson jack = new Jackson();
        GetTHEJSON json = jack.GetServletJSON(request);
        
        //Données
        String token = json.getToken();
        Boolean TestBoolean = Boolean.valueOf(json.getTest());
        
        //Création du JSON à renvoyer (vide)
        String jsonString = "";
        String loginLog = "Aucun";
        
        //Si login null alors on ne fait rien
        if(token == null){
                //JSON renvoyé
                jsonString = "{\"erreur\":\"pas de token (req)\"}";
        }
        else{
            //Si login vide alors on ne fait rien
            if(token.equals("")){
                    //JSON renvoyé
                    jsonString = "{\"erreur\":\"pas de token (req)\"}";
            }
            else{
                try { 
                    //Vérification du token
                    jsonString = DAO.checkToken(token, TestBoolean);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        
        
        //Droits d'infos sur l'utilisateur pour les logger (exclusif à ce servlet)
        json = jack.GetStringJSON(jsonString);
        String rights = json.getDroits();
        if(rights == null){
            rights = "Aucun";
        }
        else{
            if(rights.equals("")){
                rights = "Aucun";
            }
        }
        
        
        //Log
        //loginLog = DAO.getLogin();
        //AddLog addLog = new AddLog();
        //addLog.addLog(jsonString, request, loginLog, TestBoolean, servletName, rights);
        
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

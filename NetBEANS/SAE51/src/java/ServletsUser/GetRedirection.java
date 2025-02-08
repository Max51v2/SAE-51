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
 * @version 1.5
 */
@WebServlet(name = "GetRedirection", urlPatterns = {"/GetRedirection"})
public class GetRedirection extends HttpServlet {

    /**
     * Renvoi la page de redirection de l'utilisateur selon sa position et ses droits<br><br>
     * 
     * Variables à envoyer au servlet (POST)<br>
     * String currentPage       &emsp;&emsp;        token de l'utilisateur qui fait la demande <br>
     * String token       &emsp;&emsp;        token de l'utilisateur qui fait la demande ("" ou token) <br>
     * String Test       &emsp;&emsp;        BD à utiliser (true : test | false : sae_51) <br>
     * 
     * <br>
     * Variables renvoyées par le servlet (JSON)<br>
     * String erreur       &emsp;&emsp;        types d'erreur : pas de page (req) | Pas de redirection (BD) | pas de page ou token null (req) | accès refusé | none <br>
     * String redirect       &emsp;&emsp;        la page sur laquelle l'utilisateur doit être redirigé ('none' si pas de redirection !!!) <br>
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
        String currentPage = json.getCurrentPage();
        Boolean TestBoolean = Boolean.valueOf(json.getTest());
        String rights = "Aucun";
        String token = json.getToken();
        String redirect = "";
        String jsonString = "";
        String loginLog = "Aucun";
       
        //Vérification du contenu envoyé
        if(currentPage == null | currentPage == null){
            //JSON renvoyé
            jsonString = "{\"erreur\":\"pas de page ou token null (req)\"}";
        }
        else{
            //Vérification du contenu envoyé
            if(currentPage.equals("")){
                //JSON renvoyé
                jsonString = "{\"erreur\":\"pas de token (req)\"}";
            }
            else{
                //Récuppération des droits de l'utilisateur
                rights = DAO.getUserRightsFromToken(token, TestBoolean);
                
                //Récuppération des droits d'accès au servlet
                String access = DAO.getServletRights("GetRedirection", rights, false);
                
                //Si l'utilisateur a les droits
                if(access.equals("true")){
                    //Récuppération de la redirection de l'utilisateur
                    redirect = DAO.getRedirection(rights, currentPage, TestBoolean);

                    //Vérification de la présence d'une entrée de redirection (BD)
                    if(redirect.equals("No redirect")){
                        //JSON renvoyé
                        jsonString = "{\"erreur\":\"Pas de redirection (BD)\"}";
                    }
                    else{
                        //JSON renvoyé
                       jsonString = "{\"redirect\":\""+redirect+"\", \"erreur\":\"none\"}";
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

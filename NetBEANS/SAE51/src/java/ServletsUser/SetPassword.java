package ServletsUser;

import Autre.AddLog;
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
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author Maxime VALLET
 * @version 1.4
 */
@WebServlet(name = "SetPassword", urlPatterns = {"/SetPassword"})
public class SetPassword extends HttpServlet {

    /**
     * Modifie le MDP d'un utilisateur<br><br>
     * 
     * Variables à envoyer au servlet (POST)<br>
     * String target       &emsp;&emsp;        utilisateur qui subi le changement de MDP <br>
     * String password       &emsp;&emsp;        nouveau MDP de l'utilisateur <br>
     * String token       &emsp;&emsp;        token de l'utilisateur qui fait la demande <br>
     * String Test       &emsp;&emsp;        BD à utiliser (true : test | false : sae_51) <br>
     * 
     * <br>
     * Variables renvoyées par le servlet (JSON)<br>
     * String erreur       &emsp;&emsp;        types d'erreur : champ(s) manquant (req) | permission refusée | accès refusé | done <br>
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
        String servletName = "SetPassword";
        
        DAOusers DAO = new DAOusers();
        DAOLogs log = new DAOLogs();
        
        //Récuperation du JSON envoyé
        BufferedReader reader = request.getReader();
        Gson gsonRequest = new Gson();
        
        // Convertion des données du JSON dans un objet Java
        JSON.GetJSONInfoUsers user = gsonRequest.fromJson(reader, JSON.GetJSONInfoUsers.class);
        
        //Données
        String token = user.getToken();
        String password = user.getPassword();
        String target = user.getTarget();
        Boolean TestBoolean = Boolean.valueOf(user.getTest());
        String rights = "Aucun";
        String hashedPassword = "";
        String login = "";
        String jsonString = "";
        String loginLog = "Aucun";
        
        //Vérification du contenu envoyé
        if(token == null | password == null | target == null){
            jsonString = "{\"erreur\":\"champ(s) manquant (req)\"}";
        }
        else{
            //Vérification du contenu envoyé
            if(token.equals("") | target.equals("") | password.equals("")){
                jsonString = "{\"erreur\":\"champ(s) manquant (req)\"}";
            }
            else{
                //Récuppération des droits de l'utilisateur
                rights = DAO.getUserRightsFromToken(token, TestBoolean);

                //Récuppération des droits d'accès au servlet
                String access = DAO.getServletRights("SetPassword", rights, false);

                //Si l'utilisateur a les droits
                if(access.equals("true")){
                    //Un Admin peut uniquement changer le MDP de tout le monde
                    if(rights.equals("Admin")){
                        //génération du hash du MDP
                        hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12));

                        //Modification du MDP
                        DAO.setPassword(target, hashedPassword, TestBoolean);

                        jsonString = "{\"erreur\":\"none\"}";
                    }
                    //Un utilisateur peut uniquement changer son MDP
                    else if(rights.equals("Utilisateur")){
                        //Récupération du login de l'utilisateur qui a envoyé la demande
                        login = DAO.getUserLoginFromToken(token, TestBoolean);

                        //Si l'utilisateur qui a fait la demande veut modifier SON MDP
                        if(login.equals(target)){
                            //génération du hash du MDP
                            hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12));

                            //Modification du MDP
                            DAO.setPassword(target, hashedPassword, TestBoolean);
                        }
                        else{
                            jsonString = "{\"erreur\":\"permission refusée\"}";
                        }
                    }
                }
                else{
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

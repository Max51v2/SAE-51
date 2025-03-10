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
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author Maxime VALLET
 * @version 1.6
 */
@WebServlet(name = "AddUser", urlPatterns = {"/AddUser"})
public class AddUser extends HttpServlet {

    /**
     * Ajoute un utilisateur dans la BD<br><br>
     * 
     * Variables à envoyer au servlet (POST)<br>
     * String nom       &emsp;&emsp;        nom de l'utilisateur <br>
     * String prenom       &emsp;&emsp;        prenom de l'utilisateur <br>
     * String role       &emsp;&emsp;        droits de l'utilisateur <br>
     * String login       &emsp;&emsp;        login de l'utilisateur <br>
     * String password       &emsp;&emsp;        MDP de l'utilisateur <br>
     * String token       &emsp;&emsp;        token de l'utilisateur qui fait la demande <br>
     * String Test       &emsp;&emsp;        BD à utiliser (true : test | false : sae_51) <br>
     * 
     * <br>
     * Variables renvoyées par le servlet (JSON)<br>
     * String erreur       &emsp;&emsp;        types d'erreur : champ(s) manquant (req) | accès refusé | login existe (DB) | none <br>
     * String login       &emsp;&emsp;        login de l'utilisateur <br>
     * String prenom       &emsp;&emsp;        prenom de l'utilisateur <br>
     * String nom       &emsp;&emsp;        nom de l'utilisateur <br>
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
        String nom = json.getNom();
        String prenom = json.getPrenom();
        String role = json.getDroits();
        String login = json.getLogin();
        String password = json.getPassword();
        Boolean TestBoolean = Boolean.valueOf(json.getTest());
        String rights = "Aucun";
        Boolean doLoginExist;
        String jsonString = "";
        String loginLog = "Aucun";
        
        
        //Vérification du contenu envoyé
        if(token == null | nom == null | prenom == null | login == null | password == null | role == null ){
            //JSON renvoyé
            jsonString = "{\"erreur\":\"champ(s) manquant (req)\"}";
        }
        else{
            //Vérification du contenu envoyé
            if(token.equals("") | nom.equals("") | prenom.equals("") | login.equals("") | password.equals("") | role.equals("")){
                //JSON renvoyé
                jsonString = "{\"erreur\":\"champ(s) manquant (req)\"}";
            }
            else{
                //Récuppération des droits de l'utilisateur
                rights = DAO.getUserRightsFromToken(token, TestBoolean);
                
                //Récuppération des droits d'accès au servlet
                String access = DAO.getServletRights(servletName, rights, false);

                if(access.equals("true")){
                    //On vérifie si l'utilisateur n'existe pas
                    doLoginExist = DAO.doLoginExist(login, TestBoolean);

                    if(doLoginExist == false){
                        //génération du hash du MDP
                        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12));

                        //Ajout de l'utilisateur
                        DAO.addUser(login, nom, prenom, role, hashedPassword, TestBoolean);

                        //JSON renvoyé
                        jsonString = "{\"erreur\":\"none\"}";

                    }
                    else{
                        //JSON renvoyé
                        jsonString = "{\"erreur\":\"login existe (DB)\"}";
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

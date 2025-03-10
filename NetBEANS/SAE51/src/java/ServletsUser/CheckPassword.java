package ServletsUser;

import Autre.AddLog;
import Autre.ProjectConfig;
import DAO.DAOusers;
import JSON.GetTHEJSON;
import JSON.Jackson;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import org.apache.commons.lang3.RandomStringUtils;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author Maxime VALLET
 * @version 1.7
 */
@WebServlet(name = "CheckPassword", urlPatterns = {"/CheckPassword"})
public class CheckPassword extends HttpServlet {

    /**
     * Vérifie si le MDP+login donnés sont OK<br><br>
     * 
     * Variables à envoyer au servlet (POST)<br>
     * String login       &emsp;&emsp;        login de l'utilisateur <br>
     * String password       &emsp;&emsp;        MDP de l'utilisateur (clair) <br>
     * String Test       &emsp;&emsp;        BD à utiliser (true : test | false : sae_51) <br>
     * 
     * <br>
     * Variables renvoyées par le servlet (JSON)<br>
     * String erreur       &emsp;&emsp;        types d'erreur : login ou MDP vide (req) | pas de hash (DB) | mauvais MDP (req) | none <br>
     * String login       &emsp;&emsp;        login de l'utilisateur <br>
     * String droits       &emsp;&emsp;        droits de l'utilisateur <br>
     * String token       &emsp;&emsp;        token de l'utilisateur (clair) <br>
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
        String login = json.getLogin();
        String password = json.getPassword();
        Boolean TestBoolean = Boolean.valueOf(json.getTest());
        String rights = "Aucun";
        String token = "";
        String loginLog = "Aucun";
        String error = "no error";
        
        //Création du JSON à renvoyer (vide)
        String jsonString = "";
        
        //Si login ou MDP null alors on ne fait rien
        if(password == null | login == null){
            //JSON renvoyé
            jsonString = "{\"erreur\":\"login ou MDP vide (req)\"}";
            
        }
        else{
            //Si login ou MDP vide alors on ne fait rien
            if(password.equals("") | login.equals("")){
                //JSON renvoyé
                jsonString = "{\"erreur\":\"login ou MDP vide (req)\"}";
            }
            else{
                try { 
                    //Récuperation du hash stocké dans la BD
                    String hashDB = DAO.getUserPasswordHash(login, TestBoolean);

                    //si il n'y a pas de hash, utilisateur inexistant
                    if(hashDB.equals("")){
                        //JSON renvoyé
                        jsonString = "{\"erreur\":\"pas de hash (DB)\"}";
                    }

                    //l'utilisateur existe mais il faut vérifier le MDP
                    else{
                        //si le hash de la DB est identique au hash envoyé 
                        Boolean isPasswordOK = BCrypt.checkpw(password, hashDB);
                        if(isPasswordOK == true){
                            loginLog = login;
                            
                            //Récupération des droits utilisateur
                            rights = DAO.getUserRightsFromLogin(login, TestBoolean);

                            //Génération d'une chaine de 32 caractères (token)
                            if(TestBoolean == true){
                                //Sert uniquement pour les tests unitaires
                                token = "10101010101010101010101010101010";
                            }
                            else{
                                token = RandomStringUtils.randomAlphanumeric(32);
                            }

                            //Récupération du nombre de passes
                            ProjectConfig conf = new ProjectConfig();
                            Integer rounds = conf.getIntValue("TokenHashRounds");
                            
                            //génération du hash du token
                            String hashedToken = BCrypt.hashpw(token, BCrypt.gensalt(rounds));

                            //Enregistrement du token dans la DB
                            Integer TokenLifeCycle = conf.getIntValue("TokenLifeCycle");
                            DAO.setToken(hashedToken, login, TokenLifeCycle, TestBoolean);

                            //JSON renvoyé
                            jsonString = "{\"droits\":\""+rights+"\", \"token\":\""+token+"\", \"login\":\""+login+"\", \"erreur\":\"none\"}";
                        }
                        else{
                            //JSON renvoyé
                            jsonString = "{\"erreur\":\"mauvais MDP (req)\"}";
                        }
                    }
                } 
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        
        
        //Log
        //loginLog = DAO.getLogin(); //servlet spécifique donc ligne en com
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

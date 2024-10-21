package ServletsUser;

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
 * @author root
 */
@WebServlet(name = "DeleteToken", urlPatterns = {"/DeleteToken"})
public class DeleteToken extends HttpServlet {

    /**
     * Supprime un token de la BD<br><br>
     * 
     * Variables à envoyer au servlet (POST)<br>
     * String login       &emsp;&emsp;        login de l'utilisateur à qui il faut supprimer le token <br>
     * String token       &emsp;&emsp;        token de l'utilisateur qui fait la demande <br>
     * String Test       &emsp;&emsp;        BD à utiliser (true : test | false : sae_51) <br>
     * 
     * <br>
     * Variables renvoyées par le servlet (JSON)<br>
     * String erreur       &emsp;&emsp;        types d'erreur : pas de login ou token (req) | login inexistant (DB) | accès refusé | none <br>
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
        
        DAOusers DAO = new DAOusers();
        
        //Récuperation du JSON envoyé
        BufferedReader reader = request.getReader();
        Gson gsonRequest = new Gson();
        
        // Convertion des données du JSON dans un objet Java
        Autre.GetJSONInfo user = gsonRequest.fromJson(reader, Autre.GetJSONInfo.class);
        
        //Données
        String login = user.getLogin();
        String token = user.getToken();
        Boolean TestBoolean = Boolean.valueOf(user.getTest());
        String rights = "Aucun";
        Boolean doLoginExist;
        String jsonString = "";
        
        //Vérification du contenu envoyé
        if(login.equals("") | token.equals("")){
            jsonString = "{\"erreur\":\"pas de login (req)\"}";
        }
        else{
            //Récuppération des droits de l'utilisateur
            rights = DAO.getUserRightsFromToken(token, TestBoolean);
            
            if(rights.equals("Admin")){
                //Vérification de l'existance du login
                doLoginExist = DAO.doLoginExist(login, TestBoolean);
                
                if(doLoginExist == true){
                    //Suppression du token
                    DAO.deleteToken(login, TestBoolean);
                    
                    //Remise à 0 du lifecycle du token
                    DAO.setLifeCycle(login, 0, TestBoolean);

                    //JSON renvoyé
                    jsonString = "{\"erreur\":\"none\"}";
                }
                else{
                    jsonString = "{\"erreur\":\"login inexistant (DB)\"}";
                }
            }
            else{
                //JSON renvoyé
                jsonString = "{\"erreur\":\"accès refusé\"}";
            }
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

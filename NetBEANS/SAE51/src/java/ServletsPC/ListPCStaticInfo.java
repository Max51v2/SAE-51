package ServletsPC;

import Autre.AddLog;
import DAO.DAOLogs;
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
 * @version 1.2
 */
@WebServlet(name = "ListPCStaticInfo", urlPatterns = {"/ListPCStaticInfo"})
public class ListPCStaticInfo extends HttpServlet {

    /**
     * Liste les infos statiques d'un PC spécifié<br><br>
     * 
     * Variables à envoyer au servlet (POST)<br>
     * String id       &emsp;&emsp;        id de l'ordinateur <br>
     * String token       &emsp;&emsp;        token de l'utilisateur qui fait la demande <br>
     * String Test       &emsp;&emsp;        BD à utiliser (true : test | false : sae_51) <br>
     * 
     * <br>
     * Variables renvoyées par le servlet (JSON)<br>
     * String erreur       &emsp;&emsp;        types d'erreur : champ(s) manquant (req) | accès refusé | none <br>
     * String id       &emsp;&emsp;        id de la machine <br>
     * String cpu_model       &emsp;&emsp;        modèle du procésseur <br>
     * Integer cores       &emsp;&emsp;        nombre de coeurs du CPU <br>
     * Integer threads       &emsp;&emsp;        nombre de threads du CPu <br>
     * String maximum_frequency       &emsp;&emsp;        fréquence max du procésseur (boost) <br>
     * String ram_quantity       &emsp;&emsp;        capacitée totale de la RAM <br>
     * Integer dimm_quantity       &emsp;&emsp;        nombre de barrettes de RAM <br>
     * String dimm_speed       &emsp;&emsp;        vitesse des barrettes de RAM <br>
     * Integer storage_device_number       &emsp;&emsp;        nombre de périphériques de stockages <br>
     * String storage_space       &emsp;&emsp;        capacité de stockage total <br>
     * Integer network_int_number       &emsp;&emsp;        nombre d'interfaces <br>
     * String network_int_speed       &emsp;&emsp;        vitesse des interfaces <br>
     * String os       &emsp;&emsp;        OS utilisé <br>
     * String version       &emsp;&emsp;        version de l'OS <br>
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
        DAOLogs log = new DAOLogs();

        //Nom du servlet
        String servletName = "ListPCStaticInfo";
        
        //Récuperation du JSON envoyé
        BufferedReader reader = request.getReader();
        Gson gsonRequest = new Gson();
        
        //Convertion des données du JSON dans un objet Java
        JSON.GetJSONInfoPC json = gsonRequest.fromJson(reader, JSON.GetJSONInfoPC.class);
        
        //Données envoyées par la requête
        String id = json.getId();
        String token = json.getToken();
        Boolean TestBoolean = Boolean.valueOf(json.getTest());

        //Données
        String rights = "Aucun";
        String jsonString = "";
        String loginLog = "Aucun";
        
        //Vérification du contenu envoyé
        if(token == null | id == null){
            jsonString = "{\"erreur\":\"champ(s) manquant (req)\"}";
        }
        else{
            if(token.equals("") | id.equals("")){
                jsonString = "{\"erreur\":\"champ(s) manquant (req)\"}";
            }
            else{
                //Récuppération des droits de l'utilisateur
                rights = DAO.getUserRightsFromToken(token, TestBoolean);
                
                //Récuppération des droits d'accès au servlet (Merci d'ajouter votre servlet à la BD => voir "README_Java.txt" dossier "/Serveur")
                String access = DAO.getServletRights(servletName, rights, false);

                //Si l'utilisateur a les droits
                if(access.equals("true")){

                    //Récupération des ordinateurs auquel l'utilisateur a accès
                    String login = DAO.getLogin();
                    jsonString = DAO2.getPCStaticInfo(id, login, rights, TestBoolean);
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

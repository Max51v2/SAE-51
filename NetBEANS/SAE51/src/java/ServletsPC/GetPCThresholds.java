package ServletsPC;

import Autre.AddLog;
import DAO.DAOPC;
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
 * @version 1.0
 */
@WebServlet(name = "GetPCThresholds", urlPatterns = {"/GetPCThresholds"})
public class GetPCThresholds extends HttpServlet {

    /**
     * Liste les seuils de chaque métriques d'un PC spécifié<br><br>
     * 
     * Variables à envoyer au servlet (POST)<br>
     * String token       &emsp;&emsp;        token de l'utilisateur qui fait la demande <br>
     * Integer id       &emsp;&emsp;        id du PC <br>
     * String Test       &emsp;&emsp;        BD à utiliser (true : test | false : sae_51) <br>
     * 
     * <br>
     * Variables renvoyées par le servlet (JSON)<br>
     * String erreur       &emsp;&emsp;        types d'erreur : champ(s) manquant (req) | accès refusé |   | none <br>
     * Integer CPUUtilization       &emsp;&emsp;        utilisation du CPU % <br>
     * Integer CPUTemp       &emsp;&emsp;        température du CPU en °C <br>
     * Integer CPUConsumption       &emsp;&emsp;        consommation du CPU en W <br>
     * Integer RAMUtilization       &emsp;&emsp;        utilisation de la RAM en % <br>
     * Integer storageLoad       &emsp;&emsp;        taux d'écritures des appareils en % <br>
     * Integer storageLeft       &emsp;&emsp;        capacité de stockage restante en Go <br>
     * Integer storageTemp       &emsp;&emsp;        température du périphérique en °C <br>
     * Integer storageErrors       &emsp;&emsp;        nombre d'erreurs du périphérique Int <br>
     * Integer networkLatency       &emsp;&emsp;         latence du NIC (avec google par ex) en ms <br>
     * Integer networkBandwidth       &emsp;&emsp;        taux utilisation débit sortant NIC en % <br>
     * Integer fanSpeed       &emsp;&emsp;        taux de vitesse de rotation en % <br>
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

        //Nom du servlet
        String servletName = request.getServletPath().substring(request.getServletPath().lastIndexOf("/")+1);
        
        //Récuperation du JSON envoyé
        Jackson jack = new Jackson();
        GetTHEJSON json = jack.GetServletJSON(request);
        
        //Données envoyées par la requête
        String token = json.getToken();
        Integer id = json.getId();
        Boolean TestBoolean = Boolean.valueOf(json.getTest());

        //Données
        String rights = "Aucun";
        String jsonString = "";
        String loginLog = "Aucun";
        
        //Vérification du contenu envoyé
        if(token == null || id == null){
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

                    //Récupération des ordinateurs auquel l'utilisateur a accès
                    String login = DAO.getLogin();
                    jsonString = DAO2.getThresholds(id, login, TestBoolean);
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

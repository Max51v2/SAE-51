package ServletsPC;

import Autre.AddLog;
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
 * @author MAxime VALLET
 * @version 1.0
 */
@WebServlet(name = "SetThresholds", urlPatterns = {"/SetThresholds"})
public class SetThresholds extends HttpServlet {

    /**
     * Liste les seuils de chaque métriques d'un PC spécifié<br><br>
     * 
     * Variables à envoyer au servlet (POST)<br>
     * Integer CPUUtilization       &emsp;&emsp;        utilisation du CPU % <br>
     * Integer CPUTemp       &emsp;&emsp;        température du CPU en °C <br>
     * Integer CPUConsumption       &emsp;&emsp;        consommation du CPU en W <br>
     * Integer RAMUtilization       &emsp;&emsp;        utilisation de la RAM en % <br>
     * Integer storageLoad       &emsp;&emsp;        taux d'écritures des appareils en % <br>
     * Integer storageLeft       &emsp;&emsp;        capacité de stockage restante en Go <br>
     * Integer storageTemp       &emsp;&emsp;        température du périphérique en °C <br>
     * Integer storageErrors       &emsp;&emsp;        nombre d'erreurs du périphérique Int <br>
     * Integer networkLatency       &emsp;&emsp;         latence du NIC (avec google par ex) en ms <br>
     * Integer networkBandwith       &emsp;&emsp;        taux utilisation débit sortant NIC en % <br>
     * Integer fanSpeed       &emsp;&emsp;        taux de vitesse de rotation en % <br>
     * String token       &emsp;&emsp;        token de l'utilisateur qui fait la demande <br>
     * Integer id       &emsp;&emsp;        id du PC <br>
     * String Test       &emsp;&emsp;        BD à utiliser (true : test | false : sae_51) <br>
     * 
     * <br>
     * Variables renvoyées par le servlet (JSON)<br>
     * String erreur       &emsp;&emsp;        types d'erreur : champ(s) manquant (req) | accès refusé | none <br>
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
        String servletName = "SetThresholds";
        
        //Récuperation du JSON envoyé
        BufferedReader reader = request.getReader();
        Gson gsonRequest = new Gson();
        
        //Convertion des données du JSON dans un objet Java
        JSON.GetJSONInfoPC json = gsonRequest.fromJson(reader, JSON.GetJSONInfoPC.class);
        
        //Données envoyées par la requête
        Integer CPUUtilization = json.getCPUUtilization();
        Integer CPUTemp = json.getCPUTemp();
        Integer CPUConsumption = json.getCPUConsumption();
        Integer RAMUtilization = json.getRAMUtilization();
        Integer storageLoad = json.getStorageLoad();
        Integer storageLeft = json.getStorageLeft();
        Integer storageTemp = json.getStorageTemp();
        Integer storageErrors = json.getStorageErrors();
        Integer networkLatency = json.getNetworkLatency();
        Integer networkBandwith = json.getNetworkBandwith();
        Integer fanSpeed = json.getFanSpeed();
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
                    DAO2.addThresholds(id, CPUUtilization, CPUTemp, CPUConsumption, RAMUtilization, storageLoad, storageLeft, storageTemp, storageErrors, networkLatency, networkBandwith, fanSpeed, TestBoolean);
                    
                    //JSON renvoyé
                jsonString = "{\"erreur\":\"none\"}";
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

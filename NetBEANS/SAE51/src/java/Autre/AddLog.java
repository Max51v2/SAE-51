package Autre;

import DAO.DAOLogs;
import DAO.DAOPC;
import DAO.DAOusers;
import javax.servlet.http.HttpServletRequest;

/**
 * Ajoute ou non les logs
 * 
 * @author Maxime VALLET
 * @version 1.0
 */
public class AddLog {
    DAO.DAOusers DAO = new DAOusers();
    DAO.DAOPC DAO2 = new DAOPC();
    DAOLogs log = new DAOLogs();
    
    public void addLog(String jsonString, HttpServletRequest request, String loginLog, Boolean TestBoolean, String servletName, String rights) {
        String error = "none";

        //Si le json contient le champ erreur, alors on la récupère, sinon il n'y a pas d'erreur
        if (jsonString.contains("erreur")) {
            Integer startIndex = jsonString.indexOf("\"erreur\":\"") + 10;
            jsonString = jsonString.substring(startIndex);
            Integer endIndex = jsonString.indexOf("\"");
            error = jsonString.substring(0, endIndex);
            jsonString = jsonString.substring(endIndex+1);
            
            //Vérification de la présence de plusieurs champs erreur (peut arrive dans le cas de GetLogs)
            if(jsonString.contains("erreur")){
                error = "none";
            }
        }
        else{
            error = "none";
        }

        //Récupération du niveau de log
        ProjectConfig conf = new ProjectConfig();
        String LogLevel = conf.getStringValue("LogLevel");

        //Enregistrement des logs
        if ("ErrorsOnly".equals(LogLevel) && !error.equals("none") && !TestBoolean) {
            log.addLog(servletName, request.getRemoteAddr(), loginLog, rights, error, TestBoolean);
        } else if ("All".equals(LogLevel) && !TestBoolean) {
            log.addLog(servletName, request.getRemoteAddr(), loginLog, rights, error, TestBoolean);
        }
    }
}

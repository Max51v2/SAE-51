package Autre;

import DAO.DAOLogs;
import DAO.DAOPC;
import DAO.DAOusers;
import com.google.gson.Gson;
import javax.servlet.http.HttpServletRequest;

/**
 * Ajoute ou non les logs
 * 
 * @author Maxime VALLET
 */
public class AddLog {
    DAO.DAOusers DAO = new DAOusers();
    DAO.DAOPC DAO2 = new DAOPC();
    DAOLogs log = new DAOLogs();
    
    public void addLog(Gson gsonRequest, HttpServletRequest request, String loginLog, String jsonString, Boolean TestBoolean, String servletName, String rights){
        String error ="";
        
        //Si le json contient le champ erreur alors on la récupère, sinon il n'y a pas d'erreur
        if(jsonString.contains("\"erreur\":") == true){
            JSON.GetJSONInfoUsers JSONlog = gsonRequest.fromJson(jsonString, JSON.GetJSONInfoUsers.class);
            error = JSONlog.getErreur();
        }
        else{
            error = "none";
        }
        
        //Récupération du niveau de log
        ProjectConfig conf = new ProjectConfig();
        String LogLevel = conf.getStringValue("LogLevel");
        
        //Enregistrement des logs
        if(LogLevel.equals("ErrorsOnly") & ! error.equals("none") & TestBoolean == false){
            log.addLog(servletName, request.getRemoteAddr(), loginLog, rights, error, TestBoolean);
        }
        else if(LogLevel.equals("All") & TestBoolean == false){
            log.addLog(servletName, request.getRemoteAddr(), loginLog, rights, error, TestBoolean);    
        }
    }
}

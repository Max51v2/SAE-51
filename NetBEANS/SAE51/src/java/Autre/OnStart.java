package Autre;


import TCP_Server.SecureServer;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Démarre la classe qui vérifie l'expiration des tokens
 * 
 * @author Maxime VALLET
 * @version 0.7
 */
public class OnStart implements ServletContextListener {
  TokenExpiration run = new TokenExpiration();
  ProjectConfig conf = new ProjectConfig();
  SecureServer run3 = new SecureServer();
  
  @Override
  public void contextInitialized(ServletContextEvent sce) {
        //Récupération des données dans le fichier de conf
        Integer CheckIntervall = conf.getIntValue("CheckIntervall");
        Integer AnswerPingPort = conf.getIntValue("AnswerPingPort");
        
        System.out.println("##########################################");
        
        //lancement de la vérification des tokens
        run.start(CheckIntervall, false);
        System.out.println("Vérification des tokens expirés lancée");
        
        //lancement du serveur de réponse au ping du client

        run3.start(12345);
        
        System.out.println("##########################################");
  }
  
  @Override
  public void contextDestroyed(ServletContextEvent sce) {
        //Arrêt de la vérification des tokens
         if (run != null) {
            run.stop();
            System.out.println("Vérification des tokens expirés arrêtée");
        }
         
        //Arrêt du serveur de réponse au ping du client
          if (run3 != null) {
            run3.stop();
           System.out.println("Serveur TCP info arrêté");
        } 
    }
}
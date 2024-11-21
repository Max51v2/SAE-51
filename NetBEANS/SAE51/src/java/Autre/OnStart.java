package Autre;
import TCP_Server.AnswerPing;
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
  AnswerPing run2 = new AnswerPing();
  ProjectConfig conf = new ProjectConfig();
  
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
        run2.start(AnswerPingPort);
        System.out.println("Serveur TCP de réponse au ping du client lancé sur le port "+AnswerPingPort);
        
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
         if (run2 != null) {
            run2.stop();
            System.out.println("Serveur TCP de réponse au ping du client arrêté");
        } 
    }
}
package Autre;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Démarre la classe qui vérifie l'expiration des tokens
 * @author root
 */
public class OnStart implements ServletContextListener {
  TokenExpiration run = new TokenExpiration();
  
  @Override
  public void contextInitialized(ServletContextEvent sce) {
        //lancement de la vérification
        run.start();
        System.out.println("Vérification des tokens expirés lancée");
  }

  @Override
  public void contextDestroyed(ServletContextEvent sce) {
        //Arrêt de la vérification
         if (run != null) {
            run.stop();
            System.out.println("Vérification des tokens expirés arrêtée");
        }
        
  }
}

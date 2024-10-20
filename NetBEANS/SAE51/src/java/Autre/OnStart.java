package Autre;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import static org.apache.tomcat.jni.Pool.clear;

/**
 * Démarre la classe qui vérifie l'expiration des tokens
 * @author Maxime VALLET
 */
public class OnStart implements ServletContextListener {
  TokenExpiration run = new TokenExpiration();
  
  @Override
  public void contextInitialized(ServletContextEvent sce) {
        //lancement de la vérification des tokens
        run.start();
        System.out.println("Vérification des tokens expirés lancée");
        
        System.out.println("##########################################");
  }

  @Override
  public void contextDestroyed(ServletContextEvent sce) {
        //Arrêt de la vérification des tokens
         if (run != null) {
            run.stop();
            System.out.println("Vérification des tokens expirés arrêtée");
        }
        
  }
}

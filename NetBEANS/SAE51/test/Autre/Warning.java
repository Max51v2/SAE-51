package Autre;

import org.junit.Test;

/**
 *
 * @author Maxime VALLET
 * @version 1.0
 */
public class Warning {

    @Test
    public void WarningMsg() {
        System.out.println("");
        System.out.println("###########################################################################################");
        System.out.println("INFO : AUCUN test ne doit laisser de données dans la BD de test sinon il y aura des erreurs");
        System.out.println("INFO : Veuillez démarrer le projet avant de lancer les test sinon il y aura des erreurs");
        System.out.println("###########################################################################################");
    }
}

package Main;


/**
 * Classe qui lance tous les tests unitaires
 * ATTENTION : Veuillez à lancer le projet avant d'exécuter les tests sinon ils vont échouer
 * 
 * @author Maxime VALLET
 * @version 0.9
 */
import Autre.TokenExpirationTest;
import Autre.Warning;
import DAO.DAOusersTest;
import ServletsPC.DeletePCTest;
import ServletsPC.GetPCThresholdsTest;
import ServletsPC.ListPCStaticInfoTest;
import ServletsPC.ListPCTest;
import ServletsPC.ListUsersWithAccessTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import ServletsUser.AddUserTest;
import ServletsUser.CheckPasswordTest;
import ServletsUser.CheckTokenTest;
import ServletsUser.DeleteTokenTest;
import ServletsUser.DeleteUserTest;
import ServletsUser.GetAccessiblePagesTest;
import ServletsUser.GetCheckIntervallTest;
import ServletsUser.GetLogsTest;
import ServletsUser.GetNotificationsTest;
import ServletsUser.GetRedirectionTest;
import ServletsUser.GetTokenStatusTest;
import ServletsUser.ListUsersTest;
import ServletsUser.SetPasswordTest;


@RunWith(Suite.class)
@Suite.SuiteClasses({
    AddUserTest.class,
    CheckPasswordTest.class,
    CheckTokenTest.class,
    DeleteTokenTest.class,
    DeleteUserTest.class,
    GetRedirectionTest.class,
    ListUsersTest.class,
    DAOusersTest.class,
    SetPasswordTest.class,
    TokenExpirationTest.class,
    GetLogsTest.class,
    DeletePCTest.class,
    ListPCStaticInfoTest.class,
    ListPCTest.class,
    GetTokenStatusTest.class,
    GetCheckIntervallTest.class,
    GetAccessiblePagesTest.class,
    GetNotificationsTest.class,
    ListUsersWithAccessTest.class,
    GetPCThresholdsTest.class,
    Warning.class //Laisser en dernier
})
public class RunTests {}

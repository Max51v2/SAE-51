package Main;


/**
 *
 * @author Maxime VALLET
 * @version 0.3
 */
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import ServletsUser.AddUserTest;
import ServletsUser.CheckPasswordTest;
import ServletsUser.CheckTokenTest;
import ServletsUser.DeleteTokenTest;
import ServletsUser.DeleteUserTest;
import ServletsUser.GetRedirectionTest;
import ServletsUser.ListUsersTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    AddUserTest.class,
    CheckPasswordTest.class,
    CheckTokenTest.class,
    DeleteTokenTest.class,
    DeleteUserTest.class,
    GetRedirectionTest.class,
    ListUsersTest.class
})
public class RunTests {}

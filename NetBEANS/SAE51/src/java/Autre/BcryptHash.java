package Autre;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author root
 */
public class BcryptHash {
    
    public String getBcryptHash(String password){
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12));
        
        return hashedPassword;
    }
}

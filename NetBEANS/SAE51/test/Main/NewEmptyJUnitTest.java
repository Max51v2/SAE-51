package Main;

import DAO.DAOPC;
import org.junit.Test;
import java.util.ArrayList;

/**
 *
 * @author root
 */
public class NewEmptyJUnitTest {

    //Ajout PC : insert into pc (id, ip, droits) values (1, 'test', '');
    // relancer projet
    //Changer status : update pc_status set status = 'En Ligne' where id = 1;
    @Test
    public void hello() {
        DAOPC DAO = new DAOPC();
        ArrayList<String> messages = new ArrayList<>();
        
        
        messages = DAO.checkThresholds(1, false);
        
        Integer c=0;
        while(c < messages.size()){
            System.out.println("M"+c+" : "+messages.get(c));
            
            c += 1;
        }
    }
}

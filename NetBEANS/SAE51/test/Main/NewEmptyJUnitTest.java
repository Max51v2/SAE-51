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
        
        System.out.println(DAO.getArrayList("/a/bb//ccc/dddd/").toString());
        
    }
}

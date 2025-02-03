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
        //Données à envoyer
                            Integer id = 1;
                            Integer CPUUtilization = 2;
                            Integer CPUTemp = 3;
                            Integer CPUConsumption = 4;
                            Integer RAMUtilization = 5;
                            Integer storageLoad = 6;
                            Integer storageLeft = 7;
                            Integer storageTemp = 8;
                            Integer storageErrors = 9;
                            Integer networkLatency = 10;
                            Integer networkBandwith = 11;
                            Integer fanSpeed = 12;
                            Boolean Test = false;

                            DAO.addThresholds(id, CPUUtilization, CPUTemp, CPUConsumption, RAMUtilization, storageLoad, storageLeft, storageTemp, storageErrors, networkLatency, networkBandwith, fanSpeed, Test);
        
    }
}

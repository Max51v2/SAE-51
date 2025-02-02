/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package Main;

import DAO.DAOPC;
import org.junit.Test;

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
        //DAO.addPCDynamicInfo(1, false);
    }
}

package com.mycompany.sae51_client;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class test {
    public static void main(String[] args) throws UnknownHostException {
        PCInfo pcInfo = new PCInfo();

        //Obtention de l'id de la machine
        String id = pcInfo.getPCID();

        //Obtention de l'IP de la machine
        String IP = pcInfo.getPCIP();

        //Obtention de l'IP du serveur
        ProjectConfig conf = new ProjectConfig();
        String ServerIP = conf.getStringValue("ServerIP");
    }
}

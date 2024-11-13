import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.sun.jna.platform.win32.COM.WbemcliUtil;
import com.sun.jna.platform.win32.Ole32;
import com.sun.jna.platform.win32.WinNT;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.Sensors;
import oshi.software.os.OperatingSystem;

import static java.lang.Thread.sleep;

public class Main {
    public static void main(String[] args) throws UnknownHostException, InterruptedException {
        String[] Info_hardware = new String[7];
        SystemInfo systemInfo = new SystemInfo();

        CentralProcessor processor = systemInfo.getHardware().getProcessor();
        GlobalMemory memory = systemInfo.getHardware().getMemory();
        OperatingSystem os = systemInfo.getOperatingSystem();

        String osFamily = os.getFamily(); // Famille de l'OS, ex. : "Windows"
        String osVersion = os.getVersionInfo().getVersion(); // Version de l'OS, ex. : "24H2"
        String buildNumber = os.getVersionInfo().getBuildNumber(); // Numéro de build

        Info_hardware[0] = os.getFamily();
        Info_hardware[1] = os.getVersionInfo().getVersion();
        Info_hardware[2] = os.getVersionInfo().getBuildNumber();
        Info_hardware[3] = processor.getProcessorIdentifier().getName();
        Info_hardware[4] = memory.getTotal() / (1024 * 1024) / 1000 + "";
        Info_hardware[5] = processor.getLogicalProcessorCount() + "";
        Info_hardware[6] = processor.getPhysicalProcessorCount() + "";
        for (int i = 0; Info_hardware.length > i; i++) {
            System.out.println(Info_hardware[i]);
        }
        InetAddress local = // adresse de la machine locale
                InetAddress.getLocalHost();
        //InetAddress nancy = // adresse principale du domaine
          //      InetAddress.getByName("plex.madec.ovh");
        System.out.println("local :\t" + local.getHostAddress());
// Adresse IP (byte[])
        //System.out.println("nancy :\t" + nancy.getHostAddress());
        long maxFreqHz = processor.getMaxFreq();

        
        // Afficher la fréquence en GHz
        System.out.printf("Fréquence maximale du processeur : %.2f GHz%n", maxFreqHz / 1_000_000_000.0);
        while (true) {// Obtenir les fréquences actuelles par cœur (en Hz)
            long freq = 0;
            long[] currentFreqs = processor.getCurrentFreq();
            for (int i = 0; i < currentFreqs.length; i++) {
                freq += currentFreqs[i];
            }
            double averageFreq = 0;
            averageFreq = freq / (double) currentFreqs.length / 1_000_000_000.0;

            // Affichage de la fréquence globale moyenne
            double cpuLoad = processor.getSystemCpuLoad(1000);

            // Conversion en pourcentage
            System.out.printf("Charge moyenne du processeur : %.1f%%%n", cpuLoad * 100);
            System.out.printf("Fréquence moyenne du processeur global : %.2f GHz%n", averageFreq);
            sleep(1000);
        }
        }
}
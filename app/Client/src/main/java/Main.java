import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.software.os.OperatingSystem;

public class Main {
    public static void main(String[] args) {
        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();

        System.out.println("Nom de l'OS : " + osBean.getName());
        System.out.println("Version de l'OS : " + osBean.getVersion());
        System.out.println("Architecture de l'OS : " + osBean.getArch());

        // Pour Java 10+ pour obtenir la RAM totale disponible
        if (osBean instanceof com.sun.management.OperatingSystemMXBean) {
            com.sun.management.OperatingSystemMXBean sunOSBean = (com.sun.management.OperatingSystemMXBean) osBean;
            System.out.println("Mémoire physique totale : " + sunOSBean.getTotalPhysicalMemorySize() / (1024 * 1024) + " Mo");
            System.out.println("Mémoire physique libre : " + sunOSBean.getFreePhysicalMemorySize() / (1024 * 1024) + " Mo");
        }
        SystemInfo systemInfo = new SystemInfo();
        CentralProcessor processor = systemInfo.getHardware().getProcessor();
        GlobalMemory memory = systemInfo.getHardware().getMemory();
        OperatingSystem os = systemInfo.getOperatingSystem();

        String osFamily = os.getFamily(); // Famille de l'OS, ex. : "Windows"
        String osVersion = os.getVersionInfo().getVersion(); // Version de l'OS, ex. : "24H2"
        String buildNumber = os.getVersionInfo().getBuildNumber(); // Numéro de build

        System.out.println("Famille de l'OS : " + osFamily);
        System.out.println("Version de l'OS : " + osVersion);
        System.out.println("Numéro de build : " + buildNumber);

        System.out.println("Nombre de processeurs physiques (cœurs) : " + processor.getPhysicalProcessorCount());
        System.out.println("Processeur : " + processor.getProcessorIdentifier().getName());
        System.out.println("Nombre de cœurs : " + (processor.getLogicalProcessorCount() - processor.getPhysicalProcessorCount()));
        System.out.println("Mémoire totale : " + memory.getTotal() / (1024 * 1024)/1000 + " Go");
        try {
            Runtime.getRuntime().exec("powershell.exe -Command \"(New-Object -ComObject Microsoft.Update.AutoUpdate).DetectNow()\"");
            System.out.println("Mise à jour initiée.");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

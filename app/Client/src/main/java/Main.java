import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import oshi.SystemInfo;
import oshi.hardware.*;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;
import oshi.util.Util;

import javax.net.ssl.*;

import static java.lang.Thread.sleep;

public class Main {
    public static void main(String[] args) throws UnknownHostException, InterruptedException {
        String[] Info_hardware = new String[9];
        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();

        System.out.println("Nom de l'OS : " + osBean.getName());
        System.out.println("Version de l'OS : " + osBean.getVersion());
        System.out.println("Architecture de l'OS : " + osBean.getArch());

        // Pour Java 10+ pour obtenir la RAM totale disponible
        if (osBean instanceof com.sun.management.OperatingSystemMXBean) {
            com.sun.management.OperatingSystemMXBean sunOSBean = (com.sun.management.OperatingSystemMXBean) osBean;
            Info_hardware[7] = Long.toString(sunOSBean.getTotalPhysicalMemorySize() / (1024 * 1024));
        }
        SystemInfo systemInfo = new SystemInfo();

        CentralProcessor processor = systemInfo.getHardware().getProcessor();
        GlobalMemory memory = systemInfo.getHardware().getMemory();
        OperatingSystem os = systemInfo.getOperatingSystem();

        String osFamily = os.getFamily(); // Famille de l'OS, ex. : "Windows"
        String osVersion = os.getVersionInfo().getVersion(); // Version de l'OS, ex. : "24H2"
        String buildNumber = os.getVersionInfo().getBuildNumber(); // Numéro de build

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
        HardwareAbstractionLayer hal = systemInfo.getHardware();
        Sensors sensors = hal.getSensors();
        double temps = sensors.getCpuTemperature();

        System.out.println(temps);

        Integer c=0;
        Integer DisksSize = 0;
        for (HWDiskStore disk : hal.getDiskStores()) {
            System.out.println("Nom du disque : " + disk.getName());

            DisksSize += Math.toIntExact(Math.round(hal.getDiskStores().get(c).getSize() / 1e9));

            System.out.println("Espace disponible :" + (disk.getReadBytes() / 1e9));
            System.out.println("Statut SMART : " + disk.getTransferTime());

            // Les données S.M.A.R.T. incluent des attributs qui peuvent indiquer la durée de vie restante
            System.out.println("Attributs S.M.A.R.T. : " + disk.updateAttributes());

            c += 1;
        }
        Info_hardware[8] = ((int) DisksSize)+"";

        String computerName = System.getenv("COMPUTERNAME"); // Windows
        if (computerName == null) {
            computerName = System.getenv("HOSTNAME"); // Linux/Mac
        }

        // Vérification et affichage
        if (computerName != null) {
            System.out.println("Nom de l'ordinateur : " + computerName);
        }

        Info_hardware[0] = os.getFamily();
        Info_hardware[1] = os.getVersionInfo().getVersion();
        Info_hardware[2] = os.getVersionInfo().getBuildNumber();
        Info_hardware[3] = processor.getProcessorIdentifier().getName();
        Info_hardware[4] = Double.toString(processor.getMaxFreq() / 1_000_000_000.0);
        Info_hardware[5] = processor.getLogicalProcessorCount() + "";
        Info_hardware[6] = processor.getPhysicalProcessorCount() + "";

        String serverAddress = "sae51.madec.ovh"; // Adresse du serveur
        int port = 12345; // Port sécurisé

        try {
            // Initialiser le contexte SSL
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) { }
                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) { }
                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() { return null; }
                    }
            }, null);

            SSLSocketFactory socketFactory = sslContext.getSocketFactory();


            // Créer un SSLSocket
            SSLSocket socket = (SSLSocket) socketFactory.createSocket(serverAddress, port);

            // Flux de communication
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Afficher le message de bienvenue du serveur
            System.out.println("Serveur : " + in.readLine());


            // Envoyer des messages en continu
            Scanner scanner = new Scanner(System.in);
            String message;
            String messages;
            out.println("leffe");
            in.readLine();
            int clientId = ClientUtils.getClientId();
            if (clientId != -1) {
                System.out.println("Client ID récupéré : " + clientId);
                out.println("ID : " + clientId);
            } else {
                System.out.println("Aucun ID client trouvé, connexion en tant que nouveau client.");
                out.println("no ID");
                message = in.readLine();
                ClientUtils.saveClientId(Integer.parseInt(message.replace("Votre ID client est : ", "").trim()));
            }

            System.out.println(in.readLine());
            String Info = Arrays.toString(Info_hardware);
            out.println("Array : " + Info);
            while (true) {
                    message = in.readLine();
                    if (message != null) {
                        System.out.println("Serveur: " + message);
                        if ("action".equals(message)) {
                            String action = in.readLine();
                            System.out.println("Serveur: " + action);
                            if ("1".equals(action)) {
                                if (Info_hardware[0].equals("Windows")) {
                                    out.println("ok");
                                    Process process = Runtime.getRuntime().exec("cmd /c start powershell.exe -Command \"Install-WindowsUpdate -AcceptAll -AutoReboot\"");
                                    process.waitFor();
                                } else {
                                    out.println("ok");
                                    Process process = Runtime.getRuntime().exec("apt update && sudo apt upgrade -y");
                                    process.waitFor();
                                }
                            } else if ("2".equals(action)) {
                                if (Info_hardware[0].equals("Windows")) {
                                    out.println("ok");
                                    Process process = Runtime.getRuntime().exec("shutdown -r -t 0");
                                    process.waitFor();
                                } else {
                                    out.println("ok");
                                    Process process = Runtime.getRuntime().exec("reboot");
                                    process.waitFor();
                                }
                            } else if ("3".equals(action)) {
                                if (Info_hardware[0].equals("Windows")) {
                                    out.println("ok");
                                    Process process = Runtime.getRuntime().exec("shutdown -s -t 0");
                                    process.waitFor();
                                } else {
                                    out.println("ok");
                                    Process process = Runtime.getRuntime().exec("poweroff");
                                    process.waitFor();
                                }
                            } else if ("4".equals(action)) {
                                List<HWDiskStore> storage = systemInfo.getHardware().getDiskStores();
                                List<NetworkIF> network = systemInfo.getHardware().getNetworkIFs();


                                //données
                                CentralProcessor cpu = systemInfo.getHardware().getProcessor();
                                GlobalMemory ram = systemInfo.getHardware().getMemory();

                                double CPUUtilizationRaw = cpu.getSystemCpuLoad(400);
                                int CPUUtilization = (int) Math.ceil(CPUUtilizationRaw * 100);

                                int CPUTemp = (int) sensors.getCpuTemperature();
                                if(CPUTemp == 0){
                                    CPUTemp = -1;
                                }

                                int CPUConsumption = -1;

                                long RAMAvailable = ram.getAvailable();
                                long RAMTot = ram.getTotal();
                                long RAMUtilizationRaw = ((RAMTot - RAMAvailable) * 100) / RAMTot;
                                int RAMUtilization = (int) (RAMUtilizationRaw);

                                c=0;
                                String storageName = "";
                                String storageLoad = "";
                                String storageLeft = "";
                                String storageTemp = "";
                                String storageErrors = "";
                                String name = "";
                                List<OSFileStore> FS = systemInfo.getOperatingSystem().getFileSystem().getFileStores();
                                while(c < storage.size()){

                                    if(c > 0){
                                        storageName += "/";
                                        storageLoad += "/";
                                        storageLeft += "/";
                                        storageTemp += "/";
                                        storageErrors += "/";
                                    }

                                    HWDiskStore target = storage.get(c);

                                    name = storage.get(c).getName();
                                    String output = name.replace("/", "");
                                    storageName += output;

                                    //Estimation de la charge
                                    target.updateAttributes();
                                    long initTransferTime = target.getTransferTime();
                                    long initTimeStamp = target.getTimeStamp();
                                    Util.sleep(500);
                                    target.updateAttributes();
                                    long transferTime = target.getTransferTime();
                                    long timeStamp = target.getTimeStamp();
                                    double deltaTransferTime = transferTime - initTransferTime;
                                    double deltaTimeStamp = timeStamp - initTimeStamp;
                                    double utilization = Math.round((deltaTransferTime * 100.0) / deltaTimeStamp);
                                    if(utilization > 100){
                                        utilization = 100;
                                    }
                                    storageLoad += (int) utilization;

                                    storageLeft += (int) Math.sqrt(Math.pow(FS.get(c).getFreeSpace()/(1e8*8),2));

                                    storageTemp += -1;

                                    storageErrors += -1;

                                    c += 1;
                                }

                                c=0;
                                String networkName = "";
                                String networkLatency = "";
                                String networkBandwith = "";
                                while(c < network.size()){

                                    if(c > 0){
                                        networkName += "/";
                                        networkLatency += "/";
                                        networkBandwith += "/";
                                    }

                                    try{

                                    }
                                    catch(Exception e){

                                    }
                                    NetworkIF target2 = network.get(c);
                                    target2.updateAttributes();

                                    name = target2.getDisplayName();
                                    String output = name.replace("/", "");
                                    networkName += output;

                                    long timeElapsed = 0;
                                    try{
                                        InetAddress InetAddress = null;
                                        InetAddress address = InetAddress.getByName("google.fr");

                                        long start = System.currentTimeMillis();
                                        boolean reachable = address.isReachable(1000);
                                        long finish = System.currentTimeMillis();

                                        if(reachable){
                                            timeElapsed = finish - start;
                                        }
                                        else{
                                            timeElapsed = -1;
                                        }

                                    } catch (Exception e){
                                        e.printStackTrace();
                                    }
                                    networkLatency += timeElapsed;

                                    target2.updateAttributes();
                                    long initRx = target2.getBytesRecv();
                                    long initTx = target2.getBytesSent();
                                    Thread.sleep(200);
                                    target2.updateAttributes();
                                    long finalRx = target2.getBytesRecv();
                                    long finalTx = target2.getBytesSent();
                                    long speed = target2.getSpeed();
                                    double rxRate = 0;
                                    double txRate = 0;
                                    if (finalRx > initRx) {
                                        rxRate = (((double) (finalRx - initRx)) * 800) / (speed * 0.2);
                                    }
                                    if (finalTx > initTx) {
                                        txRate = (((double) (finalTx - initTx)) * 800) / (speed * 0.2);
                                    }

                                    double bandwidth = 0;
                                    if (rxRate > txRate) {
                                        bandwidth = rxRate;
                                    } else {
                                        bandwidth = txRate;
                                    }

                                    networkBandwith += (int) bandwidth;

                                    c += 1;
                                }

                                int[] fans = sensors.getFanSpeeds();
                                String fanSpeed = "-1";

                                c=0;
                                while(c < fans.length){
                                    if(c > 0){
                                        fanSpeed += "/";
                                    }

                                    fanSpeed += fans[c];

                                    c += 1;
                                }

                                String JSONString = "coucou:{"
                                        + "\"CPUUtilization\":\""+CPUUtilization+"\","
                                        + "\"CPUTemp\":\""+CPUTemp+"\","
                                        + "\"CPUConsumption\":\""+CPUConsumption+"\","
                                        + "\"RAMUtilization\":\""+RAMUtilization+"\","
                                        + "\"storageName\":\""+storageName+"\","
                                        + "\"storageLoad\":\""+storageLoad+"\","
                                        + "\"storageLeft\":\""+storageLeft+"\","
                                        + "\"storageTemp\":\""+storageTemp+"\","
                                        + "\"storageErrors\":\""+storageErrors+"\","
                                        + "\"networkName\":\""+networkName+"\","
                                        + "\"networkLatency\":\""+networkLatency+"\","
                                        + "\"networkBandwith\":\""+networkBandwith+"\","
                                        + "\"fanSpeed\":\""+fanSpeed+"\""
                                        + "}";

                                JSONString = JSONString.replaceAll("\\\\", "")
                                        .replaceAll("\\.", "");

                                System.out.println("JSON : "+JSONString);
                                out.println(JSONString);

                                out.println("keep alive");
                            }
                        }
                    }


                sleep(500);
            }
        } catch (Exception e) {
            System.err.println("Erreur côté client : " + e.getMessage());
            e.printStackTrace();
        }
        }

    public class ClientUtils {
        private static final String FILE_NAME = "/save/client_id.txt";

        // Sauvegarde l'ID dans un fichier
        public static void saveClientId(int clientId) {
            File file = new File(FILE_NAME);
            if (!file.exists()) {
                try {
                    if (file.createNewFile()) {
                        System.out.println("Fichier client_id.txt créé.");
                    }
                } catch (IOException e) {
                    System.err.println("Erreur lors de la création du fichier : " + e.getMessage());
                }
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
                writer.write(String.valueOf(clientId));
                System.out.println("ID client sauvegardé : " + clientId);
            } catch (IOException e) {
                System.err.println("Erreur lors de la sauvegarde de l'ID client : " + e.getMessage());
            }
        }

        // Récupère l'ID depuis le fichier
        public static int getClientId() {
            try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
                return Integer.parseInt(reader.readLine());
            } catch (IOException | NumberFormatException e) {
                System.err.println("Impossible de récupérer l'ID client, fichier inexistant ou corrompu.");
                return -1; // Retourne -1 si aucun ID n'est trouvé
            }
        }
    }

}

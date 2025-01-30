import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Scanner;

import oshi.SystemInfo;
import oshi.hardware.*;
import oshi.software.os.OperatingSystem;

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
        for (HWDiskStore disk : hal.getDiskStores()) {
            System.out.println("Nom du disque : " + disk.getName());
            Info_hardware[8] = Double.toString(disk.getSize() / 1e9);
            System.out.println("Espace disponible :" + (disk.getReadBytes() / 1e9));
            System.out.println("Statut SMART : " + disk.getTransferTime());

            // Les données S.M.A.R.T. incluent des attributs qui peuvent indiquer la durée de vie restante
            System.out.println("Attributs S.M.A.R.T. : " + disk.updateAttributes());
        }

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

        String serverAddress = "127.0.0.1"; // Adresse du serveur
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
                                    Process process = Runtime.getRuntime().exec("cmd /c start powershell.exe -Command \"Install-WindowsUpdate -AcceptAll -AutoReboot\"");
                                    process.waitFor();
                                } else {
                                    Process process = Runtime.getRuntime().exec("apt update && sudo apt upgrade -y");
                                    process.waitFor();
                                }
                            } else if ("2".equals(action)) {
                                if (Info_hardware[0].equals("Windows")) {
                                    Process process = Runtime.getRuntime().exec("shutdown -r -t 0");
                                    process.waitFor();
                                } else {
                                    Process process = Runtime.getRuntime().exec("reboot");
                                    process.waitFor();
                                }
                            } else if ("3".equals(action)) {
                                if (Info_hardware[0].equals("Windows")) {
                                    Process process = Runtime.getRuntime().exec("shutdown -s -t 0");
                                    process.waitFor();
                                } else {
                                    Process process = Runtime.getRuntime().exec("poweroff");
                                    process.waitFor();
                                }
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

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

        String serverAddress = "localhost"; // Adresse du serveur
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

            System.out.println("Connexion sécurisée établie avec le serveur.");

            // Flux de communication
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Afficher le message de bienvenue du serveur
            System.out.println("Serveur : " + in.readLine());
            System.out.println(in.readLine());


            // Envoyer des messages en continu
            Scanner scanner = new Scanner(System.in);
            String message;
            while (true) {
                out.println("password");
                System.out.println(in.readLine());
                System.out.println("Vous : ");
                System.out.println("1");
                message = scanner.nextLine(); // Lire un message depuis le clavier
                System.out.println("5");
                out.print(message);
                System.out.println("7");
                if (in.ready()) {  // Vérifie si le serveur a envoyé un message
                    System.out.println("8");
                    message = in.readLine();
                    System.out.println("9");
                    System.out.println("Serveur: " + message);
                    if (message.equals("action")) {
                        message = in.readLine();
                        System.out.println("Serveur: " + message);
                        if (message.equals("1")) {
                            System.out.println("caca");
                        } else if (message.equals("2")) {
                            System.out.println("ca");
                        } else if (message.equals("3")) {
                            System.out.println("cacaca");
                        }
                    }

                }
                System.out.println("3");
                message = in.readLine();
                System.out.println(message);
                System.out.println("test");
                sleep(500);

                // Si le message est "exit", quitter la boucle
                if ("exit".equalsIgnoreCase(message)) {
                    break;
                }
            }

            System.out.println("Connexion terminée.");
        } catch (Exception e) {
            System.err.println("Erreur côté client : " + e.getMessage());
            e.printStackTrace();
        }

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

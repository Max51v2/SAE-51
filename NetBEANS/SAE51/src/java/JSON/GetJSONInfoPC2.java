package JSON;

/**
 * Permet de récupérer les informations contenues dans un JSON contenant les infos d'un pc (exemple utilisation : voir req POST servlet / TokenExpiration)
 * 
 * @author Maxime VALLET
 * @version 0.1
 */
public class GetJSONInfoPC2{
        private Integer CPUUtilization;
        private Integer CPUTemp;
        private Integer CPUConsumption;
        private Integer RAMUtilization;
        private String storageName;
        private String storageLoad;
        private String storageLeft;
        private String storageTemp;
        private String storageErrors;
        private String networkName;
        private String networkLatency;
        private String networkBandwith;
        private String fanSpeed;
        
        public GetJSONInfoPC2(Integer CPUUtilization, Integer CPUTemp, Integer CPUConsumption, Integer RAMUtilization, String storageName, String storageLoad, 
                String storageLeft, String storageTemp, String storageErrors, String networkName, String networkLatency, String networkBandwith, String fanSpeed){
            this.CPUUtilization = CPUUtilization;
            this.CPUTemp = CPUTemp;
            this.CPUConsumption = CPUConsumption;
            this.RAMUtilization = RAMUtilization;
            this.storageLoad = storageLoad;
            this.storageLeft = storageLeft;
            this.storageTemp = storageTemp;
            this.storageErrors = storageErrors;
            this.networkLatency = networkLatency;
            this.networkBandwith = networkBandwith;
            this.fanSpeed = fanSpeed;
        } 

    public Integer getCPUUtilization() {
        return CPUUtilization;
    }

    public void setCPUUtilization(Integer CPUUtilization) {
        this.CPUUtilization = CPUUtilization;
    }

    public Integer getCPUTemp() {
        return CPUTemp;
    }

    public void setCPUTemp(Integer CPUTemp) {
        this.CPUTemp = CPUTemp;
    }

    public Integer getCPUConsumption() {
        return CPUConsumption;
    }

    public void setCPUConsumption(Integer CPUConsumption) {
        this.CPUConsumption = CPUConsumption;
    }

    public Integer getRAMUtilization() {
        return RAMUtilization;
    }

    public void setRAMUtilization(Integer RAMUtilization) {
        this.RAMUtilization = RAMUtilization;
    }

    public String getStorageName() {
        return storageName;
    }

    public void setStorageName(String storageName) {
        this.storageName = storageName;
    }

    public String getStorageLoad() {
        return storageLoad;
    }

    public void setStorageLoad(String storageLoad) {
        this.storageLoad = storageLoad;
    }

    public String getStorageLeft() {
        return storageLeft;
    }

    public void setStorageLeft(String storageLeft) {
        this.storageLeft = storageLeft;
    }

    public String getStorageTemp() {
        return storageTemp;
    }

    public void setStorageTemp(String storageTemp) {
        this.storageTemp = storageTemp;
    }

    public String getStorageErrors() {
        return storageErrors;
    }

    public void setStorageErrors(String storageErrors) {
        this.storageErrors = storageErrors;
    }

    public String getNetworkName() {
        return networkName;
    }

    public void setNetworkName(String networkName) {
        this.networkName = networkName;
    }

    public String getNetworkLatency() {
        return networkLatency;
    }

    public void setNetworkLatency(String networkLatency) {
        this.networkLatency = networkLatency;
    }

    public String getNetworkBandwith() {
        return networkBandwith;
    }

    public void setNetworkBandwith(String networkBandwith) {
        this.networkBandwith = networkBandwith;
    }

    public String getFanSpeed() {
        return fanSpeed;
    }

    public void setFanSpeed(String fanSpeed) {
        this.fanSpeed = fanSpeed;
    }
    }
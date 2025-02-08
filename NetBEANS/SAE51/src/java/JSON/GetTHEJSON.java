package JSON;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * ENORME
 * 
 * @author Maxime VALLET
 * @version 1.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)  // Ignore les propriétés inconnues*
public class GetTHEJSON {

    @JsonProperty("test")
    private String test;

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("IP")
    private String IP;

    @JsonProperty("token")
    private String token;

    @JsonProperty("hasAccess")
    private String hasAccess;

    @JsonProperty("login")
    private String login;

    @JsonProperty("action")
    private Integer action;

    @JsonProperty("message")
    private String message;

    @JsonProperty("date")
    private String date;

    @JsonProperty("time")
    private String time;

    @JsonProperty("CPUUtilization")
    private Integer CPUUtilization;

    @JsonProperty("CPUTemp")
    private Integer CPUTemp;

    @JsonProperty("CPUConsumption")
    private Integer CPUConsumption;

    @JsonProperty("RAMUtilization")
    private Integer RAMUtilization;

    @JsonProperty("storageLoad")
    private Integer storageLoad;

    @JsonProperty("storageLeft")
    private Integer storageLeft;

    @JsonProperty("storageTemp")
    private Integer storageTemp;

    @JsonProperty("storageErrors")
    private Integer storageErrors;

    @JsonProperty("networkLatency")
    private Integer networkLatency;

    @JsonProperty("networkBandwith")
    private Integer networkBandwith;

    @JsonProperty("fanSpeed")
    private Integer fanSpeed;

    @JsonProperty("storageNameStr")
    private String storageNameStr;

    @JsonProperty("storageLoadStr")
    private String storageLoadStr;

    @JsonProperty("storageLeftStr")
    private String storageLeftStr;

    @JsonProperty("storageTempStr")
    private String storageTempStr;

    @JsonProperty("storageErrorsStr")
    private String storageErrorsStr;

    @JsonProperty("networkName")
    private String networkName;

    @JsonProperty("networkLatencyStr")
    private String networkLatencyStr;

    @JsonProperty("networkBandwithStr")
    private String networkBandwithStr;

    @JsonProperty("fanSpeedStr")
    private String fanSpeedStr;

    @JsonProperty("nom")
    private String nom;

    @JsonProperty("prenom")
    private String prenom;

    @JsonProperty("droits")
    private String droits;

    @JsonProperty("password")
    private String password;

    @JsonProperty("currentPage")
    private String currentPage;

    @JsonProperty("target")
    private String target;

    @JsonProperty("erreur")
    private String erreur;

    @JsonProperty("beginDate")
    private String beginDate;

    @JsonProperty("endDate")
    private String endDate;

    @JsonProperty("logLevelReq")
    private String logLevelReq;

    public GetTHEJSON(){
        
    }
            
    // Constructeur avec tous les champs
    public GetTHEJSON(String test, Integer id, String IP, String token, String hasAccess, String login, 
            Integer action, String message, String date, String time, Integer CPUUtilization, Integer CPUTemp, 
            Integer CPUConsumption, Integer RAMUtilization, Integer storageLoad, Integer storageLeft, Integer storageTemp,
            Integer storageErrors, Integer networkLatency, Integer networkBandwith, Integer fanSpeed, String storageNameStr,
            String storageLoadStr, String storageLeftStr, String storageTempStr, String storageErrorsStr, String networkName,
            String networkLatencyStr, String networkBandwithStr, String fanSpeedStr, String nom, String prenom, String droits,
            String password, String currentPage, String target, String erreur, String beginDate, String endDate, String logLevelReq) {
        
        this.test = test;
        this.id = id;
        this.IP = IP;
        this.token = token;
        this.hasAccess = hasAccess;
        this.login = login;
        this.action = action;
        this.message = message;
        this.date = date;
        this.time = time;
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
        this.storageNameStr = storageNameStr;
        this.storageLoadStr = storageLoadStr;
        this.storageLeftStr = storageLeftStr;
        this.storageTempStr = storageTempStr;
        this.storageErrorsStr = storageErrorsStr;
        this.networkName = networkName;
        this.networkLatencyStr = networkLatencyStr;
        this.networkBandwithStr = networkBandwithStr;
        this.fanSpeedStr = fanSpeedStr;
        this.nom = nom;
        this.prenom = prenom;
        this.droits = droits;
        this.password = password;
        this.currentPage = currentPage;
        this.target = target;
        this.erreur = erreur;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.logLevelReq = logLevelReq;
    }

    public String getTest() {
        return test;
    }

    public Integer getId() {
        return id;
    }

    public String getIP() {
        return IP;
    }

    public String getToken() {
        return token;
    }

    public String getHasAccess() {
        return hasAccess;
    }

    public String getLogin() {
        return login;
    }

    public Integer getAction() {
        return action;
    }

    public String getMessage() {
        return message;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public Integer getCPUUtilization() {
        return CPUUtilization;
    }

    public Integer getCPUTemp() {
        return CPUTemp;
    }

    public Integer getCPUConsumption() {
        return CPUConsumption;
    }

    public Integer getRAMUtilization() {
        return RAMUtilization;
    }

    public Integer getStorageLoad() {
        return storageLoad;
    }

    public Integer getStorageLeft() {
        return storageLeft;
    }

    public Integer getStorageTemp() {
        return storageTemp;
    }

    public Integer getStorageErrors() {
        return storageErrors;
    }

    public Integer getNetworkLatency() {
        return networkLatency;
    }

    public Integer getNetworkBandwith() {
        return networkBandwith;
    }

    public Integer getFanSpeed() {
        return fanSpeed;
    }

    public String getStorageNameStr() {
        return storageNameStr;
    }

    public String getStorageLoadStr() {
        return storageLoadStr;
    }

    public String getStorageLeftStr() {
        return storageLeftStr;
    }

    public String getStorageTempStr() {
        return storageTempStr;
    }

    public String getStorageErrorsStr() {
        return storageErrorsStr;
    }

    public String getNetworkName() {
        return networkName;
    }

    public String getNetworkLatencyStr() {
        return networkLatencyStr;
    }

    public String getNetworkBandwithStr() {
        return networkBandwithStr;
    }

    public String getFanSpeedStr() {
        return fanSpeedStr;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getDroits() {
        return droits;
    }

    public String getPassword() {
        return password;
    }

    public String getCurrentPage() {
        return currentPage;
    }

    public String getTarget() {
        return target;
    }

    public String getErreur() {
        return erreur;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getLogLevelReq() {
        return logLevelReq;
    }
}
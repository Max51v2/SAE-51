package JSON;

/**
 * Permet de récupérer les informations contenues dans un JSON contenant les infos d'un pc (exemple utilisation : voir req POST servlet / TokenExpiration)
 * @author Maxime VALLET
 */
public class GetJSONInfoPC{
        private String Test;
        private String id;
        private String IP;
        
        public GetJSONInfoPC(String Test, String IP, String id){
            this.Test = Test;
            this.IP = IP;
            this.id = id;
        }

    public String getTest() {
        return Test;
    }

    public void setTest(String Test) {
        this.Test = Test;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }
        
    }
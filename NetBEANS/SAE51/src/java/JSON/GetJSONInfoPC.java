package JSON;

/**
 * Permet de récupérer les informations contenues dans un JSON contenant les infos d'un pc (exemple utilisation : voir req POST servlet / TokenExpiration)
 * 
 * @author Maxime VALLET
 * @version 0.1
 */
public class GetJSONInfoPC{
        private String Test;
        private Integer id;
        private String IP;
        private String token;
        
        public GetJSONInfoPC(String Test, String IP, Integer id, String token){
            this.Test = Test;
            this.IP = IP;
            this.id = id;
            this.token = token;
        }

        public String getTest() {
            return Test;
        }

        public void setTest(String Test) {
            this.Test = Test;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getIP() {
            return IP;
        }

        public void setIP(String IP) {
            this.IP = IP;
        }
        
        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
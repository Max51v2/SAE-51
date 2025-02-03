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
        private String hasAccess;
        private String login;
        private Integer action;
        private String message;
        private String date;
        private String time;
        
        public GetJSONInfoPC(String Test, String IP, Integer id, String hasAccess, String login, Integer action, String message, String date, String time, String token){
            this.Test = Test;
            this.IP = IP;
            this.id = id;
            this.token = token;
            this.hasAccess = hasAccess;
            this.login = login;
            this.action = action;
            this.message = message;
            this.date = date;
            this.time = time;
        }

        public String getTest() {
            return Test;
        }

        public void setTest(String Test) {
            this.Test = Test;
        }
        
        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
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
        
        public String getHasAccess() {
            return hasAccess;
        }

        public void setHasAccess(String hasAccess) {
            this.hasAccess = hasAccess;
        }
        
        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }
        
        public Integer getAction() {
            return action;
        }

        public void setAction(Integer action) {
            this.action = action;
        }
        
        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
        
        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
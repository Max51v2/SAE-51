package JSON;

/**
 * Permet de récupérer les informations contenues dans un JSON (exemple utilisation : voir req POST servlet / TokenExpiration)
 * 
 * @author Maxime VALLET
 * @version 1.0
 */
public class GetJSONInfoUsers{
        private String login;
        private String nom;
        private String prenom;
        private String droits;
        private String token;
        private String password;
        private String currentPage;
        private String Test;
        private String id;
        private String IP;
        
        public GetJSONInfoUsers(String login, String nom, String prenom, String droits, String token, String password, String currentPage, String Test, String IP, String id){
            this.login=login;
            this.nom=nom;
            this.prenom=prenom;
            this.droits=droits;
            this.token=token;
            this.password=password;
            this.currentPage=currentPage;
            this.Test = Test;
            this.IP = IP;
            this.id = id;
        }

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public String getNom() {
            return nom;
        }

        public void setNom(String nom) {
            this.nom = nom;
        }

        public String getPrenom() {
            return prenom;
        }

        public void setPrenom(String prenom) {
            this.prenom = prenom;
        }

        public String getDroits() {
            return droits;
        }

        public void setDroits(String droits) {
            this.droits = droits;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
        
        public String getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(String currentPage) {
            this.currentPage = currentPage;
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

package Autre;

/**
 *
 * @author Maxime VALLET
 */
public class GetJSONInfo{
        private String login;
        private String nom;
        private String prenom;
        private String droits;
        private String token;
        private String password;
        private String Test;
        
        public GetJSONInfo(String login, String nom, String prenom, String droits, String token, String password, String Test){
            this.login=login;
            this.nom=nom;
            this.prenom=prenom;
            this.droits=droits;
            this.token=token;
            this.password=password;
            this.Test = Test;
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

        public String getTest() {
            return Test;
        }

        public void setTest(String Test) {
            this.Test = Test;
        }
        
        
    }
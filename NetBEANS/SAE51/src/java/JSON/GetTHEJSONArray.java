package JSON;

import java.util.List;

/**
 * Liste d'objets GetJSONInfo (utilisé pour récupérer la liste des utilisateurs à partir d'un JSON fournis par getUsers) cf. TokenExpiration
 * 
 * @author Maxime VALLET
 * @version 1.0
 */
public class GetTHEJSONArray {
    private List<GetTHEJSON> json;

    public List<GetTHEJSON> UsersArray() {
        return json;
    }

    public void setUsers(List<GetTHEJSON> json) {
        this.json = json;
    }
}

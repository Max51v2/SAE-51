package JSON;

import java.util.List;

/**
 * Liste d'objets GetJSONInfo (utilisé pour récupérer la liste des utilisateurs à partir d'un JSON fournis par getUsers) cf. TokenExpiration
 * 
 * @author Maxime VALLET
 * @version 1.0
 */
public class UsersArray {
    private List<GetJSONInfoUsers> users;

    public List<GetJSONInfoUsers> UsersArray() {
        return users;
    }

    public void setUsers(List<GetJSONInfoUsers> users) {
        this.users = users;
    }
}

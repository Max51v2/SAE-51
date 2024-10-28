/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package JSON;

import JSON.GetJSONInfoUsers;
import java.util.List;

/**
 * Liste d'objets GetJSONInfo (utilisé pour récupérer la liste des utilisateurs à partir d'un JSON fournis par getUsers) cf. TokenExpiration
 * @author Maxime VALLET
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

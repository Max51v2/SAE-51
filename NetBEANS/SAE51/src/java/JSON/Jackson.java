package JSON;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Maxime VALLET
 * @version 1.0
 */
public class Jackson {
    
    public GetTHEJSON GetServletJSON(HttpServletRequest request) throws IOException {
    BufferedReader reader = request.getReader();
    StringBuilder json = new StringBuilder();
    String line;
    
    while ((line = reader.readLine()) != null) {
        json.append(line);
    }
    
    if (json.length() == 0) {
        throw new IOException("Le corps de la requête est vide !");
    }

    ObjectMapper mapper = new ObjectMapper();
    return mapper.readValue(json.toString(), GetTHEJSON.class);
}

    
    
    public GetTHEJSON GetClientJSON(String str) throws JsonProcessingException{
        // Récupération du JSON envoyé
        String JSONString = str.substring(str.indexOf(":") + 1);

        // Utilisation de Jackson pour convertir la chaîne JSON en objet Java
        ObjectMapper objectMapper = new ObjectMapper();
        GetTHEJSON json = objectMapper.readValue(JSONString, GetTHEJSON.class);

        return json;
    }
    
    
    public GetTHEJSON GetStringJSON(String str) throws JsonProcessingException{

        // Utilisation de Jackson pour convertir la chaîne JSON en objet Java
        ObjectMapper objectMapper = new ObjectMapper();
        JSON.GetTHEJSON json = objectMapper.readValue(str, JSON.GetTHEJSON.class);

        return json;
    }
}

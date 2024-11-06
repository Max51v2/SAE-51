package Tests;

import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class POSTRequest {

    public String doRequest(String servletAddress, String jsonPayload) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(servletAddress);

            // Définir le corps de la requête comme JSON
            StringEntity entity = new StringEntity(jsonPayload);
            httpPost.setEntity(entity);

            // Définir le type de contenu comme JSON
            httpPost.setHeader("Content-Type", "application/json");

            // Exécution de la requête et récupération de la réponse
            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                HttpEntity responseEntity = response.getEntity();
                return responseEntity != null ? EntityUtils.toString(responseEntity) : null;
            }
        }
    }
}

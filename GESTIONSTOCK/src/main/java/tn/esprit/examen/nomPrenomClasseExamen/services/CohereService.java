package tn.esprit.examen.nomPrenomClasseExamen.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import org.json.JSONArray;
import org.json.JSONObject;

@Service
public class CohereService {

    @Value("${cohere.api.key}")
    private String cohereApiKey;

    private static final String API_URL = "https://api.cohere.ai/v1/generate";

    public String getChatbotResponse(String userMessage) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + cohereApiKey);
            headers.setContentType(MediaType.APPLICATION_JSON);

            JSONObject body = new JSONObject();
            body.put("model", "command"); // modèle stable, évite "xlarge"
            body.put("prompt", userMessage);
            body.put("max_tokens", 100);
            body.put("temperature", 0.9);

            HttpEntity<String> entity = new HttpEntity<>(body.toString(), headers);

            ResponseEntity<String> response = restTemplate.exchange(API_URL, HttpMethod.POST, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                JSONObject jsonResponse = new JSONObject(response.getBody());
                JSONArray generations = jsonResponse.getJSONArray("generations");
                return generations.getJSONObject(0).getString("text").trim();
            }

            return "Erreur : réponse non valide";
        } catch (Exception e) {
            return "Erreur lors du traitement de la réponse : " + e.getMessage();
        }
    }
}

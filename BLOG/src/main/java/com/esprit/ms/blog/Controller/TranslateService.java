package com.esprit.ms.blog.Controller;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class TranslateService {

    private static final String APERTIUM_URL = "https://apertium.org/apy/translate?langpair=%s|%s&q=%s";

    public String translateText(String text, String sourceLang, String targetLang) {
        try {
            String encodedText = java.net.URLEncoder.encode(text, "UTF-8");
            String url = String.format(APERTIUM_URL, sourceLang, targetLang, encodedText);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                String responseBody = response.getBody();
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(responseBody);

                JsonNode translatedTextNode = rootNode.path("responseData").path("translatedText");

                if (!translatedTextNode.isMissingNode()) {
                    return translatedTextNode.asText();
                } else {
                    throw new RuntimeException("Translated text not found in the response.");
                }
            } else {
                throw new RuntimeException("Error translating text: " + response.getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Translation failed: " + e.getMessage());
        }
    }
}

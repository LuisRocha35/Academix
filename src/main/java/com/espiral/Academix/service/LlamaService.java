package com.espiral.Academix.service;

import com.espiral.Academix.interfaces.AiService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class LlamaService implements AiService {

    private final RestTemplate restTemplate;
    private final String OLLAMA_URL = "http://localhost:11434/api/generate";

    public LlamaService() {
        this.restTemplate = new RestTemplate();
    }

    @Override
    public String generateContent(String prompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "llama3.2");
        requestBody.put("prompt", prompt);
        requestBody.put("stream", false);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try {
            Map response = restTemplate.postForObject(OLLAMA_URL, request, Map.class);
            if (response != null && response.containsKey("response")) {
                return response.get("response").toString();
            }
            return "Erro: Resposta vazia do modelo local.";
        } catch (Exception e) {
            return "Erro na comunicação com o Ollama: " + e.getMessage();
        }
    }
}
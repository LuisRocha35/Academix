package com.espiral.Academix.service;

import com.espiral.Academix.interfaces.AiGenerator;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

@Service
public class LlamaService implements AiGenerator {

    private final RestTemplate restTemplate;
    private final Dotenv dotenv;
    private final String ollamaUrl;

    public LlamaService(Dotenv dotenv) {
        this.dotenv = dotenv;
        this.ollamaUrl = dotenv.get("OLLAMA_API_URL", "http://localhost:11434/api/generate");

        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000);
        factory.setReadTimeout(300000);
        this.restTemplate = new RestTemplate(factory);
    }

    @Override
    public String generateContent(String systemPrompt, String userPrompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("ngrok-skip-browser-warning", "true");

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "llama3.2");
        requestBody.put("system", systemPrompt);
        requestBody.put("prompt", userPrompt);
        requestBody.put("stream", false);
        requestBody.put("format", "json");

        Map<String, Object> options = new HashMap<>();
        options.put("temperature", 0.1);
        options.put("num_ctx", 8192);
        requestBody.put("options", options);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try {
            String jsonResponse = restTemplate.postForObject(this.ollamaUrl, request, String.class);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(jsonResponse);

            if (root != null && root.has("response")) {
                return root.get("response").asText();
            }
            return "Erro: Resposta vazia do modelo local.";
        } catch (Exception e) {
            return "Erro na comunicação com o Ollama: " + e.getMessage();
        }
    }
}
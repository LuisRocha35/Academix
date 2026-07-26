package com.espiral.Academix.service;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;

import io.github.cdimascio.dotenv.Dotenv;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GeminiService {

    //Teste
    private final Dotenv dotenv;

    public GeminiService(Dotenv dotenv) {
        this.dotenv = dotenv;
    }

    public void teste() {
        String apiKey = dotenv.get("GEMINI_API_KEY");
        System.out.println(apiKey);
    }

    /*
     * @Value("${gemini.api.key}")
     * private String apiKey;
     */

    /*
     * public String generateContent(String prompt) {
     * Client client = Client.builder()
     * .apiKey(apiKey)
     * .build();
     * 
     * GenerateContentResponse response = client.models.generateContent(
     * "gemini-2.0-flash-exp",
     * prompt,
     * null
     * );
     * 
     * return response.text();
     * }
     */
}
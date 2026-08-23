package com.espiral.Academix.controller;

import com.espiral.Academix.service.CardService;
import com.espiral.Academix.interfaces.AiGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("/api/pdf")
public class ApiController {

    @Autowired
    private CardService cardService;

    @PostMapping("/extract")
    public ResponseEntity<String> testExtraction(@RequestParam("file") MultipartFile file) {
        try {
            String extractedText = cardService.extractTextFromPdf(file);
            return ResponseEntity.ok(extractedText);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao processar o arquivo: " + e.getMessage());
        }
    }

    @Autowired
    private AiGenerator aiGenerator;

    @GetMapping("/test-ia")
    public ResponseEntity<String> testarIA() {
        try {
            // Passando a persona (system) e a pergunta (user) para respeitar os 2 argumentos
            String resposta = aiGenerator.generateContent(
                    "Você é um assistente direto e objetivo.",
                    "Responda apenas com a frase exata: 'quantas copa do mundo o Brasil ja venceu'"
            );
            return ResponseEntity.ok("Resposta da IA: " + resposta);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao conectar com a IA: " + e.getMessage());
        }
    }

    @PostMapping(value = "/gerar-Cards", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> gerarFlashcards(@RequestParam("file") MultipartFile file) {
        try {
            // Chama o método síncrono que espera a IA terminar
            String respostaIA = cardService.gerarCardsDoPdf(file);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(respostaIA);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao processar o arquivo: " + e.getMessage());
        }
    }
}
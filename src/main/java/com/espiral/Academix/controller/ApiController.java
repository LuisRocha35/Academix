package com.espiral.Academix.controller;

import com.espiral.Academix.service.CardService;
//import com.espiral.Academix.service.GeminiService;
import com.espiral.Academix.service.PdfService;
import com.espiral.Academix.interfaces.AiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    private AiService aiService;

    @GetMapping("/test-ia")
    public ResponseEntity<String> testarIA() {
        try {
            String resposta = aiService.generateContent("Responda apenas com a frase exata: 'quantas copa do mundo o Brasil ja venceu'");
            return ResponseEntity.ok("Resposta da IA: " + resposta);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao conectar com a IA: " + e.getMessage());
        }
    }

    @PostMapping("/gerar-Cards")
    public ResponseEntity<String> gerarFlashcards(@RequestParam("file") MultipartFile file) {
        try {
            String respostaIA = cardService.gerarCardsDoPdf(file);
            return ResponseEntity.ok(respostaIA);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao processar o arquivo: " + e.getMessage());
        }
    }
}
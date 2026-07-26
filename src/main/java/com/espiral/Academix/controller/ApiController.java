package com.espiral.Academix.controller;

import com.espiral.Academix.service.CardService;
import com.espiral.Academix.service.GeminiService;
import com.espiral.Academix.service.PdfService;
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

    private GeminiService geminiService;

    @GetMapping("/testgemini")
    public ResponseEntity<String> testarGemini() {
        try {
            String prompt = "Diga apenas: 'Chave funcionando!' em português.";
            String resposta = geminiService.generateContent(prompt);
            return ResponseEntity.ok("✅ Resposta da IA: " + resposta);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("❌ Erro: " + e.getMessage());
        }
    }
}
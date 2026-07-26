package com.espiral.Academix.controller;

import com.espiral.Academix.service.CardService;
import com.espiral.Academix.service.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/pdf")
public class PdfController {

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
}
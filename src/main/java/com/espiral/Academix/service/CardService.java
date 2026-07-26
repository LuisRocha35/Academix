package com.espiral.Academix.service;

import java.io.IOException;

import com.espiral.Academix.interfaces.AiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CardService {
    
    @Autowired
    private PdfService pdfService;

    @Autowired
    private AiService aiService;

    public String extractTextFromPdf(MultipartFile file) throws IOException{
        String extractedText = pdfService.extractTextFromPdf(file);

        return extractedText;
    }

    public String gerarCardsDoPdf(MultipartFile file) throws IOException {
        String textoExtraido = extractTextFromPdf(file);
        String prompt = "Crie flashcards de estudo: " + textoExtraido;

        return aiService.generateContent(prompt);
    }
}

package com.espiral.Academix.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CardService {
    
    @Autowired
    private PdfService pdfService;

    public String extractTextFromPdf(MultipartFile file) throws IOException{
        String extractedText = pdfService.extractTextFromPdf(file);

        
        
        return extractedText;
    }
}

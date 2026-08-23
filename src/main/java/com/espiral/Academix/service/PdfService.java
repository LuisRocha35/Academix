package com.espiral.Academix.service;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.stream.Collectors;

@Service
public class PdfService {

    /** Método responsavel por receber um arquivo PDF e extrair seu texto bruto. **/
    public String extractTextFromPdf(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("O arquivo enviado está vazio!");
        }

        try (PDDocument document = Loader.loadPDF(file.getBytes())) {
            if (document.isEncrypted()) {
                throw new IOException("O document está criptografado e não pode ser lido!");
            }

            PDFTextStripper extractor = new PDFTextStripper();
            extractor.setSortByPosition(true);

            String rawText = extractor.getText(document);
            rawText = rawText.replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "");
            rawText = rawText.replaceAll("[ \\t\\x0B\\f]+", " ");

            String cleanText = rawText.lines()
                    .filter(line -> !line.trim().isEmpty())
                    .collect(Collectors.joining("\n"));

            return cleanText;
        }
    }
    }
package com.espiral.Academix.service;

import java.io.IOException;
import com.espiral.Academix.interfaces.AiGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CardService {

    @Autowired
    private PdfService pdfService;

    @Autowired
    private AiGenerator aiGenerator;

    public String extractTextFromPdf(MultipartFile file) throws IOException {
        return pdfService.extractTextFromPdf(file);
    }

    // Método único e direto que processa e espera o resultado da IA
    public String gerarCardsDoPdf(MultipartFile file) throws IOException {
        String textoExtraido = extractTextFromPdf(file);

        int limiteCaracteres = 50000;
        if (textoExtraido.length() > limiteCaracteres) {
            throw new IllegalArgumentException("O texto extraído possui " + textoExtraido.length() +
                    " caracteres, ultrapassando o limite seguro de " + limiteCaracteres + " para a IA local.");
        }

        String systemPrompt =
                "Você é um tutor educacional. Seu objetivo é criar flashcards precisos baseados no texto fornecido.\n" +
                        "### REGRAS RIGOROSAS ###\n" +
                        "1. SAÍDA OBRIGATÓRIA: Responda EXCLUSIVAMENTE em formato JSON válido, sem nenhum texto adicional fora dele.\n" +
                        "2. ESTRUTURA OBRIGATÓRIA: O JSON deve conter um array chamado 'flashcards' onde cada item possui exatamente as chaves 'question' e 'answer'.\n" +
                        "Exemplo:\n" +
                        "{\n" +
                        "  \"flashcards\": [\n" +
                        "    {\n" +
                        "      \"question\": \"Texto da pergunta?\",\n" +
                        "      \"answer\": \"Texto da resposta.\"\n" +
                        "    }\n" +
                        "  ]\n" +
                        "}\n" +
                        "3. QUANTIDADE: Crie entre 5 e 10 flashcards baseados no texto.";

        String userPrompt = "Texto base para extração:\n\n" + textoExtraido;

        return aiGenerator.generateContent(systemPrompt, userPrompt);
    }
}
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

    public String extractTextFromPdf(MultipartFile file) throws IOException{
        String extractedText = pdfService.extractTextFromPdf(file);

        return extractedText;
    }

    public String gerarCardsDoPdf(MultipartFile file) throws IOException {
        String textoExtraido = extractTextFromPdf(file);

        int limiteCaracteres = 50000;
        if (textoExtraido.length() > limiteCaracteres) {
            throw new IllegalArgumentException("O texto extraído possui " + textoExtraido.length() +
                    " caracteres, ultrapassando o limite seguro de " + limiteCaracteres + " para a IA local.");
        }

        String prompt =
                "Você é um tutor educacional especialista em metodologias de aprendizagem ativa.\n" +
                        "Seu objetivo é criar flashcards de estudo precisos e úteis, baseados EXCLUSIVAMENTE no texto fornecido dentro das tags <TEXTO_BASE>.\n\n" +
                        "### REGRAS RIGOROSAS ###\n" +
                        "1. ADAPTAÇÃO DE NÍVEL: Adapte a linguagem das perguntas para corresponder ao nível do texto.\n" +
                        "2. FOCO: Crie perguntas focadas nos conceitos centrais. Evite detalhes triviais.\n" +
                        "3. FIDELIDADE: Respostas diretas apenas com informações presentes no texto base. Não invente dados.\n" +
                        "4. FORMATO: Siga EXATAMENTE o formato abaixo, sem saudações ou texto extra:\n" +
                        "Flashcard [Número]:\n" +
                        "Pergunta: [Insira a pergunta]\n" +
                        "Resposta: [Insira a resposta]\n\n" +
                        "5. QUANTIDADE: Crie no mínimo 5 e no máximo 10 flashcards (Foque em 7).\n" +
                        "### FIM DAS REGRAS ###\n\n" +
                        "<TEXTO_BASE>\n" +
                        textoExtraido +
                        "\n</TEXTO_BASE>";
        return aiGenerator.generateContent(prompt);
    }
}

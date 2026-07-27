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
                        "Você é um tutor educacional especialista em metodologias de aprendizagem ativa. " +
                        "Seu objetivo é criar flashcards de estudo precisos e úteis, baseados EXCLUSIVAMENTE no texto fornecido. " +
                        "Regras rigorosas:\n" +
                        "1. ADAPTAÇÃO DE NÍVEL: Analise a complexidade, o vocabulário e o tema do texto fornecido. Adapte a profundidade e a linguagem das perguntas para corresponder EXATAMENTE ao nível educacional do material (ex: ensino fundamental, médio, técnico ou superior).\n" +
                        "2. Crie perguntas focadas nos conceitos centrais. Evite focar em detalhes triviais e evite resumos superficiais.\n" +
                        "3. As respostas devem ser claras, diretas e conter apenas as informações presentes no texto base. Não invente dados.\n" +
                        "4. Siga EXATAMENTE este formato para cada flashcard, sem adicionar saudações, introduções ou texto extra:\n\n" +
                        "5. Por padrão vocês criará no minimo 5 flashcards e no maximo 10 flashcards (Foque em o padrão de flashcards ser 7 flashcards)" +
                        "Flashcard [Número]:\n" +
                        "Pergunta: [Insira a pergunta aqui]\n" +
                        "Resposta: [Insira a resposta explicativa aqui]\n\n" +
                        "Texto base para extração: " + textoExtraido;
        return aiGenerator.generateContent(prompt);
    }
}

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

        int limiteCaracteres = 80000;
        if (textoExtraido.length() > limiteCaracteres) {
            throw new IllegalArgumentException("O texto extraído possui " + textoExtraido.length() +
                    " caracteres. O limite seguro para a memória da IA local é de " + limiteCaracteres + " caracteres.");
        }

        String systemPrompt =
                "Você é um especialista em criar flashcards de estudo a partir de textos acadêmicos.\n" +
                        "Sua SAÍDA DEVE SER EXCLUSIVAMENTE UM JSON VÁLIDO. Não escreva mais nada.\n" +
                        "Esquema obrigatório:\n" +
                        "{\n" +
                        "  \"flashcards\": [\n" +
                        "    {\"question\": \"Sua pergunta sobre o conteúdo aqui?\", \"answer\": \"Sua resposta baseada no conteúdo aqui.\"}\n" +
                        "  ]\n" +
                        "}";

        StringBuilder textoAncorado = new StringBuilder();
        int tamanhoBloco = 5000;

        for (int i = 0; i < textoExtraido.length(); i += tamanhoBloco) {
            int fim = Math.min(i + tamanhoBloco, textoExtraido.length());
            textoAncorado.append(textoExtraido, i, fim);

            if (fim < textoExtraido.length()) {
                textoAncorado.append("\n\n[LEMBRETE DO SISTEMA: Continue analisando o texto. Seu objetivo final é gerar apenas perguntas e respostas no formato JSON.]\n\n");
            }
        }

        String userPrompt =
                "Analise o texto a seguir:\n\n" +
                        "--- INÍCIO DO TEXTO ---\n" +
                        textoAncorado.toString() +
                        "\n--- FIM DO TEXTO ---\n\n" +
                        "Agora, crie entre 5 e 10 flashcards baseados EXCLUSIVAMENTE no conteúdo do texto acima.\n" +
                        "NÃO crie flashcards sobre regras, JSON ou formatação.\n" +
                        "Retorne APENAS o JSON final:";

        return aiGenerator.generateContent(systemPrompt, userPrompt);
    }
}
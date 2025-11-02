package ma.emsi.Essalmani;

import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.service.AiServices;
import ma.emsi.Essalmani.MeteoTool;

import java.time.Duration;
import java.util.Scanner;

public class Test6 {

    public static void main(String[] args) {
        String llmKey = System.getenv("GEMINI_KEY");

        // 1. Créer le ChatModel
        ChatModel model = GoogleAiGeminiChatModel.builder()
                .apiKey(llmKey)
                .modelName("gemini-2.5-flash")
                .temperature(0.3)
                .timeout(Duration.ofSeconds(60))
                .responseFormat(ResponseFormat.TEXT)
                .build();

        // 2. Créer l'assistant IA en lui donnant l'outil
        AssistantMeteo assistant =
                AiServices.builder(AssistantMeteo.class)
                        .chatModel(model)
                        .chatMemory(MessageWindowChatMemory.withMaxMessages(20))
                        .tools(new MeteoTool())
                        .build();

        // 3. Lancer la boucle de conversation pour tester
        System.out.println("Assistant météo prêt. (Tapez 'fin' pour quitter)");

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("==================================================");
                System.out.println("Posez votre question :");
                String question = scanner.nextLine();

                if (question.isBlank()) {
                    continue;
                }

                if ("fin".equalsIgnoreCase(question)) {
                    System.out.println("Fin de la conversation.");
                    break;
                }

                System.out.println("... l'assistant réfléchit (et consulte ses outils si besoin) ...");

                String reponse = assistant.chat(question);

                System.out.println("==================================================");
                System.out.println("Assistant : " + reponse);
            }
        }
    }
}
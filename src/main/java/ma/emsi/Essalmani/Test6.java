package ma.emsi.Essalmani;

import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.service.AiServices;

import java.time.Duration;
import java.util.Scanner;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Test6 {

    // --- Configuration du logger JUL ---
    private static void configureLogger() {
        // Récupère le logger principal de LangChain4j
        Logger packageLogger = Logger.getLogger("dev.langchain4j");

        // Définit le niveau de logs (FINE = détaillé)
        packageLogger.setLevel(Level.FINE);

        // Crée un handler console pour afficher les logs dans la sortie standard
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.FINE);


        packageLogger.addHandler(handler);
    }

    public static void main(String[] args) {

        configureLogger();

        String llmKey = System.getenv("GEMINI_KEY");

        ChatModel model = GoogleAiGeminiChatModel.builder()
                .apiKey(llmKey)
                .modelName("gemini-2.5-flash")
                .temperature(0.3)
                .timeout(Duration.ofSeconds(60))
                .responseFormat(ResponseFormat.TEXT)
                .logRequestsAndResponses(true)
                .build();

        AssistantMeteo assistant = AiServices.builder(AssistantMeteo.class)
                .chatModel(model)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(20))
                .tools(new MeteoTool())
                .build();

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

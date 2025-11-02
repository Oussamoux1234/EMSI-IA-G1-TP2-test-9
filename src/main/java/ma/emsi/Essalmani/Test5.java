package ma.emsi.Essalmani;


import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
// Importez le parser de document
import dev.langchain4j.data.document.parser.apache.tika.ApacheTikaDocumentParser;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;

import java.time.Duration;
// Importez le Scanner
import java.util.Scanner;

/**
 * Le RAG facile !
 */
public class Test5 {

    // Assistant conversationnel
    interface Assistant {
        String chat(String userMessage);
    }

    public static void main(String[] args) {
        String llmKey = System.getenv("GEMINI_KEY");

        ChatModel model = GoogleAiGeminiChatModel.builder()
                .apiKey(llmKey)
                .modelName("gemini-2.5-flash")
                .temperature(0.3)
                .timeout(Duration.ofSeconds(60))
                .responseFormat(ResponseFormat.TEXT)
                .build();


        String nomDocument = "ml.pdf";


        Document document = FileSystemDocumentLoader.loadDocument(nomDocument, new ApacheTikaDocumentParser());

        EmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();


        EmbeddingStoreIngestor.ingest(document, embeddingStore);


        Assistant assistant =
                AiServices.builder(Assistant.class)
                        .chatModel(model)
                        .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                        .contentRetriever(EmbeddingStoreContentRetriever.from(embeddingStore))
                        .build();


        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("==================================================");
                System.out.println("Posez votre question (ou 'fin' pour quitter) :");
                String question = scanner.nextLine();

                if (question.isBlank()) {
                    continue;
                }

                if ("fin".equalsIgnoreCase(question)) {
                    System.out.println("Fin de la conversation.");
                    break;
                }

                System.out.println("... l'assistant réfléchit ...");

                String reponse = assistant.chat(question);

                System.out.println("==================================================");
                System.out.println("Assistant : " + reponse);
            }
        }

    }

}
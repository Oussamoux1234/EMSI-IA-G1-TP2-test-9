package ma.emsi.Essalmani;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.model.input.Prompt;

import dev.langchain4j.data.message.UserMessage;

import java.time.Duration;

public class Test2 {
    public static void main(String[] args) {

        String cle = System.getenv("GEMINI_KEY");

        ChatModel modele = GoogleAiGeminiChatModel.builder()
                .apiKey(cle)
                .modelName("gemini-2.5-flash")
                .temperature(0.7)
                .timeout(Duration.ofSeconds(60))
                .responseFormat(ResponseFormat.TEXT)
                .build();

        PromptTemplate template = PromptTemplate.from("Traduis le texte suivant en anglais : {{it}}");

        Prompt prompt = template.apply("Bonjour, je m'appelle Oussama et j'étudie à l'EMSI.");

        ChatRequest requete = ChatRequest.builder()
                .messages(UserMessage.from(prompt.text()))
                .build();

        ChatResponse reponse = modele.chat(requete);

        System.out.println("Texte à traduire : " + prompt.text());
        System.out.println("Traduction : " + reponse.aiMessage().text());
    }
}

package ma.emsi.Essalmani;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.model.googleai.GoogleAiEmbeddingModel;
import dev.langchain4j.model.googleai.GoogleAiEmbeddingModel.TaskType;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.store.embedding.CosineSimilarity;

import java.time.Duration;

public class Test3 {

    public static void main(String[] args) {

        String cle =  System.getenv("GEMINI_KEY");

        GoogleAiEmbeddingModel modele = GoogleAiEmbeddingModel.builder()
                .apiKey(cle)
                .modelName("text-embedding-004")
                .taskType(TaskType.SEMANTIC_SIMILARITY)
                .outputDimensionality(300)
                .timeout(Duration.ofSeconds(2))
                .build();


        String phrase1 = "Bonjour, comment allez-vous ?";
        String phrase2 = "Salut, quoi de neuf ?";


        Response<Embedding> reponse1 = modele.embed(phrase1);
        Response<Embedding> reponse2 = modele.embed(phrase2);

        Embedding emb1 = reponse1.content();
        Embedding emb2 = reponse2.content();


        double similarite = CosineSimilarity.between(emb1, emb2);


        System.out.println("Phrase 1 : " + phrase1);
        System.out.println("Phrase 2 : " + phrase2);
        System.out.println("Similarité cosinus : " + similarite);


        testerCouple(modele, "Je mange une pomme", "Je mange une banane");
        testerCouple(modele, "Il fait beau aujourd’hui", "J’adore le chocolat");
    }

    private static void testerCouple(GoogleAiEmbeddingModel modele, String p1, String p2) {
        Response<Embedding> r1 = modele.embed(p1);
        Response<Embedding> r2 = modele.embed(p2);
        double s = CosineSimilarity.between(r1.content(), r2.content());
        System.out.println("\nPhrase 1 : " + p1);
        System.out.println("Phrase 2 : " + p2);
        System.out.println("Similarité cosinus : " + s);
    }
}

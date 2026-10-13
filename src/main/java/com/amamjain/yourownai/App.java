
package com.amamjain.yourownai;

import java.util.List;

public class App {

    public static void main(String[] args) {
        EmbeddingService embeddingService =
                new OllamaEmbeddingService();

        SemanticSearchService semanticSearch =
                new SemanticSearchService(embeddingService);

        semanticSearch.addText(
                "doc1",
                "Java is an object-oriented programming language.");

        semanticSearch.addText(
                "doc2",
                "Python is widely used for data science and machine learning.");

        semanticSearch.addText(
                "doc3",
                "Neural networks learn patterns from training data.");

        semanticSearch.addText(
                "doc4",
                "A database stores and organizes information.");

        String query = "Which language is used for machine learning?";

        System.out.println("Query: " + query);
        System.out.println("\nSearch results:");

        List<SearchResult> results = semanticSearch.search(query, 3);

        for (SearchResult result : results) {
            String text = semanticSearch.getText(
                    result.getVector().getId());

            System.out.printf(
                    "ID: %s | Distance: %.4f%nText: %s%n%n",
                    result.getVector().getId(),
                    result.getDistance(),
                    text);
        }
    }
}

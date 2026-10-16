
package com.amamjain.yourownai;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

public class App {

    public static void main(String[] args) throws Exception {
        Path indexPath = Path.of("data", "semantic-index.json");

        // Create an index only if one does not exist yet.
        if (!Files.exists(indexPath)) {
            TextDocumentLoader loader =
                    new TextDocumentLoader();

            String document =
                    loader.load(Path.of("sample.txt"));

            TextChunker chunker =
                    new TextChunker(300, 50);

            List<String> chunks =
                    chunker.chunkText(document);

            SemanticSearchService newIndex =
                    new SemanticSearchService(
                            new OllamaEmbeddingService());

            for (int i = 0; i < chunks.size(); i++) {
                newIndex.addText(
                        "chunk-" + (i + 1),
                        chunks.get(i));
            }

            Files.createDirectories(indexPath.getParent());
            newIndex.save(indexPath);

            System.out.println("Created and saved index.");
        }

        // Restore saved passages and embeddings.
        SemanticSearchService searchService =
                SemanticSearchService.load(
                        indexPath,
                        new OllamaEmbeddingService());

        System.out.println("=== YourOwnAI-Java: RAG Demo ===");
        System.out.println(
                "Restored passages: " + searchService.size());

        // Connect retrieval to answer generation.
        RAGService ragService = new RAGService(
                searchService,
                new OllamaGenerationService());

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.print(
                        "\nAsk a question (type 'exit' to quit): ");

                String question = scanner.nextLine().trim();

                if (question.equalsIgnoreCase("exit")) {
                    System.out.println("Goodbye!");
                    break;
                }

                if (question.isBlank()) {
                    System.out.println(
                            "Please enter a non-empty question.");
                    continue;
                }

                try {
                    RAGResponse response =
        ragService.answerQuestion(question, 3);

System.out.println("\n=== Generated Answer ===");
System.out.println(response.getAnswer());

System.out.println("\n=== Sources ===");

if (response.getSources().isEmpty()) {
    System.out.println("No source passages were retrieved.");
} else {
    for (RAGResponse.SourceCitation source
            : response.getSources()) {

        System.out.printf(
                "%nSource: %s | Distance: %.4f%nText: %s%n",
                source.getId(),
                source.getDistance(),
                source.getText());
    }
}

                } catch (RuntimeException e) {
                    System.err.println(
                            "Could not generate an answer: "
                                    + e.getMessage());
                }
            }
        }
    }
}

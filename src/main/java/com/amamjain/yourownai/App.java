
package com.amamjain.yourownai;

import java.nio.file.Path;
import java.util.List;

public class App {

    public static void main(String[] args) throws Exception {
        Path indexPath = Path.of("data", "semantic-index.json");

        // Create an index only if one does not exist yet.
        if (!java.nio.file.Files.exists(indexPath)) {
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

            java.nio.file.Files.createDirectories(
                    indexPath.getParent());

            newIndex.save(indexPath);

            System.out.println("Created and saved index.");
        }

        // Load the saved vectors and text from disk.
        SemanticSearchService searchService =
                SemanticSearchService.load(
                        indexPath,
                        new OllamaEmbeddingService());

        System.out.println(
                "Restored passages: " + searchService.size());

        String query =
                "How does RAG use retrieved documents to answer questions?";

        System.out.println("\nQuery: " + query);

        List<SearchResult> results =
                searchService.search(query, 3);

        for (SearchResult result : results) {
            String id = result.getVector().getId();

            System.out.printf(
                    "%nID: %s | Distance: %.4f%nText: %s%n",
                    id,
                    result.getDistance(),
                    searchService.getText(id));
        }
    }
}

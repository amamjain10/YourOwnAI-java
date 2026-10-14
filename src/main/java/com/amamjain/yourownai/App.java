
package com.amamjain.yourownai;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class App {

    public static void main(String[] args) throws IOException {

        // 1. Load the document
        TextDocumentLoader loader = new TextDocumentLoader();

        String document = loader.load(Path.of("sample.txt"));

        System.out.println("Document loaded successfully.");
        System.out.println("Document characters: "
                + document.length());

        // 2. Split the document into chunks
        TextChunker chunker = new TextChunker(300, 50);
        List<String> chunks = chunker.chunkText(document);

        System.out.println("Total chunks: " + chunks.size());

        // 3. Create the embedding and semantic search services
        EmbeddingService embeddingService =
                new OllamaEmbeddingService();

        SemanticSearchService searchService =
                new SemanticSearchService(embeddingService);

        // 4. Generate embeddings and index every chunk
        for (int i = 0; i < chunks.size(); i++) {
            String id = "chunk-" + (i + 1);
            searchService.addText(id, chunks.get(i));
        }

        System.out.println("Indexed passages: "
                + searchService.size());

        // 5. Search the document
        String query =
                "How does RAG use retrieved documents to answer questions?";

        System.out.println("\nQuery: " + query);
        System.out.println("\nTop search results:");

        List<SearchResult> results =
                searchService.search(query, 3);

        // 6. Display the matching text
        for (SearchResult result : results) {
            String id = result.getVector().getId();
            String originalText = searchService.getText(id);

            System.out.printf(
                    "%nID: %s | Cosine distance: %.4f%n",
                    id,
                    result.getDistance());

            System.out.println("Text: " + originalText);
        }
    }
}

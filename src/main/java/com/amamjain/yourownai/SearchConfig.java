
package com.amamjain.yourownai;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SearchConfig {

    @Bean
    public EmbeddingService embeddingService() {
        return new OllamaEmbeddingService();
    }

    @Bean
    public SemanticSearchService semanticSearchService(
            EmbeddingService embeddingService)
            throws IOException {

        Path indexPath = Path.of("data", "semantic-index.json");
        Path documentsPath = Path.of("data", "documents");

        // Reuse the existing index if available.
        if (Files.exists(indexPath)) {
            System.out.println("Loading existing semantic index...");

            return SemanticSearchService.load(
                    indexPath, embeddingService);
        }

        // Create a fresh semantic index.
        System.out.println("No semantic index found.");
        System.out.println("Starting PDF ingestion...");

        if (!Files.isDirectory(documentsPath)) {
            throw new IllegalStateException(
                    "Documents directory not found: "
                            + documentsPath.toAbsolutePath());
        }

        SemanticSearchService searchService =
                new SemanticSearchService(embeddingService);

        DocumentIngestionService ingestionService =
                new DocumentIngestionService(
                        new PdfDocumentLoader(),
                        new TextChunker(1000, 150),
                        searchService);

        List<Path> pdfFiles;

        try (Stream<Path> files = Files.list(documentsPath)) {
            pdfFiles = files
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName()
                            .toString()
                            .toLowerCase()
                            .endsWith(".pdf"))
                    .sorted()
                    .toList();
        }

        if (pdfFiles.isEmpty()) {
            throw new IllegalStateException(
                    "No PDF files found in: "
                            + documentsPath.toAbsolutePath());
        }

        for (Path pdf : pdfFiles) {
            System.out.println(
                    "Ingesting: " + pdf.getFileName());

            ingestionService.ingest(pdf);
        }

        // Save the generated semantic index.
        Files.createDirectories(indexPath.getParent());
        searchService.save(indexPath);

        System.out.println("PDF ingestion completed.");
        System.out.println(
                "Total PDFs: " + pdfFiles.size());
        System.out.println(
                "Total chunks: " + searchService.size());
        System.out.println(
                "Index saved to: " + indexPath.toAbsolutePath());

        return searchService;
    }

    @Bean
    public TextGenerationService textGenerationService() {
        return new OllamaGenerationService();
    }

    @Bean
    public RAGService ragService(
            SemanticSearchService semanticSearchService,
            TextGenerationService textGenerationService) {

        return new RAGService(
                semanticSearchService,
                textGenerationService);
    }
}

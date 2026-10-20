
package com.amamjain.yourownai;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

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

        if (!Files.exists(indexPath)) {
            throw new IllegalStateException(
                    "Semantic index not found: "
                            + indexPath.toAbsolutePath());
        }

        return SemanticSearchService.load(
                indexPath, embeddingService);
    }
}

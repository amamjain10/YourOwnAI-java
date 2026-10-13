
package com.amamjain.yourownai;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SemanticSearchService {

    private final EmbeddingService embeddingService;
    private final VectorStore vectorStore;
    private final NearestNeighborSearch searchEngine;
    private final Map<String, String> textById;

    public SemanticSearchService(EmbeddingService embeddingService) {
        if (embeddingService == null) {
            throw new IllegalArgumentException(
                    "EmbeddingService cannot be null");
        }

        this.embeddingService = embeddingService;
        this.vectorStore = new VectorStore();
        this.searchEngine = new NearestNeighborSearch(vectorStore);
        this.textById = new LinkedHashMap<>();
    }

    public void addText(String id, String text) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException(
                    "ID cannot be null or blank");
        }

        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException(
                    "Text cannot be null or blank");
        }

        if (textById.containsKey(id)) {
            throw new IllegalArgumentException(
                    "Duplicate text ID: " + id);
        }

        double[] embedding = embeddingService.embed(text);
        Vector vector = new Vector(id, embedding);

        vectorStore.add(vector);
        textById.put(id, text);
    }

    public List<SearchResult> search(String query, int k) {
        if (query == null || query.isBlank()) {
            throw new IllegalArgumentException(
                    "Query cannot be null or blank");
        }

        double[] queryEmbedding = embeddingService.embed(query);

        return searchEngine.search(
                new Vector("search-query", queryEmbedding),
                k,
                SearchMetric.COSINE);
    }

    public String getText(String id) {
        return textById.get(id);
    }

    public int size() {
        return vectorStore.size();
    }
}

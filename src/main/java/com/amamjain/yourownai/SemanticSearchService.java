
package com.amamjain.yourownai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SemanticSearchService {

    private final EmbeddingService embeddingService;
    private final VectorStore vectorStore;
    private final NearestNeighborSearch searchEngine;
    private final Map<String, String> textById;
    private final ObjectMapper mapper;

    public SemanticSearchService(EmbeddingService embeddingService) {
        this(embeddingService, new VectorStore(),
                new LinkedHashMap<>());
    }

    private SemanticSearchService(
            EmbeddingService embeddingService,
            VectorStore vectorStore,
            Map<String, String> textById) {

        if (embeddingService == null) {
            throw new IllegalArgumentException(
                    "EmbeddingService cannot be null");
        }

        this.embeddingService = embeddingService;
        this.vectorStore = vectorStore;
        this.searchEngine =
                new NearestNeighborSearch(vectorStore);
        this.textById = textById;
        this.mapper = new ObjectMapper();
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

        double[] queryEmbedding =
                embeddingService.embed(query);

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

    public void save(Path path) throws IOException {
        if (path == null) {
            throw new IllegalArgumentException(
                    "Path cannot be null");
        }

        ObjectNode root = mapper.createObjectNode();
        ArrayNode documents = root.putArray("documents");

        for (Vector vector : vectorStore.getAll()) {
            ObjectNode item = documents.addObject();

            item.put("id", vector.getId());
            item.put("text", textById.get(vector.getId()));

            ArrayNode values = item.putArray("embedding");

            for (double value : vector.getValues()) {
                values.add(value);
            }
        }

        mapper.writerWithDefaultPrettyPrinter()
                .writeValue(path.toFile(), root);
    }

    public static SemanticSearchService load(
            Path path,
            EmbeddingService embeddingService)
            throws IOException {

        if (path == null) {
            throw new IllegalArgumentException(
                    "Path cannot be null");
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(path.toFile());
        JsonNode documents = root.path("documents");

        if (!documents.isArray()) {
            throw new IOException(
                    "Invalid index file: documents array missing");
        }

        SemanticSearchService service =
                new SemanticSearchService(embeddingService);

        for (JsonNode item : documents) {
            String id = item.path("id").asText();
            String text = item.path("text").asText();
            JsonNode values = item.path("embedding");

            if (id.isBlank() || text.isBlank()
                    || !values.isArray() || values.isEmpty()) {
                throw new IOException(
                        "Invalid stored document in index");
            }

            double[] embedding = new double[values.size()];

            for (int i = 0; i < values.size(); i++) {
                JsonNode value = values.get(i);

                if (!value.isNumber()
                        || !Double.isFinite(value.asDouble())) {
                    throw new IOException(
                            "Invalid embedding value for " + id);
                }

                embedding[i] = value.asDouble();
            }

            Vector vector = new Vector(id, embedding);
            service.vectorStore.add(vector);
            service.textById.put(id, text);
        }

        return service;
    }
}


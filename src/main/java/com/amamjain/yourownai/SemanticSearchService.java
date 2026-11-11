
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
    private final Map<String, DocumentMetadata> metadataById;

    private final ObjectMapper mapper;

    public SemanticSearchService(EmbeddingService embeddingService) {
        this(embeddingService, new VectorStore(),
                new LinkedHashMap<>(),
                new LinkedHashMap<>());
    }

    private SemanticSearchService(
            EmbeddingService embeddingService,
            VectorStore vectorStore,
            Map<String, String> textById,
            Map<String, DocumentMetadata> metadataById) {

        if (embeddingService == null) {
            throw new IllegalArgumentException(
                    "EmbeddingService cannot be null");
        }

        this.embeddingService = embeddingService;
        this.vectorStore = vectorStore;
        this.searchEngine =
                new NearestNeighborSearch(vectorStore);

        this.textById = textById;
        this.metadataById = metadataById;

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

    /**
     * Adds source metadata to an already stored chunk.
     */
    public void addMetadata(
            String id,
            DocumentMetadata metadata) {

        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException(
                    "ID cannot be null or blank");
        }

        if (!textById.containsKey(id)) {
            throw new IllegalArgumentException(
                    "Cannot add metadata: unknown chunk ID " + id);
        }

        if (metadata == null) {
            throw new IllegalArgumentException(
                    "Metadata cannot be null");
        }

        metadataById.put(id, metadata);
    }

    public List<SearchResult> search(String query, int k) {
        if (query == null || query.isBlank()) {
            throw new IllegalArgumentException(
                    "Query cannot be null or blank");
        }

        double[] queryEmbedding =
                embeddingService.embed(query);

        List<SearchResult> results = searchEngine.search(
                new Vector("search-query", queryEmbedding),
                k,
                SearchMetric.COSINE);

        return results.stream()
                .map(result -> new SearchResult(
                        result.getVector(),
                        result.getDistance(),
                        metadataById.get(
                                result.getVector().getId())))
                .toList();
    }

    public String getText(String id) {
        return textById.get(id);
    }

    public DocumentMetadata getMetadata(String id) {
        return metadataById.get(id);
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

            String id = vector.getId();

            ObjectNode item = documents.addObject();

            item.put("id", id);
            item.put("text", textById.get(id));

            ArrayNode values = item.putArray("embedding");

            for (double value : vector.getValues()) {
                values.add(value);
            }

            DocumentMetadata metadata =
                    metadataById.get(id);

            if (metadata != null) {
                ObjectNode metadataNode =
                        item.putObject("metadata");

                metadataNode.put(
                        "documentName",
                        metadata.documentName());

                metadataNode.put(
                        "pageNumber",
                        metadata.pageNumber());

                metadataNode.put(
                        "chunkNumber",
                        metadata.chunkNumber());
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

            double[] embedding =
                    new double[values.size()];

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

            // Restore source metadata if available.
            JsonNode metadataNode = item.path("metadata");

            if (metadataNode.isObject()) {

                String documentName =
                        metadataNode.path("documentName").asText();

                int pageNumber =
                        metadataNode.path("pageNumber").asInt();

                int chunkNumber =
                        metadataNode.path("chunkNumber").asInt();

                try {
                    DocumentMetadata metadata =
                            new DocumentMetadata(
                                    documentName,
                                    pageNumber,
                                    chunkNumber
                            );

                    service.metadataById.put(id, metadata);

                } catch (IllegalArgumentException e) {
                    throw new IOException(
                            "Invalid metadata for chunk " + id, e);
                }
            }
        }

        return service;
    }
}

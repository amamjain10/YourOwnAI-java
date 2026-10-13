
package com.amamjain.yourownai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class OllamaEmbeddingService implements EmbeddingService {

    private static final String ENDPOINT =
            "http://localhost:11434/api/embed";
    private static final String MODEL = "nomic-embed-text";

    private final HttpClient client;
    private final ObjectMapper mapper;

    public OllamaEmbeddingService() {
        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.mapper = new ObjectMapper();
    }

    @Override
    public double[] embed(String text) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException(
                    "Text cannot be null or blank");
        }

        try {
            String body = mapper.createObjectNode()
                    .put("model", MODEL)
                    .put("input", text)
                    .toString();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(ENDPOINT))
                    .timeout(Duration.ofSeconds(120))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new IllegalStateException(
                        "Ollama returned HTTP "
                                + response.statusCode()
                                + ": " + response.body());
            }

            JsonNode root = mapper.readTree(response.body());
            JsonNode values = root.path("embeddings").path(0);

            if (!values.isArray() || values.isEmpty()) {
                throw new IllegalStateException(
                        "Ollama returned an empty embedding");
            }

            double[] embedding = new double[values.size()];

            for (int i = 0; i < values.size(); i++) {
                JsonNode value = values.get(i);

                if (!value.isNumber()
                        || !Double.isFinite(value.asDouble())) {
                    throw new IllegalStateException(
                            "Invalid embedding value at index " + i);
                }

                embedding[i] = value.asDouble();
            }

            return embedding;

        } catch (IOException e) {
            throw new IllegalStateException(
                    "Could not communicate with Ollama", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(
                    "Ollama request was interrupted", e);
        }
    }
}

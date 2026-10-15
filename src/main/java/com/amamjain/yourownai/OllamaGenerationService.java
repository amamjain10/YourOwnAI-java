
package com.amamjain.yourownai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;

public class OllamaGenerationService implements TextGenerationService {

    private static final String DEFAULT_URL =
            "http://localhost:11434/api/generate";

    private final String model;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public OllamaGenerationService() {
        this("qwen2.5:3b");
    }

    public OllamaGenerationService(String model) {
        if (model == null || model.isBlank()) {
            throw new IllegalArgumentException(
                    "Model name cannot be blank");
        }

        this.model = model;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.objectMapper = new ObjectMapper();
    }

    public String generate(String prompt) {
        if (prompt == null || prompt.isBlank()) {
            throw new IllegalArgumentException(
                    "Prompt cannot be blank");
        }

        try {
            String requestBody = objectMapper.writeValueAsString(
                    Map.of(
                            "model", model,
                            "prompt", prompt,
                            "stream", false
                    )
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(DEFAULT_URL))
                    .timeout(Duration.ofMinutes(3))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(
                            requestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            if (response.statusCode() != 200) {
                throw new IllegalStateException(
                        "Ollama returned HTTP "
                                + response.statusCode()
                );
            }

            JsonNode json = objectMapper.readTree(
                    response.body());

            JsonNode answer = json.get("response");

            if (answer == null || !answer.isTextual()
                    || answer.asText().isBlank()) {
                throw new IllegalStateException(
                        "Ollama returned an empty response");
            }

            return answer.asText().trim();

        } catch (IOException e) {
            throw new IllegalStateException(
                    "Could not communicate with Ollama", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(
                    "Generation was interrupted", e);
        }
    }
}

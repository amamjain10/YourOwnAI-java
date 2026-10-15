
package com.amamjain.yourownai;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.*;

public class RAGServiceTest {

    private SemanticSearchService createSearchService() {
        EmbeddingService embeddingService = text -> {
            String normalized = text.toLowerCase();

            if (normalized.contains("java")) {
                return new double[]{1.0, 0.0, 0.0};
            }

            if (normalized.contains("python")) {
                return new double[]{0.0, 1.0, 0.0};
            }

            return new double[]{0.8, 0.2, 0.0};
        };

        SemanticSearchService service =
                new SemanticSearchService(embeddingService);

        service.addText(
                "java-doc",
                "Java is a programming language.");

        service.addText(
                "python-doc",
                "Python is widely used for machine learning.");

        return service;
    }

    @Test
    public void shouldGenerateAnswerUsingRetrievedContext() {
        SemanticSearchService searchService =
                createSearchService();

        AtomicReference<String> receivedPrompt =
                new AtomicReference<>();

        TextGenerationService generator = prompt -> {
            receivedPrompt.set(prompt);
            return "Java is a programming language.";
        };

        RAGService ragService =
                new RAGService(searchService, generator);

        String answer = ragService.answerQuestion(
                "Tell me about Java", 1);

        assertEquals(
                "Java is a programming language.", answer);

        assertNotNull(receivedPrompt.get());
        assertTrue(receivedPrompt.get().contains(
                "Java is a programming language."));
        assertTrue(receivedPrompt.get().contains(
                "Tell me about Java"));
    }

    @Test
    public void shouldRejectBlankQuestion() {
        RAGService ragService = new RAGService(
                createSearchService(),
                prompt -> "Test answer");

        try {
            ragService.answerQuestion("   ", 1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            // Expected.
        }
    }

    @Test
    public void shouldRejectInvalidTopK() {
        RAGService ragService = new RAGService(
                createSearchService(),
                prompt -> "Test answer");

        try {
            ragService.answerQuestion("Tell me about Java", 0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            // Expected.
        }
    }

    @Test
    public void shouldHandleEmptySearchResults() {
        EmbeddingService embeddingService =
                text -> new double[]{1.0, 0.0, 0.0};

        SemanticSearchService emptySearchService =
                new SemanticSearchService(embeddingService);

        AtomicReference<String> receivedPrompt =
                new AtomicReference<>();

        TextGenerationService generator = prompt -> {
            receivedPrompt.set(prompt);
            return "Unexpected answer";
        };

        RAGService ragService =
                new RAGService(emptySearchService, generator);

        String answer = ragService.answerQuestion(
                "Tell me about Java", 3);

        assertTrue(answer.contains(
                "could not find relevant information"));
        assertNull(receivedPrompt.get());
    }
}

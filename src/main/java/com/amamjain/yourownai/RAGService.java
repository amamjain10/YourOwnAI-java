
package com.amamjain.yourownai;

import java.util.ArrayList;
import java.util.List;

public class RAGService {

    private final SemanticSearchService semanticSearchService;
    private final TextGenerationService generationService;

    public RAGService(
            SemanticSearchService semanticSearchService,
            TextGenerationService generationService) {

        if (semanticSearchService == null) {
            throw new IllegalArgumentException(
                    "Semantic search service cannot be null");
        }

        if (generationService == null) {
            throw new IllegalArgumentException(
                    "Generation service cannot be null");
        }

        this.semanticSearchService = semanticSearchService;
        this.generationService = generationService;
    }

    public RAGResponse answerQuestion(String question, int topK) {
        if (question == null || question.isBlank()) {
            throw new IllegalArgumentException(
                    "Question cannot be blank");
        }

        if (topK <= 0) {
            throw new IllegalArgumentException(
                    "topK must be greater than zero");
        }

        List<SearchResult> results =
                semanticSearchService.search(question, topK);

        if (results.isEmpty()) {
            return new RAGResponse(
                    "I could not find relevant information "
                            + "to answer this question.",
                    List.of());
        }

        StringBuilder context = new StringBuilder();
        List<RAGResponse.SourceCitation> citations =
                new ArrayList<>();

        for (SearchResult result : results) {
            String id = result.getVector().getId();
            String text = semanticSearchService.getText(id);

            if (text != null && !text.isBlank()) {
                context.append("[Passage: ")
                        .append(id)
                        .append("]\n")
                        .append(text)
                        .append("\n\n");

                citations.add(
                        new RAGResponse.SourceCitation(
                                id,
                                text,
                                result.getDistance()));
            }
        }

        if (context.isEmpty()) {
            return new RAGResponse(
                    "I could not retrieve document text "
                            + "to answer this question.",
                    List.of());
        }

        String prompt = """
                You are a helpful assistant answering questions
                using retrieved document passages.

                Rules:
                - Use only the information in the provided context.
                - Do not invent facts or unsupported details.
                - If the context does not contain the answer,
                  clearly say that the provided context is insufficient.
                - Answer clearly and directly.

                Retrieved context:
                %s

                User question:
                %s

                Answer:
                """.formatted(context, question);

        String answer = generationService.generate(prompt);

        return new RAGResponse(answer, citations);
    }
}

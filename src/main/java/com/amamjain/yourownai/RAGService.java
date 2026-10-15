
package com.amamjain.yourownai;

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

    public String answerQuestion(String question, int topK) {
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
            return "I could not find relevant information "
                    + "to answer this question.";
        }

        StringBuilder context = new StringBuilder();

        for (SearchResult result : results) {
            String id = result.getVector().getId();

            String text = semanticSearchService.getText(id);

                if (text != null && !text.isBlank()) {
                    context.append("[Passage: ")
                            .append(id)
                            .append("]\n")
                            .append(text)
                            .append("\n\n");
                }
        }

        if (context.isEmpty()) {
            return "I could not retrieve document text "
                    + "to answer this question.";
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

        return generationService.generate(prompt);
    }
}

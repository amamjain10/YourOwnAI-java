
package com.amamjain.yourownai;

import java.util.List;

public class RAGResponse {

    private final String answer;
    private final List<SourceCitation> sources;

    public RAGResponse(
            String answer,
            List<SourceCitation> sources) {

        if (answer == null || answer.isBlank()) {
            throw new IllegalArgumentException(
                    "Answer cannot be blank");
        }

        if (sources == null) {
            throw new IllegalArgumentException(
                    "Sources cannot be null");
        }

        this.answer = answer;
        this.sources = List.copyOf(sources);
    }

    public String getAnswer() {
        return answer;
    }

    public List<SourceCitation> getSources() {
        return sources;
    }

    public static class SourceCitation {

        private final String id;
        private final String text;
        private final double distance;

        public SourceCitation(
                String id,
                String text,
                double distance) {

            if (id == null || id.isBlank()) {
                throw new IllegalArgumentException(
                        "Source ID cannot be blank");
            }

            if (text == null || text.isBlank()) {
                throw new IllegalArgumentException(
                        "Source text cannot be blank");
            }

            if (!Double.isFinite(distance) || distance < 0) {
                throw new IllegalArgumentException(
                        "Distance must be finite and non-negative");
            }

            this.id = id;
            this.text = text;
            this.distance = distance;
        }

        public String getId() {
            return id;
        }

        public String getText() {
            return text;
        }

        public double getDistance() {
            return distance;
        }
    }
}

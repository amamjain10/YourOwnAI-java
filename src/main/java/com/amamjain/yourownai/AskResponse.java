
package com.amamjain.yourownai;

import java.util.List;

public class AskResponse {

    private final String answer;
    private final List<SourceItem> sources;

    public AskResponse(String answer, List<SourceItem> sources) {
        this.answer = answer;
        this.sources = List.copyOf(sources);
    }

    public String getAnswer() {
        return answer;
    }

    public List<SourceItem> getSources() {
        return sources;
    }

    public static class SourceItem {

        private final String id;
        private final String text;
        private final double score;

        public SourceItem(String id, String text, double score) {
            this.id = id;
            this.text = text;
            this.score = score;
        }

        public String getId() {
            return id;
        }

        public String getText() {
            return text;
        }

        public double getScore() {
            return score;
        }
    }
}

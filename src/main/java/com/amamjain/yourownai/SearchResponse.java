
package com.amamjain.yourownai;

import java.util.List;

public class SearchResponse {

    private final String query;
    private final List<SearchItem> results;

    public SearchResponse(String query, List<SearchItem> results) {
        this.query = query;
        this.results = List.copyOf(results);
    }

    public String getQuery() {
        return query;
    }

    public List<SearchItem> getResults() {
        return results;
    }

    public static class SearchItem {

        private final String id;
        private final String text;
        private final double score;

        public SearchItem(String id, String text, double score) {
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

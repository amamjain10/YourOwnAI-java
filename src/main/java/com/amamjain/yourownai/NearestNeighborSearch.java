
package com.amamjain.yourownai;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.PriorityQueue;

public class NearestNeighborSearch {

    private final VectorStore vectorStore;

    public NearestNeighborSearch(VectorStore vectorStore) {
        this.vectorStore = Objects.requireNonNull(
                vectorStore, "VectorStore cannot be null");
    }

    // Default search uses Euclidean distance
    public List<SearchResult> search(Vector query, int k) {
        return search(query, k, SearchMetric.EUCLIDEAN);
    }

    // Search using the selected metric
    public List<SearchResult> search(
            Vector query, int k, SearchMetric metric) {

        Objects.requireNonNull(query, "Query vector cannot be null");

        if (metric == null) {
            throw new IllegalArgumentException(
                    "Search metric cannot be null");
        }

        if (k <= 0) {
            throw new IllegalArgumentException(
                    "k must be greater than zero");
        }

        Comparator<SearchResult> bestFirst =
                Comparator.comparingDouble(SearchResult::getDistance)
                        .thenComparing(
                                result -> result.getVector().getId());

        // The worst result among the retained candidates stays at the head.
        PriorityQueue<SearchResult> topResults =
                new PriorityQueue<>(bestFirst.reversed());

        for (Vector candidate : vectorStore.getAll()) {
            double score;

            switch (metric) {
                case EUCLIDEAN:
                    score = VectorMetrics.euclideanDistance(
                            query, candidate);
                    break;

                case MANHATTAN:
                    score = VectorMetrics.manhattanDistance(
                            query, candidate);
                    break;

                case COSINE:
                    score = 1.0
                            - VectorMetrics.cosineSimilarity(
                                    query, candidate);
                    break;

                default:
                    throw new IllegalArgumentException(
                            "Unsupported search metric: " + metric);
            }

            SearchResult result = new SearchResult(candidate, score);

            if (topResults.size() < k) {
                topResults.offer(result);
            } else if (bestFirst.compare(
                    result, topResults.peek()) < 0) {
                topResults.poll();
                topResults.offer(result);
            }
        }

        List<SearchResult> results = new ArrayList<>(topResults);
        results.sort(bestFirst);

        return List.copyOf(results);
    }
}

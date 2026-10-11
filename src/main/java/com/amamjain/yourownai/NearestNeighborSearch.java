
package com.amamjain.yourownai;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

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
        Objects.requireNonNull(metric, "Search metric cannot be null");

        if (k <= 0) {
            throw new IllegalArgumentException(
                    "k must be greater than zero");
        }

        List<SearchResult> results = new ArrayList<>();

        for (Vector candidate : vectorStore.getAll()) {
            double score;

            switch (metric) {
                case EUCLIDEAN:
                    score = VectorMetrics.euclideanDistance(query, candidate);
                    break;

                case MANHATTAN:
                    score = VectorMetrics.manhattanDistance(query, candidate);
                    break;

                case COSINE:
                    score = 1.0
                            - VectorMetrics.cosineSimilarity(query, candidate);
                    break;

                default:
                    throw new IllegalArgumentException(
                            "Unsupported search metric: " + metric);
            }

            results.add(new SearchResult(candidate, score));
        }

        results.sort(
                Comparator.comparingDouble(SearchResult::getDistance)
                        .thenComparing(result -> result.getVector().getId())
        );

        return List.copyOf(
                results.subList(0, Math.min(k, results.size())));
    }
}


package com.amamjain.yourownai;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class NearestNeighborSearch {

    private final VectorStore vectorStore;

    public NearestNeighborSearch(VectorStore vectorStore) {
        if (vectorStore == null) {
            throw new IllegalArgumentException(
                "VectorStore cannot be null"
            );
        }

        this.vectorStore = vectorStore;
    }

    public List<SearchResult> search(Vector query, int k) {
        if (query == null) {
            throw new IllegalArgumentException(
                "Query vector cannot be null"
            );
        }

        if (k <= 0) {
            throw new IllegalArgumentException(
                "k must be greater than zero"
            );
        }

        List<SearchResult> results = new ArrayList<>();

        for (Vector candidate : vectorStore.getAll()) {
            double distance =
                VectorMetrics.euclideanDistance(query, candidate);

            results.add(new SearchResult(candidate, distance));
        }

        results.sort(
            Comparator.comparingDouble(SearchResult::getDistance)
                .thenComparing(
                    result -> result.getVector().getId()
                )
        );

        return List.copyOf(
            results.subList(0, Math.min(k, results.size()))
        );
    }
}

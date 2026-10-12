
package com.amamjain.yourownai;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class VectorSearchService {

    private final VectorStore vectorStore;
    private final NearestNeighborSearch nearestNeighborSearch;

    public VectorSearchService(VectorStore vectorStore) {
        this.vectorStore = Objects.requireNonNull(
                vectorStore, "VectorStore cannot be null");

        this.nearestNeighborSearch =
                new NearestNeighborSearch(vectorStore);
    }

    public void addVector(Vector vector) {
        vectorStore.add(vector);
    }

    public Optional<Vector> getVector(String id) {
        return vectorStore.get(id);
    }

    public boolean removeVector(String id) {
        return vectorStore.remove(id);
    }

    public int getVectorCount() {
        return vectorStore.size();
    }

    public List<SearchResult> search(
            Vector query, int k, SearchMetric metric) {

        return nearestNeighborSearch.search(query, k, metric);
    }
}

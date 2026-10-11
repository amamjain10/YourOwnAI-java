
package com.amamjain.yourownai;

import java.util.List;

public class App {

    public static void main(String[] args) {
        // 1. Create the vector store.
        VectorStore store = new VectorStore();

        // 2. Add sample vectors.
        store.add(new Vector("A", new double[]{1, 2}));
        store.add(new Vector("B", new double[]{2, 4}));
        store.add(new Vector("C", new double[]{2, 1}));
        store.add(new Vector("D", new double[]{5, 5}));

        // 3. Define the query vector.
        Vector query = new Vector("query", new double[]{1, 2});

        // 4. Create the search engine.
        NearestNeighborSearch search =
            new NearestNeighborSearch(store);

        // 5. Run a search with each metric.
        runSearch(search, query, SearchMetric.EUCLIDEAN);
        runSearch(search, query, SearchMetric.MANHATTAN);
        runSearch(search, query, SearchMetric.COSINE);
    }

    private static void runSearch(
            NearestNeighborSearch search,
            Vector query,
            SearchMetric metric) {

        System.out.println("\n=== " + metric + " Search ===");

        List<SearchResult> results = search.search(query, 3, metric);

        for (int i = 0; i < results.size(); i++) {
            SearchResult result = results.get(i);

            System.out.printf(
                "%d. Vector ID: %s | Score: %.4f%n",
                i + 1,
                result.getVector().getId(),
                result.getDistance()
            );
        }
    }
}

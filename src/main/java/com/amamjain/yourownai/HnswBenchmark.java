
package com.amamjain.yourownai;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.HashSet;
import java.util.Set;

public class HnswBenchmark {

    private static final int DIMENSIONS = 128;
    private static final int K = 3;
    private static final int EF_SEARCH = 30;
    private static final int QUERY_COUNT = 100;
    private static final int WARMUP_ROUNDS = 2;
    private static final int MEASURED_ROUNDS = 5;

    public static void main(String[] args) {
        int[] datasetSizes = {100, 1000, 5000};

        System.out.println("Vector Search Benchmark");
        System.out.println("Dimensions: " + DIMENSIONS);
        System.out.println("Top K: " + K);
        System.out.println("HNSW efSearch: " + EF_SEARCH);
        System.out.println("Queries per round: " + QUERY_COUNT);
        System.out.println();

        for (int size : datasetSizes) {
            runBenchmark(size);
        }
    }

    private static void runBenchmark(int size) {
        Random random = new Random(42 + size);
        List<Vector> vectors = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            double[] values = new double[DIMENSIONS];

            for (int j = 0; j < DIMENSIONS; j++) {
                values[j] = random.nextDouble() * 100;
            }

            vectors.add(new Vector("V" + i, values));
        }

        VectorStore store = new VectorStore();
        for (Vector vector : vectors) {
            store.add(vector);
        }

        NearestNeighborSearch bruteForce =
                new NearestNeighborSearch(store);

        long kdBuildStart = System.nanoTime();
        KDTree kdTree = new KDTree(vectors);
        long kdBuildTime = System.nanoTime() - kdBuildStart;

        long hnswBuildStart = System.nanoTime();
        HnswIndex hnsw = new HnswIndex(8, 42);

        for (Vector vector : vectors) {
            hnsw.add(vector);
        }

        long hnswBuildTime = System.nanoTime() - hnswBuildStart;

        List<Vector> queries = new ArrayList<>();

        for (int i = 0; i < QUERY_COUNT; i++) {
            double[] values = new double[DIMENSIONS];

            for (int j = 0; j < DIMENSIONS; j++) {
                values[j] = random.nextDouble() * 100;
            }

            queries.add(new Vector("Q" + i, values));
        }

        // Warm up all algorithms.
        for (int round = 0; round < WARMUP_ROUNDS; round++) {
            for (Vector query : queries) {
                bruteForce.search(query, K);
                kdTree.search(query, K);
                hnsw.search(query, K, EF_SEARCH);
            }
        }

        long bruteTotal = 0;
        long kdTotal = 0;
        long hnswTotal = 0;

        int recallMatches = 0;
        int recallExpected = 0;

        for (Vector query : queries) {
            List<SearchResult> exact = bruteForce.search(query, K);
            List<SearchResult> approximate =
                    hnsw.search(query, K, EF_SEARCH);

            Set<String> exactIds = new HashSet<>();
            for (SearchResult result : exact) {
                exactIds.add(result.getVector().getId());
            }

            for (SearchResult result : approximate) {
                if (exactIds.contains(result.getVector().getId())) {
                    recallMatches++;
                }
            }

            recallExpected += exact.size();
        }

        for (int round = 0; round < MEASURED_ROUNDS; round++) {
            // Alternate order to reduce order-related timing bias.
            if (round % 2 == 0) {
                bruteTotal += timeBruteForce(bruteForce, queries);
                kdTotal += timeKDTree(kdTree, queries);
                hnswTotal += timeHnsw(hnsw, queries);
            } else {
                hnswTotal += timeHnsw(hnsw, queries);
                kdTotal += timeKDTree(kdTree, queries);
                bruteTotal += timeBruteForce(bruteForce, queries);
            }
        }

        int totalQueries = MEASURED_ROUNDS * QUERY_COUNT;

        double bruteMs = averageMs(bruteTotal, totalQueries);
        double kdMs = averageMs(kdTotal, totalQueries);
        double hnswMs = averageMs(hnswTotal, totalQueries);

        double recall = recallExpected == 0
                ? 1.0
                : (double) recallMatches / recallExpected;

        System.out.println("Dataset size: " + size);
        System.out.printf("KD-tree build time: %.3f ms%n",
                kdBuildTime / 1_000_000.0);
        System.out.printf("HNSW build time:    %.3f ms%n",
                hnswBuildTime / 1_000_000.0);
        System.out.printf("Brute-force:       %.4f ms/query%n", bruteMs);
        System.out.printf("KD-tree:           %.4f ms/query%n", kdMs);
        System.out.printf("HNSW:              %.4f ms/query%n", hnswMs);
        System.out.printf("HNSW Recall@3:     %.2f%%%n", recall * 100);
        System.out.println();
    }

    private static double averageMs(long totalNanos, int queries) {
        return totalNanos / (double) queries / 1_000_000.0;
    }

    private static long timeBruteForce(
            NearestNeighborSearch search, List<Vector> queries) {
        long start = System.nanoTime();
        for (Vector query : queries) {
            search.search(query, K);
        }
        return System.nanoTime() - start;
    }

    private static long timeKDTree(
            KDTree tree, List<Vector> queries) {
        long start = System.nanoTime();
        for (Vector query : queries) {
            tree.search(query, K);
        }
        return System.nanoTime() - start;
    }

    private static long timeHnsw(
            HnswIndex index, List<Vector> queries) {
        long start = System.nanoTime();
        for (Vector query : queries) {
            index.search(query, K, EF_SEARCH);
        }
        return System.nanoTime() - start;
    }
}

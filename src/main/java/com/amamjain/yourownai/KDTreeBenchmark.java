
package com.amamjain.yourownai;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class KDTreeBenchmark {

    private static final int DIMENSIONS = 128;
    private static final int K = 5;
    private static final int QUERY_COUNT = 100;
    private static final int WARMUP_ROUNDS = 2;
    private static final int MEASURED_ROUNDS = 5;

    public static void main(String[] args) {
        int[] datasetSizes = {100, 1000, 5000};

        System.out.println("KD-tree vs Brute-Force Benchmark");
        System.out.println("Dimensions: " + DIMENSIONS);
        System.out.println("Top K: " + K);
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

            vectors.add(new Vector("vector-" + i, values));
        }

        VectorStore store = new VectorStore();

        for (Vector vector : vectors) {
            store.add(vector);
        }

        NearestNeighborSearch bruteForce =
                new NearestNeighborSearch(store);

        long buildStart = System.nanoTime();
        KDTree kdTree = new KDTree(vectors);
        long buildTime = System.nanoTime() - buildStart;

        List<Vector> queries = new ArrayList<>();

        for (int i = 0; i < QUERY_COUNT; i++) {
            double[] values = new double[DIMENSIONS];

            for (int j = 0; j < DIMENSIONS; j++) {
                values[j] = random.nextDouble() * 100;
            }

            queries.add(new Vector("query-" + i, values));
        }

        // Warm up both implementations.
        for (int round = 0; round < WARMUP_ROUNDS; round++) {
            for (Vector query : queries) {
                bruteForce.search(query, K);
                kdTree.search(query, K);
            }
        }

        long bruteForceTotal = 0;
        long kdTreeTotal = 0;

        for (int round = 0; round < MEASURED_ROUNDS; round++) {
            // Alternate measurement order to reduce order bias.
            if (round % 2 == 0) {
                bruteForceTotal += timeSearches(bruteForce, queries);
                kdTreeTotal += timeSearches(kdTree, queries);
            } else {
                kdTreeTotal += timeSearches(kdTree, queries);
                bruteForceTotal += timeSearches(bruteForce, queries);
            }
        }

        int totalQueries = MEASURED_ROUNDS * QUERY_COUNT;

        double bruteForceMs =
                bruteForceTotal / (double) totalQueries / 1_000_000.0;

        double kdTreeMs =
                kdTreeTotal / (double) totalQueries / 1_000_000.0;

        double speedup = bruteForceMs / kdTreeMs;

        System.out.println("Dataset size: " + size);
        System.out.printf(
                "KD-tree build time: %.3f ms%n",
                buildTime / 1_000_000.0);
        System.out.printf(
                "Brute-force average: %.4f ms/query%n",
                bruteForceMs);
        System.out.printf(
                "KD-tree average:     %.4f ms/query%n",
                kdTreeMs);
        System.out.printf(
                "KD-tree speedup:     %.2fx%n",
                speedup);
        System.out.println();
    }

    private static long timeSearches(
            NearestNeighborSearch search, List<Vector> queries) {
        long start = System.nanoTime();

        for (Vector query : queries) {
            search.search(query, K);
        }

        return System.nanoTime() - start;
    }

    private static long timeSearches(
            KDTree tree, List<Vector> queries) {
        long start = System.nanoTime();

        for (Vector query : queries) {
            tree.search(query, K);
        }

        return System.nanoTime() - start;
    }
}

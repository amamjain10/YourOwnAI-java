
package com.amamjain.yourownai;

import java.util.Random;

public class SearchBenchmark {

    public static void main(String[] args) {
        int dimensions = 128;
        int[] datasetSizes = {100, 1000, 5000};
        int k = 3;

        Random random = new Random(42);

        System.out.println("Brute-Force Search Benchmark");
        System.out.println("Dimensions: " + dimensions);
        System.out.println("Top K: " + k);
        System.out.println();

        for (int size : datasetSizes) {
            VectorStore store = new VectorStore();

            for (int i = 0; i < size; i++) {
                double[] values = new double[dimensions];

                for (int j = 0; j < dimensions; j++) {
                    values[j] = random.nextDouble();
                }

                store.add(new Vector("vector-" + i, values));
            }

            NearestNeighborSearch search =
                    new NearestNeighborSearch(store);

            double[] queryValues = new double[dimensions];

            for (int j = 0; j < dimensions; j++) {
                queryValues[j] = random.nextDouble();
            }

            Vector query = new Vector("query", queryValues);

            // Warm up the JVM before timing.
            for (int i = 0; i < 5; i++) {
                search.search(query, k);
            }

            int repetitions = 20;
            long start = System.nanoTime();

            for (int i = 0; i < repetitions; i++) {
                search.search(query, k);
            }

            long elapsed = System.nanoTime() - start;
            double averageMs =
                    elapsed / (double) repetitions / 1_000_000.0;

            System.out.printf(
                    "Vectors: %d | Average search: %.3f ms%n",
                    size, averageMs);
        }
    }
}


package com.amamjain.yourownai;

import org.junit.Test;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.List;

public class HnswIndexTest {

    @Test
    public void testInsertVectors() {
        HnswIndex index = new HnswIndex(4, 42);

        index.add(new Vector("A", new double[]{1, 2}));
        index.add(new Vector("B", new double[]{2, 3}));
        index.add(new Vector("C", new double[]{3, 4}));

        assertEquals(3, index.size());
    }

    @Test
    public void testEmptyIndex() {
        HnswIndex index = new HnswIndex(4, 42);

        assertEquals(0, index.size());
        assertEquals(-1, index.getMaxLevel());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRejectDuplicateIds() {
        HnswIndex index = new HnswIndex(4, 42);

        index.add(new Vector("A", new double[]{1, 2}));
        index.add(new Vector("A", new double[]{3, 4}));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRejectDifferentDimensions() {
        HnswIndex index = new HnswIndex(4, 42);

        index.add(new Vector("A", new double[]{1, 2}));
        index.add(new Vector("B", new double[]{1, 2, 3}));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRejectInvalidNeighborCount() {
        new HnswIndex(1, 42);
    }

    @Test
    public void testDeterministicLevelAssignment() {
        HnswIndex first = new HnswIndex(4, 42);
        HnswIndex second = new HnswIndex(4, 42);

        for (int i = 0; i < 20; i++) {
            double[] values = {i, i * 2.0};

            first.add(new Vector("V" + i, values));
            second.add(new Vector("V" + i, values));
        }

        assertEquals(first.getMaxLevel(), second.getMaxLevel());
    }
    
    @Test
    public void testInsertedNodesHaveNeighborConnections() {
        HnswIndex index = new HnswIndex(4, 42);

        index.add(new Vector("A", new double[]{0, 0}));
        index.add(new Vector("B", new double[]{1, 1}));
        index.add(new Vector("C", new double[]{2, 2}));

        assertTrue(index.getNeighborCount("A", 0) > 0);
        assertTrue(index.getNeighborCount("B", 0) > 0);
        assertTrue(index.getNeighborCount("C", 0) > 0);
    }

    @Test
    public void testNeighborCountDoesNotExceedLimit() {
        int maxNeighbors = 2;
        HnswIndex index = new HnswIndex(maxNeighbors, 42);

        for (int i = 0; i < 20; i++) {
            index.add(new Vector(
                    "V" + i,
                    new double[]{i, i * 2.0}));
        }

        for (int i = 0; i < 20; i++) {
            String id = "V" + i;
            assertTrue(
                    index.getNeighborCount(id, 0) <= maxNeighbors);
        }
    }
    
    @Test
    public void testSearchReturnsNearestVector() {
        HnswIndex index = new HnswIndex(4, 42);

        index.add(new Vector("A", new double[]{1, 1}));
        index.add(new Vector("B", new double[]{5, 5}));
        index.add(new Vector("C", new double[]{2, 2}));

        List<SearchResult> results = index.search(
                new Vector("query", new double[]{1.1, 1.1}),
                1, 10);

        assertEquals(1, results.size());
        assertEquals("A", results.get(0).getVector().getId());
    }

    @Test
    public void testSearchReturnsResultsInDistanceOrder() {
        HnswIndex index = new HnswIndex(4, 42);

        index.add(new Vector("A", new double[]{1, 1}));
        index.add(new Vector("B", new double[]{5, 5}));
        index.add(new Vector("C", new double[]{2, 2}));

        List<SearchResult> results = index.search(
                new Vector("query", new double[]{0, 0}),
                3, 10);

        assertEquals(3, results.size());

        for (int i = 1; i < results.size(); i++) {
            assertTrue(
                    results.get(i - 1).getDistance()
                            <= results.get(i).getDistance());
        }
    }

    @Test
    public void testSearchOnEmptyIndex() {
        HnswIndex index = new HnswIndex(4, 42);

        List<SearchResult> results = index.search(
                new Vector("query", new double[]{1, 2}),
                3, 10);

        assertTrue(results.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSearchRejectsWrongDimensions() {
        HnswIndex index = new HnswIndex(4, 42);
        index.add(new Vector("A", new double[]{1, 2}));

        index.search(
                new Vector("query", new double[]{1, 2, 3}),
                1, 10);
    }
        
    @Test
    public void testRecallAgainstBruteForce() {
        HnswIndex hnsw = new HnswIndex(8, 42);
        VectorStore store = new VectorStore();
        Random random = new Random(123);

        // Build a dataset of 200 two-dimensional vectors.
        for (int i = 0; i < 200; i++) {
            Vector vector = new Vector(
                    "V" + i,
                    new double[]{
                            random.nextDouble() * 100,
                            random.nextDouble() * 100
                    });

            hnsw.add(vector);
            store.add(vector);
        }

        NearestNeighborSearch bruteForce =
                new NearestNeighborSearch(store);

        int totalMatches = 0;
        int totalExpected = 0;
        int k = 3;

        // Compare both algorithms on 30 deterministic queries.
        for (int i = 0; i < 30; i++) {
            Vector query = new Vector(
                    "Q" + i,
                    new double[]{
                            random.nextDouble() * 100,
                            random.nextDouble() * 100
                    });

            List<SearchResult> exact =
                    bruteForce.search(query, k);

            List<SearchResult> approximate =
                    hnsw.search(query, k, 30);

            for (SearchResult expected : exact) {
                for (SearchResult actual : approximate) {
                    if (expected.getVector().getId()
                            .equals(actual.getVector().getId())) {
                        totalMatches++;
                        break;
                    }
                }
            }

            totalExpected += exact.size();
        }

        double recall =
                (double) totalMatches / totalExpected;

        System.out.printf(
                "%nHNSW Recall@3: %.2f%%%n",
                recall * 100);

        assertTrue(
                "Expected recall of at least 70%",
                recall >= 0.70);
    }

}

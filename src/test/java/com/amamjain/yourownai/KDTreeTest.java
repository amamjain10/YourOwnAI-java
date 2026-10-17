
package com.amamjain.yourownai;

import org.junit.Test;
import java.util.ArrayList;
import java.util.Random;
import java.util.List;

import static org.junit.Assert.*;

public class KDTreeTest {

    @Test
    public void testTreeSize() {
        List<Vector> vectors = List.of(
                new Vector("A", new double[]{1, 2}),
                new Vector("B", new double[]{4, 5}),
                new Vector("C", new double[]{2, 3})
        );

        KDTree tree = new KDTree(vectors);

        assertEquals(3, tree.size());
    }

    @Test
    public void testNearestNeighbor() {
        List<Vector> vectors = List.of(
                new Vector("A", new double[]{1, 1}),
                new Vector("B", new double[]{5, 5}),
                new Vector("C", new double[]{2, 2}),
                new Vector("D", new double[]{8, 8})
        );

        KDTree tree = new KDTree(vectors);
        Vector query = new Vector("query", new double[]{2.1, 2.1});

        List<SearchResult> results = tree.search(query, 2);

        assertEquals(2, results.size());
        assertEquals("C", results.get(0).getVector().getId());
        assertEquals("A", results.get(1).getVector().getId());
    }

    @Test
    public void testResultsMatchBruteForce() {
        List<Vector> vectors = List.of(
                new Vector("A", new double[]{1, 2}),
                new Vector("B", new double[]{4, 5}),
                new Vector("C", new double[]{2, 3}),
                new Vector("D", new double[]{7, 1}),
                new Vector("E", new double[]{3, 6})
        );

        Vector query = new Vector("query", new double[]{3, 3});

        KDTree tree = new KDTree(vectors);

        VectorStore store = new VectorStore();
        for (Vector vector : vectors) {
            store.add(vector);
        }

        NearestNeighborSearch bruteForce =
                new NearestNeighborSearch(store);

        List<SearchResult> kdResults = tree.search(query, 3);
        List<SearchResult> exactResults = bruteForce.search(query, 3);

        assertEquals(exactResults.size(), kdResults.size());

        for (int i = 0; i < exactResults.size(); i++) {
            assertEquals(
                    exactResults.get(i).getVector().getId(),
                    kdResults.get(i).getVector().getId());

            assertEquals(
                    exactResults.get(i).getDistance(),
                    kdResults.get(i).getDistance(),
                    1e-9);
        }
    }

    @Test
    public void testEmptyTree() {
        KDTree tree = new KDTree(List.of());

        assertEquals(0, tree.size());
        assertTrue(
                tree.search(new Vector("query", new double[]{1, 2}), 3)
                        .isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRejectsMismatchedDimensions() {
        new KDTree(List.of(
                new Vector("A", new double[]{1, 2}),
                new Vector("B", new double[]{1, 2, 3})
        ));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRejectsMismatchedQueryDimensions() {
        KDTree tree = new KDTree(List.of(
                new Vector("A", new double[]{1, 2}),
                new Vector("B", new double[]{3, 4})
        ));

        tree.search(new Vector("query", new double[]{1, 2, 3}), 1);
    }

    
@Test
public void testRandomizedResultsMatchBruteForce() {
    Random random = new Random(42);
    List<Vector> vectors = new ArrayList<>();

    for (int i = 0; i < 200; i++) {
        double[] values = new double[3];

        for (int j = 0; j < values.length; j++) {
            values[j] = random.nextDouble() * 100;
        }

        vectors.add(new Vector("vector-" + i, values));
    }

    KDTree tree = new KDTree(vectors);

    VectorStore store = new VectorStore();
    for (Vector vector : vectors) {
        store.add(vector);
    }

    NearestNeighborSearch bruteForce =
            new NearestNeighborSearch(store);

    for (int trial = 0; trial < 30; trial++) {
        double[] queryValues = new double[3];

        for (int j = 0; j < queryValues.length; j++) {
            queryValues[j] = random.nextDouble() * 100;
        }

        Vector query = new Vector("query-" + trial, queryValues);

        List<SearchResult> expected = bruteForce.search(query, 5);
        List<SearchResult> actual = tree.search(query, 5);

        assertEquals(expected.size(), actual.size());

        for (int i = 0; i < expected.size(); i++) {
            assertEquals(
                    "Mismatch at trial " + trial + ", result " + i,
                    expected.get(i).getVector().getId(),
                    actual.get(i).getVector().getId());

            assertEquals(
                    expected.get(i).getDistance(),
                    actual.get(i).getDistance(),
                    1e-9);
        }
    }
}

}

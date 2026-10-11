
package com.amamjain.yourownai;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

public class NearestNeighborSearchTest {

    @Test
    public void testSearchReturnsNearestVectorFirst() {
        VectorStore store = new VectorStore();

        store.add(new Vector("far", new double[]{10, 10}));
        store.add(new Vector("near", new double[]{1, 1}));
        store.add(new Vector("medium", new double[]{3, 3}));

        NearestNeighborSearch search =
            new NearestNeighborSearch(store);

        List<SearchResult> results =
            search.search(new Vector("query",
                new double[]{0, 0}), 3);

        assertEquals(3, results.size());
        assertEquals("near",
            results.get(0).getVector().getId());
        assertEquals("medium",
            results.get(1).getVector().getId());
        assertEquals("far",
            results.get(2).getVector().getId());
    }

    @Test
    public void testSearchReturnsOnlyTopK() {
        VectorStore store = new VectorStore();

        store.add(new Vector("A", new double[]{1, 0}));
        store.add(new Vector("B", new double[]{2, 0}));
        store.add(new Vector("C", new double[]{3, 0}));

        NearestNeighborSearch search =
            new NearestNeighborSearch(store);

        List<SearchResult> results =
            search.search(new Vector("query",
                new double[]{0, 0}), 2);

        assertEquals(2, results.size());
        assertEquals("A",
            results.get(0).getVector().getId());
    }

    @Test
    public void testEmptyStoreReturnsEmptyList() {
        VectorStore store = new VectorStore();

        NearestNeighborSearch search =
            new NearestNeighborSearch(store);

        assertTrue(search.search(
            new Vector("query", new double[]{1, 2}), 3
        ).isEmpty());
    }

    @Test
    public void testKMustBePositive() {
        NearestNeighborSearch search =
            new NearestNeighborSearch(new VectorStore());

        try {
            search.search(new Vector("query",
                new double[]{1, 2}), 0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            // Expected result.
        }
    }

    @Test
    public void testQueryDimensionMismatchIsRejected() {
        VectorStore store = new VectorStore();
        store.add(new Vector("A", new double[]{1, 2}));

        NearestNeighborSearch search =
            new NearestNeighborSearch(store);

        try {
            search.search(new Vector("query",
                new double[]{1, 2, 3}), 1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            // Expected result.
        }
    }
}

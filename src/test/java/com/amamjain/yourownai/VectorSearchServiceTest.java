
package com.amamjain.yourownai;

import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class VectorSearchServiceTest {

    @Test
    public void shouldAddAndRetrieveVector() {
        VectorStore store = new VectorStore();
        VectorSearchService service = new VectorSearchService(store);

        Vector vector = new Vector("A", new double[]{1, 2});
        service.addVector(vector);

        Optional<Vector> result = service.getVector("A");

        assertTrue(result.isPresent());
        assertEquals("A", result.get().getId());
    }

    @Test
    public void shouldRemoveVector() {
        VectorStore store = new VectorStore();
        VectorSearchService service = new VectorSearchService(store);

        service.addVector(new Vector("A", new double[]{1, 2}));

        assertTrue(service.removeVector("A"));
        assertEquals(0, service.getVectorCount());
    }

    @Test
    public void shouldReturnVectorCount() {
        VectorStore store = new VectorStore();
        VectorSearchService service = new VectorSearchService(store);

        service.addVector(new Vector("A", new double[]{1, 2}));
        service.addVector(new Vector("B", new double[]{3, 4}));

        assertEquals(2, service.getVectorCount());
    }

    @Test
    public void shouldSearchUsingSelectedMetric() {
        VectorStore store = new VectorStore();
        VectorSearchService service = new VectorSearchService(store);

        service.addVector(new Vector("A", new double[]{1, 2}));
        service.addVector(new Vector("B", new double[]{5, 5}));

        List<SearchResult> results = service.search(
                new Vector("query", new double[]{1, 2}),
                2,
                SearchMetric.EUCLIDEAN
        );

        assertEquals(2, results.size());
        assertEquals("A", results.get(0).getVector().getId());
        assertEquals(0.0, results.get(0).getDistance(), 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectDuplicateVectorId() {
        VectorStore store = new VectorStore();
        VectorSearchService service = new VectorSearchService(store);

        service.addVector(new Vector("A", new double[]{1, 2}));
        service.addVector(new Vector("A", new double[]{3, 4}));
    }
}

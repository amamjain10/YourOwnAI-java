
package com.amamjain.yourownai;

import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class VectorStoreTest {

    @Test
    public void shouldAddAndRetrieveVector() {
        VectorStore store = new VectorStore();
        Vector vector = new Vector("v1", new double[]{1.0, 2.0});

        store.add(vector);

        Optional<Vector> result = store.get("v1");

        assertTrue(result.isPresent());
        assertEquals("v1", result.get().getId());
        assertEquals(1, store.size());
    }

    @Test
    public void shouldRejectDuplicateIds() {
        VectorStore store = new VectorStore();

        store.add(new Vector("v1", new double[]{1.0}));

        try {
            store.add(new Vector("v1", new double[]{2.0}));
            fail("Expected duplicate ID to be rejected");
        } catch (IllegalArgumentException expected) {
            assertEquals(1, store.size());
        }
    }

    @Test
    public void shouldReturnEmptyForMissingId() {
        VectorStore store = new VectorStore();

        assertFalse(store.get("missing").isPresent());
    }

    @Test
    public void shouldCheckWhetherIdExists() {
        VectorStore store = new VectorStore();
        store.add(new Vector("v1", new double[]{1.0}));

        assertTrue(store.contains("v1"));
        assertFalse(store.contains("v2"));
    }

    @Test
    public void shouldRemoveVector() {
        VectorStore store = new VectorStore();
        store.add(new Vector("v1", new double[]{1.0}));

        assertTrue(store.remove("v1"));
        assertFalse(store.remove("v1"));
        assertEquals(0, store.size());
    }

    @Test
    public void shouldReturnAllVectors() {
        VectorStore store = new VectorStore();

        store.add(new Vector("v1", new double[]{1.0}));
        store.add(new Vector("v2", new double[]{2.0}));

        List<Vector> vectors = store.getAll();

        assertEquals(2, vectors.size());
        assertEquals("v1", vectors.get(0).getId());
        assertEquals("v2", vectors.get(1).getId());
    }

    @Test
    public void shouldNotAllowChangesToReturnedList() {
        VectorStore store = new VectorStore();
        store.add(new Vector("v1", new double[]{1.0}));

        List<Vector> vectors = store.getAll();

        try {
            vectors.clear();
            fail("Expected an immutable list");
        } catch (UnsupportedOperationException expected) {
            assertEquals(1, store.size());
        }
    }
}


package com.amamjain.yourownai;

import org.junit.Test;

import static org.junit.Assert.*;

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

}

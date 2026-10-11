
package com.amamjain.yourownai;

import org.junit.Test;
import static org.junit.Assert.*;

public class VectorMetricsTest {

    @Test
    public void testEuclideanDistance() {
        Vector a = new Vector("A", new double[]{1, 2});
        Vector b = new Vector("B", new double[]{4, 6});

        assertEquals(5.0,
            VectorMetrics.euclideanDistance(a, b), 1e-9);
    }
    
    @Test
    public void testManhattanDistance() {
    Vector a = new Vector("A", new double[]{1, 2});
    Vector b = new Vector("B", new double[]{4, 6});

    assertEquals(7.0,
        VectorMetrics.manhattanDistance(a, b), 1e-9);
    }


    @Test
    public void testDistanceBetweenIdenticalVectors() {
        Vector a = new Vector("A", new double[]{1, 2, 3});
        Vector b = new Vector("B", new double[]{1, 2, 3});

        assertEquals(0.0,
            VectorMetrics.euclideanDistance(a, b), 1e-9);
    }

    @Test
    public void testCosineSimilarityIdenticalDirections() {
        Vector a = new Vector("A", new double[]{1, 2});
        Vector b = new Vector("B", new double[]{2, 4});

        assertEquals(1.0,
            VectorMetrics.cosineSimilarity(a, b), 1e-9);
    }

    @Test
    public void testCosineSimilarityPerpendicularVectors() {
        Vector a = new Vector("A", new double[]{1, 0});
        Vector b = new Vector("B", new double[]{0, 1});

        assertEquals(0.0,
            VectorMetrics.cosineSimilarity(a, b), 1e-9);
    }

    @Test
    public void testDifferentDimensionsAreRejected() {
        Vector a = new Vector("A", new double[]{1, 2});
        Vector b = new Vector("B", new double[]{1, 2, 3});

        try {
            VectorMetrics.euclideanDistance(a, b);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            // Expected result.
        }
    }

    @Test
    public void testZeroVectorRejectedForCosineSimilarity() {
        Vector a = new Vector("A", new double[]{0, 0});
        Vector b = new Vector("B", new double[]{1, 2});

        try {
            VectorMetrics.cosineSimilarity(a, b);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            // Expected result.
        }
    }
        
    @Test
    public void testManhattanDistanceRejectsDifferentDimensions() {
    Vector a = new Vector("A", new double[]{1, 2});
    Vector b = new Vector("B", new double[]{1, 2, 3});

    try {
        VectorMetrics.manhattanDistance(a, b);
        fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException expected) {
        // Expected result.
    }
}

}

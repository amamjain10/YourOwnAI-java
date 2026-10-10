
package com.amamjain.yourownai;

import org.junit.Test;
import static org.junit.Assert.*;

public class VectorTest {

    @Test
    public void shouldCreateValidVector() {
        Vector vector = new Vector("v1", new double[]{1.0, 2.0, 3.0});

        assertEquals("v1", vector.getId());
        assertEquals(3, vector.dimension());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectBlankId() {
        new Vector(" ", new double[]{1.0, 2.0});
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectEmptyVector() {
        new Vector("v1", new double[]{});
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectNaN() {
        new Vector("v1", new double[]{1.0, Double.NaN});
    }

    @Test
    public void shouldProtectInternalValues() {
        double[] original = {1.0, 2.0};
        Vector vector = new Vector("v1", original);

        original[0] = 99.0;
        assertEquals(1.0, vector.getValues()[0], 0.000001);

        double[] returned = vector.getValues();
        returned[0] = 88.0;
        assertEquals(1.0, vector.getValues()[0], 0.000001);
    }
}

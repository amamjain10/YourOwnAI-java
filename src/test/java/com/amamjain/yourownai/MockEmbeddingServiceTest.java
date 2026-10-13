
package com.amamjain.yourownai;

import org.junit.Test;

import static org.junit.Assert.*;

public class MockEmbeddingServiceTest {

    @Test
    public void shouldReturnEmbeddingForValidText() {
        EmbeddingService service = new MockEmbeddingService();

        double[] embedding = service.embed("Java programming");

        assertArrayEquals(
                new double[]{0.1, 0.2, 0.3},
                embedding,
                0.000001
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectBlankText() {
        EmbeddingService service = new MockEmbeddingService();

        service.embed("   ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectNullText() {
        EmbeddingService service = new MockEmbeddingService();

        service.embed(null);
    }
}

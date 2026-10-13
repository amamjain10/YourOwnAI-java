package com.amamjain.yourownai;

public class MockEmbeddingService implements EmbeddingService {

    @Override
    public double[] embed(String text) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Text cannot be null or blank");
        }

        return new double[] {0.1,0.2,0.3};
    }
}
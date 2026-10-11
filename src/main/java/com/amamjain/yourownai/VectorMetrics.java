
package com.amamjain.yourownai;

public final class VectorMetrics {

    private VectorMetrics() {
        // Utility class: prevent object creation.
    }

    public static double euclideanDistance(Vector a, Vector b) {
        validateVectors(a, b);

        double[] valuesA = a.getValues();
        double[] valuesB = b.getValues();
        double sum = 0.0;

        for (int i = 0; i < valuesA.length; i++) {
            double difference = valuesA[i] - valuesB[i];
            sum += difference * difference;
        }

        return Math.sqrt(sum);
    }
    
    public static double manhattanDistance(Vector a, Vector b) {
    validateVectors(a, b);

    double[] valuesA = a.getValues();
    double[] valuesB = b.getValues();
    double sum = 0.0;

    for (int i = 0; i < valuesA.length; i++) {
        sum += Math.abs(valuesA[i] - valuesB[i]);
    }

    return sum;
    }

    public static double cosineSimilarity(Vector a, Vector b) {
        validateVectors(a, b);

        double[] valuesA = a.getValues();
        double[] valuesB = b.getValues();

        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;

        for (int i = 0; i < valuesA.length; i++) {
            dotProduct += valuesA[i] * valuesB[i];
            normA += valuesA[i] * valuesA[i];
            normB += valuesB[i] * valuesB[i];
        }

        if (normA == 0.0 || normB == 0.0) {
            throw new IllegalArgumentException(
                "Cosine similarity is undefined for zero vectors"
            );
        }

        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }

    private static void validateVectors(Vector a, Vector b) {
        if (a == null || b == null) {
            throw new IllegalArgumentException(
                "Vectors cannot be null"
            );
        }

        if (a.dimension() != b.dimension()) {
            throw new IllegalArgumentException(
                "Vectors must have the same dimensions"
            );
        }
    }
}

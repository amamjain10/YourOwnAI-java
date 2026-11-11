
package com.amamjain.yourownai;

public final class SearchResult {

    private final Vector vector;
    private final double distance;
    private final DocumentMetadata metadata;

    // Backward-compatible constructor.
    public SearchResult(Vector vector, double distance) {
        this(vector, distance, null);
    }

    // Constructor with source metadata.
    public SearchResult(
            Vector vector,
            double distance,
            DocumentMetadata metadata) {

        if (vector == null) {
            throw new IllegalArgumentException(
                    "Vector cannot be null");
        }

        if (!Double.isFinite(distance) || distance < 0.0) {
            throw new IllegalArgumentException(
                    "Distance must be finite and non-negative");
        }

        this.vector = vector;
        this.distance = distance;
        this.metadata = metadata;
    }

    public Vector getVector() {
        return vector;
    }

    public double getDistance() {
        return distance;
    }

    public DocumentMetadata getMetadata() {
        return metadata;
    }

    public boolean hasSource() {
        return metadata != null;
    }

    @Override
    public String toString() {
        return "SearchResult{vectorId='"
                + vector.getId()
                + "', distance=" + distance
                + ", metadata=" + metadata
                + "}";
    }
}

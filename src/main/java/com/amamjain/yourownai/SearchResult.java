
package com.amamjain.yourownai;

public final class SearchResult {

    private final Vector vector;
    private final double distance;

    public SearchResult(Vector vector, double distance) {
        if (vector == null) {
            throw new IllegalArgumentException(
                "Vector cannot be null"
            );
        }

        if (!Double.isFinite(distance) || distance < 0.0) {
            throw new IllegalArgumentException(
                "Distance must be finite and non-negative"
            );
        }

        this.vector = vector;
        this.distance = distance;
    }

    public Vector getVector() {
        return vector;
    }

    public double getDistance() {
        return distance;
    }

    @Override
    public String toString() {
        return "SearchResult{vectorId='" + vector.getId()
            + "', distance=" + distance + "}";
    }
}

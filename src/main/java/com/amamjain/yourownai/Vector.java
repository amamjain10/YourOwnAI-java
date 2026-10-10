
package com.amamjain.yourownai;

import java.util.Arrays;
import java.util.Objects;

/**
 * Represents an immutable numerical vector.
 * Used as the foundation for vector storage and similarity search.
 */
public final class Vector {

    private final String id;
    private final double[] values;

    public Vector(String id, double[] values) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Vector ID cannot be blank");
        }

        Objects.requireNonNull(values, "Vector values cannot be null");

        if (values.length == 0) {
            throw new IllegalArgumentException("Vector cannot be empty");
        }

        for (double value : values) {
            if (!Double.isFinite(value)) {
                throw new IllegalArgumentException(
                    "Vector values must be finite numbers"
                );
            }
        }

        this.id = id;
        this.values = Arrays.copyOf(values, values.length);
    }

    public String getId() {
        return id;
    }

    public double[] getValues() {
        return Arrays.copyOf(values, values.length);
    }

    public int dimension() {
        return values.length;
    }

    @Override
    public String toString() {
        return "Vector{id='" + id + "', dimension=" + dimension() + "}";
    }
}

package com.amamjain.yourownai;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Stores vectors by their unique IDs.
 */
public class VectorStore {

    private final Map<String, Vector> vectors = new LinkedHashMap<>();

    /**
     * Adds a vector to the store.
     * Duplicate IDs are not allowed.
     */
    public void add(Vector vector) {
        Objects.requireNonNull(vector, "Vector cannot be null");

        String id = vector.getId();

        if (vectors.containsKey(id)) {
            throw new IllegalArgumentException(
                "Vector ID already exists: " + id
            );
        }

        vectors.put(id, vector);
    }

    /**
     * Retrieves a vector by ID.
     */
    public Optional<Vector> get(String id) {
        validateId(id);
        return Optional.ofNullable(vectors.get(id));
    }

    /**
     * Checks whether an ID exists.
     */
    public boolean contains(String id) {
        validateId(id);
        return vectors.containsKey(id);
    }

    /**
     * Removes a vector by ID.
     *
     * @return true if a vector was removed.
     */
    public boolean remove(String id) {
        validateId(id);
        return vectors.remove(id) != null;
    }

    /**
     * Returns the number of stored vectors.
     */
    public int size() {
        return vectors.size();
    }

    /**
     * Returns a snapshot of all stored vectors.
     */
    public List<Vector> getAll() {
        return List.copyOf(new ArrayList<>(vectors.values()));
    }

    private void validateId(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException(
                "Vector ID cannot be blank"
            );
        }
    }
}

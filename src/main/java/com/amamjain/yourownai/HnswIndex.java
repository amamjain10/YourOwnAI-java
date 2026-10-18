
package com.amamjain.yourownai;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

/**
 * Foundation for a Hierarchical Navigable Small World index.
 * Day 22: graph structure and insertion.
 */
public class HnswIndex {

    private static class Node {
        private final Vector vector;
        private final int level;
        private final List<List<Node>> neighbors;

        private Node(Vector vector, int level) {
            this.vector = vector;
            this.level = level;
            this.neighbors = new ArrayList<>();

            for (int i = 0; i <= level; i++) {
                neighbors.add(new ArrayList<>());
            }
        }
    }

    private final Map<String, Node> nodes = new HashMap<>();
    private final int maxNeighbors;
    private final Random random;

    private int dimensions = -1;
    private int maxLevel = -1;
    private Node entryPoint;

    public HnswIndex(int maxNeighbors, long randomSeed) {
        if (maxNeighbors < 2) {
            throw new IllegalArgumentException(
                    "maxNeighbors must be at least 2");
        }

        this.maxNeighbors = maxNeighbors;
        this.random = new Random(randomSeed);
    }

    public int size() {
        return nodes.size();
    }

    public int getMaxLevel() {
        return maxLevel;
    }
    
    public void add(Vector vector) {
        Objects.requireNonNull(vector, "Vector cannot be null");

        if (nodes.containsKey(vector.getId())) {
            throw new IllegalArgumentException(
                    "Duplicate vector ID: " + vector.getId());
        }

        if (dimensions == -1) {
            dimensions = vector.dimension();
        } else if (vector.dimension() != dimensions) {
            throw new IllegalArgumentException(
                    "All vectors must have the same dimension");
        }

        int level = randomLevel();
        Node newNode = new Node(vector, level);

        if (entryPoint == null) {
            entryPoint = newNode;
            maxLevel = level;
            nodes.put(vector.getId(), newNode);
            return;
        }

        nodes.put(vector.getId(), newNode);

        for (int layer = 0; layer <= level; layer++) {
            List<Node> candidates = new ArrayList<>();

            for (Node existing : nodes.values()) {
                if (existing != newNode && existing.level >= layer) {
                    candidates.add(existing);
                }
            }

            candidates.sort((a, b) ->
                    Double.compare(
                            VectorMetrics.euclideanDistance(
                                    vector, a.vector),
                            VectorMetrics.euclideanDistance(
                                    vector, b.vector)));

            int connections = Math.min(maxNeighbors, candidates.size());

            for (int i = 0; i < connections; i++) {
                Node neighbor = candidates.get(i);

                newNode.neighbors.get(layer).add(neighbor);
                neighbor.neighbors.get(layer).add(newNode);

                pruneNeighbors(neighbor, layer);
            }
        }

        if (level > maxLevel) {
            maxLevel = level;
            entryPoint = newNode;
        }
    }

    private int randomLevel() {
        int level = 0;

        while (random.nextDouble() < 0.5 && level < 16) {
            level++;
        }

        return level;
    }

    private void pruneNeighbors(Node node, int layer) {
        List<Node> neighbors = node.neighbors.get(layer);

        if (neighbors.size() <= maxNeighbors) {
            return;
        }

        neighbors.sort((a, b) ->
                Double.compare(
                        VectorMetrics.euclideanDistance(
                                node.vector, a.vector),
                        VectorMetrics.euclideanDistance(
                                node.vector, b.vector)));

        while (neighbors.size() > maxNeighbors) {
            neighbors.remove(neighbors.size() - 1);
        }
    }
    
    // Package-private helper for testing graph connections.
    int getNeighborCount(String vectorId, int layer) {
        Node node = nodes.get(vectorId);

        if (node == null) {
            throw new IllegalArgumentException(
                    "Unknown vector ID: " + vectorId);
        }

        if (layer < 0 || layer >= node.neighbors.size()) {
            throw new IllegalArgumentException(
                    "Invalid layer for vector: " + vectorId);
        }

        return node.neighbors.get(layer).size();
    }

}

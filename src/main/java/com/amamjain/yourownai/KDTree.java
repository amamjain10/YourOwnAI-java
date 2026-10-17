
package com.amamjain.yourownai;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.PriorityQueue;

/**
 * KD-tree for Euclidean nearest-neighbor search.
 *
 * Builds a balanced tree by splitting vectors along alternating dimensions.
 * Search uses branch pruning to avoid visiting regions that cannot contain
 * a closer result.
 */
public class KDTree {

    private final Node root;
    private final int dimensions;
    private final int size;

    private static class Node {
        private final Vector vector;
        private final int axis;
        private final Node left;
        private final Node right;

        private Node(Vector vector, int axis, Node left, Node right) {
            this.vector = vector;
            this.axis = axis;
            this.left = left;
            this.right = right;
        }
    }

    public KDTree(List<Vector> vectors) {
        Objects.requireNonNull(vectors, "Vectors cannot be null");

        List<Vector> copy = new ArrayList<>(vectors);

        if (copy.isEmpty()) {
            this.root = null;
            this.dimensions = 0;
            this.size = 0;
            return;
        }

        for (Vector vector : copy) {
            Objects.requireNonNull(vector, "Vector cannot be null");
        }

        this.dimensions = copy.get(0).dimension();

        for (Vector vector : copy) {
            if (vector.dimension() != dimensions) {
                throw new IllegalArgumentException(
                        "All vectors must have the same dimension");
            }
        }

        this.size = copy.size();
        this.root = build(copy, 0);
    }

    private Node build(List<Vector> vectors, int depth) {
        if (vectors.isEmpty()) {
            return null;
        }

        int axis = depth % dimensions;

        vectors.sort(
                Comparator.comparingDouble(
                        (Vector vector) -> vector.getValues()[axis])
                        .thenComparing(Vector::getId));

        int middle = vectors.size() / 2;
        Vector median = vectors.get(middle);

        List<Vector> leftVectors =
                new ArrayList<>(vectors.subList(0, middle));

        List<Vector> rightVectors =
                new ArrayList<>(
                        vectors.subList(middle + 1, vectors.size()));

        return new Node(
                median,
                axis,
                build(leftVectors, depth + 1),
                build(rightVectors, depth + 1));
    }

    public int size() {
        return size;
    }

    public List<SearchResult> search(Vector query, int k) {
        Objects.requireNonNull(query, "Query vector cannot be null");

        if (k <= 0) {
            throw new IllegalArgumentException(
                    "k must be greater than zero");
        }

        if (root == null) {
            return List.of();
        }

        if (query.dimension() != dimensions) {
            throw new IllegalArgumentException(
                    "Query dimension must match indexed vectors");
        }

        Comparator<SearchResult> bestFirst =
                Comparator.comparingDouble(SearchResult::getDistance)
                        .thenComparing(
                                result -> result.getVector().getId());

        // The worst retained result is at the head of this heap.
        PriorityQueue<SearchResult> bestResults =
                new PriorityQueue<>(bestFirst.reversed());

        searchNode(root, query, k, bestResults, bestFirst);

        List<SearchResult> results = new ArrayList<>(bestResults);
        results.sort(bestFirst);

        return List.copyOf(results);
    }

    private void searchNode(
            Node node,
            Vector query,
            int k,
            PriorityQueue<SearchResult> bestResults,
            Comparator<SearchResult> bestFirst) {

        if (node == null) {
            return;
        }

        double[] queryValues = query.getValues();
        double[] nodeValues = node.vector.getValues();

        double distance =
                VectorMetrics.euclideanDistance(query, node.vector);

        SearchResult result = new SearchResult(node.vector, distance);

        if (bestResults.size() < k) {
            bestResults.offer(result);
        } else if (bestFirst.compare(
                result, bestResults.peek()) < 0) {
            bestResults.poll();
            bestResults.offer(result);
        }

        double difference =
                queryValues[node.axis] - nodeValues[node.axis];

        Node nearBranch =
                difference <= 0 ? node.left : node.right;

        Node farBranch =
                difference <= 0 ? node.right : node.left;

        searchNode(nearBranch, query, k, bestResults, bestFirst);

        double worstDistance = bestResults.size() < k
                ? Double.POSITIVE_INFINITY
                : bestResults.peek().getDistance();

        // Explore the far branch if its splitting plane could contain
        // a result closer than the current worst retained candidate.
        if (Math.abs(difference) <= worstDistance) {
            searchNode(farBranch, query, k, bestResults, bestFirst);
        }
    }
}


package com.amamjain.yourownai;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TextChunker {

    private final int chunkSize;
    private final int overlap;

    public TextChunker(int chunkSize, int overlap) {
        if (chunkSize <= 0) {
            throw new IllegalArgumentException(
                    "Chunk size must be greater than zero");
        }

        if (overlap < 0 || overlap >= chunkSize) {
            throw new IllegalArgumentException(
                    "Overlap must be non-negative and smaller than chunk size");
        }

        this.chunkSize = chunkSize;
        this.overlap = overlap;
    }

    public List<String> chunkText(String text) {
        Objects.requireNonNull(text, "Text cannot be null");

        List<String> chunks = new ArrayList<>();

        if (text.isBlank()) {
            return List.of();
        }

        int start = 0;

        while (start < text.length()) {
            int end = Math.min(start + chunkSize, text.length());
            chunks.add(text.substring(start, end));

            if (end == text.length()) {
                break;
            }

            start = end - overlap;
        }

        return List.copyOf(chunks);
    }
}

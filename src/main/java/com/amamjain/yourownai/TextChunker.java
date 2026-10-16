
package com.amamjain.yourownai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        if (text == null || text.isBlank()) {
            return Collections.emptyList();
        }

        List<String> chunks = new ArrayList<>();
        int start = 0;
        int length = text.length();

        while (start < length) {
            int end = Math.min(start + chunkSize, length);

            if (end < length) {
                int preferredEnd = findSentenceBoundary(
                        text, start, end);

                if (preferredEnd > start) {
                    end = preferredEnd;
                } else {
                    int whitespace = end;

                    while (whitespace > start
                            && !Character.isWhitespace(
                                    text.charAt(whitespace - 1))) {
                        whitespace--;
                    }

                    if (whitespace > start) {
                        end = whitespace;
                    } else {
                        // A single word is longer than the chunk size.
                        // Extend to the next word boundary rather than
                        // splitting that word.
                        while (end < length
                                && !Character.isWhitespace(
                                        text.charAt(end))) {
                            end++;
                        }
                    }
                }
            }

            String chunk = text.substring(start, end).trim();

            if (!chunk.isEmpty()) {
                chunks.add(chunk);
            }

            if (end >= length) {
                break;
            }
            
            int nextStart = Math.max(start + 1, end - overlap);

            // Prefer a sentence boundary within the overlap region.
            int sentenceStart = findNextSentenceStart(
                    text, nextStart, end);

            if (sentenceStart > nextStart) {
                nextStart = sentenceStart;
            } else {
                // Fall back to a word boundary.
                while (nextStart < end
                        && !Character.isWhitespace(text.charAt(nextStart))) {
                    nextStart++;
                }
            }

            // int nextStart = Math.max(start + 1, end - overlap);

            // // Start the next chunk at a word boundary.
            // while (nextStart < end
            //         && !Character.isWhitespace(
            //                 text.charAt(nextStart))) {
            //     nextStart++;
            // }

            while (nextStart < length
                    && Character.isWhitespace(
                            text.charAt(nextStart))) {
                nextStart++;
            }

            if (nextStart <= start || nextStart >= length) {
                break;
            }

            start = nextStart;
        }

        return Collections.unmodifiableList(chunks);
    }
    
        private int findNextSentenceStart(
                String text, int start, int end) {

            for (int i = start; i < end; i++) {
                char current = text.charAt(i);

                if (current == '.' || current == '!'
                        || current == '?') {

                    int boundary = i + 1;

                    while (boundary < text.length()
                            && Character.isWhitespace(
                                    text.charAt(boundary))) {
                        boundary++;
                    }

                    if (boundary < text.length()
                            && boundary <= end) {
                        return boundary;
                    }
                }
            }

            return -1;
        }

    private int findSentenceBoundary(
            String text, int start, int end) {

        int minimumEnd = start + (end - start) / 2;

        for (int i = end - 1; i >= minimumEnd; i--) {
            char current = text.charAt(i);

            if (current == '.' || current == '!'
                    || current == '?') {
                int boundary = i + 1;

                while (boundary < text.length()
                        && Character.isWhitespace(
                                text.charAt(boundary))) {
                    boundary++;
                }

                if (boundary <= end) {
                    return boundary;
                }
            }
        }

        return -1;
    }
}

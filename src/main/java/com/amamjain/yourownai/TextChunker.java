
package com.amamjain.yourownai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        List<String> sentences = splitSentences(text);
        List<String> chunks = new ArrayList<>();

        StringBuilder current = new StringBuilder();
        String previousSentence = "";

        for (String sentence : sentences) {
            if (current.length() == 0) {
                current.append(sentence);
                continue;
            }

            int combinedLength =
                    current.length() + 1 + sentence.length();

            if (combinedLength <= chunkSize) {
                current.append(' ').append(sentence);
            } else {
                chunks.add(current.toString().trim());

                // Repeat the previous sentence only when it fits
                // within the configured overlap.
                current.setLength(0);

                if (!previousSentence.isEmpty()
                        && previousSentence.length() <= overlap) {
                    current.append(previousSentence).append(' ');
                }

                // Avoid creating a chunk that contains only overlap.
                if (current.length() + sentence.length() > chunkSize
                        && current.length() > 0) {
                    current.setLength(0);
                }

                current.append(sentence);
            }

            previousSentence = sentence;
        }

        if (current.length() > 0) {
            chunks.add(current.toString().trim());
        }

        return Collections.unmodifiableList(chunks);
    }

    private List<String> splitSentences(String text) {
        List<String> sentences = new ArrayList<>();

        Pattern pattern = Pattern.compile(
                "[^.!?]+[.!?]+|[^.!?]+$");

        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            String sentence = matcher.group().trim();

            if (!sentence.isEmpty()) {
                sentences.add(sentence);
            }
        }

        return sentences;
    }
}

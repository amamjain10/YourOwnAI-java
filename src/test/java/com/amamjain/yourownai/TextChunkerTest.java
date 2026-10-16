
package com.amamjain.yourownai;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class TextChunkerTest {

    @Test
    public void shouldReturnEmptyListForBlankText() {
        TextChunker chunker = new TextChunker(20, 5);

        assertTrue(chunker.chunkText("   ").isEmpty());
    }

    @Test
    public void shouldKeepWordsTogetherAtChunkBoundaries() {
        TextChunker chunker = new TextChunker(20, 5);

        List<String> chunks = chunker.chunkText(
                "Java is useful. Python is powerful. "
                        + "Search finds relevant documents.");

        assertFalse(chunks.isEmpty());

        for (String chunk : chunks) {
            assertFalse(chunk.startsWith(" "));
            assertFalse(chunk.endsWith(" "));
        }

        assertTrue(chunks.stream()
                .anyMatch(chunk -> chunk.contains("Python")));
    }

    @Test
    public void shouldReturnOneChunkForShortText() {
        TextChunker chunker = new TextChunker(100, 10);

        List<String> chunks =
                chunker.chunkText("Java is useful.");

        assertEquals(1, chunks.size());
        assertEquals("Java is useful.", chunks.get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectInvalidOverlap() {
        new TextChunker(10, 10);
    }

    
    @Test
    public void shouldReturnEmptyListForNullText() {
        TextChunker chunker = new TextChunker(20, 5);

        assertTrue(chunker.chunkText(null).isEmpty());
    }

    @Test
    public void shouldReturnUnmodifiableList() {
        TextChunker chunker = new TextChunker(100, 10);

        List<String> chunks = chunker.chunkText("Java is useful.");

        try {
            chunks.add("Extra text");
            fail("Expected the returned list to be unmodifiable");
        } catch (UnsupportedOperationException expected) {
            // Expected behavior
        }
    }

    @Test
    public void shouldKeepEveryWordIntactAcrossChunks() {
        TextChunker chunker = new TextChunker(30, 8);

        String text = "Java supports programming. "
                + "Python supports machine learning. "
                + "Vector databases store embeddings.";

        List<String> chunks = chunker.chunkText(text);

        assertTrue(chunks.size() > 1);

        for (String chunk : chunks) {
            assertFalse(chunk.isBlank());
            assertFalse(chunk.startsWith(" "));
            assertFalse(chunk.endsWith(" "));
        }

        assertTrue(chunks.stream()
                .anyMatch(chunk -> chunk.contains("programming.")));
        assertTrue(chunks.stream()
                .anyMatch(chunk -> chunk.contains("learning.")));
        assertTrue(chunks.stream()
                .anyMatch(chunk -> chunk.contains("embeddings.")));
    }
        
    @Test
    public void shouldPreferSentenceBoundariesForChunkStarts() {
        TextChunker chunker = new TextChunker(30, 8);

        String text = "Java is useful for development. "
                + "Python is popular for machine learning. "
                + "Vector databases store embeddings.";

        List<String> chunks = chunker.chunkText(text);

        assertTrue(chunks.size() > 1);

        for (int i = 1; i < chunks.size(); i++) {
            String chunk = chunks.get(i);

            assertFalse(chunk.isBlank());
            assertFalse(chunk.startsWith(" "));
            assertFalse(chunk.endsWith(" "));
        }

        assertTrue(chunks.stream()
                .anyMatch(chunk -> chunk.startsWith("Python")
                        || chunk.startsWith("Vector")));
    }


}

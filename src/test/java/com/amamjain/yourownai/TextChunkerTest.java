
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
}

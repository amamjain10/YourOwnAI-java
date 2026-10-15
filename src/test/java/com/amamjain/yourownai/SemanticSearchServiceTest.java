
package com.amamjain.yourownai;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

public class SemanticSearchServiceTest {

    @Rule
    public TemporaryFolder temporaryFolder =
            new TemporaryFolder();

    @Test
    public void shouldSaveAndRestoreDocumentsAndEmbeddings()
            throws Exception {

        AtomicInteger embeddingCalls = new AtomicInteger();

        EmbeddingService mockEmbedding = text -> {
            embeddingCalls.incrementAndGet();

            if (text.toLowerCase().contains("java")) {
                return new double[]{1.0, 0.0, 0.0};
            }

            return new double[]{0.0, 1.0, 0.0};
        };

        SemanticSearchService original =
                new SemanticSearchService(mockEmbedding);

        original.addText(
                "doc1",
                "Java is a programming language.");

        original.addText(
                "doc2",
                "Python is used for data science.");

        assertEquals(2, original.size());
        assertEquals(2, embeddingCalls.get());

        Path indexPath =
                temporaryFolder.getRoot().toPath()
                        .resolve("semantic-index.json");

        original.save(indexPath);

        assertTrue(java.nio.file.Files.exists(indexPath));

        int callsBeforeRestore = embeddingCalls.get();

        SemanticSearchService restored =
                SemanticSearchService.load(
                        indexPath, mockEmbedding);

        // Restoring an index must not re-embed saved documents.
        assertEquals(callsBeforeRestore, embeddingCalls.get());

        assertEquals(2, restored.size());
        assertEquals(
                "Java is a programming language.",
                restored.getText("doc1"));

        assertEquals(
                "Python is used for data science.",
                restored.getText("doc2"));

        List<SearchResult> results =
                restored.search("Java programming", 2);

        assertEquals(2, results.size());
        assertEquals("doc1",
                results.get(0).getVector().getId());

        assertEquals(3, results.get(0).getVector()
                .getValues().length);
    }

    @Test
    public void shouldRejectDuplicateDocumentIds() {
        EmbeddingService mockEmbedding =
                text -> new double[]{1.0, 0.0};

        SemanticSearchService service =
                new SemanticSearchService(mockEmbedding);

        service.addText("doc1", "First document");

        try {
            service.addText("doc1", "Duplicate document");
            fail("Expected duplicate ID to be rejected");
        } catch (IllegalArgumentException expected) {
            assertEquals(1, service.size());
            assertEquals(
                    "First document",
                    service.getText("doc1"));
        }
    }

    @Test
    public void shouldRejectBlankText() {
        EmbeddingService mockEmbedding =
                text -> new double[]{1.0, 0.0};

        SemanticSearchService service =
                new SemanticSearchService(mockEmbedding);

        try {
            service.addText("doc1", "   ");
            fail("Expected blank text to be rejected");
        } catch (IllegalArgumentException expected) {
            assertEquals(0, service.size());
        }
    }
}

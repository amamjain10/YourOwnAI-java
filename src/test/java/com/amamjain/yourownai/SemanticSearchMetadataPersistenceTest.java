
package com.amamjain.yourownai;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.nio.file.Path;
import java.util.List;

import static org.junit.Assert.*;

public class SemanticSearchMetadataPersistenceTest {

    @Rule
    public TemporaryFolder temporaryFolder =
            new TemporaryFolder();

    @Test
    public void shouldSaveAndRestoreDocumentMetadata()
            throws Exception {

        EmbeddingService fakeEmbeddingService =
                text -> new double[]{1.0, 0.0, 0.0};

        SemanticSearchService originalService =
                new SemanticSearchService(fakeEmbeddingService);

        String chunkId = "TX200-p1-c1";

        originalService.addText(
                chunkId,
                "Coolant concentration should be 6 to 8 percent."
        );

        DocumentMetadata metadata = new DocumentMetadata(
                "TX200_Maintenance.pdf",
                1,
                1
        );

        originalService.addMetadata(chunkId, metadata);

        // Save the vector index.
        Path indexPath = temporaryFolder.getRoot()
                .toPath()
                .resolve("semantic-index.json");

        originalService.save(indexPath);

        assertTrue(indexPath.toFile().exists());

        // Load into a new service instance.
        SemanticSearchService loadedService =
                SemanticSearchService.load(
                        indexPath,
                        fakeEmbeddingService
                );

        // Verify vector restoration.
        assertEquals(1, loadedService.size());

        // Verify metadata restoration.
        DocumentMetadata restoredMetadata =
                loadedService.getMetadata(chunkId);

        assertNotNull(restoredMetadata);

        assertEquals(
                "TX200_Maintenance.pdf",
                restoredMetadata.documentName()
        );

        assertEquals(1, restoredMetadata.pageNumber());
        assertEquals(1, restoredMetadata.chunkNumber());

        // Verify search results include source metadata.
        List<SearchResult> results =
                loadedService.search("coolant concentration", 1);

        assertFalse(results.isEmpty());
        assertTrue(results.get(0).hasSource());

        assertEquals(
                "TX200_Maintenance.pdf",
                results.get(0).getMetadata().documentName()
        );

        System.out.println(
                "Metadata persistence test successful!"
        );
    }
}

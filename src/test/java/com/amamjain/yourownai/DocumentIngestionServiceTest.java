
package com.amamjain.yourownai;

import org.junit.Test;

import java.net.URL;
import java.nio.file.Path;
import java.util.List;

import static org.junit.Assert.*;

public class DocumentIngestionServiceTest {

    @Test
    public void shouldIngestPdfAndStoreChunks() throws Exception {

        // Locate the test PDF.
        URL resource = getClass().getClassLoader()
                .getResource(
                    "MNT-MAN-CNC-02_TX-200_Preventive_Maintenance.pdf"
                );

        assertNotNull("Test PDF not found", resource);

        Path pdfPath = Path.of(resource.toURI());

        // Fake embedding service: no Ollama connection required.
        EmbeddingService fakeEmbeddingService =
                text -> new double[]{1.0, 0.0, 0.0};

        // Initialize the existing RAG components.
        SemanticSearchService searchService =
                new SemanticSearchService(fakeEmbeddingService);

        PdfDocumentLoader pdfLoader =
                new PdfDocumentLoader();

        TextChunker textChunker =
                new TextChunker(800, 120);

        DocumentIngestionService ingestionService =
                new DocumentIngestionService(
                        pdfLoader,
                        textChunker,
                        searchService
                );

        // Ingest the PDF.
        List<DocumentIngestionService.IngestedChunk> chunks =
                ingestionService.ingest(pdfPath);

        // Verify ingestion.
        assertFalse("Chunks should not be empty", chunks.isEmpty());

        assertEquals(
                chunks.size(),
                searchService.size()
        );

        assertEquals(
                chunks.size(),
                ingestionService.getIngestedChunkCount()
        );

        // Verify source metadata.
        DocumentIngestionService.IngestedChunk firstChunk =
                chunks.get(0);

        assertEquals(
                "MNT-MAN-CNC-02_TX-200_Preventive_Maintenance.pdf",
                firstChunk.documentName()
        );

        assertTrue(firstChunk.pageNumber() >= 1);
        assertTrue(firstChunk.chunkNumber() >= 1);

        assertNotNull(
                ingestionService.getMetadata(firstChunk.id())
        );

        System.out.println("PDF ingestion successful!");
        System.out.println("Total chunks: " + chunks.size());
        System.out.println("Stored vectors: " + searchService.size());
        System.out.println("First chunk ID: " + firstChunk.id());
        System.out.println("First chunk page: " + firstChunk.pageNumber());
    }
}

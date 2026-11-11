
package com.amamjain.yourownai;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DocumentIngestionService {

    private final PdfDocumentLoader pdfLoader;
    private final TextChunker textChunker;
    private final SemanticSearchService searchService;

    private final Map<String, IngestedChunk> metadataById;

    public DocumentIngestionService(
            PdfDocumentLoader pdfLoader,
            TextChunker textChunker,
            SemanticSearchService searchService) {

        if (pdfLoader == null
                || textChunker == null
                || searchService == null) {
            throw new IllegalArgumentException(
                    "Dependencies cannot be null");
        }

        this.pdfLoader = pdfLoader;
        this.textChunker = textChunker;
        this.searchService = searchService;

        this.metadataById = new LinkedHashMap<>();
    }

    /**
     * Extracts a PDF, splits its pages into chunks,
     * and stores each chunk in the semantic vector database.
     */
    public List<IngestedChunk> ingest(Path pdfPath)
            throws IOException {

        if (pdfPath == null) {
            throw new IllegalArgumentException(
                    "PDF path cannot be null");
        }

        String documentName =
                pdfPath.getFileName().toString();

        String documentId = documentName
                .replaceAll("[^a-zA-Z0-9_-]", "_");

        List<PdfDocumentLoader.PageContent> pages =
                pdfLoader.load(pdfPath);

        List<IngestedChunk> ingestedChunks =
                new ArrayList<>();

        for (PdfDocumentLoader.PageContent page : pages) {

            List<String> chunks =
                    textChunker.chunkText(page.text());

            for (int i = 0; i < chunks.size(); i++) {

                String chunkText = chunks.get(i);

                int chunkNumber = i + 1;

                String chunkId = documentId
                        + "-p" + page.pageNumber()
                        + "-c" + chunkNumber;

                // Store the chunk and generate its embedding.
                searchService.addText(
                        chunkId,
                        chunkText
                );

                // Preserve the original document information.
                IngestedChunk metadata = new IngestedChunk(
                        chunkId,
                        documentName,
                        page.pageNumber(),
                        chunkNumber
                );

                        metadataById.put(chunkId, metadata);
                        ingestedChunks.add(metadata);
                        searchService.addMetadata(
                chunkId,
                new DocumentMetadata(
                        documentName,
                        page.pageNumber(),
                        chunkNumber
                )
        );
            }
        }

        return Collections.unmodifiableList(
                ingestedChunks);
    }

    /**
     * Returns metadata for a particular chunk ID.
     */
    public IngestedChunk getMetadata(String chunkId) {
        return metadataById.get(chunkId);
    }

    /**
     * Returns all ingested chunk metadata.
     */
    public List<IngestedChunk> getAllMetadata() {
        return List.copyOf(metadataById.values());
    }

    /**
     * Returns the total number of chunks ingested
     * through this service instance.
     */
    public int getIngestedChunkCount() {
        return metadataById.size();
    }

    /**
     * Metadata associated with each stored chunk.
     */
    public record IngestedChunk(
            String id,
            String documentName,
            int pageNumber,
            int chunkNumber) {
    }
}

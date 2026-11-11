
package com.amamjain.yourownai;

public record DocumentMetadata(
        String documentName,
        int pageNumber,
        int chunkNumber) {

    public DocumentMetadata {
        if (documentName == null || documentName.isBlank()) {
            throw new IllegalArgumentException(
                    "Document name cannot be blank");
        }

        if (pageNumber <= 0) {
            throw new IllegalArgumentException(
                    "Page number must be positive");
        }

        if (chunkNumber <= 0) {
            throw new IllegalArgumentException(
                    "Chunk number must be positive");
        }
    }
}

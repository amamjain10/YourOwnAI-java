
package com.amamjain.yourownai;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class TextDocumentLoader {

    public String load(Path path) throws IOException {
        Objects.requireNonNull(path, "File path cannot be null");

        if (!Files.isRegularFile(path)) {
            throw new IOException("Not a regular file: " + path);
        }

        return Files.readString(path, StandardCharsets.UTF_8);
    }
}

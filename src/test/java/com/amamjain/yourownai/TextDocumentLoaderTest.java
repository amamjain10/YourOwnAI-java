
package com.amamjain.yourownai;

import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertEquals;

public class TextDocumentLoaderTest {

    @Test
    public void shouldLoadTextFile() throws IOException {
        Path file = Files.createTempFile("document", ".txt");

        try {
            Files.writeString(
                    file, "YourOwnAI Java", StandardCharsets.UTF_8);

            TextDocumentLoader loader = new TextDocumentLoader();

            assertEquals("YourOwnAI Java", loader.load(file));
        } finally {
            Files.deleteIfExists(file);
        }
    }
}


package com.amamjain.yourownai;

import java.nio.file.Path;
import java.util.List;

public class App {

    public static void main(String[] args) throws Exception {
        TextDocumentLoader loader = new TextDocumentLoader();

        String text = loader.load(Path.of("sample.txt"));

        System.out.println("=== Original Document ===");
        System.out.println(text);

        TextChunker chunker = new TextChunker(80, 15);
        List<String> chunks = chunker.chunkText(text);

        System.out.println("\n=== Document Chunks ===");

        for (int i = 0; i < chunks.size(); i++) {
            System.out.println("\nChunk " + (i + 1) + ":");
            System.out.println(chunks.get(i));
        }

        System.out.println("\nTotal chunks: " + chunks.size());
    }
}


package com.amamjain.yourownai;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class PdfDocumentLoader {

    public record PageContent(int pageNumber, String text) {
    }

    public List<PageContent> load(Path path) throws IOException {

        if (path == null) {
            throw new IllegalArgumentException(
                    "PDF path cannot be null");
        }

        if (!Files.isRegularFile(path)) {
            throw new IOException(
                    "PDF file does not exist: " + path);
        }

        List<PageContent> pages = new ArrayList<>();

        try (PDDocument document =
                     Loader.loadPDF(path.toFile())) {

            PDFTextStripper stripper = new PDFTextStripper();

            int totalPages = document.getNumberOfPages();

            for (int page = 1; page <= totalPages; page++) {

                stripper.setStartPage(page);
                stripper.setEndPage(page);

                String text = stripper.getText(document).trim();

                if (!text.isBlank()) {
                    pages.add(new PageContent(page, text));
                }
            }
        }

        if (pages.isEmpty()) {
            throw new IOException(
                    "No extractable text found in PDF: " + path);
        }

        return List.copyOf(pages);
    }
}

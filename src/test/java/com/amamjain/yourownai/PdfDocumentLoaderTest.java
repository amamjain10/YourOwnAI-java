
package com.amamjain.yourownai;

import org.junit.Test;

import java.net.URL;
import java.nio.file.Path;
import java.util.List;

import static org.junit.Assert.*;

public class PdfDocumentLoaderTest {

    @Test
    public void shouldExtractTextPageByPage() throws Exception {

        URL resource = getClass().getClassLoader()
                .getResource(
                    "MNT-MAN-CNC-02_TX-200_Preventive_Maintenance.pdf"
                );

        assertNotNull("Test PDF not found", resource);

        Path pdfPath = Path.of(resource.toURI());

        PdfDocumentLoader loader = new PdfDocumentLoader();

        List<PdfDocumentLoader.PageContent> pages =
                loader.load(pdfPath);

        assertEquals("PDF should contain 2 pages", 2, pages.size());

        for (PdfDocumentLoader.PageContent page : pages) {

            System.out.println("\n===== PAGE "
                    + page.pageNumber() + " =====");

            // Print complete extracted page text
            System.out.println(page.text());
        }

        assertTrue(
                pages.get(0).text().contains("Preventive Maintenance")
        );

        assertTrue(
                "Page 2 should contain alarm information",
                pages.get(1).text().contains("AL-104")
        );
    }
}

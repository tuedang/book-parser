package com.twiki.pdf;

import com.google.common.collect.Lists;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageFitWidthDestination;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * This is an example on how to add bookmarks to a PDF document.  It simply
 * adds 1 bookmark for every page.
 */
public class BookmarksUtil {
    private BookmarksUtil() {
        //utility class
    }

    /**
     * This will print the documents data.
     *
     * @param inFile The PDF input
     * @param outFile the PDF output
     * @throws Exception If there is an error parsing the document.
     */
    public static void generateBookmark(File inFile, File outFile) throws IOException {

        try (PDDocument document = PDDocument.load(inFile)) {
            if (document.isEncrypted()) {
                throw new IOException("Error: Cannot add bookmarks to encrypted document.");
            }
            PDDocumentOutline outline = new PDDocumentOutline();
            document.getDocumentCatalog().setDocumentOutline(outline);
            PDOutlineItem pagesOutline = new PDOutlineItem();
            pagesOutline.setTitle("All Pages");
            outline.addLast(pagesOutline);

            List<PDPage> pages = Lists.newArrayList(document.getPages().iterator());
            for (int i = 0; i < pages.size(); i++) {
                PDPage page = pages.get(i);
                PDOutlineItem bookmark = makeBookMark("Page " + (i + 1), page);
                pagesOutline.addLast(bookmark);
            }
            pagesOutline.openNode();
            outline.openNode();

            document.save(outFile);
        }
    }

    private static PDOutlineItem makeBookMark(String title, PDPage targetPage) {
        PDPageFitWidthDestination dest = new PDPageFitWidthDestination();
        dest.setPage(targetPage);
        PDOutlineItem bookmark = new PDOutlineItem();
        bookmark.setDestination(dest);
        bookmark.setTitle(title);
        return bookmark;
    }

}

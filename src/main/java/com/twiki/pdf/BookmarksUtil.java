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
import java.util.Map;
import java.util.stream.Collectors;

public class BookmarksUtil {
    private BookmarksUtil() {
        //utility class
    }

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

    public static void generateBookmark(File inFile, File outFile, Map<Integer, String> bookmarks) throws IOException {
        if (bookmarks == null) {
            throw new IllegalArgumentException("Bookmarks cannot be null");
        }
        List<Integer> indexPages = bookmarks.keySet().stream()
                .sorted()
                .collect(Collectors.toList());

        try (PDDocument document = PDDocument.load(inFile)) {
            if (document.isEncrypted()) {
                throw new IOException("Error: Cannot add bookmarks to encrypted document.");
            }
            PDDocumentOutline outline = new PDDocumentOutline();
            document.getDocumentCatalog().setDocumentOutline(outline);
            PDOutlineItem pagesOutline = new PDOutlineItem();
            pagesOutline.setTitle("Bookmark pages");
            outline.addLast(pagesOutline);

            List<PDPage> pages = Lists.newArrayList(document.getPages().iterator());
            for (int i = 0; i < indexPages.size(); i++) {
                int pageIndex = indexPages.get(i);
                PDPage page = pages.get(pageIndex);
                PDOutlineItem bookmark = makeBookMark(bookmarks.get(pageIndex), page);
                pagesOutline.addLast(bookmark);
            }
            pagesOutline.openNode();
            outline.openNode();

            document.save(outFile);
            document.close();
        }
    }

    public static Map<Integer, String> shiftBookMarks(Map<Integer, String> bookmarks, int offset) {
        return bookmarks.entrySet()
                .stream()
                .collect(Collectors.toMap(e -> e.getKey() + offset,
                        e -> e.getValue()));
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

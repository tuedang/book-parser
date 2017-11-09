package com.twiki.tools;

import com.google.common.collect.Lists;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineNode;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class PdfToc {
    @FunctionalInterface
    public interface BookMarkVisitor {
        void visit(PDDocument document, PDOutlineItem bookmark, int pageNumber, int level);
    }

    public void execute(File file, BookMarkVisitor visitor) throws IOException {
        PDDocument document = PDDocument.load(file);
        PDDocumentOutline outline = document.getDocumentCatalog().getDocumentOutline();
        if (outline == null) {
            return;
        }
        printBookmark(document, outline, 0, visitor);
    }

    private void printBookmark(PDDocument document, PDOutlineNode bookmark, Integer level, BookMarkVisitor visitor) throws IOException {
        PDOutlineItem current = bookmark.getFirstChild();
        while (current != null) {
            int pageNumber = 1;   // first page

            List<PDPage> pages = Lists.newArrayList(document.getPages().iterator());
            for (PDPage page : pages) {
                if (page.equals(current.findDestinationPage(document))) {
                    break;
                }
                pageNumber++;
            }

            visitor.visit(document, current, pageNumber, level);

            printBookmark(document, current, level + 1, visitor);
            current = current.getNextSibling();
        }
    }

}

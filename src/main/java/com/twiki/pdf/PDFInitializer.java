package com.twiki.pdf;

import com.twiki.bookstack.BookStack;
import com.twiki.bookstack.Chapter;
import com.twiki.bookstack.Page;
import com.twiki.processor.BookProcessor;
import com.twiki.tools.PdfToc;

import java.io.File;
import java.io.IOException;

public class PDFInitializer implements BookProcessor {
    private BookStack bookStack;
    private File pdfFile;

    public PDFInitializer(File pdffile) {
        bookStack = new BookStack(pdffile.getName(), "content");
        this.pdfFile = pdffile;
    }

    @Override
    public BookStack processBook(BookStack bookStackNull) throws IOException {
        if (bookStackNull != null) throw new IOException("This is using for initialize bookstack");

        PdfToc pdfToc = new PdfToc();
        pdfToc.execute(pdfFile, (document, bookmark, pageNumber, level) -> {
            if (level < 2) {
                switch (level + 1) {
                    case 1:
                        if (!bookmark.hasChildren()) {
                            bookStack.addPage(new Page(bookmark.getTitle(), ""+pageNumber));
                        } else {
                            Chapter chapter = new Chapter(bookmark.getTitle(), ""+pageNumber);
                            bookStack.addChapter(chapter);
                        }
                        break;
                    case 2:

                        Page page = new Page(bookmark.getTitle(), ""+pageNumber);
                        bookStack.getLastChapter().addPage(page);
                        break;
                }
            }
        });
        return bookStack;
    }
}

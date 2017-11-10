package com.twiki.tools;

import com.twiki.pdf.BookmarksUtil;

import java.io.File;

public class BookmarkGeneratorApp {
    public static void main(String[] args) throws Exception {
        File pdfIn = new File("D:\\books\\book_pdf\\build-apis-you-wont-hate\\03_useful-database-seeding.pdf");
        File pdfOut = new File("D:\\books\\book_pdf\\03_useful-database-seeding-withbookmarks.pdf");
        BookmarksUtil.generateBookmark(pdfIn, pdfOut);
    }
}

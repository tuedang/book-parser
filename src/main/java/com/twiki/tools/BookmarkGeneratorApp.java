package com.twiki.tools;

import com.twiki.pdf.BookmarksUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class BookmarkGeneratorApp {
    public static void main(String[] args) throws Exception {
        File pdfIn = new File("D:\\books\\book_pdf\\build-apis-you-wont-hate\\03_useful-database-seeding.pdf");
        File pdfOut = new File("D:\\books\\book_pdf\\03_useful-database-seeding-withbookmarks.pdf");
        Map<Integer, String> bookmarks = new HashMap<>();
        bookmarks.put(1, "Day la page 2");
        bookmarks.put(4, "Day la page 5");
        BookmarksUtil.generateBookmark(pdfIn, pdfOut, bookmarks);
    }
}

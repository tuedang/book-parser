package com.twiki.tools;

import com.twiki.pdf.BookmarksUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class BookmarkGeneratorApp {
    public static void main(String[] args) throws Exception {
        File pdfIn = new File("/Data/books/writing-academic-english-4th-edition/07_chapter-4_from-paragraph-to-essay.pdf");
        File pdfOut = new File("/Data/books/06_part-ii-writing-an-essay_bookmark.pdf");
        Map<Integer, String> bookmarks = new HashMap<>();
        bookmarks.put(1, "Day la page 2");
        bookmarks.put(4, "Day la page 5");
        BookmarksUtil.generateBookmark(pdfIn, pdfOut, bookmarks);
    }
}

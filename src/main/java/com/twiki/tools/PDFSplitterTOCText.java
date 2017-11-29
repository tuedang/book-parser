package com.twiki.tools;

import com.twiki.bookstack.BookStack;
import com.twiki.helper.BookStackTraversal;
import com.twiki.helper.PlainTextTocReader;
import com.twiki.pdf.PDFSplitter;

import java.io.File;
import java.util.Map;

public class PDFSplitterTOCText {
    public static void main(String... args) throws Exception {
        File pdfPathFile = new File("/Data/books/problem-solving-in-data-structures-algorithms-using-java.pdf");

        BookStack bookStack = new PlainTextTocReader().read("problem-solving-in-data-structures-algorithms-using-java.txt");
        BookStackTraversal.print(bookStack);
        Map<Integer, String> splitter = PDFSplitter.createSplitterMap(bookStack);
        PDFSplitter.splitPage(pdfPathFile, splitter, new File("/Data/books/books_pdf"));
    }
}

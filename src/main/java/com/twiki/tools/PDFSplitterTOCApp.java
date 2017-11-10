package com.twiki.tools;

import com.twiki.bookstack.BookStack;
import com.twiki.helper.BookStackTraversal;
import com.twiki.pdf.PDFInitializer;
import com.twiki.pdf.PDFSplitter;

import java.io.File;
import java.util.Map;

public class PDFSplitterTOCApp {
    public static void main(String... args) throws Exception {
        File pdfPathFile = new File("D:\\books\\Build APIs You Won't Hate.pdf");

        BookStack bookStack = new PDFInitializer(pdfPathFile).processBook(null);
        BookStackTraversal.print(bookStack);
        Map<Integer, String> splitter = PDFSplitter.createSplitterMap(bookStack);
        PDFSplitter.splitPage(pdfPathFile, splitter, new File("D:\\books\\book_pdf"));
    }

}

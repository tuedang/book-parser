package com.twiki;

import com.twiki.bookstack.BookStack;
import com.twiki.helper.BookStackTraversal;
import com.twiki.pdf.PDFInitializer;
import com.twiki.pdf.SplitterMapUtil;
import com.twiki.tools.PDFSplitter;

import java.io.File;
import java.util.Map;

public class PDFApp {
    public static void main(String... args) throws Exception {
        File pdfPathFile = new File("/Users/tuedang/Downloads/download_shuttle/Spring Microservices.pdf");

        BookStack bookStack = new PDFInitializer(pdfPathFile).processBook(null);
        BookStackTraversal.print(bookStack);
        Map<Integer, String> splitter = SplitterMapUtil.createSplitterMap(bookStack);
        PDFSplitter.splitPage(pdfPathFile, splitter, new File("/Data/books"));
    }

}

package com.twiki;

import com.twiki.bookstack.BookStack;
import com.twiki.helper.BookStackTraversal;
import com.twiki.pdf.PDFInitializer;

import java.io.File;

public class PDFApp {
    public static void main(String... args) throws Exception {
        File epubPathFile = new File("/Users/tuedang/Downloads/download_shuttle/Spring Microservices.pdf");

        BookStack bookStack = new PDFInitializer(epubPathFile).processBook(null);
        BookStackTraversal.print(bookStack);
    }

}

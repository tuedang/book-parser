package com.twiki;

import com.twiki.bookstack.BookStack;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.epub.EpubReader;

import java.io.File;
import java.io.FileInputStream;

public class MainApp {

    public static void main(String... args) throws Exception {
        EpubReader epubReader = new EpubReader();
        Book book = epubReader.readEpub(new FileInputStream("/Users/tuedang/Downloads/RESTful_Web_Services.epub"));
        BookStack bookStack = new BookStackProcessor(book).get();
        BookStackUtils.generateFolderHtml(bookStack, new File("/Data/NCT/"));
    }


}


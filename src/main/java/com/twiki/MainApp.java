package com.twiki;

import com.twiki.bookstack.BookStack;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.epub.EpubReader;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.util.Collection;

public class MainApp {

    public static void main(String... args) throws Exception {
        EpubReader epubReader = new EpubReader();
        Collection<File> epubs = FileUtils.listFiles(new File("D:\\books\\"), new String[]{"epub"}, false);
        for (File epub : epubs) {
            Book book = epubReader.readEpub(new FileInputStream(epub));
            BookStack bookStack = new BookStackProcessor(book).get();
            BookStackUtils.generateFolderHtml(bookStack, new File("D:\\books\\books_html"));
        }
    }
}


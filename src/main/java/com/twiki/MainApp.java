package com.twiki;

import com.google.common.base.Stopwatch;
import com.twiki.bookstack.BookStack;
import com.twiki.processor.*;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.epub.EpubReader;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

public class MainApp {

    public static void main(String... args) throws Exception {
        EpubReader epubReader = new EpubReader();
        Collection<File> epubs = FileUtils.listFiles(new File("/Data/books/books_dev"), new String[]{"epub"}, false);

        String pathGenerate = "/Data/Development/projects/BookStack/database/seeds/books";
        for (File epub : epubs) {
            Stopwatch stopwatch = Stopwatch.createStarted();

            Book book = epubReader.readEpub(new FileInputStream(epub));
            BookStack bookStack = new BookStackInitializer(book).get();
            BookProcessor bookProcessor = new DefaultPipelineBookProcessor()
                    .addBookProcessor(new ImageUrlBookProcessor(pathGenerate, "/book_res/"))
//                    .addBookProcessor(new HtmlSplitterBookStackProcessor("/Data/books/books_html"));
                    .addBookProcessor(new JsonSerializeBookStackProcessor(pathGenerate));
            bookProcessor.processBook(bookStack);
            System.out.println(String.format("Parsing time: %s seconds", stopwatch.elapsed(TimeUnit.SECONDS)));

        }
    }
}


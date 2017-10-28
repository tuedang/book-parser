package com.twiki;

import com.google.common.base.Stopwatch;
import com.twiki.bookstack.BookStack;
import com.twiki.processor.*;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.epub.EpubReader;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.FileInputStream;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class MainApp implements CommandLineRunner {

    @Value("${file.path.out}")
    private String generatedFolder;

    @Value("${file.path.epub}")
    private String epubPath;

    @Value("${resource.url.prefix}")
    private String imageSrcPrefixPath;

    public static void main(String... args) throws Exception {
        SpringApplication.run(MainApp.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        EpubReader epubReader = new EpubReader();
        Collection<File> epubs = FileUtils.listFiles(new File(epubPath), new String[]{"epub"}, false);

        for (File epub : epubs) {
            Stopwatch stopwatch = Stopwatch.createStarted();

            Book book = epubReader.readEpub(new FileInputStream(epub));
            BookStack bookStack = new BookStackInitializer(book).get();
            BookProcessor bookProcessor = new DefaultPipelineBookProcessor()
                    .addBookProcessor(new ImageUrlBookProcessor(generatedFolder, imageSrcPrefixPath))
//                    .addBookProcessor(new HtmlSplitterBookStackProcessor("/Data/books/books_html"));
                    .addBookProcessor(new JsonSerializeBookStackProcessor(generatedFolder));
            bookProcessor.processBook(bookStack);
            System.out.println(String.format("Parsing time: %s seconds", stopwatch.elapsed(TimeUnit.SECONDS)));
        }
    }
}


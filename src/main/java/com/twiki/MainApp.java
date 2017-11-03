package com.twiki;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.twiki.bookstack.BookStack;
import com.twiki.processor.*;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.epub.EpubReader;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class MainApp implements CommandLineRunner {
    private static Log LOG = LogFactory.getLog(MainApp.class);

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
        File epubPathFile = new File(epubPath);

        List<File> epubs = epubPathFile.isFile() ?
                Lists.newArrayList(epubPathFile) :
                Lists.newArrayList(FileUtils.listFiles(epubPathFile, new String[]{"epub"}, false));

        BookProcessor bookProcessor = new DefaultPipelineBookProcessor()
                .addBookProcessor(new ImageUrlBookProcessor(generatedFolder, imageSrcPrefixPath))
//                    .addBookProcessor(new HtmlSplitterBookStackProcessor("/Data/books/books_html"));
                .addBookProcessor(new JsonSerializeBookStackProcessor(generatedFolder));
        for (File epub : epubs) {
            Stopwatch stopwatch = Stopwatch.createStarted();

            LOG.info("Parsing book: " + epub.getName());
            Book book = epubReader.readEpub(new FileInputStream(epub));
            BookStack bookStack = new BookStackInitializer(book).get();

            bookProcessor.processBook(bookStack);
            LOG.info(String.format("Parsed '%s' in: %s seconds", epub.getName(), stopwatch.stop().elapsed(TimeUnit.SECONDS)));
        }
    }
}


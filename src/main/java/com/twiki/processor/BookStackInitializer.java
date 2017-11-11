package com.twiki.processor;

import com.twiki.bookstack.BookMetaContextHolder;
import com.twiki.helper.ResourceHelper;
import com.twiki.bookstack.BookStack;
import com.twiki.bookstack.Chapter;
import com.twiki.bookstack.Page;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.TOCReference;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

public class BookStackInitializer implements BookProcessor {
    private static Log LOG = LogFactory.getLog(BookStackInitializer.class);

    private Book book;
    private BookStack bookStack;

    public BookStackInitializer(Book book) {
        this.book = book;
        processBook(null);
    }

    public BookStack get() {
        return this.bookStack;
    }

    @Override
    public BookStack processBook(BookStack bookStackInit) {
        if (bookStackInit != null) {
            throw new IllegalArgumentException("This is init state, it must be null");
        }
        if (bookStack != null) {
            return bookStack;
        }
        this.bookStack = new BookStack(book.getTitle(), ResourceHelper.getData(book.getCoverPage()));
        this.bookStack.setBookMetaContextHolder(new BookMetaContextHolder(book));
        handle(0, book.getTableOfContents().getTocReferences());
        return bookStack;
    }

    private void handle(Integer level, List<TOCReference> tocReferences)  {
        level++;
        for (TOCReference tocReference : tocReferences) {
            processTOC(level, tocReference);
            if (tocReference.getChildren() != null) {
                handle(level, tocReference.getChildren());
            }
        }
    }

    private void processTOC(int level, TOCReference tocReference) {
        Document document = Jsoup.parseBodyFragment(ResourceHelper.getData(tocReference.getResource()));

        String htmlContent;
        if (StringUtils.isNotEmpty(tocReference.getFragmentId())) {
            Element fragment = document.getElementById(tocReference.getFragmentId());
            fragment = closest(fragment, "div.sect1, section, body");
            fragment.select("h1.header-title, div.titlepage").remove();
            for (TOCReference toc : tocReference.getChildren()) {
                Element f = fragment.getElementById(toc.getFragmentId());
                if (f != null) f.remove();
            }

            //transform sect2 to h2 (match with bookstack
            Elements elements = fragment.select("div.sect2");
            elements.forEach(element -> element.prepend("<h2>" + element.attr("title")));
            elements.unwrap();

            //adhoc?
            fragment.select("h1:first-child").remove();//object-oriented vs function programming

            htmlContent = fragment.html();

        } else {
            document.select("h1.header-title, div.sect1, div.footnotes, div.titlepage").remove();
            Element element = document.getElementById(tocReference.getResourceId().replace("id-", ""));
            if (element == null) {
                htmlContent = document.select("section").html();
            } else {
                htmlContent = element.html();
            }
        }

        switch (level) {
            case 1:
                if (tocReference.getChildren().isEmpty()) {
                    bookStack.addPage(new Page(tocReference.getTitle(), htmlContent));
                } else {
                    Chapter chapter = new Chapter(tocReference.getTitle(), htmlContent);
                    bookStack.getBookMetaContextHolder().putResource(tocReference.getCompleteHref(), chapter);
                    bookStack.addChapter(chapter);
                }
                break;
            case 2:
                Page page = new Page(tocReference.getTitle(), htmlContent);
                bookStack.getLastChapter().addPage(page);
                break;
        }

    }

    private Element closest(Element element, String cssSelector) {
        while (!element.is(cssSelector)) {
            element = element.parent();
        }
        return element;
    }

}

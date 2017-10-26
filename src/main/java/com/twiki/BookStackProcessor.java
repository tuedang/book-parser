package com.twiki;

import com.twiki.bookstack.BookStack;
import com.twiki.bookstack.Chapter;
import com.twiki.bookstack.Page;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.TOCReference;
import nl.siegmann.epublib.util.CollectionUtil;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;

public class BookStackProcessor {
    private Book book;
    private BookStack bookStack;

    public BookStackProcessor(Book book) {
        this.book = book;
        try {
            this.bookStack = new BookStack(book.getTitle(), new String(book.getCoverPage().getData()));
            process();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public BookStack get() {
        return this.bookStack;
    }

    private BookStack process() throws IOException {
        handle(0, book.getTableOfContents().getTocReferences());
        return bookStack;

    }

    private void handle(Integer level, List<TOCReference> tocReferences) throws IOException {
        level++;
        for (TOCReference tocReference : tocReferences) {
            processTOC(level, tocReference);
            if (tocReference.getChildren() != null) {
                handle(level, tocReference.getChildren());
            }
        }
    }

    private void processTOC(int level, TOCReference tocReference) throws IOException {
        String data = new String(tocReference.getResource().getData());
        Document document = Jsoup.parseBodyFragment(data);

        String htmlContent;
        if (StringUtils.isNotEmpty(tocReference.getFragmentId())) {
            Element fragment = document.getElementById(tocReference.getFragmentId());
            fragment = closest(fragment, "div.sect1, section, body");
            fragment.select("div.titlepage").remove();
            for (TOCReference toc : tocReference.getChildren()) {
                Element f = fragment.getElementById(toc.getFragmentId());
                if (f != null) f.remove();
            }

            //transform sect2 to h2 (match with bookstack
            Elements elements = fragment.select("div.sect2");
            elements.forEach(element -> element.prepend("<h2>" + element.attr("title")));
            elements.unwrap();

            //adhoc?
            fragment.select("h1").remove();//object-oriented vs function programming

            htmlContent = fragment.html();

        } else {
            document.select("div.sect1, div.footnotes").remove();
            Element element = document.getElementById(tocReference.getResourceId().replace("id-", ""));
            if (element == null) {
                htmlContent = document.select("section").html();
            } else {
                htmlContent = element.html();
            }
        }

        switch (level) {
            case 1:
                if (CollectionUtil.isEmpty(tocReference.getChildren())) {
                    bookStack.addPage(new Page(tocReference.getTitle(), htmlContent));
                } else {
                    bookStack.addChapter(new Chapter(tocReference.getTitle(), htmlContent));
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

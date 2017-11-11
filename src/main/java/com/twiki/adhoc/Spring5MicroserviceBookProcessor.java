package com.twiki.adhoc;

import com.twiki.bookstack.BookStack;
import com.twiki.helper.BookStackTraversal;
import com.twiki.processor.BookProcessor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class Spring5MicroserviceBookProcessor implements BookProcessor {
    @Override
    public BookStack processBook(BookStack bookStack) throws IOException {
        BookStackTraversal.visitPage(bookStack, (page -> {
            Document document = Jsoup.parseBodyFragment(page.getHtmlContent());
            document
                    .select("pre.calibre20")
                    .select("br.title-page-name")
                    .before("\n")
                    .remove();
            document.select("body>article").unwrap();
            document.select("body>header").remove();
            document.select("body>style").remove();


            page.setHtmlContent(document.outerHtml());
        }));
        return bookStack;
    }
}

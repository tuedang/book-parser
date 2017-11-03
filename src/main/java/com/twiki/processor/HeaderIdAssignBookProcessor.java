package com.twiki.processor;

import com.twiki.bookstack.BookStack;
import com.twiki.helper.AppStringUtils;
import com.twiki.helper.BookStackTraversal;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class HeaderIdAssignBookProcessor implements BookProcessor {
    private static Log LOG = LogFactory.getLog(HeaderIdAssignBookProcessor.class);
    @Override
    public BookStack processBook(BookStack bookStack) throws IOException {
        BookStackTraversal.visitPage(bookStack, (page -> {
            Document document = Jsoup.parseBodyFragment(page.getHtmlContent());
            Elements headings = document.select("h1, h2, h3");
            if (!headings.isEmpty()) {
                headings.forEach(h -> {
                    if (StringUtils.isEmpty(h.attr("id"))) {
                        h.attr("id", String.format("id-%s-%s", h.tagName(), AppStringUtils.slugify(h.text())));
                        LOG.debug(String.format("Assign Id in page [%s#%s] ", page.getTitle(), h.attr("id")));
                    }
                });
                page.setHtmlContent(document.outerHtml());
            }
        }));
        return bookStack;
    }
}

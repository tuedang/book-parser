package com.twiki.processor;

import com.twiki.bookstack.BookStack;
import com.twiki.helper.BookStackTraversal;

import java.io.IOException;
import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

public class SlugBookProcessor implements BookProcessor {
    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");

    private static String slugify(String input) {
        String nowhitespace = input.trim().replaceAll("\\s+", "-");
        String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
        String slug = NONLATIN.matcher(normalized).replaceAll("")
                .replaceAll("-", " ").trim()
                .replaceAll("\\s+", "-");
        return slug.toLowerCase(Locale.ENGLISH);
    }

    @Override
    public BookStack processBook(BookStack bookStack) throws IOException {
        bookStack.setSlug(slugify(bookStack.getTitle()));
        BookStackTraversal.visitContent(bookStack, (contentEntity) ->
                contentEntity.setSlug(slugify(contentEntity.getTitle())));
        return bookStack;
    }
}

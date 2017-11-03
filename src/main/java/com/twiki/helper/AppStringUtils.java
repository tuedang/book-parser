package com.twiki.helper;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

public class AppStringUtils {
    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");

    public static String slugify(String input) {
        String nowhitespace = input.trim().replaceAll("\\s+", "-");
        String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
        String slug = NONLATIN.matcher(normalized).replaceAll("")
                .replaceAll("-", " ").trim()
                .replaceAll("\\s+", "-");
        return slug.toLowerCase(Locale.ENGLISH);
    }
}

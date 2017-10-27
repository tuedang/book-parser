package com.twiki;

import nl.siegmann.epublib.domain.Resource;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.text.Normalizer;
import java.util.regex.Pattern;

public class ResourceHelper {
    public static String getData(Resource resource) {
        try {
            return new String(resource.getData());
        } catch (IOException ioe) {
            throw new UncheckedIOException(ioe);
        }
    }

    public static String unAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String result = pattern.matcher(temp).replaceAll("");
        result = result.replace((char) 273, 'd').replace((char) 272, 'D');
        result = result.replaceAll(":", "");
        result = trimCommonFileName(result);

        return result;
    }

    public static String trimCommonFileName(String fn) {
        fn = fn.trim();
        while (fn.startsWith(".")) {
            fn = fn.replaceFirst("\\.", "");
        }
        fn = fn.replaceAll(":", "_")
                .replaceAll("&"," ")
                .replace("/", "_")
                .replace("?", "")
                .replace(":", "")
                .replace("’", "");
        return fn.trim();
    }

    public static String escape(String folder) {
        return unAccent(folder);
    }
}

package com.twiki.helper;

import com.google.common.io.Resources;
import com.twiki.bookstack.BookStack;
import com.twiki.bookstack.Chapter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlainTextTocReader {
    private Pattern textPattern = Pattern.compile("\\((.*?)\\)");

    public BookStack read(String filename) throws IOException {

        URL url = Resources.getResource("tocs/" + filename);
        File f = new File(url.getFile());

        List<String> lines = new ArrayList<>();
        try (LineIterator lineIterator = FileUtils.lineIterator(f, "UTF-8")) {
            while (lineIterator.hasNext()) {
                String line = lineIterator.nextLine();
                if (StringUtils.isNotEmpty(line)) {
                    lines.add(line);
                }
            }
        }

        BookStack bookStack = new BookStack(FilenameUtils.getBaseName(filename), "");
        for (String s : lines) {
            Matcher matcher = textPattern.matcher(s);

            while (matcher.find()) {
                String chapterName = StringUtils.removeEnd(s, matcher.group(0)).trim();
                String pageNumber = matcher.group(1);

                bookStack.addChapter(new Chapter(chapterName, pageNumber));
            }
        }

        return bookStack;
    }

}

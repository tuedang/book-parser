package com.twiki.helper;

import com.google.common.collect.Maps;
import com.google.common.io.Resources;
import com.twiki.bookstack.BookStack;
import com.twiki.bookstack.Chapter;
import com.twiki.pdf.PDFSplitter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrSubstitutor;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    public String toHtmlIndexPage(String prefix, BookStack bookStack) throws IOException {
        String template = Resources.toString(Resources.getResource("templates/book-link-template.html"), StandardCharsets.UTF_8);

        StringBuffer htmlIndexPage = new StringBuffer();
        BookStackTraversal.visitChapter(bookStack, chapter -> {
            Map<String, String> valuesMap = Maps.newHashMap();

            int index = bookStack.getContents().indexOf(chapter);
            String href = prefix + PDFSplitter.getFileName(index, chapter.getTitle());
            valuesMap.put("id", AppStringUtils.slugify(chapter.getTitle()));
            valuesMap.put("href", href);
            valuesMap.put("text", chapter.getTitle());
            StrSubstitutor sub = new StrSubstitutor(valuesMap);
            String resolvedString = sub.replace(template);
            htmlIndexPage.append(resolvedString);
            htmlIndexPage.append("\n");
        });
        return htmlIndexPage.toString();
    }
}

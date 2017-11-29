package com.twiki.pdf;

import com.google.common.collect.Lists;
import com.twiki.bookstack.BookStack;
import com.twiki.bookstack.ContentEntity;
import com.twiki.helper.AppStringUtils;
import com.twiki.helper.BookStackTraversal;
import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class PDFSplitter {
    public static Map<Integer, String> createSplitterMap(BookStack pdfBookStack) {
        Map<Integer, String> spliterMap = new HashMap<>();

        List<ContentEntity> contentEntities = new ArrayList<>();
        BookStackTraversal.visitContent(pdfBookStack, (contentEntity, level) -> {
            if (level == BookStackTraversal.FIRST_LEVEL) {
                contentEntities.add(contentEntity);
            }
        });
        for (int i = 0; i < contentEntities.size() - 1; i++) {
            int pageEndOfChapter = Integer.valueOf(contentEntities.get(i + 1).getHtmlContent()) - 1;
            spliterMap.put(pageEndOfChapter, contentEntities.get(i).getTitle());
        }
        return spliterMap;
    }

    public static void splitPage(File pdfIn, Map<Integer, String> rangePages, File pdfOutFolder) throws IOException {
        if (!pdfOutFolder.exists() || pdfOutFolder.isFile()) {
            throw new IOException("Folder is not existed");
        }

        String name = AppStringUtils.slugify(FilenameUtils.getBaseName(pdfIn.getName()));
        File bookFolder = new File(pdfOutFolder, name);
        bookFolder.mkdir();

        PDDocument document = PDDocument.load(pdfIn);
        replaceMaxPageIsLastPage(rangePages, document.getNumberOfPages());

        List<Integer> indexPages = Lists.newArrayList(rangePages.keySet());
        Collections.sort(indexPages);



        Splitter splitter = new Splitter() {
            protected boolean splitAtPage(int pageNumber) {
                return indexPages.contains(pageNumber);
            }
        };
        List<PDDocument> splittedDocuments = splitter.split(document);
        for (PDDocument pdDocument : splittedDocuments) {
            int index = splittedDocuments.indexOf(pdDocument);

            String filename = String.format("%02d_%s", index, rangePages.get(indexPages.get(index)));
            pdDocument.save(new File(bookFolder, AppStringUtils.slugify(filename) + ".pdf"));
        }
    }

    private static void replaceMaxPageIsLastPage(Map<Integer, String> rangePages, int numberOfPage) {
        //In case of missing the last part
        int lastPageInMap = Collections.max(rangePages.keySet());
        if (numberOfPage != lastPageInMap) {
            rangePages.put(numberOfPage, rangePages.get(lastPageInMap));
            rangePages.remove(lastPageInMap);
        }
    }

    public static String getFileName(int index, String title) {
        String name = String.format("%02d_%s", index, title);
        return AppStringUtils.slugify(name) + ".pdf";
    }
}

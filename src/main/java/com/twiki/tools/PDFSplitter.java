package com.twiki.tools;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PDFSplitter {

    public static void splitPage(File pdfIn, Map<Integer, String> rangePages, File pdfOutFolder) throws IOException {
        List<Integer> indexPages = Lists.newArrayList(rangePages.keySet());
        Collections.sort(indexPages);

        String name = FilenameUtils.getBaseName(pdfIn.getName());

        PDDocument document = PDDocument.load(pdfIn);

        Splitter splitter = new Splitter() {
            protected boolean splitAtPage(int pageNumber) {
                return indexPages.contains(pageNumber);
            }
        };
        List<PDDocument> splittedDocuments = splitter.split(document);
        for (PDDocument pdDocument : splittedDocuments) {
            int index = splittedDocuments.indexOf(pdDocument);
            int fromPage = index == 0 ? 0 : indexPages.get(index - 1);
            int toPage = (index == splittedDocuments.size() - 1) ? document.getNumberOfPages() : indexPages.get(index);

//            String filename = String.format("%s_%s-%s_%s.pdf", name, fromPage + 1, toPage, rangePages.get(indexPages.get(index)));
            String filename = String.format("%s.pdf", rangePages.get(indexPages.get(index)));
            pdDocument.save(new File(pdfOutFolder, filename));
        }
    }

    public static void main(String[] args) throws IOException {
        File pdfIn = new File("D:\\books\\english\\Writing Academic English, 4th Edition_vietnamese.pdf");
        ImmutableMap<Integer, String> indexs = ImmutableMap.<Integer, String>builder()
                .put(7, "Table of content")
                .put(11, "Preface")
                .put(14, "To the Student")
                .put(15, "Part I")
                .put(29, "Chapter 1")
                .put(43, "Chapter 2")
                .put(53, "Chapter 3")
                .put(69, "Chapter 4")
                .put(84, "Chapter 5")
                .put(95, "Chapter 6")
                .put(112, "Chapter 7")
                .put(113, "Part II")
                .put(134, "Chapter 8")
                .put(164, "Chapter 9")
                .put(165, "Part III")
                .put(191, "Chapter 10")
                .put(207, "Chapter 11")
                .put(222, "Chapter 12")
                .put(243, "Chapter 13")
                .put(258, "Chapter 14")
                .put(267, "Appendix A")
                .put(270, "Appendix B")
                .put(272, "Appendix C")
                .put(280, "Appendix D")
                .build();
        splitPage(pdfIn, indexs, new File("D:\\books\\english\\Writing Academic English, 4th Edition_vietnamese"));
    }
}

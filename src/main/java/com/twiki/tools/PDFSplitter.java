package com.twiki.tools;

import com.google.common.collect.Lists;
import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class PDFSplitter {

    public static void splitPage(File pdfIn, List<Integer> rangePages, File pdfOutFolder) throws IOException {
        Collections.sort(rangePages);

        String name = FilenameUtils.getBaseName(pdfIn.getName());

        PDDocument document = PDDocument.load(pdfIn);

        Splitter splitter = new Splitter() {
            protected boolean splitAtPage(int pageNumber) {
                return rangePages.contains(pageNumber);
            }
        };
        List<PDDocument> splittedDocuments = splitter.split(document);
        for (PDDocument pdDocument : splittedDocuments) {
            int index = splittedDocuments.indexOf(pdDocument);
            int fromPage = index == 0 ? 0 : rangePages.get(index - 1);
            int toPage = (index == splittedDocuments.size() - 1) ? document.getNumberOfPages() : rangePages.get(index);
            pdDocument.save(new File(pdfOutFolder, String.format("%s_%s-%s.pdf", name, fromPage + 1, toPage)));
        }
    }

    public static void main(String[] args) throws IOException {
        File pdfIn = new File("/Users/tuedang/Downloads/Writing Academic English, 4th Edition.pdf");
        splitPage(pdfIn, Lists.newArrayList(5, 10, 15, 20, 25), new File("/Users/tuedang/Downloads/Writing_pdf"));
    }
}

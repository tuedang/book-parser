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
        if (!pdfOutFolder.exists() || pdfOutFolder.isFile()) {
            throw new IOException("Folder is not existed");
        }

        List<Integer> indexPages = Lists.newArrayList(rangePages.keySet());
        Collections.sort(indexPages);

        String name = FilenameUtils.getBaseName(pdfIn.getName());
        File bookFolder = new File(pdfOutFolder, name);
        bookFolder.mkdir();

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

//            String filename = String.format("%02d_%s_%s-%s_%s.pdf", index, name, fromPage + 1, toPage, rangePages.get(indexPages.get(index)));
            String filename = String.format("%02d_%s.pdf", index, rangePages.get(indexPages.get(index)));
//            String filename = String.format("%s.pdf", rangePages.get(indexPages.get(index)));
            pdDocument.save(new File(bookFolder, filename));
        }
    }

    public static void main(String[] args) throws IOException {
        File pdfIn = new File("D:\\books\\english\\Writing Academic English, 4th Edition.pdf");
        ImmutableMap<Integer, String> indexs = ImmutableMap.<Integer, String>builder()
                .put(7, "Table of Content")
                .put(10, "Preface")

                .put(1 + 10, "PART I_WRITING A PARAGRAPH")
                .put(17 + 10, "Chapter 1_Paragraph Structure")
                .put(38 + 10, "Chapter 2_Unity and Coherence")
                .put(54 + 10, "Chapter 3_Supporting Details Facts, Quotations, and Statistics")
                .put(55 + 10, "Part II_ WRITING AN ESSAY")
                .put(80 + 10, "Chapter 4_From Paragraph to Essay")
                .put(93 + 10, "Chapter 5_Chronological Order_ Process Essays")
                .put(110 + 10, "Chapter 6_Cause,Effect essays")
                .put(126 + 10, "Chapter 7_Comparison,Contrast Essay")
                .put(141 + 10, "Chapter 8_Paraphrase and Summary")
                .put(160 + 10, "Chapter 9_Argumentative Essay")
                .put(161 + 10, "Part III_SENTENCE STRUCTURE")
                .put(178 + 10, "Chapter 10_Types of Sentences")
                .put(193 + 10, "Chapter 11_Using Parallel Structures and Fixing Sentence Problems")
                .put(209 + 10, "Chapter 12_Noun Clauses")
                .put(229 + 10, "Chapter 13_Adverb Clauses")
                .put(249 + 10, "Chapter 14_Adjective Clauses")
                .put(264 + 10, "Chapter 15_Participial Phrases")
                .put(279 + 10, "Appendix A_ The Process of Academic Writing")
                .put(290 + 10, "Appendix B_Punctuation Rules")
                .put(299 + 10, "Appendix C_Charts of Connecting Words and Transition Signals")
                .put(302 + 10, "Appendix D_Editing Symbols")
                .put(312 + 10, "Appendix E_Research and Documentation of Sources")
                .put(330 + 10, "Appendix F_Self-Editing and Peer-Editing Worksheets")
                .put(345, "Index and Credit")
                .build();
        splitPage(pdfIn, indexs, new File("D:\\books\\english"));
    }
}

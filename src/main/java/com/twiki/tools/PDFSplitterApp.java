package com.twiki.tools;

import com.google.common.collect.ImmutableMap;
import com.twiki.pdf.PDFSplitter;

import java.io.File;
import java.io.IOException;

public class PDFSplitterApp {

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
                .put(93 + 10, "Chapter 5_Chronological Order Process Essays")
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
        PDFSplitter.splitPage(pdfIn, indexs, new File("D:\\books\\english"));
    }
}

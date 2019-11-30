package com.dermacon.ankipdfeditor.export;

import com.dermacon.ankipdfeditor.data.card.Card;

import java.io.File;
import java.util.List;

public class TexExporter extends Exporter {

    private static final String DOC_TEMPLATE = "\\documentclass{article}\n"
            + "\\usepackage{tikz,lipsum,lmodern}\n"
            + "\\usepackage[most]{tcolorbox}\n"
            + "\\usepackage[paperheight=10.75in,paperwidth=7.25in,margin=1in,heightrounded]{geometry}\n"
            + "\\usepackage{graphicx}\n"
            + "\\usepackage{blindtext}\n"
            + "\\usepackage{ragged2e}\n"
            + "\\usepackage[space]{grffile}\n"
            + "\n"
            + "\\graphicspath{ {./img/} }\n"
            + "\n"
            + "\\begin{document}\n"
            + "  %s\n"
            + "\\end{document}\n"
            + "\n";

    private static final String CARD_TEMPLATE = "\\begin{tcolorbox}[colback=white!10!white,colframe=lightgray!75!black,\n"
            + "  savelowerto=\\jobname_ex.tex]\n"
            + "\n"
            + "  \\begin{center}\n"
            + "    %s\n"
            + "  \\end{center}\n"
            + "\n"
            + "  \\tcblower\n"
            + "\n"
            + "  \\justifying\n"
            + "  %s\n"
            + "\n"
            + "\\end{tcolorbox}\n";

    private static final String IMG_TEMPLATE =
            "\n  \\\\begin{center}\n"
            + "    \\\\includegraphics[width=\\\\linewidth]{\\\\detokenize{%s}}\n"
            + "  \\\\end{center}\n";

    public TexExporter(ExportInfo exportInfo) {
        super(exportInfo);
    }

    @Override
    String parseFormat(List<Card> stack) {
        StringBuilder temp = new StringBuilder();
        for(Card curr : stack) {
            temp.append(String.format(CARD_TEMPLATE,
                    escapeTexChars(curr.getFrontSide()),
                    escapeTexChars(curr.getBackSide()))
            );
        }

        String content = translateHtml(temp.toString());
        System.out.println(content);
        return String.format(DOC_TEMPLATE, content);
    }

    private String translateHtml(String content) {
        return translateTags(updateImages(content));
    }

    private String translateTags(String content) {
        return content.replaceAll("<div>", "")
                .replaceAll("</div>", "\n\n")
                .replaceAll("<br />", "")
                .replaceAll("\\&nbsp;", "")
                .replaceAll("\\&gt;", "")
                .replaceAll(":\\\\", "")
                .replaceAll("\\\\<", "")
//                .replaceAll("\\\\([A-Za-z]*?)", "$1")
                ;
//                .replaceAll("\\\\", "");
    }

    // todo check if really necessary
    private String escapeTexChars(String content) {
        return content
                .replaceAll("\\\\", "")
                .replaceAll("&", "\\\\&")
                .replaceAll("%", "\\\\%")
                .replaceAll("\\$", "\\\\\\$")
                .replaceAll("#", "\\\\#")
//                .replaceAll("_", "\\\\_")
                .replaceAll("\\{", "\\\\\\{")
                .replaceAll("}", "\\\\}")
                .replaceAll("~", "\\\\~")
                .replaceAll("\\^", "\\\\\\^")
                .replaceAll("ä", "\"a")
                .replaceAll("ö", "\"o")
                .replaceAll("ü", "\"u")
                .replaceAll("Ä", "\"A")
                .replaceAll("Ö", "\"O")
                .replaceAll("Ü", "\"U");
    }

    private String updateImages(String content) {
        String regex = "<img src=[\"]?(.*?)[\"]?[ ]?/>";
        return content
                .replaceAll(regex, String.format(IMG_TEMPLATE, exportInfo.getMediaPath() + "$1"))
                .replaceAll(".png", "")
                .replaceAll(".jpg", "");
    }

}
package com.dermacon.data.project;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.input.ReversedLinesFileReader;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.io.IOException;

public class FileBuilder {
    private static final String VIM_USAGE =
            "************************************************\n"
                    + "*         Anki-Editor - version 1.0            *\n"
                    + "*              In normal mode:                 *\n"
                    + "*  - type ,c to add a template for a new card  *\n"
                    + "*  - type p to paste the current page number   *\n"
                    + "*  - type z to turn to the next page           *\n"
                    + "*  - type Z to turn to the previous page       *\n"
                    + "*                                              *\n"
                    + "*  hint:                                       *\n"
                    + "*  - If page is not available with simply      *\n"
                    + "*    typing p try ,p                           *\n"
                    + "************************************************\n\n";

    private static final String LAST_DOCS_DIR = new File(System.getProperty("user.dir")).getParent() + "/lastDocs/";
    private static final String IMG_TEMP_DIR = LAST_DOCS_DIR + "img_temp/";

    private static final String DECK_DIR = LAST_DOCS_DIR + "decks/";
    private static final String DECK_FILE = DECK_DIR + "%s.anki";

    private static final String PDF_DIR = LAST_DOCS_DIR + "pdf/";
    private static final String PDF_FILE = PDF_DIR + "%s.pdf";

    private static final File PROJ_HISTORY = new File(LAST_DOCS_DIR + ".projHistory");
    private static final String DEFAULT_DECK = String.format(DECK_FILE, "TestDeck");
    private static final String DEFAULT_PDF = String.format(PDF_FILE, "manual");
    private static final String MANUAL_RES_PATH = System.getProperty("user.dir") + "/src/main/resources/com/dermacon/manual.pdf";

    private String deckPath = DEFAULT_DECK;
    private String pdfPath = DEFAULT_PDF;
    private int currPage = 1;

    public ProjectInfo build() throws IOException {
        initProjectStructure();
        if (PROJ_HISTORY.exists()) {
            parseProjHistory();
        }

        File deckFile = new File(deckPath);
        if (!deckFile.exists() && !deckFile.isDirectory()) {
            createDeckFile(deckFile);
        }

        return new ProjectInfo(deckPath, pdfPath, currPage);
    }


    private void initProjectStructure() throws IOException {
        saveMkDir(LAST_DOCS_DIR);
        saveMkDir(DECK_DIR);
        saveMkDir(PDF_DIR);
        saveMkDir(IMG_TEMP_DIR);

        saveCP(MANUAL_RES_PATH, DEFAULT_PDF);
    }

    private void saveMkDir(String path) {
        File dir = new File(path);
        if (!dir.exists() || !dir.isDirectory()) {
            dir.mkdir();
        }
    }

    private void saveCP(String srcPath, String targetPath) throws IOException {
        File targetFile = new File(targetPath);
        if (!targetFile.exists() || !targetFile.isDirectory()) {
            File srcFile = new File(srcPath);
            FileUtils.copyFile(srcFile, targetFile);
        }
    }


    /**
     * parse /lastDocs/.projectHistory file
     *
     * @throws IOException
     */
    public ProjectInfo parseProjHistory() throws IOException {
        ReversedLinesFileReader object = new ReversedLinesFileReader(PROJ_HISTORY);
        int counter = 0, n_lines = 3;
        String line;

        while (counter < n_lines) {
            line = object.readLine();
            if (line.startsWith("deck")) {
                deckName(line.split(":")[1]);
            } else if (line.startsWith("pdf")) {
                pdfName(line.split(":")[1]);
            } else if (line.startsWith("page")) {
                currPage(line.split(":")[1]);
            } else {
                throw new IOException("input line does not match pattern: " + line); // todo pattern to javadoc
            }
            counter++;
        }

        return build();
    }

    private void deckName(String deckName) {
        this.deckPath = String.format(DECK_DIR, deckName);
    }

    private void pdfName(String pdfName) {
        this.pdfPath = String.format(PDF_DIR, pdfName);
    }

    private void currPage(String currPage) {
        this.currPage = Integer.parseInt(currPage);
    }


    private void createDeckFile(File deckFile) throws IOException {
        int lineLength = VIM_USAGE.split("\n")[0].length();
        String deckDescription = formatDeckdescr(deckFile.getName(),lineLength);
        FileUtils.writeStringToFile(deckFile, VIM_USAGE + deckDescription);
        System.out.println("file wrote");
        System.out.println(deckFile);
    }

    private static String formatDeckdescr(String input, int lineLen) {
        char delimiter = '-';
        int inputLen = input.length() + 2;
        assert inputLen <= lineLen;
        String output = "";

        int offset = (lineLen - inputLen) / 2;
        for (int i = 0; i < offset; i++) {
            output += delimiter;
        }
        output += '\'' + input + '\'';
        for (int i = 0; i < offset; i++) {
            output += delimiter;
        }

        if ((lineLen - inputLen) % 2 != 0) {
            output += delimiter;
        }

        return output + "\n\n";
    }

}

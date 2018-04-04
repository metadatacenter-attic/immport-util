package org.immport.data.airrstandard.writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.immport.data.airrstandard.util.AirrStandardConstants;

public class TextWriter {

    /** The logger. */
    private static final Logger log = LoggerFactory.getLogger(TextWriter.class);

    private String filePath;

    private FileWriter fileWriter;
    private BufferedWriter outPut;

    private StringBuffer tableContent;

    public TextWriter(String filePath) throws IOException {
        this.filePath = filePath;
        tableContent = new StringBuffer(AirrStandardConstants.EMPTY_STR);
    }

    public void addRowContent(String[] columns) {
        tableContent.append(String.join(AirrStandardConstants.TAB, columns) + AirrStandardConstants.NEWLINE);
    }

    public void addContent(String content) {
        tableContent.append(content);
    }

    //
    // Close without Writing
    //
    public void close() {
        closeWriter();
        tableContent = null;
    }

    public void write() {
        try {
            openWriter();
            if (outPut != null && tableContent != null
                    && !tableContent.toString().equals(AirrStandardConstants.EMPTY_STR)) {
                outPut.write(tableContent.toString());
            }
        } catch (IOException ex) {
            log.error("opening text file writer error: " + filePath);
            log.error(ex.getClass().getName() + ":" + ex.getMessage());
            ex.printStackTrace();
        } finally {
            close();
        }
    }

    private void openWriter() throws IOException {
        File file = new File(filePath);
        log.debug("output file = " + file.getAbsolutePath());
        fileWriter = null;
        outPut = null;
        try {
            fileWriter = new FileWriter(file);
            outPut = new BufferedWriter(fileWriter);
        } catch (Exception ex) {
            fileWriter = null;
            outPut = null;
            log.error("opening text file writer error: " + file.getName());
            log.error(ex.getClass().getName() + ":" + ex.getMessage());
            ex.printStackTrace();
            throw new IOException(ex.getMessage());
        }
    }

    private void closeWriter() {
        try {
            if (outPut != null) {
                outPut.close();
            }
        } catch (Exception ex) {
            log.error("Attempted to close writer (Exception, message) = ("
                    + String.join(AirrStandardConstants.COMMA_SPACE, ex.getClass().getName(), ex.getMessage()) + ")");
        }
        outPut = null;
        try {
            if (fileWriter != null) {
                fileWriter.close();
            }
        } catch (Exception ex) {
            log.error("Attempted to close writer (Exception, message) = ("
                    + String.join(AirrStandardConstants.COMMA_SPACE, ex.getClass().getName(), ex.getMessage()) + ")");
        }
        fileWriter = null;
    }
}

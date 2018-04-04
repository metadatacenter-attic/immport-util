package org.immport.data.airrstandard.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.lang3.StringUtils;

import org.immport.data.airrstandard.manager.PropertiesManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.immport.data.airrstandard.util.AirrStandardConstants;

public class TabReader {

    /** The logger. */
    private static final Logger log = LoggerFactory.getLogger(TabReader.class);

    private String filePath;
    private BufferedReader bufferedReader = null;

    private int currentLineNumber;

    private File templatesDirectory;

    public TabReader(String filePath) throws IOException {
        this.filePath = filePath;
        init();
    }

    public int getCurrentLineNumber() {
        return currentLineNumber;
    }

    public String readFile() {
        StringBuffer str = new StringBuffer(AirrStandardConstants.EMPTY_STR);
        if (bufferedReader == null) {
            return str.toString();
        }
        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                currentLineNumber++;
                line = StringUtils.trimToEmpty(line);
                str.append(line + AirrStandardConstants.NEWLINE);
            }
        } catch (Exception ex) {
            log.error("text file reader read error:");
            log.error(ex.getClass().getName() + ":" + ex.getMessage());
            ex.printStackTrace();
        } finally {
            close();
        }
        return str.toString();
    }

    public void close() {
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
        } catch (Exception ex) {
            log.error("closing text file reader error");
            log.error(ex.getClass().getName() + ":" + ex.getMessage());
            ex.printStackTrace();
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
                log.error("closing text file reader error");
                log.error(e.getClass().getName() + ":" + e.getMessage());
                e.printStackTrace();
            }
        } finally {
            bufferedReader = null;
        }
    }

    public String[] readLine() {
        String line;
        if (bufferedReader == null) {
            return null;
        }
        try {
            while ((line = bufferedReader.readLine()) != null) {
                currentLineNumber++;
                line = StringUtils.trimToEmpty(line);
                if (line.equals(AirrStandardConstants.EMPTY_STR)) {
                    continue;
                }
                String[] components = line.split(AirrStandardConstants.TAB);
                for (int i = 0; i < components.length; i++) {
                    components[i] = StringUtils.trimToEmpty(components[i]);
                }
                return components;
            }
        } catch (Exception ex) {
            log.error("text file reader read error:");
            log.error(ex.getClass().getName() + ":" + ex.getMessage());
            ex.printStackTrace();
            close();
        }
        return null;
    }

    private void init() throws IOException {
        templatesDirectory = new File(PropertiesManager.getDataAirrStandardProperties().getTemplatesDirectory());
        currentLineNumber = 0;
        openReader();
    }

    private void openReader() throws IOException {
        File file = new File(templatesDirectory, filePath);
        log.debug("input file = " + file.getAbsolutePath());
        bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(file), AirrStandardConstants.FILE_ENCODING));
        } catch (Exception ex) {
            close();
            log.error("opening text file reader error: " + file.getName());
            log.error(ex.getClass().getName() + ":" + ex.getMessage());
            ex.printStackTrace();
            throw new IOException(ex.getMessage());
        }
    }

}

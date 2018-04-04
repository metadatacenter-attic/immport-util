package org.immport.data.airrstandard.reader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.immport.data.airrstandard.manager.PropertiesManager;
import org.immport.data.airrstandard.text.MiairrTemplateColumn;
import org.immport.data.airrstandard.writer.TextWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.immport.data.airrstandard.util.AirrStandardConstants;

public class MiAirrSpecificationReader {

    /** The logger. */
    private static final Logger log = LoggerFactory.getLogger(MiAirrSpecificationReader.class);

    private TextWriter writer;
    private TabReader reader;

    private File packageDirectory;

    private HashMap<String, String> airrColumnToEntityMap;
    private HashMap<String, List<MiairrTemplateColumn>> miairrTemplates;

    public MiAirrSpecificationReader(AirrSpecificationReader airrSpecification, File packageDirectory) {
        this.packageDirectory = packageDirectory;
        init(airrSpecification);
    }

    public void loadFiles() {
        readMiairrTemplates();
    }

    public HashMap<String, List<MiairrTemplateColumn>> getMiairrTemplates() {
        return miairrTemplates;
    }

    private void init(AirrSpecificationReader airrSpecificationReader) {
        airrColumnToEntityMap = airrSpecificationReader.getAirrColumnToEntityMap();
    }

    private void readMiairrTemplates() {
        miairrTemplates = new HashMap<String, List<MiairrTemplateColumn>>();
        String datasetPath = PropertiesManager.getDataAirrStandardProperties().getMiairrTemplatesDataSet();
        String templateCategoryPath = PropertiesManager.getDataAirrStandardProperties()
                .getMiairrTemplatesTargetRepository();
        String filePath = PropertiesManager.getDataAirrStandardProperties().getMiairrTemplates();
        writer = null;
        reader = null;
        try {
            HashMap<String, String> datasetMapping = readMapping(datasetPath);
            HashMap<String, String> templateCategoryMapping = readMapping(templateCategoryPath);
            reader = new TabReader(filePath);
            writer = new TextWriter(String.join(AirrStandardConstants.SLASH, packageDirectory.getCanonicalPath(),
                    AirrStandardConstants.getFile(AirrStandardConstants.MIAIRR_SPECIFICATION_FILE)));
            writer.addRowContent(MiairrTemplateColumn.getTabHeader());
            String[] line = reader.readLine();
            while (line != null) {
                String[] currentLine = line;
                int currentLineNumber = reader.getCurrentLineNumber();
                line = reader.readLine();
                if (currentLineNumber == 1) {
                    log.debug("header = " + currentLine.toString());
                    continue;
                }
                MiairrTemplateColumn miairrTemplateColumns = new MiairrTemplateColumn(currentLine,
                        airrColumnToEntityMap, datasetMapping, templateCategoryMapping);
                String entityName = miairrTemplateColumns.getAirrEntityName();
                if (!miairrTemplates.containsKey(entityName)) {
                    miairrTemplates.put(entityName, new ArrayList<MiairrTemplateColumn>());
                }
                miairrTemplates.get(entityName).add(miairrTemplateColumns);
                writer.addRowContent(miairrTemplateColumns.toTabString());
            }
        } catch (IOException ex) {
            log.error("readAllTemplateColumns text file error: " + filePath);
            log.error(ex.getClass().getName() + ":" + ex.getMessage());
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            log.error("readAllTemplateColumns bad row: " + filePath);
            log.error(ex.getClass().getName() + ":" + ex.getMessage());
            ex.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
            reader = null;
            if (writer != null) {
                if (log.isDebugEnabled()) {
                    writer.write();
                } else {
                    writer.close();
                }
            }
            writer = null;
        }
    }

    private HashMap<String, String> readMapping(String filePath) throws IOException {
        HashMap<String, String> mapping = new HashMap<String, String>();
        try {
            reader = new TabReader(filePath);
            String[] line = reader.readLine();
            while (line != null) {
                String[] currentLine = line;
                log.debug("current line = " + currentLine.toString());
                int currentLineNumber = reader.getCurrentLineNumber();
                line = reader.readLine();
                if (currentLineNumber == 1) {
                    log.debug("header = " + currentLine.toString());
                    continue;
                }
                mapping.put(currentLine[0], currentLine[1]);
            }
        } catch (IOException ex) {
            log.error("readMapping text file error: " + filePath);
            log.error(ex.getClass().getName() + ":" + ex.getMessage());
            ex.printStackTrace();
        } finally {
            reader.close();
        }
        return mapping;
    }

}

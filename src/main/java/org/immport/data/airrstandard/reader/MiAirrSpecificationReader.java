package org.immport.data.airrstandard.reader;

import org.immport.data.airrstandard.manager.PropertiesManager;
import org.immport.data.airrstandard.text.MiairrTemplateColumn;
import org.immport.data.airrstandard.util.AirrStandardConstants;
import org.immport.data.airrstandard.writer.TextWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MiAirrSpecificationReader {

  /**
   * The logger.
   */
  private static final Logger log = LoggerFactory.getLogger(MiAirrSpecificationReader.class);

  private static final String DEFAULT_MIAIRR_TEMPLATE_RESOURCE =
      "immport/specifications/AIRR_Minimal_Standard_Data_Elements.tsv";
  private static final String DEFAULT_MIAIRR_DATASET_RESOURCE =
      "immport/specifications/AIRR_Minimal_Standard_Data_Elements.DataSet";
  private static final String DEFAULT_MIAIRR_TARGET_REPOSITORY_RESOURCE =
      "immport/specifications/AIRR_Minimal_Standard_Data_Elements.TargetRepository";

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
    try {
      HashMap<String, String> datasetMapping = readMapping(getMiairrDatasetInputStream());
      HashMap<String, String> templateCategoryMapping = readMapping(getMiairrTargetRepositoryInputStream());
      reader = new TabReader(getMiairrTemplateInputStream());
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
      log.error(ex.getClass().getName() + ":" + ex.getMessage());
      ex.printStackTrace();
    } catch (InstantiationException ex) {
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

  private HashMap<String, String> readMapping(InputStream inputStream) throws IOException {
    HashMap<String, String> mapping = new HashMap<String, String>();
    try {
      reader = new TabReader(inputStream);
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
      log.error(ex.getClass().getName() + ":" + ex.getMessage());
      ex.printStackTrace();
    } finally {
      reader.close();
    }
    return mapping;
  }

  private static InputStream getMiairrDatasetInputStream() throws IOException {
    InputStream stream = null;
    if (hasUserSpecifiedResources()) {
      String userResource = getUserMiairrDatasetResource();
      log.debug("Loading user-specified MiAIRR dataset resource: " + userResource);
      stream = getInputStreamFromAbsolutePath(userResource);
    } else {
      log.debug("Loading the default MiAIRR dataset resource");
      final ClassLoader cl = Thread.currentThread().getContextClassLoader();
      stream = cl.getResourceAsStream(DEFAULT_MIAIRR_DATASET_RESOURCE);
    }
    return stream;
  }

  private static String getUserMiairrDatasetResource() {
    String parentDirectory = PropertiesManager.getDataAirrStandardProperties().getTemplatesDirectory();
    String filePath = PropertiesManager.getDataAirrStandardProperties().getMiairrTemplatesDataSet();
    return String.join(AirrStandardConstants.SLASH, parentDirectory, filePath);
  }

  private static InputStream getMiairrTargetRepositoryInputStream() throws IOException {
    InputStream stream = null;
    if (hasUserSpecifiedResources()) {
      String userResource = getUserMiairrTargetRepositoriesResource();
      log.debug("Loading user-specified MiAIRR target repositories resource: " + userResource);
      stream = getInputStreamFromAbsolutePath(userResource);
    } else {
      log.debug("Loading the default MiAIRR target repositories resource");
      final ClassLoader cl = Thread.currentThread().getContextClassLoader();
      stream = cl.getResourceAsStream(DEFAULT_MIAIRR_TARGET_REPOSITORY_RESOURCE);
    }
    return stream;
  }

  private static String getUserMiairrTargetRepositoriesResource() {
    String parentDirectory = PropertiesManager.getDataAirrStandardProperties().getTemplatesDirectory();
    String filePath = PropertiesManager.getDataAirrStandardProperties().getMiairrTemplatesTargetRepository();
    return String.join(AirrStandardConstants.SLASH, parentDirectory, filePath);
  }

  private static InputStream getMiairrTemplateInputStream() throws IOException {
    InputStream stream = null;
    if (hasUserSpecifiedResources()) {
      String userResource = getUserMiairrTemplateResource();
      log.debug("Loading user-specified MiAIRR template resource: " + userResource);
      stream = getInputStreamFromAbsolutePath(userResource);
    } else {
      log.debug("Loading the default MiAIRR template resource");
      final ClassLoader cl = Thread.currentThread().getContextClassLoader();
      stream = cl.getResourceAsStream(DEFAULT_MIAIRR_TEMPLATE_RESOURCE);
    }
    return stream;
  }

  private static String getUserMiairrTemplateResource() {
    String parentDirectory = PropertiesManager.getDataAirrStandardProperties().getTemplatesDirectory();
    String filePath = PropertiesManager.getDataAirrStandardProperties().getMiairrTemplates();
    return String.join(AirrStandardConstants.SLASH, parentDirectory, filePath);
  }

  private static boolean hasUserSpecifiedResources() {
    return PropertiesManager.getDataAirrStandardProperties() != null;
  }

  private static InputStream getInputStreamFromAbsolutePath(String resource) throws IOException {
    return new FileInputStream(resource);
  }
}

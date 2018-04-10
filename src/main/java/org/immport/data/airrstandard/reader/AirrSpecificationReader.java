package org.immport.data.airrstandard.reader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.immport.data.airrstandard.manager.PropertiesManager;
import org.immport.data.airrstandard.util.AirrStandardConstants;
import org.immport.data.airrstandard.writer.TextWriter;
import org.immport.data.airrstandard.yaml.AirrTemplates;
import org.immport.data.airrstandard.yaml.Entity;
import org.immport.data.airrstandard.yaml.Specification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AirrSpecificationReader {

  /**
   * The logger.
   */
  private static final Logger log = LoggerFactory.getLogger(AirrSpecificationReader.class);

  private static final String DEFAULT_AIRR_TEMPLATE_RESOURCE = "immport/specifications/definitions.yaml";

  private final File packageDirectory;

  private TextWriter writer;

  private AirrTemplates airrTemplatesYml;
  private HashMap<String, List<Specification>> airrTemplates = new HashMap<String, List<Specification>>();

  private HashMap<String, String> airrColumnToEntityMap = new HashMap<String, String>();

  private static final String[] AirrStandardHeader = new String[]{"Airr Entity", "Airr Column", "Data"};

  private static final String GET_METHOD_PREFIX = "get";

  public AirrSpecificationReader(File packageDirectory) {
    this.packageDirectory = packageDirectory;
  }

  public void loadFiles() {
    readAirrSpecification();
  }

  public AirrTemplates getAirrTemplatesYml() {
    return airrTemplatesYml;
  }

  public HashMap<String, List<Specification>> getAirrTemplates() {
    return airrTemplates;
  }

  public HashMap<String, String> getAirrColumnToEntityMap() {
    return airrColumnToEntityMap;
  }

  private void readAirrSpecification() {
    final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    try {
      InputStream inputStream = getAirrTemplatesInputStream();
      airrTemplatesYml = mapper.readValue(inputStream, AirrTemplates.class);
      writer = new TextWriter(String.join(AirrStandardConstants.SLASH, packageDirectory.getCanonicalPath(),
          AirrStandardConstants.getFile(AirrStandardConstants.AIRR_SPECIFICATION_FILE)));
      writer.addRowContent(AirrStandardHeader);
      generateAirrTemplateColumns(airrTemplatesYml, airrTemplates);
      generateAirrColumnToEntityMap(airrTemplatesYml);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
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

  private void generateAirrTemplateColumns(AirrTemplates templates, HashMap<String, List<Specification>> allTemplates)
      throws Exception {
    if (airrTemplatesYml == null) {
      return;
    }
    for (String entityComponent : AirrTemplates.AIRR_ATTRIBUTES) {
      addEntity(entityComponent, getEntity(entityComponent, templates), allTemplates);
    }
  }

  private void addEntity(String entityName, Entity entity, HashMap<String, List<Specification>> allTemplates) {
    if (entity == null) {
      log.debug("entityName is empty = " + entityName);
      return;
    }
    Map<String, Specification> properties = entity.getProperties();
    for (String columnName : properties.keySet()) {
      if (!allTemplates.containsKey(entityName)) {
        allTemplates.put(entityName, new ArrayList<Specification>());
      }
      Specification specification = properties.get(columnName);
      allTemplates.get(entityName).add(specification);
      writer.addRowContent(new String[]{entityName, columnName, specification.toString()});
    }
  }

  private void generateAirrColumnToEntityMap(AirrTemplates templates) throws Exception {
    for (String entityComponent : AirrTemplates.AIRR_ATTRIBUTES) {
      Entity entity = getEntity(entityComponent, templates);
      Map<String, Specification> properties = entity.getProperties();
      for (String columnName : properties.keySet()) {
        if (!airrColumnToEntityMap.containsKey(columnName)) {
          airrColumnToEntityMap.put(columnName, entityComponent);
        }
      }
    }
  }

  private Entity getEntity(String entityComponent, AirrTemplates templates) throws Exception {
    String firstChar = entityComponent.substring(0, 1).toUpperCase();
    String suffix = entityComponent.substring(1, entityComponent.length());
    String methodName = String.join(AirrStandardConstants.EMPTY_STR, GET_METHOD_PREFIX, firstChar, suffix);
    Class theClass = templates.getClass();
    Method method = theClass.getMethod(methodName);
    Entity entity = (Entity) method.invoke(templates);
    return entity;
  }

  private static InputStream getAirrTemplatesInputStream() throws IOException {
    InputStream stream = null;
    if (PropertiesManager.getDataAirrStandardProperties() != null) {
      String userResource = getUserAirTemplatesResource();
      log.debug("Loading user-specified AIRR template resource: " + userResource);
      stream = getInputStreamFromAbsolutePath(userResource);
    }
    if (stream == null) {
      // Load the default AIRR template specification
      log.debug("User-specified AIRR template resource could not be detected. Loading the default resource instead.");
      final ClassLoader cl = Thread.currentThread().getContextClassLoader();
      stream = cl.getResourceAsStream(DEFAULT_AIRR_TEMPLATE_RESOURCE);
    }
    return stream;
  }

  private static InputStream getInputStreamFromAbsolutePath(String resource) {
    try {
      return new FileInputStream(resource);
    } catch (IOException e) {
      return null;
    }
  }

  private static String getUserAirTemplatesResource() {
    String parentDirectory = PropertiesManager.getDataAirrStandardProperties().getTemplatesDirectory();
    String templateFileName = PropertiesManager.getDataAirrStandardProperties().getAirrTemplates();
    return parentDirectory + "/" + templateFileName;
  }

}

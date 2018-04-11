package org.immport.data.airrstandard.reader;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.immport.data.airrstandard.manager.PropertiesManager;
import org.immport.data.airrstandard.reader.util.MiairrJsonContext;
import org.immport.data.airrstandard.reader.util.MiairrJsonDerivedContext;
import org.immport.data.airrstandard.util.AirrStandardConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class MiairrJsonReader {

  /**
   * The logger.
   */
  private static final Logger log = LoggerFactory.getLogger(MiairrJsonReader.class);

  private static final String DEFAULT_SCHEMA_VERSION = "3.17";

  private HashMap<String, Object> miairrJson;
  private HashMap<String, Object> miairrJsonContext;

  private String templateSchemaVersion;

  public MiairrJsonReader(MiAirrSpecificationReader miAirrSpecification) {
    //
    // Set the mappings for context generation
    //
    MiairrJsonContext.setMappings(miAirrSpecification.getMiairrTemplates());
    //
    // Get the template schema version
    //
    templateSchemaVersion = getTemplateSchemaVersion();
  }

  private static String getTemplateSchemaVersion() {
    String templateSchemaVersion = DEFAULT_SCHEMA_VERSION;
    if (hasUserSpecifiedResources()) {
      templateSchemaVersion = PropertiesManager.getDataAirrStandardProperties().getTemplateSchemaVersion();
    }
    return templateSchemaVersion;
  }

  public HashMap<String, Object> getMiairrJson() {
    return miairrJson;
  }

  public HashMap<String, Object> getMiairrContext() {
    return miairrJsonContext;
  }

  public void loadFile(File file, File packageDirectory, String miairrJsonFileName) {
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    try {
      log.debug("Reading MiAIRR Json File = " + file.getCanonicalPath());
      miairrJson = (HashMap<String, Object>) mapper.readValue(file, HashMap.class);
      log.debug("Generating MiAIRR Json Context = " + file.getCanonicalPath());
      miairrJsonContext = MiairrJsonContext.generateContext(miairrJson);
      MiairrJsonDerivedContext.generateDerivedColumns(miairrJsonContext, templateSchemaVersion, packageDirectory,
          file);
      log.debug("----->>>>>Start Context<<<<<-----" + AirrStandardConstants.NEWLINE + toString()
          + AirrStandardConstants.NEWLINE + "----->>>>>Finish Context<<<<<-----");
    } catch (Exception ex) {
      miairrJson = null;
      miairrJsonContext = null;
      log.error(ex.getMessage(), ex);
      log.error("json file error: " + file.getName());
      log.error(ex.getClass().getName() + ":" + ex.getMessage());
      ex.printStackTrace();
    }
  }

  public String miairrJsonToString() {
    StringBuffer str = new StringBuffer(AirrStandardConstants.EMPTY_STR);
    if (miairrJson != null) {
      log.debug("miairrJson length = " + miairrJson.keySet().size());
      str.append(String.join(AirrStandardConstants.EQUALS, "miairrJson", AirrStandardConstants.OPEN_PAREN));
      int count = 0;
      for (String key : miairrJson.keySet()) {
        if (count > 0) {
          str.append(AirrStandardConstants.COMMA);
        }
        count++;
        str.append(key + AirrStandardConstants.EQUALS + AirrStandardConstants.NEWLINE);
        str.append(miairrJson.get(key).toString());
        str.append(AirrStandardConstants.NEWLINE);
      }
      str.append(AirrStandardConstants.CLOSE_PAREN);
    }
    return str.toString();
  }

  public String miairrJsonContextToString() {
    StringBuffer str = new StringBuffer(AirrStandardConstants.OPEN_PAREN);
    int count = 0;
    for (String key : miairrJsonContext.keySet()) {
      if (count > 0) {
        str.append(AirrStandardConstants.COMMA);
      }
      count++;
      Object value = miairrJsonContext.get(key);
      if (value instanceof String) {
        str.append(String.join(AirrStandardConstants.EQUALS, key, (String) value));
      } else if (value instanceof HashMap) {
        HashMap<String, Object> obj = (HashMap<String, Object>) value;
        str.append(String.join(AirrStandardConstants.EQUALS, key, AirrStandardConstants.OPEN_PAREN));
        int ncount = 0;
        for (String nkey : obj.keySet()) {
          if (ncount > 0) {
            str.append(AirrStandardConstants.COMMA);
          }
          ncount++;
          str.append("    " + String.join(AirrStandardConstants.EQUALS, nkey, obj.get(nkey).toString()));
        }
        str.append(AirrStandardConstants.CLOSE_PAREN);
      } else if (value instanceof ArrayList) {
        ArrayList<HashMap<String, Object>> obj = (ArrayList<HashMap<String, Object>>) value;
        str.append(String.join(AirrStandardConstants.EQUALS, key, AirrStandardConstants.OPEN_BRACKET));
        for (HashMap<String, Object> nobj : obj) {
          str.append("    " + AirrStandardConstants.OPEN_PAREN);
          int ncount = 0;
          for (String nkey : nobj.keySet()) {
            if (ncount > 0) {
              str.append(AirrStandardConstants.COMMA);
            }
            ncount++;
            str.append(
                "      " + String.join(AirrStandardConstants.EQUALS, nkey, nobj.get(nkey).toString()));
          }
          str.append("    " + AirrStandardConstants.CLOSE_PAREN);
        }
        str.append(AirrStandardConstants.CLOSE_BRACKET);
      }
    }
    str.append(AirrStandardConstants.CLOSE_PAREN);
    return str.toString();
  }

  private static boolean hasUserSpecifiedResources() {
    return PropertiesManager.getDataAirrStandardProperties() != null;
  }
}

package org.immport.data.airrstandard.render;

import com.hubspot.jinjava.Jinjava;
import org.immport.data.airrstandard.manager.PropertiesManager;
import org.immport.data.airrstandard.reader.MiairrJsonReader;
import org.immport.data.airrstandard.reader.TabReader;
import org.immport.data.airrstandard.util.AirrStandardConstants;
import org.immport.data.airrstandard.writer.TextWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class RenderTemplate {

  /**
   * The logger.
   */
  private static final Logger log = LoggerFactory.getLogger(RenderTemplate.class);

  private static final String DEFAULT_RENDER_TEMPLATE_DIRECTORY = "immport/renderTemplates";

  public RenderTemplate() {
    // NO-OP
  }

  public String renderTemplate(MiairrJsonReader miairrJsonReader, String templateName, File packageDirectory) {
    TextWriter writer = null;
    String renderedTemplateFilePath = null;
    try {
      Jinjava jinjava = new Jinjava();
      InputStream inputStream = getRenderTemplateInputStream(templateName);
      String template = new TabReader(inputStream).readFile();
      HashMap<String, Object> miairrContext = miairrJsonReader.getMiairrContext();
      String renderedTemplate = jinjava.render(template, miairrContext);
      String outputLocation = getOutputLocation(packageDirectory.getCanonicalPath(), templateName);
      writer = new TextWriter(outputLocation);
      writer.addContent(renderedTemplate);
      renderedTemplateFilePath = outputLocation;
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      log.error(ex.getClass().getName() + ":" + ex.getMessage());
      ex.printStackTrace();
    } finally {
      if (writer != null) {
        writer.write();
      }
    }
    return renderedTemplateFilePath;
  }

  private String getOutputLocation(String parentDirectory, String templateName) {
    String fileName = getTemplateFileName(templateName);
    return String.join(AirrStandardConstants.SLASH, parentDirectory, fileName);
  }

  private static InputStream getRenderTemplateInputStream(String templateName) throws IOException {
    InputStream stream = null;
    if (PropertiesManager.getDataAirrStandardProperties() != null) {
      String userResource = getUserRenderTemplateResource(templateName);
      log.debug("Loading user-specified rendering template resource: " + userResource);
      stream = getInputStreamFromAbsolutePath(userResource);
    }
    if (stream == null) {
      // Load the default rendering template specification
      log.debug("User-specified rendering resource could not be detected. Loading the default resource instead.");
      final ClassLoader cl = Thread.currentThread().getContextClassLoader();
      String defaultRenderTemplate = getDefaultRenderTemplateResource(templateName);
      stream = cl.getResourceAsStream(defaultRenderTemplate);
    }
    return stream;
  }

  private static String getUserRenderTemplateResource(String templateName) {
    String parentDirectory = PropertiesManager.getDataAirrStandardProperties().getImmportJsonRenderTemplatesDirectory();
    String fileName = getTemplateFileName(templateName);
    return String.join(AirrStandardConstants.SLASH, parentDirectory, fileName);
  }

  private static String getDefaultRenderTemplateResource(String templateName) {
    String parentDirectory = DEFAULT_RENDER_TEMPLATE_DIRECTORY;
    String fileName = getTemplateFileName(templateName);
    return String.join(AirrStandardConstants.SLASH, parentDirectory, fileName);
  }

  private static String getTemplateFileName(String templateName) {
    return String.join(AirrStandardConstants.DOT, templateName, AirrStandardConstants.JSON_SUFFIX);
  }

  private static InputStream getInputStreamFromAbsolutePath(String resource) {
    try {
      return new FileInputStream(resource);
    } catch (IOException e) {
      return null;
    }
  }
}

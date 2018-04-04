package org.immport.data.airrstandard.render;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;

import org.immport.data.airrstandard.manager.PropertiesManager;
import org.immport.data.airrstandard.reader.MiairrJsonReader;
import org.immport.data.airrstandard.reader.TabReader;
import org.immport.data.airrstandard.util.AirrStandardConstants;
import org.immport.data.airrstandard.writer.TextWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hubspot.jinjava.Jinjava;

public class RenderTemplate {

    /** The logger. */
    private static final Logger log = LoggerFactory.getLogger(RenderTemplate.class);

    private String renderTemplatesDirectory;

    public RenderTemplate() {
        init();
    }

    public String renderTemplate(MiairrJsonReader miairrJsonReader, String templateName, File packageDirectory) {
        HashMap<String, Object> miairrContext = miairrJsonReader.getMiairrContext();
        File templateFile = new File(packageDirectory,
                String.join(AirrStandardConstants.DOT, templateName, AirrStandardConstants.JSON_SUFFIX));
        String renderedTemplateFilePath = null;
        String renderTemplatePath = String.join(AirrStandardConstants.SLASH, renderTemplatesDirectory,
                templateFile.getName());
        TextWriter writer = null;
        try {
            Jinjava jinjava = new Jinjava();
            String template = new TabReader(renderTemplatePath).readFile();
            String renderedTemplate = jinjava.render(template, miairrContext);
            writer = new TextWriter(templateFile.getCanonicalPath());
            writer.addContent(renderedTemplate);
            renderedTemplateFilePath = templateFile.getCanonicalPath();
        } catch (Exception ex) {
            renderedTemplateFilePath = null;
            log.error(ex.getMessage(), ex);
            log.error("json file error: " + renderTemplatePath);
            log.error(ex.getClass().getName() + ":" + ex.getMessage());
            ex.printStackTrace();
        } finally {
            if (writer != null) {
                writer.write();
            }
        }
        return renderedTemplateFilePath;
    }

    private void init() {
        renderTemplatesDirectory = PropertiesManager.getDataAirrStandardProperties()
                .getImmportJsonRenderTemplatesDirectory();
    }
}

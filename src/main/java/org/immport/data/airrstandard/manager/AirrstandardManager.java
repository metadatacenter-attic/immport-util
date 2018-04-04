package org.immport.data.airrstandard.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import org.immport.data.airrstandard.util.DirectoryUtils;
import org.immport.data.airrstandard.reader.MiairrJsonReader;
import org.immport.data.airrstandard.reader.MiAirrSpecificationReader;
import org.immport.data.airrstandard.reader.AirrSpecificationReader;
import org.immport.data.airrstandard.render.RenderTemplate;
import org.immport.data.airrstandard.util.AirrStandardConstants;

public class AirrstandardManager {

    /** The logger. */
    private static final Logger log = LoggerFactory.getLogger(AirrstandardManager.class);

    public AirrstandardManager() {
    }

    public void transformToTemplates(String buildDirectoryPath, String miairrJsonFilePath, String packageName,
            String templateNames) throws Exception {
        log.debug("----->>>>>Start<<<<<-----");
        //
        // MiAIRR Json File
        //
        File miairrrJsonFile = new File(miairrJsonFilePath);
        //
        // Create Package Directory (remove if already exists!)
        //
        File buildDirectory = new File(buildDirectoryPath);
        File packageDirectory = new File(buildDirectory, packageName);
        DirectoryUtils.cleanUpDirectory(packageDirectory);
        //
        // Read the AIRR Standard
        //
        AirrSpecificationReader airrSpecification = new AirrSpecificationReader(packageDirectory);
        airrSpecification.loadFiles();
        //
        // Read the MiAIRR Standard
        //
        MiAirrSpecificationReader miAirrSpecification = new MiAirrSpecificationReader(airrSpecification,
                packageDirectory);
        miAirrSpecification.loadFiles();
        //
        // Read the MiAIRR Json File to Transform into ImmPort Template Upload Package
        //
        MiairrJsonReader miairrJsonReader = new MiairrJsonReader(miAirrSpecification);
        miairrJsonReader.loadFile(miairrrJsonFile, packageDirectory, miairrrJsonFile.getName());
        //
        // Render the ImmPort Templates from the MiAIRR Json File
        //
        RenderTemplate renderTemplate = new RenderTemplate();
        String[] immportTemplateNames = templateNames.split(AirrStandardConstants.SEMI_COLON_PATTERN);
        for (String immportTemplateName : immportTemplateNames) {
            String renderedTemplateFilePath = renderTemplate.renderTemplate(miairrJsonReader, immportTemplateName,
                    packageDirectory);
            log.debug("(immportTemplateName, renderedTemplateFilePath) = (" + String
                    .join(AirrStandardConstants.COMMA_SPACE, immportTemplateName, renderedTemplateFilePath + ")"));
        }
        //
        // Generate Zip Package
        //
        File packageZipFile = new File(buildDirectory,
                String.join(AirrStandardConstants.DOT, packageName, AirrStandardConstants.ZIP_SUFFIX));
        DirectoryUtils.zipDirectory(packageDirectory.getCanonicalPath(), packageZipFile.getCanonicalPath());
        DirectoryUtils.remove(packageDirectory);
        log.debug("----->>>>>Finish<<<<<-----");
    }
}

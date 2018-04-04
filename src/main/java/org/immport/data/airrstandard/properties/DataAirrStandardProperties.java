package org.immport.data.airrstandard.properties;

import lombok.Data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Data
public class DataAirrStandardProperties {

    /** The logger. */
    private static final Logger log = LoggerFactory.getLogger(DataAirrStandardProperties.class);

    private String airrTemplates;
    private String immportJsonRenderTemplatesDirectory;
    private String miairrTemplates;
    private String miairrTemplatesDataSet;
    private String miairrTemplatesTargetRepository;
    private String templateSchemaVersion;
    private String templatesDirectory;

    public DataAirrStandardProperties() {
        log.debug("Creating instance of class DataAirrStandardProperties");
    }
}

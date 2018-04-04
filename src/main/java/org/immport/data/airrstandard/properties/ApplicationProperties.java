package org.immport.data.airrstandard.properties;

import lombok.Data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Data
public class ApplicationProperties {

    /** The logger. */
    private static final Logger log = LoggerFactory.getLogger(ApplicationProperties.class);

    private String name;

    public ApplicationProperties() {
        log.debug("Creating instance of class ApplicationProperties");
    }

}

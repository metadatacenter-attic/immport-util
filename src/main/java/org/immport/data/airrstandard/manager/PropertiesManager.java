package org.immport.data.airrstandard.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.immport.data.airrstandard.properties.ApplicationProperties;
import org.immport.data.airrstandard.properties.DataAirrStandardProperties;

/**
 * The Class PropertiesManager.
 * 
 * @author BISC-Team
 */
public class PropertiesManager {

    /** The logger. */
    private static final Logger log = LoggerFactory.getLogger(PropertiesManager.class);

    private static ApplicationProperties applicationProperties;

    private static DataAirrStandardProperties dataAirrStandardProperties;

    public static void setDataAirrStandardProperties(DataAirrStandardProperties tmpDataAirrStandardProperties) {
        if (log.isDebugEnabled())
            log.debug("setting dataAirrStandardProperties");
        dataAirrStandardProperties = tmpDataAirrStandardProperties;
        if (log.isDebugEnabled())
            log.debug("dataAirrStandardProperties = " + dataAirrStandardProperties);
        return;
    }

    public static DataAirrStandardProperties getDataAirrStandardProperties() {
        if (log.isDebugEnabled())
            log.debug("dataAirrStandardProperties = " + dataAirrStandardProperties);
        return dataAirrStandardProperties;
    }

    public static void setApplicationProperties(ApplicationProperties tmpApplicationProperties) {
        if (log.isDebugEnabled())
            log.debug("setting applicationProperties");
        applicationProperties = tmpApplicationProperties;
        ;
        if (log.isDebugEnabled())
            log.debug("applicationProperties = " + applicationProperties);
        return;
    }

    public static ApplicationProperties getApplicationProperties() {
        if (log.isDebugEnabled())
            log.debug("applicationProperties = " + applicationProperties);
        return applicationProperties;
    }

}

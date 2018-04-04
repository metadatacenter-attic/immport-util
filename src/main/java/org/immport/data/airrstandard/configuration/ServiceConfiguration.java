package org.immport.data.airrstandard.configuration;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import org.immport.data.airrstandard.manager.PropertiesManager;
import org.immport.data.airrstandard.properties.ApplicationProperties;
import org.immport.data.airrstandard.properties.DataAirrStandardProperties;

@Configuration
@DependsOn({ "applicationProperties", "dataAirrStandardProperties" })
public class ServiceConfiguration {

    /** The logger. */
    private static final Logger log = LoggerFactory.getLogger(ServiceConfiguration.class);

    @Autowired
    ApplicationProperties applicationProperties;

    @Autowired
    DataAirrStandardProperties dataAirrStandardProperties;

    public ServiceConfiguration() {
        log.debug("Creating instance of class ServiceConfiguration");
    }

    @PostConstruct
    private void initPropertiesManager() {
        PropertiesManager.setDataAirrStandardProperties(dataAirrStandardProperties);
        PropertiesManager.setApplicationProperties(applicationProperties);
        return;
    }

}

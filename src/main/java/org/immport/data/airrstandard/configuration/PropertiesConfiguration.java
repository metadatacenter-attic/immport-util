package org.immport.data.airrstandard.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.immport.data.airrstandard.properties.ApplicationProperties;
import org.immport.data.airrstandard.properties.DataAirrStandardProperties;

@Configuration
@EnableConfigurationProperties
public class PropertiesConfiguration {

    /** The logger. */
    private static final Logger log = LoggerFactory.getLogger(PropertiesConfiguration.class);

    public PropertiesConfiguration() {
        log.debug("Creating instance of class PropertiesConfiguration");
    }

    /*
     * Application Properties
     */
    @Bean
    @ConfigurationProperties(prefix = "immport.application", ignoreUnknownFields = false)
    ApplicationProperties applicationProperties() {
        return new ApplicationProperties();
    }

    /*
     * Data Upload Properties
     */
    @Bean
    @ConfigurationProperties(prefix = "immport.data.airrstandard", ignoreUnknownFields = false)
    DataAirrStandardProperties dataAirrStandardProperties() {
        return new DataAirrStandardProperties();
    }

}

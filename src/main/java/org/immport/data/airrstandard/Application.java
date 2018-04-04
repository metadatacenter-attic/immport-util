package org.immport.data.airrstandard;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.HashMap;

import org.immport.data.airrstandard.util.GetParameters;
import org.immport.data.airrstandard.manager.AirrstandardManager;

@SpringBootApplication
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class })
public class Application {

    /** The logger. */
    private static final Logger log = LoggerFactory.getLogger(Application.class);

    //
    // parameters
    //
    private static final String INPUT_MIAIRRSTANDARD_BUILD_DIRECTORY_PARAMETER = "-BD";
    private static final String INPUT_MIAIRRSTANDARD_JSON_FILE_PARAMETER = "-UF";
    private static final String INPUT_MIAIRRSTANDARD_TEMPLATE_NAMES_PARAMETER = "-TP";
    private static final String INPUT_MIAIRRSTANDARD_PACKAGE_NAME_PARAMETER = "-PN";

    public Application() throws Exception {
        log.info("Creating instance of class Application");
    }

    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
        try {
            //
            // Get the Parameters
            //
            GetParameters params = new GetParameters();
            params.setParameter(INPUT_MIAIRRSTANDARD_BUILD_DIRECTORY_PARAMETER);
            params.setParameter(INPUT_MIAIRRSTANDARD_JSON_FILE_PARAMETER);
            params.setParameter(INPUT_MIAIRRSTANDARD_TEMPLATE_NAMES_PARAMETER);
            params.setParameter(INPUT_MIAIRRSTANDARD_PACKAGE_NAME_PARAMETER);
            HashMap<String, String> parameters = params.getParameters(args);

            String miairrJsonFilePath = parameters.get(INPUT_MIAIRRSTANDARD_JSON_FILE_PARAMETER);
            String packageName = parameters.get(INPUT_MIAIRRSTANDARD_PACKAGE_NAME_PARAMETER);
            String buildDirectoryPath = parameters.get(INPUT_MIAIRRSTANDARD_BUILD_DIRECTORY_PARAMETER);
            String templateNames = parameters.get(INPUT_MIAIRRSTANDARD_TEMPLATE_NAMES_PARAMETER);

            AirrstandardManager manager = new AirrstandardManager();
            manager.transformToTemplates(buildDirectoryPath, miairrJsonFilePath, packageName, templateNames);
        } finally {
            context.close();
        }
    }
}

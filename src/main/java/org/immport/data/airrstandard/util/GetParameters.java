package org.immport.data.airrstandard.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetParameters {

    /** The logger. */
    private static final Logger log = LoggerFactory.getLogger(GetParameters.class);
    
    public static final String WORKSPACE_ID_PATTERN = "^\\-?\\d+$";

    private Set<String> toolParameters;
    
    public GetParameters() {
        toolParameters = new HashSet<String>();
    }

    public void setParameter(String parameter) {
        toolParameters.add(parameter);
    }

    public void setParameters(List<String> parameters) {
        for (String parameter : parameters) {
            setParameter(parameter);
        }
    }

    public HashMap<String, String> getParameters(String[] args) {
        HashMap<String, String> parameters = new HashMap<String, String>();
        String currentParam = null;
        for (int i = 0; i < args.length; i++) {
            String param = args[i];
            log.debug("Parameter = " + param);
            if (currentParam == null && toolParameters.contains(param)) {
                currentParam = param;
            } else if (currentParam != null) {
                parameters.put(currentParam, param);
                log.debug("Got option =  " + currentParam + ", value = " + param);
                currentParam = null;
            }
        }
        return parameters;
    }
}

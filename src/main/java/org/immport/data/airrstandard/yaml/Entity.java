package org.immport.data.airrstandard.yaml;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.immport.data.airrstandard.util.AirrStandardConstants;

public class Entity {

    private String discriminator;
    private String type;
    private List<String> required;
    private Map<String, Specification> properties;

    public String getDiscriminator() {
        return discriminator;
    }

    public void setDiscriminator(String discriminator) {
        this.discriminator = discriminator;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getRequired() {
        if (required == null) {
            required = new ArrayList<String>();
        }
        return required;
    }

    public void setRequired(List<String> required) {
        this.required = required;
    }

    public Map<String, Specification> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Specification> properties) {
        this.properties = properties;
    }

    public String toString() {
        StringBuffer str = new StringBuffer(AirrStandardConstants.OPEN_PAREN);
        str.append(String.join(AirrStandardConstants.EQUALS, "discriminator", getDiscriminator()));
        str.append(AirrStandardConstants.COMMA);
        str.append(String.join(AirrStandardConstants.EQUALS, "type", getType()));
        str.append(AirrStandardConstants.COMMA);
        str.append(String.join(AirrStandardConstants.EQUALS, "required",
                AirrStandardConstants.OPEN_BRACKET_LINE + String.join(AirrStandardConstants.COMMA_LINE, getRequired())
                        + AirrStandardConstants.CLOSE_BRACKET_LINE));
        str.append(AirrStandardConstants.COMMA);
        str.append(String.join(AirrStandardConstants.EQUALS, "properties", AirrStandardConstants.OPEN_PAREN));
        int index = 0;
        for (String property : properties.keySet()) {
            str.append(String.join(AirrStandardConstants.EQUALS, property, properties.get(property).toString()));
            index++;
            if (index < properties.keySet().size()) {
                str.append(AirrStandardConstants.COMMA);
            }
        }
        str.append(AirrStandardConstants.CLOSE_PAREN);
        str.append(AirrStandardConstants.CLOSE_PAREN);
        return str.toString();
    }
}

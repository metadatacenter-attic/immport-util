package org.immport.data.airrstandard.yaml;

import java.util.ArrayList;
import java.util.List;

import org.immport.data.airrstandard.util.AirrStandardConstants;

public class Specification {

    private String type;
    private String description;
    private Boolean xmiairr;
    private List<String> enumeration;

    public String getType() {
        if (type == null && getEnumeration().size() != 0) {
            type = "enumeration";
        }
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        if (description == null) {
            description = AirrStandardConstants.EMPTY_STR;
        }
        return description;
    }

    public void setDescription(String description) {
        if (description != null) {
            description = AirrStandardConstants.removeFormFeeds(description);
        }
        this.description = description;
    }

    public Boolean getXmiairr() {
        if (xmiairr == null) {
            xmiairr = false;
        }
        return xmiairr;
    }

    public void setXmiairr(Boolean xmiairr) {
        this.xmiairr = xmiairr;
    }

    public List<String> getEnumeration() {
        if (enumeration == null) {
            enumeration = new ArrayList<String>();
        }
        return enumeration;
    }

    public void setEnumeration(List<String> enumeration) {
        this.enumeration = enumeration;
    }

    public String toString() {
        StringBuffer str = new StringBuffer(AirrStandardConstants.OPEN_PAREN_LINE);
        str.append(String.join(AirrStandardConstants.EQUALS, "type", getType()));
        str.append(AirrStandardConstants.COMMA_LINE);
        str.append(String.join(AirrStandardConstants.EQUALS, "description", getDescription()));
        str.append(AirrStandardConstants.COMMA_LINE);
        str.append(String.join(AirrStandardConstants.EQUALS, "xmiairr", getXmiairr().toString()));
        str.append(AirrStandardConstants.COMMA_LINE);
        str.append(String.join(AirrStandardConstants.EQUALS, "enumeration",
                AirrStandardConstants.OPEN_BRACKET_LINE
                        + String.join(AirrStandardConstants.COMMA_LINE, getEnumeration())
                        + AirrStandardConstants.CLOSE_BRACKET_LINE));
        str.append(AirrStandardConstants.CLOSE_PAREN_LINE);
        return str.toString();

    }

}

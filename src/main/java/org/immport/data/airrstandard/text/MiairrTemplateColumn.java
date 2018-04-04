package org.immport.data.airrstandard.text;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.immport.data.airrstandard.util.AirrStandardConstants;

public class MiairrTemplateColumn {
    /** The logger. */
    private static final Logger log = LoggerFactory.getLogger(MiairrTemplateColumn.class);

    private static int line_num = 1;

    private String miairrEntityName;
    private String miairrColumnName;
    private String datasetName;
    private String targetRepository;
    private String dataType;
    private String contentFormat;
    private String contentDescription;
    private String example;

    private String airrEntityName;
    private String airrColumnName;

    public MiairrTemplateColumn(String[] line, HashMap<String, String> ymlColumnToEntityMap,
            HashMap<String, String> datasetMapping, HashMap<String, String> templateCategoryMapping)
            throws InstantiationException {
        if (line == null) {
            throw new InstantiationException("Line is empty");
        }

        line_num++;
        log.debug("line_num = " + line_num + ", num elements = " + line.length + ", first columm = " + line[0]);
        if (line.length != 7) {
            throw new InstantiationException(
                    "Line does not have seven (7) columns (line_num = " + line_num + ") = '" + line + "'");
        }

        miairrEntityName = line[0];
        String[] components = miairrEntityName.split(AirrStandardConstants.SPACES_PATTERN);
        datasetName = AirrStandardConstants.EMPTY_STR;
        if (datasetMapping.containsKey(components[0])) {
            datasetName = datasetMapping.get(components[0]);
        }
        targetRepository = AirrStandardConstants.EMPTY_STR;
        if (templateCategoryMapping.containsKey(miairrEntityName)) {
            targetRepository = templateCategoryMapping.get(miairrEntityName);
        }

        miairrColumnName = line[1];
        dataType = line[2];
        contentFormat = line[3];
        contentDescription = line[4];
        example = line[5];

        airrColumnName = line[6];
        airrEntityName = ymlColumnToEntityMap.get(airrColumnName);
        if (airrEntityName == null) {
            airrEntityName = AirrStandardConstants.NO_AIRR_ENTITY;
        }
    }

    public String getMiairrEntityName() {
        return miairrEntityName;
    }

    public String getMiairrColumnName() {
        return miairrColumnName;
    }

    public String getDatasetName() {
        return datasetName;
    }

    public String getTargetRepository() {
        return targetRepository;
    }

    public String getDataType() {
        return dataType;
    }

    public String getContentFormat() {
        return contentFormat;
    }

    public String getContentDescription() {
        return contentDescription;
    }

    public String getExample() {
        return example;
    }

    public String getAirrEntityName() {
        return airrEntityName;
    }

    public String getAirrColumnName() {
        return airrColumnName;
    }

    public String toString() {
        StringBuffer str = new StringBuffer(AirrStandardConstants.OPEN_PAREN_LINE);
        str.append(String.join(AirrStandardConstants.EQUALS, "miairrEntityName", getMiairrEntityName()));
        str.append(AirrStandardConstants.COMMA_LINE);
        str.append(String.join(AirrStandardConstants.EQUALS, "miairrColumnName", getMiairrColumnName()));
        str.append(AirrStandardConstants.COMMA_LINE);
        str.append(String.join(AirrStandardConstants.EQUALS, "datasetName", getDatasetName()));
        str.append(AirrStandardConstants.COMMA_LINE);
        str.append(String.join(AirrStandardConstants.EQUALS, "target Repository", getTargetRepository()));
        str.append(AirrStandardConstants.COMMA_LINE);
        str.append(String.join(AirrStandardConstants.EQUALS, "airrEntityName", getAirrEntityName()));
        str.append(AirrStandardConstants.COMMA_LINE);
        str.append(String.join(AirrStandardConstants.EQUALS, "airrColumnName", getAirrColumnName()));
        str.append(AirrStandardConstants.COMMA_LINE);
        str.append(String.join(AirrStandardConstants.EQUALS, "dataType", getDataType()));
        str.append(AirrStandardConstants.COMMA_LINE);
        str.append(String.join(AirrStandardConstants.EQUALS, "contentFormat", getContentFormat()));
        str.append(AirrStandardConstants.COMMA_LINE);
        str.append(String.join(AirrStandardConstants.EQUALS, "contentDescription", getContentDescription()));
        str.append(AirrStandardConstants.COMMA_LINE);
        str.append(String.join(AirrStandardConstants.EQUALS, "example", getExample()));
        str.append(AirrStandardConstants.CLOSE_PAREN_LINE);
        return str.toString();
    }

    public static String[] getTabHeader() {
        String[] tabString = new String[] { "Miairr Entity", "Miairr Data Set", "Mirairr Target Repository",
                "Miairr Column", "Airr Entity", "Airr Column", "Data" };
        return tabString;

    }

    public String[] toTabString() {
        String[] tabString = new String[] { getMiairrEntityName(), getDatasetName(), getTargetRepository(),
                getMiairrColumnName(), getAirrEntityName(), getAirrColumnName(), toString() };
        return tabString;

    }

}

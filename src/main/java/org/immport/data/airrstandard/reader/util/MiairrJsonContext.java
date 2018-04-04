package org.immport.data.airrstandard.reader.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.immport.data.airrstandard.text.MiairrTemplateColumn;
import org.immport.data.airrstandard.util.AirrStandardConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MiairrJsonContext {

    /** The logger. */
    private static final Logger log = LoggerFactory.getLogger(MiairrJsonContext.class);

    //
    // Attributes In Original JSON File
    //
    private static final String CONTEXT_ATTRIBUTE = "@context";
    private static final String FILE_NAME_ATTRIBUTE = "File Name";
    private static final String FILE_TYPE_ATTRIBUTE = "File Type";
    private static final String ID_ATTRIBUTE = "@id";
    private static final String LABEL_ATTRIBUTE = "rdfs:label";
    private static final String SRA_MULTIPLE_FILE_UPLOAD_ATTRIBUTES = "sra multiple file upload attributes";
    private static final String SYNTHETIC_LIBRARY_ATTIBUTE = "synthetic";
    private static final String TYPE_ATTRIBUTE = "@type";
    private static final String VALUE_ATTRIBUTE = "@value";
    
    private static final String SEQUENCE_READ_ARCHIVE_INPUT_DATA_COLUMN_NAME = "sequence read archive";
    private static final String SEQUENCE_READ_ARCHIVE_CONTEXT_COLUMN_NAME = "SRA";

    //
    // Column Names After Generating Context Map
    //
    private static final String SRA_MULTIPLE_FILE_UPLOAD_COLUMN_NAME = "sraMultipleFileUploadAttributes";

    //
    // Special Attributes
    //
    private static final HashSet<String> specialAttributes = new HashSet<String>();
    private static final HashMap<String, String> specialAttributeKeyNames = new HashMap<String, String>();
    static {
        specialAttributes.add(SYNTHETIC_LIBRARY_ATTIBUTE);
        specialAttributes.add(SRA_MULTIPLE_FILE_UPLOAD_ATTRIBUTES);
        specialAttributeKeyNames.put(SYNTHETIC_LIBRARY_ATTIBUTE, SYNTHETIC_LIBRARY_ATTIBUTE);
        specialAttributeKeyNames.put(SRA_MULTIPLE_FILE_UPLOAD_ATTRIBUTES, SRA_MULTIPLE_FILE_UPLOAD_COLUMN_NAME);
    }

    private static HashSet<String> targetRepositories;
    private static HashMap<String, String> miairrColumnToAirrColumn;

    public static void setMappings(HashMap<String, List<MiairrTemplateColumn>> miairrTemplates) {
        targetRepositories = new HashSet<String>();
        miairrColumnToAirrColumn = new HashMap<String, String>();
        for (String template : miairrTemplates.keySet()) {
            for (MiairrTemplateColumn column : miairrTemplates.get(template)) {
                targetRepositories.add(column.getTargetRepository().toLowerCase());
                miairrColumnToAirrColumn.put(column.getMiairrColumnName().toLowerCase(), column.getAirrColumnName());
            }
        }
    }

    public static HashMap<String, Object> generateContext(HashMap<String, Object> miairrJson) {
        HashMap<String, Object> miairrJsonContext = new HashMap<String, Object>();
        if (miairrJson == null) {
            return miairrJsonContext;
        }
        log.debug("miairrJson length = " + miairrJson.keySet().size());
        for (String key : miairrJson.keySet()) {
            Object value = miairrJson.get(key);
            //
            // Change the input key to context key for downstream processing...
            //
            if (key.toLowerCase().equals(SEQUENCE_READ_ARCHIVE_INPUT_DATA_COLUMN_NAME)) {
                key = SEQUENCE_READ_ARCHIVE_CONTEXT_COLUMN_NAME;
            }
            log.debug("MiAIRR Json file key = " + key);
            //
            // Do not process keys that are not needed...
            //
            if (skipKey(key)) {
                continue;
            }
            //
            // Convert key to proper key name for downstream processing...
            //
            String keyName = getKeyName(key);
            log.debug(AirrStandardConstants.NEWLINE + "(0)KEY = " + key + ", class = "
                    + value.getClass().getCanonicalName() + AirrStandardConstants.NEWLINE);
            if (value instanceof ArrayList) {
                List<HashMap<String, Object>> subcontext = new ArrayList<HashMap<String, Object>>();
                miairrJsonContext.put(keyName, subcontext);
                ArrayList<Object> newValue = (ArrayList<Object>) value;
                log.debug(AirrStandardConstants.NEWLINE + "(1)KEY(" + key + ")(ArrayList) length = " + newValue.size()
                        + AirrStandardConstants.NEWLINE);
                for (Object object : newValue) {
                    HashMap<String, Object> subsubcontext = generateLinkedHashMap(object);
                    if (subsubcontext != null) {
                        subcontext.add(subsubcontext);
                    }
                }
            } else if (value instanceof String) {
                String newValue = (String) value;
                log.debug(AirrStandardConstants.NEWLINE + "(2)KEY(" + key + ")(String) value = " + newValue
                        + AirrStandardConstants.NEWLINE);
                miairrJsonContext.put(keyName, StringUtils.trimToEmpty(newValue));
                log.debug("ADDING - (key, value) = (" + String.join(AirrStandardConstants.COMMA_SPACE, key, newValue)
                        + ")");
            } else if (value instanceof LinkedHashMap) {
                HashMap<String, Object> subcontext = generateLinkedHashMap(value);
                miairrJsonContext.put(keyName, subcontext);
            }
        }
        return miairrJsonContext;
    }

    private static HashMap<String, Object> generateLinkedHashMap(Object object) {
        HashMap<String, Object> context = null;
        if (!(object instanceof LinkedHashMap)) {
            return context;
        }
        context = new HashMap<String, Object>();
        LinkedHashMap<String, Object> newObject = (LinkedHashMap<String, Object>) object;
        log.debug(AirrStandardConstants.NEWLINE + "(1)SUB KEY(LinkedHashMap)" + AirrStandardConstants.NEWLINE);
        for (String newKey : newObject.keySet()) {
            if (skipKey(newKey)) {
                continue;
            }
            String newKeyName = getKeyName(newKey);
            Object newObj = newObject.get(newKey);
            if (newObj instanceof LinkedHashMap) {
                context.put(newKeyName, getValue(newObj));
                log.debug("ADDING LinkedHashMap - (key, value) = ("
                        + String.join(AirrStandardConstants.COMMA_SPACE, newKey, context.get(newKeyName).toString())
                        + ")");
            } else if (newObj instanceof ArrayList) {
                getValue(context, newKeyName, newObj);
            } else if (newObj instanceof String) {
                String newStr = (String) newObj;
                context.put(newKeyName, StringUtils.trimToEmpty(newStr));
                log.debug("ADDING - (key, value) = ("
                        + String.join(AirrStandardConstants.COMMA_SPACE, newKey, context.get(newKeyName).toString())
                        + ")");
            }
            log.debug(AirrStandardConstants.NEWLINE + "(1)NKEY(" + newKey + ")(String) class = "
                    + newObject.get(newKey).getClass().getCanonicalName() + ", " + AirrStandardConstants.NEWLINE
                    + "(1)value = " + newObject.get(newKey) + AirrStandardConstants.NEWLINE);
        }
        return context;
    }

    private static String getValue(Object object) {
        String value = AirrStandardConstants.EMPTY_STR;
        if (object instanceof LinkedHashMap) {
            LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) object;
            if (map.containsKey(VALUE_ATTRIBUTE)) {
                log.debug("Contains Value Attribute!! = " + VALUE_ATTRIBUTE);
                value = (String) map.get(VALUE_ATTRIBUTE);
            } else if (map.containsKey(LABEL_ATTRIBUTE)) {
                log.debug("Contains Label Attribute!! = " + LABEL_ATTRIBUTE);
                value = (String) map.get(LABEL_ATTRIBUTE);
            } else if (map.containsKey(TYPE_ATTRIBUTE)) {
                log.debug("Contains Type Attribute!! = " + TYPE_ATTRIBUTE);
                value = (String) map.get(TYPE_ATTRIBUTE);
            }
            log.debug("VALUE = " + value);
        }
        return StringUtils.trimToEmpty(value);
    }

    private static void getValue(HashMap<String, Object> context, String keyName, Object object) {
        if (!(object instanceof ArrayList)) {
            return;
        }
        log.debug("Processing array list for key = " + keyName);
        List<Object> list = (ArrayList<Object>) object;
        HashMap<String, String> fileMap = null;
        if (keyName.equals(SRA_MULTIPLE_FILE_UPLOAD_ATTRIBUTES)) {
            log.debug("Processing array list for optional biosample attributes = " + keyName);
            fileMap = new HashMap<String, String>();
            context.put(specialAttributeKeyNames.get(keyName), fileMap);
        }
        if (specialAttributes.contains(keyName)) {
            for (Object obj : list) {
                LinkedHashMap<String, Object> optionalAttributes = (LinkedHashMap<String, Object>) obj;
                if (keyName.equals(SYNTHETIC_LIBRARY_ATTIBUTE)) {
                    String value = getValue(optionalAttributes);
                    context.put(specialAttributeKeyNames.get(keyName), value);
                } else if (keyName.equals(SRA_MULTIPLE_FILE_UPLOAD_ATTRIBUTES)) {
                    String fileName = getValue(optionalAttributes.get(FILE_NAME_ATTRIBUTE));
                    String fileType = getValue(optionalAttributes.get(FILE_TYPE_ATTRIBUTE));
                    fileMap.put(fileName, fileType);
                }
            }
        }
    }

    private static Boolean skipKey(String key) {
        String lcKey = key.toLowerCase();
        if (lcKey.equals(CONTEXT_ATTRIBUTE) || lcKey.equals(ID_ATTRIBUTE)
                || lcKey.contains(AirrStandardConstants.COLON)) {
            return true;
        } else if (targetRepositories.contains(lcKey) || miairrColumnToAirrColumn.containsKey(lcKey)) {
            return false;
        } else if (specialAttributes.contains(lcKey)) {
            log.debug("special attribute appears = " + key);
            return false;
        }
        return true;
    }

    private static String getKeyName(String key) {
        String keyName = AirrStandardConstants.EMPTY_STR;
        key = key.toLowerCase();
        if (targetRepositories.contains(key)) {
            keyName = key;
        } else if (miairrColumnToAirrColumn.containsKey(key)) {
            keyName = miairrColumnToAirrColumn.get(key);
        } else if (specialAttributes.contains(key)) {
            log.debug("Special attributes key name = " + key);
            keyName = key;
        }
        return keyName;
    }

}

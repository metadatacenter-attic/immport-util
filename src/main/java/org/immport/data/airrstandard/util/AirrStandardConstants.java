package org.immport.data.airrstandard.util;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AirrStandardConstants {

    /** The logger. */
    private static final Logger log = LoggerFactory.getLogger(AirrStandardConstants.class);

    public static final String COLON = ":";
    public static final String COMMA_SPACE = ", ";
    public static final String CTRL_M = "\r";
    public static final String DOT = ".";
    public static final String EMPTY_STR = "";
    public static final String EQUALS = "=";
    public static final String FILE_ENCODING = "UTF8";
    public static final String NEWLINE = "\n";
    public static final String SEMI_COLON = ";";
    public static final String SEMI_COLON_SPACE = "; ";
    public static final String SINGLE_QUOTE = "'";
    public static final String SLASH = "/";
    public static final String SPACE = " ";
    public static final String TAB = "\t";
    public static final String UNDERSCORE = "_";

    public static final String CONTEXT_INFIX = "context";

    public static final String JSON_SUFFIX = "json";
    public static final String TXT_SUFFIX = "txt";
    public static final String ZIP_SUFFIX = "zip";

    public static final String AIRR_SPECIFICATION_FILE = "AirrTemplates";
    public static final String MIAIRR_SPECIFICATION_FILE = "MiairrTemplates";

    private static final HashMap<String, String> SPECIFICATION_FILES = new HashMap<String, String>();
    static {
        SPECIFICATION_FILES.put(AIRR_SPECIFICATION_FILE, String.join(DOT, AIRR_SPECIFICATION_FILE, TXT_SUFFIX));
        SPECIFICATION_FILES.put(MIAIRR_SPECIFICATION_FILE, String.join(DOT, MIAIRR_SPECIFICATION_FILE, TXT_SUFFIX));
    }

    public static String getFile(String fileName) {
        return SPECIFICATION_FILES.get(fileName);
    }

    public static final String NO_AIRR_ENTITY = "NO AIRR Entity";
    public static final String NO_NCBI_ATTRIBUTE = "NO NCBI ATTRIBUTE";

    public static final String CLOSE_CURLY_BRACKET_PATTERN = "\\}";
    public static final String CLOSE_BRACKET_PATTERN = "\\]";
    public static final String CLOSE_PAREN_PATTERN = "\\)";
    public static final String COLON_PATTERN = COLON;
    public static final String COMMA_PATTERN = ",";
    public static final String DOT_PATTERN = "\\.";
    public static final String DOUBLE_QUOTE_PATTERN = "\"";
    public static final String END_SPACES_PATTERN = SPACE + "+$";
    public static final String JSON_SUFFIX_PATTERN = DOT_PATTERN + JSON_SUFFIX + "$";
    public static final String OPEN_CURLY_BRACKET_PATTERN = "\\{";
    public static final String OPEN_BRACKET_PATTERN = "\\[";
    public static final String OPEN_PAREN_PATTERN = "\\(";
    public static final String POSITIVE_INTEGER_PATTERN = "\\d+";
    public static final String SEMI_COLON_PATTERN = SEMI_COLON;
    public static final String SPACES_PATTERN = SPACE + "+";
    public static final String UNDERSCORE_PATTERN = UNDERSCORE;

    public static String removeFormFeeds(String str) {
        return str.replaceAll(CTRL_M + NEWLINE, SPACE).replaceAll(NEWLINE, SPACE).replaceAll(CTRL_M, SPACE)
                .replaceAll(SPACES_PATTERN, SPACE).replaceFirst(END_SPACES_PATTERN, EMPTY_STR);
    }

    public static String upperCaseFirst(String value) {
        // Convert String to char array.
        char[] array = value.toLowerCase().toCharArray();
        // Modify first element in array.
        array[0] = Character.toUpperCase(array[0]);
        // Return string.
        return new String(array);
    }

    //
    // For toString Methods
    //
    public static final String CLOSE_BRACKET_LINE = "]";
    public static final String CLOSE_PAREN_LINE = "}";
    public static final String COMMA_LINE = ",";
    public static final String OPEN_BRACKET_LINE = "[";
    public static final String OPEN_PAREN_LINE = "{";

    public static final String CLOSE_BRACKET = "\n]\n";
    public static final String CLOSE_PAREN = "\n}\n";
    public static final String COMMA = ",\n";
    public static final String OPEN_BRACKET = "[\n";
    public static final String OPEN_PAREN = "{\n";

}

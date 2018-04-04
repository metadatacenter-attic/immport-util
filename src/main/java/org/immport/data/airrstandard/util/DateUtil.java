package org.immport.data.airrstandard.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class DateUtil.
 * 
 * @author BISC-Team
 */
public class DateUtil {

     /** The logger. */
    private static final Logger log = LoggerFactory.getLogger(DateUtil.class);

    private static final String EMPTY_STR = "";
    //
    // Date Time Format
    //
    private static final String IMMPORT_DATE_TIME_FORMAT = "yyyy-MM-dd-HH:mm:ss";
    private static final SimpleDateFormat dateTimeFormatter = new SimpleDateFormat(IMMPORT_DATE_TIME_FORMAT);
    private static final String IMMPORT_DATE_TIME_FORMAT_PATTERN = "^\\d+-\\d+-\\d+-\\d+:\\d+:\\d+$";

    private static final String IMMPORT_DATE_TIME_FORMAT_ONE = "yyyy-MM-dd.HH-mm-ss";
    private static final SimpleDateFormat dateTimeFormatterOne = new SimpleDateFormat(IMMPORT_DATE_TIME_FORMAT_ONE);

    private static final String UPLOAD_REGISTRATION_DATE_FORMAT = "yyyyMMdd";
    private static final SimpleDateFormat uploadRegistrationDateFormat = new SimpleDateFormat(UPLOAD_REGISTRATION_DATE_FORMAT);

    private static final String SYSTEM_DATE_FORMAT = "dd-MMM-yyyy";
    private static final SimpleDateFormat systemDateFormat = new SimpleDateFormat(SYSTEM_DATE_FORMAT);

    public static String getDateTimeFormatForNow() {
        return dateTimeFormatter.format(Calendar.getInstance().getTime());
    }

    public static String getUploadRegistrationDate() {
        return uploadRegistrationDateFormat.format(Calendar.getInstance().getTime());
    }

    public static String getDateTimeFormatForNowForFileName() {
        return dateTimeFormatterOne.format(Calendar.getInstance().getTime());
    }

    public static String getSystemDate() {
        return systemDateFormat.format(Calendar.getInstance().getTime());
    }

    public static String getDateTimeFormatForFile(File file) {
        return dateTimeFormatterOne.format(new Date(file.lastModified()));
    }

    //
    // Date Specifics
    //
    private static final String DATE_COMPONENT_SEPARATOR = "-";

    /** The Constant SHORT_YEAR_DATE_FORMAT **/
    private static final String SHORT_YEAR_DATE_FORMAT = "dd-MMM-yy";
    private static final SimpleDateFormat shortYeardateFormatter = new SimpleDateFormat(SHORT_YEAR_DATE_FORMAT);

    /** The Constant LONG_YEAR_DATE_FORMAT **/
    private static final String LONG_YEAR_DATE_FORMAT = "dd-MMM-yyyy";
    private static final SimpleDateFormat longYeardateFormatter = new SimpleDateFormat(LONG_YEAR_DATE_FORMAT);

    static {
        longYeardateFormatter.setLenient(false);
        shortYeardateFormatter.setLenient(false);
        dateTimeFormatter.setLenient(false);
    }

    private static String[] getDateComponents(String date) {
        if (date == null || date.equals(EMPTY_STR)) {
            return null;
        }
        String[] dateComps = StringUtils.splitByWholeSeparator(date, DATE_COMPONENT_SEPARATOR);
        if (date.matches(IMMPORT_DATE_TIME_FORMAT_PATTERN)) {
            // OK!
        } else if (dateComps.length != 3 || (dateComps[2].length() != 2 && dateComps[2].length() != 4)) {
            return null;
        }
        return dateComps;
    }

    public static SimpleDateFormat getSimpleDateFormatter(String date) {
        String[] dateComps = getDateComponents(date);
        if (dateComps == null) {
            return null;
        }
        SimpleDateFormat simpleDateFormatter = null;
        if (date.matches(IMMPORT_DATE_TIME_FORMAT_PATTERN)) {
            simpleDateFormatter = dateTimeFormatter;
        } else if (dateComps[2].length() == 2) {
            simpleDateFormatter = shortYeardateFormatter;
        } else if (dateComps[2].length() == 4) {
            simpleDateFormatter = longYeardateFormatter;
        }
        return simpleDateFormatter;
    }

    public static String getDateFormat(String date) {
        String[] dateComps = getDateComponents(date);
        if (dateComps == null) {
            return null;
        }
        String dateFormat = null;
        if (dateComps[2].length() == 2) {
            dateFormat = SHORT_YEAR_DATE_FORMAT;
        } else if (dateComps[2].length() == 4) {
            dateFormat = LONG_YEAR_DATE_FORMAT;
        }
        return dateFormat;
    }

    public static String getDateFormat() {
        return "Date Format = " + SHORT_YEAR_DATE_FORMAT + " or " + LONG_YEAR_DATE_FORMAT;
    }
}

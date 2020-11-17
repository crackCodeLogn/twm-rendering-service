package com.vv.personal.twm.render.util;

/**
 * @author Vivek
 * @since 17/11/20
 */
public class JsonUtil {

    public static String[] extractRecordsFromString(String data) {
        return data.substring(1, data.length() - 1).replace("}, {", "},, {").split(",, ");
    }
}

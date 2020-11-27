package com.vv.personal.twm.render.util;

import com.google.gson.Gson;

/**
 * @author Vivek
 * @since 17/11/20
 */
public class JsonUtil {
    public static final Gson GSON = new Gson();

    public static String[] extractRecordsFromString(String data) {
        return data.substring(1, data.length() - 1).replaceAll("}, ", "}~").split("~");
    }
}

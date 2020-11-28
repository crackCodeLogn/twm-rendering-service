package com.vv.personal.twm.render.engine;

import static com.vv.personal.twm.render.constants.Constants.*;

/**
 * @author Vivek
 * @since 29/11/20
 */
public abstract class Rend {

    private static void addCells(String FORMAT, StringBuilder table, Object... vals) {
        table.append(HTML_TABLE_ROW_START);
        for (Object val : vals) table.append(String.format(FORMAT, val));
        table.append(HTML_TABLE_ROW_END);
    }

    protected static void addHeaderCells(StringBuilder table, Object... vals) {
        addCells(HTML_TABLE_HEADER, table, vals);
    }

    protected static void addRowCells(StringBuilder table, Object... vals) {
        addCells(HTML_TABLE_CELL, table, vals);
    }
}

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


    protected static void startRow(StringBuilder table) {
        table.append(HTML_TABLE_ROW_START);
    }

    protected static void endRow(StringBuilder table) {
        table.append(HTML_TABLE_ROW_END);
    }

    private static void addUnboundedCells(String FORMAT, StringBuilder table, Object... vals) {
        for (Object val : vals) table.append(String.format(FORMAT, val));
    }

    protected static void addUnboundedHeaderCells(StringBuilder table, Object... vals) {
        addUnboundedCells(HTML_TABLE_HEADER, table, vals);
    }

    protected static void addUnboundedRowCells(StringBuilder table, Object... vals) {
        addUnboundedCells(HTML_TABLE_CELL, table, vals);
    }
}

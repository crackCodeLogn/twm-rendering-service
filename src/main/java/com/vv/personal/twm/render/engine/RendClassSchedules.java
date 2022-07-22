package com.vv.personal.twm.render.engine;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.vv.personal.twm.artifactory.generated.scheduler.ClassScheduler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.vv.personal.twm.render.constants.Constants.HTML_TABLE_END;
import static com.vv.personal.twm.render.constants.Constants.HTML_TABLE_START;

/**
 * @author Vivek
 * @since 22/07/22
 */
public class RendClassSchedules extends Rend {
    private static final Logger LOGGER = LoggerFactory.getLogger(RendClassSchedules.class);

    public static String generateTable(ClassScheduler.ClassCells classes) {
        final StringBuilder table = new StringBuilder(HTML_TABLE_START);
        addHeaderCells(table,
                "Class",
                "M",
                "T",
                "W",
                "Th",
                "F");

        Table<String, String, StringBuilder> scheduleTable = HashBasedTable.create();
        classes.getClassCellsList().forEach(scheduledClass -> {
            String code = scheduledClass.getCode();
            String day = scheduledClass.getDay();
            String timeSlot = String.format("<tr><td>%s-%s</td></tr>", scheduledClass.getStartTime(), scheduledClass.getEndTime());

            if (scheduleTable.contains(code, day)) {
                scheduleTable.put(code, day, scheduleTable.get(code, day).append(timeSlot));
            } else {
                scheduleTable.put(code, day, new StringBuilder("<table>").append(timeSlot));
            }
        });
        LOGGER.info("Generated schedule table: {}", scheduleTable);

        scheduleTable.rowKeySet().forEach(code -> {
            try {
                addRowCells(table,
                        code,
                        getSafeData(scheduleTable, code, "M").append("</table>"),
                        getSafeData(scheduleTable, code, "T").append("</table>"),
                        getSafeData(scheduleTable, code, "W").append("</table>"),
                        getSafeData(scheduleTable, code, "Th").append("</table>"),
                        getSafeData(scheduleTable, code, "F").append("</table>"));
            } catch (Exception e) {
                LOGGER.error("Failed to convert '{}' to HTML. Skipping. ", code, e);
            }
        });
        table.append(HTML_TABLE_END);
        LOGGER.info("Rendering finished for {} cells", classes.getClassCellsCount());
        return table.toString();
    }

    private static StringBuilder getSafeData(Table<String, String, StringBuilder> table, String row, String column) {
        StringBuilder result = table.get(row, column);
        return StringUtils.isEmpty(result) ? new StringBuilder("<table>") : result;
    }
}
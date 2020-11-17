package com.vv.personal.twm.render.engine;

import com.google.gson.Gson;
import com.vv.personal.twm.artifactory.bank.Bank;
import com.vv.personal.twm.render.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.vv.personal.twm.render.constants.Constants.*;

/**
 * @author Vivek
 * @since 17/11/20
 */
public class RendBank {
    private static final Logger LOGGER = LoggerFactory.getLogger(RendBank.class);

    private static final Gson GSON = new Gson();

    public static String generateTable(String entireJsonList) {
        String[] jsons = JsonUtil.extractRecordsFromString(entireJsonList);
        StringBuilder table = new StringBuilder(HTML_TABLE_START);
        table.append(HTML_TABLE_ROW_START)
                .append(String.format(HTML_TABLE_HEADER, "Name"))
                .append(String.format(HTML_TABLE_HEADER, "Type"))
                .append(String.format(HTML_TABLE_HEADER, "IFSC"))
                .append(String.format(HTML_TABLE_HEADER, "Contact"))
                .append(HTML_TABLE_ROW_END);

        for (String json : jsons) {
            try {
                Bank bank = GSON.fromJson(json, Bank.class);
                LOGGER.info(bank.toString());
                table.append(HTML_TABLE_ROW_START);
                table.append(String.format(HTML_TABLE_CELL, bank.getName()));
                table.append(String.format(HTML_TABLE_CELL, bank.getType()));
                table.append(String.format(HTML_TABLE_CELL, bank.getIFSC()));
                table.append(String.format(HTML_TABLE_CELL, bank.getContactNumber()));
                table.append(HTML_TABLE_ROW_END);
            } catch (Exception e) {
                LOGGER.error("Failed to parse '{}' to Bank. Skipping. ", json, e);
            }
        }
        table.append(HTML_TABLE_END);
        return table.toString();
    }
}

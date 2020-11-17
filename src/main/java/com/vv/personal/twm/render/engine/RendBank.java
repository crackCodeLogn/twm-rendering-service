package com.vv.personal.twm.render.engine;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vv.personal.twm.artifactory.bank.Bank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.Collection;

import static com.vv.personal.twm.render.constants.Constants.*;

/**
 * @author Vivek
 * @since 17/11/20
 */
public class RendBank {
    private static final Logger LOGGER = LoggerFactory.getLogger(RendBank.class);

    private static final Gson GSON = new Gson();

    public static String generateTable(String entireJsonList) {
        //https://sites.google.com/site/gson/gson-user-guide#TOC-Collections-Examples
        //Helps in extracting list of Bank from the combined json input string
        Type collectionType = new TypeToken<Collection<Bank>>() {
        }.getType();
        Collection<Bank> banks = GSON.fromJson(entireJsonList, collectionType);

        StringBuilder table = new StringBuilder(HTML_TABLE_START);
        table.append(HTML_TABLE_ROW_START)
                .append(String.format(HTML_TABLE_HEADER, "Name"))
                .append(String.format(HTML_TABLE_HEADER, "Type"))
                .append(String.format(HTML_TABLE_HEADER, "IFSC"))
                .append(String.format(HTML_TABLE_HEADER, "Contact"))
                .append(HTML_TABLE_ROW_END);

        banks.forEach(bank -> {
            try {
                LOGGER.info(bank.toString());
                table.append(HTML_TABLE_ROW_START);
                table.append(String.format(HTML_TABLE_CELL, bank.getName()));
                table.append(String.format(HTML_TABLE_CELL, bank.getType()));
                table.append(String.format(HTML_TABLE_CELL, bank.getIFSC()));
                table.append(String.format(HTML_TABLE_CELL, bank.getContactNumber()));
                table.append(HTML_TABLE_ROW_END);
            } catch (Exception e) {
                LOGGER.error("Failed to convert '{}' to HTML. Skipping. ", bank.getName(), e);
            }
        });
        table.append(HTML_TABLE_END);
        return table.toString();
    }
}

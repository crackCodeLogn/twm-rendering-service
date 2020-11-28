package com.vv.personal.twm.render.engine;

import com.google.protobuf.util.JsonFormat;
import com.vv.personal.twm.artifactory.generated.bank.BankProto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import static com.vv.personal.twm.render.constants.Constants.*;
import static com.vv.personal.twm.render.util.JsonUtil.extractRecordsFromString;

/**
 * @author Vivek
 * @since 17/11/20
 */
public class RendBank extends Rend {
    private static final Logger LOGGER = LoggerFactory.getLogger(RendBank.class);

    public static String generateTable(BankProto.BankList bankList) {
        final StringBuilder table = new StringBuilder(HTML_TABLE_START);

        addHeaderCells(table,
                "Number",
                "Name",
                "Type",
                "IFSC",
                "Contact");

        AtomicInteger counter = new AtomicInteger(0);
        bankList.getBanksList().forEach(bank -> {
            try {
                //LOGGER.info(bank.toString()); //getting too verbose
                addRowCells(table,
                        counter.incrementAndGet(),
                        bank.getName(),
                        bank.getBankType(),
                        bank.getIFSC(),
                        bank.getContactNumber());
            } catch (Exception e) {
                LOGGER.error("Failed to convert '{}' to HTML. Skipping. ", bank, e);
            }
        });
        table.append(HTML_TABLE_END);
        LOGGER.info("Rendering finished for {} banks", bankList.getBanksCount());
        return table.toString();
    }

    @Deprecated
    public static String generateTable(String entireJsonList) {
        final String[] bankSplit = extractRecordsFromString(entireJsonList);
        //https://sites.google.com/site/gson/gson-user-guide#TOC-Collections-Examples
        //Helps in extracting list of Bank from the combined json input string
        /*Type collectionType = new TypeToken<Collection<BankProto.Bank>>() {
        }.getType();
        Collection<BankProto.Bank> banks = GSON.fromJson(entireJsonList, collectionType);*/

        /*BankListProto.BankList.Builder builder2 = BankListProto.BankList.newBuilder();
        try {
            JsonFormat.parser().merge(entireJsonList, builder2);
            System.out.println(builder2.build().getBankListList().get(0).getIFSC());

        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }*/

        StringBuilder table = new StringBuilder(HTML_TABLE_START);
        table.append(HTML_TABLE_ROW_START)
                .append(String.format(HTML_TABLE_HEADER, "Name"))
                .append(String.format(HTML_TABLE_HEADER, "Type"))
                .append(String.format(HTML_TABLE_HEADER, "IFSC"))
                .append(String.format(HTML_TABLE_HEADER, "Contact"))
                .append(HTML_TABLE_ROW_END);

        Arrays.stream(bankSplit).forEach(bankJson -> {
            try {
                BankProto.Bank.Builder builder = BankProto.Bank.newBuilder();
                JsonFormat.parser().ignoringUnknownFields().merge(bankJson, builder);
                BankProto.Bank bank = builder.build();
                LOGGER.info(bank.toString());
                table.append(HTML_TABLE_ROW_START);
                table.append(String.format(HTML_TABLE_CELL, bank.getName()));
                table.append(String.format(HTML_TABLE_CELL, bank.getBankType()));
                table.append(String.format(HTML_TABLE_CELL, bank.getIFSC()));
                table.append(String.format(HTML_TABLE_CELL, bank.getContactNumber()));
                table.append(HTML_TABLE_ROW_END);
            } catch (Exception e) {
                LOGGER.error("Failed to convert '{}' to HTML. Skipping. ", "Adf", e);
            }
        });
        table.append(HTML_TABLE_END);
        LOGGER.info("Rendering finished for {} banks", bankSplit.length);
        return table.toString();
    }
}

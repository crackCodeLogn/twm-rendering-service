package com.vv.personal.twm.render.engine;

import com.vv.personal.twm.artifactory.generated.deposit.FixedDepositProto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

import static com.vv.personal.twm.render.constants.Constants.HTML_TABLE_END;
import static com.vv.personal.twm.render.constants.Constants.HTML_TABLE_START;

/**
 * @author Vivek
 * @since 29/11/20
 */
public class RendFixedDeposit extends Rend {
    private static final Logger LOGGER = LoggerFactory.getLogger(RendFixedDeposit.class);

    public static String generateTable(FixedDepositProto.FixedDepositList fixedDepositList) {
        final StringBuilder table = new StringBuilder(HTML_TABLE_START);
        addHeaderCells(table,
                "Number",
                "User",
                "Bank IFSC",
                "Deposit Amount",
                "Rate of interest",
                "Start Date",
                "Months",
                "Days",
                "Nominee",
                "InsertionTime",
                "Key");

        AtomicInteger counter = new AtomicInteger(0);
        fixedDepositList.getFixedDepositsList().forEach(fixedDeposit -> {
            try {
                //LOGGER.info(fixedDeposit.toString()); //getting too verbose
                addRowCells(table,
                        counter.incrementAndGet(),
                        fixedDeposit.getUser(),
                        fixedDeposit.getBankIFSC(),
                        fixedDeposit.getDepositAmount(),
                        fixedDeposit.getRateOfInterest(),
                        fixedDeposit.getStartDate(),
                        fixedDeposit.getMonths(),
                        fixedDeposit.getDays(),
                        fixedDeposit.getNominee(),
                        fixedDeposit.getInsertionTime(),
                        fixedDeposit.getKey());
            } catch (Exception e) {
                LOGGER.error("Failed to convert '{}' to HTML. Skipping. ", fixedDeposit, e);
            }
        });
        table.append(HTML_TABLE_END);
        LOGGER.info("Rendering finished for {} fixed deposits", fixedDepositList.getFixedDepositsCount());
        return table.toString();
    }
}

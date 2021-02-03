package com.vv.personal.twm.render.engine;

import com.vv.personal.twm.artifactory.generated.deposit.FixedDepositProto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

import static com.vv.personal.twm.render.constants.Constants.HTML_TABLE_END;
import static com.vv.personal.twm.render.constants.Constants.HTML_TABLE_START;
import static com.vv.personal.twm.render.util.DoubleFormatterUtil.formatDouble;

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
                "FD Number (Key)",
                "Cust Id",
                "Bank IFSC",
                "Deposit Amount",
                "Rate of interest",
                "Start Date",
                "End Date",
                "Months",
                "Days",
                "Earned Interest",
                "Total Amount",
                "Nominee");

        AtomicInteger counter = new AtomicInteger(0);
        fixedDepositList.getFixedDepositsList().forEach(fixedDeposit -> {
            try {
                //LOGGER.info(fixedDeposit.toString()); //getting too verbose
                addRowCells(table,
                        counter.incrementAndGet(),
                        fixedDeposit.getUser(),
                        fixedDeposit.getFdNumber(),
                        fixedDeposit.getCustomerId(),
                        fixedDeposit.getBankIFSC(),
                        fixedDeposit.getDepositAmount(),
                        fixedDeposit.getRateOfInterest(),
                        fixedDeposit.getStartDate(),
                        fixedDeposit.getEndDate(),
                        fixedDeposit.getMonths(),
                        fixedDeposit.getDays(),
                        formatDouble(fixedDeposit.getExpectedInterest()),
                        formatDouble(fixedDeposit.getExpectedAmount()),
                        fixedDeposit.getNominee());
            } catch (Exception e) {
                LOGGER.error("Failed to convert '{}' to HTML. Skipping. ", fixedDeposit, e);
            }
        });
        table.append(HTML_TABLE_END);
        LOGGER.info("Rendering finished for {} fixed deposits", fixedDepositList.getFixedDepositsCount());
        return table.toString();
    }
}

package com.vv.personal.twm.render.engine;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.vv.personal.twm.artifactory.generated.deposit.FixedDepositProto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static com.vv.personal.twm.render.constants.Constants.*;
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
        fixedDepositList.getFixedDepositList().forEach(fixedDeposit -> {
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
        LOGGER.info("Rendering finished for {} fixed deposits", fixedDepositList.getFixedDepositCount());
        return table.toString();
    }

    public static String generateTableWithAnnualBreakdown(FixedDepositProto.FixedDepositList fixedDepositList) {
        Table<String, String, FixedDepositProto.AnnualBreakdown> annualBreakdownTable = HashBasedTable.create();
        fixedDepositList.getFixedDepositList().forEach(fixedDeposit ->
                fixedDeposit.getAnnualBreakdownList().getAnnualBreakdownList().forEach(annualBreakdown ->
                        annualBreakdownTable.put(fixedDeposit.getFdNumber(), annualBreakdown.getFinancialYear(), annualBreakdown)));
        LOGGER.info("Guava table constructed: {}", annualBreakdownTable.toString());


        final StringBuilder table = new StringBuilder(HTML_TABLE_START);
        startRow(table);
        addUnboundedHeaderCells(table,
                "Number",
                "User",
                "FD Number (Key)",
                "Bank IFSC",
                "Deposit Amount",
                "Rate of interest",
                "Start -> End");
        annualBreakdownTable.columnKeySet().forEach(fy -> addUnboundedHeaderCells(table, fy));
        endRow(table);

        AtomicInteger counter = new AtomicInteger(0);

        Map<String, Double> annualInterestEarned = new LinkedHashMap<>();
        annualBreakdownTable.columnKeySet().forEach(fy ->
                annualInterestEarned.put(fy, annualBreakdownTable.column(fy).values().stream()
                        .mapToDouble(FixedDepositProto.AnnualBreakdown::getExpectedInterestGained).sum()));

        fixedDepositList.getFixedDepositList().forEach(fixedDeposit -> {
            try {
                //LOGGER.info(fixedDeposit.toString()); //getting too verbose
                startRow(table);
                addUnboundedRowCells(table,
                        counter.incrementAndGet(),
                        fixedDeposit.getUser(),
                        fixedDeposit.getFdNumber(),
                        fixedDeposit.getBankIFSC(),
                        fixedDeposit.getDepositAmount(),
                        fixedDeposit.getRateOfInterest(),
                        String.format("%s -> %s : %dm%dd", fixedDeposit.getStartDate(), fixedDeposit.getEndDate(), fixedDeposit.getMonths(), fixedDeposit.getDays()));

                annualBreakdownTable.columnKeySet().forEach(fy -> {
                    FixedDepositProto.AnnualBreakdown annualBreakdown = annualBreakdownTable.get(fixedDeposit.getFdNumber(), fy);
                    String data = EMPTY_STR;
                    if (annualBreakdown != null) {
                        data = String.format("%s I | %s A | %d d", formatDouble(annualBreakdown.getExpectedInterestGained()),
                                formatDouble(annualBreakdown.getExpectedAmountAccumulated()), annualBreakdown.getDaysInBetween());
                    }
                    addUnboundedRowCells(table, data);
                });
                endRow(table);
            } catch (Exception e) {
                LOGGER.error("Failed to convert '{}' to HTML. Skipping. ", fixedDeposit, e);
            }
        });

        //agg row
        startRow(table);
        addUnboundedRowCells(table,
                counter.incrementAndGet(),
                EMPTY_STR,
                EMPTY_STR,
                EMPTY_STR,
                fixedDepositList.getFixedDepositList().stream().mapToDouble(FixedDepositProto.FixedDeposit::getDepositAmount).sum(),
                EMPTY_STR,
                EMPTY_STR);
        annualInterestEarned.forEach((fy, interest) -> addUnboundedRowCells(table, formatDouble(interest)));
        endRow(table);
        table.append(HTML_TABLE_END);
        LOGGER.info("Rendering finished for {} fixed deposits", fixedDepositList.getFixedDepositCount());
        return table.toString();
    }
}
